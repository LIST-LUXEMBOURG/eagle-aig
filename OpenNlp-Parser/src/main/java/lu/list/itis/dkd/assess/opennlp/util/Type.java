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

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Type {
    public static enum Language {
        EN, DE, FR
    }

    public static enum Font {
        DEFAULT, UNDERLINE, BOLD, ITALIC, ITALIC_BOLD, ITALIC_UNDERLINE, ITALIC_BOLD_UNDERLINE, BOLD_UNDERLINE
    }

    public static enum Level {
        I, II, III, IIII
    }
    
    public static enum List {
        DEFAULT, ORDERED, UNORDERED, DEFINITION, ORDEREDDEFINITION, UNORDEREDDEFINITION
    }    
    
    //TODO Connective Type enum!
    public static enum Connective {
        CONTRAST, SIMILARITY, RESULT, EXAMPLIFICATION, LINKING, CAUSALITY,
        SEQUENCE, ORDER, PARTICULATION, EXPLANATION, EMPHASISING, CONCLUSION,
        CORRECTION, TIME, DISMISSIAL
    }
    
    public static enum Content{
        Text, HTML, JSON
    }
    
    public static enum DifficultyLevel{
        EASY, MEDIUMEASY, MEDIUM, MEDIUMHARD, HARD
    }
}
