package thkoeln.divekithelper.table.interfaces;


public interface DivekitHelperTableBuilderPostWrongColumnOr {
    DivekitHelperTableBuilderPostWrongColumn wrongColumn( String expectedColumnName, String actualColumnName );

    DivekitHelperTableBuilderPostWrongColumn wrongColumn( int expectedColumn, int actualColumn );
}

