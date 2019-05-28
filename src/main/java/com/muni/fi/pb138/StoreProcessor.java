package com.muni.fi.pb138;

import net.xqj.basex.BaseXXQDataSource;
import net.xqj.basex.bin.L;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Jakub Petras
 * 2019-05-15
 */
public class StoreProcessor implements Processor {
    private XQDataSource xqDataSource = new BaseXXQDataSource();
    private XQConnection connection;
    private static final String xsdPath = "europass-xml-schema-definition-v3/EuropassSchema_V3.0.xsd";

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

    private void executeXQuery(String path) throws XQException {
        String xquery = "insert node for $xmlFile in doc(\"" + path + "\") return $xmlFile into /europasses";
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

    @Override
    public void process(String[] args) {
        setUpDB();
        try {
            for (int i = 0; i < args.length; i++) {
                //TODO validateXML(args[i]);
                executeXQuery(args[i]);
            }
            connection.close();
        } catch (XQException e) {
            System.err.println("Cannot save file to DB");
            e.printStackTrace();
            System.exit(2);
        }
    }

}
