package thkoeln.divekithelper.table;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A Class that implements table tests.
 */
public class TableTest {

    @Getter
    private MarkdownTable userTable;

    private MarkdownTable solutionTable;

    private List<Integer> caseSensitiveColumns = new ArrayList<>();

    @Getter
    private boolean testValid;

    /**
     * Create a table test on two given tables.
     * @param userTablePath path to the user table
     * @param solutionTablePath path to the solution table
     */
    public TableTest( String userTablePath, String solutionTablePath ) {
        userTable = MarkdownTableHelper.loadTable(userTablePath, false);
        solutionTable = MarkdownTableHelper.loadTable(solutionTablePath, true);

        testValid = userTable.isValid() && solutionTable.isValid() && MarkdownTableHelper.areTablesComparable( userTable, solutionTable );
    }

    /**
     * Create a table test on two given tables.
     * @param userTablePath path to the user table
     * @param solutionTablePath path to the solution table
     * @param caseSensitiveColumns List of call Columns that should be case-sensitive
     */
    public TableTest( String userTablePath, String solutionTablePath, List<Integer> caseSensitiveColumns ) {
        userTable = MarkdownTableHelper.loadTable(userTablePath, false);
        solutionTable = MarkdownTableHelper.loadTable(solutionTablePath, true);
        this.caseSensitiveColumns = caseSensitiveColumns;

        testValid = userTable.isValid() && solutionTable.isValid() && MarkdownTableHelper.areTablesComparable( userTable, solutionTable );
    }


    /**
     * Get the column names.
     * @return the column names.
     */
    public String[] getColumnNames(){
        return solutionTable.getColumnNames();
    }

    /**
     * Get a column name by its number.
     * @param columnNumber number of the column.
     * @return the column name
     */
    public String getColumnName( int columnNumber ){
        if( getColumnNames().length < columnNumber + 1 ) {
            testValid = false;
            return null;
        }
        return getColumnNames()[columnNumber];
    }

    /**
     * Get a column number by its name.
     * @param columnName name of the column.
     * @return the column number
     */
    public int getColumnNumber( String columnName ){
        int columnNumber = Arrays.asList( solutionTable.getColumnNames() ).indexOf( columnName );
        if(columnNumber == -1) {
            testValid = false;
        }
        return columnNumber;
    }

    /**
     * Get all missing elements in a given column.
     * @param column the column number
     * @return the missing elements
     */
    public List<String> getMissingInColumn( int column ){
        if( solutionTable.getPlaceholderColumns().contains( column ) )
            return new ArrayList<>();
        return arrayDifference( getColumn( solutionTable.getContent(), column ), getColumn( userTable.getContent(), column ), caseSensitiveColumns.contains( column ) );
    }

    /**
     * Get all unnecessary elements in a given column.
     * @param column the column number
     * @return the unnecessary elements
     */
    public List<String> getTooManyInColumn( int column ){
        if( solutionTable.getPlaceholderColumns().contains( column ) )
            return new ArrayList<>();
        return arrayDifference(getColumn( userTable.getContent(), column), getColumn(solutionTable.getContent(), column), caseSensitiveColumns.contains( column ) );
    }

    /**
     * Get all wrongly capitalized elements in a given column.
     * @param column the column number
     * @return all wrongly capitalized elements
     */
    public List<String> getWrongCapitalisation( int column ){
        ArrayList<String> foundWrongCapitalisation = new ArrayList<>();

        String[] tableColumn = getColumn( userTable.getContent(), column );

        String[] solutionColumn = getColumn( solutionTable.getContent(), column );

        List<String> solutionElements = new ArrayList<>();

        for( int row = 0; row < solutionColumn.length; row++ ) {
            solutionElements.addAll( Arrays.asList( solutionColumn[ row ].split("\\s*,\\s*") ) );
        }

        for( int row = 0; row < tableColumn.length; row++ ){
            String[] tableElements =  tableColumn[ row ].split("\\s*,\\s*");

            for( String tableElement: tableElements ){
                if( solutionElements.stream().anyMatch( solutionElement -> Objects.equals(solutionElement.toLowerCase(), tableElement.toLowerCase())  && !Objects.equals(solutionElement, tableElement ) ) )
                    foundWrongCapitalisation.add( tableElement );
            }

        }

        return foundWrongCapitalisation;
    }

