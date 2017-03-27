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

import lu.list.itis.dkd.aig.process.InitializationProcess;
import lu.list.itis.dkd.aig.process.OptimizationProcess;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import org.jdom2.Document;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class called upon to resolve all variables in a template. The class will try to ground all
 * variables in the template. It will also check logical constraints specified in the XML so that
 * all variables are sound and follow from the provided template.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.8.0
 */
@NonNullByDefault
public abstract class ItemFactory {
    protected final Map<String, String> input;
    protected Template template;
    protected Map<URI, URI> dependencyEquivalenceByTailElement;
    protected int maximumNumberOfItems;

    protected static final Logger logger = Logger.getLogger(ItemFactory.class.getSimpleName());

    static {
        ItemFactory.logger.setLevel(Level.parse(PropertiesFetcher.getProperties().getProperty(Externalization.LOGGER_LEVEL_PROEPRTY, Level.INFO.toString())));
    }

    /**
     * Constructor.
     *
     * @param template
     *        The template XML that will be parsed and stored locally.
     * @param input
     *        A map of inputs. The keys should correspond to inputs variables as defined by the
     *        provided template. All variables need to be present.
     * @throws ResolutionException
     *         Thrown when at least one of the outcome keys was not backed by a variable declaration
     *         which caused the variable to be unknown to the system.
     * @throws TemplateConsistencyException
     *         Thrown when inconsistencies in the provided variables, their identifiers, or their
     *         mapping caused an exception while trying to construct an item.
     * @pre Template has been validated against templateDocument schema. TODO
     */
    @SuppressWarnings("null")
    public ItemFactory(final Template template, final Map<String, String> input, final int maximumNumberOfItems) throws TemplateConsistencyException, ResolutionException {
        this.template = template;
        this.input = input;
        this.maximumNumberOfItems = maximumNumberOfItems;
        
        for (final InitializationProcess process : template.getInitializationProcesses().values()) {
        	logger.info("Init process "+process.getIdentifier());
            process.initializeVariables(this.input, template.getVariables());
        }

        for (final OptimizationProcess process : template.getOptimizationProcesses().values()) {
            process.optimizeVariables();
        }

        resolveDependencies();
    }

    /**
     * Method used to browse all dependencies and
     *
     * @throws TemplateConsistencyException
     *         Thrown when a tail element was to be retrieved while the tail contained no more
     *         elements. This error indicates an issue with the cardinality or dependency mapping in
     *         the template.
     */
    private void resolveDependencies() throws TemplateConsistencyException {
        dependencyEquivalenceByTailElement = new HashMap<>();

        for (final Dependency dependency : template.getDependencies()) {
            for (int i = 0; i < dependency.getCardinality(); i++) {
                mapCurrentlyUnmappedTailElement(dependency);
            }
        }
    }

    /**
     * Method used to browse the dependency tail and find a suitable, currently unmapped, entry to
     * map to the head.
     *
     * @param dependency
     *        The dependency for which to map parts of its tail to its head.
     * @throws TemplateConsistencyException
     *         Thrown when a tail element was to be retrieved while the tail contained no more
     *         elements. This error indicates an issue with the cardinality or dependency mapping in
     *         the template.
     *
     */
    private void mapCurrentlyUnmappedTailElement(final Dependency dependency) throws TemplateConsistencyException {
        URI tailElement = null;
        do {
            tailElement = dependency.getUnmappedTailElement();
        } while (dependencyEquivalenceByTailElement.containsKey(tailElement));

        dependencyEquivalenceByTailElement.put(tailElement, dependency.getHead());
    }


    /**
     * Method for building QTI items. The method addresses the item factory to populate the QTI item
     * template.
     *
     * @pre template != null
     * @pre itemFactory != null
     * @return A {@link List} of {@link Document} instances holding possible QTI items in XML
     *         format.
     * @throws TemplateParseException
     *         Thrown when building the individual from the template item did not succeed. This is
     *         most likely due to the item structure no longer being valid as the resolution process
     *         might have introduced values that violate the XML specification.
     * @throws TemplateConsistencyException
     *         Thrown when an inconsistency in the template resulted in an error of interpreting
     *         values from or assigning values to the template.
     * @throws ResolutionException
     *         Thrown when the resolution of one or more variables or otherwise resolvable items
     *         failed.
     * @post for all variables in item template where
     *       itemFactory.getVariables().get(variable).getValue() != null -> variable in item is
     *       replaced by value
     * @post for all variables in item template where
     *       itemFactory.getVariables().get(variable).getValue() == null -> variable in item remains
     *       unchanged
     */
    public abstract List<String> buildItems() throws TemplateParseException, TemplateConsistencyException, ResolutionException;

    /**
     * Method used to add a metadata element to the item layer contained by this template.
     *
     * @param item
     *        The item to which to add the metadata to.
     * @param name
     *        The name of the element to add.
     * @param value
     *        The value of the element to add.
     * @return The item with the metadata added.
     */
    @SuppressWarnings("null")
    protected String addItemMetadata(final String item, final String name, final String value) {
        final String replacement = "<" + name + ">" + value + "</" + name + ">" + "</itemMetadata>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        return item.replace("</itemMetadata>", replacement); //$NON-NLS-1$
    }

}