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
public class FrenchLemmatizer {
    protected static final Logger logger = Logger.getLogger(FrenchLemmatizer.class.getName());
    
    private static String clean(String word){
        String cleanedWord = word;
        if (cleanedWord.contains("’")) {
            cleanedWord = cleanedWord.substring(cleanedWord.indexOf("’")+1, cleanedWord.length());
        }
        if (cleanedWord.contains("…")) {
            cleanedWord = cleanedWord.replace("…", "");
        }
        return cleanedWord;
    }

    static String getLemma(String word, String tag) throws IOException {
        String cleanedWord = clean(word);
        Transducer transducer = ModelLoader.getLemmaModel(Language.FR);
        Collection<String> analyses = new ArrayList<>();
        try {
            analyses = transducer.analyze(cleanedWord.toLowerCase());
        } catch (NoTokenizationException e) {
            logger.log(Level.WARNING, "Lemmatizing the word (" + cleanedWord + ") failed!");
            return cleanedWord;
        }
        
        for (String analysis : analyses) {
            if (GrammarHelper.isNoun(tag, Language.FR)) {
                if (analysis.contains("commonNoun")) {
                    return analysis.substring(0, analysis.indexOf("+"));
                }
            }
            else if (GrammarHelper.isVerb(tag, Language.FR)) {
                if (analysis.contains("verb")) {
                    return analysis.substring(0, analysis.indexOf("+"));
                }
            }
            else if (GrammarHelper.isAdj(tag, Language.FR)) {
                if (analysis.contains("adjective")) {
                    return analysis.substring(0, analysis.indexOf("+"));
                }
            }
            else if (GrammarHelper.isAdv(tag, Language.FR)) {
                if (analysis.contains("adverb")) {
                    return analysis.substring(0, analysis.indexOf("+"));
                }
            }
            else if (tag.equals("PRO") || tag.equals("P") || tag.equals("CC") || tag.equals("CS") || tag.equals("CLO")) {
                if (analysis.contains("functionWord")) {
                    return analysis.substring(0, analysis.indexOf("+"));
                }
            } 
        }
        
        if (analyses.size() > 1) {
            String lemma = analyses.toArray()[analyses.size()-1].toString();
            return lemma.substring(0, lemma.indexOf("+"));
        }
        
        return cleanedWord;
    }
}
