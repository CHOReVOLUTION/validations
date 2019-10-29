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
import org.eclipse.bpmn2.StartEvent;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class StartEventValidation extends ElementsValidation {

	   /**
	    * This class validates the start event
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public StartEventValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   private final int START_EVENT_UPPER_BOUND = 1;
	   private final int START_EVENT_LOWER_BOUND = 1;
	   private final int START_EVENT_INCOMING_FLOWS_UPPER_BOUND = 0;
	   private final int START_EVENT_INCOMING_FLOWS_LOWER_BOUND = 0;
	   private final int START_EVENT_OUTGOING_FLOWS_UPPER_BOUND = 1;
	   private final int START_EVENT_OUTGOING_FLOWS_LOWER_BOUND = 1;
	   
	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();
		
	      for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
	          int lowerbound = this.START_EVENT_LOWER_BOUND;
	          int upperbound = this.START_EVENT_UPPER_BOUND;

	          List<StartEvent> startEvents = new ArrayList<StartEvent>();
	          for (FlowElement flowElement : choreography.getFlowElements()) {
	             if (flowElement instanceof StartEvent) {
	                startEvents.add((StartEvent) flowElement);
	             }
	          }

	          if (lowerbound > startEvents.size() || upperbound < startEvents.size()) {
	             super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
	                   Bpmn2ChoreographyValidatorMessage.START_EVENT_BOUNDS_ERROR_MESSAGE, choreography.getName(), startEvents.size()));
	          }

	          for (StartEvent startEvent : startEvents) {
	             int lowerboundIncomingFlows = this.START_EVENT_INCOMING_FLOWS_LOWER_BOUND;
	             int upperboundIncomingFlows = this.START_EVENT_INCOMING_FLOWS_UPPER_BOUND;

	             if (lowerboundIncomingFlows > startEvent.getIncoming().size()
	                   || upperboundIncomingFlows < startEvent.getIncoming().size()) {
	            	 super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
	                      Bpmn2ChoreographyValidatorMessage.START_EVENT_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE,
	                      startEvent.getName(), startEvent.getIncoming().size()));
	             }

	             int lowerboundOutgoingFlows = this.START_EVENT_OUTGOING_FLOWS_LOWER_BOUND;
	             int upperboundOutgoingFlows = this.START_EVENT_OUTGOING_FLOWS_UPPER_BOUND;

	             if (lowerboundOutgoingFlows > startEvent.getOutgoing().size()
	                   || upperboundOutgoingFlows < startEvent.getOutgoing().size()) {
	            	 super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
	                      Bpmn2ChoreographyValidatorMessage.START_EVENT_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE,
	                      startEvent.getName(), startEvent.getOutgoing().size()));
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