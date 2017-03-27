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
public class SentenceProperties {
    private OntProperty language;
    private OntProperty content;
    private OntProperty length;
    private OntProperty sentenceNumberInParagraph;
    private OntProperty sentenceNumberInText;
    private OntProperty tag;
    private OntProperty lemma;
    
    private OntProperty type;
    
    private OntProperty averageWordLength;
    private OntProperty averageSyllablesPerWord;
    
    private OntProperty numberOfWords;
    private OntProperty numberOfSyllables;
    private OntProperty numberOfLetters;
    private OntProperty numberOfReferences;
    
    private OntProperty hasWord;
    private OntProperty hasConnective;
    private OntProperty partOfParagraph;
    
    public SentenceProperties (OntologyHelper onto, String namespace) {
        language = onto.createProperty("language", namespace);
        content = onto.createProperty("content", namespace);
        length = onto.createProperty("length", namespace);
        sentenceNumberInParagraph = onto.createProperty("sentenceNumberInParagraph", namespace);
        sentenceNumberInText = onto.createProperty("sentenceNumberInText", namespace);
        tag = onto.createProperty("tag", namespace);
        lemma = onto.createProperty("lemma", namespace);
        type = onto.createProperty("type", namespace);
        
        averageWordLength = onto.createProperty("averageWordLength", namespace);
        averageSyllablesPerWord = onto.createProperty("averageSyllablesPerWord", namespace);
        
        numberOfWords = onto.createProperty("numberOfWords", namespace);
        numberOfSyllables = onto.createProperty("numberOfSyllables", namespace);
        numberOfLetters = onto.createProperty("numberOfLetters", namespace);
        numberOfReferences = onto.createProperty("numberOfReferences", namespace);
        
        hasWord = onto.createProperty("hasWord", namespace);
        hasConnective = onto.createProperty("hasConnective", namespace);
        partOfParagraph = onto.createProperty("partOfParagraph", namespace);
    }

    public OntProperty getLanguage() {
        return language;
    }

    public OntProperty getContent() {
        return content;
    }

    public OntProperty getLength() {
        return length;
    }

    public OntProperty getSentenceNumberInParagraph() {
        return sentenceNumberInParagraph;
    }

    public OntProperty getSentenceNumberInText() {
        return sentenceNumberInText;
    }

    public OntProperty getTag() {
        return tag;
    }

    public OntProperty getLemma() {
        return lemma;
    }

    public OntProperty getType() {
        return type;
    }

    public OntProperty getNumberOfWords() {
        return numberOfWords;
    }
    
    public OntProperty getNumberOfReferences() {
        return numberOfReferences;
    }

    public OntProperty getNumberOfSyllables() {
        return numberOfSyllables;
    }

    public OntProperty getNumberOfLetters() {
        return numberOfLetters;
    }

    public OntProperty getHasWord() {
        return hasWord;
    }

    public OntProperty getHasConnective() {
        return hasConnective;
    }

    public OntProperty getPartOfParagraph() {
        return partOfParagraph;
    }

    public OntProperty getAverageWordLength() {
        return averageWordLength;
    }

    public OntProperty getAverageSyllablesPerWord() {
        return averageSyllablesPerWord;
    }
}
