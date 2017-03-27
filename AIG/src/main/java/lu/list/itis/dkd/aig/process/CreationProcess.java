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
import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.SetMultimap;

import org.apache.jena.ext.com.google.common.base.Splitter;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public final class CreationProcess extends InitializationProcess {

    private final Map<String, String> creationParameters = new HashMap<>();

    /**
     * Constructor initializing all fields.
     *
     * @param parameters
     *        A list of creationParameters keyed by the parameter name to draw all fields values
     *        from.
     * @throws TemplateParseException
     *         Thrown when one or more of the required parameter values was missing or of an
     *         erroneous value.
     * @throws ResolutionException
     */
    public CreationProcess(final ArrayListMultimap<String, String> parameters) throws TemplateParseException, ResolutionException {
        super(parameters);

        extractGenerationParameters(parameters);
    }

    /**
     * Method used to populate the map of creation parameters by splitting the equal-sign separated
     * key-value parameters held by the previously extracted list of parameters using the
     * <tt>parameter</tt> key.
     *
     * @throws TemplateParseException
     *         Thrown when the parameters were not correctly split or one of them had an emtpy key
     *         or value.
     */
    private void extractGenerationParameters(final ArrayListMultimap<String, String> parameters) throws TemplateParseException {
        try {
            for (final String parameter : parameters.get(Externalization.PARAMETER_NODE)) {
                final List<String> keyValueList = Splitter.on(Externalization.EQUAL).omitEmptyStrings().trimResults().splitToList(parameter);
                creationParameters.put(keyValueList.get(0), keyValueList.get(1));
            }
        } catch (final IndexOutOfBoundsException e) {
            throw new TemplateParseException("The parameters specified for the creation process must be complete, i.e. they must consist of a non-empty key and value separated by an equal sign!", e); //$NON-NLS-1$
        }
    }

    /** {@inheritDoc} */
    @Override
    public void initializeVariables(final Map<String, String> inputs, final SetMultimap<URI, Variable> map) throws ResolutionException {
        // TODO Auto-generated method stub

    }
}