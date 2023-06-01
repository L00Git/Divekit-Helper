package thkoeln.divekithelper.common;


import lombok.NoArgsConstructor;
import lombok.Setter;
import thkoeln.divekithelper.code.SureFireHelper;
import thkoeln.divekithelper.common.testlevel.TestLevel;

import java.util.HashMap;

/**
 * A generic DivekHelperClass implementing common methods.
 */
@NoArgsConstructor
public class DivekitHelper {
    private HashMap<Integer, String> messages = new HashMap<>();


    protected SureFireHelper xmlHelper = new SureFireHelper();

    private JSONHelper jsonHelper = new JSONHelper();

    @Setter
    private String testName;
    @Setter
    private String testCategory;
    /**
     * Save a message.
     * @param testLevel the test level, at which this message will be displayed
     * @param message the message
     */
    public void addMessage( int testLevel, String message ){
        messages.put( testLevel, message );
    }


    /**
     * Create a test result.
     * @param message the message that will be displayed, in case the test failed
     * @param status the status of the test
     */
    protected void postResult( String message, boolean status ){
        if ( message != null && !status ) {
            jsonHelper.createFailingTest(testName, testCategory, message);
        } else if (status) {
            jsonHelper.createPassingTest( testName, testCategory );
        }
    }

    /**
     * Get the message corresponding to the current TestLevel.
     * @return the message
     */
    protected String getMessage(){


        int testlevel = TestLevel.getTestLevel( testName, testCategory );
        String finalMessage = null;

        for( ; testlevel > 0 && finalMessage == null; testlevel-- ){
            finalMessage = messages.get( testlevel );
        }

        if( finalMessage == null ){
            System.out.println("No message could be generated, at least one message of level 1 should always be set.");
            System.exit(1);
        }

        return finalMessage;
    }


}
