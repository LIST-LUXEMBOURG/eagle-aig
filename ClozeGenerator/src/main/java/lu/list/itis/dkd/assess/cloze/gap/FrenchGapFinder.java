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
package lu.list.itis.dkd.assess.cloze.gap;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lu.list.itis.dkd.assess.cloze.generation.DistractorGenerator;
import lu.list.itis.dkd.assess.cloze.generation.VerbGenerator;
import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.GapHelper;
import lu.list.itis.dkd.assess.cloze.util.KodaAnnotation;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class FrenchGapFinder {
    private int numberOfDistractors = 3;
    private int identifier = 1;
    private List<Key> previousKeys = new ArrayList<>();
    private List<String> exclusions = new ArrayList<>();
    
    protected static final Logger logger = Logger.getLogger(FrenchGapFinder.class.getSimpleName());
    
    public FrenchGapFinder(int numberOfDistractors) {
        this.numberOfDistractors = numberOfDistractors;
    }
    
    public FrenchGapFinder() {
        this.numberOfDistractors = 3;
    }
    
    /**
     * Returns a gap sentence for the sentence if one or more key(s) were fount based on annotations.
     * @param sentence
     * @param retrieveDistractors: If true, distractors are retrieved for the key(s).
     * @return gap sentence
     */
    public ClozeSentence findAnnotations(Sentence sentence, boolean retrieveDistractors) {
        List<Key> keys = new ArrayList<>();
        String clozeSentence = "";
        String article = "";        
        
        List<String> annotations = new ArrayList<>();
        annotations = KodaAnnotation.getAnnotations(sentence);
        if (annotations.size() == 0 || annotations == null) {
            logger.log(Level.INFO, "Koda did not found any results, another approach based on nouns is used.");
            return findNouns(sentence, retrieveDistractors);
        }
        
        for (int i = 0; i < sentence.getWords().size()-1; i++) {
            Word word = sentence.getWord(i+1);
            
            //Save last article!
            if (word.getTag().equals("DET")) {
                article = word.getContent();
            }
            
            //Check if the word has been annotated by Koda
            boolean isAnnotated = false;
            for (String annotation : annotations) {
                if (annotation.contains(word.getContent())) {
                   isAnnotated = true;
                   break;
                }
            }
            
            //Add word to the sentence if it has not been annotated
            if (!isAnnotated) {
                clozeSentence += word.getContent() + " ";
                continue;
            }
            
            //Add word to the sentence if it is not unique
            if (!isUnique(word)) {
                clozeSentence += word.getContent() + " ";
                continue;
            }
            
            //Create key object
            Key key = new Key(word);
            key.setArticle(article);
            article = "";

            //Search distractors for the key
            if (retrieveDistractors){
                List<Distractor> distractors = DistractorGenerator.getDistractors(sentence, key, numberOfDistractors);                    
                if (distractors == null || distractors.size() == 0) {
                    clozeSentence += word.getContent() + " ";
                    continue;
                }  
                key.setDistractors(distractors);                
            }    
                
            //Set key information
            key.setKeyNumber(identifier);
            keys.add(key);
            if (!previousKeys.contains(key)) {
                previousKeys.add(key);
            }
            logKeyAndDistractor(key);
                
            //Create cloze sentence
            clozeSentence += "___" + identifier + "___ ";
            identifier++;   
        }
        
        return GapHelper.correctAndCreateCloze(sentence, clozeSentence, keys);
    }
    
    /**
     * Returns a gap sentence for the sentence if one or more key(s) were fount.
     * @param sentence
     * @param retrieveDistractors: If true, distractors are retrieved for the key(s).
     * @return gap sentence
     */
    public ClozeSentence findVerbs(Sentence sentence, boolean retrieveDistractors) {
        List<Key> keys = new ArrayList<>();
        String clozeSentence = "";
        Boolean isPlural = false;
        
        this.exclusions.addAll(GapHelper.excludeNames(sentence)); 
        //Only consider a word as possible key if it it or the next word is not followed by another noun.
        for (int i = 0; i < sentence.getWords().size()-2; i++) {
            Word word = sentence.getWords().get(i);
            Word nextWord = sentence.getWords().get(i+1);
            Word afterNextWord = sentence.getWords().get(i+2);
            
            //Singular or plural phrase
            isPlural = GrammarHelper.isPlural(word);
            
            if (GrammarHelper.isVerb(word) && !GrammarHelper.isVerb(nextWord) && !GrammarHelper.isVerb(afterNextWord) ) {
                if (i > 0 && sentence.getWords().get(i-1).getTag().equals("P")) {
                    continue;
                }
                
                if (i > 1 && sentence.getWords().get(i-2).getTag().equals("P")) {
                    continue;
                }
                
                //Check if not exclusion word
                if (exclusions.contains(word.getContent())){
                    clozeSentence += word.getContent() + " ";
                    continue;
                }
                
                //Add word to the sentence if it is not unique
                if (!isUnique(word)) {
                    clozeSentence += word.getContent() + " ";
                    continue;
                }
                
                //Create key object
                Key key = null;
                List<Distractor> distractors = new ArrayList<>();
                
                //Handle infinitif
                if (word.getTag().equals("VINF") || word.getTag().equals("VPP")) {
                    //Find auxilary verb                
                    int auxilaryIndex = GapHelper.wordIndex(sentence, word, "V");
                    if (auxilaryIndex == 0) {
                        continue;
                    }
                    String indexedWord = sentence.getWord(auxilaryIndex).getContent();
                    clozeSentence = clozeSentence.replace(indexedWord + " ", "**");
                         
                    //Correct sentence
                    if (clozeSentence.contains("**")) {
                        int beginning = clozeSentence.indexOf("**");
                        String clozeSentencePart = clozeSentence.substring(beginning, clozeSentence.length());
                        keys = removeGaps(clozeSentencePart, keys);                     
                        clozeSentence = clozeSentence.substring(0, clozeSentence.indexOf("**"));
                             
                        //Create key & Set key information
                        Word auxilaryVerb = new Word(indexedWord, auxilaryIndex, Language.FR);
                        key = new Key(auxilaryVerb);
                        key.setPlural(isPlural);
                        
                        //Correct Distractor by adding the word to the generated auxilary
                        if (retrieveDistractors){
                            for (Distractor distractor : VerbGenerator.getTenses(key)) {
                                String token = distractor.getDistractorWord().getContent() + " " + word.getContent();
                                int number = distractor.getDistractorWord().getSentenceWordNumber();
                                Word distractorWord = new Word(token, number, distractor.getDistractorWord().getLanguage());
                                distractor.setDistractorWord(distractorWord);
                                distractors.add(distractor);
                            }
                            
                        }
                             
                        Word keyWord = new Word(indexedWord + " " + word.getContent(), word.getSentenceWordNumber(), Language.EN);
                        key.setKeyWord(keyWord);
                        key.setKeyNumber(identifier);
                        key.setDistractors(distractors);
                        
                        keys.add(key);
                        if (!previousKeys.contains(key)) {
                            previousKeys.add(key);
                        }
                        logKeyAndDistractor(key);
                            
                        //Create cloze sentence
                        clozeSentence += "___" + identifier + "___ ";
                        identifier++;
                    }
                }
                //Handle other verbs
                else {
                    //Find distractors for the key
                    key = new Key(word);
                    key.setPlural(isPlural);
                    if (retrieveDistractors){
                        distractors = VerbGenerator.getTenses(key);
                        if (distractors == null || distractors.size() == 0) {
                            clozeSentence += word.getContent() + " ";
                            continue;
                        }
                        key.setDistractors(distractors);                        
                    }

                    //Set key information
                    key.setKeyNumber(identifier);
                    keys.add(key);
                    if (!previousKeys.contains(key)) {
                        previousKeys.add(key);
                    }
                    logKeyAndDistractor(key);
                        
                    //Create cloze sentence
                    clozeSentence += "___" + identifier + "___ ";
                    identifier++;
                }
            }
            //If word is no key, add the word to both sentence types
            else {
                clozeSentence += word.getContent() + " ";
            }
        }
        
        return GapHelper.createClozeSentence(sentence, clozeSentence, keys);
    }

    /**
     * Returns a gap sentence for the sentence if one or more key(s) were fount.
     * @param sentence
     * @param retrieveDistractors: If true, distractors are retrieved for the key(s).
     * @return gap sentence
     */
    public ClozeSentence findNouns(Sentence sentence, boolean retrieveDistractors) {
        List<Key> keys = new ArrayList<>();
        String clozeSentence = "";
        String article = "";

        this.exclusions.addAll(GapHelper.excludeNames(sentence)); 
        //Only consider a word as possible key if it it or the next word is not followed by another noun.
        for (int i = 0; i < sentence.getWords().size()-2; i++) {
            Word word = sentence.getWords().get(i);
            Word nextWord = sentence.getWords().get(i+1);
            Word afterNextWord = sentence.getWords().get(i+2);
            
            //Save last article!
            if (word.getTag().equals("DET")) {
                article = word.getContent();
            }
            
            if (GrammarHelper.isNoun(word) && !GrammarHelper.isNoun(nextWord) && !GrammarHelper.isNoun(afterNextWord) ) {   
                //Check if not exclusion word
                if (exclusions.contains(word.getContent())){
                    clozeSentence += word.getContent() + " ";
                    continue;
                }
                
                //Add word to the sentence if it is not unique
                if (!isUnique(word)) {
                    clozeSentence += word.getContent() + " ";
                    continue;
                }

                //Create key object
                Key key = new Key(word);
                key.setArticle(article);
                article = "";
                
                //Find distractors for the key
                if (retrieveDistractors) {
                    List<Distractor> distractors = DistractorGenerator.getDistractors(sentence, key, numberOfDistractors);
                    if (distractors == null || distractors.size() == 0) {
                        clozeSentence += word.getContent() + " ";
                        continue;
                    }
                    key.setDistractors(distractors);
                }                

                //Set key information
                key.setKeyNumber(identifier);
                keys.add(key);
                if (!previousKeys.contains(key)) {
                    previousKeys.add(key);
                }
//                logKeyAndDistractor(key);
                    
                //Create cloze sentence
                clozeSentence += "___" + identifier + "___ ";
                identifier++;
            }
            //If word is no key, add the word to both sentence types
            else {
                clozeSentence += word.getContent() + " ";
            }
        }
        
        return GapHelper.correctAndCreateCloze(sentence, clozeSentence, keys);
    }
    
    private boolean isUnique(Word word){
        for (Key previousKey : previousKeys) {
            String previousKeyString = previousKey.getKeyWord().getContent();
            String currentKey = word.getContent();
            if (previousKeyString.equals(currentKey)) {
                return false;
            }
        }
      
        return true;
    }
    
    private void logKeyAndDistractor(Key key){
        String loggerOutput = key.getKeyWord().getContent() + " (";
        for (Distractor distractor : key.getDistractors()) {
            loggerOutput += distractor.getDistractorWord().getContent() +"(" + distractor.getSimilarity() + "), ";
        }
        logger.log(Level.INFO, loggerOutput.substring(0, loggerOutput.length()-2) + ")");
    }
    
    private List<Key> removeGaps(String cloze, List<Key> keys){
        int numberOfGaps = 0;
      //Check if their is a previous key in the sentence we need to correct
        final Pattern gapPattern = Pattern.compile("___[0-9]+___");
        final Matcher gapMatcher = gapPattern.matcher(cloze);
        while (gapMatcher.find()) {
            numberOfGaps++;
            identifier--;
        }
            
        //Remove previous key(s) found in the sentence we need to correct
        if (numberOfGaps > 0) {
            List<Key> correctedKeys = new ArrayList<>();
            for (int j = 0; j < keys.size()-numberOfGaps; j++) {
                correctedKeys.add(keys.get(j));
            }
            return correctedKeys;
        }
        
        return keys;
    }

}
