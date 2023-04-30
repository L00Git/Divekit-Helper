package thkoeln.divekithelper.table.interfaces;

public interface DivekitHelperTableBuilderPostTooManyOr {
    DivekitHelperTableBuilderPostTooManyOrColumn column(String columnName);

    DivekitHelperTableBuilderPostTooManyOrColumn column(int columnNumber);
}
