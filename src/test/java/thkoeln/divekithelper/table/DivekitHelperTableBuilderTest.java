package thkoeln.divekithelper.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static thkoeln.divekithelper.table.DivekitHelperTableBuilder.table;

class DivekitHelperTableBuilderTest {
    private TableTest e1a;
    private TableTest e1b;
    private TableTest e1c;
    private TableTest e1d;
    private TableTest e3a;
    private TableTest e3a2;
    private TableTest e3b;

    @BeforeEach
    void setupTestClasses(){
        e1a = new TableTest("src/main/java/thkoeln/divekithelper/mock/tables/E01a.md", "src/main/java/thkoeln/divekithelper/mock/tables/E01aSolution.md");
        e1b = new TableTest("src/main/java/thkoeln/divekithelper/mock/tables/E01b.md", "src/main/java/thkoeln/divekithelper/mock/tables/E01bSolution.md");
        e1c = new TableTest("src/main/java/thkoeln/divekithelper/mock/tables/E01c.md", "src/main/java/thkoeln/divekithelper/mock/tables/E01cSolution.md");
        e1d = new TableTest("src/main/java/thkoeln/divekithelper/mock/tables/E01d.md", "src/main/java/thkoeln/divekithelper/mock/tables/E01dSolution.md");
        e3a = new TableTest("src/main/java/thkoeln/divekithelper/mock/tables/E3a.md", "src/main/java/thkoeln/divekithelper/mock/tables/E3aSolution.md");
        e3a2 = new TableTest("src/main/java/thkoeln/divekithelper/mock/tables/E3a2.md", "src/main/java/thkoeln/divekithelper/mock/tables/E3a2Solution.md");
        e3b = new TableTest("src/main/java/thkoeln/divekithelper/mock/tables/E3b.md", "src/main/java/thkoeln/divekithelper/mock/tables/E3bSolution.md");

    }



    @Test
    void e1aTest(){

        assertEquals(false,
                table(e1a)
                        .column(0)
                        .rowMismatch()
                        .message(1,"An Element in this row is not matching the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column(0)
                        .missing()
                        .message(1,"You are missing something.")
                        .message(2,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                        .message(1,"A Element doesn't match the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1a", "TableTests")
        );

        assertEquals(false,
                table(e1a)
                        .column("Begriff")
                        .rowMismatch()
                        .message(1,"An Element in this row is not matching the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column("Begriff")
                        .missing()
                        .message(1,"You are missing something.")
                        .message(2,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column("Begriff")
                        .tooMany()
                        .message(1,"A Element doesn't match the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1aSameTestByName", "TableTests")
        );
    }

    @Test
    void e1bTest(){
        assertEquals(false,
                table(e1b)
                        .column(0)
                        .missing()
                        .message(1,"You are missing something.")
                        .message(2,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                        .message(1,"A Element doesn't match the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .capitalisation()
                        .message(1,"Something does is wrongly capitalized.")
                        .message(2, "_ELEMENT_ is wrongly capitalized.")
                        .test("E1b", "TableTests")
        );
    }

    @Test
    void e1cTest(){
        assertEquals(false,
                table(e1c)
                        .column(0)
                        .rowMismatch()
                        .message(1,"An Element in this row is not matching the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column(0)
                        .missing()
                        .message(1,"You are missing something.")
                        .message(2,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                        .message(1,"A Element doesn't match the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1c", "TableTests")
        );
    }

    @Test
    void e1dTest(){
        assertEquals(true,
                table(e1d)
                        .column(0)
                        .rowMismatch()
                        .message(1,"An Element in this row is not matching the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column(0)
                        .missing()
                        .message(1,"You are missing something.")
                        .message(2,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                        .message(1,"A Element doesn't match the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1d", "TableTests")
        );
    }


    @Test
    void e3aTest(){
        assertEquals(true,
                table(e3a)
                        .column(0)
                        .rowMismatch()
                        .message(1,"An Element in this row is not matching the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution.")
                        .test("E3a","TableTests")
        );

    }

    @Test
    void e3a2Test(){
        assertEquals(false,
                table(e3a2)
                        .column(0)
                        .rowMismatch()
                        .message(1,"An Element in this row is not matching the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution.")
                        .test("E3a2","TableTests")
        );
    }

    @Test
    void e3bTest(){
        assertEquals(false,
                table(e3b)
                        .column(0)
                        .rowMismatch()
                        .message(1,"An Element in this row is not matching the solution.")
                        .message(2,"_ELEMENT_ is not matching the solution.")
                        .test("E3b","TableTests")
        );
    }

}