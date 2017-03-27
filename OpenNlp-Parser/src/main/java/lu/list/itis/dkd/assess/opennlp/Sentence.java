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
package lu.list.itis.dkd.assess.opennlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.connectives.Connectives;
import lu.list.itis.dkd.assess.opennlp.connectives.questions.Question;
import lu.list.itis.dkd.assess.opennlp.density.IdeaDensityRater;
import lu.list.itis.dkd.assess.opennlp.density.Item;
import lu.list.itis.dkd.assess.opennlp.dependency.Dependency;
import lu.list.itis.dkd.assess.opennlp.references.References;
import lu.list.itis.dkd.assess.opennlp.util.HtmlHelper;
import lu.list.itis.dkd.assess.opennlp.util.StringHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type;
import lu.list.itis.dkd.assess.opennlp.util.Type.Font;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Sentence {
	private String content = "";
	private Language language;
	private String taggedSentence = "";
	private String lemmatizedSentence = "";
	private Type.List type = Type.List.DEFAULT;
	private int length = 0;
	private int numberOfLetters = 0;
	private int numberOfSyllables = 0;
	private int numberOfPropositions = 0;
	private int sentenceNumber = 0;
	private int paragraphSentenceNumber = 0;
	private List<Word> words = new ArrayList<>();
	private List<Item> propositions = new ArrayList<>();

	protected static final Logger logger = Logger.getLogger(Sentence.class.getSimpleName());

	public Sentence(String sentence, int paragraphSentenceNumber, Language language) {
		this.language = language;
		this.content = sentence;
		this.paragraphSentenceNumber = paragraphSentenceNumber;
		length = sentence.length();
		words.addAll(StringHelper.breakSentenceIntoWordsAndTags(sentence, language));
		createSentence();
	}

	public Sentence(List<Word> words, int paragraphSentenceNumber, Language language) {
		this.language = language;
		this.content = "";
		this.words.addAll(words);
		for (Word word : words) {
			this.content += word.getContent() + " ";
		}
		this.content = this.content.trim() + ".";
		this.paragraphSentenceNumber = paragraphSentenceNumber;
		length = content.length();
		createSentence();
	}

	public Sentence(String htmlSentence, int paragraphSentenceNumber, String type, Language language) {
		logger.info("New Sentence: " + htmlSentence);
		this.language = language;
		this.paragraphSentenceNumber = paragraphSentenceNumber;
        this.content = HtmlHelper.cleanHtml(HtmlHelper.removeTags(HtmlHelper.replaceFontTags(HtmlHelper.cleanHtml(htmlSentence))));
		setType(type);
		length = content.length();
		words.addAll(HtmlHelper.breakHtmlSentenceIntoWords(htmlSentence, language));
		// don't analyse if only one punctuation character
		if (words.size() > 0) {
			createSentence();
		}
	}

	private void createSentence() {
		findCharacteristics();
		IdeaDensityRater density = new IdeaDensityRater(taggedSentence, language);
		numberOfPropositions = density.getNumberOfpropositions();
		propositions = density.getPropositions();
	}

	// TODO Ask if this is how it should be interpreted
	private void setType(String type) {
		switch (type) {
		case "default@definition":
			this.type = Type.List.DEFINITION;
			break;
		case "default@ordered":
			this.type = Type.List.ORDERED;
			break;
		case "default@unordered":
			this.type = Type.List.UNORDERED;
			break;
		case "default@definition@ordered":
			this.type = Type.List.ORDEREDDEFINITION;
			break;
		case "default@definition@unordered":
			this.type = Type.List.UNORDEREDDEFINITION;
			break;
		case "default@ordered@unordered":
			this.type = Type.List.ORDERED;
			break;
		case "default@definition@ordered@unordered":
			this.type = Type.List.ORDEREDDEFINITION;
			break;
		default:
			break;
		}
	}

	private void findCharacteristics() {
		int nLetters = 0;
		int nSyllables = 0;

		Dependency dependency = new Dependency(this);
		List<String> relationTags = dependency.getRelationTags();
		List<Integer> relationNumbers = dependency.getRelationNumbers();

		for (Word word : words) {
			nLetters += word.getNumberOfLetters();
			nSyllables += word.getNumberOfSyllables();
			lemmatizedSentence += word.getLemma() + " ";
			taggedSentence += word.getContent() + "_" + word.getTag() + " ";
			if(!language.equals(Language.DE)){
				word.setRelationTag(relationTags.get(word.getSentenceWordNumber() - 1));
				word.setRelationTo(relationNumbers.get(word.getSentenceWordNumber() - 1));
			}	
		}

		char sentenceEnding = content.charAt(content.length() - 1);
		Character firstLemmaLetter = lemmatizedSentence.charAt(0);
		Character firstTagLetter = taggedSentence.charAt(0);

        //Add sentence ending to the lemmatized/tagged sentence and uppercase First word.
		lemmatizedSentence = lemmatizedSentence.trim() + sentenceEnding;
        lemmatizedSentence = Character.toUpperCase(firstLemmaLetter) + lemmatizedSentence.substring(1, lemmatizedSentence.length());

		taggedSentence = taggedSentence.trim() + sentenceEnding;
		taggedSentence = Character.toUpperCase(firstTagLetter) + taggedSentence.substring(1, taggedSentence.length());

		numberOfLetters = nLetters;
		numberOfSyllables = nSyllables;
	}

	public double getAverageSyllablesPerWord() {
		return (double) numberOfSyllables / (double) words.size();
	}

	public double getAverageWordLength() {
		return (double) numberOfLetters / (double) words.size();
	}

	public double getDensity() {
		return (double) numberOfPropositions / (double) words.size();
	}

	public String getContent() {
		return content;
	}

	public Language getLanguage() {
		return language;
	}

	public int getLength() {
		return length;
	}

	public List<Word> getWords() {
		return words;
	}

    //TODO Maybe change word number from 1 to 0.
	public Word getWord(int sentenceWordNumber) {
		if (sentenceWordNumber == 1) {
			return words.get(0);
        }
        else if (words.size() >= sentenceWordNumber) {
			return words.get(sentenceWordNumber - 1);
		}
		return null;
	}

	/**
	 * Generates a question out of the sentence.
     * @param qType: Why, What, ...
	 * @return
	 */
	public Question generateQuestionFromSentence(String qType) {
		return new Question(this, qType);
	}

	public int getNumberOfWords() {
		return words.size();
	}

	public int getNumberOfSyllables() {
		return numberOfSyllables;
	}

	public int getNumberOfPropositions() {
		return this.numberOfPropositions;
	}

	public int getNumberOfLetters() {
		return numberOfLetters;
	}

	public int getNumberOfReferences() {
		return References.getNumberOfReferences(this);
	}

	public String getTaggedSentence() {
		return taggedSentence;
	}

	public int getSentenceNumber() {
		return sentenceNumber;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setSentenceNumber(int pos) {
		this.sentenceNumber = pos;
	}

	public int getParagraphSentenceNumber() {
		return paragraphSentenceNumber;
	}

	/**
     * @return Returns a Map of connective types in the sentence.
     * Connective types are: 
     * CONTRAST, SIMILARITY, RESULT, EXAMPLIFICATION, LINKING, CAUSALITY, SEQUENCE, ORDER, 
     * PARTICULATION, EXPLANATION, EMPHASISING, CONCLUSION, CORRECTION, TIME, DISMISSIAL
	 */
	public Map<Type.Connective, String> getConnectiveTypes() {
		return Connectives.getConnectives(this);
	}

	public Type.List getType() {
		return type;
	}

	public String[] getTokenArray() {
		String[] tokens = new String[words.size()];
		for (int i = 0; i < words.size(); i++) {
			tokens[i] = words.get(i).getContent();
		}
		return tokens;
	}

	public List<Word> getFontWords(Font font) {
		List<Word> fontWords = new ArrayList<>();
		for (Word word : words) {
			if (word.getFont().equals(font)) {
				fontWords.add(word);
			}
		}
		return fontWords;
	}

	public List<Word> getAllButLastWord() {
		return words.subList(0, words.size() - 1);
	}

	/**
     * Method to print a sentence with a list of words.
     * Warning: At least 2 words are needed to form a sentence!
	 * @param words
	 * @return A sentence with a point at the end.
	 */
	public String getSentence(List<Word> words) {
		String sentence = "";
		if (words.size() > 2) {
			char firstLetter = words.get(0).getContent().charAt(0);

			for (Word word : words) {
				sentence += word.getContent() + " ";
			}

			sentence = sentence.trim();
			return firstLetter + sentence.substring(1, sentence.length()) + ".";
        }
        else {
            logger.log(Level.SEVERE, "Sentence could not be build because the number of words in the list are equal or less than 2. ");
			return sentence;
		}
	}

	public String getLemmatizedSentence() {
		return lemmatizedSentence;
	}

	@Override
	public String toString() {
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

		if (that instanceof Sentence) {
			return hashCode() == ((Sentence) that).hashCode();
		}
		return false;
	}

	public List<Item> getPropositions() {
		return propositions;
	}
}
