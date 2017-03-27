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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Key {
    private String feedback = "The response is correct.";
    private Word keyWord;
    private Language language;
    private String article = "";
    private int keyNumber;
    private boolean isPlural = false;
    private List<Distractor> distractors = new ArrayList<>();
    
    protected static final Logger logger = Logger.getLogger(Key.class.getSimpleName());

    /**
     * Creates a key object based on the word object.
     * @param word: word object
     */
    public Key(Word keyWord){
        this.keyWord = keyWord;
        this.language = keyWord.getLanguage();
        setFeedback();
    }
    
    /**
     * Creates a key object based on the word object.
     * @param word: word object
     * @param feedback: Content provided if the correct answer was picked. 
     */
    public Key(Word keyWord, String feedback){
        this.keyWord = keyWord;
        this.language = keyWord.getLanguage();
        this.feedback = feedback;
    }
    
    private void setFeedback(){
        switch (language) {
            case DE:
                this.feedback = "Die Antowrt ist richtig.";
                break;
            case FR:
                this.feedback = "La réponse est correcte.";
                break;
            default:
                this.feedback = "The response is correct.";
                break;
        }
    }

    public Word getKeyWord() {
        return keyWord;
    }
    
    public void setKeyWord(Word word) {
        this.keyWord = word;
    }
    
    public List<Distractor> getDistractors() {
        return distractors;
    }
    
    /**
     * Returns the x best distractors for the key or the maximum if too many were asked.
     * @param numberOfDistractors: A number between 1 and all distractors.
     * @return A list of the n best distractors found.
     */
    public List<Distractor> getBestDistractors(int numberOfDistractors) {
        List<Distractor> bestDistractors = new ArrayList<>();
        
        if (distractors.size() < numberOfDistractors) {
            logger.log(Level.INFO, "Not enough distractors available, number of distractors set to the maximum, so " + distractors.size());
            numberOfDistractors = distractors.size();
        }
        
        for (int i = 0; i < numberOfDistractors; i++) {
            bestDistractors.add(distractors.get(i));
        }
        return bestDistractors;
    }
    
    public void setDistractors(List<Distractor> distractors) {
        this.distractors = distractors;
    }
    
    public void setKeyNumber(int number){
        keyNumber = number;
    }

    public int getKeyNumber() {
        return keyNumber;
    }
    
    public String getArticle(){
        return article;
    }
    
    public void setArticle(String article) {
        this.article = article;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setPlural(boolean isPlural) {
        this.isPlural = isPlural;
    }

    public boolean isPlural() {
        return isPlural;
    }
}
