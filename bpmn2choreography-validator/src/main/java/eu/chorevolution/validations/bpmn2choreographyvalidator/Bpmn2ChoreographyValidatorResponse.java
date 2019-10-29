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

public class Bpmn2ChoreographyValidatorResponse implements java.io.Serializable {
    private static final long serialVersionUID = 7046992133958679462L;

    //if valid is true then the choreography is valid. if false, it's not valid.
    private boolean valid;
    private List<String> errors;

    public Bpmn2ChoreographyValidatorResponse() {
        super();
        this.valid = true;
        this.errors = new ArrayList<String>();
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addErrors(List<String> errors) {
        this.errors.addAll(errors);
        this.valid = false;
    }

    public void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }
    
    public static Bpmn2ChoreographyValidatorResponse mergeAll(List<Bpmn2ChoreographyValidatorResponse> responseList) {
    	Bpmn2ChoreographyValidatorResponse response=new Bpmn2ChoreographyValidatorResponse();
    	
    	for(Bpmn2ChoreographyValidatorResponse chorResponse: responseList) {
    		if(!chorResponse.getErrors().isEmpty())
    			response.addErrors(chorResponse.getErrors());
    			
    	}
    	
    	return response;
    }

}
