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
package lu.list.itis.dkd.assess.cloze.option;

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Distractor {
    private String feedback = "The response is incorrect.";
    private Language language;
    private Word distractorWord;
    private double similarity;

    /**
     * Creates a distractor object based on the word object.
     * @param word: Word object.
     * @param similarity: Distributional similarity distance between key and distractor.
     */
    public Distractor(Word word, double similarity){
        this.distractorWord = word;
        this.language = word.getLanguage();
        this.similarity = similarity;
        setFeedback();
    }
    
    /**
     * Creates a distractor object based on the word object.
     * @param word: Word object.
     * @param similarity: Distributional similarity distance between key and distractor.
     * @param feedback: Content provided if the wrong answer was picked. 
     */
    public Distractor(Word word, double similarity, String feedback){
        this.distractorWord = word;
        this.language = word.getLanguage();
        this.similarity = similarity;
        this.feedback = feedback;
    }
    
    private void setFeedback(){
        switch (language) {
            case DE:
                this.feedback = "Die Antowrt ist falsch.";
                break;
            case FR:
                this.feedback = "La réponse est incorrecte.";
                break;
            default:
                this.feedback = "The response is incorrect.";
                break;
        }
    }

    public Word getDistractorWord() {
        return distractorWord;
    }

    public void setDistractorWord(Word word) {
        this.distractorWord = word;
    }

    /**
     * @return Distributional Similarity between key and distractor. 
     */
    public double getSimilarity() {
        return similarity;
    }
    
    public String getFeedback(){
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}