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
import java.util.Arrays;
import java.util.List;


/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.3
 */
public class GermanSyllabification {
	private String content;
	private int numberOfSyllables = 0;
	private int consonantParents = 0;
	private int groupConsonantParents = 0;
	private List<Vowel> vowels = new ArrayList<>();
	private List<GroupConsonant> groupConsonants = new ArrayList<>();
	private List<Consonant> consonants = new ArrayList<>();
//	private Collection<String> parts = new ArrayList<String>();

	private static final List<String> syllableExeptions = Arrays.asList("rosa", "ebbe", "tofu", "elba", "ungarn");

	private static final List<String> oneSyllableExeptions = Arrays.asList("wurst", "links", "rechts");
	
	GermanSyllabification(String content) {
		this.content = content.toLowerCase();
		findConsonant();
		findVowels();
		findGroupConsonant();
		this.consonantParents = parentConsonants();
		this.groupConsonantParents = parentGroupConsonants();
		//this.parts = getCompoundParts(content);
	}
	
	List<Vowel> getVowels() {
        return vowels;
    }

    List<GroupConsonant> getGroupConsonants() {
        return groupConsonants;
    }

    List<Consonant> getConsonants() {
        return consonants;
    }

    private void findVowels() {
		
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

	private void findGroupConsonant() {
		int beginning = 0, ending = 1;
		if (content.length() > 4)
			beginning = 2;
		
		String groupConsonantWord = content.substring(beginning, content.length()-ending);
		
		for (String currentGroupConsonant : GroupConsonant.getGroupConsonants()) {
			int numberOfPreviousLetters = 0;
			while (groupConsonantWord.contains(currentGroupConsonant)) {				
				if (currentGroupConsonant.equals("ch")) {		
					int chPos = Integer.valueOf(groupConsonantWord.indexOf("ch")) + numberOfPreviousLetters;
						
					if (groupConsonantWord.contains("sch")) {
						int schPos = Integer.valueOf(groupConsonantWord.indexOf("sch")) + numberOfPreviousLetters;
						if (chPos - 1 == schPos) {
							GroupConsonant groupConsonant = new GroupConsonant("sch", schPos + beginning);
							groupConsonants.add(groupConsonant);
						}						
					}
					else {
						GroupConsonant groupConsonant = new GroupConsonant("ch", chPos + beginning);
						groupConsonants.add(groupConsonant);
					}
				} 
				else {
					GroupConsonant groupConsonant = new GroupConsonant(currentGroupConsonant, groupConsonantWord.indexOf(currentGroupConsonant) + beginning);
					groupConsonants.add(groupConsonant);
				}
				
				if ((groupConsonantWord.indexOf(currentGroupConsonant)+1 < groupConsonantWord.length()) && (currentGroupConsonant.equals("x"))) {
				    groupConsonantWord = groupConsonantWord.substring(groupConsonantWord.indexOf(currentGroupConsonant)+1, groupConsonantWord.length());
				}
				else if (groupConsonantWord.indexOf(currentGroupConsonant)+2 < groupConsonantWord.length()) {
				    groupConsonantWord = groupConsonantWord.substring(groupConsonantWord.indexOf(currentGroupConsonant)+2, groupConsonantWord.length()-ending);
				}
				else {
				    break;
				}
					
				numberOfPreviousLetters++;
			}
		}
	}
	
	private void findConsonant() {
		int nextLetter = 0;
		int previousLetter = 0;
		int beginning = 0; int ending = 0;
		
		//String remainingWord = content;
		if (content.length() > 4) {
			beginning = 2;
			ending = 2;
			if (content.length() <= 6) {
			    ending = 1;
			}
		}
		
		//Find consonants
		for (int i = 0; i < Consonant.getGermanConsonants().length; i++) {
	  		for (int j = beginning; j < content.length()-ending; j++) {	   			
	  			if (content.charAt(j) == Consonant.getGermanConsonants()[i]) {			
	  				boolean groupConstant = false;
			   				
	  				//Check next letter to check group consonant
	  				if (j + 1 < content.length()) {
	  					nextLetter = j + 1;	  					
	  					if (content.charAt(nextLetter) == 'c') {
	  					    groupConstant = true;
	  					}
	  					else if (content.charAt(nextLetter) == 'h') {
	  					    groupConstant = true;
	  					}
	  				}
	  				//Check previous letter to check group consonant
	  				if ((j - 1 > 0) && (!groupConstant)) {
	  					previousLetter = j - 1;
	  					if (content.charAt(previousLetter) == 'c') {
	  					    groupConstant = true;
	  					}
	  					else if (content.charAt(nextLetter) == 'h') {
	  					    groupConstant = true;
	  					}
	  				}	
	  				
	  				//Check for ph & & rh & sh & th
	  				if ((content.charAt(j) == 'h') && (j - 1 > 0)) {
	  					if (content.charAt(j - 1) == 'p') {
	  					    groupConstant = true;
	  					}
	  					if (content.charAt(j - 1) == 'r') {
	  					    groupConstant = true;
	  					}
	  					if (content.charAt(j - 1) == 's') {
	  					    groupConstant = true;
	  					}
	  					if (content.charAt(j - 1) == 't') {
	  					    groupConstant = true;
	  					}
	  				}
			   				
	  				if (!groupConstant) {
	  					String currentConsonant = String.valueOf(content.charAt(j));
	  					Consonant consonant = new Consonant(currentConsonant, j);
	  					consonants.add(consonant);
	  				}	  				
	  			}
	  		}
	   	}
	}

	private int parentConsonants() {
		int parent = 0;
		
		if (consonants.size() >= 2) {
			for (Consonant currentConsonant : consonants) {
				int pos = currentConsonant.getPosition()+1;
				for (Consonant bufferConsonant : consonants) {
					if (bufferConsonant.getPosition() == pos) {
					    parent++;
					}
				}
			}
		}
		
		return parent;
	}
	
	private int parentGroupConsonants() {
		int parent = 0;
		
		if ((consonants.size() >= 1) && (groupConsonants.size() >= 1)) {
			
			for (GroupConsonant currentGroupConsonant : groupConsonants) {
				int beginningPos = currentGroupConsonant.getPosition()-1;
				int endingPos = currentGroupConsonant.getPosition() + 2;
				if (currentGroupConsonant.getLabel().equals("x"))
					endingPos = currentGroupConsonant.getPosition() + 1;
				else if (currentGroupConsonant.getLabel().equals("sch"))
					endingPos = currentGroupConsonant.getPosition() + 3;
				
				if (groupConsonants.size() > 1) {
					for (GroupConsonant bufferGroupConsonant : groupConsonants) {
						if ((bufferGroupConsonant.getPosition() == beginningPos) || 
							(bufferGroupConsonant.getPosition() == endingPos)) 
							parent++;
					}
				}
				
				for (Consonant bufferConsonant : consonants) {
					if ((bufferConsonant.getPosition() == beginningPos) || 
						(bufferConsonant.getPosition() == endingPos)) 
						parent++;
				}
			}
		}
		
		return parent;
	}
	
//	public Collection<String> getCompoundParts(String word) throws IOException
//	{
//		AbstractWordSplitter splitter = new GermanWordSplitter(true);
//		splitter.setStrictMode(true);
//		return splitter.splitWord(word);
//	}
	
	//Source: http://www.oxforddictionaries.com/words/german-general-advice-on-writing-in-german-4-syllable-division-in-german
	int getNumberOfSyllables() {		
		//Exceptions
	    if (oneSyllableExeptions.contains(content)) {
	        return 1;
	    }
	    
	    if (syllableExeptions.contains(content)) {
	        return 2;
	    }
		
		//Too short
		if (content.length() <= 4) {
		    return 1;
		}

//		Collection<String> parts = getCompoundParts(content);
//		if (parts.size() >= 2) {
//			return parts.size(); 
//		}
//		else {			
		if (content.contains("ck")) {
		    return 2;
		}
					
		if ((groupConsonants.size() != 0) && (groupConsonantParents == 0)) {
		    numberOfSyllables += groupConsonants.size();
		}
				
		if ((consonantParents >= 1) && (consonants.size() != 0)) {
		    numberOfSyllables += (consonants.size()-consonantParents);
		}
		else {
		    numberOfSyllables += consonants.size();
		}
			
			//Ignore groupConsonant at the end
//			for (GroupConsonant currentGroupConsonant : groupConsonants)
//			{	
//				int pos = currentGroupConsonant.getPosition();
//				
//				if (currentGroupConsonant.getLabel().equals("sch"))
//				{
//					if (content.length() - (pos + 3) == 0)
//						numberOfSyllables--;
//				}
//				else
//				{
//					if (content.length() - (pos + 2) == 0)
//						numberOfSyllables--;
//				}
//			}	
			
		return numberOfSyllables+1;
		
	}
}
