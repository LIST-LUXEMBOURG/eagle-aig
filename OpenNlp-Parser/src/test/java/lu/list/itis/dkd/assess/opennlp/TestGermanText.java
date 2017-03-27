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
package lu.list.itis.dkd.assess.opennlp;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class TestGermanText {
    @Test
    public void plainTest() {
        String german = "Ein Computer ist ein Digitalcomputer, wenn er mit digitalen Geräteeinheiten digitale Daten verarbeitet (also Zahlen und Textzeichen). Er ist ein Analogcomputer, wenn er mit analogen Geräteeinheiten analoge Daten verarbeitet (also kontinuierlich verlaufende elektrische Messgrößen wie Spannung oder Strom)! Heute werden fast ausschließlich Digitalcomputer eingesetzt. Diese folgen gemeinsamen Grundprinzipien, mit denen ihre freie Programmierung ermöglicht wird. Bei einem Digitalcomputer werden dabei zwei grundsätzliche Bestandteile unterschieden: Die Hardware, die aus den elektronischen, physisch anfassbaren Teilen des Computers gebildet wird, sowie die Software, die die Programmierung des Computers beschreibt. Erst durch eine Software wird der Digitalcomputer jedoch nützlich. Jede Software ist im Prinzip eine definierte, funktionale Anordnung der oben geschilderten Bausteine Berechnung, Vergleich und bedingter Sprung, wobei die Bausteine beliebig oft verwendet werden können. Diese Anordnung der Bausteine, die als Programm bezeichnet wird, wird in Form von Daten im Speicher des Computers abgelegt. Von dort kann sie von der Hardware ausgelesen und abgearbeitet werden.";
        Text germanText = new Text(german, Language.DE);
        Assert.assertEquals(
                        "Ein Computer ist ein Digitalcomputer, wenn er mit digitalen Geräteeinheiten digitale Daten verarbeitet (also Zahlen und Textzeichen). Er ist ein Analogcomputer, wenn er mit analogen Geräteeinheiten analoge Daten verarbeitet (also kontinuierlich verlaufende elektrische Messgrößen wie Spannung oder Strom)! Heute werden fast ausschließlich Digitalcomputer eingesetzt. Diese folgen gemeinsamen Grundprinzipien, mit denen ihre freie Programmierung ermöglicht wird. Bei einem Digitalcomputer werden dabei zwei grundsätzliche Bestandteile unterschieden: Die Hardware, die aus den elektronischen, physisch anfassbaren Teilen des Computers gebildet wird, sowie die Software, die die Programmierung des Computers beschreibt. Erst durch eine Software wird der Digitalcomputer jedoch nützlich. Jede Software ist im Prinzip eine definierte, funktionale Anordnung der oben geschilderten Bausteine Berechnung, Vergleich und bedingter Sprung, wobei die Bausteine beliebig oft verwendet werden können. Diese Anordnung der Bausteine, die als Programm bezeichnet wird, wird in Form von Daten im Speicher des Computers abgelegt. Von dort kann sie von der Hardware ausgelesen und abgearbeitet werden.",
                        germanText.getContent());

        //TODO Test words
    }
    
    @Test
    public void germanFileTest() throws FileNotFoundException {
        Text germanText = new Text(TestResources.class.getResourceAsStream("resources/PC(DE) - UTF8.txt"), Language.DE);
      //Paragraph 1
      Paragraph paragraph1 = germanText.getParagraph(1);
      Assert.assertEquals("Ein Computer ist ein Digitalcomputer, wenn er mit digitalen Geräteeinheiten digitale Daten verarbeitet (also Zahlen und Textzeichen).", paragraph1.getSentences().get(0).getContent());
      Assert.assertEquals("Er ist ein Analogcomputer, wenn er mit analogen Geräteeinheiten analoge Daten verarbeitet (also kontinuierlich verlaufende elektrische Messgrößen wie Spannung oder Strom)!", paragraph1.getSentences().get(1).getContent());
      
      //Paragraph2
      Paragraph paragraph2 = germanText.getParagraph(2);
      Assert.assertEquals("Heute werden fast ausschließlich Digitalcomputer eingesetzt.", paragraph2.getSentences().get(0).getContent());
      Assert.assertEquals("Diese folgen gemeinsamen Grundprinzipien, mit denen ihre freie Programmierung ermöglicht wird.", paragraph2.getSentences().get(1).getContent());
      Assert.assertEquals("Bei einem Digitalcomputer werden dabei zwei grundsätzliche Bestandteile unterschieden: Die Hardware, die aus den elektronischen, physisch anfassbaren Teilen des Computers gebildet wird, sowie die Software, die die Programmierung des Computers beschreibt.", paragraph2.getSentences().get(2).getContent());
      
      //Paragraph2
      Paragraph paragraph3 = germanText.getParagraph(3);
      Assert.assertEquals("Erst durch eine Software wird der Digitalcomputer jedoch nützlich.", paragraph3.getSentences().get(0).getContent());
      Assert.assertEquals("Jede Software ist im Prinzip eine definierte, funktionale Anordnung der oben geschilderten Bausteine Berechnung, Vergleich und bedingter Sprung, wobei die Bausteine beliebig oft verwendet werden können.", paragraph3.getSentences().get(1).getContent());
      Assert.assertEquals("Diese Anordnung der Bausteine, die als Programm bezeichnet wird, wird in Form von Daten im Speicher des Computers abgelegt.", paragraph3.getSentences().get(2).getContent());
      Assert.assertEquals("Von dort kann sie von der Hardware ausgelesen und abgearbeitet werden.", paragraph3.getSentences().get(3).getContent());
      
      //Paragraph Sentence has to be the same than text sentence
      Assert.assertEquals(paragraph1.getSentences().get(0).getContent(), germanText.getSentences().get(0).getContent());
      Assert.assertEquals(paragraph1.getSentences().get(1).getContent(), germanText.getSentences().get(1).getContent());
      Assert.assertEquals(paragraph2.getSentences().get(0).getContent(), germanText.getSentences().get(2).getContent());
      Assert.assertEquals(paragraph2.getSentences().get(1), germanText.getSentences().get(3));
      Assert.assertEquals(paragraph2.getSentences().get(2), germanText.getSentences().get(4));
      Assert.assertEquals(paragraph3.getSentences().get(0), germanText.getSentences().get(5));
      Assert.assertEquals(paragraph3.getSentences().get(1), germanText.getSentences().get(6));
      Assert.assertEquals(paragraph3.getSentences().get(2), germanText.getSentences().get(7));
      Assert.assertEquals(paragraph3.getSentences().get(3), germanText.getSentences().get(8));   

      //TODO Test words
    }
}
