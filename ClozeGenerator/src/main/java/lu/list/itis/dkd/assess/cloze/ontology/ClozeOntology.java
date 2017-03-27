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
package lu.list.itis.dkd.assess.cloze.ontology;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.opennlp.ontology.TextOntology;
import lu.list.itis.dkd.assess.opennlp.util.OntologyHelper;
import lu.list.itis.dkd.assess.opennlp.util.StringHelper;

public class ClozeOntology {
    private OntologyHelper onto;
    private String language;
    private String namespace;
    
    protected static final Logger logger = Logger.getLogger(ClozeOntology.class.getSimpleName());

    public ClozeOntology(ClozeText clozeText, TextOntology textOntology){
        switch (clozeText.getLanguage()) {
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
        this.onto = textOntology.getOntology();
        this.namespace = textOntology.getNamespace();
        build(clozeText);
    }
    
    public ClozeOntology(ClozeText clozeText, OntologyHelper onto, String namespace){
        switch (clozeText.getLanguage()) {
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
        this.onto = onto;
        this.namespace = namespace;
        build(clozeText);
    }
    
    private OntologyHelper build(ClozeText clozeText){
        if (clozeText.getApproach().equals(Approach.CTEST)){
            return build_WithoutKeyDistractorPairs(clozeText);
        }
        
        return build_WithKeyDistractorPairs(clozeText);
    }
    
    private OntologyHelper build_WithoutKeyDistractorPairs(ClozeText clozeText){
        List<String> individualNames = new ArrayList<>();
        
        //Create Jena Classes
        OntClass ontDomain = onto.createClass("Model_"+clozeText.getApproach().toString().toLowerCase());
        OntClass ontClozeText = onto.createClass("ClozeText_"+clozeText.getApproach().toString().toLowerCase());
        OntClass ontClozeSentence = onto.createClass("ClozeSentence_"+clozeText.getApproach().toString().toLowerCase());
        
        //Create Jena Properties
        ClozeTextProperties clozeTextProperties = new ClozeTextProperties(onto, namespace);
        ClozeSentenceProperties clozeSentenceProperties = new ClozeSentenceProperties(onto, namespace);
        
        //Create Jena Class Structure
        ontDomain.addSubClass(ontClozeText);
        ontDomain.addSubClass(ontClozeSentence);
        
        //Create Jena cloze Text Individual and add properties to it.
        individualNames.add("CLOZETEXT_"+clozeText.getApproach().toString().toLowerCase());
        Individual individualClozeText = onto.createIndividual("CLOZETEXT_"+clozeText.getApproach().toString().toLowerCase(), ontClozeText);
        onto.addProperty(individualClozeText, clozeTextProperties.getContent(), clozeText.getClozeText(), language);
        //onto.addProperty(individualClozeText, clozeTextProperties.getFirstSentence(), clozeText.isFirstSentenceSkipped());
        
        //Create Jena cloze Sentence Individual and add properties to it.
        for (ClozeSentence clozeSentence : clozeText.getClozeSentences()){
            //Create Jena cloze sentence Individual and add properties to it
            individualNames.add("CLOZESENTENCE_"+clozeText.getApproach().toString().toLowerCase() + clozeSentence.getSentence().getSentenceNumber());
            Individual individualClozeSentence = onto.createIndividual("CLOZESENTENCE_"+clozeText.getApproach().toString().toLowerCase() + clozeSentence.getSentence().getSentenceNumber(), ontClozeSentence);
            onto.addProperty(individualClozeSentence, clozeSentenceProperties.getContent(), clozeSentence.getContent());
         
            //Create Jena objectProperty and link the sentence Individual to the text individual.
            onto.createObecjtProperty(individualClozeText, individualClozeSentence, clozeTextProperties.getHasClozeSentence());
        }
        
        //Save individual names
        for (String individualName : individualNames){
            onto.createIndividual(individualName, ontDomain);
        }
        
        return onto;
    }
    
    private OntologyHelper build_WithKeyDistractorPairs(ClozeText clozeText){      
        List<String> individualNames = new ArrayList<>();
        
        //Create Jena Classes
        OntClass ontDomain = onto.createClass("Model_"+clozeText.getApproach().toString().toLowerCase());
        OntClass ontClozeText = onto.createClass("ClozeText_"+clozeText.getApproach().toString().toLowerCase());
        OntClass ontClozeSentence = onto.createClass("ClozeSentence_"+clozeText.getApproach().toString().toLowerCase());
        OntClass ontKey = onto.createClass("Key_"+clozeText.getApproach().toString().toLowerCase());
        OntClass ontDistractor = onto.createClass("Distractor_"+clozeText.getApproach().toString().toLowerCase());
               
        //Create Jena Properties
        ClozeTextProperties clozeTextProperties = new ClozeTextProperties(onto, namespace);
        ClozeSentenceProperties clozeSentenceProperties = new ClozeSentenceProperties(onto, namespace);  
        KeyProperties keyProperties = new KeyProperties(onto, namespace);
        DistractorProperties distractorProperties = new DistractorProperties(onto, namespace);
        
        //Create Jena Class Structure
        ontDomain.addSubClass(ontClozeText);
        ontDomain.addSubClass(ontClozeSentence);
        ontDomain.addSubClass(ontKey);
        ontDomain.addSubClass(ontDistractor);
        
        //Create Jena cloze Text Individual and add properties to it.
        individualNames.add("CLOZETEXT" + "_"+clozeText.getApproach().toString().toLowerCase());
        Individual individualClozeText = onto.createIndividual("CLOZETEXT" + "_"+clozeText.getApproach().toString().toLowerCase(), ontClozeText);
        onto.addProperty(individualClozeText, clozeTextProperties.getContent(), clozeText.getClozeText(), language);
        //onto.addProperty(individualClozeText, clozeTextProperties.getFirstSentence(), clozeText.isFirstSentenceSkipped());
        
        //Create Jena cloze Sentence Individual and add properties to it.
        int keyNumber = 1;
        int distractorNumber = 1;
        for (ClozeSentence clozeSentence : clozeText.getClozeSentences()){
            //Create Jena cloze sentence Individual and add properties to it
            individualNames.add("CLOZESENTENCE_"+clozeText.getApproach().toString().toLowerCase() + clozeSentence.getSentence().getSentenceNumber());
            Individual individualClozeSentence = onto.createIndividual("CLOZESENTENCE_"+clozeText.getApproach().toString().toLowerCase() + clozeSentence.getSentence().getSentenceNumber(), ontClozeSentence);
            onto.addProperty(individualClozeSentence, clozeSentenceProperties.getContent(), clozeSentence.getContent());
         
            //Create Jena objectProperty and link the sentence Individual to the text individual.
            onto.createObecjtProperty(individualClozeText, individualClozeSentence, clozeTextProperties.getHasClozeSentence());
            
            //Create Jena key Individual and add properties to it.
            for (Key key : clozeSentence.getKeys()){
                if (key.getDistractors().isEmpty()){
                    continue;
                }
                individualNames.add("KEY_" + clozeText.getApproach().toString().toLowerCase() + keyNumber);
                Individual individualKey = onto.createIndividual("KEY_" + clozeText.getApproach().toString().toLowerCase() + keyNumber, ontKey);
                onto.addProperty(individualKey, keyProperties.getContent(), key.getKeyWord().getContent());
                keyNumber++;
                
                //Create Jena objectProperty and link the key Individual to the sentence individual.
                onto.createObecjtProperty(individualClozeSentence, individualKey, clozeSentenceProperties.getHasKey());
                
                //Create Jena distractor Individual and add properties to it.
                for (Distractor distractor : key.getDistractors() ){
                    individualNames.add("DISTRACTOR_" + clozeText.getApproach().toString().toLowerCase() + distractorNumber );
                    Individual individualDistractor = onto.createIndividual("DISTRACTOR_" +clozeText.getApproach().toString().toLowerCase() + distractorNumber, ontDistractor);
                    onto.addProperty(individualDistractor, distractorProperties.getContent(), distractor.getDistractorWord().getContent());
                    onto.addProperty(individualDistractor, distractorProperties.getFeedback(), distractor.getFeedback());
                    onto.addProperty(individualDistractor, distractorProperties.getSimilarity(), StringHelper.convert(distractor.getSimilarity()));
                    distractorNumber++;
                    
                    //Create Jena objectProperty and link the distractor Individual to the key individual.
                    onto.createObecjtProperty(individualKey, individualDistractor, keyProperties.getHasDistractor());
                }
            }
        }
        
        //Save individual names
        for (String individualName : individualNames){
            onto.createIndividual(individualName, ontDomain);
        }
        
        return onto;
    }
    
    public void save(String location, String name) {
        try {
            onto.save(location + "/" + name + ".owl");
            logger.info("Ontology " + name + " successfully saved to " + location + ".");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to save the ontolgy!", e);
            e.printStackTrace();
        }
    }
    
    public void save(OutputStream outputStream) {
    	onto.save(outputStream);
    }
    
    public OntologyHelper getOntology(){
        return onto;
    }
    
    public String getNamespace(){
        return namespace;
    }
}
