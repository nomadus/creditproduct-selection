package com.venit.dmn.xml;

import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.venit.dmn.log.CamundaLogger;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2014 VeN-IT Ltd.
 *
 * 
 * Runs XPath query on given XML.
 */

public class QueryXML {
	
	private static Document doc;
	private static final CamundaLogger LOGGER = CamundaLogger.getInstanceOfClass(QueryXML.class.getName());
	private boolean success = true;
	
	public String query(String section, String nodeName) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		XPathExpression expr = null;
		
		// Create an XPathFactory.
		XPathFactory xFactory = XPathFactory.newInstance();
		
		// Create an XPath object.
		XPath xpath = xFactory.newXPath();

		// Compile the XPath expression.
		expr = xpath.compile("//" + section + "/" + nodeName);

		// Run the query and get a nodeset.
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		
		// Cast the result to a DOM NodeList.
		NodeList nodes = (NodeList) result;
		
		if (nodes.getLength() == 0) {
			return "-";
		} else {
			return nodes.item(0).getTextContent();
		}
	}
	
	
	// Search for specific value in XML
	public String searchForData(String ID, String nodeName) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		XPathExpression expr = null;
		
		// Create an XPathFactory.
		XPathFactory xFactory = XPathFactory.newInstance();
		
		// Create an XPath object.
		XPath xpath = xFactory.newXPath();

		// Compile the XPath expression.
		expr = xpath.compile("/DB/record[geburtszahl=" + ID + "]/" + nodeName);
		LOGGER.putMessage(Level.INFO, "Az aktuális XPATH: " + "/DB/record[geburtszahl=" + ID + "]/" + nodeName);
		
		// Run the query and get a nodeset.
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		
		// Cast the result to a DOM NodeList.
		NodeList nodes = (NodeList) result;
		
		if (nodes.getLength() == 0) {
			return "-";
		} else {
			return nodes.item(0).getTextContent();
		}
	}


	// Getting result of special query.
	public String getNodeValue(String srcVal) {
		XPathExpression expr = null;
		
		// Create an XPathFactory.
		XPathFactory xFactory = XPathFactory.newInstance();
		
		// Create an XPath object.
		XPath xpath = xFactory.newXPath();

		// Compile the XPath expression.
		try {
			expr = xpath.compile("//title[@lang='" + srcVal + "']");
		} catch (XPathExpressionException e) {
			LOGGER.putMessage(Level.SEVERE, "(QueryXML): Error on xpath compile. " + e.getLocalizedMessage());
		}

		// Run the query and get a nodeset.
		Object result;
		try {
			result = expr.evaluate(doc, XPathConstants.NODESET);
			
			// Cast the result to a DOM NodeList.
			NodeList nodes = (NodeList) result;
			
			if (nodes.getLength() == 0) {
				return "-";
			} else {
				return nodes.item(0).getTextContent();
			}
			
		} catch (XPathExpressionException e) {
			LOGGER.putMessage(Level.SEVERE, "(QueryXML): Error on xpath evaluate. " + e.getLocalizedMessage());
			return "-";
		}
	}
	
	
	public QueryXML(String XMLName) {		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
				
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(XMLName);
		} catch (ParserConfigurationException e) {
			LOGGER.putMessage(Level.SEVERE, "ParserConfigurationException (QueryXML): " + e.getLocalizedMessage());
			success = false;
		} catch (SAXException e) {
			LOGGER.putMessage(Level.SEVERE, "SAXException (QueryXML): " + e.getLocalizedMessage());
			success = false;
		} catch (IOException e) {
			LOGGER.putMessage(Level.SEVERE, "IOException (QueryXML): " + e.getLocalizedMessage());
			success = false;
		}
	}
	
	
	// Returns success status.
	public boolean getSuccess() {
		return success;
	}
	
}