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
public class EnglishDenstiyTest {
    
    @Test
    public void test6() {
        //http://www.analyzemywriting.com/lexical_density.html
        String textString = "He intensely loves going to the huge cinema everyday.";
        Text text = new Text(textString, Language.EN);
        
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        String prop5 = sentence.getPropositions().get(4).getToken();
        String prop6 = sentence.getPropositions().get(5).getToken();

        Assert.assertEquals("intensely", prop1);
        Assert.assertEquals("loves", prop2);
        Assert.assertEquals("going", prop3);
        Assert.assertEquals("huge", prop4);
        Assert.assertEquals("cinema", prop5);
        Assert.assertEquals("everyday", prop6);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 6);
    }
    
    
    
    @Test
    public void test5() {
        //http://www.analyzemywriting.com/lexical_density.html
        String textString = "He loves going to the cinema everyday .";
        Text text = new Text(textString, Language.EN);
        
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        
        Assert.assertEquals("loves", prop1);
        Assert.assertEquals("going", prop2);
        Assert.assertEquals("cinema", prop3);
        Assert.assertEquals("everyday", prop4);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 4);
    }
    
    @Test
    public void test4() {
        //http://www.analyzemywriting.com/lexical_density.html
        String textString = "He loves going to the cinema .";
        Text text = new Text(textString, Language.EN);
        
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        
        Assert.assertEquals("loves", prop1);
        Assert.assertEquals("going", prop2);
        Assert.assertEquals("cinema", prop3);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 3);
    }
    
    @Test
    public void test3() {
        //http://www.analyzemywriting.com/lexical_density.html
        String textString = "The quick brown fox jumped swiftly over the lazy dog.";
        Text text = new Text(textString, Language.EN);
        
//      for (Sentence sentence : text.getSentences()) {
//          System.out.println(sentence.getTaggedSentence());
//          for (Item prop : sentence.getPropositions()) {
//              if (prop.isProp()) {
//                  System.out.println(prop.getRulenumber() + ": " + prop.getToken() + " (prop)");
//              }                
//          }
//          System.out.println(sentence.getNumberOfPropositions());
//      }
        
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        String prop3 = sentence.getPropositions().get(2).getToken();
        String prop4 = sentence.getPropositions().get(3).getToken();
        String prop5 = sentence.getPropositions().get(4).getToken();
        String prop6 = sentence.getPropositions().get(5).getToken();
        String prop7 = sentence.getPropositions().get(6).getToken();
        
        Assert.assertEquals("quick", prop1);
        Assert.assertEquals("brown", prop2);
        Assert.assertEquals("fox", prop3);
        Assert.assertEquals("jumped", prop4);
        Assert.assertEquals("swiftly", prop5);
        Assert.assertEquals("lazy", prop6);
        Assert.assertEquals("dog", prop7);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 7);
    }
    
    @Test
    public void test2() {
        //http://www.analyzemywriting.com/lexical_density.html
        String textString = "She told him that she loved him.";
        Text text = new Text(textString, Language.EN);
        
        Sentence sentence = text.getSentences().get(0);
        String prop1 = sentence.getPropositions().get(0).getToken();
        String prop2 = sentence.getPropositions().get(1).getToken();
        
        Assert.assertEquals("told", prop1);
        Assert.assertEquals("loved", prop2);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 2);
    }
    
    
    @Test
    public void test1() {
        //http://www.sltinfo.com/lexical-density/
        String textString = "In 1918, when the chemical industry was first established in the area, Billingham was a village inhabited by a few hundred people but grew rapidly as ICI’s operations expanded, helped by the company’s reputation for providing secure employment. The wages, conditions and benefits offered by ICI were attractive and the company quickly gained a reputation as a good employer. Many of those we interviewed claimed this was their main reason for applying for a job. Our interviews also highlight the influence of family when making decisions about employment and ICI was certainly happy to recruit the sons and daughters of existing workers.";
        Text text = new Text(textString, Language.EN);
        
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
        String prop14 = sentence.getPropositions().get(13).getToken();
        String prop15 = sentence.getPropositions().get(14).getToken();
        String prop16 = sentence.getPropositions().get(15).getToken();
        String prop17 = sentence.getPropositions().get(16).getToken();
        String prop18 = sentence.getPropositions().get(17).getToken();
        String prop19 = sentence.getPropositions().get(18).getToken();
        String prop20 = sentence.getPropositions().get(19).getToken();
        String prop21 = sentence.getPropositions().get(20).getToken();
        String prop22 = sentence.getPropositions().get(21).getToken();
        
        Assert.assertEquals("1918", prop1);
        Assert.assertEquals("when", prop2);
        Assert.assertEquals("industry", prop3);
        Assert.assertEquals("first", prop4);
        Assert.assertEquals("established", prop5);
        Assert.assertEquals("was", prop6);
        Assert.assertEquals("village", prop7);
        Assert.assertEquals("inhabited", prop8);
        Assert.assertEquals("few", prop9);
        Assert.assertEquals("hundred", prop10);
        Assert.assertEquals("people", prop11);
        Assert.assertEquals("but", prop12);
        Assert.assertEquals("grew", prop13);
        Assert.assertEquals("rapidly", prop14);
        Assert.assertEquals("operations", prop15);
        Assert.assertEquals("expanded", prop16);
        Assert.assertEquals("helped", prop17);
        Assert.assertEquals("company’s", prop18);
        Assert.assertEquals("reputation", prop19);
        Assert.assertEquals("providing", prop20);
        Assert.assertEquals("secure", prop21);
        Assert.assertEquals("employment", prop22);
        
        //Propositions in sentence2
        sentence = text.getSentences().get(1);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        prop3 = sentence.getPropositions().get(2).getToken();
        prop4 = sentence.getPropositions().get(3).getToken();
        prop5 = sentence.getPropositions().get(4).getToken();
        prop6 = sentence.getPropositions().get(5).getToken();
        prop7 = sentence.getPropositions().get(6).getToken();
        prop8 = sentence.getPropositions().get(7).getToken();
        prop9 = sentence.getPropositions().get(8).getToken();
        prop10 = sentence.getPropositions().get(9).getToken();
        prop11 = sentence.getPropositions().get(10).getToken();
        prop12 = sentence.getPropositions().get(11).getToken();
        
        Assert.assertEquals("conditions", prop1);
        Assert.assertEquals("and", prop2);
        Assert.assertEquals("offered", prop3);
        Assert.assertEquals("ICI", prop4);
        Assert.assertEquals("attractive", prop5);
        Assert.assertEquals("and", prop6);
        Assert.assertEquals("company", prop7);
        Assert.assertEquals("quickly", prop8);
        Assert.assertEquals("gained", prop9);
        Assert.assertEquals("reputation", prop10);
        Assert.assertEquals("good", prop11);
        Assert.assertEquals("employer", prop12);
        
        //Propositions in sentence3
        sentence = text.getSentences().get(2);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        prop3 = sentence.getPropositions().get(2).getToken();
        prop4 = sentence.getPropositions().get(3).getToken();
        prop5 = sentence.getPropositions().get(4).getToken();
        prop6 = sentence.getPropositions().get(5).getToken();
        prop7 = sentence.getPropositions().get(6).getToken();
        prop8 = sentence.getPropositions().get(7).getToken();
        
        Assert.assertEquals("those", prop1);
        Assert.assertEquals("interviewed", prop2);
        Assert.assertEquals("claimed", prop3);
        Assert.assertEquals("was", prop4);
        Assert.assertEquals("main", prop5);
        Assert.assertEquals("reason", prop6);
        Assert.assertEquals("applying", prop7);
        Assert.assertEquals("job", prop8);

        //Propositions in sentence4
        sentence = text.getSentences().get(3);
        prop1 = sentence.getPropositions().get(0).getToken();
        prop2 = sentence.getPropositions().get(1).getToken();
        prop3 = sentence.getPropositions().get(2).getToken();
        prop4 = sentence.getPropositions().get(3).getToken();
        prop5 = sentence.getPropositions().get(4).getToken();
        prop6 = sentence.getPropositions().get(5).getToken();
        prop7 = sentence.getPropositions().get(6).getToken();
        prop8 = sentence.getPropositions().get(7).getToken();
        prop9 = sentence.getPropositions().get(8).getToken();
        prop10 = sentence.getPropositions().get(9).getToken();
        prop11 = sentence.getPropositions().get(10).getToken();
        prop12 = sentence.getPropositions().get(11).getToken();
        prop13 = sentence.getPropositions().get(12).getToken();
        prop14 = sentence.getPropositions().get(13).getToken();
        prop15 = sentence.getPropositions().get(14).getToken();
        prop16 = sentence.getPropositions().get(15).getToken();
        prop17 = sentence.getPropositions().get(16).getToken();
        
        Assert.assertEquals("interviews", prop1);
        Assert.assertEquals("also", prop2);
        Assert.assertEquals("highlight", prop3);
        Assert.assertEquals("influence", prop4);
        Assert.assertEquals("when", prop5);
        Assert.assertEquals("making", prop6);
        Assert.assertEquals("decisions", prop7);
        Assert.assertEquals("employment", prop8);
        Assert.assertEquals("and", prop9);
        Assert.assertEquals("ICI", prop10);
        Assert.assertEquals("certainly", prop11);
        Assert.assertEquals("recruit", prop12);
        Assert.assertEquals("sons", prop13);
        Assert.assertEquals("and", prop14);
        Assert.assertEquals("daughters", prop15);
        Assert.assertEquals("existing", prop16);
        Assert.assertEquals("workers", prop17);
        
        Assert.assertEquals(text.getNumberOfPropositions(), 59);
        

    }
}
