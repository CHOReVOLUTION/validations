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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilderFactory;


import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.SubChoreography;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.w3c.dom.Document;


/**
 * This class loads a BPMN model AND/OR an XSD with messages types
 */
public class Bpmn2ChoreographyValidatorLoader {
	
	private static final int N_SIZE = Integer.MAX_VALUE;
	private final int CHOREOGRAPHY_UPPER_BOUND = N_SIZE;
	private final int CHOREOGRAPHY_LOWER_BOUND = 1;
	private Bpmn2ChoreographyValidatorResponse ValidatorResponse;


	
    private static final String TEMPFILE_SUFFIX = "bpmn2choreographyvalidator";
    private static final String BPMN2_FILE_EXTENSION = ".bpmn2";
    private static final String XSD_FILE_EXTENSION = ".xsd";
    
    private DocumentRoot documentRoot;
    private Document xSDDocumentRoot;

    /**
     * this property contains all the choreography containd in the BPMN file
     */
    private List<Choreography> choreographies;

    /**
     * this property is a reference of default BPMN choreography
     */
    private Choreography defaultChoreography;

    public Bpmn2ChoreographyValidatorLoader(final byte[] bpmnContent) throws Bpmn2ChoreographyValidatorException {
    	
		ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

        this.documentRoot = getBpmn2DocumentRoot(bpmnContent);

        this.choreographies = new ArrayList<Choreography>();
        this.defaultChoreography = null;

        // get all choreography in the BPMN2 Model and set the Default
        // choreography
        for (EObject definition : documentRoot.getDefinitions().eContents()) {
            if (definition instanceof Choreography) {
                Choreography choreography = (Choreography) definition;
                choreographies.add(choreography);

                // set default choreography
                if (choreography.getId()
                        .equalsIgnoreCase(Bpmn2ChoreographyValidatorMessage
                                .getMessage(Bpmn2ChoreographyValidatorMessage.DEFAULT_CHOREOGRAPHY_ID))
                        || choreography.getName().equalsIgnoreCase(Bpmn2ChoreographyValidatorMessage
                                .getMessage(Bpmn2ChoreographyValidatorMessage.DEFAULT_CHOREOGRAPHY_NAME))) {
                    defaultChoreography = choreography;
                }
            }
        }

        int lowerbound = this.CHOREOGRAPHY_LOWER_BOUND;
        int upperbound = this.CHOREOGRAPHY_UPPER_BOUND;

        if (lowerbound > choreographies.size() || upperbound < choreographies.size()) {
        	ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
	                   Bpmn2ChoreographyValidatorMessage.CHOREOGRAPHY_BOUNDS_ERROR_MESSAGE));
        }

        // throw Bpmn2ChoreographyValidatorException
        if (defaultChoreography == null) {
        	ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
	                   Bpmn2ChoreographyValidatorMessage.DEFAULT_CHOREOGRAPHY_NAME));
        }

    }

    public Bpmn2ChoreographyValidatorLoader(final byte[] bpmnContent, final byte[] xSDContent) throws Bpmn2ChoreographyValidatorException {
    	
		ValidatorResponse = new Bpmn2ChoreographyValidatorResponse();

        this.documentRoot = getBpmn2DocumentRoot(bpmnContent);
        this.xSDDocumentRoot = getXSDDocumentRoot(xSDContent);
        
        this.choreographies = new ArrayList<Choreography>();
        this.defaultChoreography = null;

        // get all choreography in the BPMN2 Model and set the Default
        // choreography
        for (EObject definition : documentRoot.getDefinitions().eContents()) {
            if (definition instanceof Choreography) {
                Choreography choreography = (Choreography) definition;
                choreographies.add(choreography);

                // set default choreography
                if (choreography.getId()
                        .equalsIgnoreCase(Bpmn2ChoreographyValidatorMessage
                                .getMessage(Bpmn2ChoreographyValidatorMessage.DEFAULT_CHOREOGRAPHY_ID))
                        || choreography.getName().equalsIgnoreCase(Bpmn2ChoreographyValidatorMessage
                                .getMessage(Bpmn2ChoreographyValidatorMessage.DEFAULT_CHOREOGRAPHY_NAME))) {
                    defaultChoreography = choreography;
                }
            }
        }

        int lowerbound = this.CHOREOGRAPHY_LOWER_BOUND;
        int upperbound = this.CHOREOGRAPHY_UPPER_BOUND;

        if (lowerbound > choreographies.size() || upperbound < choreographies.size()) {
        	ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
	                   Bpmn2ChoreographyValidatorMessage.CHOREOGRAPHY_BOUNDS_ERROR_MESSAGE));
        }

        // throw Bpmn2ChoreographyValidatorException
        if (defaultChoreography == null) {
        	ValidatorResponse.addError(Bpmn2ChoreographyValidatorMessage.getMessage(
	                   Bpmn2ChoreographyValidatorMessage.DEFAULT_CHOREOGRAPHY_NAME));
        }
        
        

    }
    
    
    public List<Choreography> getChoreographies() {
        return choreographies;
    }

    public Choreography getDefaultChoreography() {
        return defaultChoreography;
    }
    
    public Document getXSDDocumentRoot() {
        return xSDDocumentRoot;
    }

    public DocumentRoot getDocumentRoot() {
        return documentRoot;
    }

    public static DocumentRoot getBpmn2DocumentRoot(final byte[] bpmnContent)
            throws Bpmn2ChoreographyValidatorException {

        File bpmnFile;
        try {
            bpmnFile = File.createTempFile(TEMPFILE_SUFFIX, BPMN2_FILE_EXTENSION);
            IOUtils.write(bpmnContent, FileUtils.openOutputStream(bpmnFile));
        } catch (IOException e1) {
            throw new Bpmn2ChoreographyValidatorException("Internal Error while creating the BPMN2 Model");
        }

        URI bpmnURI = URI.createURI(bpmnFile.toURI().toString(),true);

        // register the BPMN2ResourceFactory in Factory registry
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        reg.getExtensionToFactoryMap().put("bpmn", new Bpmn2ResourceFactoryImpl());
        reg.getExtensionToFactoryMap().put("bpmn2", new Bpmn2ResourceFactoryImpl());

        // load the resource and resolve
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.createResource(bpmnURI);

        try {
            // load the resource
            resource.load(null);
        } catch (IOException e) {
            throw new Bpmn2ChoreographyValidatorException("Error to load the BPMN2 Choreography Model");
        } finally {
        	FileDeleteStrategy.FORCE.deleteQuietly(bpmnFile);
        }

        if (!resource.getContents().isEmpty() && resource.getContents().get(0) instanceof DocumentRoot) {
            return (DocumentRoot) resource.getContents().get(0);
        }

        throw new Bpmn2ChoreographyValidatorException("BPMN2 model is loaded but not contain a BPMN2 DocumentRoot");

    }
    
    
    public static Document getXSDDocumentRoot(final byte[] xSDContent)
            throws Bpmn2ChoreographyValidatorException {


    	Document xSDDocumentRoot = null;
        File xSDFile;
        try {
            xSDFile = File.createTempFile(TEMPFILE_SUFFIX, XSD_FILE_EXTENSION);
            IOUtils.write(xSDContent, FileUtils.openOutputStream(xSDFile));
        } catch (IOException e1) {
            throw new Bpmn2ChoreographyValidatorException("Internal Error while creating the XSD Model");
        }

        URI xSDURI = URI.createURI(xSDFile.toURI().toString());

        // register the xSDResourceFactory in Factory registry
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        reg.getExtensionToFactoryMap().put("xSD", new Bpmn2ResourceFactoryImpl());
        reg.getExtensionToFactoryMap().put("xSD2", new Bpmn2ResourceFactoryImpl());

        // load the resource and resolve
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.createResource(xSDURI);

        try {
        	xSDDocumentRoot = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(xSDURI.toString());
        	
        }catch (Exception e) {
        	
        }finally {
        	FileDeleteStrategy.FORCE.deleteQuietly(xSDFile);
		}
        
        if(xSDDocumentRoot != null)
            return xSDDocumentRoot;

        throw new Bpmn2ChoreographyValidatorException("XSD model is loaded but not contain a XSD DocumentRoot");

    }

    

    
    /**
     * This method it is used to get a {@link Choreography} instance for the
     * {@link ChoreographyActivity} input element
     * 
     * @param choreographyActivity
     *            the {@link ChoreographyActivity} element
     * @return a {@link Choreography} instance for the
     *         {@link ChoreographyActivity} element
     */
    private Choreography getChoreography(final ChoreographyActivity choreographyActivity) {
        for (Choreography choreography : choreographies) {
            if (choreography.getName().equalsIgnoreCase(choreographyActivity.getName())) {
                return choreography;
            }
        }
        return null;
    }

    /**
     * This method it is used to get a {@link Choreography} instance for the
     * {@link CallChoreography} input element
     * 
     * @param callChoreography
     *            the {@link CallChoreography} element
     * @return a {@link Choreography} instance for the {@link CallChoreography}
     *         input element
     * @throws Bpmn2ChoreographyValidatorException
     *             a {@link Bpmn2ChoreographyValidatorException} is thrown if
     *             the BPMN model not contains a instance for the
     *             {@link CallChoreography} element
     */
    public Choreography getChoreographyToMapCallChoreographyActivity(final CallChoreography callChoreography)
            throws Bpmn2ChoreographyValidatorException {
        Choreography choreography = getChoreography(callChoreography);
        if (choreography != null) {
            return choreography;
        }

        throw new Bpmn2ChoreographyValidatorException(Bpmn2ChoreographyValidatorMessage.getMessage(
                Bpmn2ChoreographyValidatorMessage.CALL_CHOREOGRAPHY_ACTIVITY_NOT_HAS_CHOREOGRAPHY_ASSOCIATION_ERROR_MESSAGE,
                callChoreography.getName(), callChoreography.getName()));
    }

    /**
     * This method it is used to get a {@link Choreography} instance for the
     * {@link SubChoreography} input element
     * 
     * @param subChoreography
     *            the {@link SubChoreography} element
     * @return a {@link Choreography} instance for the {@link SubChoreography}
     *         input element
     * @throws Bpmn2ChoreographyValidatorException
     *             a {@link Bpmn2ChoreographyValidatorException} is thrown if
     *             the BPMN model not contains a instance for the
     *             {@link SubChoreography} element
     */
    public Choreography getChoreographyToMapSubChoreographyActivity(final SubChoreography subChoreography)
            throws Bpmn2ChoreographyValidatorException {
        Choreography choreography = getChoreography(subChoreography);
        if (choreography != null) {
            return choreography;
        }

        throw new Bpmn2ChoreographyValidatorException(Bpmn2ChoreographyValidatorMessage.getMessage(
                Bpmn2ChoreographyValidatorMessage.SUB_CHOREOGRAPHY_ACTIVITY_NOT_HAS_CHOREOGRAPHY_ASSOCIATION_ERROR_MESSAGE,
                subChoreography.getName(), subChoreography.getName()));
    }
}
