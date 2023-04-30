package thkoeln.divekithelper.mock.implementations;

import lombok.Getter;
import thkoeln.divekithelper.table.TableTestInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class MockTableTest implements TableTestInterface {

    @Getter
    private String[][] table;

    private String[][] solution;


    @Getter
    private boolean tableValid = true;

    @Getter
    private String[] columnNames;

    private List<Integer> placeholderColumn = new ArrayList<>();

    public MockTableTest( String tablePath, String solutionPath ) {
        this.table = loadTable(tablePath, false);
        this.solution = loadTable(solutionPath, true);
        try{
            if(table.length != solution.length)
                tableValid = false;
            if(table[0].length != solution[0].length)
                tableValid = false;
        }catch (Exception e){
            tableValid = false;
        }
    }

    private String[][] loadTable( String path, boolean isSolution ){
        ArrayList<String[]> table = new ArrayList<>();

        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            long columnCount = 0;
            int lineIndex = 0;

            while ((line = br.readLine()) != null){
                if(line.isBlank())
                    continue;
                lineIndex++;
                if(line.startsWith("|"))
                    line = line.substring(1);
                if(lineIndex == 1) {
                    String[] splitLine = (line.split("\\|"));
                    columnCount = splitLine.length;
                    int splitLineIndex = 0;
                    for(String lineElement: splitLine){
                        splitLine[splitLineIndex++] = lineElement.trim();
                    }
                    columnNames = splitLine;
                    continue;
                }

                if(lineIndex > 2){
                    String[] splitLine = (line.split("\\|"));
                    if( splitLine.length != columnCount ){
                        tableValid = false;
                    }
                    int splitLineIndex = 0;
                    for(String lineElement: splitLine){
                        splitLine[splitLineIndex] = lineElement.trim();
                        if(Objects.equals( lineElement.trim(), "..." ) && isSolution )
                            placeholderColumn.add( splitLineIndex );
                        splitLineIndex++;
                    }
                    table.add(splitLine);
                }
            }

        } catch (Exception e) {
            tableValid = false;
            System.out.println("Couldn't load table from File.");
            e.printStackTrace();
        }

        return table.toArray(String[][]::new);
    }


    public int getColumnNumber( String columnName ){
        int columnNumber = Arrays.asList(columnNames).indexOf(columnName);
        if(columnNumber == -1)
            throw new InputMismatchException("Couldn't find "+ columnName);
        return columnNumber;
    }

    public List<String> getMissingInColumn( int column ){
        if( placeholderColumn.contains( column ) )
            return new ArrayList<>();
        return arrayDifference(getColumn( solution, column ), getColumn( table, column ));
    }

    public List<String> getTooManyInColumn( int column ){
        if( placeholderColumn.contains( column ) )
            return new ArrayList<>();
        return arrayDifference(getColumn(table, column), getColumn(solution, column));
    }

    public List<String> getWrongCapitalisation( int column ){
        ArrayList<String> foundWrongCapitalisation = new ArrayList<>();

        String[] tableColumn = getColumn(table, column);

        String[] solutionColumn = getColumn(solution, column);

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

    public List<Integer> getWrongCapitalisationIndex( int column ){
        ArrayList<Integer> foundWrongCapitalisation = new ArrayList<>();

        String[] tableColumn = getColumn(table, column);

        String[] solutionColumn = getColumn(solution, column);

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

    public List<String> getWrongColumn( int expectedColumn, int actualColumn ){

        HashSet<String> expected = new HashSet<>();

        for (String element: getColumn( solution, expectedColumn )){
            expected.addAll( Arrays.asList( element.split("\\s*,\\s*") ) );
        }

        ArrayList<String> actual = new ArrayList<>();

        for (String element: getColumn( table, actualColumn )){
            actual.addAll( Arrays.asList( element.split("\\s*,\\s*") ) );
        }


        expected.retainAll( actual );


        return new ArrayList<>(expected);
    }
    public List<Integer> getWrongColumnIndex( int expectedColumn, int actualColumn ){

        HashSet<String> expected = new HashSet<>();

        for (String element: getColumn( solution, expectedColumn )){
            expected.addAll( Arrays.asList( element.split("\\s*,\\s*") ) );
        }

        ArrayList<String> actual = new ArrayList<>();

        for (String element: getColumn( table, actualColumn )){
            actual.addAll( Arrays.asList( element.split("\\s*,\\s*") ) );
        }

        expected.retainAll( actual );

        String[] column = getColumn( table, actualColumn );

        ArrayList<Integer> indices = new ArrayList<>();

        for( int row = 0; row < column.length; row++ ){
            List<String> cellElements =  Arrays.asList( column[ row ].split("\\s*,\\s*") );
            if( expected.stream().anyMatch( cellElements::contains ) )
                indices.add( row );
        }


        return indices;
    }

    public List<int[]> getRowMismatches( int idColumn ){
        ArrayList<int[]> rowMismatches = new ArrayList<>();

        for (int row = 0; row < table.length; row++) {
            int finalRow = row;
            Optional<String[]> matchingSolutionRow = Arrays.stream(solution).filter(solutionRow ->  areCellsEqual( table[finalRow][idColumn], solutionRow[idColumn])  ).findFirst();

            if ( matchingSolutionRow.isPresent() )  {
                for( int column = 0; column < matchingSolutionRow.get().length; column++ ) {
                    if( placeholderColumn.contains( column ) )
                        continue;
                    if( !areCellsEqual( table[row][column], matchingSolutionRow.get()[column] ) )
                        rowMismatches.add(new int[]{row, column});
                }
            }
        }
        return rowMismatches;
    }

    public List<String> getRowMismatchesInRow( int idColumn, int row ){
        ArrayList<String> rowMismatchElements = new ArrayList<>();

        Optional<String[]> matchingSolutionRow = Arrays.stream(solution).filter(solutionRow ->  areCellsEqual( table[row][idColumn], solutionRow[idColumn] ) ).findFirst();

        if ( matchingSolutionRow.isPresent() )  {
            for( int column = 0; column < matchingSolutionRow.get().length; column++ ) {
                if( placeholderColumn.contains( column ) )
                    continue;
                if( !areCellsEqual( table[row][column], matchingSolutionRow.get()[column] ) )
                    rowMismatchElements.add( table[row][column] );
            }
        }

        return rowMismatchElements;
    }


    public List<int[]> getRowColumnMismatches(){

        ArrayList<int[]> mismatches = new ArrayList<>();

        for (int row = 0; row < table.length; row++) {
            for (int column = 0; column < table[row].length; column++) {
                if( placeholderColumn.contains( column ) )
                    continue;
                if ( !areCellsEqual( table[row][column], solution[row][column] ) )  {
                    mismatches.add(new int[]{ row, column});
                }
            }
        }
        return mismatches;
    }

    public List<String> getRowColumnMismatchElementsInRow(int row ){
        String[] tableRow = table[row];

        String[] solutionRow = solution[row];

        ArrayList<String> mismatchElements = new ArrayList<>();

        for(int column = 0; column < tableRow.length; column++){
            if( placeholderColumn.contains( column ) )
                continue;
            if( !areCellsEqual( tableRow[column], solutionRow[column] ) )
                mismatchElements.add( tableRow[column] );
        }
        return mismatchElements;
    }

    public String[] getColumn(String[][] table, int column){
        return Arrays.stream(table).map( row -> row[column] ).toArray( String[]::new );
    }

    private boolean areCellsEqual( String cell1, String cell2 ){
        ArrayList<String> splitCell1 = new ArrayList<>();
        ArrayList<String> splitCell2 = new ArrayList<>();
        splitCell1.addAll( Arrays.asList( cell1.split("\\s*,\\s*") ) );
        splitCell2.addAll( Arrays.asList( cell2.split("\\s*,\\s*" ) ) );

        if( splitCell1.size() != splitCell2.size() )
            return false;
        if( !splitCell1.containsAll( splitCell2 ) || !splitCell2.containsAll( splitCell1 ) )
            return false;

        return true;
    }

    /**
     * Get all elements that are in cell1 but not cell2.
     * @param cell1 cell that has excess elements
     * @param cell2 cell that is missing elements of cell1
     * @return all excess elements of cell1 compared to cell2
     */
    private List<String> getCellDifference(String cell1, String cell2){
        List<String> splitCell1 = Arrays.asList( cell1.split("\\s*,\\s*") );
        List<String> splitCell2 = Arrays.asList( cell2.split("\\s*,\\s*") );

        for (String cell: splitCell2){
            splitCell1.remove( cell );
        }
        return splitCell1;
    }

    private List<String> arrayDifference( String[] ar1, String[] ar2){
        ArrayList<String> difference = new ArrayList<>( );


        for(String cell: ar1){
            difference.addAll( Arrays.asList( cell.split("\\s*,\\s*") ) );
        }

        for (String cell: ar2){
            for(String element:  cell.split("\\s*,\\s*") ) {
                difference.remove( element );
            }
        }

        return difference;
    }

}
