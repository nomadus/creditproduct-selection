package com.venit.dmn.demo;

import java.util.Collection;
import java.util.logging.Level;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.w3c.dom.Node;

import com.venit.dmn.log.CamundaLogger;
import com.venit.dmn.xml.QueryXMLbyMultipleIDs;
import com.venit.dmn.xml.XMLPath;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2015 VeN-IT Ltd.
 * 
 */

public class BookingAccountDelegate implements JavaDelegate {
	
	private static CamundaLogger LOGGER;
	
	String XMLName = "";
	String elementName = "";
	
	
	public void execute(DelegateExecution execution) throws Exception {
		LOGGER = CamundaLogger.getInstanceOfClass(BookingAccountDelegate.class.getName());
		
		int betragBuchen = 0;
		int restbetrag = 0;
		
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
			}
		}
					
		String currentDir = XMLPath.getXMLPath(ID);
		String XMLPath = currentDir + XMLName + ".xml";
		QueryXMLbyMultipleIDs accountXml = new QueryXMLbyMultipleIDs(XMLPath);
		
		// If query was successful. 
		if (accountXml.getSuccess()) {
			
			Object bookingResult = null;
			Object kontoNum = execution.getVariable("kontonummer").toString();
			Object unterKon = execution.getVariable("unterkonto").toString();
			
			String kontoNummer = ""; 
			String unterKonto = "";
			
			Node saldoNode = null;
			
			if (kontoNum != null) {	
				kontoNummer = kontoNum.toString();
			}
			
			if (unterKon != null) {	
				unterKonto = unterKon.toString();
			}
			
			bookingResult = execution.getVariable("Buchungsergebnisse");
			if(bookingResult != null) {
				String result = bookingResult.toString();
				String resTxt1 = "";
				String resTxt2 = "";
				
				int begPos = 0;
				int endPos = 0;
				
				// Getting "BetragBuchen".
				begPos = result.indexOf("BetragBuchen");
				endPos = result.indexOf(",");
				resTxt1 = result.substring(begPos + 13, endPos);
				betragBuchen = Integer.parseInt(resTxt1);
				
				// Getting "Restbetrag".
				begPos = result.indexOf("Restbetrag");
				endPos = result.indexOf("}");
				resTxt2 = result.substring(begPos + 11, endPos);
				restbetrag = Integer.parseInt(resTxt2);
				
				execution.setVariable("Restbetrag", restbetrag);
			}
			
			saldoNode = accountXml.searchForNode(kontoNummer, unterKonto, "saldo");
			
			if(saldoNode != null) {
				String saldoTxt = "";
				Integer saldoInt = 0;
				
				// Computing saldo + BetragBuchen.
				saldoTxt = saldoNode.getTextContent();
				saldoInt = Integer.parseInt(saldoTxt);
				saldoInt += betragBuchen;
				
				execution.setVariable("saldo", saldoInt);
				saldoNode.setTextContent(saldoInt.toString());
				accountXml.saveXML(XMLPath);
			}
			
			accountXml = null;
		}
	}
	
}