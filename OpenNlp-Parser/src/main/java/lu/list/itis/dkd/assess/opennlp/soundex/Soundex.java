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
package lu.list.itis.dkd.assess.opennlp.soundex;

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Soundex {
    
    /**
     * A better ohonetic algorithm for German.
     * @param word
     * @return The Kölner Phonetik Code if the word is in German or the simple Soundex algorithm otherwise. 
     */
    public static String getKoelnerPhonetik(Word word){
        return getKoelnerPhonetik(word.getContent(), word.getLanguage());
    }
    
    /**
     * A better soundex
     * @param word
     * @param language
     * @return The Kölner Phonetik Code if the word is in German word or the simple soundex algorithm otherwise. 
     */
    public static String getKoelnerPhonetik(String word, Language language){
        switch (language) {
            case DE:
                return GermanKoelnerPhonetik.getOriginal(word);
            default:
                return getSoundex(word, language);
        }
    }
    
    /**
     * @param word
     * @return The Soundex of the word or the word itself otherwise.
     */
    public static String getSoundex(Word word) {
        return getSoundex(word.getContent(), word.getLanguage());
    }
    
    /**
     * @param word
     * @param language
     * @return The Soundex of the word or the word itself otherwise.
     */
    public static String getSoundex(String word, Language language) {
        switch (language) {
            case DE:
                return GermanSoundex.getSoundex(word);
            case FR:
                return FrenchSoundex.getSoundex(word);
            case EN:
                return EnglishSoundex.getSoundex(word);
            default:
                return word;
        }
        
    }
}
