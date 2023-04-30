package thkoeln.divekithelper.classDiagram.interfaces;

public interface DivekitHelperClassDiagramBuilderPostClassDiagram {
    DivekitHelperClassDiagramBuilderPostMissingClasses missingClasses();

    DivekitHelperClassDiagramBuilderPostMissingRelations missingRelations();

    DivekitHelperClassDiagramBuilderPostMismatch mismatch();

    DivekitHelperClassDiagramBuilderPostWrongRelations wrongRelations();

    DivekitHelperClassDiagramBuilderPostIllegalElements illegalElements();
}