    /**
     * Get the row indices of all wrongly capitalized elements in the given column.
     * @param column the column number
     * @return the row indices
     */
    public List<Integer> getWrongCapitalisationIndex( int column ){
        ArrayList<Integer> foundWrongCapitalisation = new ArrayList<>();

        String[] tableColumn = getColumn( userTable.getContent(), column );

        String[] solutionColumn = getColumn( solutionTable.getContent(), column );

        ArrayList<String> solutionElements = new ArrayList<>();

        for( int row = 0; row < solutionColumn.length; row++ ) {
            solutionElements.addAll( Arrays.asList( solutionColumn[ row ].split("\\s*,\\s*") ) );
        }

        for( int row = 0; row < tableColumn.length; row++ ){
            String[] tableElements =  tableColumn[ row ].split("\\s*,\\s*");

            for( String tableElement: tableElements ){
                if( solutionElements.stream().anyMatch( solutionElement -> Objects.equals(solutionElement.toLowerCase(), tableElement.toLowerCase())  && !Objects.equals(solutionElement, tableElement ) ) )
                    foundWrongCapitalisation.add( row );
            }

        }

        return foundWrongCapitalisation;
    }

    /**
     * Get all elements that should be in a given column but are actually in another given column.
     * @param expectedColumn column number the elements should be in
     * @param actualColumn column number the elements are actually in
     * @return all elements that are in the actual column but should be in the expected column
     */
    public List<String> getWrongColumn( int expectedColumn, int actualColumn ){
        HashSet<String> expected = new HashSet<>();

        for (String cell: getColumn( solutionTable.getContent(), expectedColumn )){
            List<String> elements = Arrays.asList( cell.split("\\s*,\\s*") );
            if( !caseSensitiveColumns.contains( expectedColumn ) ){
                elements = elements.stream().map( String::toLowerCase ).collect(Collectors.toList());
            }
            expected.addAll( elements );
        }

        ArrayList<String> actual = new ArrayList<>();

        for (String cell: getColumn( userTable.getContent(), actualColumn )){
            List<String> elements = Arrays.asList( cell.split("\\s*,\\s*") );
            if( !caseSensitiveColumns.contains( expectedColumn ) ){
                elements = elements.stream().map( String::toLowerCase ).collect(Collectors.toList());
            }
            actual.addAll( elements );
        }

        expected.retainAll( actual );

        return new ArrayList<>(expected);
    }

    /**
     * Get the row indices of all elements that should be in a given column but are actually in another given column.
     * @param expectedColumn column number the elements should be in
     * @param actualColumn column number the elements are actually in
     * @return the row indices of all elements that are in the actual column but should be in the expected column
     */
    public List<Integer> getWrongColumnIndex( int expectedColumn, int actualColumn ){

        HashSet<String> expected = new HashSet<>();

        for (String cell: getColumn( solutionTable.getContent(), expectedColumn ) ){
            List<String> elements = Arrays.asList( cell.split("\\s*,\\s*") );
            if( !caseSensitiveColumns.contains( expectedColumn ) ){
                elements = elements.stream().map( String::toLowerCase ).collect(Collectors.toList());
            }
            expected.addAll( elements );
        }

        ArrayList<String> actual = new ArrayList<>();

        for (String cell: getColumn( userTable.getContent(), actualColumn ) ){
            List<String> elements = Arrays.asList( cell.split("\\s*,\\s*") );
            if( !caseSensitiveColumns.contains( expectedColumn ) ){
                elements = elements.stream().map( String::toLowerCase ).collect(Collectors.toList());
            }
            actual.addAll( elements );
        }

        expected.retainAll( actual );

        String[] column = getColumn( userTable.getContent(), actualColumn );

        ArrayList<Integer> indices = new ArrayList<>();

        for( int row = 0; row < column.length; row++ ){
            List<String> cellElements =  Arrays.asList( column[ row ].split("\\s*,\\s*") );
            if( expected.stream().anyMatch( cellElements::contains ) )
                indices.add( row );
        }


        return indices;
    }

    /**
     * Get the row and column indices of all row mismatched elements.
     * @param idColumn number of the column that contains unique values
     * @return row and column indices of row mismatched elements
     */
    public List<int[]> getRowMismatches( int idColumn ){
        ArrayList<int[]> rowMismatches = new ArrayList<>();

        for (int row = 0; row < userTable.getContent().length; row++) {
            int finalRow = row;
            Optional<String[]> matchingSolutionRow = Arrays.stream( solutionTable.getContent() ).filter(solutionRow ->  areCellsEqual( userTable.getContent()[finalRow][idColumn], solutionRow[idColumn], caseSensitiveColumns.contains( idColumn )  ) ).findFirst();

            if ( matchingSolutionRow.isPresent() )  {
                for( int column = 0; column < matchingSolutionRow.get().length; column++ ) {
                    if( solutionTable.getPlaceholderColumns().contains( column ) )
                        continue;
                    if( !areCellsEqual( userTable.getContent()[row][column], matchingSolutionRow.get()[column], caseSensitiveColumns.contains( column ) ) )
                        rowMismatches.add(new int[]{row, column});
                }
            }
        }
        return rowMismatches;
    }

