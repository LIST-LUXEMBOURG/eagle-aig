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

import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;

import com.google.common.collect.ArrayListMultimap;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
public abstract class OptimizationProcess extends ResolutionProcess {

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
    public OptimizationProcess(final ArrayListMultimap<String, String> parameters) throws TemplateParseException, ResolutionException {
        super(parameters);
    }

    /**
     * TODO
     */
    public abstract void optimizeVariables();
}