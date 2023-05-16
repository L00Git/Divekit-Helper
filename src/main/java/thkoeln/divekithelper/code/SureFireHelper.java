package thkoeln.divekithelper.code;


import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SureFireHelper {


    /**
     * Scans the surefire folder for all XML-Files.
     * @return a List of all XML-Files in the surefire folder
     */
    public List<File> getAllSurefireFiles(){
        try(Stream<Path> stream = Files.list( Path.of("target/surefire-reports" ))) {
            return stream.filter( file -> file.getFileName().toString().endsWith(".xml") ).map( Path::toFile ).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't load surefire-reports.");
            return new ArrayList<>();
        }
    }

    /**
     * Extracts all stacktraces and their corresponding test names in a given file.
     * @param file the file that all stacktraces should be extracted from
     * @return a List of Arrays containing all stacktraces and their test name in the given file
     *         each Array is of size 2
     *         the 0 element contains the stacktrace while the 1 Element contains the test name
     */
    public List<String[]> getStackTracesFromXML(File file){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse( file );
            NodeList nodeList = document.getElementsByTagName("error");
            List<String[]> errorStackTraceList = new ArrayList<>();
            for( int i = 0; i  < nodeList.getLength(); i++ ){
                Node node = nodeList.item( i );
                String testName = node.getParentNode().getAttributes().getNamedItem("name").getNodeValue();
                errorStackTraceList.add( new String[]{node.getTextContent(), testName } );
            }
            return errorStackTraceList;
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "Couldn't`t load " + file.getName() + " StackTrace." );
        }
        return new ArrayList<>();
    }
}
