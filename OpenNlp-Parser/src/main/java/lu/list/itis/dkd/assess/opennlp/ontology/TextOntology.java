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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import lu.list.itis.dkd.assess.opennlp.Paragraph;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Summary;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.OntologyHelper;
import lu.list.itis.dkd.assess.opennlp.util.StringHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type;
import lu.list.itis.dkd.assess.opennlp.util.Type.Connective;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 2.1.1
 */
public class TextOntology {
	private static final String namespace = "http://www.list.lu/list-parser#";
	private String language;
	private String name = "ListText";
	private OntologyHelper onto;
	
    protected static final Logger logger = Logger.getLogger(TextOntology.class.getSimpleName());
    
    //TODO Underlined: Important concepts
    //TODO Bold: Definitions
    //TODO Italic: Examples of important concepts
	
    /**
     * Creates an ontology object with a Text object with a default name "ListText".
     * @param text
     */
	public TextOntology(Text text) {	
		switch (text.getLanguage()) {
            case FR:
                this.language = "fr";
                break;
            case DE:
                this.language = "de";
                break;
            default:
                this.language = "en";
                break;
        }
        this.onto = build(text, this.name);
	}
	
	/**
     * Creates an ontology object with a Text object with the name given.
	 * @param text
	 * @param name
	 */
	public TextOntology(Text text, String name) {   
	    this.name = name;
        switch (text.getLanguage()) {
            case FR:
                this.language = "fr";
                break;
            case DE:
                this.language = "de";
                break;
            default:
                this.language = "en";
                break;
        }
        this.onto = build(text, name);
    }
		
