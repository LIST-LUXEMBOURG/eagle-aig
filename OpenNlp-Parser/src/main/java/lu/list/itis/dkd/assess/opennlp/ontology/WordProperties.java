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
public class WordProperties {
    OntProperty language;
    OntProperty content;
    OntProperty sentenceWordNumber;
    OntProperty paragraphWordNumber;
    OntProperty textWordNumber;
    OntProperty lemma;
    OntProperty tag;
    OntProperty stem;
    OntProperty soundex;
    OntProperty font;
    OntProperty length;  
    OntProperty numberOfSyllables;
    OntProperty numberOfLetters;
    OntProperty isReference;
    OntProperty isRare;
    
    public WordProperties (OntologyHelper onto, String namespace) {
        language = onto.createProperty("language", namespace);
        content = onto.createProperty("content", namespace);
        sentenceWordNumber = onto.createProperty("wordNumberInSentence", namespace);
        paragraphWordNumber = onto.createProperty("wordNumberInParagraph", namespace);
        textWordNumber = onto.createProperty("wordNumberInText", namespace);
        lemma = onto.createProperty("lemma", namespace);
        tag = onto.createProperty("tag", namespace);
        stem = onto.createProperty("stem", namespace);
        soundex = onto.createProperty("soundex", namespace);
        font = onto.createProperty("font", namespace);
        numberOfSyllables = onto.createProperty("numberOfSyllables", namespace);
        numberOfLetters = onto.createProperty("numberOfLetters", namespace);
        length = onto.createProperty("length", namespace);
        isReference = onto.createProperty("reference", namespace);
        isRare = onto.createProperty("rare", namespace);
    }

    public OntProperty getLanguage() {
        return language;
    }

    public OntProperty getContent() {
        return content;
    }

    public OntProperty getSentenceWordNumber() {
        return sentenceWordNumber;
    }

    public OntProperty getParagraphWordNumber() {
        return paragraphWordNumber;
    }

    public OntProperty getTextWordNumber() {
        return textWordNumber;
    }

    public OntProperty getLemma() {
        return lemma;
    }

    public OntProperty getTag() {
        return tag;
    }

    public OntProperty getFont() {
        return font;
    }

    public OntProperty getNumberOfSyllables() {
        return numberOfSyllables;
    }

    public OntProperty getNumberOfLetters() {
        return numberOfLetters;
    }

    public OntProperty getStem() {
        return stem;
    }

    public OntProperty getSoundex() {
        return soundex;
    }

    public OntProperty getLength() {
        return length;
    }
    
    public OntProperty isReference() {
        return isReference;
    }
    
    public OntProperty isRare() {
        return isRare;
    }
}
