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

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.QuestionHelper;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class Generation {
	
	/**
	 * Helper function to determine if the sentence need a question mark or not.
	 * @param sentence
	 * @return
	 */
	private static String isNoQuestionType(Question question, String sentence){
		if (question.getqType().equals("")) {
			int sentenceLength = sentence.length();
			int firstLetter = 0;
			char firstCharacter = sentence.charAt(firstLetter);
			
			//Find first letter
			if (!Character.isLetter(firstCharacter)) {
				for (int i = 0; i < sentenceLength; i++) {
					if (Character.isLetter(sentence.charAt(i))) {
						firstLetter = i;
						firstCharacter = sentence.charAt(firstLetter);
						break;
					}
				}
			}
			
			firstCharacter = Character.toUpperCase(firstCharacter);
			return firstCharacter + sentence.substring(firstLetter+1, sentenceLength);
		}
		
		return sentence;
	}
	
	/**
	 * This function transforms a sentence into a question.
	 * @return
	 */
	public static String transform(Question question, int questionForm) {
		String temp = question.getSentence().getContent();
		String sub = question.getSubject();
		String aux = question.getAux();
		String cop = question.getCop();
		
//		System.out.println("Sentence used: " + temp);
//		System.out.println("Sub: " + sub);
//		System.out.println("Aux: " + aux);
//		System.out.println("Cop: " + cop);

		//Transform pronoun "I", "us" and "we" to "you".
		if (!sub.equals("")) {
			if (sub.toLowerCase().equals("i") || sub.toLowerCase().equals("we") || sub.toLowerCase().equals("us")) {
				temp = temp.replaceAll(sub, "you");
			}
		}
		
		//Transform PRP$ to "your"
		for (Word word : question.getSentence().getWords()) {
		    if (word.getTag().equals("PRP$") && word.getContent().equals("my")) {
		        temp = temp.replace(word.getContent(), "your");
		    }
		}
		
		//Transfrom "can" to "to
//		temp = temp.replaceAll("can", "to");
		
		//lowercase first word if not an noun
		String firstWord = question.getSentence().getWords().get(0).getContent();
        temp = temp.replace(firstWord, firstWord.toLowerCase());
		
		switch(questionForm) {
			//Transforms a sentence in a question using the part in front of the connective.
			case 1: 
				temp = QuestionHelper.getArgument1(temp, question.getConnective());
				break;
			//Transforms a sentence in a question using the part behind the connective.
			case 2: 
				temp = QuestionHelper.getArgument2(temp, question.getConnective());
				break;
			//Transforms a sentence into a question using the entire sentence.
			case 3: 
				String arg1 = QuestionHelper.getArgument1(temp, question.getConnective());
				String arg2 = QuestionHelper.getArgumentBetween(temp, question.getSentence().getConnectiveTypes(), question.getConnective());
				
//				System.out.println("Argumaent 1: " + arg1);
//				System.out.println("Argumaent 2: " + arg2);
				
				temp =  arg1 + " " + arg2;
				temp = temp.trim();
				break;
			//Transforms a sentence in a question using the part behind the connective without further computation
			case 4:
				temp = QuestionHelper.getArgument2(temp, question.getConnective());
				int sentenceLength = temp.length()-1;
				char ending = temp.charAt(sentenceLength); 
					
				if (ending == '.' || ending == '!') {
					return isNoQuestionType(question, question.getqType() + " " + temp.replaceAll(",", ""));
				}
				
				return question.getqType() + " " + temp.replaceAll(",", "") + ".";
			//Transforms a sentence in a question using the part in front of the connective without further computation
			case 5:
				if (question.getConnective().equals("example")) {
					temp = QuestionHelper.getArgument1(temp, question.getConnective()).replaceFirst("for", "");
				}
				else {
					temp = QuestionHelper.getArgument1(temp, question.getConnective());
				}
				return isNoQuestionType(question, question.getqType() + " " + temp.replaceAll(",", "") + ".");
			//Transforms a sentence in a question using the entire sentence without further computation.
			case 6:
			    temp = temp.replace(question.getConnective(), "");
			    temp = QuestionHelper.removeStuff(temp);
			    break;
			default: break;
		}
		
		//Auxilary?
		if (!aux.equals("")) {
			temp = temp.replaceFirst(aux + " ", "");
			temp = isNoQuestionType(question, question.getqType() + " " + aux + " " + temp);			
			return QuestionHelper.replaceSentenceEndingByQuestionMark(temp);
		}
		else if (!cop.equals("")) {
			temp = temp.replaceFirst(cop + " ", "");
			temp = isNoQuestionType(question, question.getqType() + " " + cop + " " + temp);
			return QuestionHelper.replaceSentenceEndingByQuestionMark(temp);
		}
		else {			
			for (Word word : question.getSentence().getWords()) {
				String token = word.getContent();
				
				if (token.equals(question.getVerb())) {
					String verbTense = QuestionHelper.tense(question.getSentence());
					
					if (verbTense.equals("did")) {
						temp = isNoQuestionType(question, question.getqType() + " " + verbTense + " " + temp.replaceFirst(token, word.getLemma()));
						return QuestionHelper.replaceSentenceEndingByQuestionMark(temp);
					}
					
					return isNoQuestionType(question, QuestionHelper.replaceSentenceEndingByQuestionMark(question.getqType() + " " + verbTense + " " + temp));
					
				}
			}
		}
		
		return  "";
	}
	

	
//	public Question getQuestion() {
////		return prettyPrintQuestion(question.getQuestion());
//	    return question;
//	}
}