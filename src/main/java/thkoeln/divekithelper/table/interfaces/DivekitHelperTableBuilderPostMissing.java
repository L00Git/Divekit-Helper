package thkoeln.divekithelper.table.interfaces;

public interface DivekitHelperTableBuilderPostMissing {

    DivekitHelperTableBuilderPostMessage message(int testLevel, String message);

    DivekitHelperTableBuilderPostMissingOr or();
}
