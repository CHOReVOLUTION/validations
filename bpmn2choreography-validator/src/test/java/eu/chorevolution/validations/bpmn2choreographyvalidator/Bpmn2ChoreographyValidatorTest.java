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

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidator;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorRequest;
import eu.chorevolution.validations.bpmn2choreographyvalidator.Bpmn2ChoreographyValidatorResponse;

public class Bpmn2ChoreographyValidatorTest {
    private static final String TEST_RESOURCES = "." + File.separatorChar + "src" + File.separatorChar + "test"
            + File.separatorChar + "resources" + File.separatorChar;

    private static final String BPMN2_FILE_EXTENSION = ".bpmn2";
    private static final String INPUT_TEST_RESOURCES_FOLDER_NAME = "input";
    private static final String INPUT_TEST_XSD_NAME = "types.xsd";


    private static Logger logger = LoggerFactory.getLogger(Bpmn2ChoreographyValidatorTest.class);
    @Rule
    public TestName currentTestName = new TestName();

    @Before
    public void setUp() {
    }

    @Test
    public void test_01() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_02() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_03() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_04() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_05() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_06() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_07() {
        Assert.assertFalse(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_08() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_09() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_10() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }

    @Test
    public void test_11_wp4() {
        Assert.assertFalse(runValidator(currentTestName.getMethodName()));//TODO ASSERTTRUE
    }

    @Test
    public void test_12_wp5() {
        Assert.assertFalse(runValidator(currentTestName.getMethodName()));//TODO ASSERTTRUE
    }
    
    @Test
    public void test_13() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }
    
    @Test
    public void test_14() {
        Assert.assertTrue(runValidator(currentTestName.getMethodName()));
    }    
    
    //until here all tests are true, now must be false
    
    @Test
    public void test_15() {
        Assert.assertFalse(runValidator(currentTestName.getMethodName()));
    } 
    
    @Test
    public void test_16() {
        Assert.assertFalse(runValidator(currentTestName.getMethodName()));
    } 
    
    @Test
    public void test_17() {
        Assert.assertFalse(runValidator(currentTestName.getMethodName()));
    } 
    
    @Test
    public void test_18() {
        Assert.assertFalse(runValidator(currentTestName.getMethodName()));
    } 
    
    private boolean runValidator(String testName) {
        String bpmnIn = TEST_RESOURCES + testName + File.separatorChar + INPUT_TEST_RESOURCES_FOLDER_NAME
                + File.separatorChar + testName + BPMN2_FILE_EXTENSION;
        String xsdIn = TEST_RESOURCES + testName + File.separatorChar + INPUT_TEST_RESOURCES_FOLDER_NAME
                + File.separatorChar + INPUT_TEST_XSD_NAME;
        try {
            Bpmn2ChoreographyValidatorRequest bpmn2ChoreographyValidatorRequest = new Bpmn2ChoreographyValidatorRequest();
            bpmn2ChoreographyValidatorRequest.setBpmn2Content(FileUtils.readFileToByteArray(new File(bpmnIn)));
            
            //if the file exists I'll do the message type verification
            if((new File(xsdIn)).exists())
            	bpmn2ChoreographyValidatorRequest.setBpmn2XSD(FileUtils.readFileToByteArray(new File(xsdIn)));
            
            Bpmn2ChoreographyValidatorResponse validatorResponse = new Bpmn2ChoreographyValidator().validate(bpmn2ChoreographyValidatorRequest);
            if(validatorResponse.isValid()) {
            	return true;
            }
            else {
            	logger.error(testName + " > " + validatorResponse.getErrors().toString());
                return false;
            }
            
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error(testName + " > " + errors.toString());
            return false;
        }

    }

}
