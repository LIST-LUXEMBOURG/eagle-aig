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
package lu.list.itis.dkd.assess.opennlp.connectives;

import java.util.Map;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.util.Type;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Connectives {
    
    /**
     * @param sentence
     * @return A map containting the connective type and the connective itself or null otherwise.
     */
    public static Map<Type.Connective, String> getConnectives(Sentence sentence) {
        return getConnectives(sentence.getContent(), sentence.getLanguage());
    }

    /**
     * @param sentence
     * @param language
     * @return A map containting the connective type and the connective itself or null otherwise.
     */
	public static Map<Type.Connective, String> getConnectives(String sentence, Language language) {
		switch (language) {
            case DE:
                return GermanConnectives.getConnectives(sentence);
            case FR:
                return FrenchConnectives.getConnectives(sentence);
            case EN:
                return EnglishConnectives.getConnectives(sentence);
            default:
                return null;
        }
	}
	
}
