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

import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.VariableBuilder;
import lu.list.itis.dkd.aig.match.Cluster;
import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
public abstract class ResolutionProcess {

    protected URI identifier;
    protected HashMap<String, URI> outcomeVariablesByValueKey = new HashMap<>();
    protected int priority;

    private static final Logger logger = Logger.getLogger(ResolutionProcess.class.getSimpleName());
    
    /**
     * Constructor initializing all fields.
     *
     * @param parameters
     *        A list of parameters keyed by the parameter name to draw all fields values from.
     * @throws TemplateParseException
     *         Thrown when one or more of the required parameter values was missing or of an
     *         erroneous value.
     * @throws ResolutionException
     *         Thrown when at least one of the outcome keys was not backed by a variable declaration
     *         which caused the variable to be unknown to the system.
     */
    public ResolutionProcess(final ArrayListMultimap<String, String> parameters) throws TemplateParseException, ResolutionException {
        try {
            identifier = new URI(parameters.get(Externalization.IDENTIFIER_ELEMENT).get(0));
        } catch (final URISyntaxException e) {
            throw new TemplateParseException("The identifier provided for the process was not a legal URI!", e); //$NON-NLS-1$
        } catch (final IndexOutOfBoundsException e) {
            throw new TemplateParseException("The process must specify a non-empty identifier element!", e); //$NON-NLS-1$
        }

        outcomeVariablesByValueKey = flattenKeyValueMapping(parameters.get(Externalization.OUTCOME_IDENTIFIER_ELEMENT));
        
        for (HashMap.Entry<String, URI> entry  : outcomeVariablesByValueKey.entrySet()) {
        	logger.info(entry.getKey()+" / "+entry.getValue().toString());
		}

        // TODO Add priority mapping.
    }

    /**
     * Method used to extract all input variables from a list of strings and populate a map that
     * keys variables to with the identifiers of the contained value for easy reference.
     *
     * @throws TemplateParseException
     *         Thrown when the instantiation failed due to the type of variable being unknown or
     *         unsupported, respectively the provided identifier not being a valid URI.
     * @throws ResolutionException
     *         Thrown when an attempt is made to build a variable from an unknown identifier.
     */
    private HashMap<String, URI> flattenKeyValueMapping(final List<String> variableIdentifiers) throws ResolutionException, TemplateParseException {
        final HashMap<String, URI> valueUriKeyForVariable = new HashMap<>();

        for (final String identifier : variableIdentifiers) {
            @SuppressWarnings("null")
            final Variable variable = VariableBuilder.getVariableFromBlueprint(Strings.nullToEmpty(identifier));

            if (null == variable) {
                throw new TemplateParseException("The provided identifier " + identifier + " did not map to a variable!"); //$NON-NLS-1$ //$NON-NLS-2$
            }

            for (final URI valueUri : variable.getMappingByIdentifiers()) {
                valueUriKeyForVariable.put(valueUri.toString(), variable.getIdentifier());
            }
        }
        return valueUriKeyForVariable;
    }

    /**
     * Helper method for extracting all variables from a query string. The regular expression used
     * extract all variables using: \[(\w*)\]. This matches all variables written as, for example:
     * [variable_name].
     *
     * @param query
     *        The query string to extract variables from.
     * @return A {@link Set} containing all variables found.
     * @throws TemplateConsistencyException
     *         Thrown when one or more variable identifier(s) were found that were not valid URI.
     */
    protected Set<URI> findAllVariablesInQuery(final String query) throws TemplateConsistencyException {
        final Pattern pattern = Pattern.compile(Externalization.VARIABLE_EXTRACTION_REGEX, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(query);
        final Set<URI> uniqueVariables = new HashSet<>();

        while (matcher.find()) {
            try {
                uniqueVariables.add(new URI(matcher.group(1)));
            } catch (final URISyntaxException e) {
                throw new TemplateConsistencyException("One or more of the extracted variables were not valid URI!", e); //$NON-NLS-1$
            }
        }
        return uniqueVariables;
    }

    /**
     * Simple getter method used to retrieve the identifier of this process.
     *
     * @return The identifier of this process.
     */
    public URI getIdentifier() {
        return identifier;
    }
}