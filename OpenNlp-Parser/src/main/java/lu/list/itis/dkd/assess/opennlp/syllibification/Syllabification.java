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
package lu.list.itis.dkd.assess.opennlp.syllibification;

import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class Syllabification {
    
    protected static final Logger logger = Logger.getLogger(Syllabification.class.getName());
    
	/**
	 * Method that returns the number of Syllables for a Word object.
	 * @param word
	 * @return
	 */
    public static int getNumberOfSyllables(Word word) {
        return getNumberOfSyllables(word.getContent(), word.getLanguage());
    }
    
    /**
     * Method that return the number of Syllables for a text object.
     * @param text
     * @return
     */
    public static int getNumberOfSyllables(Text text) {
        int numberOfSyllables = 0;
        for (Sentence sentence : text.getSentences()) {
            numberOfSyllables += getNumberOfSyllables(sentence);
        }
    	return numberOfSyllables;
    }
    
    /**
     * Method that return the number of Syllables for a text.
     * @param text
     * @return 
     */
    public static int getNumberOfTotalSyllables(String text, Language language) {
        Text textObject = new Text(text, language);
        return textObject.getNumberOfSyllables();
    }
    
    /**
     * Method that returns the number of Syllables for a sentence object.
     * @param sentence
     * @return
     */
    public static int getNumberOfSyllables(Sentence sentence) {
    	int numberOfSyllables = 0;
    	for (Word word : sentence.getWords()) {
    		numberOfSyllables += getNumberOfSyllables(word);
    	}
    	return numberOfSyllables;
    }
    
    /**
     * Method that returns the number of syallables for a word in the specific language. If the language is not 
     * supported, English will be used. The supported languages are, English, German and French!!
     * @param word
     * @param language
     * @return
     */
    public static int getNumberOfSyllables(String word, Language language) {
        switch (language) {
            case DE:
                if (!word.equals("")) {
                    GermanSyllabification germanSyllabification = new GermanSyllabification(word);
                    return germanSyllabification.getNumberOfSyllables();
                }
                return 1;
            case FR:   
                return FrenchSyllabification.getNumberOfSyllabes(word);
            case EN: 
                EnglishSyllabification englishSyllabification = new EnglishSyllabification(word);
                return englishSyllabification.findNumberOfSyllables();
            default:
                return 1;
        }
    }
}
