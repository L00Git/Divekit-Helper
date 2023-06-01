package thkoeln.divekithelper.common;




import com.google.gson.*;



import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * A Class generating the test output in JSON-Format.
 */
public class JSONHelper {

    private final String REPORT_OUTPUT_PATH = "DivekitHelperResult.custom-test.json";

    private JsonArray testCases = new JsonArray();

    /**
     * Initialise the class and try loading the content of the output file.
     */
    public  JSONHelper (){
        try ( FileReader fileReader = new FileReader(REPORT_OUTPUT_PATH) ){
            testCases = (JsonArray) JsonParser.parseReader( fileReader );
        } catch (IOException | ClassCastException e) {
            System.out.println( "Couldn't`t load output json file." );
        }

    }

    /**
     * Create a passing test and save it.
     * @param name the test name
     * @param category the test category
     */
    public void createPassingTest( String name, String category ){
        JsonObject testcase = new JsonObject();
        testcase.addProperty("name", name);
        testcase.addProperty("category", category);
        testcase.addProperty("status", "success");
        testcase.addProperty("content", "");

        remove( name, category );
        testCases.add( testcase );
        saveToFile();
    }

    /**
     * Create a failing test.
     * @param name the test name
     * @param category the test category
     * @param content the content the test will display
     */
    public void createFailingTest( String name, String category, String content ){
        JsonObject testcase = new JsonObject();
        testcase.addProperty("name", name);
        testcase.addProperty("category", category);
        testcase.addProperty("status", "failure");
        testcase.addProperty("content", content);

        remove( name, category );
        testCases.add( testcase );
        saveToFile();
    }

    /**
     * Remove a Test
     * @param name the test name
     * @param category the test category
     */
    private void remove(String name, String category){
        for( JsonElement element: testCases ){
            JsonObject obj = element.getAsJsonObject();
            if( obj.get( "name" ) == null || obj.get( "category" ) == null )
                return;
            if( obj.get( "name" ).getAsString().equals( name ) && obj.get( "category" ).getAsString().equals( category )  ) {
                testCases.remove( element );
                return;
            }
        }
    }

    /**
     * save the modified JSON-Version to the file.
     */
    private void saveToFile(){
        Gson gson = new Gson();
        try (FileWriter fileWriter = new FileWriter(REPORT_OUTPUT_PATH)){
            gson.toJson( testCases, fileWriter );
        } catch (IOException e) {
            System.out.println( "Couldn't`t save output to json file." );
        }
    }

}
