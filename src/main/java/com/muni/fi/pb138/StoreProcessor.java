package com.muni.fi.pb138;

import net.xqj.basex.BaseXXQDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xquery.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Jakub Petras
 * 2019-05-15
 */
public class StoreProcessor implements Processor, Database {
    private XQDataSource xqDataSource = new BaseXXQDataSource();
    private XQConnection connection;

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

    private String editXMLName(String path) {
        File file = new File(path);
        return file.getName();
    }

    private void executeInsertXQuery(String path) throws XQException {
        String europassName = editXMLName(path);
        String[] europass = new String[1];
        europass[0] = europassName;
        if (getCvEntries(europass).size() != 0) {
            System.err.println("Already exists europass with name: " + europassName + " path: " + path + " in europassDB");
            return;
        }
        String xquery = "insert node (<europass name=\""+ europassName +"\">{for $xmlFile in doc(\"" + path + "\") return $xmlFile}</europass>) into /europasses";
        XQPreparedExpression expression = connection.prepareExpression(xquery);
        expression.executeQuery();
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
                executeInsertXQuery(args[i]);
            }
            List<CvEntry> cvEntries = getAllCvEntries();
            System.out.println(cvEntries.size());
            connection.close();
        } catch (XQException e) {
            System.err.println("Cannot save file to DB");
            e.printStackTrace();
            System.exit(2);
        }
    }

}
