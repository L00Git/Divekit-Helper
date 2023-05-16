package thkoeln.divekithelper.table;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A Class representation of a Markdown-Table.
 */
public class MarkdownTable {
    @Getter
    @Setter
    private String[] columnNames;

    @Getter
    @Setter
    private String[][] content;

    @Getter
    private List<Integer> placeholderColumns = new ArrayList<>();

    @Getter
    @Setter
    private boolean isValid;
}
