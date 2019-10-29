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
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ChoreographyTaskInitiatingMessageNumberValidation extends ElementsValidation {

	   /**
	    * This class validates that there is at least one initiating message in each choreography
	 * 
	 */
	private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;



	public ChoreographyTaskInitiatingMessageNumberValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	}

	   private final int CHOREOGRAPHY_INITIATING_TASK_MESSAGE_NUMBER = 1;
	   
	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   int initiatingTaskMessageNumber = this.CHOREOGRAPHY_INITIATING_TASK_MESSAGE_NUMBER;

		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   for(FlowElement flowEle : choreography.getFlowElements()) {
				   if(flowEle instanceof ChoreographyTask) {
					   ChoreographyTask chorTask = (ChoreographyTask) flowEle;

					   
					   int initiatingMessage=0;
					   for(MessageFlow messageFlow : chorTask.getMessageFlowRef()) {
						   Participant mustBeInitiating=((Participant)messageFlow.getSourceRef());
						   if(mustBeInitiating.getId()==chorTask.getInitiatingParticipantRef().getId())
							   initiatingMessage++;
					   }
					   
					   if(initiatingTaskMessageNumber != initiatingMessage)
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.CHOREOGRAPHY_TASK_INITIATING_MESSAGE_NUMBER_ERROR_MESSAGE, chorTask.getName()));
						   
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