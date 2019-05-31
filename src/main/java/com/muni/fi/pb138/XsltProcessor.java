package com.muni.fi.pb138;


import java.io.InputStream;
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
 * @author Filip Sollar Created by filipsollar on 2019-05-28.
 */
public class XsltProcessor implements Processor {

	@Override
	public void process(String[] args) {
		Options options = new Options();

		options.addOption("o", "output", false, "If set outputs to console");
		options.addOption("a", "all", false, "Sets whether to output all");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			boolean output = cmd.hasOption("o");

			List<CvEntry> entries;
			if (cmd.hasOption("a")) {
				entries = Main.getDatabase().getAllCvEntries();
			} else {
				entries = Main.getDatabase().getCvEntries(cmd
					.getArgList()
					.stream()
					.map(s -> s.replace(".xml", ""))
					.toArray(String[]::new));
			}
			transform(entries, output);

		} catch (ParseException | TransformerException e) {
			e.printStackTrace();
		}

	}

	private void transform(List<CvEntry> entries, boolean output)
		throws TransformerConfigurationException {
		TransformerFactory tf = TransformerFactoryImpl.newInstance();

		InputStream xslStream = getClass()
			.getClassLoader().getResourceAsStream("europass-to-html.xsl");
		Transformer xsltProc = tf.newTransformer(new StreamSource(xslStream));

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
