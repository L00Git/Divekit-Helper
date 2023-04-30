package thkoeln.divekithelper.mock.implementations;

import thkoeln.divekithelper.classDiagram.ClassDiagramTestInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MockClassDiagramTest implements ClassDiagramTestInterface {
    private List<String> missingClasses = new ArrayList<>( Arrays.asList("Room", "Wall") );
    private List<String> missingRelations = new ArrayList<>( Arrays.asList("Connections-Room", "Room-Wall") );

    private List<String> mismatch = new ArrayList<>( Arrays.asList( "Connection") );

    private List<String> wrongRelations =new ArrayList<>( Arrays.asList("Wall-Connection") );

    private List<String> illegalElements =new ArrayList<>( Arrays.asList("§") );

    String classDiagrammName;

    public MockClassDiagramTest (String classDiagrammName ){
        this.classDiagrammName = classDiagrammName;
    }

    public List<String> getMissingClasses(){
        switch (classDiagrammName) {
            case "1":
                return missingClasses;
            case "2":
                return new ArrayList<>();
            default:
                throw new IllegalStateException("Unexpected value: " + classDiagrammName);
        }
    }
    public List<String> getMissingRelations(){
        switch (classDiagrammName) {
            case "1":
                return missingRelations;
            case "2":
                return new ArrayList<>();
            default:
                throw new IllegalStateException("Unexpected value: " + classDiagrammName);
        }
    }

    public List<String> getMismatch(){
        switch (classDiagrammName) {
            case "1":
                return mismatch;
            case "2":
                return new ArrayList<>();
            default:
                throw new IllegalStateException("Unexpected value: " + classDiagrammName);
        }
    }

    public List<String> getWrongRelations(){
        switch (classDiagrammName) {
            case "1":
                return wrongRelations;
            case "2":
                return new ArrayList<>();
            default:
                throw new IllegalStateException("Unexpected value: " + classDiagrammName);
        }
    }

    public List<String> getIllegalElements(){
        switch (classDiagrammName) {
            case "1":
                return illegalElements;
            case "2":
                return new ArrayList<>();
            default:
                throw new IllegalStateException("Unexpected value: " + classDiagrammName);
        }
    }

}
