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

import org.eclipse.osgi.util.NLS;

/**
 * Class used to map externalized strings to constants.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@SuppressWarnings("javadoc")
public class Externalization extends NLS {
    private static final String BUNDLE_NAME = "lu.list.itis.dkd.aig.util.externalization"; //$NON-NLS-1$
    public static String AMPERSAND;
    public static String AMPERSAND_ENCODED;
    public static String APPROACH;
    public static String CARDINALITY_ELEMENT;
    public static String CLUSETER_LOOP_VARIANT_PROPERTY;
    public static String COMMA_STRING;
    public static String CONSTRUCT_TYPE_NODE;
    public static String CONSTRUCT_TYPE_XPATH;
    public static String CORRECT_RESPONSE_VARIABLE;
    public static String DATASOURCE_ELEMENT;
	public static String DATASOURCE_TYPE_ELEMENT;
    public static String DATASOURCE_KEY;
    public static String DCMI_TYPE_NODE;
    public static String DEPENDENCY;
    public static String DEPENDENCY_XPATH;
    public static String DIFFICULTY;
    public static String DISPARITY_ELEMENT;
    public static String DISTRACTOR_VARIABLE;
    public static String EMPTY_STRING;
    public static String EQUAL;
    public static String FALSE_STRING;
    public static String FEEDBACK;
    public static String FIRST_SENTENCE;
    public static String FUSEKI_HOST;
    public static String FUSEKI_DATASET;
    public static String GRAPH_ELEMENT;
    public static String GRAPH_PROPERTY;
    public static String HEAD_ELEMENT;
    public static String IDENTIFIER_ELEMENT;
    public static String INITIALIZATION_ELEMENT;
    public static String INPUT_IDENTIFIER_ELEMENT;
    public static String INTERACTION_TYPE_NODE;
    public static String INTERACTION_TYPE_XPATH;
    public static String IS_KEY_RESOLUTION_PROCESS_NODE;
    public static String KEY_ELEMENT;
    public static String KEY_LABEL_VARIABLE;
    public static String KEY_URI_VARIABLE;
    public static String KEY_VARIABLE;
    public static String KODA;
    public static String LANGUAGE_ENGLISH;
    public static String LANGUAGE_ELEMENT;

    public static String LOGGER_LEVEL_PROEPRTY;
    public static String MAPPING_XPATH;
    public static String MAXIMAL_SIZE_NODE;
    public static String MINIMAL_SIZE_NODE;
    public static String NAMESPACE_SEPARATOR;
    public static String NGRAM;
    public static String NUMBER_GROUP_REGEX;
    public static String NUMBER_OF_DISTRACTORS;
    public static String OBFUSCATION_STRING;
    public static String ONTOLOGY_PROPERTY;
    public static String OUTCOME_IDENTIFIER_ELEMENT;
    public static String PARAMETER_KEY_ONTOLOGY;
    public static String PARAMETER_NODE;
    public static String PARAMETER_VALUE_DBPEDIA_EN_DE;
    public static String PARAMETER_VALUE_DBPEDIA_EN_EN;
    public static String PARAMETER_VALUE_DBPEDIA_EN_FR;
    public static String PRIORITY_ELEMENT;
    public static String PROCESS_XPATH;
    public static String PROPERTIES_DEFAULT_NAME;
    public static String PROPERTY_KODA_URL;
    public static String QTI_XPATH;
    public static String QUERY_ELEMENT;
    public static String RANGE_NODE;
    public static String REGEX_ANY_SPACE;
    public static String REGEX_CASE_INSENSITIVE;
    public static String REGEX_LANGUAGE_TAG;
    public static String REGEX_OBFUSCATION;
    public static String REGEX_PATTERN_JSON_ANNOTATION;
    public static String REGEX_PATTERN_XML_ANNOTATION;
    public static String REGEX_SINGLE_QUOTE;
    public static String REGEX_STOPWORDS;
    public static String RELATED_VARIABLE_NODE;
    public static String SEMANTIC_SIMILARITY_PROPERTY;
    public static String SEMANTIC_SIMILARITY_WEIGHT_PROPERTY;
    public static String SERVER_ADDRESS_PROPERTY;
    public static String SERVER_PASSWORD_PROPERTY;
    public static String SERVER_PORT_PROPERTY;
    public static String SERVER_USERNAME_PROPERTY;
    public static String SIMILARTIY_ENGINE_TYPE_PROPERTY;
    public static String SOUNDEX;
    public static String SOUNDEX_SIMILARITY_PROPERTY;
    public static String SOUNDEX_SIMILARITY_WEIGHT_PROPERTY;
    public static String SPACE;
    public static String STATEMENT_NODE;
    public static String STRING_SIMILARITY_PROPERTY;
    public static String STRING_SIMILARITY_WEIGHT_PROPERTY;
    public static String SUPPORTED_MIME_TYPE_NODE;
    public static String TAG_ARTICLE_ENGLISH;
    public static String TAG_ARTICLE_FRENCH;
    public static String TAG_ARTICLE_GERMAN;
    public static String TAIL_ELEMENT;
    public static String TEMPLATE_IDENTIFIER_XPATH;
    public static String TEMPLATE_METADATA_XPATH;
    public static String TEN_INTEGER;
    public static String TEXT_NODE;
    public static String TYPE_ELEMENT;
    public static String TYPE_NODE;
    public static String USE_GRAMMATICAL_DEPENDENCIES;
    public static String USE_NGRAM_ARTICLE_CORRECTION;
    public static String UTF_8;
    public static String VALUES_ELEMENT;
    public static String VARIABLE_EXTRACTION_REGEX;
    public static String VARIABLE_EXTRACTION_XPATH;
    public static String VARIABLE_XPATH;
    public static String YES;
    public static String ZERO_INTEGER;

    static {
        NLS.initializeMessages(Externalization.BUNDLE_NAME, Externalization.class);
    }

    private Externalization() {}
}