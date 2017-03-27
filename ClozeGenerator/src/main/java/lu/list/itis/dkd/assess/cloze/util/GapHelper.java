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
package lu.list.itis.dkd.assess.cloze.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import opennlp.tools.util.Span;

/**
 * @author pfeiffer
 * @since 1.0
 * @version 1.0.0
 */
public class GapHelper {

    /**
     * Returns the word index of a word instance within a sentence instance.
     * @param sentence
     * @param word
     * @param tag
     * @return
     */
    public static int wordIndex(Sentence sentence, Word word, String tag){
        int index = 0;
        int wordNumber = word.getSentenceWordNumber();
        for (int j = wordNumber; j > 0; j--) {
            Word token = sentence.getWord(j+1);
            if (token.getTag().equals(tag)) {    
                index = token.getSentenceWordNumber();
                break;
            }
        }
        
        return index;
    }
    
    /**
     * Creates a
     * @param sentence
     * @param cloze
     * @param keys
     * @return
     * @throws IOException
     */
    public static ClozeSentence createClozeSentence(Sentence sentence, String cloze, List<Key> keys) {
        //Add last word and a point at the end.
        String clozeSentence = cloze.trim();
        Character sentenceEnding = sentence.getContent().charAt(sentence.getContent().length()-1);
        String lastWord = sentence.getWord(sentence.getWords().size()).getContent(); 
        clozeSentence += " " + lastWord + sentenceEnding; 
        
        //Create cloze sentence object and add the key and the qti sentence to it before returning.
        ClozeSentence clozePhrase = new ClozeSentence(sentence, clozeSentence);
        clozePhrase.setKeys(keys);
        return clozePhrase;
    }
    
    //TODO Somehow wrong... Check
    public static ClozeSentence correctAndCreateCloze(Sentence sentence, String cloze, List<Key> keys) {
        //Add last word and a point at the end.
        String clozeSentence = cloze.trim();
        Character sentenceEnding = sentence.getContent().charAt(sentence.getContent().length()-1);
        String lastWord = sentence.getWord(sentence.getWords().size()).getContent(); 
        clozeSentence += " " + lastWord + sentenceEnding; 
        
        //Add punctuation and stuff
        String[] clozeTokens = clozeSentence.split(" ");
        String[] sentenceTokens = sentence.getContent().split(" "); //getTokenArray can not be used because words do not have punctuation information.
        String res = "";      
        
        for (int i = 0; i < clozeTokens.length; i++) {
            String clozeToken = clozeTokens[i];
            String sentenceToken = sentenceTokens[i];
            if (!clozeToken.equals(sentenceToken) && (!clozeToken.contains("__"))) {
                res += sentenceToken + " ";
            }
            else {
                res += clozeToken + " ";
            }
        }
        
        //Create cloze sentence object and add the key and the qti sentence to it before returning.
        ClozeSentence clozePhrase = new ClozeSentence(sentence, res);
        clozePhrase.setKeys(keys);
        return clozePhrase;
    }
    
    /**
     * This functions finds people names, organization names and location names in a sentence object
     * by using the OpenNlp name loader models.
     * @param sentence
     * @return list of exclusions
     */
    public static List<String> excludeNames(Sentence sentence){
        List<String> exclusions = new ArrayList<>();
        
        //Find all exclusions (e.g. names, organizations and locations.
        Span nameSpan[] = NameLoader.getNameModel().find(sentence.getTokenArray());
        Span organizationSpan[] = NameLoader.getOrganizationModel().find(sentence.getTokenArray());
        Span locationSpan[] = NameLoader.getLocationModel().find(sentence.getTokenArray());

        //Form a String out of all exclusions
        String exclusion = Arrays.toString(Span.spansToStrings(nameSpan, sentence.getTokenArray()));
        exclusion += Arrays.toString(Span.spansToStrings(organizationSpan, sentence.getTokenArray()));
        exclusion += Arrays.toString(Span.spansToStrings(locationSpan, sentence.getTokenArray()));
        
        //Return false if no exclusions found. This means, if the String only contains [][][], then it is empty.
        if (exclusion.length() > 6) {
            Pattern exclusionPattern = Pattern.compile("[a-zA-Z]+");
            Matcher exclusionMatcher = exclusionPattern.matcher(exclusion);
        
            while (exclusionMatcher.find()) {
                String exclusionMatch = exclusionMatcher.group(0);
                if (!exclusions.contains(exclusionMatch))
                    exclusions.add(exclusionMatch); 
            }
        }
        
        return exclusions;
    }
}
