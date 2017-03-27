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

import java.util.List;

import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Evaluation {    
    static boolean useSoundex = true;
    
    private static boolean containsNumber(String word){
        if (word.contains("1") || word.contains("2") || word.contains("3") ||
            word.contains("4") || word.contains("5") || word.contains("6") ||
            word.contains("7") || word.contains("8") || word.contains("9")) {
            return true;
        }
        return false;
    }
    
    /**
     * Evaluates if a distractor is a valid distractor by discarding distractors which contain:
     * 1. non-word symbols (e.g. ".", "?", ...)
     * 2. numbers
     * 3. are contained in the key or vice versa (e.g. fish & jellyfish)
     * 4. distractors that sounds similar than the key (based on Soundex test).
     * @param key
     * @param distractor
     * @param language
     * @return Returns true if the distractor can be considered and false otherwise.
     */
    static boolean evaluateDistractor(String key, String distractor, Language language){        
        //Punctuation
        if (distractor.contains(".") || distractor.contains("?") || distractor.contains("!")) {
            return false;
        }
        
        //Hyphens
        if (distractor.contains("-") || distractor.contains("_")) {
            return false;
        }
        
        //Numbers
        if (containsNumber(distractor)) {
            return false;
        }
        
        //Distractor should not contain the answer
        if (distractor.toLowerCase().contains(key.toLowerCase())) {
            return false;
        }
        
        //Soundex check
        if (useSoundex) {
            if (Sound.soundsSame(key, distractor, language)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Evaluate if a similar distractor was already found.  
     * @param distractor
     * @param distractors
     * @return Returns true if the list already contains a similar distractor or false otherwise.
     */
    static boolean evaluateList(String distractor, List<Distractor> distractors) {
        for (Distractor listDistractor : distractors) {
            String lowerListDistractor = listDistractor.getDistractorWord().getContent().toLowerCase();
            String lowerDistractor = distractor.toLowerCase();
            if (lowerListDistractor.contains(lowerDistractor) || lowerDistractor.contains(lowerListDistractor)) {
                return false;
            }
        }
        return true;
    }
}
