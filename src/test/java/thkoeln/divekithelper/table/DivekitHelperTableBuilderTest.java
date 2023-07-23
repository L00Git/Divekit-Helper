package thkoeln.divekithelper.table;

import org.junit.jupiter.api.Test;
import thkoeln.divekithelper.common.testlevel.TestLevel;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static thkoeln.divekithelper.table.DivekitHelperTableBuilder.table;

class DivekitHelperTableBuilderTest {
    private String e1a = "src/main/java/thkoeln/divekithelper/mock/tables/E01a.md";
    private String e1aSolution = "src/main/java/thkoeln/divekithelper/mock/tables/E01aSolution.md";
    private String e1a2 = "src/main/java/thkoeln/divekithelper/mock/tables/E01a2.md";
    private String e1b = "src/main/java/thkoeln/divekithelper/mock/tables/E01b.md";
    private String e1bSolution = "src/main/java/thkoeln/divekithelper/mock/tables/E01bSolution.md";
    private String e1c = "src/main/java/thkoeln/divekithelper/mock/tables/E01c.md";
    private String e1cSolution = "src/main/java/thkoeln/divekithelper/mock/tables/E01cSolution.md";
    private String e1d = "src/main/java/thkoeln/divekithelper/mock/tables/E01d.md";
    private String e1dSolution = "src/main/java/thkoeln/divekithelper/mock/tables/E01dSolution.md";
    private String e3a = "src/main/java/thkoeln/divekithelper/mock/tables/E3a.md";
    private String e3aSolution = "src/main/java/thkoeln/divekithelper/mock/tables/E3aSolution.md";
    private String e3a2 = "src/main/java/thkoeln/divekithelper/mock/tables/E3a2.md";
    private String e3a2Solution = "src/main/java/thkoeln/divekithelper/mock/tables/E3a2Solution.md";
    private String e3b ="src/main/java/thkoeln/divekithelper/mock/tables/E3b.md";
    private String e3bSolution ="src/main/java/thkoeln/divekithelper/mock/tables/E3bSolution.md";



    @Test
    void testLevelGenerationTest() {
        TestLevel.generateTestLevel("testLevelConfig.json");
    }

    @Test
    void e1aTest(){
        assertEquals(false,
                table( new TableTest( e1a, e1aSolution ) )
                        .column(0)
                        .rowMismatch()
                            .message(0,"An Element in this row is not matching the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column(0)
                        .missing()
                            .message(0,"You are missing something.")
                            .message(1,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                            .message(0,"A Element doesn't match the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1a", "TableTests")
        );

        assertEquals(false,
                table( new TableTest( e1a, e1aSolution ) )
                        .column("Begriff")
                        .rowMismatch()
                            .message(0,"An Element in this row is not matching the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column("Begriff")
                        .missing()
                            .message(0,"You are missing something.")
                            .message(1,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column("Begriff")
                        .tooMany()
                            .message(0,"A Element doesn't match the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1aSameTestByName", "TableTests")
        );
    }

    @Test
    void e1a2Test(){
        assertEquals( false,
                table( new TableTest( e1a2, e1aSolution ) )
                        .column(3)
                        .missing()
                        .message(0,"You are missing something.")
                        .test("E1a2 Wrong Column Number", "TableTests")
        );

        assertEquals( false,
                table( new TableTest( e1a2, e1aSolution ) )
                        .column("Falscher Name")
                        .missing()
                        .message(0,"You are missing something.")
                        .test("E1a2 Wrong Column Name", "TableTests")
        );

        assertEquals( false,
                table( new TableTest( e1a2, e1aSolution ) )
                        .rowColumnMismatch()
                        .message(0,"_ELEMENT_ does not belong in the position it is currently in.")
                        .test("E1a2 Different Row Count", "TableTests")
        );

    }

    @Test
    void e1bTest(){
        assertEquals(false,
                table( new TableTest( e1b, e1bSolution ) )
                        .column(0)
                        .missing()
                            .message(0,"You are missing something.")
                            .message(1,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                            .message(0,"A Element doesn't match the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .capitalisation()
                            .message(0,"Something does is wrongly capitalized.")
                            .message(1, "_ELEMENT_ is wrongly capitalized.")
                        .test("E1b", "TableTests")
        );
    }

    @Test
    void e1cTest(){
        assertEquals(false,
                table( new TableTest( e1c, e1cSolution ) )
                        .column(0)
                        .rowMismatch()
                            .message(0,"An Element in this row is not matching the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column(0)
                        .missing()
                            .message(0,"You are missing something.")
                            .message(1,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                            .message(0,"A Element doesn't match the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1c", "TableTests")
        );
    }

    @Test
    void e1dTest(){
        assertEquals(true,
                table( new TableTest( e1d, e1dSolution ) )
                        .column(0)
                        .rowMismatch()
                            .message(0,"An Element in this row is not matching the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution.")
                        .combine()
                        .column(0)
                        .missing()
                            .message(0,"You are missing something.")
                            .message(1,"_ELEMENT_ is missing in _COLUMN1_.")
                        .combine()
                        .column(0)
                        .tooMany()
                            .message(0,"A Element doesn't match the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution in _COLUMN1_.")
                        .test("E1d", "TableTests")
        );
    }


    @Test
    void e3aTest(){
        assertEquals(true,

                table( new TableTest( e3a, e3aSolution ) )
                        .column(0)
                        .rowMismatch()
                            .message(0,"An Element in this row is not matching the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution.")
                        .test("E3a","TableTests")

        );

    }

    @Test
    void e3a2Test(){
        assertEquals(false,
                table( new TableTest( e3a2, e3a2Solution ) )
                        .column(0)
                        .rowMismatch()
                            //.message(0,"An Element in this row is not matching the solution.")
                            .message(0,"_ELEMENT_ is not matching the solution.")
                        .test("E3a2","TableTests")
        );
    }

    @Test
    void e3bTest(){
        assertEquals(false,
                table( new TableTest( e3b, e3bSolution ) )
                        .column(0)
                        .rowMismatch()
                            .message(0,"An Element in this row is not matching the solution.")
                            .message(1,"_ELEMENT_ is not matching the solution.")
                        .test("E3b","TableTests")
        );
    }

}