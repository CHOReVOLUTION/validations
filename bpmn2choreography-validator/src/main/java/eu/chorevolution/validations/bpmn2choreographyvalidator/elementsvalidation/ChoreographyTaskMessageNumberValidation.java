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
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ChoreographyTaskMessageNumberValidation extends ElementsValidation {

	   /**
	    * Each choreography task must have 1 to 2 messages
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;


	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public ChoreographyTaskMessageNumberValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
	   	   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	
	   private final int CHOREOGRAPHY_TASK_MESSAGE_NUMBER_UPPER_BOUND = 2;
	   private final int CHOREOGRAPHY_TASK_MESSAGE_NUMBER_LOWER_BOUND = 1;
	   
	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   int lowerboundTaskMessageNumber = this.CHOREOGRAPHY_TASK_MESSAGE_NUMBER_LOWER_BOUND;
		   int upperboundTaskMessageNumber = this.CHOREOGRAPHY_TASK_MESSAGE_NUMBER_UPPER_BOUND;

		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   for(FlowElement flowEle : choreography.getFlowElements()) {
				   if(flowEle instanceof ChoreographyTask) {
					   ChoreographyTask chorTask = (ChoreographyTask) flowEle;

					   if (lowerboundTaskMessageNumber > chorTask.getMessageFlowRef().size()
							   || upperboundTaskMessageNumber < chorTask.getMessageFlowRef().size()) {

						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.CHOREOGRAPHY_TASK_MESSAGE_NUMBER_BOUNDS_ERROR_MESSAGE, chorTask.getName(), chorTask.getMessageFlowRef().size()));
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