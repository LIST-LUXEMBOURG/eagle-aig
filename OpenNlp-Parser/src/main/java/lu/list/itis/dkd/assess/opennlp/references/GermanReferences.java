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
package lu.list.itis.dkd.assess.opennlp.references;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class GermanReferences {    
    private static final List<String> REFERENCES = Arrays.asList(
                    "den", "das", "dem", "dessen", "der", "die", "denen", "deren", "derer",
                    "er", "sie", "es", "sich", "sein", "seiner", "seins", "seine", "seines", 
                    "seinem", "seinen", "ihre", "ihres", "ihrem", "ihren", "ihn", "ihm", 
                    "ihr", "ihrer", "ihnen",
                    "dieser", "diesen", "diesem", "dieses", "dies", "diese", "jener", "jenen", 
                    "jenem", "jenes", "jene", "solcher", "solchen", "solchem", "solches", "solche", 
                    "derjenige", "denjenigen", "demjenigen", "desjenigen", "dasjenige", "diejenige", 
                    "derjenigen", "diejenigen", "derselbe", "denselben", "demselben", "desselben", 
                    "dieselbe", "derselben", "dasselbe", "dieselben"
    );
    
    /**
     * Referents den, das, dem, dessen, der, die, denen, deren, derer if they refer to a previously mentioned noun.
     * If Demonstrativepronomen stand alone and not included in an NP (are not followed by Noun or Adjective+Noun 
     * in the same gender, casus, singualar/plural) & if in the previous sentence or clause there is an NP that 
     * agrees on gender, casus, singular/plural, then count them as referents.   
     */
    static int getNumberOfReferences(Sentence sentence){
        int number = 0;
        for (Word word : sentence.getWords()) {
            if (REFERENCES.contains(word.getContent().toLowerCase())) {
                //If "selbst" then don' t count it as demonstrative pronoun.
                if (word.getContent().equals("selbst")) {
                    continue;
                }
                boolean reference = true;
                int numberOfWords = sentence.getNumberOfWords();
                int wordNumber = word.getSentenceWordNumber();
                
                int nextWordNumber = wordNumber + 1;
                if (nextWordNumber < numberOfWords){
                    Word nextWord = sentence.getWord(nextWordNumber);
                    if (nextWord.isNoun()) {
                        reference = false;
                    }
                }
                
                int secondNextWordNumber = wordNumber + 2;
                if (secondNextWordNumber < numberOfWords){
                    Word nextWord = sentence.getWord(secondNextWordNumber);
                    if (nextWord.isNoun()) {
                        reference = false;
                    }
                }
                
                if (reference) {
                    number++;
                }
            }
        }
        
        return number;        
    }
    
    static boolean isReference(Word word) {
        if (word.getContent().equals("selbst")) {
            return false;
        }
        if (REFERENCES.contains(word.getContent().toLowerCase())) {
            return true;
        }
        return false;
    }

    /**
     * Better reference Method than using one word.
     * @param word
     * @param nextWord
     * @return True if the word is a reference or false otherwise.
     */
    static boolean isReference(Word word, Word nextWord) {
        if (REFERENCES.contains(word.getContent().toLowerCase())) {
            //If "selbst" then don' t count it as demonstrative pronoun.
            if (word.getContent().equals("selbst")) {
                return false;
            }    
            if (nextWord.isNoun()) {
                return false;
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Better reference Method than using two words.
     * @param word
     * @param nextWord
     * @param afterNextWord nextWord
     * @return True if the word is a reference or false otherwise.
     * @return
     */
    static boolean isReference(Word word, Word nextWord, Word afterNextWord) {
        if (isReference(word, nextWord) && !afterNextWord.isNoun()) {
            return true;
        }
        
        return false;
    }
    
}
