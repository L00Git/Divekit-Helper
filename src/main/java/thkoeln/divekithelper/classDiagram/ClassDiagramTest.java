package thkoeln.divekithelper.classDiagram;

import lombok.Getter;
import thkoeln.divekithelper.classDiagram.umlet.UMLClass;
import thkoeln.divekithelper.classDiagram.umlet.UMLRelation;
import thkoeln.divekithelper.classDiagram.umlet.UMLetHelper;
import thkoeln.divekithelper.table.MarkdownTable;
import thkoeln.divekithelper.table.MarkdownTableHelper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A Class that implements class-diagramm tests.
 */
public class ClassDiagramTest{

    private UMLetHelper umletHelper;

    @Getter
    private List<String> glossaryClassNames;

    @Getter
    private boolean testValid;

    /**
     * Create a class-diagramm test on two given diagrams.
     * @param pathUserDiagram path to the user-diagram
     * @param pathSolutionDiagram path to the solution-diagram
     */
    public ClassDiagramTest( String pathUserDiagram, String pathSolutionDiagram ){
        umletHelper = new UMLetHelper( pathUserDiagram, pathSolutionDiagram );
        testValid = umletHelper.isValid();
    }

    /**
     * Get the missing UMLClasses, that are present in the solution but not in the user-diagram.
     * @return the names of missing UMLClasses
     */
    public List<String> getMissingClasses() {
        List<UMLClass> missingClasses = new ArrayList<>( umletHelper.getSolutionClasses() );
        missingClasses.removeAll( umletHelper.getUserClasses() );


        return missingClasses.stream().map( Object::toString ).collect(Collectors.toList());
    }

    /**
     * Get the UMLClasses, that are present but shouldn't be present.
     * @return the names of wrong UMLClasses
     */
    public List<String> getWrongClasses() {
        List<UMLClass> wrongClasses = new ArrayList<>( umletHelper.getUserClasses() );
        wrongClasses.removeAll( umletHelper.getSolutionClasses() );


        return wrongClasses.stream().map( Object::toString ).collect(Collectors.toList());
    }

    /**
     * Get all UMLClasses, that have wrong attributes.
     * @return the names of UMLCLasses with wrong attributes
     */
    public List<String> getClassesWithWrongAttributes() {
        List<String> classesWithWrongAttributes = new ArrayList<>();

        for ( UMLClass userClass: umletHelper.getUserClasses() ){
            Optional<UMLClass> solutionCounterpart = umletHelper.getSolutionClasses().stream().filter(solutionClass -> solutionClass.equals( userClass ) ).findFirst();

            if( solutionCounterpart.isPresent() && !areListsEqual( solutionCounterpart.get().getAttributes(),  userClass.getAttributes() ) ){
                classesWithWrongAttributes.add( userClass.getName() );
            }

        }

        return classesWithWrongAttributes;
    }

    /**
     * Get all UMLRelations that are missing.
     * @return the names of missing UMLRelations
     */
    public List<String> getMissingRelations() {
        List<String>  missingRelations = new ArrayList<>();

        for ( UMLRelation solutionRelation: umletHelper.getSolutionRelations() ){
            if( getRelationCounterpart( solutionRelation, umletHelper.getUserRelations() ).size() == 0 ){
                missingRelations.add( solutionRelation.getStartClass().getName() + "-" + solutionRelation.getEndClass().getName() );
            }
        }
        return missingRelations;
    }


    /**
     * Get all UMLRelations, that are either unnecessary or have wrong attributes.
     * @return the names of wrong UMLRelations
     */
    public List<String> getWrongRelations() {
        List<String>  wrongRelations = new ArrayList<>();

        for ( UMLRelation userRelation: umletHelper.getUserRelations() ){
            List<UMLRelation> solutionRelations = getRelationCounterpart( userRelation, umletHelper.getSolutionRelations() );

            boolean foundRelation = false;

            for( UMLRelation solutionRelation: solutionRelations ){
                if( areRelationsEqual( userRelation, solutionRelation ) ){
                    foundRelation = true;
                }
            }

            if( !foundRelation ){
                String startClass = userRelation.getStartClass() == null ? "No Connected Class Found" : userRelation.getStartClass().getName();
                String endClass = userRelation.getEndClass() == null ? "No Connected Class Found" : userRelation.getEndClass().getName();
                wrongRelations.add( startClass + "-" + endClass );
            }

        }

        return wrongRelations;
    }

    /**
     * Get the UMLClass-names that are present in the glossary.
     * @param pathGlossary path to the glossary
     * @param columnOfClassNames column-name containing the UMLClass-names
     */
    public void setGlossary( String pathGlossary, String columnOfClassNames ){
        MarkdownTable glossaryTable = MarkdownTableHelper.loadTable( pathGlossary, false );

        if( !glossaryTable.isValid() ){
            testValid = false;
            return;
        }


        int columnNumber = Arrays.asList( glossaryTable.getColumnNames() ).indexOf( columnOfClassNames );
        if(columnNumber == -1){
            testValid = false;
            return;
        }

        glossaryClassNames = List.of( Arrays.stream(glossaryTable.getContent()).map(row -> row[columnNumber]).toArray(String[]::new) );
        glossaryClassNames = glossaryClassNames.stream().map(String::toLowerCase).collect(Collectors.toList());
    }


