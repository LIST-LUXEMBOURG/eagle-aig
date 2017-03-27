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

import lu.list.itis.dkd.assess.opennlp.Paragraph;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.ontology.TextOntology;
import lu.list.itis.dkd.assess.opennlp.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class EnglishOntologyTest {
    
//    private static void url() throws IOException{
//        String htmlPath = "C:\\Users\\pfeiffer\\workspace\\OpenNLP_Parser\\test\\resources\\Glossary (EN).xml";
//        
//        File file = new File(htmlPath);
//        Document document = Jsoup.parse(file, "UTF8");
//        Text text = new Text(document, "wiki-body", "header-title", Language.EN);
//        
//        Ontology ontology = new Ontology(text, "urlTest");
//        ontology.save("C:\\Users\\pfeiffer\\workspace\\OpenNLP_Parser\\test\\Ontology");
//    }
    
    private static void local() {
        String content = Wrapper.loadTextFile(TestResources.class.getResourceAsStream("resources/PC(EN) - UTF8.txt"));
        Text englishText = new Text(content, Language.EN);
        
        for (Paragraph paragraph : englishText.getParagraphs()) {
            System.out.print("Paragraph " + paragraph.getParagraphNumber() + ": \n");
            for (Sentence sentence : paragraph.getSentences()) {
                System.out.println(sentence.getParagraphSentenceNumber() + " " + sentence.getContent());
            }
            System.out.println("\n");
        }
        
        
        TextOntology ontology = new TextOntology(englishText, "localTest");
        ontology.save("C:\\Users\\pfeiffer\\workspace\\LIST-Parser\\src\\test\\resources\\lu\\list\\itis\\dkd\\assess\\opennlp\\util\\Ontology");
    }
    
    public static void main(String[] args) {
//        url();
        local();
    }
}
