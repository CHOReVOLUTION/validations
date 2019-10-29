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
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.GatewayDirection;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class GatewayBoundsValidation extends ElementsValidation {

	   /**
	    * This class validates all the gateway bounds rules
	 * 
	 */
	private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;


	/**
	 * @param bpmn2ChoreographyValidator
	 */
	public GatewayBoundsValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	}

	private final String[] GATEWAY_DIRECTION = {"diverging", "converging"};
	   private final int DIVERGING_GATEWAY_INCOMING_UPPER_BOUND = 1;
	   private final int DIVERGING_GATEWAY_INCOMING_LOWER_BOUND = 1;
	   private final int DIVERGING_GATEWAY_OUTGOING_UPPER_BOUND = Bpmn2ChoreographyValidator.N_SIZE;
	   private final int DIVERGING_GATEWAY_OUTGOING_LOWER_BOUND = 1;
	   private final int CONVERGING_GATEWAY_INCOMING_UPPER_BOUND = Bpmn2ChoreographyValidator.N_SIZE;
	   private final int CONVERGING_GATEWAY_INCOMING_LOWER_BOUND = 1;
	   private final int CONVERGING_GATEWAY_OUTGOING_UPPER_BOUND = 1;
	   private final int CONVERGING_GATEWAY_OUTGOING_LOWER_BOUND = 1;
	   
	   
	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();
			   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   List<Gateway> divergingGateways = new ArrayList<Gateway>();
			   List<Gateway> convergingGateways = new ArrayList<Gateway>();
			   for (FlowElement flowElement : choreography.getFlowElements()) {
				   if (flowElement instanceof Gateway) {
					   if (((Gateway) flowElement).getGatewayDirection() == GatewayDirection.DIVERGING) {
						   divergingGateways.add((Gateway) flowElement);
					   } else if (((Gateway) flowElement).getGatewayDirection() == GatewayDirection.CONVERGING) {
						   convergingGateways.add((Gateway) flowElement);
					   } else {
						   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
								   Bpmn2ChoreographyValidatorMessage.GATEWAY_DIRECTION_ERROR_MESSAGE, flowElement.getName()));
					   }
				   }
			   }

			   // Diverging Gateway
			   int lowerboundIncomingFlowsDivergingGateway = this.DIVERGING_GATEWAY_INCOMING_LOWER_BOUND;
			   int upperboundIncomingFlowsDivergingGateway = this.DIVERGING_GATEWAY_INCOMING_UPPER_BOUND;
			   int lowerboundOutgoingFlowsDivergingGateway = this.DIVERGING_GATEWAY_OUTGOING_LOWER_BOUND;
			   int upperboundOutgoingFlowsDivergingGateway = this.DIVERGING_GATEWAY_OUTGOING_UPPER_BOUND;

			   for (Gateway gateway : divergingGateways) {
				   if (lowerboundIncomingFlowsDivergingGateway > gateway.getIncoming().size()
						   || upperboundIncomingFlowsDivergingGateway < gateway.getIncoming().size()) {
					   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
							   Bpmn2ChoreographyValidatorMessage.DIVERGING_GATEWAY_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE, gateway.getName(), gateway.getIncoming().size()));
				   }
				   if (lowerboundOutgoingFlowsDivergingGateway > gateway.getOutgoing().size()
						   || upperboundOutgoingFlowsDivergingGateway < gateway.getOutgoing().size()) {
					   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
							   Bpmn2ChoreographyValidatorMessage.DIVERGING_GATEWAY_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE, gateway.getName(), gateway.getOutgoing().size()));
				   }
			   }

			   // Converging Gateway
			   int lowerboundIncomingFlowsConvergingGateway = this.CONVERGING_GATEWAY_INCOMING_LOWER_BOUND;
			   int upperboundIncomingFlowsConvergingGateway = this.CONVERGING_GATEWAY_INCOMING_UPPER_BOUND;
			   int lowerboundOutgoingFlowsConvergingGateway = this.CONVERGING_GATEWAY_OUTGOING_LOWER_BOUND;
			   int upperboundOutgoingFlowsConvergingGateway = this.CONVERGING_GATEWAY_OUTGOING_UPPER_BOUND;

			   for (Gateway gateway : convergingGateways) {
				   if (lowerboundIncomingFlowsConvergingGateway > gateway.getIncoming().size()
						   || upperboundIncomingFlowsConvergingGateway < gateway.getIncoming().size()) {
					   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
							   Bpmn2ChoreographyValidatorMessage.CONVERGING_GATEWAY_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE, gateway.getName(), gateway.getIncoming().size()));
				   }
				   if (lowerboundOutgoingFlowsConvergingGateway > gateway.getOutgoing().size()
						   || upperboundOutgoingFlowsConvergingGateway < gateway.getOutgoing().size()) {
					   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
							   Bpmn2ChoreographyValidatorMessage.CONVERGING_GATEWAY_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE, gateway.getName(), gateway.getOutgoing().size()));
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