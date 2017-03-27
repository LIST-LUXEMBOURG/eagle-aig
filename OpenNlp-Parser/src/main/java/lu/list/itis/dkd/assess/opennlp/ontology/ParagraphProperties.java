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
package lu.list.itis.dkd.assess.opennlp.ontology;

import com.hp.hpl.jena.ontology.OntProperty;

import lu.list.itis.dkd.assess.opennlp.util.OntologyHelper;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 2.1.1
 */
public class ParagraphProperties {
    private OntProperty title;
    private OntProperty language;
    private OntProperty content;
    private OntProperty number;
    private OntProperty lemma;
    private OntProperty tag;
    
    private OntProperty level;
    
    private OntProperty avarageSentenceLength;
    private OntProperty averageWordLength;
    private OntProperty averageSyllablesPerWord;
    
    private OntProperty numberOfSentences;
    private OntProperty numberOfWords;
    private OntProperty numberOfSyllables;
    private OntProperty numberOfLetters;
    private OntProperty numberOfReferences;
    private OntProperty numberOfIdentifications;
    private OntProperty numberOfConnectives;
    
    private OntProperty hasSentence;
    private OntProperty hasWord;
    
    public ParagraphProperties(OntologyHelper onto, String namespace) {
        title = onto.createProperty("title", namespace);
        language = onto.createProperty("language", namespace);
        content = onto.createProperty("content", namespace);
        number = onto.createProperty("paragraphNumber", namespace);
        lemma = onto.createProperty("lemma", namespace);
        tag = onto.createProperty("tag", namespace);
        level = onto.createProperty("level", namespace);
        
        avarageSentenceLength = onto.createProperty("averageSentenceLength", namespace);
        averageWordLength = onto.createProperty("averageWordLength", namespace);
        averageSyllablesPerWord = onto.createProperty("averageSyllablesPerWord", namespace);
        
        numberOfSentences = onto.createProperty("numberOfSentences", namespace);
        numberOfWords = onto.createProperty("numberOfWords", namespace);
        numberOfSyllables = onto.createProperty("numberOfSyllables", namespace);
        numberOfLetters = onto.createProperty("numberOfLetters", namespace);
        numberOfReferences = onto.createProperty("numberOfReferences", namespace);
        numberOfIdentifications = onto.createProperty("numberOfIdentifications", namespace);
        numberOfConnectives = onto.createProperty("numberOfConnectives", namespace);
        
        hasSentence = onto.createProperty("hasSentence", namespace);
        hasWord = onto.createProperty("hasWord", namespace);
    }

    public OntProperty getTitle() {
        return title;
    }

    public OntProperty getLanguage() {
        return language;
    }

    public OntProperty getContent() {
        return content;
    }

    public OntProperty getParagraphNumber() {
        return number;
    }

    public OntProperty getLemma() {
        return lemma;
    }

    public OntProperty getTag() {
        return tag;
    }

    public OntProperty getLevel() {
        return level;
    }

    public OntProperty getAvarageSentenceLength() {
        return avarageSentenceLength;
    }

    public OntProperty getAverageWordLength() {
        return averageWordLength;
    }

    public OntProperty getAverageSyllablesPerWord() {
        return averageSyllablesPerWord;
    }

    public OntProperty getNumberOfSentences() {
        return numberOfSentences;
    }

    public OntProperty getNumberOfWords() {
        return numberOfWords;
    }

    public OntProperty getNumberOfSyllables() {
        return numberOfSyllables;
    }

    public OntProperty getNumberOfLetters() {
        return numberOfLetters;
    }
    
    public OntProperty getNumberOfReferences() {
        return numberOfReferences;
    }
    
    public OntProperty getNumberOfIdentifications() {
        return numberOfIdentifications;
    }
    
    public OntProperty getNumberOfConnectives() {
        return numberOfConnectives;
    }

    public OntProperty getHasSentence() {
        return hasSentence;
    }

    public OntProperty getHasWord() {
        return hasWord;
    }
}
