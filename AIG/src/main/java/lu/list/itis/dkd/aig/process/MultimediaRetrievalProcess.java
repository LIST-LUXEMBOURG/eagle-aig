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

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.SetMultimap;

import java.net.URI;
import java.util.Map;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public final class MultimediaRetrievalProcess extends InitializationProcess {

    private final String datasource;
    private final String query;

    /**
     * Constructor initializing all fields.
     *
     * @param parameters
     *        A list of parameters keyed by the parameter name to draw all fields values from.
     * @throws TemplateParseException
     *         Thrown when one or more of the required parameter values was missing or of an
     *         erroneous value.
     * @throws ResolutionException
     */
    public MultimediaRetrievalProcess(final ArrayListMultimap<String, String> parameters) throws TemplateParseException, ResolutionException {
        super(parameters);

        if (parameters.containsKey(Externalization.DATASOURCE_ELEMENT)) {
            datasource = parameters.get(Externalization.DATASOURCE_ELEMENT).get(0);
            if (Strings.isNullOrEmpty(datasource)) {
                throw new TemplateParseException("The datasource provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
            }
        } else {
            throw new TemplateParseException("The datasource provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
        }

        if (parameters.containsKey(Externalization.QUERY_ELEMENT)) {
            query = parameters.get(Externalization.QUERY_ELEMENT).get(0);
            if (Strings.isNullOrEmpty(query)) {
                throw new TemplateParseException("The query provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
            }
        } else {
            throw new TemplateParseException("The query provided for the process must be specified and non-emtpy!"); //$NON-NLS-1$
        }
    }


    /** {@inheritDoc} */
    @Override
    public void initializeVariables(final Map<String, String> inputs, final SetMultimap<URI, Variable> map) throws ResolutionException {
        // TODO Auto-generated method stub

    }
}