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

import java.util.Arrays;
import java.util.List;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class EnglishReferences {
    private static List<String> REFERENCES = Arrays.asList("he", "she", "it", "they", "this", "these", "that", "those");
    
    static int getNumberOfReferences(Sentence sentence) {
        int number = 0;
        for (Word word : sentence.getWords()) {
            if (isReference(word)) {
                number++;
            }
        }
        return number;
    }
    
    static boolean isReference(Word word) {
        if (REFERENCES.contains(word.getContent().toLowerCase())) {
            return true;
        }
        return false;
    }
}
