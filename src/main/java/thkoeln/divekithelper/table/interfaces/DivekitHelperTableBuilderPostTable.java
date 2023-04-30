package thkoeln.divekithelper.table.interfaces;


public interface DivekitHelperTableBuilderPostTable {
    DivekitHelperTableBuilderPostRowColumnMismatch rowColumnMismatch();

    DivekitHelperTableBuilderPostColumn column(String columnName);

    DivekitHelperTableBuilderPostColumn column(int columnNumber);

    DivekitHelperTableBuilderPostWrongColumn wrongColumn(String expectedColumnName, String actualColumnName);

    DivekitHelperTableBuilderPostWrongColumn wrongColumn(int expectedColumn, int actualColumn);

}
