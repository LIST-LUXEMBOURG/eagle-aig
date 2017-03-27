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
import lu.list.itis.dkd.assess.opennlp.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0
 * @version 2.0.1
 */
public class LevenshteinTagDistance {
    private Sentence sentence1;
    private Sentence sentence2;
    private int difference;
    private int levenstheinDistance = 0;
    private List<String> commonTags = new ArrayList<>();
    private List<String> allTypeOfTags = new ArrayList<>();
    private List<String[]> sequenceTags = new ArrayList<>();
    
    public LevenshteinTagDistance(Sentence sentence1, Sentence sentence2) {
        this.sentence1 = sentence1;
        this.sentence2 = sentence2;
        this.difference = Math.abs(sentence1.getWords().size() - sentence2.getWords().size());
        this.levenstheinDistance = tagDistance(sentence1.getWords(), sentence2.getWords());

        tagStatistic();
        tagSequenceStatistic();
    }
    
    private int tagDistance(List<Word> words1, List<Word> words2) {
        int row = words1.size();
        int col = words2.size();
        
        String[] rowTags = new String[row];        
        for (int i = 0; i < row; i++) {
            rowTags[i] = words1.get(i).getTag();
        }
        
        String[] colTags = new String[col];
        for (int j = 0; j < col; j++) {
            colTags[j] = words2.get(j).getTag();
        }
        
        int[][] dTags = new int[row][col];
        
//        for(int i = 0; i < row * col ; ++i){
//            allTags[i/row][i % col] = 0;
//        }
//        
        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j++) {
                dTags[i/row][i % col] = 0;
            }
        }
        
        for (int i = 1; i < row; i++) {
            dTags[i][0] = i;
        }
        for (int j = 0; j < col; j++) {
            dTags[0][j] = j;
        }
        
        for (int j = 1; j < col; j++) {
            for (int i = 1; i < row; i++) {
                // no operation required
                if (rowTags[i].equals(colTags[j])) {
                    dTags[i][j] = dTags[i-1][j-1];
                }
                else {
                    int deletion = dTags[i-1][j] + 1;
                    int insertion = dTags[i][j-1] + 1;
                    int substitution = dTags[i-1][j-1] + 1;
                    
                    dTags[i][j] = Math.min(deletion, Math.min(insertion, substitution));
                }
            }
        }
        
        return dTags[row-1][col-1];
    }
    
    private void tagSequenceStatistic() {
        int tagPos = 0;
        String sequence = "";
        for (int i = 0; i < sentence1.getWords().size(); i++) {
            
            Word word1 = sentence1.getWords().get(i);
            for (int j = tagPos; j < sentence2.getWords().size(); j++) {
                Word word2 = sentence2.getWords().get(j);
                
                if (word2.getTag().equals(word1.getTag())) {
                    sequence += word1.getTag() + " ";
                    if (j + 1 < sentence2.getWords().size()) {
                        tagPos = j + 1;
                    }
                    
                    break;
                }
                else {
                    String[] sequences = sequence.split(" ");
                    if (sequences.length > 1) {
                        sequenceTags.add(sequences);
                    }
                    sequence = "";
                }
            }
        }
    }
    
    private void tagStatistic() {
        for (Word word : sentence1.getWords()) {
            
            String tag = word.getTag();
            
            //Count number of tags in sentence 1
            if (!allTypeOfTags.contains(tag)) {
                allTypeOfTags.add(tag);
            }
            
            //Search shared tags
            if (sentence2.getTaggedSentence().contains(tag)) {
                if (!commonTags.contains(tag)) {
                    commonTags.add(tag);
                }
            } 
        }
    }

    public Sentence getSentence1() {
        return sentence1;
    }
    
    public Sentence getSentence2() {
        return sentence2;
    }

    public int getDifference() {
        return difference;
    }

    public List<String> getCommonTags() {
        return commonTags;
    }

    public List<String> getAllTypeOfTags() {
        return allTypeOfTags;
    }

    public List<String[]> getSequenceTags() {
        return sequenceTags;
    }

    public int getDistance() {
        return levenstheinDistance - commonTags.size();
    }    
    
    public double getNormalizedDistance() {
        return 1 - ((levenstheinDistance - commonTags.size()) / (double) Math.max(sentence1.getWords().size(), sentence2.getWords().size()));
    }
}
