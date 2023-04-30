package thkoeln.divekithelper.table.interfaces;

public interface DivekitHelperTableBuilderPostColumn {


    DivekitHelperTableBuilderPostRowMismatch rowMismatch();

    DivekitHelperTableBuilderPostMissing missing();

    DivekitHelperTableBuilderPostTooMany tooMany();

    DivekitHelperTableBuilderPostCapitalisation capitalisation();
}
