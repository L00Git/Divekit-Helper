package thkoeln.divekithelper.common.testlevel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A Class representation of the testLevelConfiguration.
 */
public class TestLevelConfig {
    @Getter
    @Setter
    private int defaultDelay;
    @Getter
    @Setter
    private int defaultMaxLevel;
    @Getter
    @Setter
    private List<Test> tests;

}
