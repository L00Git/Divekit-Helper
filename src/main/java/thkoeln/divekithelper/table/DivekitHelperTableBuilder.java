package thkoeln.divekithelper.table;



import thkoeln.divekithelper.table.interfaces.*;


import java.util.ArrayList;
import java.util.List;

/**
 * Implements the DivekitHelper DSL for TableTests
 */
public class DivekitHelperTableBuilder implements DivekitHelperTableBuilderPostTable,
                                                    DivekitHelperTableBuilderPostRowColumnMismatch,
                                                    DivekitHelperTableBuilderPostCapitalisation,
                                                    DivekitHelperTableBuilderPostColumn,
                                                    DivekitHelperTableBuilderPostMissing,
                                                    DivekitHelperTableBuilderPostTooMany,
                                                    DivekitHelperTableBuilderPostRowMismatch,
                                                    DivekitHelperTableBuilderPostWrongColumn,
                                                    DivekitHelperTableBuilderPostMissingOr,
                                                    DivekitHelperTableBuilderPostTooManyOr,
                                                    DivekitHelperTableBuilderPostCapitalisationOr,
                                                    DivekitHelperTableBuilderPostWrongColumnOr,
                                                    DivekitHelperTableBuilderPostMissingOrColumn,
                                                    DivekitHelperTableBuilderPostTooManyOrColumn,
                                                    DivekitHelperTableBuilderPostCapitalisationOrColumn,
                                                    DivekitHelperTableBuilderPostMessage {


    private List<DivekitHelperTable> divekitHelperTableTests = new ArrayList<DivekitHelperTable> ();

    private List<DivekitHelperTable> tableTestsMissingMessage = new ArrayList<DivekitHelperTable> ();

    private TableTestInterface tableTest;

    /**
     * Initiate a DivekitHelperTableTest and set the TableTest.
     * @param tableTest a TableTest that implements the TableTestInterface
     */
    public DivekitHelperTableBuilder( TableTestInterface tableTest ){
        this.tableTest = tableTest;
    }

    /**
     * Initiate a DivekitHelperTableTest.
     * @param tableTest a TableTest that implements the TableTestInterface
     * @return a new Builder object
     */
    public static DivekitHelperTableBuilderPostTable table( TableTestInterface tableTest ){
        return new DivekitHelperTableBuilder( tableTest );
    }

    /**
     * Sets the column that should be tested.
     * @param columnName name of the to be tested column
     * @return this Builder object
     */
    public DivekitHelperTableBuilder column( String columnName ){
        divekitHelperTableTests.add( new DivekitHelperTable( tableTest ) );
        tableTestsMissingMessage.add( getCurrentDivekitHelperTableTest() );
        getCurrentDivekitHelperTableTest().setColumn( columnName );
        return this;
    }

    /**
     * Sets the column that should be tested.
     * @param columnNumber number of the to be tested column
     * @return this Builder object
     */
    public DivekitHelperTableBuilder column( int columnNumber ){
        divekitHelperTableTests.add( new DivekitHelperTable( tableTest ) );
        tableTestsMissingMessage.add( getCurrentDivekitHelperTableTest() );
        getCurrentDivekitHelperTableTest().setColumn( columnNumber );
        return this;
    }

    /**
     * Tests, whether all elements are in the right column and row.
     * @return this Builder object
     */
    public DivekitHelperTableBuilder rowColumnMismatch(){
        divekitHelperTableTests.add( new DivekitHelperTable( tableTest ) );
        tableTestsMissingMessage.add( getCurrentDivekitHelperTableTest() );
        getCurrentDivekitHelperTableTest().setTest( DivekitHelperTable.TestType.ROWCOLUMNMISMATCH);
        return this;
    }

    /**
     * Tests, whether all elements are in the row they belong to. The row order is ignored and the set column is used as an identifier.
     * @return this Builder object
     */
    public DivekitHelperTableBuilder rowMismatch(){
        getCurrentDivekitHelperTableTest().setTest( DivekitHelperTable.TestType.ROWMISMATCH );
        return this;
    }

    /**
     * Tests, whether all elements have the same capitalization as the solution.
     * @return this Builder object
     */
    public DivekitHelperTableBuilder capitalisation(){
        getCurrentDivekitHelperTableTest().setTest( DivekitHelperTable.TestType.CAPITALISATION );
        return this;
    }

    /**
     * Tests, whether any element is missing in the given column.
     * @return this Builder object
     */
    public DivekitHelperTableBuilder missing(){
        getCurrentDivekitHelperTableTest().setTest( DivekitHelperTable.TestType.MISSING );
        return this;
    }

    /**
     * Tests, whether any element not part of the solution is in the given column.
     * @return this Builder object
     */
    public DivekitHelperTableBuilder tooMany(){
        getCurrentDivekitHelperTableTest().setTest( DivekitHelperTable.TestType.TOOMANY );
        return this;
    }

    /**
     * Tests, whether an element of column 1 is in column 2.
     * @param expectedColumnName name of column 1
     * @param actualColumnName name of column 2
     * @return this Builder object
     */
    public DivekitHelperTableBuilder wrongColumn( String expectedColumnName, String actualColumnName ){
        divekitHelperTableTests.add( new DivekitHelperTable( tableTest ) );
        tableTestsMissingMessage.add( getCurrentDivekitHelperTableTest() );
        getCurrentDivekitHelperTableTest().setColumn( expectedColumnName, actualColumnName );
        getCurrentDivekitHelperTableTest().setTest( DivekitHelperTable.TestType.WRONGCOLUMN );
        return this;
    }

    /**
     * Tests, whether an element of column 1 is in column 2.
     * @param expectedColumn number of column 1
     * @param actualColumn number column 2
     * @return this Builder object
     */
    public DivekitHelperTableBuilder wrongColumn( int expectedColumn, int actualColumn ){
        divekitHelperTableTests.add( new DivekitHelperTable( tableTest ) );
        tableTestsMissingMessage.add( getCurrentDivekitHelperTableTest() );
        getCurrentDivekitHelperTableTest().setColumn( expectedColumn, actualColumn );
        getCurrentDivekitHelperTableTest().setTest( DivekitHelperTable.TestType.WRONGCOLUMN );
        return this;
    }

    /**
     * Combines multiple Tests of the same type and table. Allows one to define a message for all "or" combined Tests.
     * @return this Builder object
     */
    public DivekitHelperTableBuilder or(){
        return this;
    }

    /**
     * Defines the message that gets displayed if the test finds a violation.
     * @param testLevel the test level that has to be reached, for the message to be displayed
     * @param message message to be displayed, does allow Test specific placeholders (e.g.'_ELEMENT_')
     * @return this Builder object
     */
    public DivekitHelperTableBuilder message( int testLevel, String message ){
        for( DivekitHelperTable test: tableTestsMissingMessage ){
            test.addMessage( testLevel, message );
        }
        return this;
    }

    /**
     * Combine multiple Tests of different types on one table.
     * @return this Builder object
     */
    public  DivekitHelperTableBuilder combine(){
        tableTestsMissingMessage.clear();
        return this;
    }

    /**
     * Execute and combine all tests.
     * @param testName the title that the test will be displayed under.
     * @param testCategory the category under which the test will be set classified
     * @return true if all defined tests find no violation / false if at lease one test finds a violation
     */
    public boolean test( String testName, String testCategory ){
        return divekitHelperTableTests.get(0).combineResults(divekitHelperTableTests, testName, testCategory );
    }

    /**
     * A getter method for the most recently added DivekitHelperTableTest.
     * @return the newest DivekitHelperTableTest or null if no Test was added at all
     */
    private DivekitHelperTable getCurrentDivekitHelperTableTest(){
        if(divekitHelperTableTests.size() < 1)
            return null;
        return divekitHelperTableTests.get( divekitHelperTableTests.size()-1 );
    }

}
