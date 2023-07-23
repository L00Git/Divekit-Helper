package thkoeln.divekithelper.common.testlevel;

import com.google.gson.Gson;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Event;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates and retrieves the testLevel of all Tests, specified by the testLevelConfiguration-File.
 */
public class TestLevel {

    private static final String TEST_LEVEL_OUTPUT_PATH = "testLevel.json";

    /**
     * Get all the necessary information to generate the testLevel and start the generation.
     * @param pathConfig path to the testLevelConfiguration
     */
    public static void generateTestLevel( String pathConfig ){
        HashMap<String, Integer> testLevel = new HashMap<>();

        String projectAccessToken = System.getenv("CODE_REPO_TOKEN");

        String codeRepoUrl = System.getenv("CODE_REPO_URL");


        if( projectAccessToken == null ){
            System.out.println("Cannot find  'PROJECT_ACCESS_TOKEN' environmentVariable.");
            return;
        }
        if( codeRepoUrl == null ){
            System.out.println("Cannot find  'CODE_REPO_URL' environmentVariable.");
            return;
        }
        String projectPath;
        try {
            projectPath = new URL(codeRepoUrl).getPath();
        } catch ( MalformedURLException e ) {
            System.out.println("'CODE_REPO_URL' does not match URL-Format.");
            return;
        }
        if( projectPath == null ){
            System.out.println("CODE_REPO_URL' does not contain a path to the code repo.");
            return;
        }else if( projectPath.startsWith("/") ){
            projectPath = projectPath.substring(1);
        }

        String hostUrl = codeRepoUrl.split( projectPath )[0];


        TestLevelConfig testLevelConfig = null;

        Gson gson = new Gson();

        try (FileReader fileReader = new FileReader( pathConfig ) ){
            testLevelConfig = gson.fromJson( fileReader, TestLevelConfig.class );
        } catch (IOException e) {
            System.out.println( "Couldn't load testLevel Config." );
        }

        if( !testConfig( testLevelConfig ) ){
            return;
        }
        try ( GitLabApi gitLabApi = new GitLabApi(hostUrl, projectAccessToken) ) {

            gitLabApi.setDefaultPerPage(100);


            List<Event> pushEvents = gitLabApi.getEventsApi().getProjectEvents( projectPath, Constants.ActionType.PUSHED, null, null, null, null );


            assert testLevelConfig != null;
            for ( Test test: testLevelConfig.getTests() ){
                List<Commit> commits = gitLabApi.getCommitsApi().getCommits( projectPath, null, test.getPath() );
                int delay = test.getDelay() > 0 ? test.getDelay() : testLevelConfig.getDefaultDelay();
                int maxLevel = test.getMaxLevel() > 0 ? test.getMaxLevel() : testLevelConfig.getDefaultMaxLevel();

                testLevel.put(test.getTestName(), processCommits( commits, pushEvents, delay, maxLevel ) );
            }

        }catch (GitLabApiException exception) {
            System.out.println( "Error making GitLab-API call." );
            exception.printStackTrace();
        }

        System.out.println( "Generated Test Level: " + testLevel );

        try (FileWriter fileWriter = new FileWriter( TEST_LEVEL_OUTPUT_PATH ) ){
            gson.toJson( testLevel, fileWriter );
        } catch (IOException e) {
            System.out.println( "Couldn't`t save test-level to json file." );
        }
    }

    /**
     * Tests, whether all the information of the testLevelConfiguration is valid.
     * @param testLevelConfig the testLevelConfiguration
     * @return true if the configuration is valid, false if not
     */
    private static boolean testConfig( TestLevelConfig testLevelConfig ){
        if( testLevelConfig == null ){
            System.out.println( "TestLevelConfig is null." );
            return false;
        }else if( testLevelConfig.getDefaultDelay() <= 0 ){
            System.out.println( "DefaultDelay is not set to a positive number." );
            return false;
        }else if( testLevelConfig.getDefaultMaxLevel() <= 0 ){
            System.out.println( "DefaultMaxLevel is not set to a positive number." );
            return false;
        }
        for( Test test: testLevelConfig.getTests() ){
            if( test.getTestName() == null ){
                System.out.println( "A test-name is not set." );
                return false;
            } else if ( test.getPath() == null ) {
                System.out.println( "A test-path is not set." );
                return false;
            } else if ( test.getDelay() < 0 ) {
                System.out.println( "A test-delay is set to a negative number." );
                return false;
            } else if ( test.getMaxLevel() < 0 ) {
                System.out.println( "A test-maxLevel is set to a negative number." );
                return false;
            }
        }
        return true;
    }

