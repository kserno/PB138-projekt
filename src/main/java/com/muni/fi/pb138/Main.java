package com.muni.fi.pb138;

import java.util.Arrays;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-10.
 */
public class Main {


    public static void main(String[] args) {

        if (args.length < 1) {
            throw new IllegalArgumentException("No action specified");
        }


        Processor processor = null;
        switch (args[0]) {
            case "store":
                break;
            case "query":
                break;
            case "export_html":
                break;
            case "export_zip":
                processor = new ZipProcessor();
                break;
            default:
                throw new IllegalArgumentException("Unknown action");
        }
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        if (processor != null) {
            processor.process(newArgs);
        }
    }
}
