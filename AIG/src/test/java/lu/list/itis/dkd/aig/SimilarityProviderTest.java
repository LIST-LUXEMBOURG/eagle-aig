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
package lu.list.itis.dkd.aig;

import lu.list.itis.dkd.aig.util.PropertiesFetcher;
//import lu.list.itis.dkd.semantic.exception.InitializationException;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Test class for {@link SimilarityProvider}.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.4
 * @version 0.7.1
 */
@SuppressWarnings("nls")
public class SimilarityProviderTest {

    private final Properties properties = PropertiesFetcher.getProperties();

    /**
     * Test method for {@link lu.list.itis.dkd.aig.SimilarityProvider#getInstance()}.
     *
     * @throws SecurityException
     *         Thrown when permission is not given by the Security Manager to reflectively access
     *         the resource(s).
     * @throws NoSuchFieldException
     *         Thrown when no field with a given name exists (or is visible).
     * @throws IllegalAccessException
     *         Thrown when access to a resource is not permitted, likely due to visibility.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testGetInstance() throws IllegalAccessException, NoSuchFieldException, SecurityException {
        final SimilarityProvider provider = SimilarityProvider.getInstance();

        final Field useSemanticSimilarity = provider.getClass().getDeclaredField("useSemanticSimilarity");
        final Field useStringSimilarity = provider.getClass().getDeclaredField("useStringSimilarity");
        final Field useSoundexSimilarity = provider.getClass().getDeclaredField("useSoundexSimilarity");
        final Field semanticSimilarityWeight = provider.getClass().getDeclaredField("semanticSimilarityWeight");
        final Field stringSimilarityWeight = provider.getClass().getDeclaredField("stringSimilarityWeight");
        final Field soundexSimilarityWeight = provider.getClass().getDeclaredField("soundexSimilarityWeight");

        useSemanticSimilarity.setAccessible(true);
        useStringSimilarity.setAccessible(true);
        useSoundexSimilarity.setAccessible(true);
        semanticSimilarityWeight.setAccessible(true);
        stringSimilarityWeight.setAccessible(true);
        soundexSimilarityWeight.setAccessible(true);

        // TODO we are not finished here.
    }
}