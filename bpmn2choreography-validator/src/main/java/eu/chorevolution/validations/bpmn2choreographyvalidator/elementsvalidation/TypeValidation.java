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

import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class TypeValidation extends ElementsValidation {

	   
	   /**
	    * This class validates the simple types that i have in messages if i haven't got an XSD:
	    * if i haven't got an xsd file i should have only symple types, if not raise an exception
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public TypeValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   @Override	   
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   
	   	   //List<String> baseTypes = Arrays.asList("boolean","byte","date","decimal","int","integer","long","short","double","float","string","dateTime");
		   //at moment, there is no automated way to derive base types.
		   List<String> baseTypes = Arrays.asList("anyURI", "boolean", "base64Binary", "hexBinary", "date", "dateTime", "time", "duration", "dayTimeDuration", "yearMonthDuration", "gDay", "gMonth", "gMonthDay", "gYear", "gYearMonth", "decimal", "integer", "nonPositiveInteger", "negativeInteger", "long", "int", "short", "byte", "nonNegativeInteger", "unsignedLong", "unsignedInt", "unsignedShort", "unsignedByte", "positiveInteger", "double", "float", "QName", "string", "normalizedString", "token", "language", "NMTOKEN", "Name", "NCName", "ID", "IDREF", "ENTITY", "untypedAtomic", "List", "Union");
		   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   for(FlowElement flowEle : choreography.getFlowElements()) {
				   if(flowEle instanceof ChoreographyTask) {
					   ChoreographyTask chorTask = (ChoreographyTask) flowEle;
					   for(MessageFlow messageFlow : chorTask.getMessageFlowRef()) {

						   String messageStructureRef=null;
						   
						   try {
						   //I have to use this function to get the Message Structure Ref (and not simply the name)
						   messageStructureRef=((BasicEObjectImpl)messageFlow.getMessageRef().getItemRef().getStructureRef()).eProxyURI().fragment();

						   }
						   catch (Exception e) {
							   String receiverParticipant=null;
							   for(Participant receiver : chorTask.getParticipantRefs()) {
								   if(receiver.getId()!=chorTask.getInitiatingParticipantRef().getId())
									   receiverParticipant=receiver.getName();
							   }
					    	   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
					    			   Bpmn2ChoreographyValidatorMessage.MESSAGE_TYPE_NOT_DEFINED_ERROR_MESSAGE, messageFlow.getMessageRef().getName(), choreography.getName(), chorTask.getName(), chorTask.getInitiatingParticipantRef().getName(), receiverParticipant));
						   
						   }
						   
						   //the null case is managed in the exception above
					       if(messageStructureRef != null && !baseTypes.contains(messageStructureRef)) {				    	
							   String receiverParticipant=null;
							   for(Participant receiver : chorTask.getParticipantRefs()) {
								   if(receiver.getId()!=chorTask.getInitiatingParticipantRef().getId())
									   receiverParticipant=receiver.getName();
							   }
					    	   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
					    			   Bpmn2ChoreographyValidatorMessage.TYPE_NOT_EXIST_ERROR_MESSAGE, messageFlow.getMessageRef().getName(), choreography.getName(), chorTask.getName(), chorTask.getInitiatingParticipantRef().getName(), receiverParticipant, messageStructureRef));						   
					       }
						   
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