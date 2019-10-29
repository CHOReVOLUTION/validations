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

public class Bpmn2ChoreographyValidatorRequest implements java.io.Serializable {
    private static final long serialVersionUID = 7046992133958679461L;

    private byte[] bpmn2Content;
    private byte[] bpmn2XSD;
    
	public Bpmn2ChoreographyValidatorRequest() {
        super();
    }

    public Bpmn2ChoreographyValidatorRequest(final byte[] bpmn2Content) {
        super();
        this.bpmn2Content = bpmn2Content;
    }
	
    public Bpmn2ChoreographyValidatorRequest(final byte[] bpmn2Content, final byte[] bpmn2XSD) {
        super();
        this.bpmn2Content = bpmn2Content;
        this.bpmn2XSD = bpmn2XSD;
    }

    public byte[] getBpmn2Content() {
        return bpmn2Content;
    }

    public void setBpmn2Content(final byte[] bpmn2Content) {
        this.bpmn2Content = bpmn2Content;
    }

    public byte[] getBpmn2XSD() {
		return bpmn2XSD;
	}

	public void setBpmn2XSD(byte[] bpmn2xsd) {
		this.bpmn2XSD = bpmn2xsd;
	}

    
}
