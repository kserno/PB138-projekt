package com.muni.fi.pb138;

import net.xqj.basex.BaseXXQDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xquery.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Jakub Petras
 * 2019-05-15
 */
public class StoreProcessor implements Processor, Database {
    private XQDataSource xqDataSource = new BaseXXQDataSource();
    private XQConnection connection;
    private static final String xsdPath = "europass-xml-schema-definition-v3/EuropassSchema_V3.0.xsd";

    public StoreProcessor() {
        setUpDB();
    }

    private void setUpDB() {
        try {
            Properties properties = new Properties();
            properties.load(Main.class.getResourceAsStream("/dbSetup.properties"));

            xqDataSource.setProperty("serverName", properties.getProperty("serverName"));
            xqDataSource.setProperty("port", properties.getProperty("port"));
            xqDataSource.setProperty("user", properties.getProperty("user"));
            xqDataSource.setProperty("password", properties.getProperty("password"));
            xqDataSource.setProperty("databaseName", properties.getProperty("databaseName"));


            connection = xqDataSource.getConnection("admin", "admin");
        } catch (XQException | IOException e) {
            System.err.println("Can not connect to DB");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void executeInsertXQuery(String path) throws XQException {
        String[] pathSplit = path.split("/");
        String europasssNameXML = pathSplit[pathSplit.length - 1];
        String europasssName = europasssNameXML.substring(0, europasssNameXML.length() - 4);
        String xquery = "insert node (<europass name=\""+ europasssName +"\">{for $xmlFile in doc(\"" + path + "\") return $xmlFile}</europass>) into /europasses";
        XQPreparedExpression expression = connection.prepareExpression(xquery);
        expression.executeQuery();
    }

    private static StreamSource[] generateStreamSourcesFromXsdPaths(
            final String[] xsdFilesPaths) {
        return Arrays.stream(xsdFilesPaths)
                .map(StreamSource::new)
                .collect(Collectors.toList())
                .toArray(new StreamSource[xsdFilesPaths.length]);
    }

    private String[] getAllXsdPaths(String directory, List<String> paths) {
        File[] files = new File(directory).listFiles();
        for(File file : files){
            if(file.isFile()){
                //System.out.println(file.getAbsolutePath());
                paths.add(file.getAbsolutePath());
            } else {
                getAllXsdPaths(file.getAbsolutePath(), paths);
            }
        }
        String[] output = new String[paths.size()];
        return paths.toArray(output);
    }

    private void validateXML(String path) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(path)));
        } catch (IOException | SAXException e) {
            System.err.println("Validation failed! in: " + path);
            e.printStackTrace();
            System.exit(3);
        }
    }

    private List<CvEntry> findByDom(String query, List<String> namesList) throws XQException {
        List<CvEntry> cvEntries = new ArrayList<>();

        XQPreparedExpression expression = connection.prepareExpression(query);
        XQSequence result = expression.executeQuery();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();


            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result.getSequenceAsString(null)));

            Document doc = dbBuilder.parse(is);

            NodeList europassNodes = doc.getElementsByTagName("europass");
            int length = europassNodes.getLength();
            for (int i = 0; i < length; i++) {
                if (europassNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) europassNodes.item(i);
                    if (namesList == null) {
                        CvEntry cvEntry = new CvEntry(element.getAttribute("name"), element.getFirstChild().getNextSibling());
                        cvEntries.add(cvEntry);
                    } else {
                        if (namesList.contains(element.getAttribute("name"))) {
                            CvEntry cvEntry = new CvEntry(element.getAttribute("name"), element.getFirstChild());
                            cvEntries.add(cvEntry);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error with dom model");
            e.printStackTrace();
            System.exit(4);
        }
        return cvEntries;
    }

    @Override
    public List<CvEntry> getAllCvEntries() {
        String xquery = "for $cvs in /europasses return $cvs";
        List<CvEntry> cvEntries = new ArrayList<>();
        try {
            cvEntries = findByDom(xquery, null);
        } catch (XQException e) {
            System.err.println("Can not get all cvs from DB");
            e.printStackTrace();
        }
        return cvEntries;
    }

    @Override
    public List<CvEntry> getCvEntries(String[] names) {
        String xquery = "for $cvs in /europasses return $cvs";
        List<CvEntry> cvEntries = new ArrayList<>();
        List<String> namesList = Arrays.asList(names);
        try {
            cvEntries = findByDom(xquery, namesList);
        } catch (XQException e) {
            System.err.println("Can not get all cvs from DB");
            e.printStackTrace();
        }
        return cvEntries;
    }

    @Override
    public void process(String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                //TODO validateXML(args[i]);
                executeInsertXQuery(args[i]);
            }
            connection.close();
        } catch (XQException e) {
            System.err.println("Cannot save file to DB");
            e.printStackTrace();
            System.exit(2);
        }
    }

}
