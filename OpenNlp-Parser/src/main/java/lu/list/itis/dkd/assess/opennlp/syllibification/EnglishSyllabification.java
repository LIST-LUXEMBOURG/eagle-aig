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
package lu.list.itis.dkd.assess.opennlp.syllibification;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lu.list.itis.dkd.assess.opennlp.util.NLPPropertiesFetcher;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.1
 */
public class EnglishSyllabification {
	private String content;
	private int numberOfSyllables = 0;
	private List<Vowel> vowels = new ArrayList<>();
	private List<Consonant> consonants = new ArrayList<>();
	private List<ConsonantBlend> consonantBlends = new ArrayList<>();
	
	private final Properties properties = NLPPropertiesFetcher.fetchProperties();
	
	EnglishSyllabification(String content) {
		this.content = content.toLowerCase();
		findVowels();
		findConsonant();
		findConsonantBlends();
	}

	private void findVowels()
	{		
		for (String currentVowel : Vowel.getVowels()) {
			String remainingWord = content;
			int numberOfpreviousLetters = 0;
	   		while (remainingWord.contains(currentVowel)) {
	   			int pos = remainingWord.indexOf(currentVowel) + numberOfpreviousLetters;
	   			
	   			Vowel vowel = new Vowel(currentVowel, pos);
	   			vowels.add(vowel);
	   			
	   			remainingWord = content.substring(pos+1, content.length());
	   			numberOfpreviousLetters = content.length() - remainingWord.length();
	   		}
	   	}
	}
	
	private boolean consonantContainsPosition(int position) {
		for (Consonant consonant : consonants) {
			if (consonant.getPosition() == position) {
			    return true;
			}
		}
		
		return false;
	}
	
	private void findConsonantBlends() {
		for (String currentConsonantBlend : ConsonantBlend.getConsonantblends()) {
		    if (content.length()-1 > 0)
		    {
		        String remainingWord = content.substring(1, content.length()-1);
	            int numberOfPreviousLetters = 0;
	            while (remainingWord.contains(currentConsonantBlend)) {             
	                int pos = remainingWord.indexOf(currentConsonantBlend) + 1 + numberOfPreviousLetters;
	                ConsonantBlend consonantBlend = new ConsonantBlend(currentConsonantBlend, pos);
	                consonantBlends.add(consonantBlend);

	                remainingWord = content.substring(remainingWord.indexOf(currentConsonantBlend)+currentConsonantBlend.length(), remainingWord.length());
	                numberOfPreviousLetters = content.length() - remainingWord.length();
	            }
		    }
		}
	        
	        //Rule 7
	        for (ConsonantBlend consonantBlend : consonantBlends) {         
	            if (consonantBlend.getLabel().length() == 2) {
	                int consonantBlendChar1 = consonantBlend.getPosition();
	                int consonantBlendChar2 = consonantBlend.getPosition()+1;
	        
	                if ((consonantContainsPosition(consonantBlendChar1)) && (consonantContainsPosition(consonantBlendChar2))) {
	                    //Delete consonants because it is a consonantBlend
	                    List<Consonant> buffer = new ArrayList<>();
	                    
	                    for (Consonant currentConsonant : consonants) {
	                        if ((currentConsonant.getPosition() != consonantBlendChar1) && (currentConsonant.getPosition() != consonantBlendChar2))
	                            buffer.add(currentConsonant);
	                    }
	                    
	                    consonants.clear();
	                    consonants.addAll(buffer);
	                }
	            }       
	            else if (consonantBlend.getLabel().length() == 3) {
	                int consonantBlendChar1 = consonantBlend.getPosition();
	                int consonantBlendChar2 = consonantBlend.getPosition()+1;
	                int consonantBlendChar3 = consonantBlend.getPosition()+2;
	        
	                if ((consonantContainsPosition(consonantBlendChar1)) && (consonantContainsPosition(consonantBlendChar2)) && (consonantContainsPosition(consonantBlendChar3))) {
	                    //Delete consonants because it is a consonantBlend
	                    List<Consonant> buffer = new ArrayList<>();
	                    
	                    for (Consonant currentConsonant : consonants) {
	                        if ((currentConsonant.getPosition() != consonantBlendChar1) && (currentConsonant.getPosition() != consonantBlendChar2)) {
	                            buffer.add(currentConsonant);
	                        }
	                    }
	                    
	                    consonants.clear();
	                    consonants.addAll(buffer);
	                }
	            }
	        numberOfSyllables++;
		}	
		
	}
	
