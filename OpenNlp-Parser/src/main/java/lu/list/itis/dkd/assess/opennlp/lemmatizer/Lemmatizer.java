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

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Lemmatizer {   
    protected static final Logger logger = Logger.getLogger(Lemmatizer.class.getName());
    
    /**
     * Method that returns the lemma of a given word in the specified language or in English 
     * if the specified language is not supported. Supported languages are English, German and French.
     * @param word
     * @param language
     * @return
     */
    public static String getLemma(String word, String tag, Language language) {
        try {
            switch (language) {
                case DE:
                    return GermanLemmatizer.getLemma(word, tag);
                case FR:
                    return FrenchLemmatizer.getLemma(word, tag);
                case EN:    
                    return EnglishLemmatizer.getLemma(word, tag);
                default:
                    return word;
        	}
        } catch (IOException e) {
            logger.log(Level.SEVERE, "The lemma of the word " + word + " is not available or could not be fount", e);
            e.printStackTrace();
        } 
        
        return word;
    }
    
    /**
     * Method that returns the lemma of a given word in the specified language or in English 
     * if the specified language is not supported. Supported languages are English, German and French.
     * @param word
     * @param language
     * @return
     */
    public static String getLemma(Word word) {
        return getLemma(word.getContent(), word.getTag(), word.getLanguage());
    }
}
