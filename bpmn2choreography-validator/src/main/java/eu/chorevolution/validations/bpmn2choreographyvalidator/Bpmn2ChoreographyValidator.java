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
package eu.chorevolution.validations.bpmn2choreographyvalidator;

import java.util.ArrayList;
import java.util.List;

import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ChoreographyActivityBoundsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ChoreographyActivityFlowRulesValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ChoreographyActivityParticipantsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ChoreographyTaskInitiatingMessageNumberValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ChoreographyTaskMessageNumberValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.EndEventValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.EventBasedGatewayParticipantsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ExclusiveGatewayOutgoingConditionValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ExclusiveGatewayParticipantsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.GatewayBoundsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.InclusiveGatewayParticipantsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.MessageTypeXSDValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ParallelGatewayParticipantsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.ParticipantMessageTypeValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.StartEventValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.SubAndCallMappedToChoreographyValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.SubChoreographyParticipantsValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.TypeValidation;
import eu.chorevolution.validations.bpmn2choreographyvalidator.elementsvalidation.XSDTypeValidation;

public class Bpmn2ChoreographyValidator {
   public static final int N_SIZE = Integer.MAX_VALUE;
   
   private List<Bpmn2ChoreographyValidatorResponse> bpmn2ChoreographyValidatorResponseList;
   private Bpmn2ChoreographyValidatorResponse bpmn2ChoreographyValidatorResponse;
   
   private Bpmn2ChoreographyValidatorLoader bpmnLoader;
   private StartEventValidation startEventValidation;
   private EndEventValidation endEventValidation;
   private GatewayBoundsValidation gatewayBoundsValidation;
   private ChoreographyActivityBoundsValidation choreographyActivityBoundsValidation;
   private SubAndCallMappedToChoreographyValidation subAndCallMappedToChoreographyValidation;
   private ChoreographyActivityParticipantsValidation choreographyActivityParticipantsValidation;
   private ChoreographyActivityFlowRulesValidation choreographyActivityFlowRulesValidation;
   private ChoreographyTaskMessageNumberValidation choreographyTaskMessageNumberValidation;
   private ChoreographyTaskInitiatingMessageNumberValidation choreographyTaskInitiatingMessageNumberValidation;
   private ExclusiveGatewayParticipantsValidation exclusiveGatewayParticipantsValidation;
   private ExclusiveGatewayOutgoingConditionValidation exclusiveGatewayOutgoingConditionValidation;
   private EventBasedGatewayParticipantsValidation eventBasedGatewayParticipantsValidation;
   private InclusiveGatewayParticipantsValidation inclusiveGatewayParticipantsValidation;
   private ParallelGatewayParticipantsValidation parallelGatewayParticipantsValidation;
   private MessageTypeXSDValidation messageTypeXSDValidation;
   private XSDTypeValidation xSDTypeValidation;
   private TypeValidation typeValidation;
   private SubChoreographyParticipantsValidation subChoreographyParticipantsValidation;
   private ParticipantMessageTypeValidation participantMessageTypeValidation;
   
