package thkoeln.divekithelper.classDiagram;

import lombok.Getter;
import lombok.Setter;
import thkoeln.divekithelper.common.DivekitHelper;


import java.util.List;

/**
 * This class implements the tests associated with ClassDiagramms,
 * by making calls to the ClassDiagrammTest-Class and generating the corresponding output.
 */
public class DivekitHelperClassDiagram extends DivekitHelper {

    private ClassDiagramTestInterface classDiagramTest;

    @Getter
    private String comment = null;

    @Setter
    private TestType test = TestType.NONE;

    public enum TestType { NONE, MISSINGCLASS, MISSINGRELATION, MISMATCH, WRONGRELATION, ILLEGALELEMENTS }

    /**
     * Initiate the class.
     * @param classDiagramTest a ClassDiagrammTest-Class that implements the ClassDiagrammTestInterface
     */
    public DivekitHelperClassDiagram( ClassDiagramTestInterface classDiagramTest ){
        this.classDiagramTest = classDiagramTest;
    }

    /**
     * Trigger the test by calling the correct test-case.
     * @param testName the name this test will display
     * @param testCategory the name of the category this test falls under
     * @return true if the tests passes and false otherwise
     */
    public boolean buildResult( String testName, String testCategory ){
        setTestName( testName );
        setTestCategory( testCategory );
        String message = getMessage();

        switch (test) {
            case NONE:
                return false;
            case MISSINGCLASS:
                return testMissingClass(message);
            case MISSINGRELATION:
                return testMissingRelation(message);
            case MISMATCH:
                return testMismatch(message);
            case WRONGRELATION:
                return testWrongRelations(message);
            case ILLEGALELEMENTS:
                return testIllegalElements(message);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Tests, whether the diagram is missing a class.
     * @param message message that will be displayed, if the test fails
     * @return true if no class is missing, false otherwise
     */
    private boolean testMissingClass( String message ){
        List<String> missingClasses =  classDiagramTest.getMissingClasses();

        if( missingClasses.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_CLASS_", missingClasses );

        return false;
    }

    /**
     * Tests, whether the diagram is missing a relation.
     * @param message message that will be displayed, if the test fails
     * @return true if no relation is missing, false otherwise
     */
    private boolean testMissingRelation( String message ){
        List<String> missingRelations =  classDiagramTest.getMissingRelations();

        if( missingRelations.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_RELATION_", missingRelations );

        return false;
    }

    /**
     * Tests, whether the diagram and glossary have a mismatch.
     * @param message message that will be displayed, if the test fails
     * @return true if no mismatch is present, false otherwise
     */
    private boolean testMismatch( String message ){
        List<String> mismatch =  classDiagramTest.getMismatch();

        if( mismatch.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_MISMATCH_", mismatch );

        return false;
    }

    /**
     * Tests, whether an existing relation is unnecessary.
     * @param message message that will be displayed, if the test fails
     * @return true if no relation is unnecessary, false otherwise
     */
    private boolean testWrongRelations( String message ){
        List<String> wrongRelations =  classDiagramTest.getWrongRelations();

        if( wrongRelations.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_RELATION_", wrongRelations );

        return false;
    }

    /**
     * Tests, whether the diagram contains any illegal elements.
     * @param message message that will be displayed, if the test fails
     * @return true if no illegal elements are present, false otherwise
     */
    private boolean testIllegalElements( String message ){
        List<String> illegalElements = classDiagramTest.getIllegalElements();
        if ( illegalElements.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_ELEMENT_", illegalElements );

        return false;
    }

    /**
     * Replace a specific element in the message with a list of other elements.
     * @param message the message that contains an element that should be replaced
     * @param toReplace the element that will be replaced
     * @param elements the elements that are being added to the message
     * @return a String containing the replaced elements
     */
    private String replaceMessageElement( String message, String toReplace , List<String> elements ){
        return message.replace(toReplace,  "\"" + String.join( "\", \"", elements )  + "\"" );
    }

    /**
     * Triggers the tests for a given List of DivekitHelperClassDiagramms and combines their results.
     * @param diagramTests a List of DivekitHelperClassDiagramms that are being combined
     * @param testName the name this combined test will display
     * @param testCategory the name of the category this combined test falls under
     * @return true if no test fails and false otherwise
     */
    public boolean combineResults( List<DivekitHelperClassDiagram> diagramTests, String testName, String testCategory ){

        boolean result = true;

        String message = "";

        for( DivekitHelperClassDiagram test: diagramTests ){
            if ( !test.buildResult( testName, testCategory ) )
                result = false;
            message += test.getComment() != null ? test.getComment() + "<br>" : "";
        }


        if( !result )
            postResult( message, false );
        else
            postResult("", true);

        return result;
    }


}
