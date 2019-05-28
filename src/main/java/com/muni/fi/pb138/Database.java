package com.muni.fi.pb138;

import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-28.
 */
public interface Database {

    /**
     *
     * @return All CvEntries in database
     */
    List<CvEntry> getAllCvEntries();

    /**
     *
     * @param names names of requested CVs without .xml suffix
     * @return CvEntries for names
     */
    List<CvEntry> getCvEntries(String[] names);

}
