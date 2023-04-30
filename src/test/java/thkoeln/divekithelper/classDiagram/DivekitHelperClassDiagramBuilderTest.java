package thkoeln.divekithelper.classDiagram;

import org.junit.jupiter.api.Test;
import thkoeln.divekithelper.mock.implementations.MockClassDiagramTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static thkoeln.divekithelper.classDiagram.DivekitHelperClassDiagramBuilder.*;

public class DivekitHelperClassDiagramBuilderTest {

    @Test
    void isClassMissing()  {
        assertEquals(false,
                classDiagram( new MockClassDiagramTest("1") )
                        .missingClasses()
                            .message(1, "A Class is missing.")
                            .message(2, "_CLASS_ is missing.")
                        .test("MissingClass", "ClassDiagramTest")
        );
    }

    @Test
    void isClassMissingPassing()  {
        assertEquals(true,
                classDiagram( new MockClassDiagramTest("2") )
                        .missingClasses()
                            .message(1, "A Class is missing.")
                            .message(2, "_CLASS_ is missing.")
                        .test("MissingClassPassing", "ClassDiagramTest")
        );
    }

    @Test
    void isRelationMissing()  {
        assertEquals(false,
                classDiagram( new MockClassDiagramTest("1") )
                        .missingRelations()
                            .message(1, "A Relation is missing.")
                            .message(2, "_RELATION_ is missing.")
                        .test("Relation", "ClassDiagramTest")
        );
    }

    @Test
    void isRelationMissingPassing()  {
        assertEquals(true,
                classDiagram( new MockClassDiagramTest("2") )
                        .missingRelations()
                            .message(1, "A Relation is missing.")
                            .message(2, "_RELATION_ is missing.")
                        .test("RelationPassing", "ClassDiagramTest")
        );
    }

    @Test
    void mismatch()  {
        assertEquals(false,
                classDiagram( new MockClassDiagramTest("1") )
                        .mismatch()
                            .message(1, "There is a mismatch between the diagram and glossary.")
                            .message(2, "_MISMATCH_ is not matching the glossary.")
                        .test("Mismatch", "ClassDiagramTest")
        );
    }

    @Test
    void mismatchPassing()  {
        assertEquals(true,
                classDiagram( new MockClassDiagramTest("2") )
                        .mismatch()
                            .message(1, "There is a mismatch between the diagram and glossary.")
                            .message(2, "_MISMATCH_ is not matching the glossary.")
                        .test("MismatchPassing", "ClassDiagramTest")
        );
    }
    @Test
    void wrongRelation()  {
        assertEquals(false,
                classDiagram( new MockClassDiagramTest("1") )
                        .wrongRelations()
                            .message(1, "A wrong Relation is present.")
                            .message(2, "This Relation shouldn't exist: _Relation_ .")
                        .test("WrongRelation", "ClassDiagramTest")
        );
    }

    @Test
    void wrongRelationPassing()  {
        assertEquals(true,
                classDiagram( new MockClassDiagramTest("2") )
                        .wrongRelations()
                            .message(1, "A wrong Relation is present.")
                            .message(2, "This Relation shouldn't exist: _RELATION_ .")
                        .test("WrongRelationPassing", "ClassDiagramTest")
        );
    }

    @Test
    void illegalElement()  {
        assertEquals(false,
                classDiagram( new MockClassDiagramTest("1") )
                        .illegalElements()
                        .message(1, "A illegal Element is present.")
                        .message(2, "UMLet does not allow _ELEMENT_.")
                        .test("IllegalElement", "ClassDiagramTest")
        );
    }

    @Test
    void illegalElementPassing()  {
        assertEquals(true,
                classDiagram( new MockClassDiagramTest("2") )
                        .illegalElements()
                        .message(1, "A illegal Element is present.")
                        .message(2, "UMLet does not allow _ELEMENT_.")
                        .test("IllegalElementPassing", "ClassDiagramTest")
        );
    }



    @Test
    void combined()  {
        assertEquals(false,
                classDiagram( new MockClassDiagramTest("1") )
                        .missingClasses()
                            .message(1, "A Class is missing.")
                            .message(2, "_CLASS_ is missing.")
                        .combine()
                        .missingRelations()
                            .message(1, "A Relation is missing.")
                            .message(2, "_RELATION_ is missing.")
                        .combine()
                        .mismatch()
                            .message(1, "There is a mismatch between the diagram and glossary.")
                            .message(2, "_MISMATCH_ is not matching the glossary.")
                        .combine()
                        .wrongRelations()
                            .message(1, "A wrong Relation is present.")
                            .message(2, "This Relation shouldn't exist: _Relation_ .")
                        .combine()
                        .illegalElements()
                            .message(1, "A illegal Element is present.")
                            .message(2, "UMLet does not allow _ELEMENT_.")
                        .test("Combined", "ClassDiagramTest")
        );
    }

    @Test
    void combinedPassing()  {
        assertEquals(true,
                classDiagram( new MockClassDiagramTest("2") )
                        .missingClasses()
                            .message(1, "A Class is missing.")
                            .message(2, "_CLASS_ is missing.")
                        .combine()
                        .missingRelations()
                            .message(1, "A Relation is missing.")
                            .message(2, "_RELATION_ is missing.")
                        .combine()
                        .mismatch()
                            .message(1, "There is a mismatch between the diagram and glossary.")
                            .message(2, "_MISMATCH_ is not matching the glossary.")
                        .combine()
                        .wrongRelations()
                            .message(1, "A wrong Relation is present.")
                            .message(2, "This Relation shouldn't exist: _Relation_ .")
                        .combine()
                        .illegalElements()
                            .message(1, "A illegal Element is present.")
                            .message(2, "UMLet does not allow _ELEMENT_.")
                        .test("CombinedPassing", "ClassDiagramTest")
        );
    }
}
