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
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.identification.Identification;
import lu.list.itis.dkd.assess.opennlp.util.HtmlHelper;
import lu.list.itis.dkd.assess.opennlp.util.StringHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.util.Type.Level;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Paragraph {
    private String content = "";
    private String taggedParagraph = "";
    private String lemmaParagraph = "";
    private Language language;
    private Level level = Level.I;
    private String title = "";
    private int numberOfSyllables = 0;
    private int numberOfSentences = 0;
    private int numberOfWords = 0;
    private int numberOfLetters = 0;
    private int numberOfPropositions = 0;
    private int numberOfReferences = 0;
    private int numberOfIdentifications = 0;
    private int numberOfConnectives = 0;
    private int paragraphNumber;
    private List<Sentence> sentences = new ArrayList<>();
    private List<String> tokenizedLemmaParagraph = new ArrayList<>();
    private List<String> tokenizedParagraph = new ArrayList<>();

    protected static final Logger logger = Logger.getLogger(Paragraph.class.getName());

    public Paragraph(String plainText, int paragraphNumber, Language language) {
        this.content = plainText;
        this.paragraphNumber = paragraphNumber;
        this.language = language;
        sentences.addAll(StringHelper.breakTextIntoSentences(content, language));
        createParagraph();
    }

    public Paragraph(String htmlParagraph, String title, int paragraphNumber, Language language) {
        logger.info("New  Paragraph: "+htmlParagraph);
        this.content = HtmlHelper.removeTags(HtmlHelper.cleanHtml(htmlParagraph));
        this.paragraphNumber = paragraphNumber;
        this.language = language;
        setTitle(title);
        sentences.addAll(HtmlHelper.breakTaggedHTMLParagraphIntoSentences(htmlParagraph, language));    
        createParagraph();
    }
    
    private void createParagraph(){
        numberOfSentences = sentences.size();
        findCharacteristics();
    }

    private void findCharacteristics() {
        int nLetters = 0;
        int nSyllables = 0;
        int nWords = 0;
        int nPropositions = 0;
        int nReferences = 0;
        int nConnectives = 0;

        int wordPos = 1;
        for (Sentence sentence : sentences) {
            nLetters += sentence.getNumberOfLetters();
            nSyllables += sentence.getNumberOfSyllables();
            nWords += sentence.getNumberOfWords();
            nPropositions += sentence.getNumberOfPropositions();
            nReferences += sentence.getNumberOfReferences();
            nConnectives += sentence.getConnectiveTypes().size();

            for (Word word : sentence.getWords()) {
                tokenizedParagraph.add(word.getContent());
                tokenizedLemmaParagraph.add(word.getLemma());
                word.setParagraphWordNumber(wordPos);
                wordPos++;
            }

            lemmaParagraph += sentence.getLemmatizedSentence() + " ";
            taggedParagraph += sentence.getTaggedSentence() + " ";
        }
        //Remove last space
        lemmaParagraph = lemmaParagraph.trim();
        taggedParagraph = taggedParagraph.trim();

        numberOfSentences = sentences.size();
        numberOfWords = nWords;
        numberOfSyllables = nSyllables;
        numberOfLetters = nLetters;
        numberOfPropositions = nPropositions;
        numberOfReferences = nReferences;
        numberOfIdentifications = Identification.getNumberOfIdentification(sentences);
        numberOfConnectives = nConnectives;
    }

    public double getAverageSentenceLength() {
        return (double) numberOfWords / (double) numberOfSentences;
    }

    public double getAverageSyllablesPerWord() {
        return (double) numberOfSyllables / (double) numberOfWords;
    }

    public double getAverageWordLength() {
        return (double) numberOfLetters / (double) numberOfWords;
    }

    public double getDensity() {
        return (double) numberOfPropositions / (double) numberOfWords;
    }

    public Language getLanguage() {
        return language;
    }

    public String getLemmaParagraph() {
        return lemmaParagraph.trim();
    }
    
    public Level getLevel(){
        return this.level;
    }

    //TODO Pretty Print for getListSentences
    public List<Sentence> getListSentences(Type.List type) {
        List<Sentence> listSentences = new ArrayList<>();
        for (Sentence sentence : sentences) {
            if (sentence.getType() == type) {
                listSentences.add(sentence);
            }
        }
        return listSentences;
    }
    
    public int getNumberOfIdentifications() {
        return numberOfIdentifications;
    }

    public int getNumberOfLetters() {
        return numberOfLetters;
    }

    public int getNumberOfPropositions() {
        return numberOfPropositions;
    }
    
    public int getNumberOfReferences(){
        return numberOfReferences;
    }

    public int getNumberOfSentences() {
        return numberOfSentences;
    }

    public int getNumberOfSyllables() {
        return numberOfSyllables;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public String getContent() {
        return content.trim();
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }
    
    public int getNumberOfConnectives(){
        return numberOfConnectives;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public String getTaggedParagraph() {
        return taggedParagraph.trim();
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTokenizedLemmaParagraph() {
        return tokenizedLemmaParagraph;
    }

    public List<String> getTokenizedParagraph() {
        return tokenizedParagraph;
    }

    public void setTitle(String title) {
        if (title.contains("<h1>")) {
            this.level = Level.I;
            this.title = this.title.replaceAll("<h1>", "").replaceAll("</h1>", "");
        }
        if (title.contains("<h2>")) {
            this.level = Level.II;
            this.title = this.title.replaceAll("<h2>", "").replaceAll("</h2>", "");
        }
        else if (title.contains("<h3>")) {
            this.level = Level.III;
            this.title = this.title.replaceAll("<h3>", "").replaceAll("</h3>", "");
        }
        else if (title.contains("<h4>")) {
            this.level = Level.IIII;
            this.title = this.title.replaceAll("<h4>", "").replaceAll("</h4>", "");
        }
        else {
            this.title = title;
        }
    }
    
    public void setLevel(Level level){
        this.level = level;
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

        if (that instanceof Paragraph) {
            return hashCode() == ((Paragraph) that).hashCode();
        }
        return false;
    }
}
