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
package lu.list.itis.dkd.aig.util;

import lu.list.itis.dkd.aig.SimilarityProvider;
import lu.list.itis.dkd.assess.cloze.util.TestResources;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Helper class regrouping methods used by unit tests to adapt configurations such as those read
 * from properties files.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.6
 * @version 0.8.0
 */
public class ConfigurationChanger {

    /**
     * Private constructor as class should only contain static methods.
     */
    private ConfigurationChanger() {}

    /**
     * Helper method for configuring the provider to behave as expected.
     *
     * @param provider
     *        The {@link SimilarityProvider} to configure.
     * @throws SecurityException
     *         Thrown when permission is not given by the Security Manager to reflectively access
     *         the resource(s).
     * @throws NoSuchFieldException
     *         Thrown when no field with a given name exists (or is visible).
     * @throws IllegalAccessException
     *         Thrown when access to a resource is not permitted, likely due to visibility.
     */
    public static void configureProvider(final SimilarityProvider provider) throws NoSuchFieldException, SecurityException, IllegalAccessException {

        final Field useSemanticSimilarity = provider.getClass().getDeclaredField("useSemanticSimilarity"); //$NON-NLS-1$
        final Field useStringSimilarity = provider.getClass().getDeclaredField("useStringSimilarity"); //$NON-NLS-1$
        final Field useSoundexSimilarity = provider.getClass().getDeclaredField("useSoundexSimilarity"); //$NON-NLS-1$
        final Field semanticSimilarityWeight = provider.getClass().getDeclaredField("semanticSimilarityWeight"); //$NON-NLS-1$
        final Field stringSimilarityWeight = provider.getClass().getDeclaredField("stringSimilarityWeight"); //$NON-NLS-1$
        final Field soundexSimilarityWeight = provider.getClass().getDeclaredField("soundexSimilarityWeight"); //$NON-NLS-1$

        final Properties properties = PropertiesFetcher.getProperties(TestResources.class, "test.properties"); //$NON-NLS-1$

        useSemanticSimilarity.setAccessible(true);
        useSemanticSimilarity.set(provider, Boolean.parseBoolean(properties.getProperty("similarity.semantic", "false"))); //$NON-NLS-1$ //$NON-NLS-2$
        useStringSimilarity.setAccessible(true);
        useStringSimilarity.set(provider, Boolean.parseBoolean(properties.getProperty("similarity.string", "false"))); //$NON-NLS-1$ //$NON-NLS-2$
        useSoundexSimilarity.setAccessible(true);
        useSoundexSimilarity.set(provider, Boolean.parseBoolean(properties.getProperty("similarity.soundex", "false"))); //$NON-NLS-1$ //$NON-NLS-2$

        semanticSimilarityWeight.setAccessible(true);
        semanticSimilarityWeight.set(provider, Integer.parseInt(properties.getProperty("similarity.semantic.weight", "1"))); //$NON-NLS-1$ //$NON-NLS-2$
        stringSimilarityWeight.setAccessible(true);
        stringSimilarityWeight.set(provider, Integer.parseInt(properties.getProperty("similarity.string.weight", "1"))); //$NON-NLS-1$ //$NON-NLS-2$
        soundexSimilarityWeight.setAccessible(true);
        soundexSimilarityWeight.set(provider, Integer.parseInt(properties.getProperty("similarity.soundex.weight", "1"))); //$NON-NLS-1$ //$NON-NLS-2$
    }
}