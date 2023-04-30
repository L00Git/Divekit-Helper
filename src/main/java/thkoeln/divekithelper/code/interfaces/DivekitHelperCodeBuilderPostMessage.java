package thkoeln.divekithelper.code.interfaces;

public interface DivekitHelperCodeBuilderPostMessage {
    DivekitHelperCodeBuilderPostMessage message(int testLevel, String message);

    boolean test(String testName, String testCategory);
}