    /**
     * Get all UMLClasses that are present in only the glossary or the user-diagram but not both.
     * @return the names of UMLCLasses that are mismatched
     */
    public List<String> getMismatch() {
        List<String> mismatches = new ArrayList<>();

        List<String> classNames = umletHelper.getUserClasses().stream().map( Object::toString ).map( String::toLowerCase ).collect(Collectors.toList());

        List<String> glossaryMismatches = new ArrayList<>( glossaryClassNames );
        glossaryMismatches.removeAll( classNames );

        List<String> diagramMismatches = new ArrayList<>( classNames );
        diagramMismatches.removeAll( glossaryClassNames );



        mismatches.addAll( glossaryMismatches );
        mismatches.addAll( diagramMismatches );

        return mismatches;
    }

    /**
     * Get all UMLElements that are not part of the allowed Elements.
     * @return all names of the illegal UMLElements
     */
    public List<String> getIllegalElements() {
        return umletHelper.getIllegalElements();
    }

    /**
     * Get the UMLRelation counterpart of a given UMLRelation, by matching the two UMLCLasses they connect.
     * @param umlRelation the UMLRelation that is given
     * @param possibleCounterparts a list of UMLRelations that could match the given UMLRelation
     * @return the UMLRelation counterpart of the given UMLRelation, can be multiple if more that one matches or an empty list of none matches
     */
    private List<UMLRelation> getRelationCounterpart( UMLRelation umlRelation, List<UMLRelation> possibleCounterparts ){
        if( umlRelation.getStartClass() == null || umlRelation.getEndClass() == null )
            return new ArrayList<>();

        return  possibleCounterparts.stream().filter( possibleCounterpart ->
                        ( umlRelation.getStartClass().equals( possibleCounterpart.getStartClass() ) && umlRelation.getEndClass().equals( possibleCounterpart.getEndClass() ))
                        || ( umlRelation.getStartClass().equals( possibleCounterpart.getEndClass() ) && umlRelation.getEndClass().equals( possibleCounterpart.getStartClass() ) ) )
                .collect(Collectors.toList());
    }

    /**
     * Checks, whether the two UMLRelations have the same attributes. Ignores the attributes, if the solution UMLRelation is dotted.
     * @param userRelation the user-UMLRelations
     * @param solutionRelation the solution-UMLRelations
     * @return true if they have the same attribute oder the solution is dotted, false otherwise
     */
    private boolean areRelationsEqual( UMLRelation  userRelation, UMLRelation solutionRelation ){

        UMLRelation.LineType userLT = userRelation.getLineType();
        UMLRelation.LineType solutionLT = solutionRelation.getLineType();

        UMLClass userStartClass = userRelation.getStartClass();

        UMLClass solutionStartClass = solutionRelation.getStartClass();

        String userStartMulti = userRelation.getStartMultiplicity();
        String userEndMulti = userRelation.getEndMultiplicity();

        String solutionStartMulti = solutionRelation.getStartMultiplicity();
        String solutionEndMulti = solutionRelation.getEndMultiplicity();

        UMLRelation.RelationType userStartRT = userRelation.getStartRelationType();
        UMLRelation.RelationType userEndRT = userRelation.getEndRelationType();

        UMLRelation.RelationType solutionStartRT = solutionRelation.getStartRelationType();
        UMLRelation.RelationType solutionEndRT = solutionRelation.getEndRelationType();

        if( solutionLT == UMLRelation.LineType.DOTTED ){
            return true;
        }
        if( userLT != solutionLT ){
            return false;
        }
        if( userStartClass.equals( solutionStartClass) ){
            if( userStartRT != solutionStartRT || userEndRT != solutionEndRT ){
                return false;
            }
            if(solutionStartMulti != null && userStartMulti == null || solutionStartMulti != null && !userStartMulti.equals(solutionStartMulti) ){
                return false;
            }
            if( solutionEndMulti != null && userEndMulti == null || solutionEndMulti != null && !userEndMulti.equals( solutionEndMulti ) ){
                return false;
            }

        }else {
            if( userStartRT != solutionEndRT || userEndRT != solutionStartRT ){
                return false;
            }
            if( solutionStartMulti != null && userEndMulti == null || solutionStartMulti != null && !userEndMulti.equals( solutionStartMulti ) ){
                return false;
            }
            if( solutionEndMulti != null && userStartMulti == null || solutionEndMulti != null &&  !userStartMulti.equals( solutionEndMulti ) ){
                return false;
            }
        }
        return true;
    }

    /**
     * Tests, whether two lists are equal.
     * @param first first list
     * @param second second list
     * @return true if they are equal
     */
    private boolean areListsEqual( List<String> first, List<String> second ){
        return first.size() == second.size() && first.containsAll( second ) && second.containsAll( first );
    }

}
