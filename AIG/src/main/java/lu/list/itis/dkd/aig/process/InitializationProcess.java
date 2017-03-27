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
import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.SetMultimap;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public abstract class InitializationProcess extends ResolutionProcess {

    protected List<String> outcomeIdentifiers;

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
    public InitializationProcess(final ArrayListMultimap<String, String> parameters) throws TemplateParseException, ResolutionException {
        super(parameters);

        outcomeIdentifiers = parameters.get(Externalization.OUTCOME_IDENTIFIER_ELEMENT);
    }

    /**
     * Method used to trigger the initialization of all variables associated to the process. TODO
     *
     * @param variables
     *        The multimap of variables that is to be initialized, respectively, contain the
     *        initialized variables.
     * @param input
     *        the input(s) used to initialize the variables. The nature of the input depends on the
     *        concrete process.
     *
     * @throws TemplateConsistencyException
     *         Thrown when an inconsistency in the template led to issues with how the template was
     *         interpreted or how values were to be added to the template.
     * @throws ResolutionException
     *         Thrown when the resolution of one or more variables, metrics, or values as specified
     *         in the template or the derived processes failed.
     */
    public abstract void initializeVariables(Map<String, String> input, SetMultimap<URI, Variable> variables) throws TemplateConsistencyException, ResolutionException;
}