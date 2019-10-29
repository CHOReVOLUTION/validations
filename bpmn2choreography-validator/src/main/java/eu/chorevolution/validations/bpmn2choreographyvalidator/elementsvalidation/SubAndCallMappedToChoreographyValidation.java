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

import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.SubChoreography;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class SubAndCallMappedToChoreographyValidation extends ElementsValidation {

	   /**
	 * 
	 */
	   private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;

	/**
	 * @param bpmn2ChoreographyValidator
	 */
	   public SubAndCallMappedToChoreographyValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		   this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	   }

	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() {
		   return null;
	   }
	   
	   public Bpmn2ChoreographyValidatorResponse validateElements(final Choreography choreography) throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();
			   
		   for (FlowElement flowElement : choreography.getFlowElements()) {
			   if (flowElement instanceof SubChoreography) {
				   this.validateElements(
						   this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographyToMapSubChoreographyActivity((SubChoreography) flowElement));
			   } else if (flowElement instanceof CallChoreography) {

				   this.validateElements(
						   this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographyToMapCallChoreographyActivity((CallChoreography) flowElement));

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