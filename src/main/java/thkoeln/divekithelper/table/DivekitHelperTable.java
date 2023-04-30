package thkoeln.divekithelper.table;

import lombok.Getter;
import lombok.Setter;
import thkoeln.divekithelper.common.DivekitHelper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the tests associated with Tables,
 * by making calls to the TableTest-Class and generating the corresponding output.
 */
public class DivekitHelperTable extends DivekitHelper {

    private TableTestInterface tableTest;

    @Getter
    private Map<Integer, String> rowComments = new HashMap<>();

    @Getter
    private Map<Integer, String> columnComments = new HashMap<>();

    @Getter
    private List<int[]> highlightErrors = new ArrayList<>();

    private int column1, column2 = 0;
    private String columnName1 = "";
    private String columnName2 = "";

    public enum TestType { NONE, MISSING, TOOMANY, WRONGCOLUMN, ROWMISMATCH, ROWCOLUMNMISMATCH, CAPITALISATION}

    @Setter
    private TestType test = TestType.NONE;


    /**
     * Initialize the class.
     * @param tableTest a TableTest-Class that implements the TabelTestInterface
     */
    public DivekitHelperTable( TableTestInterface tableTest ) {
        this.tableTest = tableTest;
    }

    /**
     * Set the column that should be tested by its name.
     * @param columnName the column name
     */
    public void setColumn( String columnName ){
        if( !tableTest.isTableValid() )
            return;

        columnName1 = columnName;
        column1 = tableTest.getColumnNumber( columnName );
    }

    /**
     * Set the column that should be tested by its number.
     * @param columnNumber the column number (0 being the first column)
     */
    public void setColumn( int columnNumber ){
        if( !tableTest.isTableValid() )
            return;

        columnName1 = tableTest.getColumnNames()[columnNumber];
        column1 = columnNumber;
    }

    /**
     * Set the columns that should be tested by their names.
     * @param columnName1 the name of the first column
     * @param columnName2 the name of the second column
     */
    public void setColumn( String columnName1, String columnName2 ){
        if( !tableTest.isTableValid() )
            return;

        this.columnName1 = columnName1;
        this.columnName2 = columnName2;
        column1 = tableTest.getColumnNumber( columnName1 );
        column2 = tableTest.getColumnNumber( columnName2 );
    }

    /**
     * Set the columns that should be tested by their number.
     * @param columnNumber1 the number of the fist column
     * @param columnNumber2 the number of the second column
     */
    public void setColumn( int columnNumber1, int columnNumber2 ){
        if( !tableTest.isTableValid() )
            return;


        this.columnName1 = tableTest.getColumnNames()[columnNumber1];
        this.columnName2 = tableTest.getColumnNames()[columnNumber2];
        column1 = columnNumber1;
        column2 = columnNumber2;
    }


