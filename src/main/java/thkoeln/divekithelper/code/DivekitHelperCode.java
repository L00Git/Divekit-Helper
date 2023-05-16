package thkoeln.divekithelper.code;

import lombok.Setter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import thkoeln.divekithelper.common.DivekitHelper;


import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the tests associated with Java-Code,
 * by using Reflection.
 */
public class DivekitHelperCode extends DivekitHelper {

    private String pathToPackage;

    @Setter
    private Set< Class<?> > classesToTest = null;

    @Setter
    private Class<? extends Annotation>  innerAnnotation;

    @Setter
    private String otherClassName;

    @Setter
    private String packageName;


    public enum ConditionType { NONE, HAVE, NOT_HAVE}

    @Setter
    private ConditionType condition = ConditionType.NONE;

    public enum TestType { NONE, ANNOTATION, OTHER_CLASS, DIRECTORY, IMMUTABLE , STACKTRACE, CIRCULAR_DEPENDENCY}

    @Setter
    private TestType test = TestType.NONE;


    /**
     * Initiate the class.
     * @param packagePath Path to the package that contains the code which should be tested
     */
    public DivekitHelperCode( String packagePath){
        pathToPackage = packagePath;
    }


    /**
     * Get all classes with a given name from the set package.
     * @param classname the name all classes are filtered by
     * @return a Set of all classes with the given name, can be empty
     */
    public Set<Class<?>> getClassByName( String classname ){
        Set< Class<?> > classes = getAllClasses().stream().filter(aClass -> aClass.getSimpleName().equals( classname ) ).collect(Collectors.toSet());

        return classes;
    }

