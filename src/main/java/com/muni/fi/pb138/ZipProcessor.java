package com.muni.fi.pb138;

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


    }

    private void zipFiles(String archiveName, String[] files) {
        File archive = new File(archiveName);
        if (archive.exists()) {
            System.out.println("");
            return;
        }


        try {
            FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : files) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
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