    /**
     * Get all elements that are row mismatched in a given column.
     * @param idColumn number of the column that contains unique values
     * @param row row number the mismatches are in
     * @return all row mismatched elements in the given row
     */
    public List<String> getRowMismatchesInRow( int idColumn, int row ){
        ArrayList<String> rowMismatchElements = new ArrayList<>();

        Optional<String[]> matchingSolutionRow = Arrays.stream( solutionTable.getContent() ).filter(solutionRow ->  areCellsEqual( userTable.getContent()[row][idColumn], solutionRow[idColumn], caseSensitiveColumns.contains( idColumn ) ) ).findFirst();

        if ( matchingSolutionRow.isPresent() )  {
            for( int column = 0; column < matchingSolutionRow.get().length; column++ ) {
                if( solutionTable.getPlaceholderColumns().contains( column ) )
                    continue;
                if( !areCellsEqual( userTable.getContent()[row][column], matchingSolutionRow.get()[column], caseSensitiveColumns.contains( column ) ) )
                    rowMismatchElements.add( userTable.getContent()[row][column] );
            }
        }

        return rowMismatchElements;
    }


    /**
     * Get the row and column indices of all row column mismatches.
     * @return the row and column indices
     */
    public List<int[]> getRowColumnMismatches(){

        ArrayList<int[]> mismatches = new ArrayList<>();

        for (int row = 0; row < userTable.getContent().length; row++) {
            for (int column = 0; column < userTable.getContent()[row].length; column++) {
                if( solutionTable.getPlaceholderColumns().contains( column ) )
                    continue;
                if ( ( userTable.getContent().length >= row +1) && !areCellsEqual( userTable.getContent()[row][column], solutionTable.getContent()[row][column], caseSensitiveColumns.contains( column ) ) )  {
                    mismatches.add(new int[]{ row, column});
                }
            }
        }
        return mismatches;
    }

    /**
     * Get all row column mismatched elements in a given row.
     * @param row row number
     * @return all mismatched elements in the given row
     */
    public List<String> getRowColumnMismatchElementsInRow( int row ){
        String[] tableRow = userTable.getContent()[row];

        String[] solutionRow = solutionTable.getContent()[row];

        ArrayList<String> mismatchElements = new ArrayList<>();

        for(int column = 0; column < tableRow.length; column++){
            if( solutionTable.getPlaceholderColumns().contains( column ) )
                continue;
            if( ( userTable.getContent().length >= row +1) && !areCellsEqual( tableRow[column], solutionRow[column], caseSensitiveColumns.contains( column ) ) )
                mismatchElements.add( tableRow[column] );
        }
        return mismatchElements;
    }

    /**
     * Extract a column from the given table.
     * @param table the table
     * @param column the column number
     * @return the extracted column
     */
    public String[] getColumn(String[][] table, int column){
        return Arrays.stream(table).map( row -> row[column] ).toArray( String[]::new );
    }

    /**
     * Tests, whether two table cells are equal. The cells can contain lists separated by ','.
     * @param cell1 first cell
     * @param cell2 second cell
     * @param caseSensitive  whether this test should be case-sensitive
     * @return true if the cells are equal
     */
    private boolean areCellsEqual( String cell1, String cell2, boolean caseSensitive ){
        ArrayList<String> splitCell1 = new ArrayList<>(Arrays.asList(cell1.split("\\s*,\\s*")));
        ArrayList<String> splitCell2 = new ArrayList<>(Arrays.asList(cell2.split("\\s*,\\s*")));

        if( !caseSensitive ){
            splitCell1 = splitCell1.stream().map( String::toLowerCase ).collect( Collectors.toCollection( ArrayList::new ) );
            splitCell2 = splitCell2.stream().map( String::toLowerCase ).collect( Collectors.toCollection( ArrayList::new ) );
        }

        if( splitCell1.size() != splitCell2.size() )
            return false;
        if( !splitCell1.containsAll( splitCell2 ) || !splitCell2.containsAll( splitCell1 ) )
            return false;

        return true;
    }


    /**
     * Get the difference of two arrays by removing all elements of an array from the other and treating every element as table cells.
     * @param ar1 first array of cells
     * @param ar2 second array of cells
     * @param caseSensitive whether this test should be case-sensitive
     * @return the difference
     */
    private List<String> arrayDifference( String[] ar1, String[] ar2, boolean caseSensitive ){
        ArrayList<String> difference = new ArrayList<>( );

        for(String cell: ar1){
            List<String> elements = Arrays.asList( cell.split("\\s*,\\s*") );
            if( !caseSensitive ){
                elements = elements.stream().map( String::toLowerCase ).collect(Collectors.toList());
            }
            difference.addAll( elements );
        }

        for (String cell: ar2){
            for(String element:  cell.split("\\s*,\\s*") ) {
                if( !caseSensitive ){
                    element = element.toLowerCase();
                }
                difference.remove( element );
            }
        }

        return difference;
    }

}