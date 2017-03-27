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
package lu.list.itis.dkd.assess.opennlp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.StringHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Summary {
    private final Language language;
    private final Text text;
    private final List<Sentence> sentences = new ArrayList<>();

    /**
     * Summarizer constructor generating a summmary out of a text object.
     *
     * @param text
     *        : Text object
     * @param numberOfSentences
     *        : Number of Sentences left at the end.
     * @param excludeCommonWords
     *        : If true, words like "a", "the", "for", .. are excluded by the summary generation
     */
    public Summary(Text text, int numberOfSentences, boolean excludeCommonWords) {
        this.text = text;
        language = text.getLanguage();
        summarize(numberOfSentences, excludeCommonWords);
    }

    private int countWords(final Word word, final List<Word> words) {
        int numberOfOccurences = 0;
        for (final Word token : words) {
            if (word.getContent().toLowerCase().equals(token.getContent().toLowerCase())) {
                numberOfOccurences++;
            }
        }
        return numberOfOccurences;
    }

    private void summarize(int numberOfSentences, boolean excludeCommonWords) {
        final List<Word> mostFrquentWords = new ArrayList<>(wordFrequency(excludeCommonWords));

        for (final Word word : mostFrquentWords) {
            for (final Sentence sentence : text.getSentences()) {
                if (sentence.getContent().toLowerCase().indexOf(word.getContent().toLowerCase()) >= 0) {

                    if (!sentences.contains(sentence)) {
                        sentences.add(sentence);
                        break;
                    }
                }
                if (sentences.size() >= numberOfSentences) {
                    break;
                }
            }
            if (sentences.size() >= numberOfSentences) {
                break;
            }
        }
    }

    private List<Word> wordFrequency(final boolean excludeCommonWords) {
        final List<Word> allWords = new ArrayList<>();
        final List<Word> uniqueWords = new ArrayList<>();

        for (final Sentence sentence : text.getSentences()) {
            for (final Word word : sentence.getWords()) {
                allWords.add(word);
                if (!uniqueWords.contains(word)) {
                    if (excludeCommonWords) {
                        // Exclude words like "to", "a", "and", ...
                        if (GrammarHelper.isAdj(word) || GrammarHelper.isAdv(word) || GrammarHelper.isNoun(word) || GrammarHelper.isVerb(word)) {
                            uniqueWords.add(word);
                        }
                    } else {
                        uniqueWords.add(word);
                    }
                }
            }

        }

        final Map<Word, Integer> results = new HashMap<>();
        for (int i = 0; i < uniqueWords.size(); i++) {
            final Word uniqueWord = uniqueWords.get(i);
            results.put(uniqueWord, countWords(uniqueWord, allWords));
        }

        // Sort
        final Map<Word, Integer> sortedResults = StringHelper.sortMapByComparator(results);

        final List<Word> temp = new ArrayList<>();
        for (final Word keys : sortedResults.keySet()) {
            temp.add(keys);
        }

        final List<Word> orderedKeys = new ArrayList<>();
        for (int i = temp.size() - 1; i >= 0; i--) {
            orderedKeys.add(temp.get(i));
        }

        return orderedKeys;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }
    
    public String getContent() {
        String content = "";
        for (Sentence sentence : sentences) {
            content += sentence.getContent() + " ";
        }
        return content.trim();
    }

    public Language getLanguage() {
        return language;
    }
   
    @Override
    public String toString(){
        return getContent();
    }
}
