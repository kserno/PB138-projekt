package com.muni.fi.pb138;

import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

/**
 * @author Filip Sollar
 * Created by filipsollar on 2019-05-28.
 */
public interface Database {

    List<Node> getAllXmlRootNodes();
    List<Node> getXmlRootNodes(String[] names);

}
