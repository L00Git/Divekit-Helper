package thkoeln.divekithelper.classDiagram.umlet;

import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/**
 * This Class generates a Java-Representation of a UMLet-files.
 */
public class UMLetHelper {
    @Getter
    private List<UMLClass> userClasses = new ArrayList<>();
    @Getter
    private List<UMLClass> solutionClasses = new ArrayList<>();
    @Getter
    private List<UMLRelation> userRelations = new ArrayList<>();
    @Getter
    private List<UMLRelation> solutionRelations = new ArrayList<>();
    @Getter
    private List<String> illegalElements = new ArrayList<>();

    @Getter
    private boolean isValid = true;

    private final List<String> ALLOWED_UML_ELEMENTS = Arrays.asList( "UMLClass", "Relation", "UMLNote" );

    private final float RELATION_TOLERANCE = 5;

    /**
     * Create a UMLetHelper.
     * @param pathUserDiagram path to the user-diagram
     * @param pathSolutionDiagram path to the solution-diagram
     */
    public UMLetHelper( String pathUserDiagram, String pathSolutionDiagram ){

        processDiagram( pathUserDiagram, userClasses, userRelations );
        processDiagram( pathSolutionDiagram, solutionClasses, solutionRelations );

        addRelationClassConnection( userClasses, userRelations );
        addRelationClassConnection( solutionClasses, solutionRelations );
    }

    /**
     * Creates the UMLClasses and UMLRelations given the diagram-file path
     * @param path path to the diagram
     * @param classes a list that will be filled with the generated classes
     * @param relations a list that will be filled with the generated relations
     */
    private void processDiagram( String path, List<UMLClass> classes, List<UMLRelation> relations){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse( new File( path ) );

            Float zoomLevel = Float.parseFloat( document.getElementsByTagName("zoom_level").item(0).getTextContent() );

            NodeList elements = document.getElementsByTagName("element");


            for( int i = 0; i  < elements.getLength(); i++ ){
                Element element = (Element) elements.item( i );

                String id = element.getElementsByTagName( "id" ).item(0).getTextContent();
                if( !ALLOWED_UML_ELEMENTS.contains( id ) ){
                    illegalElements.add( id );
                }
                if( id.equals("UMLClass") ){
                    classes.add( createUmlClass( element ) );
                }
                if( id.equals("Relation") ){
                    relations.add( createUMLRelation( element, zoomLevel ) );
                }
            }
        } catch (Exception e) {
            isValid = false;
            System.out.println("Couldn't load diagram from file.");
        }
    }


    /**
     * Creates a UMLClass, given a XML-Element.
     * @param element the XML-Element representing the UMLClass
     * @return the generated UMLClass
     */
    private UMLClass createUmlClass( Element element ){
        Element coordinates = (Element) element.getElementsByTagName("coordinates").item(0);
        String x = coordinates.getElementsByTagName( "x" ).item(0).getTextContent();
        String y = coordinates.getElementsByTagName( "y" ).item(0).getTextContent();
        String w = coordinates.getElementsByTagName( "w" ).item(0).getTextContent();
        String h = coordinates.getElementsByTagName( "h" ).item(0).getTextContent();

        UMLClass newUMLClass = new UMLClass( Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(w), Float.parseFloat(h) );
        newUMLClass.setNameAndAttributes( element.getElementsByTagName( "panel_attributes" ).item(0).getTextContent() );

        return newUMLClass;
    }

    /**
     * Creates a UMLRelation, given a XML-Element.
     * @param element the XML-Element representing the UMLRelation
     * @param zoomLevel the zoom-level of the diagram
     * @return the generated UMLRelation
     */
    private UMLRelation createUMLRelation( Element element, Float zoomLevel ){
        Element coordinates = (Element) element.getElementsByTagName("coordinates").item(0);
        String x = coordinates.getElementsByTagName( "x" ).item(0).getTextContent();
        String y = coordinates.getElementsByTagName( "y" ).item(0).getTextContent();

        String[] additionalAttributes = element.getElementsByTagName( "additional_attributes" ).item(0).getTextContent().split(";");


        UMLRelation newUMLRelation = new UMLRelation( Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(additionalAttributes[0]), Float.parseFloat(additionalAttributes[1]), Float.parseFloat(additionalAttributes[additionalAttributes.length-2]), Float.parseFloat(additionalAttributes[additionalAttributes.length-1]), zoomLevel );

        String[] panelAttributes = element.getElementsByTagName("panel_attributes").item(0).getTextContent().split("\n");

        Optional<String> lineType = Arrays.stream(panelAttributes).filter(line -> line.startsWith("lt=") ).findFirst();

        Optional<String> startMultiplicity = Arrays.stream(panelAttributes).filter(line -> line.startsWith("m1=") ).findFirst();

        Optional<String> endMultiplicity = Arrays.stream(panelAttributes).filter(line -> line.startsWith("m2=") ).findFirst();

        if( lineType.isEmpty() ) {
            newUMLRelation.setTypes( "" );
        }
        else {
            newUMLRelation.setTypes( lineType.get().substring(3) );
        }
        startMultiplicity.ifPresent( newUMLRelation::setStartMultiplicity );
        endMultiplicity.ifPresent( newUMLRelation::setEndMultiplicity );

        return newUMLRelation;
    }

    /**
     * Adds the UMLClasses that UMLRelations connect.
     * @param umlClasses all possible UMLClasses that could be connected by the given UMLRelation
     * @param umlRelations the UMLRelations that should have their connected UMLClasses added
     */
    private void addRelationClassConnection( List<UMLClass> umlClasses, List<UMLRelation> umlRelations ){
        for( UMLRelation umlRelation: umlRelations ){
            for ( UMLClass umlClass: umlClasses){
                if( distance(  umlClass, umlRelation.getXStartPoint(), umlRelation.getYStartPoint()) < RELATION_TOLERANCE ){
                    umlRelation.setStartClass( umlClass );
                }
                if( distance(  umlClass, umlRelation.getXEndPoint(), umlRelation.getYEndPoint()) < RELATION_TOLERANCE ){
                    umlRelation.setEndClass( umlClass );
                }
            }
        }
    }

    /**
     * Calculates the distance of a point to a UMLClass.
     * @param umlClass the UMLClass
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return the distance
     */
    private float distance( UMLClass umlClass, float x, float y){
        float xDist = Math.max( umlClass.getXStart() - x, x - umlClass.getXEnd() );
        xDist = Math.max( xDist, 0 );

        float yDist = Math.max( umlClass.getYStart() - y, y - umlClass.getYEnd() );
        yDist = Math.max( yDist, 0 );

        return (float) Math.sqrt( xDist*xDist + yDist*yDist );
    }
}
