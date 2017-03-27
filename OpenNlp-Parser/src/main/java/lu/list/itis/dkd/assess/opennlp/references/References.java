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

import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class References {
    protected static final Logger logger = Logger.getLogger(References.class.getName());
    
    /**
     * @param sentence: A sentence object
     * @return True if the word is a reference or false otherwise.
     */
    public static boolean isReference(Word word) {
        switch (word.getLanguage()) {
            case DE:
                logger.log(Level.WARNING, "References in German are detected more accurately by using a sentence instead of a word.");
                return GermanReferences.isReference(word);
            case FR:
                return FrenchReferences.isReference(word);
            case EN:
                return EnglishReferences.isReference(word);
            default: 
                return false;
        }
    }
    
    /**
     * @param sentence: A sentence object
     * @return The number of References is a sentence.
     */
    public static int getNumberOfReferences(Sentence sentence) {
        switch (sentence.getLanguage()) {
            case DE:
                return GermanReferences.getNumberOfReferences(sentence);
            case FR:
                return FrenchReferences.getNumberOfReferences(sentence);
            case EN:
                return EnglishReferences.getNumberOfReferences(sentence);
            default: 
                return 0;
        }
    }
}
