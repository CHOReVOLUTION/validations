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

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ChoreographyActivityBoundsValidation extends ElementsValidation {

	/**
	 * This class validates the choreography activity incoming and outgoing bounds
	 	* 
	 	*/
	  private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;


	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public ChoreographyActivityBoundsValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   private final int CHOREOGRAPHY_ACTIVITY_GATEWAY_INCOMING_UPPER_BOUND = Bpmn2ChoreographyValidator.N_SIZE;
	   private final int CHOREOGRAPHY_ACTIVITY_GATEWAY_INCOMING_LOWER_BOUND = 1;
	   private final int CHOREOGRAPHY_ACTIVITY_GATEWAY_OUTGOING_UPPER_BOUND = 1;
	   private final int CHOREOGRAPHY_ACTIVITY_GATEWAY_OUTGOING_LOWER_BOUND = 1;
	   
	   
	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();
			   
		   int lowerboundIncomingFlowsChoreographyActivity = this.CHOREOGRAPHY_ACTIVITY_GATEWAY_INCOMING_LOWER_BOUND;
		   int upperboundIncomingFlowsChoreographyActivity = this.CHOREOGRAPHY_ACTIVITY_GATEWAY_INCOMING_UPPER_BOUND;
		   int lowerboundOutgoingFlowsChoreographyActivity = this.CHOREOGRAPHY_ACTIVITY_GATEWAY_OUTGOING_LOWER_BOUND;
		   int upperboundOutgoingFlowsChoreographyActivity = this.CHOREOGRAPHY_ACTIVITY_GATEWAY_OUTGOING_UPPER_BOUND;

		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   for (FlowElement flowElement : choreography.getFlowElements()) {
				   if (flowElement instanceof ChoreographyActivity) {
					   if (lowerboundIncomingFlowsChoreographyActivity > ((ChoreographyActivity) flowElement).getIncoming()
							   .size()
							   || upperboundIncomingFlowsChoreographyActivity < ((ChoreographyActivity) flowElement).getIncoming()
							   .size()) {
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.CHOREOGRAPHY_ACTIVITY_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE, flowElement.getName(), ((ChoreographyActivity) flowElement).getIncoming().size()));
					   }

					   if (lowerboundOutgoingFlowsChoreographyActivity > ((ChoreographyActivity) flowElement).getOutgoing()
							   .size()
							   || upperboundOutgoingFlowsChoreographyActivity < ((ChoreographyActivity) flowElement).getOutgoing()
							   .size()) {
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.CHOREOGRAPHY_ACTIVITY_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE, flowElement.getName(), ((ChoreographyActivity) flowElement).getOutgoing().size()));
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