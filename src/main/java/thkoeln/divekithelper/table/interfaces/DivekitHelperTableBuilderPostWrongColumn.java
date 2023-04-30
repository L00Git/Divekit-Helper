package thkoeln.divekithelper.table.interfaces;

public interface DivekitHelperTableBuilderPostWrongColumn {
    DivekitHelperTableBuilderPostMessage message(int testLevel, String message);

    DivekitHelperTableBuilderPostWrongColumnOr or();
}
