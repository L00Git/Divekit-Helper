package thkoeln.divekithelper.table.interfaces;

public interface DivekitHelperTableBuilderPostMessage {

    DivekitHelperTableBuilderPostMessage message(int testLevel, String message);


    DivekitHelperTableBuilderPostTable combine();


    boolean test(String testName, String testCategory);
}
