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
package lu.list.itis.dkd.assess.opennlp.gui;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.difficulty.Matrix;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Younes Djaghloul [younes.djaghloul@list.lu]
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0
 * @version 4.0.1
 */
public class Complexity {   
    private Text text;  

    /**
     * Simple constructor using a String text and the language of the text to compute the difficulty.
     * @param text
     */
    public Complexity(String text, Language language) {
        this.text = new Text(text, language);
    }
    
    /**
     * Simple constructor using a Text object to compute the difficulty of a text.
     * @param text
     */
    public Complexity(Text text) {
        this.text = text;
    }   
    
    /**
     * Structures the information gathered from the text.
     * @return List of Strings.
     */
    public String getComplexity() {  
        String resultsAnalyse = "";
        resultsAnalyse = resultsAnalyse.concat("Number of sentences: " + text.getNumberOfSentences());
        resultsAnalyse = resultsAnalyse.concat("\nNumber of words: " + text.getNumberOfWords());
        resultsAnalyse = resultsAnalyse.concat("\nNumber of syllables: " + text.getNumberOfSyllables());
        resultsAnalyse = resultsAnalyse.concat("\nNumber of propositions: " + text.getNumberOfPropositions());
        resultsAnalyse = resultsAnalyse.concat("\nAverage Sentence Length: " + Math.round(text.getAverageSentenceLength()));
        resultsAnalyse = resultsAnalyse.concat("\nAverage Word Length: " + Math.round(text.getAverageWordLength()));
        resultsAnalyse = resultsAnalyse.concat("\nAverage #Syllables per word: " + Math.round(text.getAverageSyllablesPerWord()));
        resultsAnalyse = resultsAnalyse.concat("\nAverage #Propositions: " + Math.round(text.getAveragePropositions()));
//        resultsAnalyse = resultsAnalyse.concat("\nType Token Ratio: " + typeTokenRatio());
        
//        resultsAnalyse = resultsAnalyse.concat("\nReferences: " + text.getNumberOfReferences());
//        resultsAnalyse = resultsAnalyse.concat("\nIdentifications: " + text.getNumberOfIdentifications());
//        resultsAnalyse = resultsAnalyse.concat("\nDensity: " + text.getDensity());
        
        Matrix matrix = new Matrix(text);
        resultsAnalyse = resultsAnalyse.concat(matrix.getAnalyses());
        
        return resultsAnalyse;
    }
    
    public String getDifficulty() {
        Matrix matrix = new Matrix(text);
        return matrix.getDifficulty().toString();
    }
}

