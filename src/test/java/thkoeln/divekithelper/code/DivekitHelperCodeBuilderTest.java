package thkoeln.divekithelper.code;



import org.junit.jupiter.api.Test;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static thkoeln.divekithelper.code.DivekitHelperCodeBuilder.*;

public class DivekitHelperCodeBuilderTest {


    @Test
    void test(){
        classes( "thkoeln.divekithelper.mock.repo" )
                .withName("ConnectionRepository");
    }
    @Test
    void isAnnotationPresent()  {
        assertEquals(true,
                classes( "thkoeln.divekithelper.mock.repo" )
                    .withAnnotation( Entity.class )
                    .shouldHave()
                    .annotation(  javax.persistence.Id.class )
                        .message(1, "Entities should have Ids.")
                        .message(2, "_CLASS_ is an Entities and should have an Id.")
                    .test( "EntityId" , "AnnotationTest"));
    }

    @Test
    void isAnnotationNotPresent()  {
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Embeddable.class )
                            .shouldNotHave()
                        .annotation( Id.class )
                            .message(1, " VO should not have an ID.")
                            .message(2, "_CLASS_ is a VO and shouldn't have a ID.")
                        .test("VoNoId" , "AnnotationTest"));

        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Embeddable.class )
                        .shouldNotHave()
                        .annotation( Entity.class )
                            .message(1, " VO should not also be an Entity.")
                            .message(2, "_CLASS_ is a VO and shouldn't also be an Entity.")
                        .test("VoNoEntity", "AnnotationTest"));

    }

@Test
    void isOtherClassPresent()  {
        assertEquals(false,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Entity.class )
                        .shouldHave()
                        .otherClass( "_CLASS_Repository" )
                            .message(1, "You are missing a Repository.")
                            .message(2, "Entities should have Repositories.")
                            .message(2, "_CLASS_ is missing a Repository.")
                        .test("EntityRepo", "AnnotationTest"));
    }

    @Test
    void isClassInPackage()  {
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation(Entity.class)
                        .inPackage("thkoeln.divekithelper.mock.repo.entities")
                            .message(1, "All Entities should be in the Entity Folder")
                            .message(2, "_CLASS_ is an Entity and should be in a Folder called entities")
                        .test("EntityDirectory", "AnnotationTest"));
    }

    @Test
    void isCircularDependencyPresent(){
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .noCircularDependencies()
                            .message(1,"Circular Dependencies should be avoided.")
                            .message(2, "_CLASS_ has circular Dependencies.")
                        .test("CircularDependency", "AnnotationTest"));
    }
    @Test
    void isCircularDependencyPresentEntity(){
        assertEquals(true,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation(Entity.class)
                        .noCircularDependencies()
                            .message(1,"Circular Dependencies should be avoided in Entities.")
                            .message(2, "_CLASS_ is an Entity and shouldn't have circular Dependencies.")
                        .test("CircularDependencyEntity", "AnnotationTest"));
    }


    @Test
    void isImmutable()  {
        assertEquals(false,
                classes("thkoeln.divekithelper.mock.repo")
                        .withAnnotation( Embeddable.class )
                        .immutable()
                            .message(1, " VO should be immutable.")
                            .message(2, "_CLASS_ should be immutable.")
                        .test("VOImmutable", "VOTest"));
    }
    @Test
    void stackTrace()  {
        assertEquals(true,
                classes("thkoeln.mockData")
                        .stackTrace()
                            .message(1,"The stacktrace in Test: _TEST_  contains your own class, please take a closer look.")
                            .message(2, "This line of the stacktrace in Test: _TEST_ contains your own Class maybe take a look there:\n _LINE_" )
                        .test("StacktraceTip", "StacktraceTest"));
    }
}
