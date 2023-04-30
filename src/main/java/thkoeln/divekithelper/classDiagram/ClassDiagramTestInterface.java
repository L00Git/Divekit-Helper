package thkoeln.divekithelper.classDiagram;

import java.util.List;

/**
 * This interface defines all methods that a ClassDiagramTest needs, in order to be compatible With the DivekitHelper.
 */
public interface ClassDiagramTestInterface {
    /**
     * Get all missing classes.
     * @return a List of all missing classes
     */
    List<String> getMissingClasses();

    /**
     * Get all missing relations.
     * @return a List of all missing relations
     */
    List<String> getMissingRelations();

    /**
     * Get all mismatches between the glossary and diagram.
     * @return a List of all mismatches
     */
    List<String> getMismatch();

    /**
     * Get all relations that are present but should not exist.
     * @return a List of all wrong Relations
     */
    List<String> getWrongRelations();

    /**
     * Get all illegal elements, that are not accepted by UMLet.
     * @return a List of all illegal elements
     */
    List<String> getIllegalElements();
}
