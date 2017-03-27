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
package lu.list.itis.dkd.assess.cloze.generation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ClozePropertiesFetcher;
import lu.list.itis.dkd.assess.cloze.util.UrlHelper;
import lu.list.itis.dkd.assess.opennlp.Word;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class VerbGenerator {
    protected static final Logger logger = Logger.getLogger(VerbGenerator.class.getSimpleName());
    private static Properties properties = ClozePropertiesFetcher.fetchProperties("cloze.properties");

    public static List<Distractor> getTenses(Key key) {
        String language = "en";
        switch (key.getKeyWord().getLanguage()) {
            case DE:
                language = "de";
                break;
            case FR:
                language = "fr";
                break;
            case EN:
                language = "en";
                break;
            default:
                break;
        }
        
        String url = properties.getProperty("verb.url", "http://eagle.list.lu:8080/VerbGenerator-Webservice/tense/generate?");
        String verb = key.getKeyWord().getContent();
        String lemma = key.getKeyWord().getLemma();
        String tag = key.getKeyWord().getTag();
        String isPlural = Boolean.toString(key.isPlural());
        try {
            verb = URLEncoder.encode(verb, java.nio.charset.StandardCharsets.UTF_8.toString());
            lemma = URLEncoder.encode(lemma, java.nio.charset.StandardCharsets.UTF_8.toString());            
            tag = URLEncoder.encode(tag, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING, "Problem encoding the sentence.", e);
            e.printStackTrace();
        }
        //Add parameters to the url & connect
        url += "verb="+verb + "&lemma="+ lemma + "&tag="+tag + "&language="+language+"&isPlural=" + isPlural;
        String source = UrlHelper.getSource(url);
        
        //Get the verbs from a String, generate a distractor and return the list
        List<Distractor> verbTenses = new ArrayList<>();
        String[] verbs = source.split(", ");
        for (String distractor : verbs) {
            Word word = new Word(distractor, key.getKeyWord().getSentenceWordNumber(), key.getKeyWord().getLanguage());  
            verbTenses.add(new Distractor(word, 1.0));
        }
        
        return verbTenses;
    }
    
}
