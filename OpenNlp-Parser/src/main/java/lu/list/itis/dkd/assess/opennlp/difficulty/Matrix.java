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
import lu.list.itis.dkd.assess.opennlp.util.Type.DifficultyLevel;

import java.text.DecimalFormat;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class Matrix {
    private Difficulties difficulties;
    
    public Matrix(Text text) {
        difficulties = new Difficulties(text.getLanguage()); 
        switch (text.getLanguage()) {
            case DE:
                difficulties.addDifficulty(GermanMatrixes.lix(text));
                difficulties.addDifficulty(GermanMatrixes.amstad(text));
                difficulties.addDifficulty(GermanMatrixes.dictionaryA2(text));
//                difficulties.addDifficulty(dictionaryA1(rootPathResources + goetheg_Word_List_A1));
                difficulties.addDifficulty(GermanMatrixes.averageSentenceLength(text));
                difficulties.addDifficulty(GermanMatrixes.averageWordLength(text));
                difficulties.addDifficulty(GermanMatrixes.numberOfSyllables(text));
                difficulties.addDifficulty(GermanMatrixes.numberOfSentences(text)); 
                difficulties.addDifficulty(GermanMatrixes.numberOfPropositions(text)); 
                difficulties.addDifficulty(GermanMatrixes.numberOfReferences(text)); 
                break;
            case FR:
                difficulties.addDifficulty(FrenchMatrixes.kandel(text));
                difficulties.addDifficulty(FrenchMatrixes.lix(text));
//                difficulties.addDifficulty(FrenchMatrixes.dictionaryAlterego(text));
                difficulties.addDifficulty(FrenchMatrixes.dictionaryGoug(text));
                break;
            case EN:
                difficulties.addDifficulty(EnglishMatrixes.fleschKincaid(text));
                difficulties.addDifficulty(EnglishMatrixes.daleChall(text));
//                difficulties.addDifficulty(EnglishMatrixes.dictionaryCoca(text));
                difficulties.addDifficulty(EnglishMatrixes.readabilityEase(text));
                break;
        }     
    }
    
    public String getAnalyses() {
        String output = "";
        DecimalFormat df = new DecimalFormat("#.##");
        
        for (Difficulty difficulty : difficulties.getDifficulties()) {
            output += difficulty.getType() + ": " + difficulty.getRating() + "(" + df.format(difficulty.getValue()) + ")\n"; 
        }
        
        return "\n" + output + "\n\nFinal Text difficulty: " + difficulties.getDifficulty();
    }
    
    public DifficultyLevel getDifficulty(){
        return difficulties.getDifficulty();
    }
}
