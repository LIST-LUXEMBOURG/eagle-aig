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
package lu.list.itis.dkd.assess.opennlp.stemmer;

import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.FrenchStemmer;
import org.tartarus.snowball.ext.GermanStemmer;

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Stemmer {
    
    /**
     * @param word
     * @return The stem of the word or the word itself otherwise.
     */
    public static String getStem(Word word) {
        return getStem(word.getContent(), word.getLanguage());
    }
    
    /**
     * @param word
     * @param language
     * @return The stem of the word or the word itself otherwise.
     */
    public static String getStem(String word, Language language) {
        switch (language) { 
            case DE:
                final GermanStemmer germanStemmer = new GermanStemmer();
                germanStemmer.setCurrent(word);
                germanStemmer.stem();
                return germanStemmer.getCurrent();
            case FR:
                final FrenchStemmer frenchStemmer = new FrenchStemmer();
                frenchStemmer.setCurrent(word);
                frenchStemmer.stem();
                return frenchStemmer.getCurrent();
            case EN:
                final EnglishStemmer englishStemmer = new EnglishStemmer();
                englishStemmer.setCurrent(word);
                englishStemmer.stem();
                return englishStemmer.getCurrent();
            default:
                return word;
        }
    }
    
}
