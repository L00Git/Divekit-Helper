package thkoeln.divekithelper.table;

import java.util.List;

/**
 * This Interface defines all Methods that a TableTest needs, in order to be compatible With the DivekitHelper-DSL.
 */
public interface TableTestInterface {
    /**
     * Get the table this Test designated for.
     * @return a 2D Array representing the table
     */
    String[][] getTable();


    /**
     * Tests, whether the specified Table has a valid Syntax.
     * @return  true if the table is valid
     *          false otherwise
     */
    boolean isTableValid();

    /**
     * Get the names/titles of the columns.
     * @return a String-Array of column names ordered consistent with the table
     */
    String[] getColumnNames();

    /**
     * Get the index of a column by its name.
     * @param columnName name of the column
     * @return the corresponding column index
     */
    int getColumnNumber( String columnName );

    /**
     * Get all elements that are in the solution but not the table in the given column.
     * @param column the column index
     * @return a List of missing elements
     */
    List<String> getMissingInColumn( int column );

    /**
     * Get all elements that are in the table but not the solution in the given column.
     * @param column the column index
     * @return a List of excessive elements
     */
    List<String> getTooManyInColumn( int column );

    /**
     * Get all elements that are wrongly capitalized.
     * @param column the column index
     * @return a List of wrongly capitalized elements
     */
    List<String> getWrongCapitalisation( int column );

    /**
     * Get all  row-indices of elements that are wrongly capitalized.
     * @param column the column index
     * @return a List containing the row-indices of wrongly capitalized elements
     */
    List<Integer> getWrongCapitalisationIndex(int column );

    /**
     * Get all elements that should be in column 1 but are in column2.
     * @param expectedColumn index of column elements should be in
     * @param actualColumn index of column elements are in
     * @return a List of all misplaced elements
     */
    List<String> getWrongColumn( int expectedColumn, int actualColumn );

    /**
     * Get all  row-indices of elements  hat should be in column 1 but are in column2
     * @param expectedColumn index of column elements should be in
     * @param actualColumn index of column elements are in
     * @return a List containing the row-indices of wrongly placed elements
     */
    List<Integer> getWrongColumnIndex( int expectedColumn, int actualColumn );

    /**
     * Get a List of all mismatched cells, by comparing rows and ignoring the row order.
     * @param idColumn the column index of a column containing unique values
     * @return an int-Array-List of mismatched cells, every int-Array consists of [0] row and [1] column.
     */
    List<int[]> getRowMismatches( int idColumn );

    /**
     * Get a List of all elements that are mismatched, by comparing rows and ignoring the row order.
     * @param idColumn the column index of a column containing unique values
     * @param row the row index
     * @return a List of mismatched elements in a given row
     */
    List<String> getRowMismatchesInRow( int idColumn, int row );

    /**
     * Get a List of all mismatched cells between the solution and table, this test is row order sensitive.
     * @return an int-Array-List of mismatched cells, every int-Array consists of [0] row and [1] column.
     */
    List<int[]> getRowColumnMismatches();

    /**
     * Get a List of all elements that are mismatched in the given row, this test is row order sensitive.
     * @param row the row index
     * @return a List of mismatched elements in a given row
     */
    List<String> getRowColumnMismatchElementsInRow(int row );

}
