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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.connectives.questions.Question;
import lu.list.itis.dkd.assess.opennlp.connectives.questions.Questions;
import lu.list.itis.dkd.assess.opennlp.difficulty.Matrix;
import lu.list.itis.dkd.assess.opennlp.util.HtmlHelper;
import lu.list.itis.dkd.assess.opennlp.util.LanguageHelper;
import lu.list.itis.dkd.assess.opennlp.util.StringHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type;
import lu.list.itis.dkd.assess.opennlp.util.Type.DifficultyLevel;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.util.Type.Level;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Text {
	private Language language;
    private String content = "";
    private String taggedText = "";
    private String lemmaText = "";
    private String title = "";
    private int numberOfSyllables = 0;
    private int numberOfSentences = 0;
    private int numberOfWords = 0;
    private int numberOfLetters = 0;
    private int numberOfPropositions = 0;
    private int numberOfReferences = 0;
    private int numberOfIdentifications = 0;
    private int numberOfConnectives = 0;
    private DifficultyLevel difficulty;
    private List<Paragraph> paragraphs = new ArrayList<>();
    private List<Sentence> sentences = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private List<String> tokenizedText = new ArrayList<>();
    
    protected static final Logger logger = Logger.getLogger(Text.class.getSimpleName());
    
    //TODO Documentation of all public functions!
    //TODO Recognise language automatically!
    
    /**
     * Creates a text object and use a statisitcal approach to guess the language of the Text.
     * @param plainText
     */
    public Text(String plainText) {
        this.language = LanguageHelper.detectLanguage(plainText);
        this.content = plainText;
        Text.logger.info("In a String, a Paragraph may not be considered. Hence, the whole text is maybe considered as one paragraph. If you want to have paragraph information, load the plain text document and use Text(File) or Text(Document).");
        
        Paragraph paragraph = new Paragraph(plainText, 1, language);
        paragraphs.add(paragraph);
        findCharacteristics();
    }
    
    public Text(String plainText, Language language)  {
        this.language = language;
        this.content = plainText;
        Text.logger.info("In a String, a Paragraph may not be considered. Hence, the whole text is maybe considered as one paragraph. If you want to have paragraph information, load the plain text document and use Text(File) or Text(Document).");
        
        Paragraph paragraph = new Paragraph(plainText, 1, language);
        paragraphs.add(paragraph);
        findCharacteristics();
    }

    public Text(InputStream is, Language language) throws FileNotFoundException {
        this.language = language;        
        content = Wrapper.loadTextFile(is);    
        Text.logger.info("In a File, fonts can not be considered. If you want to consider fonts, use Text(Document).");
        
        paragraphs.addAll(StringHelper.breakTextIntoParagraphs(content, language));
        findCharacteristics();
    }
    
    public Text(String pageSource, String bodyClassName, Language language) {
        this.language = language;
        paragraphs.addAll(HtmlHelper.breakHtmlTextIntoParagraph(pageSource, bodyClassName, language));
        //TODO Find text heading.
        
        findCharacteristics();
        for (Sentence sentence : sentences) {
            content += sentence.getContent() + " ";
        }
        //Remove last space
        content = content.trim();
    }
    
    private void findCharacteristics()  {
        for (Paragraph paragraph : paragraphs) {
            sentences.addAll(paragraph.getSentences());
            numberOfSentences += paragraph.getNumberOfSentences();
            numberOfWords += paragraph.getNumberOfWords();
            numberOfSyllables += paragraph.getNumberOfSyllables();
            numberOfLetters += paragraph.getNumberOfLetters();
            numberOfPropositions += paragraph.getNumberOfPropositions();
            numberOfReferences += paragraph.getNumberOfReferences();
            numberOfIdentifications += paragraph.getNumberOfIdentifications();
            numberOfConnectives += paragraph.getNumberOfConnectives();
            tokenizedText.addAll(paragraph.getTokenizedParagraph());
            lemmaText += paragraph.getLemmaParagraph() + " ";
            taggedText += paragraph.getTaggedParagraph() + " ";
        }
        
        //Remove last space
        lemmaText = lemmaText.trim();
        taggedText = taggedText.trim();
        
        int sentencePos = 1;
        int wordPos = 1;
        for (Sentence sentence : sentences) {
            sentence.setSentenceNumber(sentencePos);
            sentencePos++;
            
            for (Word word : sentence.getWords()) {
                word.setTextWordNumber(wordPos);
                wordPos++;
            }
        }
        
        //Compute Text Difficulty
        Matrix matrix = new Matrix(this);
        this.difficulty = matrix.getDifficulty();
        
        //Generate text questions based on connectives.
        Questions queries = new Questions(this);
        this.questions = queries.getQuestions();
    }   

    public DifficultyLevel getDifficulty(){
        return difficulty;
    }
    
    public double getDensity() {
        return numberOfPropositions / numberOfWords;
    }

    public int getNumberOfPropositions() {
        return numberOfPropositions;
    }

    public int getNumberOfSyllables() {
        return numberOfSyllables;
    }

    public double getAverageSyllablesPerWord() {
        return (double) numberOfSyllables / (double) numberOfWords;
    }

    public double getAveragePropositions() {
        return (double) numberOfPropositions / (double) numberOfSentences;
    }

    public double getAverageSentenceLength() {
        return (double) numberOfWords / (double) numberOfSentences;
    }

    public double getAverageWordLength() {
        return (double) numberOfLetters / (double) numberOfWords;
    }

    public Language getLanguage() {
        return language;
    }

    public int getNumberOfSentences() {
        return numberOfSentences;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }
    
    public int getNumberOfIdentifications() {
        return numberOfIdentifications;
    }
    
    public int getNumberOfConnectives(){
        return numberOfConnectives;
    }

    public int getNumberOfLetters() {
        return numberOfLetters;
    }

    public int getNumberOfReferences() {
        return numberOfReferences;
    }
    
    public List<Sentence> getSentences() {
        return sentences;
    }

    public String getTaggedText() {
        return taggedText.trim();
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public String getContent() {
        return content.trim();
    }
    
    /**
     * Returns the text without any commas, brackets, ...
     * @return
     */
    public String getCleanedContent(){
        String cleanedText = "";
        for (Sentence sentence : sentences) {
            char sentenceEnding = sentence.getContent().charAt(sentence.getLength()-1);
            for (Word word : sentence.getWords()) {
                cleanedText += word.getContent() + " ";
            }
            cleanedText += sentenceEnding + " ";
        }
        return cleanedText.trim();
    }

    public String getLemmaText() {
        return lemmaText.trim();
    }

    public List<String> getTokenizedText() {
        return tokenizedText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Returns the paragraph if it exists otherwise null. This means the paragraph 0 does not exist!
     * E.g. paragraph number 1 returns the first paragraph.
     * @param number
     * @return
     */
    public Paragraph getParagraph(int paragraphNumber) {
        if (paragraphNumber == 0) {
            return null;
        }
        else if (paragraphNumber == 1) {
            return paragraphs.get(0);
        }
        else if (paragraphs.size() >= paragraphNumber) {
            return paragraphs.get(paragraphNumber-1);
        }
        return null;
    }
    
    /**
     * Returns a paragraph or sub paragraph if it exists.
     * @param level: I, II, III, IIII
     * @return
     */
    public Paragraph getParagraph(Level level) {
        for (Paragraph paragraph : paragraphs) {
            if (paragraph.getLevel() == level) {
                return paragraph;
            }
            else {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Returns a list of questions based on connectives.
     */
    public List<Question> getQuestions(){
        return questions;
    }

    /**
     * Returns the sentences that contain a list type.
     * @param List type: DEFAULT, ORDERED, UNORDERED, DEFINITION, ORDEREDDEFINITION, UNORDEREDDEFINITION
     * @return
     */
    public List<Sentence> getListSentences(Type.List type) {
        List<Sentence> listSentences = new ArrayList<>();
        for (Sentence sentence : sentences) {
            if (sentence.getType() == type) {
                listSentences.add(sentence);
            }
        }
        return listSentences;
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

        if (that instanceof Text) {
            return hashCode() == ((Text) that).hashCode();
        }
        return false;
    }
}
