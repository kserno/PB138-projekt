package com.muni.fi.pb138;

import net.xqj.basex.BaseXXQDataSource;

import javax.xml.xquery.*;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Daniel Chorvatovic (469445)
 */
public class QueryProcessor implements Processor {
    private XQDataSource xqDataSource = new BaseXXQDataSource();
    private XQConnection connection;

    private void setUpDB() {
        try {
            Properties properties = new Properties();
            properties.load(Main.class.getResourceAsStream("/dbSetup.properties"));

            xqDataSource.setProperty("databaseName", properties.getProperty("databaseName"));
            xqDataSource.setProperty("serverName", properties.getProperty("serverName"));
            xqDataSource.setProperty("port", properties.getProperty("port"));
            xqDataSource.setProperty("user", properties.getProperty("user"));
            xqDataSource.setProperty("password", properties.getProperty("password"));

            connection = xqDataSource.getConnection(properties.getProperty("user"), properties.getProperty("password"));
        } catch (XQException | IOException e) {
            System.err.println("Can not connect to DB");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void executeQuery(String query) throws XQException {
        XQPreparedExpression expression = connection.prepareExpression(query);
        XQSequence result = expression.executeQuery();
        System.out.println(result.getSequenceAsString(null));
    }


    @Override
    public void process(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Wrong parameter number");
        }

        Properties queries = new Properties();
        try {
            queries.load(Main.class.getResourceAsStream("/queries.properties"));
        } catch (IOException ex) {
            System.err.println("Could not read queries");
            return;
        }
        String query = queries.getProperty(args[0]);
        if (query == null) {
            System.err.println("Select queries from the available options:" + System.lineSeparator() +
                    "all - all cvs" + System.lineSeparator() +
                    "experienced - cvs with work experience" + System.lineSeparator() +
                    "languages - cvs with more than one foreign language" + System.lineSeparator() +
                    "driving - cvs with B driver's licence" + System.lineSeparator() +
                    "rhcsa - cvs with rhcsa certificate" + System.lineSeparator() +
                    "nativeenglish - cvs with native english speakers" + System.lineSeparator() +
                    "foreignenglish - cvs with english as a foreign language" + System.lineSeparator() +
                    "contacts - contacts of all the cvs" + System.lineSeparator());
            return;
        }

        setUpDB();

        try {
            executeQuery(query);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }
}
