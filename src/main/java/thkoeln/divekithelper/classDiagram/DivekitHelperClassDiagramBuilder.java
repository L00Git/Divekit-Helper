package thkoeln.divekithelper.classDiagram;



import thkoeln.divekithelper.classDiagram.interfaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the DivekitHelper DSl for ClassDiagrammTests.
 */
public class DivekitHelperClassDiagramBuilder implements DivekitHelperClassDiagramBuilderPostClassDiagram,
                                                         DivekitHelperClassDiagramBuilderPostMissingClasses,
                                                         DivekitHelperClassDiagramBuilderPostWrongClasses,
                                                         DivekitHelperClassDiagramBuilderPostClassWrongAttributes,
                                                         DivekitHelperClassDiagramBuilderPostMissingRelations,
                                                         DivekitHelperClassDiagramBuilderPostMismatch,
                                                         DivekitHelperClassDiagramBuilderPostIllegalElements,
                                                         DivekitHelperClassDiagramBuilderPostWrongRelations,
                                                         DivekitHelperClassDiagramBuilderInterface {

    private List<DivekitHelperClassDiagram> classDiagramTests = new ArrayList<>();


    private ClassDiagramTest classDiagramTest;

    /**
     * Initiate the Builder and set the ClassDiagramTest
     * @param classDiagramTest a ClassDiagramTest
     */
    public DivekitHelperClassDiagramBuilder( ClassDiagramTest classDiagramTest ){
        this.classDiagramTest = classDiagramTest;
    }

    /**
     * Start a ClassDiagramTest by creating a new Builder.
     * @param classDiagramTest a ClassDiagramTest that implements the ClassDiagramTestInterface
     * @return a new Builder object
     */
    public static DivekitHelperClassDiagramBuilderPostClassDiagram classDiagram( ClassDiagramTest classDiagramTest ){
        return new DivekitHelperClassDiagramBuilder ( classDiagramTest );
    }

    /**
     * Start a missing classes test, by creating a new DivekitHelperClassDiagram object and setting its test type.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder missingClasses(){
        classDiagramTests.add( new DivekitHelperClassDiagram( classDiagramTest ) );
        getCurrentClassDiagramTest().setTest( DivekitHelperClassDiagram.TestType.MISSING_CLASS);
        return this;
    }

    /**
     * Start a wrong classes test, by creating a new DivekitHelperClassDiagram object and setting its test type.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder wrongClasses(){
        classDiagramTests.add( new DivekitHelperClassDiagram( classDiagramTest ) );
        getCurrentClassDiagramTest().setTest( DivekitHelperClassDiagram.TestType.WRONG_CLASS);
        return this;
    }

    /**
     * Start a class has wrong attributes test, by creating a new DivekitHelperClassDiagram object and setting its test type.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder classWrongAttributes(){
        classDiagramTests.add( new DivekitHelperClassDiagram( classDiagramTest ) );
        getCurrentClassDiagramTest().setTest( DivekitHelperClassDiagram.TestType.CLASS_WRONG_ATTRIBUTES);
        return this;
    }

    /**
     * Start a missing relations test, by creating a new DivekitHelperClassDiagram object and setting its test type.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder missingRelations(){
        classDiagramTests.add( new DivekitHelperClassDiagram( classDiagramTest ) );
        getCurrentClassDiagramTest().setTest( DivekitHelperClassDiagram.TestType.MISSING_RELATION);
        return this;
    }

    /**
     * Start a mismatch test, by creating a new DivekitHelperClassDiagram object and setting its test type and glossary information.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder mismatch( String glossaryPath, String columnOfClassNames ){
        classDiagramTests.add( new DivekitHelperClassDiagram( classDiagramTest ) );
        getCurrentClassDiagramTest().setTest( DivekitHelperClassDiagram.TestType.MISMATCH );
        getCurrentClassDiagramTest().setGlossary( glossaryPath, columnOfClassNames );
        return this;
    }

    /**
     * Start a wrong relations test, by creating a new DivekitHelperClassDiagram object and setting its test type.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder wrongRelations(){
        classDiagramTests.add( new DivekitHelperClassDiagram( classDiagramTest ) );
        getCurrentClassDiagramTest().setTest( DivekitHelperClassDiagram.TestType.WRONG_RELATION);
        return this;
    }

    /**
     * Start a illegal elements test, by creating a new DivekitHelperClassDiagram object and setting its test type.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder illegalElements(){
        classDiagramTests.add( new DivekitHelperClassDiagram( classDiagramTest ) );
        getCurrentClassDiagramTest().setTest( DivekitHelperClassDiagram.TestType.ILLEGAL_ELEMENTS);
        return this;
    }

    /**
     * Set the message for the current DivekitHelperClassDiagram.
     * @param testLevel the test level, at which the message will be displayed
     * @param message the message that will be displayed if the test fails and the corresponding level is reached
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder message( int testLevel, String message ){
        getCurrentClassDiagramTest().addMessage( testLevel, message );
        return this;
    }

    /**
     * Allows the combination of multiple tests into one displayed test.
     * @return this Builder object
     */
    public DivekitHelperClassDiagramBuilder combine(){
        return this;
    }

    /**
     * Triggers all tests and combines their results.
     * @param testName the name this test will display
     * @param testCategory the name of the category this test falls under
     * @return true if no test fails and false otherwise
     */
    public boolean test( String testName, String testCategory ){
        return classDiagramTests.get(0).combineResults( classDiagramTests, testName, testCategory );
    }

    /**
     * Get the latest DivekitHelperClassDiagram.
     * @return the DivekitHelperClassDiagram that was created last or null of no DivekitHelperClassDiagram was created
     */
    private DivekitHelperClassDiagram getCurrentClassDiagramTest(){
        if(classDiagramTests.size() < 1)
            return null;
        return classDiagramTests.get( classDiagramTests.size()-1 );
    }
}
