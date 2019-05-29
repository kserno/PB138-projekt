package com.muni.fi.pb138;


import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-28.
 */
public class XsltProcessor implements Processor {

    @Override
    public void process(String[] args) {
        Options options = new Options();

        options.addOption("o", "output", false, "If set outputs to console");
        options.addOption("a", "all", false, "Sets whether to output all");
        options.addRequiredOption("x", "xsl", true, "Path to xsl file");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            boolean output = cmd.hasOption("o");

            List<CvEntry> entries = Utils.getEntries(cmd);

            transform(cmd.getOptionValue("x"), entries, output);

        } catch (ParseException | TransformerException e) {
            e.printStackTrace();
        }

    }

    private void transform(String xslPath, List<CvEntry> entries, boolean output) throws TransformerConfigurationException {


        TransformerFactory tf = TransformerFactoryImpl.newInstance();

        Transformer xsltProc = tf.newTransformer(new StreamSource(new File(xslPath)));
        Transformer fileTransformer = tf.newTransformer();

        entries.forEach(entry -> {
            try {
                StreamResult result;
                if (output) {
                    result = new StreamResult(System.out);
                } else {
                    result = new StreamResult(new File(entry.getName() + ".html"));
                }


                File xmlFile = new File(entry.getName() + ".xml");
                fileTransformer.transform(
                        new DOMSource(entry.getRootNode()),
                        new StreamResult(xmlFile)
                );

                xsltProc.transform(
                        new StreamSource(xmlFile),
                        result
                );

                xmlFile.deleteOnExit();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        });


    }

}
