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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.Word;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 4.0.1
 */
public class DictionaryHelper {
	final static Properties properties = NLPPropertiesFetcher.fetchProperties();
	protected static final Logger logger = Logger.getLogger(DictionaryHelper.class.getName());

	/**
	 * Reads in a text file and saves each line to a hashmap in lowercase.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static HashSet<String> loadDictionary(InputStream textDictionary) {
		HashSet<String> set = new HashSet<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(textDictionary, "UTF8"));
			String line;

			while ((line = br.readLine()) != null) {
				set.add(line.toLowerCase());
			}

			br.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Text extension not supported.");
			e.printStackTrace();
		}
		return set;
	}

	public static HashSet<String> loadNames(InputStream dictionaryInputStream) {
		HashSet<String> dictionaryEntries = new HashSet<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(dictionaryInputStream, "UTF8"));
			String line = "";

			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("[\\w]+");
				final Matcher m = p.matcher(line);

				while (m.find()) {
					dictionaryEntries.add(m.group().toString().toLowerCase());
				}
			}

			br.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Text extension not supported.");
			e.printStackTrace();
		}
		return dictionaryEntries;
	}

	public static double countDictionaryOccurances(Text text, InputStream dictionaryInputStream) {
		HashSet<String> dictionary = loadDictionary(dictionaryInputStream);
		HashSet<String> names = loadDictionary(
				Wrapper.class.getResourceAsStream(properties.getProperty("wordlist.name")));
		double numberOfOccurences = 0;
		double numberOfNames = 0;

		for (Sentence sentence : text.getSentences()) {
			for (Word word : sentence.getWords()) {
				// Is the word or lemma contained in the dictionary?
				if (dictionary.contains(word.getLemma().toLowerCase())
						|| dictionary.contains(word.getContent().toLowerCase())) {
					numberOfOccurences++;
				}
				// Is it a name?
				else {
					if (names.contains(word.getLemma().toLowerCase())
							|| names.contains(word.getContent().toLowerCase())) {
						numberOfNames++;
					}
				}
			}
		}

		numberOfOccurences = numberOfOccurences - numberOfNames;
		if (numberOfOccurences < 0) {
			return 0;
		}

		return numberOfOccurences;
	}

	/**
	 * Counts the words not included in a dictionary
	 * 
	 * @param dictionary
	 * @return double
	 * @throws IOException
	 */
	public static double countRareWords(Text text, InputStream dictionaryInputStream) {
		HashSet<String> dictionary = loadDictionary(dictionaryInputStream);
		HashSet<String> names = loadDictionary(
				Wrapper.class.getResourceAsStream(properties.getProperty("wordlist.name")));
		double numberOfOccurences = 0;
		double numberOfNames = 0;

		for (Sentence sentence : text.getSentences()) {
			for (Word word : sentence.getWords()) {
				// Is the word or lemma contained in the dictionary?
				if (dictionary.contains(word.getLemma().toLowerCase())
						|| dictionary.contains(word.getContent().toLowerCase())) {
					numberOfOccurences++;
				}
				// Is it a name?
				else {
					if (names.contains(word.getLemma().toLowerCase())
							|| names.contains(word.getContent().toLowerCase())) {
						numberOfNames++;
					}
				}
			}
		}

		numberOfOccurences = text.getNumberOfWords() - numberOfOccurences - numberOfNames;
		if (numberOfOccurences < 0) {
			return 0;
		}

		return numberOfOccurences;
	}
}
