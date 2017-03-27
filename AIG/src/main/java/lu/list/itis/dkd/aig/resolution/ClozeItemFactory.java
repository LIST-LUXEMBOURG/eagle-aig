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

import lu.list.itis.dkd.aig.Value;
import lu.list.itis.dkd.aig.Variable;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import org.apache.jena.ext.com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.8.0
 * @version 0.8.0
 */

public class ClozeItemFactory extends ItemFactory {
    private final List<String> items = new ArrayList<>();


    /**
     * @param template
     * @param input
     * @throws ResolutionException
     * @throws InitializationException
     * @throws SimilarityComputationException
     * @throws TemplateConsistencyException
     */
    public ClozeItemFactory(final Template template, final Map<String, String> input, final int maximumNumberOfItems) throws ResolutionException, TemplateConsistencyException {
        super(template, input, maximumNumberOfItems);
    }

    @Override
    public List<String> buildItems() throws TemplateConsistencyException, ResolutionException {
        final ArrayList<String> items = new ArrayList<>();
        logger.info("BUILD ITEMS: "+template.getVariables().values().size());
        // Only one variable: clozeText
        for (final Variable variable : template.getVariables().values()) {
            String item = new String(template.getItemLayer());
            for (final Value value : variable.getValues()) {
                item = Strings.nullToEmpty(item.replace("{" + value.getIdentifier() + "}", Objects.toString(value.getValue()))); //$NON-NLS-1$ //$NON-NLS-2$
            }
            items.add(item);
        }

        return items;
    }
}