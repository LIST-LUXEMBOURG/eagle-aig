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
package lu.list.itis.dkd.assess.cloze.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.net.MediaType;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class KodaAnnotation {
    private static Properties properties = ClozePropertiesFetcher.fetchProperties("cloze.properties");
    protected static final Logger logger = Logger.getLogger(KodaAnnotation.class.getSimpleName());
    
    private static List<String> retrieveAnnotations(String source, Language language){
//        String jsonPattern = "\"([a-zA-Z\\s]*)\""; alternative
        String contentPattern = "<term>(.*)</term>";
        
        List<String> annotations = new ArrayList<>();
        Pattern annotationPattern = Pattern.compile(contentPattern);
        Matcher annotationMatcher = annotationPattern.matcher(source);

        while (annotationMatcher.find()) {
            String annotationMatch = annotationMatcher.group(1);

            //Remove article
            String temp = "";
            String[] annotationWords = annotationMatch.split(" ");
            for (String annotationWord : annotationWords) {                
                if (!ArticleHelper.isArticle(annotationWord, language)) {
                    temp += annotationWord + " ";
                }
            }
            
            temp = temp.trim();
            annotations.add(temp);
        }
        
        return annotations;
    }
    
    
    /**
     * Returns all annotations KODA annotiates for the sentence.
     * @param sentence
     * @return
     * @throws IOException
     */
    public static List<String> getAnnotations(Sentence sentence) {    
        return getAnnotations(sentence.getContent(), sentence.getLanguage());
    }
    
    /**
     * Returns all annotations KODA annotiates for the sentence.
     * @param sentence
     * @return
     * @throws IOException
     */
    public static List<String> getAnnotations(String sentence, Language language) {    
      //Choose ontology
        String ontology = "&ontology=";
        switch (language) {
            case DE:
                ontology += "DBPEDIA_EN_DE";
                break;
            case FR:
                ontology += "DBPEDIA_EN_FR";
                break;
            default:
                ontology += "DBPEDIA_EN_EN";
                break;
        }
        
        //Connect to Koda
        String encodedSentence = sentence;
        try {
            encodedSentence = URLEncoder.encode(sentence, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING, "UTF-8 encoding failed!");
            e.printStackTrace();
        }
        String url = properties.getProperty("koda.url") + "text=" + encodedSentence + ontology;
        String source = UrlHelper.getSource(url, MediaType.APPLICATION_XML_UTF_8);
//        String source = UrlHelper.getSource(url, MediaType.JSON_UTF_8); alternative

        return retrieveAnnotations(source, language);
    }
}
