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
package lu.list.itis.dkd.assess.cloze.gap.approach;

import java.util.ArrayList;
import java.util.List;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Font;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Definition {

    /**
     * Returns a list of cloze sentences if the enough font informations are available.  
     * @param sentences: A list of sentences which have font informations (e.g. bold).
     * @param numberOfDistractors
     * @param language
     * @return
     */
    public static List<ClozeSentence> getClozeSentences(List<Sentence> sentences, int numberOfDistractors, Language language) {
        List<ClozeSentence> definitionSentences = new ArrayList<>();
        List<Word> fontWords = new ArrayList<>();
        List<String> distractors = new ArrayList<>();
            
        for (Sentence sentence : sentences) {
            fontWords.addAll(sentence.getFontWords(Font.UNDERLINE));
        }
            
        if (fontWords.isEmpty()) {
            return definitionSentences;
        }
        
        fontWords.clear();
        int gapIdentifier = 1;
        for (Sentence sentence : sentences) {
            language = sentence.getLanguage();
            String distractor = "";
            if (!sentence.getFontWords(Font.UNDERLINE).isEmpty()) {
                fontWords = sentence.getFontWords(Font.UNDERLINE);
            }
            List<Key> keys = new ArrayList<>();
                    
            //Get the not underlined part of the sentence.
            String phrase = sentence.getSentence(sentence.getFontWords(Font.DEFAULT));
            if (!phrase.equals("")) {                    
                char firstLetter = Character.toUpperCase(phrase.charAt(0)); //Save first letter
                        
                //Replace the words in the sentence which are a hint to the answer.
                for (Word fontWord : fontWords){
                    distractor += fontWord.getContent() + " ";
                            
                    String definitionWord = fontWord.getContent().toLowerCase();
                    phrase = phrase.toLowerCase();
                    if (phrase.contains(definitionWord)) {
                        phrase = phrase.replace(definitionWord, "**");
                    }
                }
                        
                //Add distractor to the list if not already in.
                distractor = distractor.trim();
                if (!distractors.contains(distractor) && !distractor.equals("") ) {
                    distractors.add(distractor);
                }
                        
                //Replace the hints by "..." and uppercase first letter if needed.
                phrase = phrase.replaceAll("\\*.*\\*", "___" + gapIdentifier + "___");
                    
                //Uppercase First letter if needed
                if (phrase.charAt(0) != '_') {
                    phrase = phrase.substring(1, phrase.length());
                    phrase = (firstLetter + phrase).trim();
                }
                        
                //If the resulting sentence is too short, discard it.
                String[] numberOfWords = phrase.split(" ");
                if (numberOfWords.length <= 5) {
                    continue;
                }
                
                //Build key
                String key = "";
                for (Word word : fontWords) {
                    key += word.getContent() + " ";
                }
                        
                //Create cloze definition sentence
                if (!key.equals("")) {
                    key = key.trim();
                    Word word = new Word(key, 0, language);
                    Key keyword = new Key(word);
                    keyword.setKeyNumber(gapIdentifier);
                    keys.add(keyword);    
                }
            }
            gapIdentifier++;
                
            if (keys != null && keys.size() > 0) {
                ClozeSentence definition = new ClozeSentence(sentence, phrase, keys);
                definitionSentences.add(definition);
            }
        }
            
        //Add distractors to all keys
        List<Distractor> definitionDistractors = new ArrayList<>();
        for (ClozeSentence clozeSentence : definitionSentences) {
            for (Key key : clozeSentence.getKeys()) {
                for (String distractorWord : distractors) {
                    if (!distractorWord.equals(key.getKeyWord().getContent())) {
                        Word word = new Word(distractorWord, 0, language);
                        Distractor distractor = new Distractor(word, 1);
                        definitionDistractors.add(distractor);
                    }
                }
                key.setDistractors(definitionDistractors);
            }
        }

        return definitionSentences;
    }
}
