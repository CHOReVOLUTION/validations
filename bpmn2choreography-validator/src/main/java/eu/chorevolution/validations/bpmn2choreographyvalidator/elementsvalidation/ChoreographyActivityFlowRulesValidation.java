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

import java.util.List;

import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ChoreographyActivityFlowRulesValidation extends ElementsValidation {	   
	   
	   /**
	    * This class validates the activity participant flow rules
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
		public ChoreographyActivityFlowRulesValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {			  
			   for(FlowElement flowEle : choreography.getFlowElements()) {
				   if(flowEle instanceof ChoreographyActivity) {
					   ChoreographyActivity chorActi = (ChoreographyActivity) flowEle;

					   boolean isstarter=false;

					   for (SequenceFlow toAnalyze : chorActi.getIncoming()){
						   if(toAnalyze.getSourceRef() instanceof StartEvent) {
							   isstarter=true;
						   }
					   }

					   if(!isstarter) {
						   boolean isvalid=true;

						   for (SequenceFlow toAnalyze : chorActi.getIncoming()){
							   if(toAnalyze.getSourceRef() instanceof ChoreographyActivity) {

								   isvalid=false;
								   for(Participant participant2 : ((ChoreographyActivity) toAnalyze.getSourceRef()).getParticipantRefs())
									   if(chorActi.getInitiatingParticipantRef().getId()==participant2.getId()) {
										isvalid=true;
									}


								   if(!isvalid){
									   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
											   Bpmn2ChoreographyValidatorMessage.CHOREOGRAPHY_ACTIVITY_FLOW_ERROR_MESSAGE, chorActi.getName(), chorActi.getInitiatingParticipantRef().getName(), ((ChoreographyActivity) toAnalyze.getSourceRef()).getName()));
								   }
							   }

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