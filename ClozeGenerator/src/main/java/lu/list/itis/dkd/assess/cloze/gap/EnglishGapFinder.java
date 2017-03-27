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
import lu.list.itis.dkd.assess.cloze.util.KodaAnnotation;
import lu.list.itis.dkd.assess.cloze.util.GapHelper;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class EnglishGapFinder {
    private int numberOfDistractors = 3;
    private int identifier = 1;
    private List<Key> previousKeys = new ArrayList<>();
    private List<String> exclusions = new ArrayList<>();
    
    protected static final Logger logger = Logger.getLogger(EnglishGapFinder.class.getSimpleName());
    
    public EnglishGapFinder(int numberOfDistractors) {
        this.numberOfDistractors = numberOfDistractors;
    }
    
    public EnglishGapFinder() {
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
        List<String> annotations = new ArrayList<>();
        annotations = KodaAnnotation.getAnnotations(sentence);
        if (annotations.size() == 0 || annotations == null) {
            logger.log(Level.INFO, "Koda did not found any results in sentence " + sentence.getSentenceNumber() + ". Hence, another approach based on nouns is used.");
            return findNouns(sentence, retrieveDistractors);
        }        
        
        //Skip last word
        for (int i = 0; i < sentence.getWords().size()-1; i++) {
            Word word = sentence.getWord(i+1);
            
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
//            logKeyAndDistractor(key);
                
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
        Boolean tranformVerb = true;
        
        //Exclude all names and skip last word of the sentence 
        this.exclusions.addAll(GapHelper.excludeNames(sentence));        
        for (int i = 0; i < sentence.getWords().size()-1; i++) {
            Word word = sentence.getWord(i+1);
            
            //Singular or plural phrase
            if (GrammarHelper.isNoun(word)){
                isPlural = GrammarHelper.isPlural(word);
            }
             
           //Handle to
           if (word.getTag().equals("TO")) {
               tranformVerb = false;
           }
                
           //Verb(s) was/were ignored until next noun, use verbs from now on again 
           if (!tranformVerb && GrammarHelper.isNoun(word)) {
               tranformVerb = true;
           }
                
           if (GrammarHelper.isVerb(word)) {  
               //Ignore verb if "to" was in front
               if (!tranformVerb) {
                   clozeSentence += word.getContent() + " ";
                   continue;
               }
                    
               //Check if not exclusion word
               if (exclusions.contains(word.getContent())){
                   clozeSentence += word.getContent() + " ";
                   continue;
               }
                    
               //Check for unique key
               if (!isUnique(word)) {
                   clozeSentence += word.getContent() + " ";
                   continue;
               }
               
               //Create key object
               Key key = null;
               List<Distractor> distractors = new ArrayList<>(); 
                    
               //Handle present and past participle
               if (word.getTag().equals("VBG") || word.getTag().equals("VBN")) {
                   int MDindex = GapHelper.wordIndex(sentence, word, "MD");
                   int wordNumber = word.getSentenceWordNumber();
                        
                   //Create key and correct sentence
                   if (MDindex != 0) {
                       String keyString = "";
                       String indexedWord = sentence.getWord(MDindex).getContent();
                       clozeSentence = clozeSentence.replace(indexedWord + " ", "**");
                            
                       for (int j = MDindex; j < wordNumber; j++) {
                           Word token = sentence.getWord(j+1);                                
                           keyString += token.getContent() + " ";
                       }
                            
                       //Correct sentence
                       if (clozeSentence.contains("**")) {
                           int beginning = clozeSentence.indexOf("**");
                           String clozeSentencePart = clozeSentence.substring(beginning, clozeSentence.length());
                           keys = removeGaps(clozeSentencePart, keys);                     
                           clozeSentence = clozeSentence.substring(0, clozeSentence.indexOf("**"));
                                
                           //Create key
                           Word tenseKeyWord = new Word(keyString, MDindex, Language.EN);
                           key = new Key(tenseKeyWord);
                           key.setPlural(isPlural);
                           if (retrieveDistractors){
                               distractors = VerbGenerator.getTenses(key);
                           }
                                
                           Word keyWord = new Word(indexedWord + " " + tenseKeyWord.getContent(), MDindex, Language.EN);
                           key.setKeyWord(keyWord);
                       }   
                   }
                   else {                            
                       //Find noun  
                       int nounIndex = 0;
                       for (int j = wordNumber; j > 0; j--) {    
                           Word token = sentence.getWord(j+1);
                           if (GrammarHelper.isNoun(token)) {
                               nounIndex = token.getSentenceWordNumber();
                               break;
                           }
                       }

                       //Find pre verb  
                       int preVerbIndex = 0;
                       String preVerb = "";
                       for (int j = wordNumber; j > 0; j--) {
                           //Verb is not a preverb
                           if (j < nounIndex) {
                               break;
                           }
                              
                           //Save index and pre verb
                           Word token = sentence.getWord(j+1);
                           if (token.getTag().equals("VB") || token.getTag().equals("VBD")) {
                               preVerbIndex = token.getSentenceWordNumber();
                               preVerb = token.getContent();
                               break;
                           }
                       }
                            
                       if (preVerbIndex == 0) {
                           continue;  
                       }

                       //Build sentence part
                       String sentencePart = "";
                       for (int j = nounIndex; j < wordNumber; j++) {
                           Word token = sentence.getWord(j);
                           sentencePart += token.getContent() + " ";
                       }

                       //Correct cloze sentence if needed
                       int clozeSentenceNounIndex = clozeSentence.indexOf(sentence.getWord(nounIndex) + " ");
                       String clozeSentencePart = clozeSentence.substring(clozeSentenceNounIndex, clozeSentence.length());
                            
                       keys = removeGaps(clozeSentencePart, keys);
                       clozeSentence = clozeSentence.replace(clozeSentencePart, sentencePart);                            

                       String keyString = "";
                       clozeSentence = clozeSentence.replace(preVerb + " ", "**");
                            
                       for (int j = preVerbIndex; j < wordNumber; j++) {
                           Word token = sentence.getWord(j+1);                                
                           keyString += token.getContent() + " ";
                       }
                            
                       int beginning = clozeSentence.indexOf("**");
                       clozeSentencePart = clozeSentence.substring(beginning, clozeSentence.length());
                            
                       //Correct sentence
                       clozeSentence = clozeSentence.substring(0, clozeSentence.indexOf("**"));

                       //Create key
                       Word tenseKeyWord = new Word(preVerb + " " + keyString, preVerbIndex, Language.EN);
                       key = new Key(tenseKeyWord);
                       key.setPlural(isPlural);
                       if (retrieveDistractors){
                           distractors = VerbGenerator.getTenses(key);
                       }
                   }
               }
               //Handle other verb tenses
               else if (!word.getContent().toLowerCase().equals("be")){
                   int wordNumber = word.getSentenceWordNumber();
                   int MDindex = GapHelper.wordIndex(sentence, word, "MD");
                   if (MDindex != 0) {
                       String keyString = "";
                       String indexedWord = sentence.getWord(MDindex).getContent();
                       clozeSentence = clozeSentence.replace(indexedWord + " ", "**");
                            
                       for (int j = MDindex; j < wordNumber; j++) {
                           Word token = sentence.getWord(j+1);                                
                           keyString += token.getContent() + " ";
                       }
                            
                       //Correct sentence
                       if (clozeSentence.contains("**")) {
                           int beginning = clozeSentence.indexOf("**");
                           String clozeSentencePart = clozeSentence.substring(beginning, clozeSentence.length());
                           keys = removeGaps(clozeSentencePart, keys);                     
                           clozeSentence = clozeSentence.substring(0, clozeSentence.indexOf("**"));
                           
                           //Create key
                           Word tenseKeyWord = new Word(keyString, MDindex, Language.EN);
                           key = new Key(tenseKeyWord);
                           key.setPlural(isPlural);
                           if (retrieveDistractors){
                               distractors = VerbGenerator.getTenses(key);
                           }
                                
                           Word keyWord = new Word(indexedWord + " " + tenseKeyWord.getContent(), MDindex, Language.EN);
                           key.setKeyWord(keyWord);
                       }
                   }
                   else {
                       key = new Key(word);
                       key.setPlural(isPlural);
                       if (retrieveDistractors){
                           distractors = VerbGenerator.getTenses(key);
                       }
                   }
               }
               
               //Get verbs in another tense for the key
               if ((distractors == null || distractors.size() == 0) && retrieveDistractors) {
                   clozeSentence += word.getContent() + " ";
                   continue;
               }     
               
               //Continue if key is not good
               if (key == null && !retrieveDistractors) {
                   clozeSentence += word.getContent() + " ";
                   continue;
               }

               //Set key information
               System.out.println("Key " + identifier + ": " + key.getKeyWord().getContent());
               
               key.setKeyNumber(identifier);
               if (retrieveDistractors){
                   key.setDistractors(distractors);
               }
               keys.add(key);
               if (!previousKeys.contains(key)) {
                   previousKeys.add(key);
               }
//               logKeyAndDistractor(key);
                        
               //Create cloze sentence
               clozeSentence += "___" + identifier + "___ ";
               identifier++;
           }
           //If word is no key, add the word to both sentence types
           else {
               clozeSentence += word.getContent() + " ";
            }
        }
        
        return GapHelper.createClozeSentence(sentence, clozeSentence, keys);
    }
    
    /**
     * Returns a gap sentence for the sentence if one or more key(s) were fount based on nouns.
     * @param sentence
     * @param retrieveDistractors: If true, distractors are retrieved for the key(s).
     * @return gap sentence
     */
    public ClozeSentence findNouns(Sentence sentence, boolean retrieveDistractors) {
        List<Key> keys = new ArrayList<>();
        String clozeSentence = "";
        
        this.exclusions.addAll(GapHelper.excludeNames(sentence));      
        //Only consider a word as possible key if it is not followed by another noun.
        for (int i = 0; i < sentence.getWords().size()-1; i++) {
            Word word = sentence.getWord(i+1);
            Word nextWord = sentence.getWord(i+2);
            if ((i + 1) < sentence.getWords().size()) {
                if (GrammarHelper.isNoun(word) && !GrammarHelper.isNoun(nextWord)) {   
                    //Check if not exclusion word
                    if (exclusions.contains(word.getContent())){
                        clozeSentence += word.getContent() + " ";
                        continue;
                    }
                    
                    //Check for unique key
                    if (!isUnique(word)) {
                        clozeSentence += word.getContent() + " ";
                        continue;
                    }

                    //Create key object
                    Key key = new Key(word);

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
//                    logKeyAndDistractor(key);
                        
                    //Create cloze sentence
                    clozeSentence += "___" + identifier + "___ ";
                    identifier++;
                }
                //If word is no key, add the word to both sentence types
                else {
                    clozeSentence += word.getContent() + " ";
                }
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
    
//    private void logKeyAndDistractor(Key key){
//        String loggerOutput = key.getKeyWord().getContent() + " (";
//        for (Distractor distractor : key.getDistractors()) {
//            loggerOutput += distractor.getDistractorWord().getContent() + ", ";
//        }
//        logger.log(Level.INFO, loggerOutput.substring(0, loggerOutput.length()-2) + ")");
//    }
    
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
