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

import lu.list.itis.dkd.aig.SimilarityProvider;
import lu.list.itis.dkd.aig.Value;
import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.VariableBuilder;
import lu.list.itis.dkd.aig.mcq.McqDistractor;
import lu.list.itis.dkd.aig.mcq.Options;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import org.apache.jena.ext.com.google.common.base.Strings;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Concrete factory implementation that contains the necessary functionality to resolve all
 * variables and the structure of choice-based items for the generation of MCQ items.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.5.0
 * @version 0.8.0
 */
@NonNullByDefault
public class ChoiceItemFactory extends ItemFactory {
    private final int correctResponsesPerOption;
    private final int distractorsPerOption;
    private List<Options> options;
    private Map<URI, Variable> resolvedDependencies;

    /**
     * Constructor initialising all fields.
     *
     * @param template
     *        The template XML in DOM format containing all variables and their respective
     *        constraints that should be resolved.
     * @param input
     *        A map of inputs. The keys should correspond to inputs variables as defined by the
     *        provided template. All variables need to be present.
     * @throws SimilarityComputationException
     *         Thrown when an error arises when trying to compute the similarity between variables.
     * @throws ResolutionException
     *         Thrown when not enough resources could be resolved to allow for the complete
     *         resolution of the template.
     * @throws TemplateConsistencyException
     *         Thrown when inconsistencies in the provided variables, their identifiers, or their
     *         mapping caused an exception while trying to construct an item.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @SuppressWarnings("null")
    public ChoiceItemFactory(final Template template, final Map<String, String> input, final int maximumNumberOfItems) throws TemplateConsistencyException, ResolutionException {
        super(template, input, maximumNumberOfItems);

        try {
            correctResponsesPerOption = this.template.getDependencyForHead(this.template.getCorrectResponseURI()).getCardinality();
            distractorsPerOption = this.template.getDependencyForHead(this.template.getDistractorURI()).getCardinality();
        } catch (final NullPointerException e) {
            throw new TemplateConsistencyException("The dependencies defining correct response and distractor mapping need to be given and correctly linked via the URI specified in their head element to the respective correct response and distractor variavbles!", e); //$NON-NLS-1$
        }

        buildOptions();
    }


    @SuppressWarnings("null")
    private void buildOptions() throws TemplateConsistencyException, ResolutionException {
        options = new ArrayList<>();
        final List<Variable> correctResponses = new ArrayList<Variable>(template.getCorrectResponseVariables());
        
        System.out.println("correctResponses.size: "+correctResponses.size());

        if (correctResponses.size() < correctResponsesPerOption) {
            throw new ResolutionException("There are not enough correct responses to populate items! The required size (" + correctResponsesPerOption + ") could not be met as there were only " + correctResponses.size() + " correct response(s)!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        // Variant: correctResponses.size()
        while (correctResponses.size() >= correctResponsesPerOption) {
            buildOptionsForCorrectResponses(correctResponses.subList(0, correctResponsesPerOption));
            correctResponses.subList(0, correctResponsesPerOption).clear();
        }
    }


    private void buildOptionsForCorrectResponses(final List<Variable> correctResponses) throws TemplateConsistencyException, ResolutionException {
        final List<McqDistractor> distractors = buildDistractors(template.getDistractorVariables());

        if (distractors.size() < distractorsPerOption) {
            throw new ResolutionException("There are not enough distractors to populate items! The required size (" + distractorsPerOption + ") could not be met as there were only " + distractors.size() + " distractor(s)!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        // TODO As of now only the first key id is considered. We need to define a formula that
        // coherently states how the similarity is computed in a multi-key scenario.
        final TreeSet<McqDistractor> mcqDistractors = orderDistractors(correctResponses.get(0), distractors);

        // Variant: distractors.size()
        while (mcqDistractors.size() >= distractorsPerOption) {

            final TreeSet<McqDistractor> distractorSubSet = new TreeSet<>();

            for (int i = 0; i < distractorsPerOption; i++) {
                distractorSubSet.add(mcqDistractors.pollLast());
            }

            options.add(new Options(new ArrayList<>(correctResponses), distractorSubSet));
        }
    }

    /**
     * Method used to produce a set of {@link McqDistractor} instances from a list of
     * {@link Variable} instances that represent the distractors.
     *
     * @param distractorVariables
     *        The variables that are used to produce distractors. The list should not contain
     *        <code>null</code> entries.
     * @return A Set of McqDistractor instances.
     */
    private List<McqDistractor> buildDistractors(final Set<Variable> distractorVariables) {
        final List<McqDistractor> distractors = new ArrayList<>();

        distractorVariables.stream().forEach(variable -> distractors.add(new McqDistractor(variable)));

        return distractors;
    }

    /**
     * {@inheritDoc}
     *
     * @throws TemplateConsistencyException
     *         Thrown when the values used in dependencies had values attributed to them, that is,
     *         if they were used in a context different from that of dependency variables.
     * @throws ResolutionException
     *         Thrown when an attempt is made to build a variable from an unknown identifier.
     */
    @SuppressWarnings("null")
    @Override
    public List<String> buildItems() throws TemplateConsistencyException, ResolutionException {
        final ArrayList<String> items = new ArrayList<>();
        resolvedDependencies = new HashMap<>();

        int itemCounter = 0;
        for (final Options option : options) {
            String item = new String(template.getItemLayer());

            mapDependencies(option.clone());

            // for each Option, replace all entries in the item with the occurrences in Option
            item = replaceCorrectResponseVariables(item, option);
            item = replaceDistractorVariables(item, option);
            // add all dependencies
            item = replaceDependentVariables(item);
            // add all other variables if they occur
            item = replaceOtherVariables(item);
            // add metadata
            item = addItemMetadata(item, Externalization.DISPARITY_ELEMENT, String.valueOf(option.getDisparity()));

            items.add(item);
            
			if (++itemCounter >= maximumNumberOfItems) {
				break;
			}
        }
        return items;
    }

