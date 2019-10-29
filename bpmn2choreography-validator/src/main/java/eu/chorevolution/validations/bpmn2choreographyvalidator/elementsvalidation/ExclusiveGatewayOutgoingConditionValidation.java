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
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ExclusiveGatewayOutgoingConditionValidation extends ElementsValidation {

	   
	   /**
	    * This class verifies that the exclusive gateway participants follows their rules
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public ExclusiveGatewayOutgoingConditionValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
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
			   //for each diverging exclusive gateway, i must have a condition for each outgoing Element
			   if(exclusiveGateway.getGatewayDirection() == GatewayDirection.DIVERGING) {
				   //must have a condition for each SequenceFlow
				   for(SequenceFlow sequenceFlow : exclusiveGateway.getOutgoing()) {
					   try {
						   String conditionID = sequenceFlow.getConditionExpression().getId();
					   }
					   catch(Exception e) {
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.EXCLUSIVE_GATEWAY_MUST_HAVE_CONDITION_VALIDATION_ERROR_MESSAGE, exclusiveGateway.getName(), sequenceFlow.getName()));
						   
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