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
public class GermanDensityTest {
    //https://books.google.lu/books?id=cpn9wsEfXKoC&pg=PA114&lpg=PA114&dq=lexikalische+dichte+beispiel&source=bl&ots=XP11feFxOk&sig=e-X3iZedC0gx1xeuvCrt4EMGhe8&hl=de&sa=X&ved=0ahUKEwjF0t6V4uPNAhUGLhoKHdkCDeUQ6AEISjAJ#v=onepage&q=lexikalische%20dichte%20beispiel&f=false
    
    @Test
    public void test3() {
        String textString = "Einer intensiven kontinuierlichen Förderung in Schule und Vorschule.";
        Text text = new Text(textString, Language.DE);
        
        //Propositions in sentence1
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        String prop5 = sentence.getPropositions().get(4).getToken();
        String prop6 = sentence.getPropositions().get(5).getToken();
        
        Assert.assertEquals("intensiven", prop1);
        Assert.assertEquals("kontinuierlichen", prop2);
        Assert.assertEquals("Förderung", prop3);
        Assert.assertEquals("in", prop4); //Not indicated in the example, but a rule 16 in our code
        Assert.assertEquals("Schule", prop5);
        Assert.assertEquals("Vorschule", prop6);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 6);
    }
    
    @Test
    public void test2() {
        String textString = "Eine normale Entwicklung.";
        Text text = new Text(textString, Language.DE);
        
        //Propositions in sentence1
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        
        Assert.assertEquals("normale", prop1);
        Assert.assertEquals("Entwicklung", prop2);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 2);
    }
    
    @Test
    public void test1() {
        String textString = "Ich möchte gerne mit dir feiern und lade dich herzlich zu meiner Party am Samstagabend ein. Wir fangen um 21 Uhr an. Ist das okay für dich? Es werden viele Leute da sein, die du auch kennst. Kannst du vielleicht einen Salat mitbringen? Und vergiss bitte nicht einen Pullover oder eine Jacke! Wir wollen nämlich draußen im Garten feiern.";
        Text text = new Text(textString, Language.DE);
        
//        for (Sentence sentence : text.getSentences()) {
//            System.out.println(sentence.getTaggedSentence());
//            for (Item prop : sentence.getPropositions()) {
//                if (prop.isProp()) {
//                    System.out.println(prop.getRulenumber() + ": " + prop.getToken() + " (prop)");
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
        
        Assert.assertEquals("möchte", prop1);
        Assert.assertEquals("gerne", prop2);
        Assert.assertEquals("mit", prop3);
        Assert.assertEquals("feiern", prop4);
        Assert.assertEquals("lade", prop5);
        Assert.assertEquals("herzlich", prop6);
        Assert.assertEquals("zu", prop7);
        Assert.assertEquals("meiner", prop8);
        Assert.assertEquals("Party", prop9);
        Assert.assertEquals("am", prop10);
        Assert.assertEquals("Samstagabend", prop11);
        
        //Propositions in sentence2
        sentence = text.getSentences().get(1);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        prop3 = sentence.getPropositions().get(2).getToken();
        prop4 = sentence.getPropositions().get(3).getToken();
        
        Assert.assertEquals("fangen", prop1);
        Assert.assertEquals("um", prop2);
        Assert.assertEquals("21", prop3);
        Assert.assertEquals("Uhr", prop4);
        
        //Propositions in sentence3
        sentence = text.getSentences().get(2);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        
        Assert.assertEquals("okay", prop1);
        Assert.assertEquals("für", prop2);

        //Propositions in sentence4
        sentence = text.getSentences().get(3);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        prop3 = sentence.getPropositions().get(2).getToken();
        prop4 = sentence.getPropositions().get(3).getToken();
        prop5 = sentence.getPropositions().get(4).getToken();
        prop6 = sentence.getPropositions().get(5).getToken();
        prop7 = sentence.getPropositions().get(6).getToken();
        
        Assert.assertEquals("werden", prop1);
        Assert.assertEquals("viele", prop2);
        Assert.assertEquals("Leute", prop3);
        Assert.assertEquals("da", prop4);
        Assert.assertEquals("sein", prop5);
        Assert.assertEquals("du", prop6);
        Assert.assertEquals("kennst", prop7);

        //Propositions in sentence5
        sentence = text.getSentences().get(4);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        
        Assert.assertEquals("vielleicht", prop1);
        Assert.assertEquals("mitbringen", prop2);

        //Propositions in sentence6
        sentence = text.getSentences().get(5);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        prop3 = sentence.getPropositions().get(2).getToken();
        prop4 = sentence.getPropositions().get(3).getToken();
        prop5 = sentence.getPropositions().get(4).getToken();
        
        Assert.assertEquals("vergiss", prop1);
        Assert.assertEquals("bitte", prop2);
        Assert.assertEquals("nicht", prop3);
        Assert.assertEquals("Pullover", prop4);
        Assert.assertEquals("Jacke", prop5);

        //Propositions in sentence7
        sentence = text.getSentences().get(6);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        prop3 = sentence.getPropositions().get(2).getToken();
        prop4 = sentence.getPropositions().get(3).getToken();
        
        Assert.assertEquals("wollen", prop1);
        Assert.assertEquals("draußen", prop2);
        Assert.assertEquals("im", prop3);
        Assert.assertEquals("feiern", prop4);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 35);
    }
}

