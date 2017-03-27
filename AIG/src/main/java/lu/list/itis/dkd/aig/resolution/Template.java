/**
 * Copyright (c) 2016-2017  Luxembourg Institute of Science and Technology (LIST).
 * 
 * This software is licensed under the Apache License, Version 2.0 (the "License") ; you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at : http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * for more information about the software, please contact info@list.lu
 */
package lu.list.itis.dkd.aig.resolution;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.ext.com.google.common.collect.Lists;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.VariableBuilder;
import lu.list.itis.dkd.aig.process.InitializationProcess;
import lu.list.itis.dkd.aig.process.OptimizationProcess;
import lu.list.itis.dkd.aig.process.ResolutionProcess;
import lu.list.itis.dkd.aig.process.ResolutionProcessBuilder;
import lu.list.itis.dkd.aig.util.DocumentConverter;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;


/**
 * Class containing all the information extracted from an underlying XML template.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public class Template {
    private final Set<URI> variableKeys = new HashSet<>();
    private final SetMultimap<URI, Variable> variables = HashMultimap.create();
    private final Map<URI, InitializationProcess> initializationProcesses = new LinkedHashMap <>();
    private final Map<URI, OptimizationProcess> optimizationProcesses = new LinkedHashMap<>();
    private final TreeSet<Dependency> dependencies = new TreeSet<>();
    private final HashMap<URI, Dependency> dependencyByHead = new HashMap<>();

    private URI identifier;
    private InteractionType interactionType;
    private CorrectResponseAttributionType correctResponseAttributionType;
    private DistractorAttributionType distractorAttributionType;

    private String itemLayer;
    private final HashMap<String, String> templateMetadata = new HashMap<>();

    // private TaskModel taskModel; //TODO To be implemented in a latter version.

    private static final Logger logger = Logger.getLogger(Template.class.getSimpleName());

    static {
        Template.logger.setLevel(Level.parse(PropertiesFetcher.getProperties().getProperty(Externalization.LOGGER_LEVEL_PROEPRTY, Level.INFO.toString())));
    }

    /**
     * Constructor initializing all fields.
     *
     * @param document
     *        The document containing all the information to build the template instance from.
     * @pre null != document
     * @throws TemplateParseException
     *         Exception thrown when parsing the document failed or if any of the preconditions
     *         associated with the document failed to hold, e.g. if any of the mandatory fields are
     *         not provided or if values are given outside of their accepted inputs domain.
     */
    public Template(final @Nullable Document document) throws TemplateParseException {
        if (null == document) {
            throw new TemplateParseException("The template document must be provided and cannot be null!"); //$NON-NLS-1$
        }
        // TODO validate xml document

        populateTemplateMetadataValues(document);
        extractItem(document);
        
        try {
            identifier = new URI(templateMetadata.get(Externalization.IDENTIFIER_ELEMENT));
            interactionType = InteractionType.fromString((templateMetadata.get(Externalization.INTERACTION_TYPE_NODE)).toUpperCase());
        } catch (final URISyntaxException e) {
            Template.logger.log(Level.SEVERE, "No unique identifier could be extracted from the template. Check for correct nesting and spelling!", e); //$NON-NLS-1$
            throw new TemplateParseException("No unique identifier could be extracted from the template. Check for correct nesting and spelling!", e); //$NON-NLS-1$
        } catch (final IllegalArgumentException | NullPointerException e) {
            Template.logger.log(Level.SEVERE, "No construct type could be extracted from the template. Check your node \"metadata\" and the nested \"constructType\" nodes for correct nesting and spelling!", e); //$NON-NLS-1$
            throw new TemplateParseException("No construct type could be extracted from the template. Check your node \"metadata\" and the nested \"constructType\" nodes for correct nesting and spelling! ", e); //$NON-NLS-1$
        } catch (final ResolutionException e) {
            Template.logger.log(Level.SEVERE, "No interaction type could be extracted from the template. Check your node \"metadata\" and the nested \"interaction\" nodes for correct nesting and spelling!", e); //$NON-NLS-1$
            throw new TemplateParseException("No interaction type could be extracted from the template. Check your node \"metadata\" and the nested \"interaction\" nodes for correct nesting and spelling! ", e); //$NON-NLS-1$
        }

        extractVariableKeys(document);
        extractDependencies(document);
        extractProcesses(document);

        // TODO Sanity checks!
    }


    /**
     * Method used to extract and build all processes from the template.
     *
     * @throws TemplateParseException
     *         Thrown when either the extraction or the instantiation of a process failed.
     *
     */
    @SuppressWarnings("null")
    private void extractProcesses(final Document document) throws TemplateParseException {
        List<Element> processElements;

        try {
            final XPathExpression<Element> xpath = XPathFactory.instance().compile(Externalization.PROCESS_XPATH, Filters.element());
            processElements = xpath.evaluate(document);
            if ((null == processElements) || (processElements.isEmpty())) {
                // throw new TemplateParseException("No processes could be extracted from the
                // template. Check your node \"processes\" and the subsequent \"process\" nodes for
                // correct nesting and spelling!"); //$NON-NLS-1$
                Template.logger.log(Level.WARNING, "No processes could be extracted from the template."); //$NON-NLS-1$
            }
        } catch (final NullPointerException | IllegalArgumentException | IllegalStateException e) {
            throw new TemplateParseException("No processes could be extracted from the template. Check your node \"processes\" and the subsequent \"process\" nodes for correct nesting and spelling!", e); //$NON-NLS-1$
        }

        for (final Element processElement : processElements) {
            final ResolutionProcess process = ResolutionProcessBuilder.buildProcessFrom(processElement);

            if (process instanceof OptimizationProcess) {
                optimizationProcesses.put(process.getIdentifier(), (OptimizationProcess) process);
            } else { // Both are exclusive.
                initializationProcesses.put(process.getIdentifier(), (InitializationProcess) process);
            }
        }
    }

    /**
     * Method used to extract all dependency statements from the template document.
     *
     * @throws TemplateParseException
     *         Thrown whenever the extraction of dependencies failed or when the retrieved
     *         dependency element did not contain all the correct building blocks to instantiate a
     *         new instance of a dependency.
     */
    @SuppressWarnings("null")
    private void extractDependencies(final Document document) throws TemplateParseException {
        List<Element> dependencyElements;
        try {
            final XPathExpression<Element> xpath = XPathFactory.instance().compile(Externalization.DEPENDENCY_XPATH, Filters.element());
            // The new list is required to be able to remove elements.
            dependencyElements = Lists.newArrayList(xpath.evaluate(document));

            if ((null == dependencyElements) || (dependencyElements.isEmpty())) {
                Template.logger.log(Level.WARNING, "No dependencies could be extracted from the template."); //$NON-NLS-1$
            }

            dependencyElements.removeIf(Objects::isNull);
        } catch (final NullPointerException | IllegalArgumentException | IllegalStateException e) {
            throw new TemplateParseException("No dependencies could be extracted from the template. Check your node \"variableDependencies\" and the subsequent \"depencency\" nodes for correct nesting and spelling!", e); //$NON-NLS-1$
        }

        for (final Element dependencyElement : dependencyElements) {
            final Dependency dependency = new Dependency(dependencyElement);
            dependencies.add(dependency);
            dependencyByHead.put(dependency.getHead(), dependency);
        }
    }

    /**
     * Helper method used to extract all variable's keys from the template document and register the
     * blueprint to build the variable on demand by its key with the {@link VariableBuilder}.
     *
     * @throws TemplateParseException
     *         Thrown when the provided variable identifier was not a valid URI or if no variables
     *         could be extracted.
     */
    @SuppressWarnings("null")
    private void extractVariableKeys(final Document document) throws TemplateParseException {
        List<Element> variableElements;
        try {
            final XPathExpression<Element> xpath = XPathFactory.instance().compile(Externalization.VARIABLE_XPATH, Filters.element());
            variableElements = xpath.evaluate(document);
            if ((null == variableElements) || (variableElements.isEmpty())) {
                // throw new TemplateParseException("No variables could be extracted from the
                // template. Check your node \"variableDefinitions\" and the subsequent \"variable\"
                // nodes for correct nesting and spelling!"); //$NON-NLS-1$
                Template.logger.log(Level.WARNING, "No variables could be extracted from the template."); //$NON-NLS-1$
            }
        } catch (final NullPointerException | IllegalArgumentException | IllegalStateException e) {
            throw new TemplateParseException("No variables could be extracted from the template. Check your node \"variableDefinitions\" and the subsequent \"variable\" nodes for correct nesting and spelling!"); //$NON-NLS-1$
        }

        for (final Element variableElement : variableElements) {
            variableKeys.add(VariableBuilder.registerBlueprint(variableElement));
        }
    }

    /**
     * Method used to extract the metadata node from the provided document template.
     *
     * @param document
     *        The document to extract the metadata from.
     * @throws TemplateParseException
     *         Thrown when the metadata could not be extracted from the document.
     */
    private void populateTemplateMetadataValues(final Document document) throws TemplateParseException {
        Element metadataRoot;
        try {
            final XPathExpression<Element> xpath = XPathFactory.instance().compile(Externalization.TEMPLATE_METADATA_XPATH, Filters.element());
            metadataRoot = xpath.evaluateFirst(document);
        } catch (final NullPointerException | IllegalArgumentException | IllegalStateException e) {
            throw new TemplateParseException("Compiling the XPath expression to resolve the template metadata root node failed!", e); //$NON-NLS-1$
        }

        for (final Element metadataElement : metadataRoot.getChildren()) {
            templateMetadata.put(metadataElement.getName(), metadataElement.getText());
        }
    }

    /**
     * Method used for extracting the QTI item husk from an item template.
     *
     * @throws TemplateParseException
     *         TODO
     */
    private void extractItem(final Document document) throws TemplateParseException {
        Element qtiRoot;

        try {
            final XPathExpression<Element> xpath = XPathFactory.instance().compile(Externalization.QTI_XPATH, Filters.element());
            qtiRoot = xpath.evaluateFirst(document);
        } catch (final NullPointerException | IllegalArgumentException | IllegalStateException e) {
            throw new TemplateParseException("Compiling the Xpath expression to resolve the QTI root node failed!", e); //$NON-NLS-1$
        }

        itemLayer = DocumentConverter.convertDocumentToString(new Document(qtiRoot.detach()));
        itemLayer = itemLayer.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Simple getter method for variables.
     *
     * @return The value of variables.
     */
    @SuppressWarnings("null")
    public SetMultimap<URI, Variable> getVariables() {
        return variables;
    }

    /**
     * Simple getter method for initializationProcesses.
     *
     * @return The value of initializationProcesses.
     */
    @SuppressWarnings("null")
    public Map<URI, InitializationProcess> getInitializationProcesses() {
        return initializationProcesses;
    }

    /**
     * Simple getter method for optimizationProcesses.
     *
     * @return The value of optimizationProcesses.
     */
    @SuppressWarnings("null")
    public Map<URI, OptimizationProcess> getOptimizationProcesses() {
        return optimizationProcesses;
    }

    /**
     * Simple getter method for dependencies.
     *
     * @return The value of dependencies.
     */
    @SuppressWarnings("null")
    public TreeSet<Dependency> getDependencies() {
        return dependencies;
    }

    /**
     * Simple getter method for identifier.
     *
     * @return The value of identifier.
     */
    @SuppressWarnings("null")
    public URI getIdentifier() {
        return identifier;
    }

    /**
     * Simple getter method for interactionType.
     *
     * @return The value of interactionType.
     */
    @SuppressWarnings("null")
    public InteractionType getInteractionType() {
        return interactionType;
    }


    /**
     * Simple getter method for itemLayer.
     *
     * @return The value of itemLayer.
     */
    @SuppressWarnings("null")
    public String getItemLayer() {
        return Strings.nullToEmpty(itemLayer);
    }

    /**
     * Simple getter method for templateMetadata.
     *
     * @return The value of templateMetadata.
     */
    @SuppressWarnings("null")
    public HashMap<String, String> getTemplateMetadata() {
        return templateMetadata;
    }

    /**
     * Simple getter method for templateMetadata.
     *
     * @param key
     *        The key to retrieve metadata for.
     * @return The value associated to the key or <code>null</code> if no value was associated to
     *         the key.
     */
    public @Nullable String getTemplateMetadata(final String key) {
        return templateMetadata.get(key);
    }

    /**
     * Method used to retrieve the key variable as given by the template metadata entry. The method
     * will return all instances of the key variable as a list.
     *
     * @return A list of variables that represent all instances of the designated key variable. Note
     *         that the list may be empty if the variables have not been initialized.
     * @throws TemplateConsistencyException
     *         Thrown when the retrieval of the key variable was unsuccessful due to either the
     *         variable not being defined properly in the template, either in the metadata or
     *         variable definition section, or the key variable not being a valid URI.
     */
    @SuppressWarnings("null")
    public Set<Variable> getKeyVariables() throws TemplateConsistencyException {
        try {
            return variables.get(new URI(templateMetadata.get(Externalization.KEY_VARIABLE)));
        } catch (final URISyntaxException e) {
            throw new TemplateConsistencyException("The key variable as given by the template metadata entry \"keyVariable\" is required to be a valid URI and mapped to a variable defined in the template as well!", e); //$NON-NLS-1$
        }
    }

    /**
     * Method used to retrieve the correct response variable as given by the template metadata
     * entry. The method will return all instances of the correct response variable as a list.
     *
     * @return A list of variables that represent all instances of the designated correct response.
     *         Note that the list may be empty if the variables have not been initialized.
     * @throws TemplateConsistencyException
     *         Thrown when the retrieval of the correct response was unsuccessful due to either the
     *         variable not being defined properly in the template, either in the metadata or
     *         variable definition section, or the correct response not being a valid URI.
     */
    @SuppressWarnings("null")
    public Set<Variable> getCorrectResponseVariables() throws TemplateConsistencyException {
        return variables.get(getCorrectResponseURI());
    }

    /**
     * Method used to retrieve the URI that is the identifier of all correct responses for this
     * template.
     *
     * @return The URI that is the identifier of all correct responses.
     * @throws TemplateConsistencyException
     *         Thrown when the identifier was not a valid URI.
     */
    public URI getCorrectResponseURI() throws TemplateConsistencyException {
        try {
            return new URI(templateMetadata.get(Externalization.CORRECT_RESPONSE_VARIABLE));
        } catch (final URISyntaxException e) {
            throw new TemplateConsistencyException("The correct response variable as given by the template metadata entry \"correctResponseVariable\" is required to be a valid URI!", e); //$NON-NLS-1$
        }
    }

    /**
     * Method used to retrieve the distractor variable as given by the template metadata entry.The
     * method will return all instances of the distractor variable as a list.
     *
     * @return A list of variables that represent all instances of the designated distractor
     *         variable. Note that the list may be empty if the variables have not been initialized.
     * @throws TemplateConsistencyException
     *         Thrown when the retrieval of the distractor variable was unsuccessful due to either
     *         the variable not being defined properly in the template, either in the metadata or
     *         variable definition section, or the key variable not being a valid URI.
     */
    @SuppressWarnings("null")
    public Set<Variable> getDistractorVariables() throws TemplateConsistencyException {
        return variables.get(getDistractorURI());
    }

    /**
     * Method used to retrieve the URI of the distractor variable for this template.
     *
     * @return The URI that is the identifier of the distractor.
     * @throws TemplateConsistencyException
     *         Thrown when the identifier was not a valid URI.
     */
    public URI getDistractorURI() throws TemplateConsistencyException {
        try {
            return new URI(templateMetadata.get(Externalization.DISTRACTOR_VARIABLE));
        } catch (final URISyntaxException e) {
            throw new TemplateConsistencyException("The distractor variable as given by the template metadata entry \"distractorVariable\" is required to be a valid URI!", e); //$NON-NLS-1$
        }
    }

    /**
     * Method used to retrieve a dependency by the URI of its head.
     *
     * @param head
     *        The URI the head of the dependency to retrieve.
     * @return The dependency for the given head or <code>null</code> if no such dependency was
     *         mapped.
     */
    public @Nullable Dependency getDependencyForHead(final URI head) {
        return dependencyByHead.get(head);
    }
}