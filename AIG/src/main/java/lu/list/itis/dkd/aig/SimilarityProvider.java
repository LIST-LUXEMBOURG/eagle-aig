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

import lu.list.itis.dkd.aig.mcq.McqDistractor;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.aig.util.Soundex;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.lucene.search.spell.JaroWinklerDistance;

import java.net.URI;
import java.util.Properties;

/**
 * Class used to compare two variables. This comparator will read the properties file and deduce
 * which comparisons to make to establish a final order.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.4
 * @version 0.5.32
 */
@NonNullByDefault
public final class SimilarityProvider {
    private static SimilarityProvider INSTANCE;
    private boolean useSemanticSimilarity = false;
    private boolean useStringSimilarity = false;
    private boolean useSoundexSimilarity = false;
    private float semanticSimilarityWeight = 0f;
    private float stringSimilarityWeight = 0f;
    private float soundexSimilarityWeight = 0f;
//    private SemanticSimilarityBridge semanticSimilarityBridge;

    /**
     * Constructor initialising the comparator from properties read from a properties file.
     *
     * @throws ExceptionInInitializerError
     */
    private SimilarityProvider() throws ExceptionInInitializerError {
        final Properties properties = PropertiesFetcher.getProperties();

        if (properties.isEmpty()) {
            throw new ExceptionInInitializerError("The properties file could not be located!"); //$NON-NLS-1$
        }

        useSemanticSimilarity = Boolean.parseBoolean(properties.getProperty(Externalization.SEMANTIC_SIMILARITY_PROPERTY, Externalization.FALSE_STRING));
        useStringSimilarity = Boolean.parseBoolean(properties.getProperty(Externalization.STRING_SIMILARITY_PROPERTY, Externalization.FALSE_STRING));
        useSoundexSimilarity = Boolean.parseBoolean(properties.getProperty(Externalization.SOUNDEX_SIMILARITY_PROPERTY, Externalization.FALSE_STRING));

        semanticSimilarityWeight = Float.parseFloat(properties.getProperty(Externalization.SEMANTIC_SIMILARITY_WEIGHT_PROPERTY, Externalization.ZERO_INTEGER));
        stringSimilarityWeight = Float.parseFloat(properties.getProperty(Externalization.STRING_SIMILARITY_WEIGHT_PROPERTY, Externalization.ZERO_INTEGER));
        soundexSimilarityWeight = Float.parseFloat(properties.getProperty(Externalization.SOUNDEX_SIMILARITY_WEIGHT_PROPERTY, Externalization.ZERO_INTEGER));

        if (useSemanticSimilarity) {
        	throw new NotImplementedException("semantic similarity not yet available");
            //instantiateSemanticSimilarityBridge();
        }
    }

    /**
     * Method for returning the single instance of this class.
     *
     * @return The sole instance of this class.
     * @throws ExceptionInInitializerError
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @SuppressWarnings("null")
    public static synchronized SimilarityProvider getInstance() {
        if (null == SimilarityProvider.INSTANCE) {
            SimilarityProvider.INSTANCE = new SimilarityProvider();
        }
        return SimilarityProvider.INSTANCE;
    }

    /**
     * Method for initializing the semantic similarity bridge.
     *
     * @throws ExceptionInInitializerError
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    private void instantiateSemanticSimilarityBridge() throws ExceptionInInitializerError {
        final Properties properties = PropertiesFetcher.getProperties();

        if (properties.isEmpty()) {
            throw new ExceptionInInitializerError("The properties file could not be located!"); //$NON-NLS-1$
        }

//        semanticSimilarityBridge = new SemanticSimilarityBridge(
//                        properties.getProperty(Externalization.ONTOLOGY_PROPERTY),
//                        properties.getProperty(Externalization.GRAPH_PROPERTY),
//                        properties.getProperty(Externalization.SERVER_ADDRESS_PROPERTY),
//                        properties.getProperty(Externalization.SERVER_PASSWORD_PROPERTY),
//                        properties.getProperty(Externalization.SERVER_PORT_PROPERTY),
//                        properties.getProperty(Externalization.SERVER_USERNAME_PROPERTY),
//                        properties.getProperty(Externalization.SIMILARTIY_ENGINE_TYPE_PROPERTY));
    }


    /**
     * Method used to compare two {@link Variable} instances. The comparison is based on similarity
     * given a few metrics:<br>
     * <ul>
     * <li>The text content, together with the language, allows to use {@link Soundex}.</li>
     * <li>The text content also allows to compute other string similarities.</li>
     * <li>The semantic content is used to compute the semantic similarity.</li>
     * </ul>
     *
     * @param _this
     *        The first variable to compare.
     * @param forceLanguage
     *        If set, the language of the first parameter in the array will be used to force the
     *        language of the Soundex computation. Supported choices are "en", "de", and "fr". All
     *        other inputs default to "en". TODO review
     * @param that
     *        The second variable to compare.
     * @return The similarity given the two variables and the number of similarity algorithms in
     *         use. The result will return a value between 0f and 1f.
     * @throws SimilarityComputationException
     *         Thrown when an error occurs during the computation of the semantic similarity.
     */
    public float compare(final Variable _this, final Variable that, final String... forceLanguage) {
        final float soundexSimilarity = computeSoundexSimilarity(_this, that, forceLanguage);
       // final InstanceSimilarity semanticSimilarity = computeSemanticSimilarity(_this, that);
        final float stringSimilarity = computeStringSimilarity(_this, that);

        //return ((soundexSimilarity * soundexSimilarityWeight) + (semanticSimilarity.getValue() * semanticSimilarityWeight) + (stringSimilarity * stringSimilarityWeight)) / (countActiveAlgorithms() == 0 ? 1 : countActiveAlgorithms());
        return ((soundexSimilarity * soundexSimilarityWeight) + (stringSimilarity * stringSimilarityWeight)) / (countActiveAlgorithms() == 0 ? 1 : countActiveAlgorithms());
    }

