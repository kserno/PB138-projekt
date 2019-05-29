package com.muni.fi.pb138;

import org.apache.commons.cli.CommandLine;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-29.
 */
public class Utils {

    public static List<CvEntry> getEntries(CommandLine cmd) {
        List<CvEntry> entries;
        if (cmd.hasOption("a")) {
            entries = Main.getDatabase().getAllCvEntries();
        } else {
            List<String> namesWithoutSuffix = cmd.getArgList().stream().map(s -> s.replace(".xml", "")).collect(Collectors.toList());
            entries = Main.getDatabase().getCvEntries(namesWithoutSuffix.toArray(new String[0]));
        }
        return entries;
    }
}