   public Bpmn2ChoreographyValidatorResponse validate(final Bpmn2ChoreographyValidatorRequest bpmn2ChoreographyValidatorRequest)
         throws Bpmn2ChoreographyValidatorException {
      // load BPMN2 Choreography model
	   
	  if(bpmn2ChoreographyValidatorRequest.getBpmn2XSD() != null)
		  setBpmnLoader(new Bpmn2ChoreographyValidatorLoader(bpmn2ChoreographyValidatorRequest.getBpmn2Content(), bpmn2ChoreographyValidatorRequest.getBpmn2XSD()));
	  else
		  setBpmnLoader(new Bpmn2ChoreographyValidatorLoader(bpmn2ChoreographyValidatorRequest.getBpmn2Content()));

      
      bpmn2ChoreographyValidatorResponseList = new ArrayList<Bpmn2ChoreographyValidatorResponse>();
      bpmn2ChoreographyValidatorResponse = new Bpmn2ChoreographyValidatorResponse();
      
      startEventValidation = new StartEventValidation(this);
      endEventValidation = new EndEventValidation(this);
      gatewayBoundsValidation = new GatewayBoundsValidation(this);
      choreographyActivityBoundsValidation = new ChoreographyActivityBoundsValidation(this); 
      subAndCallMappedToChoreographyValidation = new SubAndCallMappedToChoreographyValidation(this);
      choreographyActivityParticipantsValidation = new ChoreographyActivityParticipantsValidation(this);
      choreographyActivityFlowRulesValidation = new ChoreographyActivityFlowRulesValidation(this);
      choreographyTaskMessageNumberValidation = new ChoreographyTaskMessageNumberValidation(this);
      choreographyTaskInitiatingMessageNumberValidation = new ChoreographyTaskInitiatingMessageNumberValidation(this);
      exclusiveGatewayParticipantsValidation = new ExclusiveGatewayParticipantsValidation(this);
      exclusiveGatewayOutgoingConditionValidation = new ExclusiveGatewayOutgoingConditionValidation(this);
      eventBasedGatewayParticipantsValidation = new EventBasedGatewayParticipantsValidation(this);
      inclusiveGatewayParticipantsValidation = new InclusiveGatewayParticipantsValidation(this);
      parallelGatewayParticipantsValidation = new ParallelGatewayParticipantsValidation(this);
      messageTypeXSDValidation = new MessageTypeXSDValidation(this);
      xSDTypeValidation = new XSDTypeValidation(this);
      typeValidation = new TypeValidation(this);
      subChoreographyParticipantsValidation = new SubChoreographyParticipantsValidation(this);
      participantMessageTypeValidation = new ParticipantMessageTypeValidation(this);
      
      //validate all elements and add all responses to bpmn2ChoreographyValidatorResponseList
      bpmn2ChoreographyValidatorResponseList.add(startEventValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(endEventValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(gatewayBoundsValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(choreographyActivityBoundsValidation.validateElements());
      
      bpmn2ChoreographyValidatorResponseList.add(subAndCallMappedToChoreographyValidation.validateElements(getBpmnLoader().getDefaultChoreography()));

      bpmn2ChoreographyValidatorResponseList.add(choreographyActivityParticipantsValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(choreographyActivityFlowRulesValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(choreographyTaskMessageNumberValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(choreographyTaskInitiatingMessageNumberValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(exclusiveGatewayParticipantsValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(exclusiveGatewayOutgoingConditionValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(eventBasedGatewayParticipantsValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(inclusiveGatewayParticipantsValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(parallelGatewayParticipantsValidation.validateElements());
      
      if(bpmn2ChoreographyValidatorRequest.getBpmn2XSD() != null) {
          bpmn2ChoreographyValidatorResponseList.add(messageTypeXSDValidation.validateElements());
          bpmn2ChoreographyValidatorResponseList.add(xSDTypeValidation.validateElements());
      }
      else {
    	  bpmn2ChoreographyValidatorResponseList.add(typeValidation.validateElements());
      }
          
      bpmn2ChoreographyValidatorResponseList.add(subChoreographyParticipantsValidation.validateElements());
      bpmn2ChoreographyValidatorResponseList.add(participantMessageTypeValidation.validateElements());
      
      //I merge all the choreography errors in only one response, if i have at least one error the response is false
      bpmn2ChoreographyValidatorResponse = Bpmn2ChoreographyValidatorResponse.mergeAll(bpmn2ChoreographyValidatorResponseList);
      return bpmn2ChoreographyValidatorResponse;
   }

   
public Bpmn2ChoreographyValidatorLoader getBpmnLoader() {
	return bpmnLoader;
}

public void setBpmnLoader(Bpmn2ChoreographyValidatorLoader bpmnLoader) {
	this.bpmnLoader = bpmnLoader;
}
   
   
   

   //
   //
   //
   //
   // // get outgoing transition: We assume that there is only one
   // // outgoing transition
   // if (choreographyActivity.getOutgoing().isEmpty()) {
   // throw new Bpmn2ChoreographyValidatorException("BPMN2 Choreography
   // Activity <"
   // + choreographyActivity.getName() + "> has not outgoing transition");
   // } else if (choreographyActivity.getOutgoing().size() > 1) {
   // throw new Bpmn2ChoreographyValidatorException("BPMN2 Choreography
   // Activity <"
   // + choreographyActivity.getName() + "> has more than one outgoing
   // transition");
   // }
   //
   //
   //

   
   
   
   
}
