/*
  * Copyright 2015 The CHOReVOLUTION project
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation;

import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class XSDTypeValidation extends ElementsValidation {

	   /**
	    * This class validates the XSD type file (if exists):
	    * for each element in my xsd "element" type it must exist a complexType
		* or a simpleType or must be predefined (xsd:string ..)
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public XSDTypeValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   @Override	   
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();
		   		

			   Document xSDDocumentRoot=this.bpmn2ChoreographyValidator.getBpmnLoader().getXSDDocumentRoot();
		       XPath xPath = XPathFactory.newInstance().newXPath();
		       NodeList typesFound = null;
			   List<String> baseTypes = Arrays.asList("anyURI", "boolean", "base64Binary", "hexBinary", "date", "dateTime", "time", "duration", "dayTimeDuration", "yearMonthDuration", "gDay", "gMonth", "gMonthDay", "gYear", "gYearMonth", "decimal", "integer", "nonPositiveInteger", "negativeInteger", "long", "int", "short", "byte", "nonNegativeInteger", "unsignedLong", "unsignedInt", "unsignedShort", "unsignedByte", "positiveInteger", "double", "float", "QName", "string", "normalizedString", "token", "language", "NMTOKEN", "Name", "NCName", "ID", "IDREF", "ENTITY", "untypedAtomic", "List", "Union");

		       try {
		    	   

		    	   typesFound = (NodeList) xPath
		    			   .evaluate("//*", xSDDocumentRoot.getDocumentElement(), XPathConstants.NODESET);
		    	   
		       } catch (XPathExpressionException e) {
		       }
				
		       for(int i=0; i<typesFound.getLength(); i++) {
		    	   for(int j=0; j<typesFound.item(i).getAttributes().getLength(); j++) {
		    		  
		    		   if(typesFound.item(i).getAttributes().item(j).getNodeName().toString().equals("type")) {
				    	   //for each element in my xsd element type it must exist a complexType
				    	   //or a simpleType or must be predefined (xsd:string ..)
		    			   
		    			   String typeValue=typesFound.item(i).getAttributes().item(j).getNodeValue().toString();
		    			   String[] typeValueSplit = typeValue.split(":");
		    			   
		    			   if(typeValueSplit.length<2) {
		    		    	   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
		    		    			   Bpmn2ChoreographyValidatorMessage.XSD_FILE_NOT_WELL_FORMED_ERROR_MESSAGE, typeValue));  
		    			   }
		    			   
		    			   if(typeValueSplit[0].equals("xsd")) {
		    				   //is a basetype
		    				   if(!baseTypes.contains(typeValueSplit[1]))
			    		    	   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
			    		    			   Bpmn2ChoreographyValidatorMessage.XSD_FILE_NOT_WELL_FORMED_ERROR_MESSAGE, typeValue)); 		    					   
		    				   
		    			   }
		    			   else {
		    				   //is a complex or simple type, i have to search for it in the file
						       NodeList typeFound = null;
 
						       try {
						    	   typeFound = (NodeList) xPath
						    			   .evaluate("/*/complexType[@name=\""+typeValueSplit[1]+"\"]", xSDDocumentRoot.getDocumentElement(), XPathConstants.NODESET);
						    	   
						    	   if(typeFound.getLength()==0)
							    	   typeFound = (NodeList) xPath
					    			   .evaluate("/*/simpleType[@name=\""+typeValueSplit[1]+"\"]", xSDDocumentRoot.getDocumentElement(), XPathConstants.NODESET);
					    	   

						       } catch (XPathExpressionException e) {
						    	   
						       }
								
						       
						       if(typeFound.getLength()==0)
						    	   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
						    			   Bpmn2ChoreographyValidatorMessage.XSD_FILE_NOT_WELL_FORMED_ERROR_MESSAGE, typeValueSplit[1]));						   
							   
		    				   
		    				   
		    				   
		    			   }
		    			   
		    		   }
		    	   }
		       }
			   

		
	      return super.ValidatorResponse;
	   }

	   @Override
	   public boolean getResponse() {
		   return super.ValidatorResponse.isValid();
	   }

	   @Override
	   public List<String> getErrors() {
		   return super.ValidatorResponse.getErrors();
	   }


	   
	   
	   
   }