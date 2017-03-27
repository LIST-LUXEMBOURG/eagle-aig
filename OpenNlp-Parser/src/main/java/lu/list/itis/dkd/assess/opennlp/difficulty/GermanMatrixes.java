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
package lu.list.itis.dkd.assess.opennlp.difficulty;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.DictionaryHelper;
import lu.list.itis.dkd.assess.opennlp.util.NLPPropertiesFetcher;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

import java.util.Properties;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class GermanMatrixes {
    final static Properties properties = NLPPropertiesFetcher.fetchProperties();
    
    /**
    * The Lix formula, developed by Bjson from Sweden, is very simple and uses a mapping table for
    * its scores. It is useful for documents of Western European languages. It has been successfully 
    * used on, English, German, French, Greek and Sweedish. The score is based on sentence length 
    * and the number of long words (long words are words over six characters).
    * 0-24    Very easy
    * 25-34   Easy
    * 35-44   Standard
    * 45-54   Difficult
    * 55+     Very difficult
    * @return (words / sentences) + 100 (words >= 7 characters / words)
    */
    static Difficulty lix(Text text) {
        int wordGreater7 = 0;
        
        for (String word : text.getTokenizedText()) {
            if (word.length() >= 7) {
                wordGreater7++;
            }
        }
        
        double difficultyLevel = text.getAverageSentenceLength() + 100 * (wordGreater7 / text.getNumberOfWords());
        
        if (difficultyLevel < 34.0) {
            return new Difficulty("lix", "easy", difficultyLevel, text.getLanguage());
        }
        else if (difficultyLevel >= 34.0 && difficultyLevel < 44.0) {
            return new Difficulty("lix", "medium", difficultyLevel, text.getLanguage());
        }

        return new Difficulty("lix", "hard", difficultyLevel, text.getLanguage());
    }
    
    /**
    * Computes the difficulty level by the Flesch reading Ease formula where 0 is very difficult and 100 very easy.
    * 100+  Very Easy
    * 80-100 Easy
    * 60-80  Medium
    * 30-60  Difficult
    * 0-30   Very Difficult
    * @param com
    * @return Flesch, Amstad or Kanel & Moles depening on the language
    */
    static Difficulty amstad(Text text) {      
        double difficultyLevel = 180 - text.getAverageSentenceLength() - 58.5 * text.getAverageSyllablesPerWord();
     
        double a1 = Math.abs((difficultyLevel - 80.5) / 8.54);
        double a2 = Math.abs((difficultyLevel - 68.98) / 13.36);
        double b1 = Math.abs((difficultyLevel - 58.88) / 9.5);
        double b2 = Math.abs((difficultyLevel - 52.77) / 7.1);
        double c1 = Math.abs((difficultyLevel - 24.986) / 11.17);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= b2 && a1 <= c1) {
            return new Difficulty("amstad", "easy", difficultyLevel, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= b2 && a2 <= c1) {
            return new Difficulty("amstad", "easy", difficultyLevel, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= b2 && b1 <= c1){
            return new Difficulty("amstad", "medium", difficultyLevel, text.getLanguage());
        }
        else if (b2 <= a1 && b2 <= a2 && b2 <= b1 && b2 <= c1) {
            return new Difficulty("amstad", "medium", difficultyLevel, text.getLanguage());
        }
        
        return new Difficulty("amstad", "hard", 0.0, text.getLanguage());
    }
    
//    public Difficulty dictionaryA1(String path) throws IOException {
//          DictionaryLoader dictionaryLoader = new DictionaryLoader();
//          dictionaryLoader.load(path);
//          double difficultyLevel = dictionaryLoader.countNumberOfOccurances(text) / text.getNumberOfWords() * 100;
//          
//          if (difficultyLevel < 47) {
//              return new Difficulty("DictionaryA1", "hard", difficultyLevel);
//          }
//          
//          double a1 = Math.abs((difficultyLevel - 62) / 9.7);
//          double a2 = Math.abs((difficultyLevel - 57) / 8.6);
//          double b1 = Math.abs((difficultyLevel - 51) / 7.1);
//          double b2 = Math.abs((difficultyLevel - 47.5) / 4.6);
//
//          if (a1 <= a2 && a1 <= b1 && a1 <= b2) {
//              return new Difficulty("DictionaryA1", "easy", difficultyLevel);
//          }
//          else if (a2 <= a1 && a2 <= b1 && a2 <= b2) {
//              return new Difficulty("DictionaryA1", "easy", difficultyLevel);
//          }
//          else if (b1 <= a1 && b1 <= a2 && b1 <= b2)
//              return new Difficulty("DictionaryA1", "medium", difficultyLevel);
//          else if (b2 <= a1 && b2 <= a2 && b2 <= b1){
//              return new Difficulty("DictionaryA1", "medium", difficultyLevel);
//          }
//          
//          return new Difficulty("DictionaryA1", "hard", difficultyLevel);
//    }
    
    /**
     * Evaluates the difficulty of the text by the mean, standard deviation and percentage of common words in the text.
     * Mean A1: 68.13
     * Mean A2: 64.0
     * Mean B1: 57.28
     * Mean B2: 53.48
     * Standard Deviation A1: 10.2
     * Standard Deviation A2: 10.05
     * Standard Deviation B1: 8.24
     * Standard Deviation B2: 5.26
     * @return (mean * #words) / standard deviation
     * @throws IOException 
     */
    static Difficulty dictionaryA2(Text text) {
		double difficultyLevel = DictionaryHelper.countDictionaryOccurances(text,
				Wrapper.class.getResourceAsStream(properties.getProperty("dictionary.german.a2"))) / text.getNumberOfWords() * 100;
        
        if (difficultyLevel < 54) {
            return new Difficulty("DictionaryA2", "hard", difficultyLevel, text.getLanguage());
        }
        
        double a1 = Math.abs((difficultyLevel - 68.13) / 10.2);
        double a2 = Math.abs((difficultyLevel - 64.0) / 10.05);
        double b1 = Math.abs((difficultyLevel - 57.28) / 8.24);
        double b2 = Math.abs((difficultyLevel - 53.48) / 5.26);
        double c1 = Math.abs((difficultyLevel - 49.467) / 1.67);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= b2 && a1 <= c1) {
            return new Difficulty("DictionaryA2", "easy", difficultyLevel, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= b2 && a2 <= c1) {
            return new Difficulty("DictionaryA2", "easy", difficultyLevel, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= b2 && b1 <= c1){
            return new Difficulty("DictionaryA2", "medium", difficultyLevel, text.getLanguage());
        }
        else if (b2 <= a1 && b2 <= a2 && b2 <= b1 && b2 <= c1) {
            return new Difficulty("DictionaryA2", "medium", difficultyLevel, text.getLanguage());
        }
        return new Difficulty("DictionaryA2", "hard", difficultyLevel, text.getLanguage());
    }
    
    /**
     * Evaluates the difficulty of the text by the mean, standard deviation and percentage of the average sentence length.
     * Mean A1: 8.2
     * Mean A2: 11.8
     * Mean B1: 14.1
     * Mean B2: 17.34
     * Standard Deviation A1: 2.6
     * Standard Deviation A2: 3.85
     * Standard Deviation B1: 2.63
     * Standard Deviation B2: 2.97
     * @return (mean * average sentence length) / standard deviation
     * @throws IOException 
     */
    static Difficulty averageSentenceLength(Text text){
        double averageSentenceLength = text.getAverageSentenceLength();
        
        //Average sentence length is too short to use the formula. 
        if (averageSentenceLength < 8) {
            return new Difficulty("avgSentenceLength", "easy", averageSentenceLength, text.getLanguage());
        }
        
        double a1 = Math.abs((averageSentenceLength - 8.2) / 2.6);
        double a2 = Math.abs((averageSentenceLength - 11.8) / 3.85);
        double b1 = Math.abs((averageSentenceLength - 14.1) / 2.63);
        double b2 = Math.abs((averageSentenceLength - 17.34) / 2.97);
        double c1 = Math.abs((averageSentenceLength - 16.4) / 1.51);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= b2 && a1 <= c1) {
            return new Difficulty("avgSentenceLength", "easy", averageSentenceLength, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= b2 && a2 <= c1) {
            return new Difficulty("avgSentenceLength", "easy", averageSentenceLength, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= b2 && b1 <= c1){
            return new Difficulty("avgSentenceLength", "medium", averageSentenceLength, text.getLanguage());
        }
        else if (b2 <= a1 && b2 <= a2 && b2 <= b1 && b2 <= c1) {
            return new Difficulty("avgSentenceLength", "medium", averageSentenceLength, text.getLanguage());
        }

        return new Difficulty("avgSentenceLength", "hard", averageSentenceLength, text.getLanguage());
    }
    
    /**
     * Evaluates the difficulty of the text by the mean, standard deviation and percentage of the average word length.
     * Mean A1: 4.61
     * Mean A2: 5.12
     * Mean B1: 5.54
     * Mean B2: 5.9
     * Standard Deviation A1: 0.53
     * Standard Deviation A2: 0.46
     * Standard Deviation B1: 0.61
     * Standard Deviation B2: 0.4
     * @return (mean * average word length) / standard deviation
     * @throws IOException 
     */
    static Difficulty averageWordLength(Text text) {
        double averageWordLength = text.getAverageWordLength();
        
        //Average word length to short
        if (averageWordLength < 4) {
            return new Difficulty("avgWordLength", "easy", averageWordLength, text.getLanguage());
        }
        
        double a1 = Math.abs((averageWordLength - 4.61) / 0.53);
        double a2 = Math.abs((averageWordLength - 5.12) / 0.46);
        double b1 = Math.abs((averageWordLength - 5.54) / 0.61);
        double b2 = Math.abs((averageWordLength - 5.9) / 0.4);
        double c1 = Math.abs((averageWordLength - 6.0) / 0.01);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= b2 && a1 <= c1) {
            return new Difficulty("avgWordLength", "easy", averageWordLength, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= b2 && a2 <= c1) {
            return new Difficulty("avgWordLength", "easy", averageWordLength, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= b2 && b1 <= c1){
            return new Difficulty("avgWordLength", "medium", averageWordLength, text.getLanguage());
        }
        else if (b2 <= a1 && b2 <= a2 && b2 <= b1 && b2 <= c1){
            return new Difficulty("avgWordLength", "medium", averageWordLength, text.getLanguage());
        }

        return new Difficulty("avgWordLength", "hard", averageWordLength, text.getLanguage());
    }
   
    
    /**
     * Evaluates the difficulty of the text by the mean, standard deviation and percentage of the number of syllables in the text.
     * Mean A1: 125.3
     * Mean A2: 301.4
     * Mean B1: 492.9
     * Mean B2: 579.9
     * Standart deviation A1: 47
     * Standart deviation A2: 123.5
     * Standart deviation B1: 156.8
     * Standart deviation B2: 313.9
     * @return (mean * #syllables) / standard deviation
     * @throws IOException 
     */
    static Difficulty numberOfSyllables(Text text){
        double numberOfSyllables = text.getNumberOfSyllables();
        
        //Number of Syllables too less to use the formula.
        if (numberOfSyllables < 125) {
            return new Difficulty("numberOfSyllables", "easy", numberOfSyllables, text.getLanguage());
        }
        
        double a1 = Math.abs((numberOfSyllables - 125.3) / 47);
        double a2 = Math.abs((numberOfSyllables - 301.4) / 123.5);
        double b1 = Math.abs((numberOfSyllables - 492.9) / 156.8);
        double b2 = Math.abs((numberOfSyllables - 579.9) / 313.9);
        double c1 = Math.abs((numberOfSyllables - 912.8) / 105.83);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= b2 && a1 <= c1) {
            return new Difficulty("numberOfSyllables", "easy", numberOfSyllables, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= b2 && a2 <= c1) {
            return new Difficulty("numberOfSyllables", "easy", numberOfSyllables, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= b2 && b1 <= c1){
            return new Difficulty("numberOfSyllables", "medium", numberOfSyllables, text.getLanguage());
        }
        else if (b2 <= a1 && b2 <= a2 && b2 <= b1 && b2 <= c1){
            return new Difficulty("numberOfSyllables", "medium", numberOfSyllables, text.getLanguage());
        }

        return new Difficulty("numberOfSyllables", "hard", numberOfSyllables, text.getLanguage());
    }
    
    /**
     * Evaluates the difficulty of the text by the mean, standard deviation and percentage of the number of sentences in the text.
     * Mean A1: 10
     * Mean A2: 15.9
     * Mean B1: 19.6
     * Mean B2: 17.8
     * Standart deviation A1: 5.4
     * Standart deviation A2: 8.2
     * Standart deviation B1: 7.1
     * Standart deviation B2: 9.5
     * @return (mean * #sentences) / standard deviation
     * @throws IOException 
     */
    static Difficulty numberOfSentences(Text text) {  
        double numberOfSentences = text.getNumberOfSentences();
        
        //Text to short to use the formula.
        if (numberOfSentences < 10) {
            return new Difficulty("numberOfSentences", "easy", numberOfSentences, text.getLanguage());
        }
        
        double a1 = Math.abs((numberOfSentences - 10) / 5.4);
        double a2 = Math.abs((numberOfSentences - 15.9) / 8.2);
        double b1 = Math.abs((numberOfSentences - 19.6) / 7.1);
        double b2 = Math.abs((numberOfSentences - 17.8) / 9.5);
        double c1 = Math.abs((numberOfSentences - 28.4) / 5.32);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= b2 && a1 <= c1) {
            return new Difficulty("numberOfSentences", "easy", numberOfSentences, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= b2 && a2 <= c1) {
            return new Difficulty("numberOfSentences", "easy", numberOfSentences, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= b2 && b1 <= c1){
            return new Difficulty("numberOfSentences", "medium", numberOfSentences, text.getLanguage());
        }
        else if (b2 <= a1 && b2 <= a2 && b2 <= b1 && b2 <= c1){
            return new Difficulty("numberOfSentences", "medium", numberOfSentences, text.getLanguage());
        }

        return new Difficulty("numberOfSentences", "hard", numberOfSentences, text.getLanguage());
    }
    
    static Difficulty numberOfPropositions(Text text) {
        double numberOfPropositions = text.getNumberOfPropositions();
        
        //Text to short to use the formula.
        if (numberOfPropositions < 44) {
            return new Difficulty("numberOfPropositions", "easy", numberOfPropositions, text.getLanguage());
        }
        
        double a1 = Math.abs((numberOfPropositions - 43.5) / 29.49);
        double a2 = Math.abs((numberOfPropositions - 84.725) / 43.56);
        double b1 = Math.abs((numberOfPropositions - 100.4) / 64.24);
        double c1 = Math.abs((numberOfPropositions - 183.2) / 10.04);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= c1) {
            return new Difficulty("numberOfPropositions", "easy",numberOfPropositions, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= c1){
            return new Difficulty("numberOfPropositions", "easy",numberOfPropositions, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= c1) {
            return new Difficulty("numberOfPropositions", "medium", numberOfPropositions, text.getLanguage());
        }        

        return new Difficulty("numberOfPropositions", "hard", numberOfPropositions, text.getLanguage());
    }
    
    static Difficulty numberOfReferences(Text text) {
        double numberOfReferences = text.getNumberOfReferences();
        
        //Text to short to use the formula.
        if (numberOfReferences < 3) {
            return new Difficulty("numberOfReferences", "easy", numberOfReferences, text.getLanguage());
        }
        
        double a1 = Math.abs((numberOfReferences - 2.84) / 3.51);
        double a2 = Math.abs((numberOfReferences - 5.93) / 4.8);
        double b1 = Math.abs((numberOfReferences - 8.58) / 6.95);
        double c1 = Math.abs((numberOfReferences - 15) / 3.81);
        
        if (a1 <= a2 && a1 <= b1 && a1 <= c1) {
            return new Difficulty("numberOfReferences", "easy", numberOfReferences, text.getLanguage());
        }
        else if (a2 <= a1 && a2 <= b1 && a2 <= c1){
            return new Difficulty("numberOfReferences", "easy", numberOfReferences, text.getLanguage());
        }
        else if (b1 <= a1 && b1 <= a2 && b1 <= c1) {
            return new Difficulty("numberOfReferences", "medium", numberOfReferences, text.getLanguage());
        }

        return new Difficulty("numberOfReferences", "hard", numberOfReferences, text.getLanguage());
    }
    
//    public String getDifficulty() {
//        return difficulties.getDifficulty();    
//    }
//
//    public Difficulties getMatrixDifficulties() {
//        return difficulties;
//    }
//    
//    public String printDifficulties() {
//        String output = "";
//        
//        DecimalFormat df = new DecimalFormat("#.##");
//        
//        for (Difficulty difficulty : difficulties.getDifficulties()) {
//            output += difficulty.getType() + ": " + difficulty.getRating() + "(" + df.format(difficulty.getValue()) + ")\n"; 
//        }
//        
//        return output;
//    }
}
