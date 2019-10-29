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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public final class Bpmn2ChoreographyValidatorMessage {
    private static final String VALIDATION_CONFIG = "bmpn2choreographyvalidator.messages.properties";
    private static final Properties properties;

    private Bpmn2ChoreographyValidatorMessage() {
        // Do not instantiate
    }

    public static String CHOREOGRAPHY_BOUNDS_ERROR_MESSAGE = "choreography.bounds.error.message";

    public static String DEFAULT_CHOREOGRAPHY_ID = "default.choreography.id";
    public static String DEFAULT_CHOREOGRAPHY_NAME = "default.choreography.name";

    // START EVENT RULES
    public static String START_EVENT_BOUNDS_ERROR_MESSAGE = "start.event.bounds.error.message";
    public static String START_EVENT_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE = "start.event.incoming.flows.bounds.error.message";
    public static String START_EVENT_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE = "start.event.outgoing.flows.bounds.error.message";

    // END EVENT RULES
    public static String END_EVENT_BOUNDS_ERROR_MESSAGE = "end.event.bounds.error.message";
    public static String END_EVENT_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE = "end.event.incoming.flows.bounds.error.message";
    public static String END_EVENT_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE = "end.event.outgoing.flows.bounds.error.message";

    // GATEWAY RULES
    public static String GATEWAY_DIRECTION_ERROR_MESSAGE = "gateway.direction.error.message";
    public static String DIVERGING_GATEWAY_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE = "diverging.gateway.incoming.flows.bounds.error.message";
    public static String DIVERGING_GATEWAY_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE = "diverging.gateway.outgoing.flows.bounds.error.message";

    public static String CONVERGING_GATEWAY_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE = "converging.gateway.incoming.flows.bounds.error.message";
    public static String CONVERGING_GATEWAY_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE = "converging.gateway.outgoing.flows.bounds.error.message";

    // ACTIVITY RULES
    public static String CHOREOGRAPHY_ACTIVITY_INCOMING_FLOWS_BOUNDS_ERROR_MESSAGE = "choreography.activity.incoming.flows.bounds.error.message";
    public static String CHOREOGRAPHY_ACTIVITY_OUTGOING_FLOWS_BOUNDS_ERROR_MESSAGE = "choreography.activity.outgoing.flows.bounds.error.message";

    public static String CALL_CHOREOGRAPHY_ACTIVITY_NOT_HAS_CHOREOGRAPHY_ASSOCIATION_ERROR_MESSAGE = "call.choreography.activity.not.has.choreography.association.error.message";
    public static String SUB_CHOREOGRAPHY_ACTIVITY_NOT_HAS_CHOREOGRAPHY_ASSOCIATION_ERROR_MESSAGE = "sub.choreography.activity.not.has.choreography.association.error.message";

    // CHOREOGRAPHY ACTIVITY PARTICIPANTS RULES
    public static String CHOREOGRAPHY_ACTIVITY_PARTICIPANT_BOUNDS_ERROR_MESSAGE = "choreography.activity.participant.bounds.error.message";

    // CHOREOGRAPHY ACTIVITY FLOWS RULES
    public static String CHOREOGRAPHY_ACTIVITY_FLOW_ERROR_MESSAGE = "choreography.activity.flow.error.message";
    
    // CHOREOGRAPHY TASK MESSAGE NUMBER RULES
    public static String CHOREOGRAPHY_TASK_MESSAGE_NUMBER_BOUNDS_ERROR_MESSAGE = "choreography.task.message.number.bounds.error.message";
    
    // CHOREOGRAPHY TASK INITIATING MESSAGE NUMBER RULES
    public static String CHOREOGRAPHY_TASK_INITIATING_MESSAGE_NUMBER_ERROR_MESSAGE="choreography.task.initiating.message.number.error.message";
    
    // EXCLUSIVE GATEWAY PARTICIPANT RULES
    public static String EXCLUSIVE_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE= "exclusive.gateway.participant.validation.error.message";
    
    // EXCLUSIVE GATEWAY MUST-HAVE CONDITION RULE
    public static String EXCLUSIVE_GATEWAY_MUST_HAVE_CONDITION_VALIDATION_ERROR_MESSAGE= "exclusive.gateway.condition.validation.error.message";
    
    // EVENT-BASED GATEWAY PARTICIPANT RULES
    public static String EVENT_BASED_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE= "event.based.gateway.participant.validation.error.message";
    
    // INCLUSIVE GATEWAY PARTICIPANT RULES
    public static String INCLUSIVE_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE= "inclusive.gateway.participant.validation.error.message";
    
    // INCLUSIVE SPLIT GATEWAY PARTICIPANT RULES
    public static String INCLUSIVE_SPLIT_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE= "inclusive.split.gateway.participant.validation.error.message";  
    
    // PARALLEL GATEWAY PARTICIPANT RULES
    public static String PARALLEL_GATEWAY_PARTICIPANT_VALIDATION_ERROR_MESSAGE= "parallel.gateway.participant.validation.error.message";
    
    // MESSAGE TYPE NOT PRESENT IN XSD
    public static String MESSAGE_TYPE_NOT_PRESENT_IN_XSD_ERROR_MESSAGE="message.type.not.present.in.xsd.error.message";
    
    // XSD FILE NOT WELL FORMED
    public static String XSD_FILE_NOT_WELL_FORMED_ERROR_MESSAGE="xSD.file.not.well.formed.error.message";
    
    // TYPE DOESN'T EXIST
    public static String TYPE_NOT_EXIST_ERROR_MESSAGE="type.not.exist.error.message";
    
    // THE MESSAGE HAS NO TYPE DEFINED
    public static String MESSAGE_TYPE_NOT_DEFINED_ERROR_MESSAGE="message.type.not.defined.error.message";
    
    // THE PARTICIPANTS DECLARED IN THE SUBCHOR AR NOT THE SAME AS INSIDE THE SUBCHOR
    public static String SUBCHOREOGRAPHY_PARTICIPANT_MISSING_ERROR="subchoreography.participant.missing.error.message";
    
    // THE PARTICIPANTS DECLARED IN THE CALLCHOR AR NOT THE SAME AS INSIDE THE SUBCHOR
    public static String CALLCHOREOGRAPHY_PARTICIPANT_MISSING_ERROR="callchoreography.participant.missing.error.message";
    
    // THE PARTICIPANTS MESSAGES MUST HAVE THE SAME TYPE IN THE WHOLE DIAGRAM
    public static String PARTICIPANT_MESSAGE_TYPE_ERROR_MESSAGE="participant.message.type.error.message";
    
    static {
        properties = new Properties();
        try {
            final ClassLoader loader = Bpmn2ChoreographyValidatorMessage.class.getClassLoader();

            final InputStream propFile = loader.getResourceAsStream(VALIDATION_CONFIG);
            if (propFile != null) {
                properties.load(propFile);
                propFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String get(final String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        String value = properties.getProperty(key);
        if (value != null) {
            value.trim();
        }
        return value;
    }

    public static String getMessage(final String key, final Object... arguments) {
        if (arguments.length == 0) {
            return get(key);
        }
        return MessageFormat.format(get(key), arguments);
    }

}
