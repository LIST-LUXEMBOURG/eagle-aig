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

import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Concrete resolver implementation that contains the necessary functionality to resolve all
 * variables and the structure of open question items.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.6
 * @version 0.8.0
 */
@NonNullByDefault
public class OpenItemFactory extends ItemFactory {
    /**
     * Constructor initialising all fields.
     *
     * @param template
     *        The template XML in DOM format containing all variables and their respective
     *        constraints that should be resolved.
     * @param input
     *        A map of inputs. The keys should correspond to inputs variables as defined by the
     *        provided template. All variables need to be present.
     * @throws ResolutionException
     * @throws SimilarityComputationException
     * @throws InitializationException
     * @throws TemplateConsistencyException
     */
    public OpenItemFactory(final Template template, final Map<String, String> input, final int maximumNumberOfItems) throws ResolutionException, TemplateConsistencyException {
        super(template, input, maximumNumberOfItems);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> buildItems() throws TemplateParseException, TemplateConsistencyException, ResolutionException {
        // TODO Auto-generated method stub
        return new ArrayList<String>();
    }
}