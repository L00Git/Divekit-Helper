package thkoeln.divekithelper.code;



import org.junit.jupiter.api.Test;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static thkoeln.divekithelper.code.DivekitHelperCodeBuilder.*;

public class DivekitHelperCodeBuilderTest {


    @Test
    void isAnnotationPresent()  {
        assertEquals(true,
                classes( "thkoeln.divekithelper.mock.repo" )
                    .withAnnotation( Entity.class )
                    .shouldHave()
                    .annotation(  javax.persistence.Id.class )
                        .message(0, "Entities should have Ids.")
                        .message(1, "_CLASS_ is an Entities and should have an Id.")
                    .test( "EntityId" , "AnnotationTest"));
    }

    @Test
    void isAnnotationNotPresent()  {
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Embeddable.class )
                            .shouldNotHave()
                        .annotation( Id.class )
                            .message(0, " VO should not have an ID.")
                            .message(1, "_CLASS_ is a VO and shouldn't have a ID.")
                        .test("VoNoId" , "AnnotationTest"));

        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Embeddable.class )
                        .shouldNotHave()
                        .annotation( Entity.class )
                            .message(0, " VO should not also be an Entity.")
                            .message(1, "_CLASS_ is a VO and shouldn't also be an Entity.")
                        .test("VoNoEntity", "AnnotationTest"));

    }

@Test
    void isOtherClassPresent()  {
        assertEquals(false,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Entity.class )
                        .shouldHave()
                        .otherClass( "_CLASS_Repository" )
                            .message(0, "You are missing a Repository.")
                            .message(1, "Entities should have Repositories.")
                            .message(2, "_CLASS_ is missing a Repository.")
                        .test("EntityRepo", "AnnotationTest"));
    }

    @Test
    void isClassInPackage()  {
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation(Entity.class)
                        .inPackage("thkoeln.divekithelper.mock.repo.entities")
                            .message(0, "All Entities should be in the Entity Folder")
                            .message(1, "_CLASS_ is an Entity and should be in a Folder called entities")
                        .test("EntityDirectory", "AnnotationTest"));
    }

    @Test
    void isCircularDependencyPresent(){
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .noCircularDependencies()
                            .message(0,"Circular Dependencies should be avoided.")
                            .message(1, "_CLASS_ has circular Dependencies.")
                        .test("CircularDependency", "AnnotationTest"));
    }
    @Test
    void isCircularDependencyPresentEntity(){
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation(Entity.class)
                        .noCircularDependencies()
                            .message(0,"Circular Dependencies should be avoided in Entities.")
                            .message(1, "_CLASS_ is an Entity and shouldn't have circular Dependencies.")
                        .test("CircularDependencyEntity", "AnnotationTest"));
    }


    @Test
    void isImmutable()  {
        assertEquals(false,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Embeddable.class )
                        .immutable()
                            .message(0, " VO should be immutable.")
                            .message(1, "_CLASS_ should be immutable.")
                        .test("VOImmutable", "VOTest"));
    }
    @Test
    void stackTrace()  {
        assertEquals(true,
                classes("thkoeln.mockData")
                        .stackTrace()
                            .message(0,"The stacktrace in Test: _TEST_  contains your own class, please take a closer look.")
                            .message(1, "This line of the stacktrace in Test: _TEST_ contains your own Class maybe take a look there:\n _LINE_" )
                        .test("StacktraceTip", "StacktraceTest"));
    }
}
