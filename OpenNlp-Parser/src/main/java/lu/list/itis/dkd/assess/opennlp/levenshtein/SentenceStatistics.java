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
public class SentenceStatistics {
    private List<LevenshteinTagDistance> sentenceStatistics = new ArrayList<>();
    
    public SentenceStatistics() {}
    
    public void addSentenceStatistic (LevenshteinTagDistance statistic) {
        sentenceStatistics.add(statistic);
    }

    public List<LevenshteinTagDistance> getSentenceStatistics() {
        return sentenceStatistics;
    }
    
    public void print(){
        for (LevenshteinTagDistance statistic : sentenceStatistics) {
            Sentence sentence1 = statistic.getSentence1();
            Sentence sentence2 = statistic.getSentence2();
            
            System.out.println("Sentence Statistics (" + sentence1.getSentenceNumber() + " - " + sentence2.getSentenceNumber() + ")\n");
            
            System.out.println("Sentence " + sentence1.getSentenceNumber() + ": " + sentence1.getTaggedSentence() + " ( " + sentence1.getWords().size() + ")\n");
            System.out.println("Sentence " + sentence2.getSentenceNumber() + ": " + sentence2.getTaggedSentence() + " ( " + sentence2.getWords().size() + ")\n");

            System.out.println("Length difference: " + statistic.getDifference());
            System.out.println("Tags in common with sentence " + sentence2.getSentenceNumber() + ": " + statistic.getCommonTags() + " (" + statistic.getCommonTags().size() + " / " + statistic.getAllTypeOfTags().size() + ")\n");
            
            for (String[] sequences : statistic.getSequenceTags()) {
                System.out.print("Common sequences with sentence " + sentence2.getSentenceNumber() + ": ");
                for (String sequence : sequences) {
                    System.out.print(sequence + " ");
                }
                System.out.println("\n");
            }
            
            System.out.println("Levensthein Distance between sentence " + sentence1.getSentenceNumber() + " and sentence " + sentence2.getSentenceNumber() + ": " + statistic.getDistance() + " --> " + statistic.getNormalizedDistance());
            
            System.out.println("\n\n");
            
        }
    }
}
