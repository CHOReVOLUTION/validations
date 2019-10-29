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
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class InclusiveGatewayParticipantsValidation extends ElementsValidation {

	   
	   /**
	    * This class verifies that the inclusive gateway participants follows their rules
	 * 
	 */
	private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	public InclusiveGatewayParticipantsValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	}

	@Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   List<InclusiveGateway> inclusiveGateways = new ArrayList<InclusiveGateway>();
		   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   
			   for (FlowElement flowElement : choreography.getFlowElements()) {
				   if (flowElement instanceof InclusiveGateway) {
						   inclusiveGateways.add((InclusiveGateway) flowElement);
				   }
			   }
		   }
		   
		   for(InclusiveGateway inclusiveGateway : inclusiveGateways) {//for each gateway i make checks
			   
			   List<ChoreographyActivity> nextChors = new ArrayList<ChoreographyActivity>();
			   List<ChoreographyActivity> precChors = new ArrayList<ChoreographyActivity>();

			   
 			   for(SequenceFlow outgoing : inclusiveGateway.getOutgoing()) {
				   if(outgoing.getTargetRef() instanceof ChoreographyActivity) {
					   nextChors.add((ChoreographyActivity) outgoing.getTargetRef());
				   }
			   }

 			   precChors.addAll(getIncomingChoreographyActivity(inclusiveGateway));
 	
 			   
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
 					   for(Participant initiator : initiators) {
 						   boolean present=false;
 						   for(Participant precChorParticipant : precChor.getParticipantRefs()) {
 							   if(initiator.getId()==precChorParticipant.getId()) {
								present=true;
							}
 						   }
 						   if(present==false) {
 							   //The initiator of a nextchor is not present in an precchor
 							   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
 									   Bpmn2ChoreographyValidatorMessage.INCLUSIVE_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE, inclusiveGateway.getName()));
 						   }
 					   }
 				   }
 				   
 				   
 	 			   //Case Merge (Converging)
 				   if (inclusiveGateway.getGatewayDirection() == GatewayDirection.CONVERGING) {
 					   //Already Verified
 				   }

 	 			   
 	 			   
 	 			   //Case Split (Diverging)
 				   if (inclusiveGateway.getGatewayDirection() == GatewayDirection.DIVERGING) {
 	 				   for(ChoreographyActivity precChor : precChors) {
 	 					   
 	 					   Participant sender = null;
 	 	 				   List<Participant> receivers = new ArrayList<Participant>();
 	 	 				   boolean areSenders=true;
 	 	 				   boolean areReceivers=true;
 	 					   
 	 					   sender=precChor.getInitiatingParticipantRef();
 	 					   receivers=precChor.getParticipantRefs();
 	 	 				   
 	 					   //receivers=allparticipants-initiatingparticipant
 	 					   receivers.remove(sender);
 	 					   
 	 					   
 	 					   for(Participant initiator : initiators) {
 	 						   //the initiators must be present as common sender or receivers in all the preceding choreography
 	 						   boolean present=false;
 	 						   
 	 						   for(Participant receiver : receivers) {
 	 							   if(receiver.getId()==initiator.getId()) {
 	 								   present=true;
 	 								   areSenders=false;
 	 							   }
 	 						   }
 	 						   
 	 						   if(initiators.size()==1) {
 	 							   if((initiators.get(0) != null)&&(initiators.get(0).getId()==sender.getId())) {
 	 								   present=true;
 	 								   areReceivers=false;
 	 							   }
 	 						   }
 	 					   
 	 						   if(present==false) {
 	 							   areSenders=false;
 	 							   areReceivers=false;
 	 						   }
 	 						   
 	 						   
 	 						   
 	 					   }
 	 					   
 	 					   //they MUST be all senders or all receivers for each choreography. can't be mixed
 	 					   if(!(areSenders ^ areReceivers)) {
 	 						 super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
									   Bpmn2ChoreographyValidatorMessage.INCLUSIVE_SPLIT_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE, inclusiveGateway.getName()));
 	 					   }
 	 					   
 	 				   }
 				   }
 				   
 				   
 				   
 			   }
 			   

 			   
		   }
   
		   
		   

		
	      return super.ValidatorResponse;
	   }

	   public List<ChoreographyActivity> getIncomingChoreographyActivity(InclusiveGateway inclusiveGateway) {
		   List<ChoreographyActivity> precChors = new ArrayList<ChoreographyActivity>();

		   for(SequenceFlow incoming : inclusiveGateway.getIncoming()) {
			   if(incoming.getSourceRef() instanceof ChoreographyActivity) {
				   precChors.add((ChoreographyActivity) incoming.getSourceRef());
			   }
			   else if(incoming.getSourceRef() instanceof InclusiveGateway) {
				   precChors.addAll(getIncomingChoreographyActivity((InclusiveGateway)incoming.getSourceRef()));
			   }
		   }
		   
		   
		   
		   return precChors;
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