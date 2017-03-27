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
package lu.list.itis.dkd.assess.opennlp.references;

import java.util.Arrays;
import java.util.List;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class FrenchReferences {
    private static List<String> REFERENCES = Arrays.asList(
         "il", "elle", "on", "se", "s’", "le", "la", "l’", "en", "lui", "y", "soi", "ils", "elles", "lui-même",
         "elle-même", "eux-mêmes", "elles-mêmes", "les", "leur", "eux", "cela", "ce", "ç'", "ça", "celui",
         "celle", "ceux", "celles", "celui-là", "celle-là", "ceux-là", "celles-là", "ceci", "celui-ci", 
         "celle-ci", "ceux-ci", "celles-ci", "ici", "là", "là-bas", "ici-bas", "là-haut", "où", "qui", "lequel", 
         "laquelle", "lesquels", "lesquelles", "que", "à qui", "à quoi", "auquel", "à laquelle", "auxquels", 
         "auxquelles", "de qui", "dont", "de quoi", "duquel", "de laquelle", "desquels", "desquelles", "le mien",
         "la mienne", "les miens", "les miennes", "le tien", "la tienne", "les tiens", "les tiennes", "le sien", 
         "la sienne", "les siens", "les siennes", "le nôtre", "la nôtre", "les nôtres", "le vôtre", "la vôtre", 
         "les vôtres", "le leur", "la leur", "les leurs", "l’un", "les uns", "un autre", "d’autres", "l’autre", 
         "les autres"
    );
    
    static int getNumberOfReferences(Sentence sentence) {
        int number = 0;
        for (Word word : sentence.getWords()) {
            if (isReference(word)) {
                number++;
            }
        }
        return number;
    }
    
    static boolean isReference(Word word) {
        if (REFERENCES.contains(word.getContent().toLowerCase())) {
            return true;
        }
        return false;
        
    }
}
