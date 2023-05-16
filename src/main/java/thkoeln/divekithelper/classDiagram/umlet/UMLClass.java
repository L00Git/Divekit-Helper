package thkoeln.divekithelper.classDiagram.umlet;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * A Class representation of an UMLClass.
 */
public class UMLClass {
    @Getter
    private float xStart;
    @Getter
    private float yStart;
    @Getter
    private float xEnd;
    @Getter
    private float yEnd;
    @Getter
    private String name;
    @Getter
    private List<String> attributes = new ArrayList<>();


    /**
     * Create a UMLClass
     * @param x its x-axis position
     * @param y its y-axis position
     * @param w its width
     * @param h its height
     */
    public UMLClass( float x, float y, float w, float h ){
        xStart = x;
        yStart = y;
        xEnd = x + w;
        yEnd = y + h;
    }

    /**
     * Sets the Name and Attributes.
     * @param panelAttribute the attribute containing the necessary information
     */
    public void setNameAndAttributes(String panelAttribute ) {
        String[] splitAttributes = panelAttribute.split("-+");

        name = splitAttributes[0].trim();

        if( splitAttributes.length > 1 ){
            attributes =  List.of( splitAttributes[1].trim().split("\n") );
        }
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals( Object object ){
        if( !(object instanceof UMLClass) ){
            return false;
        }
        return ( (UMLClass) object).name.equalsIgnoreCase(name);
    }


}
