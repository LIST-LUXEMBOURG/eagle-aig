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

import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.Resources;
import lu.list.itis.dkd.assess.cloze.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.ontology.TextOntology;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

public class EnglishClozeOntologyTest {

    private static void local() {        
//        Text englishText = new Text(TestResources.class.getResourceAsStream("resources/PC(EN) - UTF8.txt"), Language.EN);
//        for (Paragraph paragraph : englishText.getParagraphs()) {
//            System.out.print("Paragraph " + paragraph.getParagraphNumber() + ": \n");
//            for (Sentence sentence : paragraph.getSentences()) {
//                System.out.println(sentence.getParagraphSentenceNumber() + " " + sentence.getContent());
//            }
//            System.out.println("\n");
//        }
        
        //Create a noun ClozeText
        String content = Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt"));
        ClozeText clozeNounText = new ClozeText(content, Language.EN, Approach.NOUN);
        TextOntology ontology = new TextOntology(clozeNounText, "localClozeTest");   
        ClozeOntology clozeNounOntology = new ClozeOntology(clozeNounText, ontology);
        
        //Create an annotation ClozeText
        ClozeText clozeAnnotationText = new ClozeText(content, Language.EN, Approach.ANNOTATION);
        ClozeOntology clozeAnnotationOntology = new ClozeOntology(clozeAnnotationText, clozeNounOntology.getOntology(), clozeNounOntology.getNamespace());
        
        //Create a verb ClozeText
        ClozeText clozeVerbText = new ClozeText(content, Language.EN, Approach.VERB);
        ClozeOntology clozeVerbOntology = new ClozeOntology(clozeVerbText, clozeAnnotationOntology.getOntology(), clozeAnnotationOntology.getNamespace());
        
        //Create a CText
        ClozeText clozeCText = new ClozeText(content, Language.EN, Approach.CTEST);
        ClozeOntology clozeCOntology = new ClozeOntology(clozeCText, clozeVerbOntology.getOntology(), clozeVerbOntology.getNamespace());
        
        //Create a Definition CText
        String html = Wrapper.loadTextFile(Resources.class.getResourceAsStream("Glossary (EN).xml"));
        ClozeText clozeDefinitionText = new ClozeText(html, "wiki-body", Language.EN, Approach.DEFINITION);
        ClozeOntology clozeDefinitionOntology = new ClozeOntology(clozeDefinitionText, clozeCOntology.getOntology(), clozeCOntology.getNamespace());
        
        clozeDefinitionOntology.save("C:\\Users\\pfeiffer\\workspace\\ClozeGenerator\\src\\test\\resources\\lu\\list\\itis\\dkd\\assess\\cloze\\util\\Ontology", "ListCloze");
    }
    
    public static void main(String[] args) {
//        url();
        local();
    }
}
