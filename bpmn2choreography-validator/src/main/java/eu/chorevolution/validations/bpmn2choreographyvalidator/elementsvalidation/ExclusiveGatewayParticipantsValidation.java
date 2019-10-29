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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ExclusiveGatewayParticipantsValidation extends ElementsValidation {

	   
	   /**
	    * This class verifies that the exclusive gateway participants follows their rules
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public ExclusiveGatewayParticipantsValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   List<ExclusiveGateway> exclusiveGateways = new ArrayList<ExclusiveGateway>();
		   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   
			   for (FlowElement flowElement : choreography.getFlowElements()) {
				   if (flowElement instanceof ExclusiveGateway) {
						   exclusiveGateways.add((ExclusiveGateway) flowElement);
				   }
			   }
		   }
		   
		   for(ExclusiveGateway exclusiveGateway : exclusiveGateways) {//for each gateway i make checks
			   
			   List<Participant> initialParticipants = new ArrayList<Participant>();
			   
			   for(SequenceFlow outgoing : exclusiveGateway.getOutgoing()) {
				   if(outgoing.getTargetRef() instanceof ChoreographyActivity) {
					   initialParticipants.add((Participant)((ChoreographyActivity)outgoing.getTargetRef()).getInitiatingParticipantRef());
				   }
			   }

			   //initial participants must be presents in EACH choreography prior the exclusive gateway
			   for(ChoreographyActivity incoming : getIncomingChoreographyActivity(exclusiveGateway)) {
				   List<Participant> missingParticipants = new ArrayList<Participant>();
				   
					   for(Participant initialParticipant: initialParticipants) {
						   boolean isvalid=false;
						   for(Participant mustBeParticipant: ((List<Participant>)incoming.getParticipantRefs())) {
							   if(initialParticipant.getId()==mustBeParticipant.getId()) {
								isvalid=true;
							}
						   }
						   if(!isvalid) {
							   missingParticipants.add(initialParticipant);
					   	   }

					   }
					   
					   if(missingParticipants.size()!=0) {
						   //i have errors, raise them
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.EXCLUSIVE_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE, exclusiveGateway.getName(), incoming.getName(), listOfParticipants(missingParticipants)));
					   }
			   }
			   
			   

		   
		   }
		   

		
	      return super.ValidatorResponse;
	   }

	   public List<ChoreographyActivity> getIncomingChoreographyActivity(ExclusiveGateway exclusiveGateway) {
		   List<ChoreographyActivity> precChors = new ArrayList<ChoreographyActivity>();

		   for(SequenceFlow incoming : exclusiveGateway.getIncoming()) {
			   if(incoming.getSourceRef() instanceof ChoreographyActivity) {
				   precChors.add((ChoreographyActivity) incoming.getSourceRef());
			   }
			   else if(incoming.getSourceRef() instanceof ExclusiveGateway) {
				   precChors.addAll(getIncomingChoreographyActivity((ExclusiveGateway)incoming.getSourceRef()));
			   }
		   }
		   
		   
		   
		   return precChors;
	   }
	   
	   
	   public String listOfParticipants(List<Participant> participantList) {
		   String listToReturn="";
		   int i=0;
		   for(Participant participant : participantList) {
			   if(i!=0)
				   listToReturn+=", ";
			   listToReturn+=participant.getName();
			   i++;
		   }
		return listToReturn;
	
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