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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.SetMultimap;

import lu.list.itis.dkd.aig.Value;
import lu.list.itis.dkd.aig.ValueType;
import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.VariableBuilder;
import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.FusekiHttpHelper;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.assess.cloze.generation.DistractorGenerator;
import lu.list.itis.dkd.assess.cloze.ontology.ClozeOntology;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Difficulty;
import lu.list.itis.dkd.assess.opennlp.ontology.TextOntology;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

@NonNullByDefault
public final class OnDemandTextOntologyCreationProcess extends InitializationProcess {

	private final BiMap<String, String> variableKeyToEncodedKey = HashBiMap.create();
	private BiMap<String, String> encodedKeyToVariableKey;
	private Variable datasourceVariable;
	private Value datasourceValue;
	private String datasourceKey;

	private int numberOfDistractors = 3;
	private boolean skipFirstSentence = true;
	private boolean useNGramArticleCorrection = true;
	private boolean useGrammaticalDependencies = false;
	private boolean useSoundex = true;
	private boolean generateFeedback = false;
	private Difficulty difficulty = Difficulty.MEDIUM;
	private Approach approach = Approach.DEFINITION;

	/**
	 * Constructor initializing all fields.
	 *
	 * @param parameters
	 *            A list of parameters keyed by the parameter name to draw all
	 *            fields values from.
	 * @throws TemplateParseException
	 *             Thrown when one or more of the required parameter values was
	 *             missing or of an erroneous value.
	 */
	public OnDemandTextOntologyCreationProcess(final ArrayListMultimap<String, String> parameters)
			throws TemplateParseException, ResolutionException {
		super(parameters);

		checkArgument(parameters.containsKey(Externalization.DATASOURCE_KEY),
				"The datasource key provided for the process must be specified and non-emtpy!");

		datasourceKey = Strings.emptyToNull(parameters.get(Externalization.DATASOURCE_KEY).get(0));
		checkNotNull(datasourceKey, "The datasource key provided for the process must be specified and non-emtpy!");

		URI datasource = outcomeVariablesByValueKey.get(datasourceKey);
		datasourceVariable = VariableBuilder.getVariableFromBlueprint(datasource);

		try {
			datasourceValue = datasourceVariable.getValueByIdentifier(datasourceKey);
		} catch (TemplateConsistencyException e) {
			throw new TemplateParseException(
					"Unable to get value: " + datasourceKey + " in: " + datasourceValue.getIdentifier(), e);
		}

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

	/**
	 * {@inheritDoc}<br>
	 *
	 * The method will execute a query to Jena, encoding key variables as
	 * necessary. The latter will be stored in a local {@link BiMap} for easy
	 * referencing. The query solution is then
	 *
	 * @throws ResolutionException
	 *
	 * @throws TemplateConsistencyException
	 *             Thrown when one or more variables, respectively their values,
	 *             were found in the query that could not be identified, i.e.
	 *             their identifier was not a valid URI or not mapped due to
	 *             variable definitions containing errors.
	 */
	@Override
	public void initializeVariables(final Map<String, String> input, final SetMultimap<URI, Variable> variables)
			throws ResolutionException, TemplateConsistencyException {

		configureClozeGenerator();

		ClozeText clozeText = new ClozeText(input.get("text"), "body", Language.EN, approach, numberOfDistractors, //$NON-NLS-1$
				skipFirstSentence);

		TextOntology textOntology = new TextOntology(clozeText, "onDemandCloze");
		ClozeOntology clozeOntology = new ClozeOntology(clozeText, textOntology);
		//clozeOntology.save("C:\\Temp", "OWL");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		clozeOntology.save(outputStream);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String dataSetNamePrefix = PropertiesFetcher.getProperties().getProperty(Externalization.FUSEKI_DATASET) + '_';
		String dataSetName = FusekiHttpHelper.createRandomDataSetName(dataSetNamePrefix);
		try {
			FusekiHttpHelper.createDataSet(dataSetName);
			FusekiHttpHelper.uploadOntology(inputStream, dataSetName, null);
		} catch (IOException | org.apache.http.HttpException e) {
			throw new ResolutionException("Failed to create dataset", e);
		}

		// set datasetname
		URL endpoint;
		try {
			endpoint = FusekiHttpHelper.getSparqlEndPoint(dataSetName);
		} catch (MalformedURLException e) {
			throw new ResolutionException("Failed to get endpoint address", e);
		}

		datasourceValue.setInnerValueTo(endpoint.toString());
		variables.put(datasourceVariable.getIdentifier(), datasourceVariable);

	}

	private void configureClozeGenerator() {
		DistractorGenerator.setDependencyUsage(useGrammaticalDependencies);
		DistractorGenerator.useSoundex(useSoundex);
		DistractorGenerator.useKeyArticleSearch(useNGramArticleCorrection);
		DistractorGenerator.setDistractorArticleSearch(useNGramArticleCorrection);
	}
}