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
package lu.list.itis.dkd.assess.opennlp.identification;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class EnglishIdentification {
    private static final List<String> identifications = Arrays.asList("a", "the", "this", "that");
    
    private static String getIdentification(List<Word> words) {
        for (Word word : words) {
            if (identifications.contains(word.getContent().toLowerCase())) {
                return word.getContent();
            }
        }
        return "";
    }
    
    private static String getNoun(List<Word> words) {
        for (Word word : words) {
            if (GrammarHelper.isNoun(word)) {
                return word.getContent();
            }
        }
        return "";
    }
    
    static int getNumberOfIdentifications(List<Sentence> sentences) {
        List<String> identifications = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++) {
            List<Word> words = sentences.get(i).getWords();
            String identification1 = getIdentification(words); 
            if (!identification1.equals("")) {
                String noun1 = getNoun(words);
                if (!noun1.equals("")) {
                    for (int j = i+1; j < sentences.size(); j++) {
                        String identification2 = getIdentification(sentences.get(j).getWords());
                        String noun2 = getNoun(sentences.get(j).getWords());
                        if (!identification2.equals("") && !noun2.equals("")) {
                            if (noun2.equals(noun1)) {
                                if (!identifications.contains(identification2)) {
                                    identifications.add(identification2);
                                }
                                break;
                            }
                        }
                    }
                }   
            }
        }
        
        if (identifications.size() >= 1) {
            return identifications.size();
        }
        return 0;
    }
}
