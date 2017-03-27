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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.QuestionHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class Question implements Comparable<Question>{
    private Sentence sentence;
	private String qType = "";
	private String connective = "";
	private String verb = "";
	private String aux = "";
	private String subject = "";
	private String content = "";
	private String cop = "";
    protected static final Logger logger = Logger.getLogger(Text.class.getSimpleName());
    
    private boolean checkLanguage(Language language){
        switch (language) {
            case EN:            
                return true;
            case DE:
                logger.log(Level.WARNING, "This function is not yet implemented in the current version.");
                return false;
            case FR: 
                logger.log(Level.WARNING, "This function is not yet implemented in the current version.");
                return false;
            default:
                return false;
        }
    }
	
	public Question(String sentence, Language language) {
	    if (checkLanguage(language)) {
	        content = prettyPrintQuestion(QuestionHelper.replaceSentenceEndingByQuestionMark(sentence));
	    }
	}
	
	/**
	 * Constructor to form a question out of a sentence with the provided question type (Why, When, ...)
	 * @param sentence: A sentence object
	 * @param qType: Why, When, ...
	 */
	public Question(Sentence sentence, String qType) {
	    if (checkLanguage(sentence.getLanguage())) {
    	    this.sentence = sentence;
            this.qType = qType;
            syntactics(sentence, 0);
//            System.out.println("Sentence used for the question generation: " + sentence);
            this.content = prettyPrintQuestion(Generation.transform(this, 3));        
	    }
    }
	
	/**
	 * Constructor to form a question out of a sentence with the provided question type (Why, When, ...)
	 * Form 1: Part in front of the connective is used to form a question.
	 * Form 2: Part after the connective is used to form a question.
	 * Form 3: Entire Sentence is used to form a question.
	 * @param sentence
	 * @param connective
	 * @param qType
	 * @param questionForm
	 */
	public Question(Sentence sentence, String connective, String qType, int questionForm) {
	    if (checkLanguage(sentence.getLanguage())) {
    	    this.sentence = sentence;
    		this.connective = connective;
    		this.qType = qType;
    		syntactics(sentence, questionForm);
    		this.content = prettyPrintQuestion(Generation.transform(this, questionForm));
	    }
	}
	
	private void syntactics(Sentence sentence, int questionForm) {
		boolean subjectFound = false;
		boolean auxFound = false;
		List<Word> sentenceDependency = new ArrayList<>();
		
		switch(questionForm) {
		    //Part in front of the connective is used.
    		case 1: 
    			for (Word word : sentence.getWords()) {
    				if (word.getContent().toLowerCase().equals(connective)) {
    					break;
    				}
    				else {					
    					sentenceDependency.add(word);
    				}
    			}
    			break;
    		//Part behind the connective is used
    		case 2: 
    			String[] connectiveParts = connective.split(" ");
    			
    			boolean startAdding = false;
    			for (Word word : sentence.getWords()) {
    				if (startAdding) {
    					sentenceDependency.add(word);
    				}
    				
    				String lastConnectivePart = connectiveParts[connectiveParts.length-1];
    				if (word.getContent().toLowerCase().equals(lastConnectivePart)) {
    					startAdding = true;
    				}
    			}
    			break;
    		//Entire sentence is used.
    		default: 
    			sentenceDependency.addAll(sentence.getWords());
    			break;
		}
		
    	boolean dobj = false;
        String root = "";
    	for (Word word : sentenceDependency) {
    	    String type = word.getRelationTag();
    	    
    	    if (type.contains("nsubj") && !subjectFound) {
    	        this.subject = word.getContent();
                subjectFound = true;
            }
            
            if (type.equals("root")) {
                root = word.getContent();
            }
            
            if (type.equals("cop")) {
                this.cop = word.getContent();
            }
            
            if (type.equals("dobj")) {
                this.verb = word.getContent();
                dobj = true;
                break;
            }
            
            //Stanford tags "to" as mark, maltparser not...
            if (type.contains("aux") && !auxFound && !word.getContent().equals("to")) {
                this.aux = word.getContent();
                auxFound = true;
            }   
    	}
		
    	//No object of the verb fount
    	if (!dobj) {
    	    this.verb = root;
    	}
	}
	
    /**
     * Helper function to remove mutliple spaces and to remove the space before the sentence ending symbol.
     * @param question
     * @return
     */
     private String prettyPrintQuestion(String question) {   
         if (question.equals("")) {
             return question;
         }
         
         String res = question.replaceAll("\\s+", " ");         
         char lastChar = res.charAt(res.length()-1);
         char secondLastChar = res.charAt(res.length()-2);
         
         if (secondLastChar == ' ') {
             return res.substring(0, res.length()-2) + lastChar;
         }
         
         return question;
     }
     
    public Sentence getSentence(){
        return sentence;
    }

	public String getqType() {
		return qType;
	}

	public String getConnective() {
		return connective;
	}

	public String getVerb() {
		return verb;
	}

	public String getAux() {
		return aux;
	}

	public String getSubject() {
		return subject;
	}
	
	public String getContent() {
		return content;
	}

	public String getCop() {
		return cop;
	}
   
    @Override
    public String toString(){
        return getContent();
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }

        if (that instanceof Question) {
            return hashCode() == ((Question) that).hashCode();
        }
        return false;
    }

	@Override
	public int compareTo(Question o) {
		return this.content.compareTo(o.content);
	}
}