    /**
     * Generate the testLevel, based on the time of the commits, relative to the time of the pushEvents and the delay between each commit.
     * @param commits all commits that modified a file pertaining to a test
     * @param pushEvents all pushEvents
     * @param delay the minimum delay between two commits, before the testLevel is increased
     * @param maxLevel the maximum level of this test
     * @return the testLevel
     */
    private static int processCommits( List<Commit> commits, List<Event> pushEvents, int delay, int maxLevel ){
        int testLevel = 0;
        int currentPushDate = 0;
        Instant lastLevelIncrementDate = null;

        List<Instant> pushDates = pushEvents.stream().map( Event::getCreatedAt ).map( Date::toInstant ).collect( Collectors.toList() );

        List<Instant> commitDates = commits.stream().map( Commit::getAuthoredDate ).map( Date::toInstant ).collect( Collectors.toList() );
        for ( int i = 0; i < commitDates.size() && testLevel < maxLevel; i++ ){

            while ( pushDates.size() > ( currentPushDate + 1 ) && pushDates.get( currentPushDate + 1 ).isAfter( commitDates.get( i ) ) ){
                currentPushDate++;
            }

            if( pushDates.size() <= ( currentPushDate + 1 ) ){
                return testLevel;
            }
            long commitDelay;

            if(  lastLevelIncrementDate == null ){
                commitDelay = Long.MAX_VALUE;
            }else {
                commitDelay = Math.abs( Duration.between( lastLevelIncrementDate, commitDates.get( i ) ).toMinutes() );
            }

           if( commitDelay >= delay && isInstantBetween( pushDates.get( currentPushDate ), pushDates.get( currentPushDate + 1 ), commitDates.get( i ) ) ){
                testLevel++;
                lastLevelIncrementDate = commitDates.get( i );
                currentPushDate++;
            }
        }

        return testLevel;
    }

    /**
     * Tests, whether a given Insatnt is between two other Instants. The Instant that is in between can be equal to one or both of the delimitations.
     * @param newInstant newer delimitation
     * @param oldInstant older delimitation
     * @param commitInstant possible Instant that is between the two delimitations
     * @return true if the  given Instant is between the two other given Instants, false if not
     */
    private static boolean isInstantBetween( Instant newInstant, Instant oldInstant, Instant commitInstant ){
        return ( newInstant.equals( commitInstant ) || newInstant.isAfter( commitInstant ) ) && ( oldInstant.equals( commitInstant ) || oldInstant.isBefore( commitInstant ) );
    }

    /**
     * Get the testLevel of a test, by its name or category. Prioritises the testName, by first searching the testLevelConfiguration-testNames for it.
     * If no match was found, the configuration-testNames are searched for a match of the testCategory.
     * @param testName name of the Test
     * @param testCategory name of the Test Category
     * @return the TestLevel
     */
    public static int getTestLevel( String testName, String testCategory ){
        HashMap<String, Double> testLevel = new HashMap<>();
        Gson gson = new Gson();

        try (FileReader fileReader = new FileReader( TEST_LEVEL_OUTPUT_PATH ) ){
            testLevel =  gson.fromJson( fileReader, testLevel.getClass() );
        } catch (IOException | ClassCastException e) {
            System.out.println( "Couldn't`t load testLevel file, progressing with testLevel 0." );
            return 0;
        }

        if( testLevel == null ){
            System.out.println( "Couldn't load testLevel-File, proceeding with testLevel 0." );
            return 0;
        } else if ( testLevel.containsKey( testName ) ) {
            return testLevel.get(testName ).intValue();
        } else if ( testLevel.containsKey( testCategory ) ) {
            return testLevel.get(testCategory ).intValue();
        }else {
            System.out.println( "No testLevel found for testName: " +testName + " or testCategory: " + testCategory + " proceeding with testLevel 0."  );
            return 0;
        }
    }
}
