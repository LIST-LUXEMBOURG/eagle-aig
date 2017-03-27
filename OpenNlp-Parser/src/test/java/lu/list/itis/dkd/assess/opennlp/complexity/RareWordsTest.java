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
package lu.list.itis.dkd.assess.opennlp.complexity;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.1.1
 * @version 1.0.0
 */
public class RareWordsTest {

    @Test
    public void rareEnglishWords() {
        String english = "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved. Mechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs).";
        Text englishText = new Text(english, Language.EN);
        
        List<String> rareWords = new ArrayList<>();
        for (Sentence sentence : englishText.getSentences()) {
            for (Word word : sentence.getWords()){
                if (word.isRare()){
                    rareWords.add(word.getContent());
                }
            }
        }
        
        Assert.assertEquals(rareWords.get(0), "general-purpose");
        Assert.assertEquals(rareWords.get(1), "PCs");
    }
    
    @Test
    public void rareGermanWords() {
        String german = "Ein Computer ist ein Digitalcomputer, wenn er mit digitalen Geräteeinheiten digitale Daten verarbeitet (also Zahlen und Textzeichen). Er ist ein Analogcomputer, wenn er mit analogen Geräteeinheiten analoge Daten verarbeitet (also kontinuierlich verlaufende elektrische Messgrößen wie Spannung oder Strom)! Heute werden fast ausschließlich Digitalcomputer eingesetzt. Diese folgen gemeinsamen Grundprinzipien, mit denen ihre freie Programmierung ermöglicht wird. Bei einem Digitalcomputer werden dabei zwei grundsätzliche Bestandteile unterschieden: Die Hardware, die aus den elektronischen, physisch anfassbaren Teilen des Computers gebildet wird, sowie die Software, die die Programmierung des Computers beschreibt. Erst durch eine Software wird der Digitalcomputer jedoch nützlich. Jede Software ist im Prinzip eine definierte, funktionale Anordnung der oben geschilderten Bausteine Berechnung, Vergleich und bedingter Sprung, wobei die Bausteine beliebig oft verwendet werden können. Diese Anordnung der Bausteine, die als Programm bezeichnet wird, wird in Form von Daten im Speicher des Computers abgelegt. Von dort kann sie von der Hardware ausgelesen und abgearbeitet werden.";
        Text germanText = new Text(german, Language.DE);
        
        List<String> rareWords = new ArrayList<>();
        for (Sentence sentence : germanText.getSentences()) {
            for (Word word : sentence.getWords()){
                if (word.isRare()){
                    rareWords.add(word.getContent());
                }
            }
        }
        
        Assert.assertEquals(rareWords.get(0), "Geräteeinheiten");
        Assert.assertEquals(rareWords.get(1), "Geräteeinheiten");
        Assert.assertEquals(rareWords.get(2), "Messgrößen");
        Assert.assertEquals(rareWords.get(3), "ausschließlich");
        Assert.assertEquals(rareWords.get(4), "ermöglicht");
        Assert.assertEquals(rareWords.get(5), "grundsätzliche");
        Assert.assertEquals(rareWords.get(6), "nützlich");
        Assert.assertEquals(rareWords.get(7), "können");
    }
    
    @Test
    public void rareFrenchWords() {
        String french = "Dans le cadre d’une relation de travail, le salaire constitue la contrepartie directe du travail effectué par le salarié au profit de son employeur";
        Text frenchText = new Text(french, Language.FR);
        
        List<String> rareWords = new ArrayList<>();
        for (Sentence sentence : frenchText.getSentences()) {
            for (Word word : sentence.getWords()){
                if (word.isRare()){
                    rareWords.add(word.getContent());
                }
            }
        }
        
        Assert.assertEquals(rareWords.get(0), "effectué");
        Assert.assertEquals(rareWords.get(1), "salarié");
    }
}
