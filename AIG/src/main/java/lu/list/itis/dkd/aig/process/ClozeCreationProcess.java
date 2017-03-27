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
package lu.list.itis.dkd.aig.process;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.SetMultimap;

import lu.list.itis.dkd.aig.Value;
import lu.list.itis.dkd.aig.ValueType;
import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.VariableBuilder;
import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.assess.cloze.generation.DistractorGenerator;
import lu.list.itis.dkd.assess.cloze.ontology.ClozeOntology;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.template.ClozeItem;
import lu.list.itis.dkd.assess.cloze.template.ClozeItems;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Difficulty;
import lu.list.itis.dkd.assess.opennlp.ontology.TextOntology;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since [major].[minor]
 * @version [major].[minor].[micro]
 */
@NonNullByDefault
public class ClozeCreationProcess extends InitializationProcess {

	private int numberOfDistractors = 3;
	private boolean skipFirstSentence = true;
	private boolean useNGramArticleCorrection = true;
	private boolean useGrammaticalDependencies = false;
	private boolean useSoundex = true;
	private boolean generateFeedback = false;
	private Difficulty difficulty = Difficulty.MEDIUM;
	private Approach approach = Approach.ANNOTATION;
	private XMLOutputter outputter = new XMLOutputter();

	/**
	 * @param parameters
	 * @throws TemplateParseException
	 * @throws ResolutionException
	 */
	public ClozeCreationProcess(final ArrayListMultimap<String, String> parameters)
			throws TemplateParseException, ResolutionException {
		super(parameters);

		if (parameters.containsKey(Externalization.NUMBER_OF_DISTRACTORS)) {
			numberOfDistractors = Integer.parseInt(parameters.get(Externalization.NUMBER_OF_DISTRACTORS).get(0));
		}

		if (parameters.containsKey(Externalization.FIRST_SENTENCE)) {
			skipFirstSentence = Boolean.parseBoolean(parameters.get(Externalization.FIRST_SENTENCE).get(0));
		}
		if (parameters.containsKey(Externalization.USE_NGRAM_ARTICLE_CORRECTION)) {
			useNGramArticleCorrection = Boolean
					.parseBoolean(parameters.get(Externalization.USE_NGRAM_ARTICLE_CORRECTION).get(0));
		}
		if (parameters.containsKey(Externalization.USE_GRAMMATICAL_DEPENDENCIES)) {
			useGrammaticalDependencies = Boolean
					.parseBoolean(parameters.get(Externalization.USE_GRAMMATICAL_DEPENDENCIES).get(0));
		}
		if (parameters.containsKey(Externalization.SOUNDEX)) {
			useSoundex = Boolean.parseBoolean(parameters.get(Externalization.SOUNDEX).get(0));
		}
		if (parameters.containsKey(Externalization.FEEDBACK)) {
			generateFeedback = Boolean.parseBoolean(parameters.get(Externalization.FEEDBACK).get(0));
		}
		if (parameters.containsKey(Externalization.APPROACH)) {
			approach = Approach.valueOf(parameters.get(Externalization.APPROACH).get(0).toUpperCase());
		}
		if (parameters.containsKey(Externalization.DIFFICULTY)) {
			difficulty = Difficulty.valueOf(parameters.get(Externalization.DIFFICULTY).get(0).toUpperCase());
		}

	}

	public static void storeOntology(ClozeText clozeText) {
		String envVarValue = System
				.getenv(PropertiesFetcher.getProperties().getProperty("ontology.save.directory.var.en"));
		if (envVarValue != null) {
			TextOntology textOntology = new TextOntology(clozeText, "onDemandCloze");
			ClozeOntology clozeOntology = new ClozeOntology(clozeText, textOntology);
			String random = Long.toString(Math.round(Math.random() * 1000));
			String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'mm'm'ss's'S"));
			clozeOntology.save(envVarValue, date + '_' + random);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void initializeVariables(final Map<String, String> input, final SetMultimap<URI, Variable> variables)
			throws TemplateConsistencyException, ResolutionException {

		configureClozeGenerator();

		ClozeText clozeText = null;
		ClozeItems clozeItems = null;

		clozeText = new ClozeText(input.get("text"), "body", Language.EN, approach, numberOfDistractors, //$NON-NLS-1$
				skipFirstSentence);

		storeOntology(clozeText);

		clozeItems = new ClozeItems(clozeText, clozeText.getNumberOfDistractors(), difficulty);

		for (final ClozeItem clozeItem : clozeItems.getClozeItems()) {

			final Element clozeBlock = clozeItem.getClozeBlock();
			final List<Element> correctResponses = clozeItem.getCorrectResponseBlocks();
			final List<Element> outcomeDeclarations = clozeItem.getOutcomeDeclarationBlocks(generateFeedback);
			final Element responseProcessingBlock = clozeItem.getResponseProcessingBlock(generateFeedback);
			final List<Element> modalFeedbackBlocks = clozeItem.getModalFeedbackBlocks(generateFeedback);

			// There should only be one outcome: clozeText
			Variable outcomeVariable = null;
			try {
				outcomeVariable = VariableBuilder
						.getVariableFromBlueprint(Strings.nullToEmpty(outcomeIdentifiers.get(0)));
			} catch (final TemplateParseException e) {
				throw new ResolutionException(e);
			}

			for (final Value value : outcomeVariable.getValues(ValueType.TEXT)) {
				switch (value.getIdentifier().toString()) {
				case "http://list.lu/responseProcessingBlock":
					value.setInnerValueTo(outputter.outputString(responseProcessingBlock));
					break;
				case "http://list.lu/outcomeDeclarationBlock":
					value.setInnerValueTo(outputter.outputString(outcomeDeclarations));
					break;
				case "http://list.lu/clozeBlock":
					value.setInnerValueTo(outputter.outputString(clozeBlock));
					break;
				case "http://list.lu/correctResponseBlock":
					value.setInnerValueTo(outputter.outputString(correctResponses));
					break;
				case "http://list.lu/modalFeedbackBlock":
					value.setInnerValueTo(outputter.outputString(modalFeedbackBlocks));
					break;
				default:
					break;
				}

			}
			variables.put(outcomeVariable.getIdentifier(), outcomeVariable);
		}

	}

	private void configureClozeGenerator() {
		DistractorGenerator.setDependencyUsage(useGrammaticalDependencies);
		DistractorGenerator.useSoundex(useSoundex);
		DistractorGenerator.useKeyArticleSearch(useNGramArticleCorrection);
		DistractorGenerator.setDistractorArticleSearch(useNGramArticleCorrection);
	}
}