    /**
     * @param item
     * @return
     */
    @SuppressWarnings("null")
    private String replaceDependentVariables(String item) {
        for (final Variable variable : resolvedDependencies.values()) {
            for (final Value value : variable.getValues()) {
                item = Strings.nullToEmpty(item.replace("{" + value.getIdentifier() + "}", Objects.toString(value.getValue()))); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return item;
    }

    /**
     * Method used to map the values of the variables used as dependencies, placeholders, to actual
     * values as resolved by the resolution processes.
     *
     * @param option
     *        The construct that holds the variables to draw the values from
     * @throws TemplateConsistencyException
     *         Thrown when the values used in dependencies had values attributed to them, that is,
     *         if they were used in a context different from that of dependency variables.
     * @throws ResolutionException
     *         Thrown when an attempt is made to build a variable from an unknown identifier.
     */
    @SuppressWarnings("null")
    private void mapDependencies(final Options option) throws TemplateConsistencyException, ResolutionException {
        resolvedDependencies.clear();
        for (final URI dependentURI : dependencyEquivalenceByTailElement.keySet()) {
            if (template.getVariables().containsKey(dependentURI)) {
                throw new TemplateConsistencyException("Variables defined as dependencies should be defined as placeholders and not be resolved to contain values by automated processes! At least one such dependency placeholder contained multiple resolved values!"); //$NON-NLS-1$
            }

            final Variable dependentVariable = VariableBuilder.getVariableFromBlueprint(dependentURI);
            dependentVariable.assimilateValuesOf(option.getAndRemoveVariable(dependencyEquivalenceByTailElement.get(dependentURI)));
            resolvedDependencies.put(dependentVariable.getIdentifier(), dependentVariable);
        }
    }

    /**
     * Method used to replace all non correct-response or distractor variables.
     *
     * @param item
     *        The item to replace the variables in.
     * @return The item with the variables replaced by their values.
     */
    @SuppressWarnings("null")
    private String replaceOtherVariables(String item) {
        for (final URI variableKey : template.getVariables().keySet()) {
            // If the prospective variable contains more than one value mapping, it is not to be
            // resolved in this flag manner for all items.
            if (template.getVariables().get(variableKey).size() > 1) {
                continue;
            }

            for (final Variable variable : template.getVariables().get(variableKey)) {
                for (final Value value : variable.getValues()) {
                    item = Strings.nullToEmpty(item.replace("{" + value.getIdentifier() + "}", Objects.toString(value.getValue()))); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
        return item;
    }

    /**
     * Method used to replace all distractor variables.
     *
     * @param item
     *        The item to replace the variables in.
     * @param option
     *        The construct containing the distractor variables to replace in the item.
     * @return The item with the variables replaced by their values.
     */
    @SuppressWarnings("null")
    private String replaceDistractorVariables(String item, final Options option) {
        for (final McqDistractor distractor : option.getDistractors()) {
            // TODO This is also the ideal spot to add the metrics on the distractor disparity to
            // the metadata.
            for (final Value value : distractor.getVariable().getValues()) {
                item = Strings.nullToEmpty(item.replace("{" + value.getIdentifier() + "}", Objects.toString(value.getValue()))); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return item;
    }

    /**
     * Method used to replace the correct response variable(s).
     *
     * @param item
     *        The item to replace the variables in.
     * @param option
     *        The construct containing the correct response variable(s) to replace in the item.
     * @return The item with the variables replaced by their values.
     */
    @SuppressWarnings("null")
    private String replaceCorrectResponseVariables(String item, final Options option) {
        for (final Variable variable : option.getCorrectResponses()) {
            for (final Value value : variable.getValues()) {
            	System.out.println(value.getIdentifier()+" / "+value.getValue());
                item = Strings.nullToEmpty(item.replace("{" + value.getIdentifier() + "}", Objects.toString(value.getValue()))); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return item;
    }

    /**
     * Helper method used to order distractors according to their similarity. The similarity will be
     * given by the {@link SimilarityProvider}.
     *
     * @param correctResponse
     *        The variable holding the correct response to which the distractors should be compared
     *        to.
     * @param distractors
     *        The list of {@link McqDistractor} instances to order.
     * @return A {@link TreeSet} of {@link McqDistractor} instances which is ordered by default
     *         according to the {@link McqDistractor} implementation of
     *         {@link McqDistractor#compareTo(McqDistractor)}.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    private TreeSet<McqDistractor> orderDistractors(final Variable correctResponse, final List<McqDistractor> distractors) {
        final TreeSet<McqDistractor> sortedDistractors = new TreeSet<>();
        for (final McqDistractor distractor : distractors) {
            final float similarity = SimilarityProvider.getInstance().compare(correctResponse, distractor.getVariable());
            distractor.setSimilarityToCorrectResponse(similarity);
            sortedDistractors.add(distractor);
        }
        return sortedDistractors;
    }
}