package thkoeln.divekithelper.classDiagram;

import lombok.Getter;
import lombok.Setter;
import thkoeln.divekithelper.common.DivekitHelper;


import java.util.List;

/**
 * This class creates test-calls to the ClassDiagrammTest-Class and generating the corresponding output.
 */
public class DivekitHelperClassDiagram extends DivekitHelper {

    private ClassDiagramTest classDiagramTest;

    @Getter
    private String comment = null;

    @Setter
    private TestType test = TestType.NONE;

    public enum TestType { NONE, MISSING_CLASS, WRONG_CLASS, CLASS_WRONG_ATTRIBUTES, MISSING_RELATION, MISMATCH, WRONG_RELATION, ILLEGAL_ELEMENTS }

    /**
     * Initiate the class.
     * @param classDiagramTest a ClassDiagrammTest
     */
    public DivekitHelperClassDiagram( ClassDiagramTest classDiagramTest ){
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
            case MISSING_CLASS:
                return testMissingClass(message);
            case WRONG_CLASS:
                return testWrongClass(message);
            case MISSING_RELATION:
                return testMissingRelation(message);
            case WRONG_RELATION:
                return testWrongRelations(message);
            case MISMATCH:
                return testMismatch(message);
            case ILLEGAL_ELEMENTS:
                return testIllegalElements(message);
            case CLASS_WRONG_ATTRIBUTES:
                return testClassWrongAttributes(message);
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
     * Tests, whether the diagram contains wrong (unnecessary) classes.
     * @param message message that will be displayed, if the test fails
     * @return true if no wrong class is present, false otherwise
     */
    private boolean testWrongClass( String message ){
        List<String> wrongClasses =  classDiagramTest.getWrongClasses();

        if( wrongClasses.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_CLASS_", wrongClasses );

        return false;
    }

    /**
     * Tests, whether any classes in the user diagram do not have the same attributes as the solution classes.
     * @param message message that will be displayed, if the test fails
     * @return true if no user class has different attributes, false otherwise
     */
    private boolean testClassWrongAttributes( String message ){
        List<String> classesWithWrongAttributes = classDiagramTest.getClassesWithWrongAttributes();
        if ( classesWithWrongAttributes.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_CLASS_", classesWithWrongAttributes );

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
     * Tests, whether an existing relation is unnecessary or has wrong attributes.
     * @param message message that will be displayed, if the test fails
     * @return true if no relation is unnecessary or has wrong attributes, false otherwise
     */
    private boolean testWrongRelations( String message ){
        List<String> wrongRelations =  classDiagramTest.getWrongRelations();

        if( wrongRelations.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_RELATION_", wrongRelations );

        return false;
    }

    /**
     * Tests, whether the diagram and glossary have a mismatch.
     * @param message message that will be displayed, if the test fails
     * @return true if no mismatch is present, false otherwise
     */
    private boolean testMismatch( String message ){

        if( classDiagramTest.getGlossaryClassNames() == null ){
            comment = "Cannot load Glossary-Table";
            return false;
        }

        List<String> mismatch =  classDiagramTest.getMismatch();

        if( mismatch.size() < 1 )
            return true;

        comment = replaceMessageElement( message, "_CLASS_", mismatch );

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
     * Sets the glossary information that is being used in a mismatch test.
     * @param glossaryPath  path to the glossary
     * @param columnOfClassNames name of the column, which contains the Class names
     */
    public void setGlossary( String glossaryPath, String columnOfClassNames ){
        classDiagramTest.setGlossary( glossaryPath, columnOfClassNames );
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

        if( !classDiagramTest.isTestValid() ){
            setTestName( testName );
            setTestCategory( testCategory );
            postResult("Test invalid, cannot read a resource.", false);
            return false;
        }

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
