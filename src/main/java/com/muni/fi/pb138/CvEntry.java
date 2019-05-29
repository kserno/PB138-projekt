package com.muni.fi.pb138;

import org.w3c.dom.Node;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-28.
 */
public class CvEntry {

    private String name;

    private Node rootNode;

    public CvEntry(String name, Node rootNode) {
        this.name = name;
        this.rootNode = rootNode;
    }

    /**
     *
     * @return name of this CV without .xml suffix
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return rootNode of this CV
     */
    public Node getRootNode() {
        return rootNode;
    }
}
