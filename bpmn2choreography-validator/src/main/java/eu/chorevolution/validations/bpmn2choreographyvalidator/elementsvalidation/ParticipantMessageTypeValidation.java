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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubChoreography;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorException;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorMessage;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class ParticipantMessageTypeValidation extends ElementsValidation {

	   /**
	    * This class validates that all the SAME message types are the same for the same user in a choreography
	    *  If an user has two different messages with the same name, they must have the same data type
	 * 
	 */
	private final Bpmn2ChoreographyValidator bpmn2ChoreographyValidator;


	/**
	 * @param bpmn2ChoreographyValidator
	 */
	public ParticipantMessageTypeValidation(Bpmn2ChoreographyValidator bpmn2ChoreographyValidator) {
		this.bpmn2ChoreographyValidator = bpmn2ChoreographyValidator;
	}


	   
	   @Override
	   public Bpmn2ChoreographyValidatorResponse validateElements() throws Bpmn2ChoreographyValidatorException {
		   super.ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

		   //i should verify that each message with the same name from the same participant has the same data type in the same diagram
		   //i scan each chor activity, when find a message, put user-messagetype in a structure.
		   //if i find a different user-msgname-msgtype combination i raise the error.
		   //in the first cicle i count the occurrencies, in the second i raise the errors
		   
		   //list: userid and messagename and messagetype
		   List<MessageSenderMessageNameMessageType> messageSenderMessageNameMessageType = new ArrayList<MessageSenderMessageNameMessageType>();
		   
		   for (Choreography choreography : this.bpmn2ChoreographyValidator.getBpmnLoader().getChoreographies()) {
			   for(FlowElement flowEle : choreography.getFlowElements()) {
				   if(flowEle instanceof ChoreographyTask) {
					   ChoreographyTask chorTask = (ChoreographyTask) flowEle;
					   for(MessageFlow messageFlow : chorTask.getMessageFlowRef()) {

						   String messageSenderId=((Participant)messageFlow.getSourceRef()).getId();
						   String messageName=messageFlow.getMessageRef().getName();
						   String messageType="No Data-Type Defined";
						   
						   try {
							   messageType=((BasicEObjectImpl)messageFlow.getMessageRef().getItemRef().getStructureRef()).eProxyURI().fragment();
						   }
						   catch (Exception e) {
							   //already managed in TypeValidation
						   }
						   //now i have to visit the list to find the association userid-messagename-messagetype
						   boolean isPresent=false;
						   for(MessageSenderMessageNameMessageType toVerify : messageSenderMessageNameMessageType) {
							   if(toVerify.getMsgSender().equals(messageSenderId)&&toVerify.getMsgName().equals(messageName)&&toVerify.getMsgType().equals(messageType)) {
								   //this userid-name-type is already defined, i add the occurrency
								   isPresent=true;
								   toVerify.addOccurrency();
								   break;
							   }
						   }
						   
						   //if the association userid-messagename-messagetype is not present, add to the list
						   if(!isPresent) {
							   messageSenderMessageNameMessageType.add(new MessageSenderMessageNameMessageType(messageSenderId, messageName, messageType));
						   }


					   }


				   }
			   
			   }
			   
			   //Second part
			   //now i have the list with occurrencies, i can calculate the list with major occurrency
			   //and return that if i have an error

			   //is the list with all duplicates messages, on the end of this control i raise all errors based on this list
			   List<MessageSenderMessageNameMessageType> duplicateMessages = new ArrayList<MessageSenderMessageNameMessageType>();
			   
			   for(MessageSenderMessageNameMessageType toVerify : messageSenderMessageNameMessageType) {
				   for(MessageSenderMessageNameMessageType toVerifyInside : messageSenderMessageNameMessageType) {
						   if(toVerify.getMsgSender().equals(toVerifyInside.getMsgSender())&&toVerify.getMsgName().equals(toVerifyInside.getMsgName())) {
							   boolean isPresent=false;
							   for(MessageSenderMessageNameMessageType duplicated : duplicateMessages) {
								   if(toVerifyInside.getMsgSender().equals(duplicated.getMsgSender())&&toVerifyInside.getMsgName().equals(duplicated.getMsgName())) {
									   isPresent=true;
									   if(duplicated.getOccurrencesNum()<toVerifyInside.getOccurrencesNum()) {
										   duplicated.setMsgName(toVerifyInside.getMsgName());
										   duplicated.setMsgType(toVerifyInside.getMsgType());
										   duplicated.setMsgSender(toVerifyInside.getMsgSender());
										   duplicated.setOccurrencesNum(toVerifyInside.getOccurrencesNum());
									   }
								   }
							   }
							   if(!isPresent) {
								   duplicateMessages.add(toVerifyInside);
							   }
						   }
				   }
			   }

			   

			   //now i check for the original tasks and compare the lists
			   
			   for(FlowElement flowEle : choreography.getFlowElements()) {
				   if(flowEle instanceof ChoreographyTask) {
					   ChoreographyTask chorTask = (ChoreographyTask) flowEle;
					   for(MessageFlow messageFlow : chorTask.getMessageFlowRef()) {

						   String messageSenderId=((Participant)messageFlow.getSourceRef()).getId();
						   String messageName=messageFlow.getMessageRef().getName();
						   String messageType=null;
						   
						   try {
							   messageType=((BasicEObjectImpl)messageFlow.getMessageRef().getItemRef().getStructureRef()).eProxyURI().fragment();
						   }
						   catch (Exception e) {
							   //already managed in TypeValidation
						   }
						   
						   for(MessageSenderMessageNameMessageType duplicated : duplicateMessages) {
							   if(duplicated.getMsgSender().equals(messageSenderId)&&duplicated.getMsgName().equals(messageName)&&!duplicated.getMsgType().equals(messageType)) {
								  
								   String receiverParticipant=null;
								   for(Participant receiver : chorTask.getParticipantRefs()) {
									   if(receiver.getId()!=chorTask.getInitiatingParticipantRef().getId())
										   receiverParticipant=receiver.getName();
								   }
								   
								   if(messageType!=null) {
									   super.ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
											   Bpmn2ChoreographyValidatorMessage.PARTICIPANT_MESSAGE_TYPE_ERROR_MESSAGE, 
											   messageName, choreography.getName(), chorTask.getName(), chorTask.getInitiatingParticipantRef().getName(), receiverParticipant, messageType, duplicated.getMsgType()));			
						   
								   }
							   }
						   
					   }
				   }
					   
				 }
			 }


			   
			   
		   }


	      return super.ValidatorResponse;
	   }


	   
	   private class MessageSenderMessageNameMessageType {
		   private String msgSender;
		   private String msgName;
		   private String msgType;
		   private int occurrencesNum;
		   
		   //in case of creation from 0
		   MessageSenderMessageNameMessageType(String msgSender, String msgName, String msgType) {
			   this.setMsgSender(msgSender);
			   this.setMsgName(msgName);
			   this.setMsgType(msgType);
			   this.occurrencesNum=1;
		   }

		public void addOccurrency() {
			this.occurrencesNum++;
		}
		   

		
		public int getOccurrencesNum() {
			return this.occurrencesNum;
		}
		
		
		public void setOccurrencesNum(int occurrencesNum) {
			this.occurrencesNum=occurrencesNum;
		}

		public String getMsgSender() {
			return msgSender;
		}

		public void setMsgSender(String msgSender) {
			this.msgSender = msgSender;
		}

		public String getMsgName() {
			return msgName;
		}

		public void setMsgName(String msgName) {
			this.msgName = msgName;
		}

		public String getMsgType() {
			return msgType;
		}

		public void setMsgType(String msgType) {
			this.msgType = msgType;
		}

		   
		   
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