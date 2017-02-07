package com.venit.dmn.demo;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.xml.sax.SAXException;

import com.venit.dmn.log.CamundaLogger;
import com.venit.dmn.xml.QueryXML;
import com.venit.dmn.xml.XMLPath;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2014 VeN-IT Ltd.
 * 
 */

public class GetDataFromXMLDelegate implements JavaDelegate {
	
	private static CamundaLogger LOGGER;
	
	public void execute(DelegateExecution execution) throws IOException {
		String XMLName = "";
		String elementName;
		String xmlAttribute = "";
		String xmlAttribute01 = "";
		String xmlAttribute02 = "";
		String xmlAttribute03 = "";
		String xmlAttribute04 = "";
		String xmlAttribute05 = "";
		String xmlAttribute06 = "";
		String xmlAttribute07 = "";
		String xmlAttribute08 = "";
		String xmlAttribute09 = "";
		String xmlAttribute10 = "";
		String xmlAttribute11 = "";
		String retVal = "";
		
		LOGGER = CamundaLogger.getInstanceOfClass(GetDataFromXMLDelegate.class.getName());
		
		// Reading of process ID.
		String procDefID = execution.getProcessDefinitionId();
				LOGGER.putMessage(Level.INFO, "(GetDataFromXMLDelegate) ProcDefID is: " + procDefID);
		String ID = procDefID.substring(0, procDefID.indexOf(':'));
				LOGGER.putMessage(Level.INFO, "(GetDataFromXMLDelegate) Process is: " + ID);
				
		
		// Reading of extension elements.
		ServiceTask serviceTask = (ServiceTask) execution.getBpmnModelElementInstance();
		CamundaProperties camundaProperties = 
				serviceTask.getExtensionElements().getElementsQuery().filterByType(CamundaProperties.class).singleResult();
		
		Collection<CamundaProperty> properties = camundaProperties.getCamundaProperties();
		for (CamundaProperty property : properties) {
			
			elementName = property.getAttributeValue("name");
			
			if (elementName.equals("XMLInputName")) {
				XMLName = property.getCamundaValue();
				if (XMLName == null) XMLName = "";
			} else if (elementName.equals("xml_attribute")) {
				xmlAttribute = property.getCamundaValue();
				if (xmlAttribute == null) xmlAttribute = ""; 
			} else if (elementName.equals("xml_attribute01")) {
				xmlAttribute01 = property.getCamundaValue();
				if (xmlAttribute01 == null) xmlAttribute01 = "";
			} else if (elementName.equals("xml_attribute02")) {
				xmlAttribute02 = property.getCamundaValue();
				if (xmlAttribute02 == null) xmlAttribute02 = "";
			} else if (elementName.equals("xml_attribute03")) {
				xmlAttribute03 = property.getCamundaValue();
				if (xmlAttribute03 == null) xmlAttribute03 = "";
			} else if (elementName.equals("xml_attribute04")) {
				xmlAttribute04 = property.getCamundaValue();
				if (xmlAttribute04 == null) xmlAttribute04 = "";
			} else if (elementName.equals("xml_attribute05")) {
				xmlAttribute05 = property.getCamundaValue();
				if (xmlAttribute05 == null) xmlAttribute05 = "";
			} else if (elementName.equals("xml_attribute06")) {
				xmlAttribute06 = property.getCamundaValue();
				if (xmlAttribute06 == null) xmlAttribute06 = "";
			} else if (elementName.equals("xml_attribute07")) {
				xmlAttribute07 = property.getCamundaValue();
				if (xmlAttribute07 == null) xmlAttribute07 = "";
			} else if (elementName.equals("xml_attribute08")) {
				xmlAttribute08 = property.getCamundaValue();
				if (xmlAttribute08 == null) xmlAttribute08 = "";
			} else if (elementName.equals("xml_attribute09")) {
				xmlAttribute09 = property.getCamundaValue();
				if (xmlAttribute09 == null) xmlAttribute09 = "";
			} else if (elementName.equals("xml_attribute10")) {
				xmlAttribute10 = property.getCamundaValue();
				if (xmlAttribute10 == null) xmlAttribute10 = "";
			} else if (elementName.equals("xml_attribute11")) {
				xmlAttribute11 = property.getCamundaValue();
				if (xmlAttribute11 == null) xmlAttribute11 = "";
			}
		}
		
		String currentDir = XMLPath.getXMLPath(ID);
		String XMLPath = currentDir + XMLName + ".xml";
		QueryXML queryXml = new QueryXML(XMLPath);
		
		
		// Search requested value from XML.
		String finalValue = "-";
		if (ID.equals("") || xmlAttribute.equals("")) {
			retVal = "-";
		} else {
			try {
				String personNo = execution.getVariable("geburtszahl_form").toString();
				retVal = queryXml.searchForData(personNo, xmlAttribute);
				finalValue = retVal;
			} catch (XPathExpressionException e) {
				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.XPathExpressionException): " + e.getLocalizedMessage());
			} catch (ParserConfigurationException e) {
				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.ParserConfigurationException): " + e.getLocalizedMessage());
			} catch (SAXException e) {
				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.SAXException): " + e.getLocalizedMessage());
			}
		}
		
		// Setting value of necessary process variable. 
