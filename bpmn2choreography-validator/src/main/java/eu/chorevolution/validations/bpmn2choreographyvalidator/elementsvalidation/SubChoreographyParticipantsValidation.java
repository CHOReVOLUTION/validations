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

import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubChoreography;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class SubChoreographyParticipantsValidation extends ElementsValidation {

	   /**
	    * This class validates that all the sub-chor participants are as declared
	    * And also for call-choreographies
	 * 
	 */
	private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;


	/**
	 * @param bpmn2ChoreographyValidator
	 */
	public SubChoreographyParticipantsValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	}


	   
	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();
		   
		   //I should verify that in each sub-chor declaration the participants must be the same as
		   //in the sub-chor diagram (and relative subchoreographyes).
		   //when we visit the inside of a choreography, we have not to visit also the inside of all his sub-chors
		   //and so on but we can just verify the participants declarations. if there is a participant not present,
		   //when analyzing the others sub-chor it will be found.
		   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {

			   for (FlowElement flowElement : choreography.getFlowElements()) {
				   if (flowElement instanceof SubChoreography) {
					   Choreography subChoreography = this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographyToMapSubChoreographyActivity((SubChoreography) flowElement);
					   
					   List<Participant> declaredParticipants = new ArrayList<Participant>();
					   List<Participant> insideParticipants = new ArrayList<Participant>();

					   declaredParticipants = ((SubChoreography) flowElement).getParticipantRefs();
					   
					   for (FlowElement subFlowElement : subChoreography.getFlowElements()) {
						   if (subFlowElement instanceof ChoreographyActivity) {
							   for(Participant participant : ((ChoreographyActivity) subFlowElement).getParticipantRefs()) {
								   boolean isPresent=false;
								   for (Participant listParticipant : insideParticipants) {
									   if(listParticipant.getId()==participant.getId()) {
										   isPresent=true;
										   break;
									   }
								   }
								   
								   //avoid duplicates
								   if(!isPresent)
									   insideParticipants.add(participant);
							   }
						   }
					   }
					   
					   //now i have lists fullfilled, i can compare
					   boolean isValid=true;
					   if(insideParticipants.size()!=declaredParticipants.size())
						   isValid=false;
					   else {
						   for(Participant participant : declaredParticipants) {
							   boolean isPresent=false;
							   for (Participant listParticipant : insideParticipants) {
								   if(participant.getId()==listParticipant.getId()) {
									   isPresent=true;
									   break;
								   }
							   }
							   if(!isPresent) {
								   isValid=false;
								   break;
							   }
							   
						   }
					   }
					   if(!isValid) {
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.SUBCHOREOGRAPHY_PARTICIPANT_MISSING_ERROR, subChoreography.getName(), listOfParticipants(declaredParticipants), listOfParticipants(insideParticipants)));
					   }
					   
				   }
				   else if (flowElement instanceof CallChoreography) {
					   Choreography callChoreography = this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographyToMapCallChoreographyActivity((CallChoreography) flowElement);
					   
					   List<Participant> declaredParticipants = new ArrayList<Participant>();
					   List<Participant> insideParticipants = new ArrayList<Participant>();

					   declaredParticipants = ((CallChoreography) flowElement).getParticipantRefs();
					   
					   for (FlowElement subFlowElement : callChoreography.getFlowElements()) {
						   if (subFlowElement instanceof ChoreographyActivity) {
							   for(Participant participant : ((ChoreographyActivity) subFlowElement).getParticipantRefs()) {
								   boolean isPresent=false;
								   for (Participant listParticipant : insideParticipants) {
									   if(listParticipant.getId()==participant.getId()) {
										   isPresent=true;
										   break;
									   }
								   }
								   
								   //avoid duplicates
								   if(!isPresent)
									   insideParticipants.add(participant);
							   }
						   }
					   }
					   
					   //now i have lists fullfilled, i can compare
					   boolean isValid=true;
					   if(insideParticipants.size()!=declaredParticipants.size())
						   isValid=false;
					   else {
						   for(Participant participant : declaredParticipants) {
							   boolean isPresent=false;
							   for (Participant listParticipant : insideParticipants) {
								   if(participant.getId()==listParticipant.getId()) {
									   isPresent=true;
									   break;
								   }
							   }
							   if(!isPresent) {
								   isValid=false;
								   break;
							   }
							   
						   }
					   }
					   if(!isValid) {
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.CALLCHOREOGRAPHY_PARTICIPANT_MISSING_ERROR, callChoreography.getName(), listOfParticipants(declaredParticipants), listOfParticipants(insideParticipants)));
					   }
					   
				   }
			   }
		   }

		
		
	      return super.ValidatorResponse;
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