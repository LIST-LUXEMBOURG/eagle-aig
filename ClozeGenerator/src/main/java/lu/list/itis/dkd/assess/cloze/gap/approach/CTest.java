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
package lu.list.itis.dkd.assess.cloze.gap.approach;

import java.util.ArrayList;
import java.util.List;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.GapHelper;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class CTest {

    /**
     * Returns a C-Test sentence, where every second word is replaced by half the word + underscore.
     * Example: factory = fac____
     * @param A sentence object.
     * @return A clozeSentence Object
     */
    public static ClozeSentence getClozeSentence(Sentence sentence) { 
        return getClozeSentence(sentence, 2);
    }
    
    /**
     * Returns a C-Test sentence, where each word that matches the distance, is replaced 
     * by half the word + underscore. Example: factory = fac____
     * @param A sentence object.
     * @param A distance. Hence the ith word which should be transformed. The standard is 2.
     * @return A clozeSentence Object
     */
    public static ClozeSentence getClozeSentence(Sentence sentence, int distance) { 
        String clozeSentence = "";
        int keyNumber = 1;
        List<Key> keys = new ArrayList<>();
        //Only consider each second word
        for (int j = 0; j < sentence.getWords().size()-1; j++) {
            Word word = sentence.getWord(j+1);
            if ((j + 1) < sentence.getWords().size() && ((j+1) % distance == 0)) {
                //Create key object % set information
                Key key = new Key(word);
                key.setKeyNumber(keyNumber);
                keys.add(key);
                keyNumber++;
                            
                //Create cloze sentence                
                String cWord = word.getContent();
                cWord = cWord.substring(0, cWord.length()/2);
                clozeSentence += cWord + "________ ";
            }
            else {
                clozeSentence += word.getContent() + " ";
            }
        }
            
        return GapHelper.correctAndCreateCloze(sentence, clozeSentence, keys);
    }
}