//		if (!finalValue.equals("-")) {
			execution.setVariable(xmlAttribute, finalValue);
			LOGGER.putMessage(Level.INFO, "Process variable '" + xmlAttribute + "' is set to " + finalValue);
			LOGGER.putMessage(Level.INFO, "--------------------------------------------------------------------------------");
//		} else {
//			LOGGER.putMessage(Level.INFO, "Couldn't find value for '" + xmlAttribute + "'. NO process variable has set.");
//			LOGGER.putMessage(Level.INFO, "--------------------------------------------------------------------------------");
//		}
		
		
		// This is a fix section. Have to do a loop for more attributes...
		String personNo = execution.getVariable("geburtszahl_form").toString();
		
		for (int i=1; i<12; i++) {
			switch (i) {
			case 1: getInfoFromXML(execution, personNo, xmlAttribute01, queryXml);
					break;
			case 2: getInfoFromXML(execution, personNo, xmlAttribute02, queryXml);
					break;
			case 3: getInfoFromXML(execution, personNo, xmlAttribute03, queryXml);
					break;
			case 4: getInfoFromXML(execution, personNo, xmlAttribute04, queryXml);
					break;
			case 5: getInfoFromXML(execution, personNo, xmlAttribute05, queryXml);
					break;
			case 6: getInfoFromXML(execution, personNo, xmlAttribute06, queryXml);
					break;
			case 7: getInfoFromXML(execution, personNo, xmlAttribute07, queryXml);
					break;
			case 8: getInfoFromXML(execution, personNo, xmlAttribute08, queryXml);
					break;
			case 9: getInfoFromXML(execution, personNo, xmlAttribute09, queryXml);
					break;
			case 10: getInfoFromXML(execution, personNo, xmlAttribute10, queryXml);
					break;
			case 11: getInfoFromXML(execution, personNo, xmlAttribute11, queryXml);
					break;
			}
			
		}
		
		
//		if (!xmlAttribute01.equals("")) {
//			try {
//				String personNo = execution.getVariable("geburtszahl_form").toString();
//				retVal = queryXml.searchForData(personNo, xmlAttribute01);
//				finalValue = retVal;
//			} catch (XPathExpressionException e) {
//				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.XPathExpressionException): " + e.getLocalizedMessage());
//			} catch (ParserConfigurationException e) {
//				
//				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.ParserConfigurationException): " + e.getLocalizedMessage());
//			} catch (SAXException e) {
//				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.SAXException): " + e.getLocalizedMessage());
//			}
//			
//			// Setting value of necessary process variable.
//			execution.setVariable(xmlAttribute01, finalValue);
//			LOGGER.putMessage(Level.INFO, "Process varible '" + xmlAttribute01 + "' is set to " + finalValue);
//			
//			LOGGER.putMessage(Level.INFO, "--------------------------------------------------------------------------------");
//		}
		
	}
	
	
	// Searching for values in XML.
	private void getInfoFromXML(DelegateExecution execution, String personNo, String attribute, QueryXML queryXml) throws IOException {
		String retVal = "-";
		
		if (!attribute.equals("")) {
			try {
				retVal = queryXml.searchForData(personNo, attribute);
			} catch (XPathExpressionException e) {
				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.XPathExpressionException): " + e.getLocalizedMessage());
			} catch (ParserConfigurationException e) {
				
				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.ParserConfigurationException): " + e.getLocalizedMessage());
			} catch (SAXException e) {
				LOGGER.putMessage(Level.SEVERE, "(GetDataFromXMLDelegate.SAXException): " + e.getLocalizedMessage());
			}
			
			// Setting value of necessary process variable.
			execution.setVariable(attribute, retVal);
			LOGGER.putMessage(Level.INFO, "Process varible '" + attribute + "' is set to " + retVal);
			LOGGER.putMessage(Level.INFO, "--------------------------------------------------------------------------------");
		}
	}
	
}