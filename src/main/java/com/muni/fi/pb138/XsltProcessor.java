package com.muni.fi.pb138;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-28.
 */
public class XsltProcessor implements Processor {

    @Override
    public void process(String[] args) {
        Options options = new Options();

        options.addOption("o", "output", true, "Path to output file.");
        options.addRequiredOption("x", "xsl", true, "Path to xsl file");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            //cmd.
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
