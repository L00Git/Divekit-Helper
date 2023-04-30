package thkoeln.divekithelper.common;

public interface CommitFrequencyInterface {

    /**
     * Get the Test-Level for a given Test. The Test-Level represents the amount of help given with 1 being the lowest.
     * @param testName a String identifying the Test
     * @return an int value of the Test Level
     */
    int getTestLevel(String testName);

}
