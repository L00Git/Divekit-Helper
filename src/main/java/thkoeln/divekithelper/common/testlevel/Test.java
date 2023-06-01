package thkoeln.divekithelper.common.testlevel;

import lombok.Getter;
import lombok.Setter;

/**
 * A Class representation of a Test in the testLevelConfiguration.
 */
public class Test {
    @Getter
    @Setter
    private String testName;
    @Getter
    @Setter
    private String path;
    @Getter
    @Setter
    private int delay;

    @Getter
    @Setter
    private int maxLevel;
}
