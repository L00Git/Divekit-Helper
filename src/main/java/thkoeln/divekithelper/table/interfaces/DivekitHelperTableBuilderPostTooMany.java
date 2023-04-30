package thkoeln.divekithelper.table.interfaces;

public interface DivekitHelperTableBuilderPostTooMany {
    DivekitHelperTableBuilderPostMessage message(int testLevel, String message);

    DivekitHelperTableBuilderPostTooManyOr or();
}
