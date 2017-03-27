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

import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.opennlp.stemmer.Stemmer;
import lu.list.itis.dkd.assess.opennlp.soundex.Soundex;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Sound {

    /**
     * Checks if the key and the distractor has the same Soundex value.
     * @param key
     * @param distractor
     * @return Returns true if the key sounds similar than the distractor or false otherwise.
     */
    static boolean soundsSame(Key key, String distractor) {
        Language language = key.getKeyWord().getLanguage();

        String KeyStem = key.getKeyWord().getStem();
        String KeySoundex = Soundex.getSoundex(KeyStem, language); 
        
        String distractorStem = Stemmer.getStem(distractor, language);
        String distractorSoundex = Soundex.getSoundex(distractorStem, language);
        
        if (KeySoundex.equals(distractorSoundex)) {
            return true;
        }
        
        return false;    
    }

    /**
     * Checks if value1 and the value2 has the same Soundex value in the chosen language.
     * @param key
     * @param distractor
     * @return Returns true if value1 sounds similar than value2 or false otherwise.
     */
    static boolean soundsSame(String input1, String input2, Language language) {
        String input1Stem = Stemmer.getStem(input1, language);
        String input1Soundex = Soundex.getSoundex(input1Stem, language); 
        
        String input2Stem = Stemmer.getStem(input2, language);
        String input2Soundex = Soundex.getSoundex(input2Stem, language);
        
        if (input1Soundex.equals(input2Soundex)) {
            return true;
        }
        
        return false;    
    }

}
