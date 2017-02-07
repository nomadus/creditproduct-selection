package com.venit.dmn.xml;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.venit.dmn.log.CamundaLogger;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2015 VeN-IT Ltd.
 *
 * 
 * Runs XPath query on given XML searching with key attributes.
 */

public class QueryXMLbyMultipleIDs {
	
	private Document doc;
	private CamundaLogger LOGGER;
	private boolean success = true;
	private Node foundNode = null;
	
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
	public String searchForData(String ID1, String ID2, String nodeName) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		LOGGER = CamundaLogger.getInstanceOfClass(QueryXMLbyMultipleIDs.class.getName());
		
		XPathExpression expr = null;
		
		// Create an XPathFactory.
		XPathFactory xFactory = XPathFactory.newInstance();
		
		// Create an XPath object.
		XPath xpath = xFactory.newXPath();

		// Compile the XPath expression.
		expr = xpath.compile("/DB/account[@kontonummer='" + ID1 + "'][@unterkonto='" + ID2 + "']/" + nodeName);
		LOGGER.putMessage(Level.INFO, "Az aktuális XPATH: " + "/DB/account[@kontonummer='" + ID1 + "'][@unterkonto='" + ID2 + "']/" + nodeName);
		
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

	
	// Search for specific Node in XML
	public Node searchForNode(String ID1, String ID2, String nodeName)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		LOGGER = CamundaLogger.getInstanceOfClass(QueryXMLbyMultipleIDs.class.getName());

		XPathExpression expr = null;

		// Create an XPathFactory.
		XPathFactory xFactory = XPathFactory.newInstance();

		// Create an XPath object.
		XPath xpath = xFactory.newXPath();

		// Compile the XPath expression.
		expr = xpath.compile("/DB/account[@kontonummer='" + ID1 + "'][@unterkonto='" + ID2 + "']/" + nodeName);
		LOGGER.putMessage(Level.INFO, "Az aktuális XPATH: " + "/DB/account[@kontonummer='" + ID1 + "'][@unterkonto='"
				+ ID2 + "']/" + nodeName);

		// Run the query and get a nodeset.
		Object result = expr.evaluate(doc, XPathConstants.NODESET);

		// Cast the result to a DOM NodeList.
		NodeList nodes = (NodeList) result;

		if (nodes.getLength() == 0) {
			return null;
		} else {
			foundNode = nodes.item(0);
			return foundNode;
		}
	}		
	
	
	// Getting result of special query.
	public String getNodeValue(String srcVal) {
		LOGGER = CamundaLogger.getInstanceOfClass(QueryXMLbyMultipleIDs.class.getName());
		
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
	
	
	// Constructor for reading.
	public QueryXMLbyMultipleIDs(String XMLName) {		
		LOGGER = CamundaLogger.getInstanceOfClass(QueryXMLbyMultipleIDs.class.getName());
		
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
	
	
	// Saving XML back.
	public void saveXML(String filePath) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			LOGGER.putMessage(Level.SEVERE, "TransformerException (new): " + e.getLocalizedMessage());
		}
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));
		
		try {
			transformer.transform(source, result);
//			if (result != null) result.getOutputStream().close();
		} catch (TransformerException e) {
			LOGGER.putMessage(Level.SEVERE, "TransformerException (transform): " + e.getLocalizedMessage());
		}
	}
	
	
	// Returns success status.
	public boolean getSuccess() {
		return success;
	}
	
}