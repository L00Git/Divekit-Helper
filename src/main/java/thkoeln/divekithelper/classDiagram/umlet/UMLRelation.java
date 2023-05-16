package thkoeln.divekithelper.classDiagram.umlet;

import lombok.Getter;
import lombok.Setter;

import java.util.InputMismatchException;

/**
 * A Class representation of an UMLRelation.
 */
public class UMLRelation {
    @Getter
    private float xStartPoint;
    @Getter
    private float yStartPoint;
    @Getter
    private float xEndPoint;
    @Getter
    private float yEndPoint;
    @Getter
    @Setter
    private UMLClass startClass;
    @Getter
    @Setter
    private UMLClass endClass;

    public enum RelationType{
        NONE,
        SIMPLE_ARROW,
        HOLLOW_ARROW,
        FULL_ARROW,
        HOLLOW_DIAMOND,
        FULL_DIAMOND
    }

    @Getter
    private RelationType startRelationType;
    @Getter
    private RelationType endRelationType;
    @Getter
    @Setter
    private String startMultiplicity;
    @Getter
    @Setter
    private String endMultiplicity;


    public enum LineType {
        LINE,
        DASHED,
        DOTTED
    }

    @Getter
    private LineType lineType;


    /**
     * Create a UMLRelation
     * @param xPos its x position
     * @param yPos its y position
     * @param xToStart its x distance to the start-point
     * @param yToStart its y distance to the start-point
     * @param xToEnd its x distance to the end-point
     * @param yToEnd its y distance to the end-point
     * @param zoomLevel the zoom-level of the UML-Diagram
     */
    public UMLRelation( float xPos, float yPos, float xToStart, float yToStart, float xToEnd, float yToEnd, float zoomLevel ){
        xStartPoint = xPos + (xToStart * zoomLevel / 10);
        yStartPoint = yPos + (yToStart * zoomLevel / 10);
        xEndPoint = xPos + (xToEnd * zoomLevel / 10);
        yEndPoint = yPos + (yToEnd * zoomLevel / 10);
    }

    /**
     * Set the Types of this UMLRelation
     * @param panelAttribute the attribute containing the necessary information
     */
    public void setTypes( String panelAttribute ) {
        startRelationType = mapRelation((int) panelAttribute.chars().filter(ch -> ch == '<' ).count());
        endRelationType = mapRelation((int) panelAttribute.chars().filter(ch -> ch == '>' ).count());
        lineType = mapLineType((int) panelAttribute.chars().filter(ch -> ch == '.' ).count());
    }


    /**
     * Map the number of '<' or '>' occurrences to their relation-type.
     * @param occurrences the number of character occurrences
     * @return the corresponding relation-tpye
     */
    private RelationType mapRelation( int occurrences ){
        switch ( occurrences ){
            case 0:
                return RelationType.NONE;
            case 1:
                return RelationType.SIMPLE_ARROW;
            case 2:
                return RelationType.HOLLOW_ARROW;
            case 3:
                return RelationType.FULL_ARROW;
            case 4:
                return RelationType.HOLLOW_DIAMOND;
            case 5:
                return RelationType.FULL_DIAMOND;
            default:
                throw new InputMismatchException( "Can't recognize LineType" );
        }
    }

    /**
     * Map the number of '.' occurrences to their line-type.
     * @param occurrences the number of '.' occurrences
     * @return the corresponding line-type
     */
    private LineType mapLineType( int occurrences ){
        switch ( occurrences ){
            case 0:
                return LineType.LINE;
            case 1:
                return LineType.DASHED;
            case 2:
                return LineType.DOTTED;
            default:
                throw new InputMismatchException( "Can't recognize LineType" );
        }
    }

}
