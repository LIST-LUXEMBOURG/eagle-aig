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
package lu.list.itis.dkd.assess.opennlp.util;

import lu.list.itis.dkd.assess.opennlp.Paragraph;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class StringHelper {
    protected static final Logger logger = Logger.getLogger(StringHelper.class.getSimpleName());
    
    /**
     * Converts a float value into a String.
     * @param value
     * @return The float value as String.
     */
    public static String convert(Double value) {
        return Double.toString(value); 
    }
    
    /**
     * Converts an integer into a String.
     * @param value
     * @return The Integer value as String.
     */
    public static String convert(int value) {
        return Integer.toString(value); 
    }
    
    /**
     * Breaks a String sentence into words.
     * @param sentence
     * @param language
     * @return A list of word objects.
     */
    public static List<Word> breakSentenceIntoWordsAndTags(String sentence, Language language) {
        List<Word> words = new ArrayList<>();
        String temp = StringHelper.replaceNonWordSymbols(sentence);        
        String[] tokens = temp.split(" ");
        String[] tags = tagSentence(tokens, language);
        
        int wordPos = 1;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            String tag = tags[i];
            if (token.equals("")) {
                continue;
            }
            Word word = new Word(token, tag, wordPos, language);
            words.add(word);
            wordPos++;
        }
        
        return words;
    }
    
    /**
     * @param plainText
     * @return The String where all mutli spaces were replaced by a single spaces.
     */
    public static String replaceMultiSpaces(String plainText){
        Pattern spacePattern = Pattern.compile("(\\s\\s+)");
        Matcher spaceMatcher = spacePattern.matcher(plainText);

        String temp = plainText;
        while (spaceMatcher.find()) {
            // Replace multi space by a simple space.
            String spaceMatch = spaceMatcher.group(1);
            temp = temp.replace(spaceMatch, " ").trim();
        }
        
        return temp;
    }
    
    /**
     * Breaks a text into its paragraphs. A paragraph end is considered by "\n".
     * @param plainText
     * @param language
     * @return A list of paragraph objects.
     */
    public static List<Paragraph> breakTextIntoParagraphs(String plainText, Language language) {
        List<Paragraph> paragraphs = new ArrayList<>();
        Pattern paragraphPattern1 = Pattern.compile("(.*)[\r\n]+");
        Matcher paragraphMatcher1 = paragraphPattern1.matcher(plainText);

        String temp = plainText;
        int i = 1;
        while (paragraphMatcher1.find()) {
            String match = paragraphMatcher1.group(1).trim();
            Paragraph paragraph = new Paragraph(match, i, language);
            temp = temp.replace(match, "");
            paragraphs.add(paragraph);
            i++;
        }

        if (paragraphs.isEmpty()) {
            logger.log(Level.INFO, "Paragraphs could not be found. The entire Text is considered as one paragraph. If this is wrong, check your plain text entry. Paragraphs in plain text documents are usually separated by two or more line separators. A line separator may be a linefeed (\n), a carriage-return (\r), or a carriage-return followed by a linefeed (\r\n).");
            Paragraph paragraph = new Paragraph(plainText, 1, language);
            paragraphs.add(paragraph);
        }

        return paragraphs;
    }
    
    /**
     * Breaks a text into sentences.
     * @param plainText
     * @param language
     * @return A list of sentence objects.
     */
    public static List<Sentence> breakTextIntoSentences(String plainText, Language language) {
        List<Sentence> sentences = new ArrayList<>();
        
        SentenceDetectorME sentenceTagger = ModelLoader.getSentenceModel(language);
        String[] phrases = sentenceTagger.sentDetect(plainText);
        
        int sentencePos = 1;
        for (String phrase : phrases) {            
            String[] words = phrase.split(" ");
            
            //Add sentence part to the previous sentence
            if (words.length < 3 && sentencePos > 1) {
                //Extent previous sentence object
                String previousSentence = sentences.get(sentencePos-2).getContent() + phrase;
                sentences.get(sentencePos-2).setContent(previousSentence);
                continue;
            }
            
            if (words.length > 2) { 
                Sentence sentence = new Sentence(phrase.trim(), sentencePos, language);
                sentences.add(sentence);
                sentencePos++;
            }
        }

        return sentences;
    }
    
    /**
     * @param plainText
     * @return The String without non word symbols. E.g. parantheses, sentence endings, ...
     */
    public static String replaceNonWordSymbols(String plainText) {
        String token = plainText;
        token = token.replace("”", "");
        token = token.replace("“", "");
        token = token.replace("(", "");
        token = token.replace(")", "");
        token = token.replace("[", "");
        token = token.replace("]", "");
        token = token.replace("{", "");
        token = token.replace("}", "");
        token = token.replace(":", "");
        token = token.replace(";", "");
        token = token.replace(",", "");
        token = token.replace(".", "");
        token = token.replace("!", "");
        token = token.replace("?", "");
        return token;
    }

    /**
     * @param unsortMap
     * @return A sorted map by its comparator.
     */
    public static Map<Word, Integer> sortMapByComparator(Map<Word, Integer> unsortMap) {

        // Convert Map to List
        List<Map.Entry<Word, Integer>> list =
                        new LinkedList<Map.Entry<Word, Integer>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

        // Convert sorted map back to a Map
        Map<Word, Integer> sortedMap = new LinkedHashMap<Word, Integer>();
        for (Iterator<Map.Entry<Word, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Word, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
    
    private static String[] tagSentence(String[] word, Language language) {
        POSTaggerME posTagger = ModelLoader.getPosModel(language);
        return posTagger.tag(word);
    }

}
