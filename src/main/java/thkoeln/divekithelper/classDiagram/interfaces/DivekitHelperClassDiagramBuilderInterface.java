package thkoeln.divekithelper.classDiagram.interfaces;

public interface DivekitHelperClassDiagramBuilderInterface {
    DivekitHelperClassDiagramBuilderInterface message(int testLevel, String message );
    DivekitHelperClassDiagramBuilderPostClassDiagram combine();
    boolean test (String testName, String testCategory );
}

