package thkoeln.divekithelper.code;





import thkoeln.divekithelper.code.interfaces.*;

import java.lang.annotation.Annotation;

/**
 * Implements the DivekitHelper DSl for CodeTests.
 */
public class DivekitHelperCodeBuilder implements DivekitHelperCodeBuilderPostClasses,
                                                    DivekitHelperCodeBuilderPostWithName,
                                                    DivekitHelperCodeBuilderPostWithAnnotation,
                                                    DivekitHelperCodeBuilderPostPackage,
                                                    DivekitHelperCodeBuilderPostNoCircularDependencies,
                                                    DivekitHelperCodeBuilderPostImmutable,
                                                    DivekitHelperCodeBuilderPostShouldHave,
                                                    DivekitHelperCodeBuilderPostShouldNotHave,
                                                    DivekitHelperCodeBuilderPostAnnotation,
                                                    DivekitHelperCodeBuilderPostOtherClass,
                                                    DivekitHelperCodeBuilderPostStacktrace,
                                                    DivekitHelperCodeBuilderPostMessage{

    DivekitHelperCode codeTest;

    /**
     * Initiate the Builder and set the path the package.
     * @param packagePath path to the package, which contains all classes that should be tested
     */
    public DivekitHelperCodeBuilder( String packagePath ){
        codeTest = new DivekitHelperCode( packagePath );
    }


    /**
     * Start a CodeTest by creating a new Builder.
     * @param packagePath path to the package, which contains all classes that should be tested
     * @return a new Builder object
     */
    public static DivekitHelperCodeBuilderPostClasses classes( String packagePath ){
        return new DivekitHelperCodeBuilder( packagePath );
    }

    /**
     * Test all classes, with a given name.
     * @param className the name, that classes that are being tested have
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder withName( String className ){
        codeTest.setClassesToTest( codeTest.getClassByName( className ) );
        return this;
    }

    /**
     * Test all classes, with a given annotation.
     * @param annotation the annotation, that classes that are being tested have
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder withAnnotation( Class<? extends Annotation> annotation ){
        codeTest.setClassesToTest( codeTest.getClassByAnnotation( annotation ) );
        return this;
    }

    /**
     * Tests, whether the classes are in a package, which ends with a given name.
     * @param packageName the name corresponds to the ending of the package structure, that the classes should be in
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder inPackage( String packageName ){
        codeTest.setPackageName( packageName );
        codeTest.setTest( DivekitHelperCode.TestType.DIRECTORY );
        return this;
    }

    /**
     * Tests, whether any Class has a circular dependency.
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder noCircularDependencies(){
        codeTest.setTest( DivekitHelperCode.TestType.CIRCULARDEPENDENCY);
        return this;
    }

    /**
     * Tests, whether all classes are immutable.
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder immutable(){
        codeTest.setTest( DivekitHelperCode.TestType.IMMUTABLE );
        return this;
    }

    /**
     * Sets the condition for the following test.
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder shouldHave(){
        codeTest.setCondition( DivekitHelperCode.ConditionType.HAVE );
        return this;
    }

    /**
     * Sets the condition for the following test.
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder shouldNotHave(){
        codeTest.setCondition( DivekitHelperCode.ConditionType.NOT_HAVE );
        return this;
    }

    /**
     * Tests, whether all classes have the given annotation if the condition is "shouldHave"
     * and whether no class has the annotation if  the condition is "shouldNotHave".
     * @param annotation the annotation all classes should have
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder annotation( Class<? extends Annotation> annotation ){
        codeTest.setTest( DivekitHelperCode.TestType.ANNOTATION );
        codeTest.setInnerAnnotation( annotation );
        return this;
    }

    /**
     * Tests, whether all classes have another class with the given name if the condition is "shouldHave"
     * and whether no class has another class with the given name if the condition is "shouldNotHave".
     * @param className the name corresponding to the other class a tested class should have or not have
     *                 (can contain a placeholder '_CLASS_' that will be replaced with the name of the currently tested class)
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder otherClass( String className ){
        codeTest.setTest( DivekitHelperCode.TestType.OTHER_CLASS );
        codeTest.setOtherClassName( className );
        return this;
    }

    /**
     * Tests, whether any surefire error stacktrace contains a class, which is part of the specified package.
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder stackTrace(){
        codeTest.setTest( DivekitHelperCode.TestType.STACKTRACE );
        return this;
    }

    /**
     * Set the message, that will be displayed if the test detects a violation.
     * @param testLevel the level that has to be reached, for the message to be displayed
     * @param message the message that will be displayed
     * @return this Builder object
     */
    public DivekitHelperCodeBuilder message( int testLevel, String message ){
        codeTest.addMessage( testLevel, message );
        return this;
    }

    /**
     * Trigger the test that has been specified by the previous method calls.
     * @param testName the name this test will display
     * @param testCategory the name of the category this test falls under
     * @return true if all tests pass and false if any test fails
     */
    public boolean test( String testName, String testCategory ){
        return codeTest.buildResult( testName, testCategory );
    }

}
