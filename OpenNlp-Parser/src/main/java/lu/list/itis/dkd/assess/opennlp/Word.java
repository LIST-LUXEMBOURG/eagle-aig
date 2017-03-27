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

import java.util.logging.Logger;
import lu.list.itis.dkd.assess.opennlp.lemmatizer.Lemmatizer;
import lu.list.itis.dkd.assess.opennlp.references.References;
import lu.list.itis.dkd.assess.opennlp.soundex.Soundex;
import lu.list.itis.dkd.assess.opennlp.syllibification.Syllabification;
import lu.list.itis.dkd.assess.opennlp.util.DictionaryLoader;
import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.ModelLoader;
import lu.list.itis.dkd.assess.opennlp.util.Type.Font;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.stemmer.Stemmer;
import opennlp.tools.postag.POSTaggerME;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Word {
    private String content = "";
    private String tag = "";
    private String relationTag = "";
    private Font font = Font.DEFAULT;
    private Language language;
    private int sentenceWordNumber = 1;
    private int paragraphWordNumber = 1;
    private int textWordNumber = 1;
    private int relatedTo = 0;

    protected static final Logger logger = Logger.getLogger(Word.class.getName());
    
    public Word(String word, String tag, Font font, int wordNumber, Language language) {
        this.content = word;
        this.sentenceWordNumber = wordNumber;
        this.tag = tag;
        this.font = font;
        this.language = language;
    }

    @SuppressWarnings("deprecation")
    public Word(String word, int sentenceWordNumber, Language language) {
        this.content = word;
        this.sentenceWordNumber = sentenceWordNumber;
        this.language = language;

        POSTaggerME posTagger = ModelLoader.getPosModel(language);
        tag = posTagger.tag(content);
        tag = tag.substring(tag.indexOf("/")+1, tag.length());
    }

    public Word(String word, String tag, int sentenceWordNumber, Language language) {
        this.content = word;
        this.sentenceWordNumber = sentenceWordNumber;
        this.tag = tag;
        this.language = language;
    }

    public double getNumberOfSyllables() {
        return Syllabification.getNumberOfSyllables(this);
    }

    public String getContent() {
        return content.trim();
    }
    
    public double getLength(){
        return content.length();
    }

    public Language getLanguage() {
        return language;
    }

    public double getNumberOfLetters() {
        return content.length();
    }

    public String getTag() {
        return tag;
    }
    
    public String getRelationTag() {
        return relationTag;
    }
    
    public void setRelationTag(String relation) {
        this.relationTag = relation;
    }

    public String getLemma() {
        return Lemmatizer.getLemma(this);
    }

    public Font getFont() {
        return font;
    }

    public int getSentenceWordNumber() {
        return sentenceWordNumber;
    }
    
    public void setParagraphWordNumber(int number) {
        this.paragraphWordNumber = number;
    }
    
    public int getParagraphWordNumber(){
        return paragraphWordNumber;
    }
    
    public void setTextWordNumber(int number) {
        this.textWordNumber = number;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getTextWordNumber(){
        return textWordNumber;
    }
    
    public boolean isReference(){
        return References.isReference(this);
    }
    
    /**
     * @return Soundex is a phonetic algorithm for indexing names by sound.
     */
    public String getSoundex() {
        return Soundex.getSoundex(this);
    }

    /**
     * @return The Stem of the word of the word itself if the stem does not exist.
     */
    public String getStem(){
        return Stemmer.getStem(this);
    }
    
    /**
     * @return True if the word is a verb or false otherwise.
     */
    public boolean isVerb() {
        return GrammarHelper.isVerb(this);
    }
    
    public int getRelationTo() {
        return relatedTo;
    }
    
    public void setRelationTo(int relatedWordNumber){
        this.relatedTo = relatedWordNumber;
    }
    
    /**
     * @return True if the word is not contained in a frequent word list and false otherwise.
     */
    public boolean isRare(){
        if (language.equals(Language.FR)){
            String newcontent = content.toLowerCase();
            newcontent = newcontent.replace("d’", "").trim();
            if (DictionaryLoader.getWordList(language).contains(newcontent.toLowerCase())){
                return false;
            }
        }
        else if (DictionaryLoader.getWordList(language).contains(content.toLowerCase())){
            return false;
        }
        return true;
    }
    
    /**
     * @return True if the word is an adjective or false otherwise.
     */
    public boolean isAdjective(){
        return GrammarHelper.isAdj(this);
    }
    
    /**
     * @return True if the word is an adverb or false otherwise.
     */
    public boolean isAdverb(){
        return GrammarHelper.isAdv(this);
    }
    
    /**
     * @return True if the word is a noun or false otherwise.
     */
    public boolean isNoun() {
        return GrammarHelper.isNoun(this);
    }
    
    /**
     * @return True if the word is a number or false otherwise.
     */
    public boolean isNumber() {
        return GrammarHelper.isNumber(this);
    }
    
    /**
     * @return True if the word is a plural noun or false otherwise.
     */
    public boolean isPluralNoun() {
        if (isNoun()) {
            return GrammarHelper.isPlural(this);
        }
        return false;        
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

        if (that instanceof Word) {
            return hashCode() == ((Word) that).hashCode();
        }
        return false;
    }
}
