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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.util.Type.DifficultyLevel;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class Difficulties {
    private List<Difficulty> difficulties = new ArrayList<>();
    private Language language = Language.EN;
    private int easy = 0;
    private int medium = 0;
    private int hard = 0;
    
    protected static final Logger logger = Logger.getLogger(Difficulties.class.getSimpleName());
    
    Difficulties(Language language) {
        this.language = language;
    }
    
    void addDifficulty(Difficulty difficulty) {
        difficulties.add(difficulty);
        
//        System.out.println(difficulty.getType() + ": " + difficulty.getRating());
        
        if (difficulty.getRating().equals("easy")) {
            easy++;
        }
        else if (difficulty.getRating().equals("medium")) {
            medium++;
        }
        else {
            hard++;
        }
    }
    
    DifficultyLevel getDifficulty() {  
        switch (language) {
            case DE:
              //Text is hard if at least 3 matrixes have a hard res
                if (hard >= 6) {
                    return DifficultyLevel.HARD;
                }
                
                //Hard is more than half present
                if (hard >= 4 && easy < 4) {
                    return DifficultyLevel.MEDIUMHARD;
                }
                break;
            default:
                if (easy >= 2) {
                    return DifficultyLevel.EASY;
                }
                
                if (easy < medium) {
                    return DifficultyLevel.MEDIUM;
                }
                
                if (hard >= 2) {
                    return DifficultyLevel.HARD;
                }
                
                //Hard is more than half present
                if (hard >= 2 && medium == 1) {
                    return DifficultyLevel.MEDIUMHARD;
                }
                break;
//            default:
//                if (hard >= 3) {
//                    return DifficultyLevel.HARD;
//                }
//                
//                //Hard is more than half present
//                if (hard >= 2 && easy < 2) {
//                    return DifficultyLevel.MEDIUMHARD;
//                }
//                break;
        }       
        
        //Remove 1 hard for 1 easy otherwise
        double balance = hard;
        for (int i = 0; i < balance; i++) {
            hard--;
            easy--;                
        }

//        System.out.println("Easy: " + easy);
//        System.out.println("Medium: " + medium);
//        System.out.println("Hard: " + hard);
        
        if (easy+1 < medium) {
            return DifficultyLevel.MEDIUM;
        }
        else if (easy == medium || easy+1 == medium || easy == medium+1) {
            return DifficultyLevel.MEDIUMEASY;
        }
        
        return DifficultyLevel.EASY;
    }
    
    List<Difficulty> getDifficulties() {
        return difficulties; 
    }
}
