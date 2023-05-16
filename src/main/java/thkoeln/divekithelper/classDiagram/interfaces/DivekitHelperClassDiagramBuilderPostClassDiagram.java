package thkoeln.divekithelper.classDiagram.interfaces;

public interface DivekitHelperClassDiagramBuilderPostClassDiagram {
    DivekitHelperClassDiagramBuilderPostMissingClasses missingClasses();

    DivekitHelperClassDiagramBuilderPostWrongClasses wrongClasses();

    DivekitHelperClassDiagramBuilderPostClassWrongAttributes classWrongAttributes();

    DivekitHelperClassDiagramBuilderPostMissingRelations missingRelations();

    DivekitHelperClassDiagramBuilderPostMismatch mismatch( String glossaryPath, String columnOfClassNames );

    DivekitHelperClassDiagramBuilderPostWrongRelations wrongRelations();

    DivekitHelperClassDiagramBuilderPostIllegalElements illegalElements();


}
