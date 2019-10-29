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

public class Bpmn2ChoreographyValidatorException extends Exception {
    private static final long serialVersionUID = 7046992133958679460L;

    public Bpmn2ChoreographyValidatorException() {
        super();
    }

    public Bpmn2ChoreographyValidatorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public Bpmn2ChoreographyValidatorException(final String message) {
        super(message);
    }

    public Bpmn2ChoreographyValidatorException(final Throwable cause) {
        super(cause);
    }

}
