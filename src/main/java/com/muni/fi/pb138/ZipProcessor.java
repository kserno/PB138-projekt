package com.muni.fi.pb138;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-10.
 */
public class ZipProcessor implements Processor {
    @Override
    public void process(String[] args) {
        Options options = new Options();

        options.addOption("o", "output", true, "Path to output zip file");
        options.addOption("a", "all", false, "Select whether all or only selected.");

        CommandLineParser parser = new DefaultParser();

        String zipFileName = "result.zip";
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("o")) {
                zipFileName = cmd.getOptionValue("o");
            } else {
                System.out.println("Using default archiveName result.zip");
            }
            List<CvEntry> entries = Utils.getEntries(cmd);

            zipFiles(zipFileName, entries);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Incorrect input!");
        }


    }

    private void zipFiles(String archiveName, List<CvEntry> entries) {
        if (entries.isEmpty()) {
            System.out.println("No entries.");
            return;
        }
        if (!archiveName.endsWith(".zip")) {
            System.out.println("Output file needs to have .zip extension!");
            return;
        }

        File archive = new File(archiveName);

        if (archive.exists()) {
            System.out.println("Archive already exists.");
            return;
        }


        try {
            TransformerFactory tf = TransformerFactoryImpl.newInstance();
            Transformer fileTransformer = tf.newTransformer();

            ZipFile zipFile = new ZipFile(archive);

            for (CvEntry entry: entries) {

                File xmlFile = new File(entry.getName() + ".xml");
                fileTransformer.transform(
                        new DOMSource(entry.getRootNode()),
                        new StreamResult(xmlFile)
                );

                ZipParameters parameters = new ZipParameters();

                parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

                zipFile.addFile(xmlFile, parameters);

                xmlFile.deleteOnExit();
            }

        } catch (TransformerException | ZipException e) {
            e.printStackTrace();
        }
    }
}
