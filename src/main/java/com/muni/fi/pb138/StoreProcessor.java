package com.muni.fi.pb138;

import net.xqj.basex.BaseXXQDataSource;
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

/**
 * @author Jakub Petras
 * 2019-05-15
 */
public class StoreProcessor implements Processor {
    private XQDataSource xqDataSource = new BaseXXQDataSource();
    private XQConnection connection;
    private static final String xsdPath = "/Users/Jakub2801/Documents/MojePrace/MU/2019-jar/" +
                                        "PB138/EuropassProject/PB138-projekt/src/main/resources/" +
                                        "europass-xml-schema-definition-v3/EuropassSchema.xsd";

    private void setUpDB() {
        try {
            xqDataSource.setProperty("serverName", "localhost");
            xqDataSource.setProperty("port", "1984");
            xqDataSource.setProperty("user", "admin");
            xqDataSource.setProperty("password", "admin");
            xqDataSource.setProperty("databaseName", "europassDB");


            connection = xqDataSource.getConnection("admin", "admin");
        } catch (XQException e) {
            System.err.println("Can not connect to DB");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void executeXQuery(String path) throws XQException {
        String xquery = "insert node (<europass>{for $xmlFile in doc(\"" + path + "\") return $xmlFile}</europass>) into /europasses";
        XQPreparedExpression expression = connection.prepareExpression(xquery);
        expression.executeQuery();
    }

    private void validateXML(String path, int i) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(path)));
        } catch (IOException | SAXException e) {
            System.err.println("Validation failed! " + i);
            e.printStackTrace();
            System.exit(3);
        }
    }

    @Override
    public void process(String[] args) {
        setUpDB();
        try {
            for (int i = 0; i < args.length; i++) {
                validateXML(args[i], i);
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
