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

import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Tag {
    
    private static boolean englishNoun(Key key, Distractor distractor) {
        String keyTag = key.getKeyWord().getTag(); 
        
        //Combines two sorts of single nouns as one noun.
        if (keyTag.equals("NN") || keyTag.equals("NNP")) {
            String distractorTag = distractor.getDistractorWord().getTag();
            if (distractorTag.equals("NN") || distractorTag.equals("NNP")) {
                return true;
            }
            return false;
        }
        
        //Combines two sorts of plural nouns as one plural noun.
        else if (keyTag.equals("NNS") || keyTag.equals("NNPS")) {
            String distractorTag = distractor.getDistractorWord().getTag();
            if (distractorTag.equals("NNS") || distractorTag.equals("NNPS")) {
                return true;
            }
            return false;
        }
        
        return false;
    }

    /**
     * Checks if the key tag and the distractor tag are the same.
     * @param key
     * @param distractor
     * @return Returns true if the have the same tag or false otherwise.
     */
    static boolean isSame(Key key, Distractor distractor) {
        if (GrammarHelper.isAdj(key.getKeyWord())) {
            if (GrammarHelper.isAdj(distractor.getDistractorWord())) {
                return true;
            }
        }
        else if (GrammarHelper.isAdv(key.getKeyWord())) {
            if (GrammarHelper.isAdv(distractor.getDistractorWord())) {
                return true;
            }
        }
        else if (GrammarHelper.isNoun(key.getKeyWord())) {
            if (key.getKeyWord().getLanguage().equals(Language.EN)) {
                return englishNoun(key, distractor);
            }
            
            if (GrammarHelper.isNoun(distractor.getDistractorWord())  || distractor.getDistractorWord().getTag().equals("XY")) {
                return true;
            }
        }
        else if (GrammarHelper.isVerb(key.getKeyWord())) {
            if (GrammarHelper.isVerb(distractor.getDistractorWord())) {
                return true;
            }
        }
        return false;
    }
}
