package thkoeln.divekithelper.table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * This Class generates a Java-Representation of a Markdown-Table.
 */
public class MarkdownTableHelper {

    /**
     * Load the Markdown-Table by generating the corresponding Class representation.
     * @param path path to the Markdown-Table
     * @param isSolution whether the Markdown-Table contains the solution
     * @return a Markdown-Table class
     */
    public static MarkdownTable loadTable( String path, boolean isSolution ){
        ArrayList<String[]> tableContent = new ArrayList<>();

        MarkdownTable markdownTable = new MarkdownTable();

        markdownTable.setValid( true );

        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            long columnCount = 0;
            int lineIndex = 0;

            while ((line = br.readLine()) != null){
                if(line.isBlank())
                    continue;
                lineIndex++;
                line = line.trim();
                if(line.startsWith("|"))
                    line = line.substring(1);
                if(lineIndex == 1) {
                    String[] splitLine = (line.split("\\|"));
                    columnCount = splitLine.length;
                    int splitLineIndex = 0;
                    for(String lineElement: splitLine){
                        splitLine[splitLineIndex++] = lineElement.trim();
                    }
                    markdownTable.setColumnNames( splitLine );
                    continue;
                }

                if(lineIndex > 2){
                    String[] splitLine = (line.split("\\|"));
                    if( splitLine.length != columnCount ){
                        markdownTable.setValid( false );
                    }
                    int splitLineIndex = 0;
                    for(String lineElement: splitLine){
                        splitLine[splitLineIndex] = lineElement.trim();
                        if(lineElement.trim().equals( "..." ) && isSolution )
                            markdownTable.getPlaceholderColumns().add( splitLineIndex );
                        splitLineIndex++;
                    }
                    tableContent.add(splitLine);
                }
            }

        } catch (Exception e) {
            System.out.println("Couldn't load table from file.");
            markdownTable.setValid( false );
            return markdownTable;
        }
        markdownTable.setContent( tableContent.toArray(String[][]::new) );
        return markdownTable;
    }

    /**
     * Tests, whether tow Markdown-Tables can be compared.
     * @param firstTable first Markdown-Table
     * @param secondTable second Markdown-Table
     * @return true if they are comparable
     */
    public static boolean areTablesComparable( MarkdownTable firstTable, MarkdownTable secondTable ){
        return firstTable.getContent()[0].length == secondTable.getContent()[0].length;
    }

}
