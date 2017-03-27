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
package lu.list.itis.dkd.assess.opennlp.difficulty;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.DictionaryHelper;
import lu.list.itis.dkd.assess.opennlp.util.NLPPropertiesFetcher;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

import java.util.Properties;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class EnglishMatrixes {
	final static Properties properties = NLPPropertiesFetcher.fetchProperties();

	/**
	 * Computes the difficulty level by the Flesch reading Ease formula where 0
	 * is very difficult and 100 very easy. 100+ Very Easy 80-100 Easy 60-80
	 * Medium 30-60 Difficult 0-30 Very Difficult
	 * 
	 * @param com
	 * @return Flesch, Amstad or Kanel & Moles depening on the language
	 */
	static Difficulty readabilityEase(Text text) {
		double difficultyLevel = 0;
		switch (text.getLanguage()) {
		// Amstad
		case DE:
			difficultyLevel = (180 - text.getAverageSentenceLength() - 58.5 * text.getAverageSyllablesPerWord());
			break;
		// Kandel and Moles: 209 � (0.68 * ( Syllables / Words)) � (1.15 * (
		// words / Sentences))
		case FR:
			// difficultyLevel = (207 - (1.015 * text.getNumberOfWords() /
			// text.getNumberOfSentences()) - 73.6 *
			// (text.getNumberOfSyllables() / text.getNumberOfWords()));
			difficultyLevel = 209 - 1.15 * text.getAverageSentenceLength() - 0.68 * text.getAverageSyllablesPerWord();
			// Flesh kincaid
		default:
			difficultyLevel = 206.835 - 1.015 * text.getAverageSentenceLength() - 84.6 * text.getAverageSyllablesPerWord();
			break;
		}

		if (difficultyLevel < 0.0) {
			return new Difficulty("amstad", "hard", 0.0, text.getLanguage());
		} else if (difficultyLevel >= 100.0) {
			return new Difficulty("amstad", "easy", 100.0, text.getLanguage());
		} else if (difficultyLevel >= 0.0 && difficultyLevel < 50.0) {
			return new Difficulty("amstad", "hard", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 50.0 && difficultyLevel < 80.0) {
			return new Difficulty("amstad", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("amstad", "easy", difficultyLevel, text.getLanguage());
	}

	/**
	 * The Flesch Grade Level Readability Formula (Flesch Kincaid) is an
	 * enhanced version of the Flesch Reading Ease. It was created by Rudolf
	 * Flesch and Co-Authored by John P. Kincaid. 0 - 3 Very Hard 3 - 5 hard 5 -
	 * 8 medium 8 - 10 easy 10+ very easy
	 * 
	 * @return 0.39 * (Words / Sentences) + 11.8 * (Syllables / Words) - 15.59
	 */
	static Difficulty fleschKincaid(Text text) {
		double difficultyLevel;
		if (text.getAverageSyllablesPerWord() > 1) {
			difficultyLevel = 0.39 * text.getAverageSentenceLength() + 11.8 * text.getAverageSyllablesPerWord() - 15.59;
		} else {
			difficultyLevel = 0.39 * text.getAverageSentenceLength() + 8.01;
		}

		if (difficultyLevel < 0.0) {
			return new Difficulty("fleschKincaid", "hard", 0.0, text.getLanguage());
		} else if (difficultyLevel > 10.0) {
			return new Difficulty("fleschKincaid", "easy", 10.0, text.getLanguage());
		} else if (difficultyLevel >= 0.0 && difficultyLevel < 5.0) {
			return new Difficulty("fleschKincaid", "hard", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 5.0 && difficultyLevel < 8.0) {
			return new Difficulty("fleschKincaid", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("fleschKincaid", "easy", difficultyLevel, text.getLanguage());
	}

	/**
	 * Based on the average sentence legnth and the number of unfamiliar words.
	 * Normally used to assess upper elementary through secondary materials. One
	 * of the most accurate Readability formulas. DS : Dale Score, or % of words
	 * not on Dale-Chall list of 3,000 common words ASL : average sentence
	 * length 0 - 3 very easy 3 - 6 easy 6 - 8 medium 8 - 10 hard 10+ very hard
	 * 
	 * @return (0.1579 x DS) + (0.0496 x ASL) + 3.6365
	 * @throws IOException
	 */
	static Difficulty daleChall(Text text) {
		double DS = DictionaryHelper.countRareWords(text, Wrapper.class.getResourceAsStream(properties.getProperty("wordlist.dale")));
		
		System.out.println("Rare Words: " + DS);

		// Reading Grade Score
		double difficultyLevel = 0.1579 * (DS / (double) text.getNumberOfWords()) + 0.0496 * text.getAverageSentenceLength()
				+ 3.6365;

		if (difficultyLevel < 0.0) {
			return new Difficulty("dale", "easy", 0.0, text.getLanguage());
		} else if (difficultyLevel > 10.0) {
			return new Difficulty("dale", "hard", 10.0, text.getLanguage());
		} else if (difficultyLevel >= 0.0 && difficultyLevel < 4.5) {
			return new Difficulty("dale", "easy", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 4.5 && difficultyLevel < 8.0) {
			return new Difficulty("dale", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("dale", "hard", difficultyLevel, text.getLanguage());
	}

	static Difficulty dictionaryCoca(Text text) {
		double difficultyLevel = DictionaryHelper.countDictionaryOccurances(text,
				Wrapper.class.getResourceAsStream(properties.getProperty("dictionary.english.coca")))
				/ text.getNumberOfWords() * 100;

		if (difficultyLevel < 0.0) {
			return new Difficulty("DictionaryCoca", "easy", 0.0, text.getLanguage());
		} else if (difficultyLevel >= 0.0 && difficultyLevel < 50.0) {
			return new Difficulty("DictionaryCoca", "easy", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 50.0 && difficultyLevel < 80.0) {
			return new Difficulty("DictionaryCoca", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("DictionaryCoca", "hard", difficultyLevel, text.getLanguage());
	}
}
