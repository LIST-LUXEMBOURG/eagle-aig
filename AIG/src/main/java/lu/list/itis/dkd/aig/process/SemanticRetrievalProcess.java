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
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

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
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public final class SemanticRetrievalProcess extends InitializationProcess {

	private static final Logger logger = Logger.getLogger(SemanticRetrievalProcess.class.getSimpleName());
	
	private String datasource;
	private @Nullable URI graph;
	private String queryString;
	private boolean datasourceIsIdentifier;

	private final BiMap<String, String> variableKeyToEncodedKey = HashBiMap.create();
	private BiMap<String, String> encodedKeyToVariableKey;

	/**
	 * Constructor initializing all fields.
	 *
	 * @param parameters
	 *            A list of parameters keyed by the parameter name to draw all
	 *            fields values from.
	 * @throws TemplateParseException
	 *             Thrown when one or more of the required parameter values was
	 *             missing or of an erroneous value.
	 * @throws ResolutionException
	 *             Thrown when at least one of the outcome keys was not backed
	 *             by a variable declaration which caused the variable to be
	 *             unknown to the system.
	 */
	public SemanticRetrievalProcess(final ArrayListMultimap<String, String> parameters)
			throws TemplateParseException, ResolutionException {
		super(parameters);

		if (parameters.containsKey(Externalization.DATASOURCE_ELEMENT)) {
			datasource = parameters.get(Externalization.DATASOURCE_ELEMENT).get(0);
			if (Strings.isNullOrEmpty(datasource)) {
				throw new TemplateParseException(
						"The datasource provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
			}
			if (parameters.containsKey(Externalization.DATASOURCE_TYPE_ELEMENT)) {
				String type = parameters.get(Externalization.DATASOURCE_TYPE_ELEMENT).get(0);
				datasourceIsIdentifier = type.equals("identifier");
			}

		} else {
			throw new TemplateParseException(
					"The datasource provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
		}
		
		if (parameters.containsKey(Externalization.QUERY_ELEMENT)) {
			queryString = parameters.get(Externalization.QUERY_ELEMENT).get(0);
			if (Strings.isNullOrEmpty(queryString)) {
				throw new TemplateParseException(
						"The queryString provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
			}
			logger.info("query in template: " + queryString);
		} else {
			throw new TemplateParseException(
					"The queryString provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
		}

		// Optional
		if (parameters.containsKey(Externalization.GRAPH_ELEMENT)) {
			try {
				graph = new URI(parameters.get(Externalization.GRAPH_ELEMENT).get(0));
			} catch (final URISyntaxException e) {
				throw new TemplateParseException("The provided graph URI was not a valid URI!", e); //$NON-NLS-1$
			}
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
	@SuppressWarnings("null")
	@Override
	public void initializeVariables(final Map<String, String> input, final SetMultimap<URI, Variable> variables)
			throws ResolutionException, TemplateConsistencyException {
		
		if (datasourceIsIdentifier) {
			//Variable datasourceVariable;
			String datasourceFromVariables = null;
			try {
				for ( Map.Entry<URI, Variable>  entry: variables.entries()) {
					Variable variable = entry.getValue();
					Value value = variable.getValueByIdentifier(datasource);
					if(value != null){
						datasourceFromVariables = (String) value.getValue();
					}
				}
				if(datasourceFromVariables == null){
					throw new NoSuchElementException("cannot find corresponding variable for: "+datasource); //$NON-NLS-1$
				}
				//datasourceVariable = VariableBuilder.getVariableFromBlueprintWithValueIdentifier(new URI(datasource));
				//datasourceVariable = variables.get(new URI(datasource)).iterator().next();
			} catch ( NoSuchElementException e) {
				throw new ResolutionException("cannot find corresponding variable for: "+datasource, e); //$NON-NLS-1$
			}
			
			datasource = datasourceFromVariables;
			//Variable datasourceVariable = VariableBuilder.getVariableFromBlueprint(outcomeVariablesByValueKey.get(datasource));
			//datasource = (String)datasourceVariable.getValueByIdentifier(datasource).getValue();
		}
		logger.info("datasource: " + datasource);
		
		Query query = QueryFactory.create(resolveQueryInput(queryString, input, variables));
		logger.info("query:" + query.toString());
		
		final QueryExecution queryExecution;
		
		if (null == graph) {
			queryExecution = QueryExecutionFactory.sparqlService(datasource, query);
		} else { // graph cannot be null in the else-clause.
			queryExecution = QueryExecutionFactory.sparqlService(datasource, query, graph.toString());
		}
		encodedKeyToVariableKey = variableKeyToEncodedKey.inverse();

		ResultSet result = null;
		try {
			result = queryExecution.execSelect();
		} catch (final HttpException e) {
			throw new ResolutionException("The remote resource used to initialize variables is not accessible!", e); //$NON-NLS-1$
		}
		while ((result != null) && result.hasNext()) {
			final QuerySolution solution = result.nextSolution();
			System.out.println("Solution: "+solution.toString());
			// Retrieve variables in solution
			final Map<URI, Variable> solutionVariables = fetchSolutionVariables(result.getResultVars());
			System.out.println("Solutions: "+solutionVariables.toString());

			for (final String queryResultVariable : result.getResultVars()) {
				final Value value = getValueFromVariables(solutionVariables, decode(queryResultVariable));
				final String object = solution.get(queryResultVariable).toString()
						.replace(Externalization.AMPERSAND, Externalization.AMPERSAND_ENCODED)
						.replaceAll(Externalization.REGEX_LANGUAGE_TAG, new String());

				if ((null == value) || (null == object)) {
					throw new ResolutionException(
							"The query result could not be mapped to a value as there was no value for the query result \"" //$NON-NLS-1$
									+ queryResultVariable + "\" defined!"); //$NON-NLS-1$
				}

				value.setInnerValueTo(object);
			}
			solutionVariables.forEach((k, v) -> System.out.println("forEach: " + k + " - " + v.toString()));
			solutionVariables.forEach((k, v) -> variables.put(k, v));
		}
		queryExecution.close();
	}

	private @Nullable Value getValueFromVariables(final Map<URI, Variable> variables, final String identifier)
			throws TemplateConsistencyException {
		return variables.get(outcomeVariablesByValueKey.get(identifier)).getValueByIdentifier(identifier);
	}

	/**
	 * Helper method to construct all unique variable instance for provided
	 * result variables as given by the query result set.
	 *
	 * @param queryResultVariables
	 *            The variables as represented in the result set from the query.
	 * @throws ResolutionException
	 *             Thrown when an attempt is made to build a variable from an
	 *             unknown identifier.
	 */
	@SuppressWarnings("null")
	private Map<URI, Variable> fetchSolutionVariables(final List<String> queryResultVariables)
			throws ResolutionException {
		final Map<URI, Variable> variables = new HashMap<>();

		for (final String queryResultVariable : queryResultVariables) {
			System.out.println("queryResultVariable: "+queryResultVariable);
			final URI variableIdenfifier = outcomeVariablesByValueKey
					.get(decode(Strings.nullToEmpty(queryResultVariable)));
			System.out.println("variableIdenfifier: "+variableIdenfifier);
			if (!variables.containsKey(variableIdenfifier)) {
				variables.put(variableIdenfifier, VariableBuilder.getVariableFromBlueprint(variableIdenfifier));
			}
		}

		return variables;
	}

	/**
	 * Helper method to extract and replace all variables from the queryString
	 * string.
	 *
	 * @param query
	 *            The query string to find variables in and replace them with
	 *            their corresponding resolved input.
	 * @param input
	 *            The input map which must contain values for all variables
	 *            contained in the query.
	 * @pre for each variable in queryString -> input.containsKey(variable)
	 *
	 * @return The queryString string with all inputs variables replaced by the
	 *         values defined in the inputs map.
	 * @throws ResolutionException
	 *             Thrown when one or more of the variables in the query were
	 *             not covered by input values.
	 * @throws TemplateConsistencyException
	 *             Thrown when a variable identifier was found that was not a
	 *             valid URI.
	 */
	@SuppressWarnings("null")
	protected String resolveQueryInput(String query, final Map<String, String> input, SetMultimap<URI, Variable> variables)
			throws ResolutionException, TemplateConsistencyException {
		for (final URI variableIdentifier : findAllVariablesInQuery(query)) {
			String concreteValue = input.get(variableIdentifier.toString());
			logger.info("Search for: "+variableIdentifier.toString());
			if(Strings.isNullOrEmpty(concreteValue)){
				// try to find value in previously resolved variable value
				for (Variable variable : variables.values()) {
					Value value = variable.getValue(variableIdentifier);
					if(value != null){
						concreteValue = value.getValue().toString();
						logger.info("concreteValue: "+concreteValue);
						break;
					}
				}
			}
			if (Strings.isNullOrEmpty(concreteValue)) {
				throw new ResolutionException(
						"The query variable could not be ground to either an input or a previously resolved variable value: "+variableIdentifier.toString()); //$NON-NLS-1$
			}
			query = query.replace("[" + variableIdentifier + "]", "<" + concreteValue + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return encodeSparqlVariables(query);
	}

	/**
	 * TODO Ensures code is reversible.
	 *
	 * @param query
	 */
	@SuppressWarnings("null")
	private String encodeSparqlVariables(String query) {
		for (final String key : outcomeVariablesByValueKey.keySet()) {
			query = query.replace("?" + key, "?" + encode(key)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		encodedKeyToVariableKey = variableKeyToEncodedKey.inverse();
		return query;
	}

	@SuppressWarnings("null")
	private String encode(final String toEncode) {
		final String code = String.valueOf(Math.abs(toEncode.hashCode()));
		variableKeyToEncodedKey.put(toEncode, String.valueOf(Math.abs(toEncode.hashCode())));
		return code;
	}

	@SuppressWarnings("null")
	private String decode(final String toDecode) {
		return (encodedKeyToVariableKey.containsKey(toDecode) ? encodedKeyToVariableKey.get(toDecode) : toDecode);
	}
}