	private OntologyHelper build(Text text, String name) {		   
        List<String> individualNames = new ArrayList<>();
	    OntologyHelper onto = new OntologyHelper(namespace);
        onto.init(name);
			
		//Create Jena Classes
        OntClass ontSyntax = onto.createClass("Syntax");
		OntClass ontText = onto.createClass("Text");
		OntClass ontParagraph = onto.createClass("Paragraph");
		OntClass ontSentence = onto.createClass("Sentence");
		OntClass ontWord = onto.createClass("Word");
		OntClass ontConnective = onto.createClass("Connective");	
		
		//Create Jena Properties
		TextProperties textProperties = new TextProperties(onto, namespace);
		ParagraphProperties paragraphProperties = new ParagraphProperties(onto, namespace);
		SentenceProperties sentenceProperties = new SentenceProperties(onto, namespace);
		WordProperties wordProperties = new WordProperties(onto, namespace);
		ConnectiveProperties connectiveProperties = new ConnectiveProperties(onto, namespace);
		
		//Create Jena Syntax Indivudual
		ontSyntax.addSubClass(ontText);
		ontSyntax.addSubClass(ontParagraph);
		ontSyntax.addSubClass(ontSentence);
		ontSyntax.addSubClass(ontWord);
		ontSyntax.addSubClass(ontConnective);
		
        //Create Jena Text Individual and add properties to it.
		individualNames.add("TEXT");
        Individual individualText = onto.createIndividual("TEXT", ontText);
        onto.addProperty(individualText, textProperties.getTitle(), text.getTitle(), language);
        onto.addProperty(individualText, textProperties.getLanguage(), language, language);
        onto.addProperty(individualText, textProperties.getContent(), text.getContent(), language);
        onto.addProperty(individualText, textProperties.getLemma(), text.getLemmaText(), language);
        onto.addProperty(individualText, textProperties.getTag(), text.getTaggedText(), language);
        
        onto.addProperty(individualText, textProperties.getAvarageSentenceLength(), StringHelper.convert(text.getAverageSentenceLength()), language);
        onto.addProperty(individualText, textProperties.getAverageWordLength(), StringHelper.convert(text.getAverageWordLength()), language);
        onto.addProperty(individualText, textProperties.getAverageSyllablesPerWord(), StringHelper.convert(text.getAverageSyllablesPerWord()), language);
        onto.addProperty(individualText, textProperties.getNumberOfSentences(), StringHelper.convert(text.getNumberOfSentences()), language);
        onto.addProperty(individualText, textProperties.getNumberOfWords(), StringHelper.convert(text.getNumberOfWords()), language);
        onto.addProperty(individualText, textProperties.getNumberOfSyllables(), StringHelper.convert(text.getNumberOfSyllables()), language);
        onto.addProperty(individualText, textProperties.getNumberOfLetters(), StringHelper.convert(text.getNumberOfLetters()), language);
        onto.addProperty(individualText, textProperties.getNumberOfReferences(), StringHelper.convert(text.getNumberOfReferences()), language);
        onto.addProperty(individualText, textProperties.getNumberOfIdentifications(), StringHelper.convert(text.getNumberOfIdentifications()), language);
        onto.addProperty(individualText, textProperties.getNumberOfConnectives(), StringHelper.convert(text.getNumberOfConnectives()), language);
        
        Summary summary = new Summary(text, (int)Math.round(text.getNumberOfSentences() / 2), true);
        String outline = "";
        for (Sentence sentence : summary.getSentences()) {
            outline += sentence.getContent() + " ";
        }
        onto.addProperty(individualText, textProperties.getSummary(), outline.trim(), language);

        int numberOfConnectives = 1;
        //Create Jena Paragraph Individual and add properties to it.
        for (Paragraph paragraph : text.getParagraphs()) {
            individualNames.add("PARAGRAPH" + paragraph.getParagraphNumber());
            Individual individualParagraph = onto.createIndividual("PARAGRAPH" + paragraph.getParagraphNumber(), ontParagraph);
            onto.addProperty(individualParagraph, paragraphProperties.getTitle(), paragraph.getTitle(), language);
            onto.addProperty(individualParagraph, paragraphProperties.getLanguage(), language, language);
            onto.addProperty(individualParagraph, paragraphProperties.getContent(), paragraph.getContent(), language);
            onto.addProperty(individualParagraph, paragraphProperties.getLemma(), paragraph.getLemmaParagraph(), language);
            onto.addProperty(individualParagraph, paragraphProperties.getTag(), paragraph.getTaggedParagraph(), language);
            onto.addProperty(individualParagraph, paragraphProperties.getLevel(), paragraph.getLevel().toString(), language);

            onto.addProperty(individualParagraph, paragraphProperties.getParagraphNumber(), StringHelper.convert(paragraph.getParagraphNumber()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getAvarageSentenceLength(), StringHelper.convert(paragraph.getAverageSentenceLength()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getAverageWordLength(), StringHelper.convert(paragraph.getAverageWordLength()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getAverageSyllablesPerWord(), StringHelper.convert(paragraph.getAverageSyllablesPerWord()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getNumberOfSentences(), StringHelper.convert(paragraph.getNumberOfSentences()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getNumberOfWords(), StringHelper.convert(paragraph.getNumberOfWords()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getNumberOfSyllables(), StringHelper.convert(paragraph.getNumberOfSyllables()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getNumberOfLetters(), StringHelper.convert(paragraph.getNumberOfLetters()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getNumberOfReferences(), StringHelper.convert(paragraph.getNumberOfReferences()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getNumberOfIdentifications(), StringHelper.convert(paragraph.getNumberOfIdentifications()), language);
            onto.addProperty(individualParagraph, paragraphProperties.getNumberOfConnectives(), StringHelper.convert(paragraph.getNumberOfConnectives()), language);
            
            //Create Jena objectProperty and link the text Individual to the paragraph(s) individuals.
            onto.createObecjtProperty(individualText, individualParagraph, textProperties.getHasParagraph());
            
            //Create Jena Sentence Individual and add properties to it.
            for (Sentence sentence : paragraph.getSentences()) {
                individualNames.add("SENTENCE" + sentence.getSentenceNumber());
                Individual individualSentence = onto.createIndividual("SENTENCE" + sentence.getSentenceNumber(), ontSentence);
                onto.addProperty(individualSentence, sentenceProperties.getLanguage(), language, language);
                onto.addProperty(individualSentence, sentenceProperties.getContent(), sentence.getContent(), language);
                onto.addProperty(individualSentence, sentenceProperties.getTag(), sentence.getTaggedSentence(), language);
                onto.addProperty(individualSentence, sentenceProperties.getLemma(), sentence.getLemmatizedSentence(), language);
                onto.addProperty(individualSentence, sentenceProperties.getLength(), StringHelper.convert(sentence.getLength()), language);
                onto.addProperty(individualSentence, sentenceProperties.getType(), sentence.getType().toString(), language);
                
                onto.addProperty(individualSentence, sentenceProperties.getSentenceNumberInParagraph(), StringHelper.convert(sentence.getParagraphSentenceNumber()), language);
                onto.addProperty(individualSentence, sentenceProperties.getSentenceNumberInText(), StringHelper.convert(sentence.getSentenceNumber()), language);
                onto.addProperty(individualSentence, sentenceProperties.getAverageWordLength(), StringHelper.convert(sentence.getAverageWordLength()), language);
                onto.addProperty(individualSentence, sentenceProperties.getAverageSyllablesPerWord(), StringHelper.convert(sentence.getAverageSyllablesPerWord()), language);
                onto.addProperty(individualSentence, sentenceProperties.getNumberOfWords(), StringHelper.convert(sentence.getNumberOfWords()), language);
                onto.addProperty(individualSentence, sentenceProperties.getNumberOfSyllables(), StringHelper.convert(sentence.getNumberOfSyllables()), language);
                onto.addProperty(individualSentence, sentenceProperties.getNumberOfLetters(), StringHelper.convert(sentence.getNumberOfLetters()), language);
                onto.addProperty(individualSentence, sentenceProperties.getNumberOfReferences(), StringHelper.convert(sentence.getNumberOfReferences()), language);
                onto.addProperty(individualSentence, sentenceProperties.getPartOfParagraph(), StringHelper.convert(paragraph.getParagraphNumber()), language);
                
                //Create Jena objectProperty and link the sentence Individual to the text and paragraph(s) individuals.
                onto.createObecjtProperty(individualParagraph, individualSentence, paragraphProperties.getHasSentence());
                onto.createObecjtProperty(individualText, individualSentence, textProperties.getHasSentence());
                
                //TODO Test connectives
                //Create Jena Connective Individual, add properties to it and link the Jena ObjectProperty to the sentence individuals.
                for (Map.Entry<Type.Connective, String> entry : sentence.getConnectiveTypes().entrySet()) {
                    individualNames.add("CONNECTIVE" + numberOfConnectives);
                    Connective connectiveType = entry.getKey();
                    String connective = entry.getValue();
                    Individual individualConnective = onto.createIndividual("CONNECTIVE" + numberOfConnectives, ontConnective);
                    onto.addProperty(individualConnective, connectiveProperties.getLanguage(), language, language);
                    onto.addProperty(individualConnective, connectiveProperties.getType(), connectiveType.toString(), language);
                    onto.addProperty(individualConnective, connectiveProperties.getConnective(), connective, language);
                    onto.createObecjtProperty(individualSentence, individualConnective, sentenceProperties.getHasConnective());
                    numberOfConnectives++;
                }
                
                //Create Jena Word Individual and add properties to it.
                for (Word word : sentence.getWords()) {
                    individualNames.add("WORD" + word.getTextWordNumber());
                    Individual individualWord = onto.createIndividual("WORD" + word.getTextWordNumber(), ontWord);
                    onto.addProperty(individualWord, wordProperties.getLanguage(), language, language);
                    onto.addProperty(individualWord, wordProperties.getContent(), word.getContent(), language);
                    onto.addProperty(individualWord, wordProperties.getLemma(), word.getLemma(), language);
                    onto.addProperty(individualWord, wordProperties.getTag(), word.getTag(), language);
                    onto.addProperty(individualWord, wordProperties.getStem(), word.getStem(), language);
                    onto.addProperty(individualWord, wordProperties.getSoundex(), word.getSoundex(), language);
                    onto.addProperty(individualWord, wordProperties.getFont(), word.getFont().toString(), language);

                    onto.addProperty(individualWord, wordProperties.getSentenceWordNumber(), StringHelper.convert(word.getSentenceWordNumber()), language);
                    onto.addProperty(individualWord, wordProperties.getParagraphWordNumber(), StringHelper.convert(word.getParagraphWordNumber()), language);
                    onto.addProperty(individualWord, wordProperties.getTextWordNumber(), StringHelper.convert(word.getTextWordNumber()), language);
                    onto.addProperty(individualWord, wordProperties.getNumberOfSyllables(), StringHelper.convert(word.getNumberOfSyllables()), language);
                    onto.addProperty(individualWord, wordProperties.getNumberOfLetters(), StringHelper.convert(word.getNumberOfLetters()), language);
                    onto.addProperty(individualWord, wordProperties.getLength(), StringHelper.convert(word.getLength()), language);
                    
                    if (word.isReference()) {
                        onto.addProperty(individualWord, wordProperties.isReference(), "Yes", language);
                    }
                    else {
                        onto.addProperty(individualWord, wordProperties.isReference(), "No", language);
                    }
                    
                    if (word.isRare()) {
                        onto.addProperty(individualWord, wordProperties.isRare(), "Yes", language);
                    }
                    else {
                        onto.addProperty(individualWord, wordProperties.isRare(), "No", language);
                    }
                    
                    //Create Jena objectProperty and link the text, paragraph(s) and sentence(s) Individuals to the word individual.
                    onto.createObecjtProperty(individualSentence, individualWord, sentenceProperties.getHasWord());
                    onto.createObecjtProperty(individualParagraph, individualWord, paragraphProperties.getHasWord());
                    onto.createObecjtProperty(individualText, individualWord, textProperties.getHasWord());
                }
            }
        }
        
        //Save individual names
        for (String individualName : individualNames){
            onto.createIndividual(individualName, ontSyntax);
        }        
        
        return onto;
	}
	
	public String getNamespace(){
	    return namespace;
	}
	
	public OntologyHelper getOntology(){
	    return onto;
	}
	
	public void save(String location) {
        try {
            onto.save(location + "\\" + name + ".owl");
            logger.info("Ontology " + name + " successfully saved to " + location + ".");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to save the ontolgy!", e);
            e.printStackTrace();
        }
	}
	
}