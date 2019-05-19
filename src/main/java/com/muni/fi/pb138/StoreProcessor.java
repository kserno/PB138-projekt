package com.muni.fi.pb138;

import net.xqj.basex.BaseXXQDataSource;

import javax.xml.xquery.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Jakub Petras
 * 2019-05-15
 */
public class StoreProcessor implements Processor {

    @Override
    public void process(String[] args) {
        try {
            XQDataSource xqDataSource = new BaseXXQDataSource();
            xqDataSource.setProperty("serverName", "localhost");
            xqDataSource.setProperty("port", "1984");
            xqDataSource.setProperty("user", "admin");
            xqDataSource.setProperty("password", "admin");
            xqDataSource.setProperty("databaseName", "europassDB");


            XQConnection connection = xqDataSource.getConnection("admin", "admin");

            File file = new File(args[0]);
            FileInputStream inputStream = new FileInputStream(file);

            String xquery = "insert node (<europass>{for $xmlFile in doc(\"" + args[0] + "\") return $xmlFile}</europass>) into /europasses";
            XQPreparedExpression expression = connection.prepareExpression(xquery);
            expression.executeQuery();

            connection.close();

        } catch (XQException | IOException e) { 
            e.printStackTrace();
            System.exit(1);
        }

    }
}
