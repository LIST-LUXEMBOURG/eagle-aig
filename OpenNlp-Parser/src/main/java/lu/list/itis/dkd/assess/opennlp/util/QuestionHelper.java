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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class QuestionHelper {
	private static List<String> connectives = Arrays.asList("because", "example", "generate", "instance", 
            "result", "since", "when", "and", "or", "but", "also", "however");
	
	private static List<String> TIME = Arrays.asList("AM", "PM");
//	private static List<String> VOCABULARY = Arrays.asList("begin", "end", "start");
	private static List<String> DAY = Arrays.asList("monday", "tuesday", "wednesday", "thursday", 
													"friday", "saturday", "sunday");
	private static List<String> MONTH = Arrays.asList("january", "february", "mars", "avril", "may", 
			                                          "june", "july", "august", "september", 
			                                          "oktober", "november", "december"); 
	


    /**
     * Helper function to add a question mark at the end of the formed question if not already there.
     * @param sentence
     * @return sentence with "?" instead of its ending.
     */
    public static String replaceSentenceEndingByQuestionMark(String sentence) {
        int sentenceLength = sentence.length()-1;
        char lastChar = sentence.charAt(sentenceLength);
            
        //Replace sentence ending by "?"
        if (lastChar == '.' || lastChar == '!') {
            return sentence.substring(0, sentenceLength) + "?";
        }
        
        //Add ?
        if (lastChar != '.' || lastChar != '!' || lastChar != '?') {
            return sentence + "?";
        }

        return sentence;
    }
	
	/**
	 * Helper class to find out with a basic approach if a sentence could have a temporal meaning or not.
	 * @param sentence
	 * @return
	 */
	public static boolean isTemporal(Sentence sentence) {		
		for (Word word : sentence.getWords()) {
			//Dummy approach to check for year
			if (word.getTag().equals("CD") && word.getContent().length() <= 4) {
				return false;
			}
			
			String lemma = word.getLemma().toLowerCase();
			
			if (TIME.contains(lemma)) {
				return false;
			}
			if (DAY.contains(lemma)) {
				return false;
			}
			if (MONTH.contains(lemma)) {
				return false;
			}
//			if (VOCABULARY.contains(lemma)) {
//				return false;
//			}
		}
			
		return true;			
	}
	
	public static String removeStuff(String text){
	    if (text.contains(" , ")) {
	        text = text.replaceAll(" ,", "");
	    }
	    else {
	        text = text.replaceAll(",", "");
	    }
	    
	    text = text.replaceAll("\\$", "");
	    return text.trim();
	}
	
	/**
	 * Helper method to receive the part of the sentence before the connective.
	 * @param phrase
	 * @param connective
	 * @return
	 */
	public static String getArgument1(String phrase, String connective) {
		if (phrase.toLowerCase().contains(connective)) {
			int connectivePos = phrase.toLowerCase().indexOf(connective);
			return removeStuff(phrase.substring(0, connectivePos));
		}
		
		return "";		
	}
	
	/**
	 * Helper function to get the part of the sentence between connective 1 and connective 2(if any).
	 * @param phrase
	 * @param connective
	 * @return
	 */
	public static String getArgumentBetween(String phrase, Map<Type.Connective, String> connectiveTypes, String connective) {		
	    String connectiveString = connective.toLowerCase();
	    for (Type.Connective type : connectiveTypes.keySet()) {
			String otherConnectiveString = connectiveTypes.get(type).toLowerCase();
			 
			if (!otherConnectiveString.equals(connective)) {
				int connectivePos = phrase.toLowerCase().indexOf(connectiveString);
				int otherConnectivePos = phrase.toLowerCase().indexOf(otherConnectiveString);
				
				if (connectivePos < otherConnectivePos) {
					return removeStuff(phrase.substring(connectivePos + connectiveString.length(), otherConnectivePos));
				}
				else {
					return removeStuff(phrase.substring(connectivePos + connectiveString.length(), phrase.length()));
				}
			}
		}
		
		int connectivePos = phrase.toLowerCase().indexOf(connectiveString);
		return removeStuff(phrase.substring(connectivePos+connectiveString.length(), phrase.length()));
	}
	
	/**
	 * Helper method to receive the part of the sentence behind the connective.
	 * @param phrase
	 * @param connective
	 * @return
	 */
	public static String getArgument2(String phrase, String connective) {
		if (phrase.toLowerCase().contains(connective)) {
			int connectivePos = phrase.toLowerCase().indexOf(connective);
			return removeStuff(phrase.substring(connectivePos+connective.length(), phrase.length()));
		}
		
		return "";
	}
	
	/**
	 * Helper method to find out the tense (present or past) of a sentence be the mean of their tags.
	 * @param sentence
	 * @return
	 */
	public static String tense(Sentence sentence) {
		for (Word word : sentence.getWords()) {
			String tag = word.getTag();
			//present
			if (tag.equals("VB") || tag.equals("VBG") || tag.equals("VBP") || tag.equals("VBZ")) {
				System.out.println("Tense: Present");
				return "do";
			}
			//past
			if (tag.equals("VBD") || tag.equalsIgnoreCase("VBN")) {
				System.out.println("Tense: Past");
				return "did";
			}
		}
		
		return "do";
	}
	
	/**
	 * Helper method to find the auxilary in a sentence.
	 * @param sentence
	 * @return
	 */
	public static String getAuxilary(Sentence sentence) {		
		for (int i = 1; i < sentence.getWords().size(); i++) {			
			String tag = sentence.getWords().get(i).getTag();
			String token = sentence.getWords().get(i).getContent();
			if (!connectives.contains(token.toLowerCase()) && tag.contains("VB")) {
				System.out.println("Auxilary: " + token);
				return token;
			}
		}
		
		return "";
	}
}