	private boolean vowelContainsPosition(int position) {
		for (Vowel vowels : vowels) {
			if (vowels.getPosition() == position)
				return true;
		}
		
		return false;
	}

	private void findConsonant() {
		for (int i = 0; i < Consonant.getEnglishConsonants().length; i++) {
			for (int j = 0; j < content.length(); j++) {
				if (content.charAt(j) == Consonant.getEnglishConsonants()[i]) {
					Consonant currentConsonant = new Consonant(String.valueOf(content.charAt(j)), j);
					consonants.add(currentConsonant);
				}
			}
		}
		 
		//Rule 8/9
		for (Consonant consonant : consonants) {
			int previousPos = consonant.getPosition()-1;
			int nextPos = consonant.getPosition()+1;
			
			if ((vowelContainsPosition(previousPos)) && (vowelContainsPosition(nextPos))) {
				numberOfSyllables++;
			}
		}
		
	}
	
	//No clue how it works... Source: http://stackoverflow.com/questions/8870261/how-to-split-text-without-spaces-into-list-of-words
//	private static List<String> splitWordWithoutSpaces(String instring, String suffix) throws IOException {
//		if(isAWord(instring)) {
//			if(suffix.length() > 0) {
//				List<String> rest = splitWordWithoutSpaces(suffix, "");
//		        if(rest.size() > 0) {
//		        	List<String> solutions = new LinkedList<>();
//		            solutions.add(instring);
//		            solutions.addAll(rest);
//		            return solutions;
//		        }
//			} else {
//				List<String> solutions = new LinkedList<>();
//		        solutions.add(instring);
//		        return solutions;
//			}
//		}
//	    if(instring.length() > 1) {
//	        String newString = instring.substring(0, instring.length()-1);
//	        suffix = instring.charAt(instring.length()-1) + suffix;
//	        List<String> rest = splitWordWithoutSpaces(newString, suffix);
//	        return rest;
//	    }
//	    return Collections.EMPTY_LIST;
//	}

	/**
	 * Read compound word list - Mix from two sources: 
       1. http://www.learningdifferences.com/Main%20Page/Topics/Compound%20Word%20Lists/Compound_Word_%20Lists_complete.htm
       2. http://www.enchantedlearning.com/wordlist/compoundwordscategorized.shtml
	 * @param term
	 * @return
	 */
    private boolean isCompound(String term) {
	    String compoundWordList = Wrapper.loadTextFile(Wrapper.class.getResourceAsStream(properties.getProperty("syllibification.english")));        
        if (compoundWordList.contains(term)) {
        	return true;
        }
        return false;
	}

	int getNumberOfSyllables() {
        return numberOfSyllables;
    }

    List<Vowel> getVowels() {
        return vowels;
    }

    List<Consonant> getConsonants() {
        return consonants;
    }

    List<ConsonantBlend> getConsonantBlends() {
        return consonantBlends;
    }
	
	//Source: http://www.createdbyteachers.com/syllablerulescharts.html
	int findNumberOfSyllables() {
		//Rule 3
		if (content.length() <= 4) {
			//Rule 5
			if (content.contains("ck") || content.contains("x")) {
				numberOfSyllables++;
			}
			else {
				return 1;
			}
		}
		
		//Rule 5
		if (content.contains("ck") || content.contains("x")) {
			numberOfSyllables++;
		}
		
		//Check for compound word - Rule 6
		if (isCompound(content)) {	
			return 2;
		}
		
		
		//Rule 13
		if ((content.startsWith("be")) || (content.startsWith("de")) || (content.startsWith("ex")))	{
			numberOfSyllables++;
		}
		
		//Rule 14
		if (content.endsWith("le")) {
			numberOfSyllables++;
		}

		//Rule 5
//		int parent = parentConsonants(consonantPositions);
//		if ((parent >= 1) && (consonants.size() != 0))	
//			numberOfSyllables += parent;
						
		return (numberOfSyllables+1);
	}
}