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
package lu.list.itis.dkd.assess.opennlp.connectives.questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.QuestionHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class Questions {    
    private List<Question> questions = new ArrayList<>();
    protected static final Logger logger = Logger.getLogger(Text.class.getSimpleName());
    
    public Questions(Text text) {    
        switch (text.getLanguage()) {
            case EN:
                generateQuestion(text);                
                break;
            case DE:
                logger.log(Level.WARNING, "This function is not yet implemented in the current version.");
                break;
            case FR: 
                logger.log(Level.WARNING, "This function is not yet implemented in the current version.");
                break;
            default:
                break;
        }
    }
    
    private void generateQuestion(Text text){
        for (Sentence sentence : text.getSentences()) {
            Map<Type.Connective, String> sentenceConnectives = new HashMap<>(sentence.getConnectiveTypes());            
            for (Type.Connective type : sentenceConnectives.keySet()) {
                String connective = sentenceConnectives.get(type);
                Question query = null;
//                System.out.println(type + ": " + connective);
                
                switch (type) {
                    case TIME:
//                        System.out.println("Sentence used to form a TIME question: " + sentence);
                        query = timeConnective(text, sentence, connective); 
                        break;
                    case RESULT:
//                        System.out.println("Sentence used to form a RESULT question: " + sentence);
                        if (connective.equals("although")) {
                            query = new Question(sentence, "although", "", 2);
                        }
                        else {
                            query = new Question(sentence, connective, "Why", 2);
                        }
                        break;
                    case CONTRAST:
//                        System.out.println("Sentence used to form a CONTRAST question: " + sentence);
                        query = new Question(sentence, connective, "Why", 3);
                        break;
                    case SIMILARITY:
//                        System.out.println("Sentence used to form a SIMILARITY question: " + sentence);
                        query = new Question(sentence, connective, "Why", 3);
                        break;
                    case EXAMPLIFICATION:
//                        System.out.println("Sentence used to form a EXAMPLIFICATION question: " + sentence);
                        if (connective.equals("for example")) {
                            query = new Question(sentence, "example", "Give an example why", 5);
                        }
                        else if (connective.equals("for instance")) {
                            query = new Question(sentence, "instance", "Give an instance where", 4);
                        }
                        break;
                    case CAUSALITY:
//                        System.out.println("Sentence used to form a CAUSALITY question: " + sentence);
                        query = causalityQuestion(sentence, connective);
                        break;
                    default:
                        break;
                }
                
                if (!questions.contains(query) && query != null) {
                    questions.add(query);
                }
            }
        }
        Collections.sort(questions);
    }
    
    private Question causalityQuestion(Sentence sentence, String connective) {
        boolean isTemporal = false;
        if (connective.equals("because") || connective.equals("since")) {
            if (QuestionHelper.isTemporal(sentence)) {
                isTemporal = true;
            }
        }
        
        int questionForm = 1;
        Sentence phrase = sentence;
        String firstWord = sentence.getWords().get(0).getContent().toLowerCase();
        if (firstWord.equals("since") || firstWord.equals("when")) {
            String temp = sentence.getContent();
            if (temp.contains(",")) {
                temp = sentence.getContent().substring(0, sentence.getContent().lastIndexOf(","));
                phrase = new Sentence(temp, sentence.getParagraphSentenceNumber(), Language.EN);
                questionForm = 3;
            }
        }
        
        if (isTemporal) {
            return new Question(phrase, connective, "Why", questionForm);
        }
        
        return new Question(phrase, connective, "When", questionForm);
    }
    
    /**
     * Helper method to get one of the previous sentence which has nothing to do with time.
     * @param sentence
     * @param connective
     * @return
     */
    private Question timeConnective(Text text, Sentence sentence, String connective){
    	int n = sentence.getSentenceNumber()-1;
		
		for (int i = n; i >= 0; i--) {
			Sentence phrase = text.getSentences().get(i);
			if (!phrase.getConnectiveTypes().containsKey("time")) {
				phrase = text.getSentences().get(i);
				return new Question(phrase, connective, "Why", 3);
			}
		}
		
		return null;		
    }
    
    public List<Question> getQuestions() {
    	return questions;
    }
}
