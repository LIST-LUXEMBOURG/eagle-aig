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
public class FrenchMatrixes {
	final static Properties properties = NLPPropertiesFetcher.fetchProperties();

	/**
	 * Computes the difficulty level by the Flesch reading Ease formula where 0
	 * is very difficult and 100 very easy. 100+ Very Easy 80-100 Easy 60-80
	 * Medium 30-60 Difficult 0-30 Very Difficult
	 * 
	 * @param com
	 * @return Flesch, Amstad or Kanel & Moles depening on the language
	 */
	static Difficulty kandel(Text text) {
		// double difficultyLevel = (207 - (1.015 * text.getNumberOfWords() /
		// text.getNumberOfSentences()) - 73.6 * (text.getNumberOfSyllables() /
		// text.getNumberOfWords()));
		double difficultyLevel = 209 - 1.15 * text.getAverageSentenceLength()
				- 0.68 * text.getAverageSyllablesPerWord();

		if (difficultyLevel < 50.0) {
			return new Difficulty("kandel", "hard", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 50.0 && difficultyLevel < 80.0) {
			return new Difficulty("kandel", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("kandel", "easy", difficultyLevel, text.getLanguage());
	}

	/**
	 * The Lix formula, developed by Bjson from Sweden, is very simple and uses
	 * a mapping table for its scores. It is useful for documents of Western
	 * European languages. It has been successfully used on, English, German,
	 * French, Greek and Sweedish. The score is based on sentence length and the
	 * number of long words (long words are words over six characters). 0-24
	 * Very easy 25-34 Easy 35-44 Standard 45-54 Difficult 55+ Very difficult
	 * 
	 * @return (words / sentences) + 100 (words >= 7 characters / words)
	 */
	static Difficulty lix(Text text) {
		int wordGreater7 = 0;

		for (String word : text.getTokenizedText()) {
			if (word.length() >= 7) {
				wordGreater7++;
			}
		}

		double difficultyLevel = text.getAverageSentenceLength() + 100 * (wordGreater7 / text.getNumberOfWords());

		if (difficultyLevel < 34.0) {
			return new Difficulty("lix", "easy", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 34.0 && difficultyLevel < 44.0) {
			return new Difficulty("lix", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("lix", "hard", difficultyLevel, text.getLanguage());
	}

	static Difficulty dictionaryAlterego(Text text) {
		double difficultyLevel = DictionaryHelper.countDictionaryOccurances(text,
				Wrapper.class.getResourceAsStream(properties.getProperty("dictionary.french.alterego")))
				/ text.getNumberOfWords() * 100;
		if (difficultyLevel < 50.0) {
			return new Difficulty("DictionaryAlterego", "easy", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 50.0 && difficultyLevel < 80.0) {
			return new Difficulty("DictionaryAlterego", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("DictionaryAlterego", "hard", difficultyLevel, text.getLanguage());
	}

	static Difficulty dictionaryGoug(Text text) {
		double difficultyLevel = DictionaryHelper.countDictionaryOccurances(text,
				Wrapper.class.getResourceAsStream(properties.getProperty("dictionary.french.gougenheim")))
				/ text.getNumberOfWords() * 100;

		if (difficultyLevel < 50.0) {
			return new Difficulty("DictionaryGoug", "easy", difficultyLevel, text.getLanguage());
		} else if (difficultyLevel >= 50.0 && difficultyLevel < 80.0) {
			return new Difficulty("DictionaryGoug", "medium", difficultyLevel, text.getLanguage());
		}

		return new Difficulty("DictionaryGoug", "hard", difficultyLevel, text.getLanguage());
	}
}
