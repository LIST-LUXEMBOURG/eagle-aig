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
package lu.list.itis.dkd.assess.opennlp.levenshtein;

import lu.list.itis.dkd.assess.opennlp.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0
 * @version 2.0.1
 */
public class TagComparison {
    private List<Sentence> sentences = new ArrayList<>();
    private SentenceStatistics statistics = new SentenceStatistics();
    
    public TagComparison(List<Sentence> sentences) {
        this.sentences.addAll(sentences);
        compare();
    }
    
    private void compare() {        
        for (int i = 0; i < sentences.size(); i++) {
            for (int j = i+1; j < sentences.size(); j++) {
                LevenshteinTagDistance statistic = new LevenshteinTagDistance(sentences.get(i), sentences.get(j));
                statistics.addSentenceStatistic(statistic);
            }
        }
    }

    public SentenceStatistics getStatistics() {
        return statistics;
    }
}