    /**
     * Get all classes with a given annotation from the set package.
     * @param annotation the annotation the classes are filtered by
     * @return a Set of all classes with the given annotation, can be empty
     */
    public Set<Class<?>> getClassByAnnotation( Class<? extends Annotation>  annotation ){
        Set< Class<?> > classes = new HashSet<>();

        for( Class<?> aClass: getAllClasses() ){
            if( aClass.isAnnotationPresent(annotation ) ){
                classes.add( aClass );
            }
        }

        return classes;
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
            case ANNOTATION:
                return testAnnotation(message);
            case OTHER_CLASS:
                return testOtherClass(message);
            case DIRECTORY:
                return testPackage(message);
            case IMMUTABLE:
                return testImmutable(message);
            case STACKTRACE:
                return testAllStackTrace(message);
            case CIRCULAR_DEPENDENCY:
                return testCircularDependency(message);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Differentiate the annotation test: all classes should have an annotation / no class should have the annotation.
     * @param message the message that will be displayed if the test fails
     * @return true if the test passes and false otherwise
     */
    private boolean testAnnotation( String message ){
        switch (condition) {
            case HAVE:
                return isAnnotationPresentAll(message, innerAnnotation);
            case NOT_HAVE:
                return !isAnnotationPresentAny(message, innerAnnotation);
            default:
                return false;
        }
    }

    /**
     * Tests, whether all classes have a given annotation.
     * @param message the message that will be displayed if any class does not have the give annotation
     * @param annotation the annotation all classes should have
     * @return true if all classes have the annotation and false otherwise
     */
    private boolean isAnnotationPresentAll( String message, Class<? extends Annotation> annotation){
        boolean annotationPresent = true;

        List<String> foundClasses =  new ArrayList<>();

        for (Class<?> aClass : classesToTest) {
            if( !aClass.isAnnotationPresent( annotation ) && !Arrays.stream( aClass.getDeclaredFields() ).anyMatch( field -> field.isAnnotationPresent(annotation ) ) ){
                annotationPresent = false;
                foundClasses.add( aClass.getSimpleName() );
            }
        }

        message = replaceMessageClass( message,  foundClasses );

        if( !annotationPresent )
            postResult( message, false );
        else
            postResult("", true);

        return annotationPresent;
    }

    /**
     * Tests, whether any class has the given annotation.
     * @param message the message that will be displayed if any class has the given annotation
     * @param annotation the annotation no class should have
     * @return true if any class has the given annotation and false otherwise
     */
    private boolean isAnnotationPresentAny ( String message, Class<? extends Annotation> annotation){
        boolean annotationPresent = false;

        List<String> foundClasses =  new ArrayList<>();


        for (Class<?> aClass : classesToTest) {
            if( aClass.isAnnotationPresent( annotation ) || Arrays.stream( aClass.getDeclaredFields()).anyMatch( field -> field.isAnnotationPresent(annotation ) ) ){
                annotationPresent = true;
                foundClasses.add( aClass.getSimpleName() );
            }
        }

        message = replaceMessageClass( message,  foundClasses );

        if( annotationPresent )
            postResult( message, false );
        else
            postResult("", true);


        return annotationPresent;
    }

    /**
     * Differentiate the other class test: all classes should have another class with a given name / no class should have another class with a given name .
     * @param message the message that will be displayed if the test fails
     * @return true if the test passes and false otherwise
     */
    private boolean testOtherClass( String message ){
        switch (condition) {
            case HAVE:
                return isClassPresentAll(message, otherClassName);
            case NOT_HAVE:
                return !isClassPresentAny(message, otherClassName);
            default:
                return false;
        }
    }

    /**
     * Tests, whether another class with the given name exists for all classes.
     * @param message the message that will be displayed if any class does not have another class
     * @param otherClassName the name of the other class, each class should have (can contain _CLASS_ as a placeholder, which is replaced with the currently tested class)
     * @return true if all classes have the another class
     */
    private boolean isClassPresentAll ( String message, String otherClassName ){
        boolean otherClassPresent = true;

        List<String> foundClasses =  new ArrayList<>();

        for (Class<?> aClass : classesToTest){
            if( getClassByName( otherClassName.replace("_CLASS_", aClass.getSimpleName() ) ).size() < 1 ) {
                otherClassPresent = false;
                foundClasses.add( aClass.getSimpleName() );
            }
        }

        message = replaceMessageClass( message,  foundClasses );

        if( !otherClassPresent )
            postResult( message, false );
        else
            postResult("", true);

        return  otherClassPresent;
    }

    /**
     * Tests, whether any class has another class with a given name.
     * @param message the message that will be displayed if any class has another class
     * @param otherClassName the name of the other class, no class should have (can contain _CLASS_ as a placeholder, which is replaced with the currently tested class)
     * @return true if any class has the another class and false otherwise
     */
    private boolean isClassPresentAny ( String message, String otherClassName ){
        boolean otherClassPresent = false;

        List<String> foundClasses =  new ArrayList<>();

        for (Class<?> aClass : classesToTest){
            if( getClassByName( otherClassName.replace("_CLASS_", aClass.getSimpleName() ) ).size() > 0 ){
                otherClassPresent = true;
                foundClasses.add( aClass.getSimpleName() );
            }
        }

        message = replaceMessageClass( message,  foundClasses );

        if( otherClassPresent )
            postResult( message, false );
        else
            postResult("", true);

        return  otherClassPresent;
    }

    /**
     * Tests, whether any stacktrace, triggered by an error in the surefireReports, contains a class, which is part of the specified package.
     * @param message the message that will be displayed if any occurrence of a local class is found in an error stacktrace
     * @return true if no occurrence is found and false otherwise
     */
    private boolean testAllStackTrace( String message ){
        List<File> surefireReports = xmlHelper.getAllSurefireFiles();

        List<String[]> stackTraces =  new ArrayList<>();

        surefireReports.forEach( report -> stackTraces.addAll( xmlHelper.getStackTracesFromXML( report ) ) );

        List<String> result = new ArrayList<>();


        for (String[] stackTrace: stackTraces){
            String processedMessage = testStackTrace( stackTrace[0], stackTrace[1] , message );
            if( processedMessage != null)
                result.add( processedMessage );
        }

        if(result.size() > 0) {
            postResult( String.join( "<br><br>", result ), false);
            return false;
        }

        postResult( "", true);
        return  true;
    }

    /**
     * Tests, whether a given stacktrace contains a path to the local package and a class, which is in said package.
     * @param stackTrace the stacktrace that is being tested
     * @param testName the name of the test, the stacktrace if triggered by
     * @param message the message, that should be displayed if a local class occurrence is found
     * @return a String with replaced placeholders if the stacktrace contained a local class and null if no occurrence was found
     */
    private String testStackTrace( String stackTrace, String testName, String message ){
        List<String> allPackageClassNames = getAllClasses().stream().map( Class::getSimpleName ).collect(Collectors.toList());

        String stackTraceLine =  stackTrace
                .lines()
                .filter( line -> line.contains(pathToPackage) && Arrays.stream(line.split("\\.")).anyMatch( allPackageClassNames::contains )  )
                .findFirst()
                .orElse( null );

        if( stackTraceLine == null ) {
            postResult("", true);
            return null;
        }

        return message.replace("_LINE_", "<code>"+stackTraceLine+"</code>" ).replace("_TEST_", testName );
    }

    /**
     * Tests, whether the classes are in a package, that ends with a specific name.
     * @param message the message that will be displayed if any package is not in the set package
     * @return true if all classes are in the specific package false otherwise
     */
    private boolean testPackage( String message ){

        boolean inPackage = true;

        List<String> foundClasses =  new ArrayList<>();

        for ( Class<?> aClass : classesToTest ) {
            if( !aClass.getPackageName().endsWith( packageName ) ) {
                inPackage = false;
                foundClasses.add( aClass.getSimpleName() );
            }
        }

        message = replaceMessageClass( message,  foundClasses );

        if( !inPackage )
            postResult( message, false );
        else
            postResult("", true);

        return inPackage;
    }

    /**
     * Tests, whether any class has a circular dependency.
     * @param message the message that will be displayed if any circular dependency is present
     * @return true if no circular dependency is present and false otherwise
     */
    private boolean testCircularDependency(String message ){
        Set< Class<?> > classes;
        classes = classesToTest != null ? classesToTest : getAllClasses();

        boolean noCircularDependency = true;

        List<String> foundClasses =  new ArrayList<>();

        for( Class<?> aClass: classes ){
            for (Field field: aClass.getDeclaredFields() ){
                if( Arrays.stream( field.getType().getDeclaredFields()).anyMatch( field1 -> field1.getType().equals( aClass ) && !field.getType().equals( aClass ) ) ){
                    noCircularDependency = false;
                    foundClasses.add( aClass.getSimpleName() );
                }
            }

        }

        message = replaceMessageClass( message,  foundClasses );

        if( !noCircularDependency )
            postResult( message, false );
        else
            postResult("", true);

        return noCircularDependency;
    }

    /**
     * Tests, whether all classes are immutable.
     * @param message the message that will be displayed if any class is mutable
     * @return true if all classes are immutable and false otherwise
     */
    private boolean testImmutable( String message ){

        boolean immutable = true;

        List<String> foundClasses =  new ArrayList<>();

        for ( Class<?> aClass : classesToTest ) {

            if ( aClass.getFields().length > 0 ) {
                immutable = false;
                foundClasses.add( aClass.getSimpleName() );
                continue;
            }

            if( Arrays.stream(aClass.getMethods()).anyMatch( method -> method.getName().startsWith( "set" ) ) ){
                immutable = false;
                foundClasses.add( aClass.getSimpleName() );
            }
        }

        message = replaceMessageClass( message,  foundClasses );

        if( !immutable )
            postResult( message, false );
        else
            postResult("", true);
        return immutable;
    }

    /**
     * Get all classes that are part of the set package.
     * @return a Set of all classes
     */
    private Set< Class<?> > getAllClasses(){
        Reflections reflections = new Reflections(pathToPackage, Scanners.SubTypes.filterResultsBy(item -> true ) );

        return new HashSet<>(reflections.getSubTypesOf( Object.class ) );
    }

    /**
     * Replace the class placeholder, if it is present, in a given message with a List of elements.
     * @param message the message that contains a placeholder
     * @param elements a List of elements that replace the placeholder
     * @return a String representation of the message that contains the replaced elements
     */
    private String replaceMessageClass(String message, List<String> elements){
        return message.replace("_CLASS_",  "\"" + String.join( "\", \"", elements )  + "\"" );
    }
}
