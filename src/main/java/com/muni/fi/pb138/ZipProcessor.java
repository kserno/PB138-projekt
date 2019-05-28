package com.muni.fi.pb138;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

        CommandLineParser parser = new DefaultParser();

        String zipFileName = "result.zip";
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("o")) {
                zipFileName = cmd.getOptionValue("o");
            } else {
                System.out.println("Using default archiveName result.zip");
            }
            System.out.println(cmd.getArgList().toString());
            zipFiles(zipFileName, cmd.getArgList().toArray(new String[0]));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Incorrect input!");
        }


    }

    private void zipFiles(String archiveName, List<CvEntry> entries) {
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
            FileOutputStream fos = new FileOutputStream(archiveName);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (CvEntry entry: entries) {

                File fileToZip = new File(archiveName);
                FileInputStream fis = new FileInputStream(fileToZip);

                ZipEntry zipEntry = new ZipEntry(entry.getName() + ".xml");
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];

                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
