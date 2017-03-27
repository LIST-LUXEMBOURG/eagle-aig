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
package lu.list.itis.dkd.assess.cloze.template;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Difficulty;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class ClozeItems {
    private static final double EASY_LOWER_BOUND = 0.5;
    private static final double EASY_UPPER_BOUND = 0.6;
    private static final double MEDIUM_LOWER_BOUND = 0.6;
    private static final double MEDIUM_UPPER_BOUND = 0.9;
    
    private static int numberOfDistractors = 3;
    private int largestDistractorSet = 0;
    private List<ClozeItem> clozeItems = new ArrayList<>();
    private List<Gap> easyGaps = new ArrayList<>();
    private List<Gap> mediumGaps = new ArrayList<>();
    private List<Gap> hardGaps = new ArrayList<>();

    protected static final Logger logger = Logger.getLogger(ClozeItems.class.getSimpleName());
    
    /**
     * Creates cloze item objects out of a clozeText. This cloze items can then be used to create a template.
     * @param clozeText: cloze text object
     */
    public ClozeItems(ClozeText clozeText) {
        logger.log(Level.INFO, "Creating cloze items.");
        List<Gap> gaps = new ArrayList<>();
        
        for (Key key : clozeText.getClozeKeys()) {
            Gap gap = new Gap(key, key.getDistractors());
            gaps.add(gap);
        }
            
        ClozeItem clozeItem = new ClozeItem(clozeText.getClozeSentences(), gaps);
        clozeItems.add(clozeItem);
    }

    /**
     * Creates a cloze item objects for the chosen difficulty only. This cloze items can then be used to create a template.
     * @param clozeText: cloze text object
     * @param numberOfDistractors: A value to specifiy how many distractors per gap are needed. Warning, 
     * the larger the number, the less gaps may be created (Standard is 3). 
     * @param difficulty: Hard, Medium, Hard. Warning, with difficulty low and hard, less items are generated,
     * because the Medium set is normally larger.
     * @throws IOException
     */
    public ClozeItems(ClozeText clozeText, int numberOfDistractors, Difficulty difficulty) {
        logger.log(Level.INFO, "Creating " + difficulty.toString() + " cloze items.");
        ClozeItems.numberOfDistractors = numberOfDistractors;
        rangeDistractors(clozeText);
        switch (difficulty) {
            case EASY:
                explode(clozeText, easyGaps);
                break;
            case MEDIUM:
                explode(clozeText, mediumGaps);
                break;
            case HARD:
                explode(clozeText, hardGaps);
                break;
            default:
                explode(clozeText, mediumGaps);
                break;
        }
        logger.log(Level.INFO, largestDistractorSet + " " + difficulty.toString() + " cloze items created.");
    }
    
    private int numberOfItems(List<Gap> gaps){
        for (Gap gap : gaps) {
            int currentSetSize = gap.getDistractors().size(); 
            if (currentSetSize > largestDistractorSet) {
                largestDistractorSet = currentSetSize;
            }
        }
        return largestDistractorSet;
    }
    
    private Gap forward(Gap gap){
        List<Distractor> exploid = new ArrayList<>();

        int index = gap.getIndex();
        if (index+numberOfDistractors <= gap.getDistractors().size()){
            for (int k = 0; k < numberOfDistractors; k++) {
                exploid.add(gap.getDistractors().get(index+k));
            }
            
            index++;
            gap.setIndex(index++);
            return new Gap(gap.getKey(), exploid);
        }
        
        return backward(gap);
    }
    
    private Gap backward(Gap gap){
        int index = gap.getIndex();
        if (index-(numberOfDistractors-1) < 0){
            return forward(gap);
        }
        
        List<Distractor> exploid = new ArrayList<>();
        for (int k = 0; k < numberOfDistractors; k++) {
            exploid.add(gap.getDistractors().get(index-k));
        }
        
        index--;
        gap.setIndex(index);
        return new Gap(gap.getKey(), exploid);
    }
    
    private void explode(ClozeText clozeText, List<Gap> gaps) {
        List<Gap> temp = new ArrayList<>();
        List<ClozeSentence> newClozeSentences = new ArrayList<>();

        //Exploit Gaps
        for (int i = 0; i < numberOfItems(gaps); i++) {
            for (Gap gap : gaps) {
                //number of distractor same as size of distractors. Hence use the array.
                if (gap.getDistractors().size() == numberOfDistractors) {
                    temp.add(gap);
                }
                //TODO Implement Window instead of this approach
                //Take distractors out of the list in forward or backward direction
                else {
                    temp.add(forward(gap));
                }
            }
        
            //Correct cloze Sentences
            List<Key> sentenceKeys = new ArrayList<>();
            List<Key> allKeys = new ArrayList<>();
            for (ClozeSentence clozeSentence : clozeText.getClozeSentences()) {
                //Temporary save the keys 
                for (Key key : clozeSentence.getKeys()) {
                    for (Gap gap : temp) {
                        if (gap.getKey().equals(key)) {
                            sentenceKeys.add(key);
                            allKeys.add(key);
                            break;
                        }
                    }
                }

                String newClozeSentenceString = clozeSentence.getContent();
                for (Key key : clozeSentence.getKeys()) {
                    //The sentence does not have any keys
                    if (sentenceKeys.size() == 0) {
                        newClozeSentenceString = clozeSentence.getSentence().getContent();
                    }
                    //The sentence have less or equal amount of keys as before, correcting is needed.
                    else {
                        if (!sentenceKeys.contains(key)) {
                            logger.log(Level.INFO, "I replace ___" + key.getKeyNumber() + "___ by " + key.getKeyWord().getContent());
                            newClozeSentenceString = newClozeSentenceString.replace("___"+key.getKeyNumber()+"___", key.getKeyWord().getContent());
                        }
                        else {
                            int keyNumber = 0;
                            for (Key sentenceKey : allKeys) {
                                if (sentenceKey.equals(key)) {
                                    break;
                                }
                                keyNumber++;
                            }
                            logger.log(Level.INFO, "I replace ___" + key.getKeyNumber() + "___ by ___"+(keyNumber+1)+"___");
                            newClozeSentenceString = newClozeSentenceString.replace("___"+key.getKeyNumber()+"___", "___"+(keyNumber+1)+"___");
                        }
                    }   
                }

                //Create cloze sentence
                ClozeSentence newClozeSentence = new ClozeSentence(clozeSentence.getSentence(), newClozeSentenceString, new ArrayList<>(sentenceKeys));
                newClozeSentences.add(newClozeSentence);
                sentenceKeys.clear();
            }
        
            //Add item
            ClozeItem clozeItem = new ClozeItem(new ArrayList<>(newClozeSentences), new ArrayList<>(temp));
            clozeItems.add(clozeItem);
            
//            for (QtiGap gap : temp) {
//                for (Distractor distractor : gap.getDistractors()) {
//                    System.out.println(distractor.getDistractor().getWord() + ", ");
//                }
//            }
            
            //Reset temp variables
            temp.clear();
            newClozeSentences.clear();
        }
    }
    
    private void rangeDistractors(ClozeText clozeText) {
        //Temp distractor lists
        List<Distractor> easyDistractors = new ArrayList<>();
        List<Distractor> mediumDistractors = new ArrayList<>();
        List<Distractor> hardDistractors = new ArrayList<>();
        
        //Range the keys into 3 different difficulties
        for (Key key : clozeText.getClozeKeys()) {
            for (Distractor distractor : key.getDistractors()) {
                double similarity = distractor.getSimilarity();
                if (similarity > EASY_LOWER_BOUND && similarity <= EASY_UPPER_BOUND) {
                    easyDistractors.add(distractor);
                }
                else if (similarity > MEDIUM_LOWER_BOUND && similarity < MEDIUM_UPPER_BOUND) {
                    mediumDistractors.add(distractor);
                }
                else {
                    hardDistractors.add(distractor);
                }
            }
            
            //Fill gap Lists
            if (easyDistractors != null && easyDistractors.size() >= numberOfDistractors) {
                Gap gap = new Gap(key, new ArrayList<>(easyDistractors));
                easyGaps.add(gap);
            }
            
            if (mediumDistractors != null && mediumDistractors.size() >= numberOfDistractors) {
                Gap gap = new Gap(key, new ArrayList<>(mediumDistractors));
                mediumGaps.add(gap);
            }
            
            if (hardDistractors != null && hardDistractors.size() >= numberOfDistractors) {
                Gap gap = new Gap(key, new ArrayList<>(hardDistractors));
                hardGaps.add(gap);
            }
            
            //Reset temp distractor lists
            easyDistractors.clear();
            mediumDistractors.clear();
            hardDistractors.clear();
        }
    }

    public List<ClozeItem> getClozeItems() {
        return clozeItems;
    }
    
}
