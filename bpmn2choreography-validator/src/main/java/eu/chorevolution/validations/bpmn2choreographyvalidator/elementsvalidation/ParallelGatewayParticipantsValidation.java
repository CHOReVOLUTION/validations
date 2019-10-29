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
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ParallelGatewayParticipantsValidation extends ElementsValidation {

	   
	   /**
	    * This class verifies that the parallel gateway participants follows their rules
	 * 
	 */
	private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	public ParallelGatewayParticipantsValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	}

	@Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   List<ParallelGateway> parallelGateways = new ArrayList<ParallelGateway>();
		   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   
			   for (FlowElement flowElement : choreography.getFlowElements()) {
				   if (flowElement instanceof ParallelGateway) {
						   parallelGateways.add((ParallelGateway) flowElement);
				   }
			   }
		   }
		   
		   for(ParallelGateway parallelGateway : parallelGateways) {
			   
			   List<ChoreographyActivity> nextChors = new ArrayList<ChoreographyActivity>();
			   List<ChoreographyActivity> precChors = new ArrayList<ChoreographyActivity>();

			   
 			   for(SequenceFlow outgoing : parallelGateway.getOutgoing()) {
				   if(outgoing.getTargetRef() instanceof ChoreographyActivity) {
					   nextChors.add((ChoreographyActivity) outgoing.getTargetRef());
				   }
			   }

 			   //I add precChors recursively, i'll stop when encounters other elements than parallel gateway or choreography task
 			   precChors.addAll(getIncomingChoreographyActivity(parallelGateway));
 			   
 			   if(!nextChors.isEmpty()) {
 				   
 				   List<Participant> initiators = new ArrayList<Participant>();
 				   
 				   for(ChoreographyActivity nextChor : nextChors) {
 					   
 					   boolean present=false;
 					   for(Participant initiator : initiators) {
 						   if(initiator.getId()==nextChor.getInitiatingParticipantRef().getId()) {
							present=true;
						}
 					   }
 					   //avoid duplicates
 					   if(!present) {
 						   initiators.add(nextChor.getInitiatingParticipantRef());
 					   }
 					   
 				   }

 				   for(ChoreographyActivity precChor : precChors) {
 					   List<Participant> missingParticipants = new ArrayList<Participant>(); 					   
 					   
 					   for(Participant initiator : initiators) {
 						   boolean present=false;
 						   for(Participant precChorParticipant : precChor.getParticipantRefs()) {
 							   if(initiator.getId()==precChorParticipant.getId()) {
 								   present=true;
 							   }	
 						   }
 						   if(!present) {
 							   //The initiator of a nextchor is not present in an precchor
							   missingParticipants.add(initiator);
 						   }
 					   }
 					   
					   if(missingParticipants.size()!=0) {
						   //i have errors, raise them
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.PARALLEL_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE, parallelGateway.getName(), precChor.getName(), listOfParticipants(missingParticipants)));	   
					   }
 					   
 				   }	   
 				   
 			   }
 			   

 			   
		   }
   
		   
		   

		
	      return super.ValidatorResponse;
	   }

	   public List<ChoreographyActivity> getIncomingChoreographyActivity(ParallelGateway parallelGateway) {
		   List<ChoreographyActivity> precChors = new ArrayList<ChoreographyActivity>();

		   for(SequenceFlow incoming : parallelGateway.getIncoming()) {
			   if(incoming.getSourceRef() instanceof ChoreographyActivity) {
				   precChors.add((ChoreographyActivity) incoming.getSourceRef());
			   }
			   else if(incoming.getSourceRef() instanceof ParallelGateway) {
				   precChors.addAll(getIncomingChoreographyActivity((ParallelGateway)incoming.getSourceRef()));
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