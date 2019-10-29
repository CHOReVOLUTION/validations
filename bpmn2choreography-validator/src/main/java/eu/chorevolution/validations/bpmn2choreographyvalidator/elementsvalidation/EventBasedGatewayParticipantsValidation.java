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
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class EventBasedGatewayParticipantsValidation extends ElementsValidation {

	   
	   /**
	    * This class verifies that the event-based gateway participants follows their rules
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public EventBasedGatewayParticipantsValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   List<EventBasedGateway> eventBasedGateways = new ArrayList<EventBasedGateway>();
		   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   
			   for (FlowElement flowElement : choreography.getFlowElements()) {
				   if (flowElement instanceof EventBasedGateway) {
						   eventBasedGateways.add((EventBasedGateway) flowElement);
				   }
			   }
		   }
		   
		   for(EventBasedGateway eventBasedGateway : eventBasedGateways) {//for each gateway i make checks
			   
			   List<ChoreographyActivity> nextChors = new ArrayList<ChoreographyActivity>();
			   
			   //add all choreographies following the gateway to a list, later i will compare if senders and receivers are the same
 			   for(SequenceFlow outgoing : eventBasedGateway.getOutgoing()) {
				   if(outgoing.getTargetRef() instanceof ChoreographyActivity) {
					   nextChors.add((ChoreographyActivity) outgoing.getTargetRef());
				   }
			   }

 			   if(!nextChors.isEmpty()) {
 				   
 				   Participant sender = null;
 				   Participant precSender = null;
 				   List<Participant> receivers = new ArrayList<Participant>();
 				   List<Participant> precReceivers = new ArrayList<Participant>();
 				   int chorCounter=0;
 				   boolean sendersAreTheSame=true;
 				   boolean receiversAreTheSame=true;
 				   
 				   for(ChoreographyActivity nextChor : nextChors) {
 					   //i can assume all choreography are made right, if not the validator had to be be stopped before from other methods
 					   sender=nextChor.getInitiatingParticipantRef();
 					   receivers=nextChor.getParticipantRefs();
 					   
 					   //receivers=allparticipants-initiatingparticipant
 					   receivers.remove(sender);
 					   
 					   //if is the first choreography, i cannot compare to myself
 					   if(chorCounter==0) {
 						   precSender=sender;
 						   precReceivers=receivers;
 						   chorCounter+=1;
 					   }
 					   else {
 						   if(sender!=precSender) {
							sendersAreTheSame=false;
						}
 						   
 						   if(receivers.size()!=precReceivers.size()) {
 							   receiversAreTheSame=false;
 						   }
 						   else {
 						   
	 						   boolean isPresent=false;
	 						   for(Participant receiver : receivers) {
	 							   isPresent=false;
	 	 						   for(Participant precReceiver : precReceivers) {
	 	 							   if(receiver.getId()==precReceiver.getId()) {
										isPresent=true;
									}
	 	 						   }
	 	 						   if(isPresent==false) {
									receiversAreTheSame=false;
								}
	 						   }
 						   
 						   }
 						   
 					   }
 					   
 				   
 				   }
 				   
 				   if((sendersAreTheSame==false)&&(receiversAreTheSame==false)) {
 					  super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
							   Bpmn2ChoreographyValidatorMessage.EVENT_BASED_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE, eventBasedGateway.getName()));
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