    /**
     * Method used to compute a string similarity using Jaro Winkler as implemented by Apache's
     * Lucene.
     *
     * @param _this
     *        The first variable to compute similarity with.
     * @param that
     *        The second variable to compute similarity with.
     * @return The string similarity of both variables.
     * @see JaroWinklerDistance#getDistance(String, String)
     */
    private float computeStringSimilarity(final Variable _this, final Variable that) {
        if (!useStringSimilarity) {
            return 0f;
        }

        return new JaroWinklerDistance().getDistance(_this.getTextContent(), that.getTextContent());
    }

    /**
     * Method used to compute the similarity between the semantic cotnent of two variables. The
     * method will use the {@link SemanticSimilarityBridge} to determine similarity. Note that the
     * result will always be <code>0</code> if no Uri has been set for one of or both of the
     * provided variables.
     *
     * @param _this
     *        The first variable to compute similarity with.
     * @param that
     *        The second variable to compute similarity with.
     * @return The semantic instance similarity of the concepts represented by the variable's
     *         {@link URI}.
     * @throws SimilarityComputationException
     *         Thrown when an error occurs during the computation of the semantic similarity.
     */
//    @SuppressWarnings("null")
//    private InstanceSimilarity computeSemanticSimilarity(final Variable _this, final Variable that) throws SimilarityComputationException {
//        if (!useSemanticSimilarity || (_this.getSemanticContent() == null) || (that.getSemanticContent() == null)) {
//            return new InstanceSimilarity(0);
//        }
//
//        return semanticSimilarityBridge.getSemanticSimilarity(_this.getSemanticContent().toString(), that.getSemanticContent().toString());
//    }

    /**
     * Helper method used to compute the {@link Soundex} similarity of the parameterised variables.
     * The {@link Soundex} will be computed in the language of the first parameter, hence, it is
     * wise to have the first {@link Variable} be the key while the second is the
     * {@link McqDistractor}.
     *
     * @param _this
     *        The first {@link Variable} to be compared. This variable will also determine what
     *        language the soundex is computed in.
     * @param that
     *        The second {@link Variable} to be compared.
     * @param forceLanguage
     *        If set, the language of the first parameter in the array will be used to force the
     *        language of the Soundex computation. Supported choices are "en", "de", and "fr". All
     *        other inputs default to "en".
     * @return The soundex-based similarity. Should the properties file indicate that soundex-based
     *         similarity is not to be used, the method will return 0f. The return value will
     *         normalise the soundex difference and return the inverse in the interval [0, 1] to
     *         provide the difference measure.
     */
    private float computeSoundexSimilarity(final Variable _this, final Variable that, final String... forceLanguage) {
        if (!useSoundexSimilarity) {
            return 0f;
        }
        return 1 - (Soundex.difference(_this, that, forceLanguage) / 4f);
    }

    /**
     * Helper method for counting the number of active similarity algorithms.
     *
     * @return A sum of all similarity algorithms that had been enabled in the properties file.
     */
    private int countActiveAlgorithms() {
        int sum = 0;
        if (useSemanticSimilarity) {
            sum++;
        }
        if (useStringSimilarity) {
            sum++;
        }
        if (useSoundexSimilarity) {
            sum++;
        }
        return sum;
    }
}