    /**
     * Trigger the test by calling the correct test-case.
     * @param testName the name this test will display
     * @param testCategory the name of the category this test falls under
     * @return true if the tests passes and false otherwise
     */
    private boolean buildResult( String testName, String testCategory ){

        setTestName( testName );
        setTestCategory( testCategory );
        String message = getMessage();
        switch (test) {
            case NONE:
                return false;
            case ROWCOLUMNMISMATCH:
                return testRowColumnMismatch( message );
            case ROWMISMATCH:
                return testRowMismatch( message );
            case CAPITALISATION:
                return testCapitalisation( message );
            case MISSING:
                return testMissing( message );
            case TOOMANY:
                return testTooMany( message );
            case WRONGCOLUMN:
                return testWrongColumn( message );
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Tests, whether all elements of the user table and solution table are in the same column and row.
     * @param message the message that will be displayed, if the test fails
     * @return true if no mismatch was found and false otherwise
     */
    private boolean testRowColumnMismatch( String message ){

        List<int[]> mismatches = tableTest.getRowColumnMismatches();

        for( int row = 0; row < tableTest.getTable().length; row ++ ){
            List<String> mismatchElementsInRow = tableTest.getRowColumnMismatchElementsInRow( row );
            if(mismatchElementsInRow.size() > 0)
                rowComments.put( row, replaceMessageElement( message, mismatchElementsInRow, mismatches ) );
        }

        return mismatches.size() < 1;
    }

    /**
     * Tests, whether all elements of the solution table, which are in a row, are also present in the user table, in a row.
     * The order of rows is ignored and a reference column is used to match the solution table to the user table.
     * @param message the message that will be displayed, if the test fails
     * @return true if no mismatch was found and false otherwise
     */
    private boolean testRowMismatch( String message ){
        List<int[]> mismatches = tableTest.getRowMismatches( column1 );

        for( int row = 0; row < tableTest.getTable().length; row ++ ){
            List<String> mismatchElementsInRow = tableTest.getRowMismatchesInRow( column1, row );
            if(mismatchElementsInRow.size() > 0)
                rowComments.put( row, replaceMessageElement( message, mismatchElementsInRow, mismatches ) );
        }

        return mismatches.size() < 1;
    }

    /**
     * Tests, whether any element, in the set column, is wrongly capitalised.
     * @param message the message that will be displayed, if the test fails
     * @return true if no violation was found and false otherwise
     */
    private boolean testCapitalisation( String message ){
        List<String> wronglyCapitalisedWords = tableTest.getWrongCapitalisation( column1 );

        if( wronglyCapitalisedWords.size() < 1)
            return true;

        List<int[]> highlights = tableTest.getWrongCapitalisationIndex( column1 ).stream().map( row -> new int[]{row, column1} ).collect(Collectors.toList());

        String result = replaceMessageElement( message, wronglyCapitalisedWords, highlights );

        result = replaceMessageColumn( result );

        columnComments.put( column1, result  + "<br>" );


        return false;
    }

    /**
     * Tests, whether any elements are missing, in a specific column.
     * @param message the message that will be displayed, if the test fails
     * @return true if no violation was found and false otherwise
     */
    private boolean testMissing( String message ){
        List<String> missingElements = tableTest.getMissingInColumn( column1 );

        if( missingElements.size() < 1)
            return true;

        String result = replaceMessageElement( message, missingElements );
        result = replaceMessageColumn( result );

        columnComments.put( column1, result  + "<br>" );

        return false;
    }

    /**
     * Tests, whether any elements are unnecessary, in a specific column.
     * @param message the message that will be displayed, if the test fails
     * @return true if no violation was found and false otherwise
     */
    private boolean testTooMany( String message ){
        List<String> tooManyElements = tableTest.getTooManyInColumn( column1 );

        if( tooManyElements.size() < 1)
            return true;

        String result = replaceMessageElement( message, tooManyElements);
        result = replaceMessageColumn( result );

        columnComments.put( column1, result  + "<br>" );

        return false;
    }

    /**
     * Tests, whether any elements of column 1 are in column 2.
     * @param message the message that will be displayed, if any elements are found to be in the wrong column
     * @return true if no elements of column 1 are in column 2 and false otherwise
     */
    private boolean testWrongColumn( String message ){

        List<String> wrongElements = tableTest.getWrongColumn( column1, column2 );

        if( wrongElements.size() < 1)
            return true;

        List<int[]> highlights = tableTest.getWrongColumnIndex( column1, column2 ).stream().map( row -> new int[]{ row, column2 } ).collect(Collectors.toList());

        String result = replaceMessageElement( message, wrongElements, highlights );
        result = replaceMessageColumn( result );

        columnComments.put( column2, result  + "<br>" );

        return false;
    }

    /**
     * Replace the element placeholders in a given message, with a List of elements.
     * @param message the message that potentially contains the placeholders
     * @param elements the elements that are replacing the placeholder
     * @return the replaced message
     */
    private String replaceMessageElement( String message, List<String> elements ){
        return message.replace("_ELEMENT_",  "\"" + String.join( "\", \"", elements )  + "\"" );
    }

    /**
     * Replace the element placeholders in a given message, with a List of elements and potentially add the highlighted cells.
     * @param message the message that potentially contains the placeholders
     * @param elements the elements that are replacing the placeholder
     * @param highlights a List of cells, which are being highlighted, if a element placeholder is present
     * @return the replaced message
     */
    private String replaceMessageElement( String message, List<String> elements, List<int[]> highlights ){
        if(message.contains("_ELEMENT_"))
            highlightErrors.addAll( highlights );

        return message.replace("_ELEMENT_",  "\"" + String.join( "\", \"", elements )  + "\"" );
    }

    /**
     * Replace the column placeholders in a given message, if they are present.
     * @param message the message that potentially contains the placeholders
     * @return the replaced message
     */
    private String replaceMessageColumn( String message ){
        return message.replace("_COLUMN1_", columnName1).replace("_COLUMN2_", columnName2);
    }

    /**
     * Trigger the tests for a given List of DivekitHelperTables and combine their results.
     * @param divekitHelperTableTests a List of DivekitHelperTables that are being combined
     * @param testName the name this combined test will display
     * @param testCategory the name of the category this combined test falls under
     * @return true if no test fails and false otherwise
     */
    public boolean combineResults( List< DivekitHelperTable > divekitHelperTableTests, String testName, String testCategory ){

        if( !tableTest.isTableValid() ){
            setTestName( testName );
            setTestCategory( testCategory );
            postResult("The table is invalid.", false);
            return false;
        }

        boolean result = true;
        for( DivekitHelperTable test: divekitHelperTableTests ){
            if ( !test.buildResult( testName, testCategory ) )
                result = false;
        }

        for ( DivekitHelperTable test: divekitHelperTableTests ){
            if(test != this)
                mergeComments( test );
        }

        if( !result )
            postResult( createTable( rowComments, columnComments ), false );
        else
            postResult("", true);

        return result;
    }

    /**
     * Merge all comments and highlights from the given DivekitHelperTable and the local object.
     * @param test the DivekitHelperTable that should be merged
     */
    private void mergeComments( DivekitHelperTable test ) {

        for ( int key: test.columnComments.keySet() ){
            if( columnComments.containsKey( key ) ){
                columnComments.put( key, columnComments.get( key ) + test.columnComments.get( key ) );
            }
            else {
                columnComments.put( key, test.columnComments.get( key ) );
            }
        }

        for ( int key: test.rowComments.keySet() ){
            if( rowComments.containsKey( key ) ){
                rowComments.put( key, rowComments.get( key ) + " | " + test.rowComments.get( key ) );
            }
            else {
                rowComments.put( key, test.rowComments.get( key ) );
            }
        }

        for ( int[] highlightError: test.getHighlightErrors() ){
            if( !highlightErrors.contains( highlightError) ){
                highlightErrors.add( highlightError );
            }
        }

    }

    /**
     * Create an HTML-Table of the current table, which includes the given row and column comments.
     * @param rowComments comments which are row specific
     * @param columnComments comments which are column specific
     * @return a String containing the HTML-Code
     */
    private String createTable( Map<Integer, String> rowComments,  Map<Integer, String> columnComments) {

        String[][] table = tableTest.getTable();
        String[] columnNames = tableTest.getColumnNames();

        StringBuilder sb = new StringBuilder();

        sb.append("<table border=\"1\">");

        if( columnNames.length == table.length){
            sb.append("<tr>");
            for (int column = 0; column < table[0].length; column++) {
                sb.append("<th style=\"padding:3px\">" + columnNames[ column ] + "</th>");
            }
            sb.append("</tr>");
        }

        for ( int row = 0; row < table.length; row++ ) {
            sb.append("<tr>");
            for (int column = 0; column < table[0].length; column++) {
                int[] cell = new int[]{row, column};
                if(highlightErrors.stream().anyMatch(element -> Arrays.equals( element, cell ) ) )
                    sb.append( "<td bgcolor=\"red\" style=\"padding:3px\">" + table[ row ][ column ] + "</td>" );
                else
                    sb.append( "<td style=\"padding:3px\">" + table[ row ][ column ] + "</td>" );
            }
            if( rowComments.containsKey( row ) )
                sb.append( "<td style=\"padding:3px\">" + rowComments.get( row ) + "</td>" );
            sb.append("</tr>");
        }


        if( columnComments.size() < 1 ){
            sb.append("</table>");
            return sb.toString();
        }



        sb.append("<tr>");
        for (int column = 0; column < table[0].length; column++) {
            sb.append( "<td style=\"padding:3px\">" + (column + 1) + "</td>" );
        }
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("<br>");

        for (int column = 0; column < table[0].length; column++) {
            if( columnComments.containsKey( column ) ){
                sb.append( (column + 1) + ":<br>" );
                sb.append( columnComments.get( column )  );
                sb.append( "<br>---------------------------------<br>" );
            }
        }

        return sb.toString();
    }
}
