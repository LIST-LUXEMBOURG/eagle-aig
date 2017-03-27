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
package lu.list.itis.dkd.assess.opennlp.density;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class FrenchDensityTest {
    //https://books.google.lu/books?id=p6uvAwAAQBAJ&pg=PA47&lpg=PA47&dq=french+lexical+density+example&source=bl&ots=CEoG12hcxJ&sig=fUpaTZMYCDdQVlkQF1E81eW_NDM&hl=de&sa=X&ved=0ahUKEwjv2sK_ouHNAhWEJhoKHUWbC2MQ6AEINjAD#v=onepage&q=french%20lexical%20density%20example&f=false
    
    @Test
    public void test4() {
        String textString = "Certaines limites à la mécanique de Newton ont conduit Einstein, en 1916, à formuler la théorie de la relaticité générale.";
        Text text = new Text(textString, Language.FR);
        
        for (Sentence sentence : text.getSentences()) {
            System.out.println(sentence.getTaggedSentence());
            for (Item prop : sentence.getPropositions()) {
                if (prop.isProp()) {
                    System.out.println(prop.getRulenumber() + ": " + prop.getToken() + "[" + prop.getTag() + "]" + " (prop)");
                }                
            }
            System.out.println(sentence.getNumberOfPropositions());
        }
        
        //Propositions in sentence1
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        String prop5 = sentence.getPropositions().get(4).getToken();
        String prop6 = sentence.getPropositions().get(5).getToken();
        String prop7 = sentence.getPropositions().get(6).getToken();
        String prop8 = sentence.getPropositions().get(7).getToken();
        String prop9 = sentence.getPropositions().get(8).getToken();
        
        Assert.assertEquals("limites", prop1);
        Assert.assertEquals("mécanique", prop2);
        Assert.assertEquals("Newton", prop3); //In the Example Einstein was more important than Newton
        Assert.assertEquals("conduit", prop4);
        Assert.assertEquals("formuler", prop5);
        Assert.assertEquals("théorie", prop6);
        Assert.assertEquals("la", prop7); //Not indicated in the example, but a rule 701 in our code
        Assert.assertEquals("relaticité", prop8);
        Assert.assertEquals("générale", prop9);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 9);
    }
    
    @Test
    public void test3() {
        String textString = "Toutfois les phénomènes observés dans l'Univers peuvent aussi nous amener parfois à modifier certaines lois physiques.";
        Text text = new Text(textString, Language.FR);
        
        //Propositions in sentence1
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        String prop5 = sentence.getPropositions().get(4).getToken();
        String prop6 = sentence.getPropositions().get(5).getToken();
        String prop7 = sentence.getPropositions().get(6).getToken();
        String prop8 = sentence.getPropositions().get(7).getToken();
        
        Assert.assertEquals("phénomènes", prop1);
        Assert.assertEquals("observés", prop2);
        Assert.assertEquals("l'Univers", prop3);
        Assert.assertEquals("peuvent", prop4);
        Assert.assertEquals("amener", prop5);
        Assert.assertEquals("modifier", prop6);
        Assert.assertEquals("lois", prop7);
        Assert.assertEquals("physiques", prop8);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 8);
    }
    
    @Test
    public void test2() {
        String textString = "Les lois de la physique nous permettent non seulement de décrire l'Univers mais aussi de l'expliquer.";
        Text text = new Text(textString, Language.FR);
        
        //Propositions in sentence1
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        String prop5 = sentence.getPropositions().get(4).getToken();
        String prop6 = sentence.getPropositions().get(5).getToken();
        String prop7 = sentence.getPropositions().get(6).getToken();
        
        Assert.assertEquals("lois", prop1);
        Assert.assertEquals("la", prop2); //Not indicated in the example, but a rule 701 in our code
        Assert.assertEquals("physique", prop3);
        Assert.assertEquals("permettent", prop4);
        Assert.assertEquals("décrire", prop5);
        Assert.assertEquals("l'Univers", prop6);
        Assert.assertEquals("l'expliquer", prop7);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 7);
    }
    
    @Test
    public void test1() {
        String textString = "Le développement de l'astronomie est lié à celui des techniques (lunettes, télescopes, spectrosopie, photographie, radar, exploration spatiale) et des connaissances théoriques.";
        Text text = new Text(textString, Language.FR);
        
//        for (Sentence sentence : text.getSentences()) {
//            System.out.println(sentence.getTaggedSentence());
//            for (Item prop : sentence.getPropositions()) {
//                if (prop.isProp()) {
//                    System.out.println(prop.getRulenumber() + ": " + prop.getToken() + "[" + prop.getTag() + "]" + " (prop)");
//                }                
//            }
//            System.out.println(sentence.getNumberOfPropositions());
//        }
        
        //Propositions in sentence1
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        String prop5 = sentence.getPropositions().get(4).getToken();
        String prop6 = sentence.getPropositions().get(5).getToken();
        String prop7 = sentence.getPropositions().get(6).getToken();
        String prop8 = sentence.getPropositions().get(7).getToken();
        String prop9 = sentence.getPropositions().get(8).getToken();
        String prop10 = sentence.getPropositions().get(9).getToken();
        String prop11 = sentence.getPropositions().get(10).getToken();
        String prop12 = sentence.getPropositions().get(11).getToken();
        String prop13 = sentence.getPropositions().get(12).getToken();
        
        Assert.assertEquals("développement", prop1);
        Assert.assertEquals("l'astronomie", prop2);
        Assert.assertEquals("lié", prop3);
        Assert.assertEquals("techniques", prop4);
        Assert.assertEquals("lunettes", prop5);
        Assert.assertEquals("télescopes", prop6);
        Assert.assertEquals("spectrosopie", prop7);
        Assert.assertEquals("photographie", prop8);
        Assert.assertEquals("radar", prop9);
        Assert.assertEquals("exploration", prop10);
        Assert.assertEquals("spatiale", prop11);
        Assert.assertEquals("connaissances", prop12);
        Assert.assertEquals("théoriques", prop13);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 13);
    }
}
