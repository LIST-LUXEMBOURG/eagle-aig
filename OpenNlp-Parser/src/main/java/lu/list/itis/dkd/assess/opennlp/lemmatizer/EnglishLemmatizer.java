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
package lu.list.itis.dkd.assess.opennlp.lemmatizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.ModelLoader;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import net.sf.hfst.NoTokenizationException;
import net.sf.hfst.Transducer;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class EnglishLemmatizer {    
    protected static final Logger logger = Logger.getLogger(EnglishLemmatizer.class.getName());

    static String getLemma(String word, String tag) throws IOException {        
        Transducer transducer = ModelLoader.getLemmaModel(Language.EN);        
        Collection<String> analyses = new ArrayList<>();
        try {
            analyses = transducer.analyze(word.toLowerCase());
        } catch (NoTokenizationException e) {
            //TODO logger info???
            if (word.contains(" ")) {
                return word;
            }
            
            logger.log(Level.WARNING, "Lemmatizing the word (" + word + ") failed!");
            return word;
        }
        
        for (String analysis : analyses) {            
            if (GrammarHelper.isNoun(tag, Language.EN)) {
                if (analysis.contains("[N]+N")) {
                    return analysis.substring(0, analysis.indexOf("["));
                }
            } 
            else if (GrammarHelper.isVerb(tag, Language.EN)) {
                if (analysis.contains("[V]+V")) {
                    return analysis.substring(0, analysis.indexOf("["));
                }
            } 
            else if (GrammarHelper.isAdj(tag, Language.EN)) {
                if (tag.equals("JJ")) {
                    if (analysis.contains("[ADJ]+ADJ") && !analysis.contains("COMP")) {
                        return analysis.substring(0, analysis.indexOf("["));
                    }
                }
                else if (analysis.contains("[ADJ]+ADJ")) {
                    return analysis.substring(0, analysis.indexOf("["));
                }
            } 
            else if (GrammarHelper.isAdv(tag, Language.EN)) {
                if (tag.equals("RB")) {
                    if (analysis.contains("[ADV]+ADV") && !analysis.contains("COMP")) {
                        return analysis.substring(0, analysis.indexOf("["));
                    }
                }
                else if (analysis.contains("[ADV]+ADV")) {
                    return analysis.substring(0, analysis.indexOf("["));                    
                }
            }
        }
        
        return word;
    }    
}
