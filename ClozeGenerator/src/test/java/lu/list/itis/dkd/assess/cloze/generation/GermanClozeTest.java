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
package lu.list.itis.dkd.assess.cloze.generation;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.TestResources;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

public class GermanClozeTest {
    
    @Test
    public void cApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/Cafe (DE) - UTF8.txt")), Language.DE, Approach.CTEST, true);
        String expectedCloze = "Ganz in der Nähe der Universität gibt es ein gern beuchtes Café. Hier tre________ sich d________ Studenten zwis________ den Vorle________ sitzen b________ Tee, Kaf________ und Bröt________ lesen Zei________ unterhalten si________ bereiten Semi________ vor. Manche sit________ alleine, u________ zu le________ andere sit________ zusammen, u________ sich z________ unterhalten u________ um z________ diskutieren. Jeden T________ ist d________ Café geöf________ auch a________ Abend ka________ man do________ noch et________ tricken od________ essen.";

        Assert.assertEquals(expectedCloze, clozeText.getClozeText());
        
        //Keys in sentence 2
        Key key1 = clozeText.getClozeSentences().get(1).getKeys().get(0); 
        Key key2 = clozeText.getClozeSentences().get(1).getKeys().get(1); 
        Key key3 = clozeText.getClozeSentences().get(1).getKeys().get(2); 
        Key key4 = clozeText.getClozeSentences().get(1).getKeys().get(3); 
        Key key5 = clozeText.getClozeSentences().get(1).getKeys().get(4); 
        Key key6 = clozeText.getClozeSentences().get(1).getKeys().get(5); 
        Key key7 = clozeText.getClozeSentences().get(1).getKeys().get(6); 
        Key key8 = clozeText.getClozeSentences().get(1).getKeys().get(7); 
        Key key9 = clozeText.getClozeSentences().get(1).getKeys().get(8); 
        Key key10 = clozeText.getClozeSentences().get(1).getKeys().get(9); 
        
        Assert.assertEquals("treffen", key1.getKeyWord().getContent());
        Assert.assertEquals("die", key2.getKeyWord().getContent());
        Assert.assertEquals("zwischen", key3.getKeyWord().getContent());
        Assert.assertEquals("Vorlesungen", key4.getKeyWord().getContent());
        Assert.assertEquals("bei", key5.getKeyWord().getContent());
        Assert.assertEquals("Kaffee", key6.getKeyWord().getContent());
        Assert.assertEquals("Brötchen", key7.getKeyWord().getContent());
        Assert.assertEquals("Zeitung", key8.getKeyWord().getContent());
        Assert.assertEquals("sich", key9.getKeyWord().getContent());
        Assert.assertEquals("Seminare", key10.getKeyWord().getContent());

        //Keys in sentence 3
        key1 = clozeText.getClozeSentences().get(2).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(2).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(2).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(2).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(2).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(2).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(2).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(2).getKeys().get(7);

        Assert.assertEquals("sitzen", key1.getKeyWord().getContent());
        Assert.assertEquals("um", key2.getKeyWord().getContent());
        Assert.assertEquals("lesen", key3.getKeyWord().getContent());
        Assert.assertEquals("sitzen", key4.getKeyWord().getContent());
        Assert.assertEquals("um", key5.getKeyWord().getContent());
        Assert.assertEquals("zu", key6.getKeyWord().getContent());
        Assert.assertEquals("und", key7.getKeyWord().getContent());
        Assert.assertEquals("zu", key8.getKeyWord().getContent());
        
        //Keys in sentence 4
        key1 = clozeText.getClozeSentences().get(3).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(3).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(3).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(3).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(3).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(3).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(3).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(3).getKeys().get(7);

        Assert.assertEquals("Tag", key1.getKeyWord().getContent());
        Assert.assertEquals("das", key2.getKeyWord().getContent());
        Assert.assertEquals("geöffnet", key3.getKeyWord().getContent());
        Assert.assertEquals("am", key4.getKeyWord().getContent());
        Assert.assertEquals("kann", key5.getKeyWord().getContent());
        Assert.assertEquals("dort", key6.getKeyWord().getContent());
        Assert.assertEquals("etwas", key7.getKeyWord().getContent());
        Assert.assertEquals("oder", key8.getKeyWord().getContent());
    }
    
    @Test
    public void verbApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(DE) - UTF8.txt")), Language.DE, Approach.VERB, 3, false);
        
        String expectedCloze = "Ein Computer ___1___ ein Digitalcomputer wenn er mit digitalen Geräteeinheiten digitale Daten ___2___ also Zahlen und Textzeichen. Er ist ein Analogcomputer wenn er mit analogen Geräteeinheiten analoge Daten verarbeitet also kontinuierlich verlaufende elektrische Messgrößen wie Spannung oder Strom! Heute ___3___ fast ausschließlich Digitalcomputer eingesetzt. Diese folgen gemeinsamen Grundprinzipien mit denen ihre freie Programmierung ermöglicht wird. Bei einem Digitalcomputer werden dabei zwei grundsätzliche Bestandteile unterschieden. Die Hardware die aus den elektronischen physisch anfassbaren Teilen des Computers gebildet ___4___ sowie die Software die die Programmierung des Computers beschreibt. Erst durch eine Software wird der Digitalcomputer jedoch nützlich. Jede Software ist im Prinzip eine definierte funktionale Anordnung der oben geschilderten Bausteine Berechnung Vergleich und bedingter Sprung wobei die Bausteine beliebig oft verwendet werden können. Diese Anordnung der Bausteine die als Programm bezeichnet wird wird in Form von Daten im Speicher des Computers abgelegt. Von dort ___5___ sie von der Hardware ___6___ und abgearbeitet werden.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());
        
        //Key 1 & distractors in sentence 1
        Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0); 
        Assert.assertEquals("ist", key1.getKeyWord().getContent());        
        Assert.assertEquals("sind", key1.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("war", key1.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("wird sein", key1.getDistractors().get(2).getDistractorWord().getContent());
        
        //Key 2 & distractors in sentence 1
        Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1); 
        Assert.assertEquals("verarbeitet", key2.getKeyWord().getContent());        
        Assert.assertEquals("verarbeiteten", key2.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("verarbeitetten", key2.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("werden verarbeitet", key2.getDistractors().get(2).getDistractorWord().getContent());

        //Key 3 & distractors in sentence 3
        Key key3 = clozeText.getClozeSentences().get(2).getKeys().get(0); 
        Assert.assertEquals("werden", key3.getKeyWord().getContent());        
        Assert.assertEquals("wird", key3.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("wurde", key3.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("wird werden", key3.getDistractors().get(2).getDistractorWord().getContent());
        
        //Key 4 & distractors in sentence 6
        Key key4 = clozeText.getClozeSentences().get(5).getKeys().get(0); 
        Assert.assertEquals("wird", key4.getKeyWord().getContent());        
        Assert.assertEquals("wurde", key4.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("wird werden", key4.getDistractors().get(1).getDistractorWord().getContent());
        
        //Key 5 & distractors in sentence 10
        Key key5 = clozeText.getClozeSentences().get(9).getKeys().get(0); 
        Assert.assertEquals("kann", key5.getKeyWord().getContent());
        Assert.assertEquals("konnte", key5.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("wird können", key5.getDistractors().get(1).getDistractorWord().getContent());
        
        //Key 6 & distractors in sentence 10
        Key key6 = clozeText.getClozeSentences().get(9).getKeys().get(1); 
        Assert.assertEquals("ausgelesen", key6.getKeyWord().getContent());        
        Assert.assertEquals("ausgelest", key6.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("ausgeleste", key6.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("wird ausgelesen", key6.getDistractors().get(2).getDistractorWord().getContent());
    }

    @Test
    public void kodaApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(DE) - UTF8.txt")), Language.DE, Approach.ANNOTATION, false);
        
        String expectedCloze = "Ein ___1___ ist ein Digitalcomputer, wenn er mit digitalen Geräteeinheiten digitale ___2___ verarbeitet (also ___3___ und Textzeichen). Er ist ein Analogcomputer, wenn er mit analogen Geräteeinheiten analoge Daten verarbeitet (also kontinuierlich verlaufende elektrische Messgrößen wie Spannung oder Strom)! Heute werden fast ausschließlich Digitalcomputer eingesetzt. Diese folgen gemeinsamen Grundprinzipien, mit denen ihre freie ___4___ ermöglicht wird. Bei einem Digitalcomputer werden dabei zwei grundsätzliche Bestandteile unterschieden. Die ___5___ die aus den elektronischen, physisch anfassbaren Teilen des Computers gebildet wird, sowie die ___6___ die die Programmierung des Computers beschreibt. Erst durch eine Software wird der Digitalcomputer jedoch nützlich. Jede Software ist im ___7___ eine definierte, funktionale Anordnung der oben geschilderten Bausteine Berechnung, ___8___ und bedingter Sprung, wobei die Bausteine beliebig ___9___ verwendet werden können. Diese Anordnung der Bausteine, die als ___10___ bezeichnet wird, wird in ___11___ von Daten im Speicher des Computers abgelegt. Von dort kann sie von der Hardware ausgelesen und abgearbeitet werden.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());
        
        //Key 1 & distractors in sentence 1
        Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0); 
        Assert.assertEquals("Computer", key1.getKeyWord().getContent());
        Assert.assertEquals("PC", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Rechner", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Laptop", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());   

        //Key 2 & distractors in sentence 1
        Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1); 
        Assert.assertEquals("Daten", key2.getKeyWord().getContent());
        Assert.assertEquals("Benutzerinformation", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Nutzungsprofil", key2.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Nutzungsstatistik", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 3 & distractors in sentence 1
        Key key3 = clozeText.getClozeSentences().get(0).getKeys().get(2); 
        Assert.assertEquals("Zahlen", key3.getKeyWord().getContent());
        Assert.assertEquals("Haushaltszahl", key3.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Steuerschätzung", key3.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Vergleichszahl", key3.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 4 & distractors in sentence 4
        Key key4 = clozeText.getClozeSentences().get(3).getKeys().get(0); 
        Assert.assertEquals("Programmierung", key4.getKeyWord().getContent());
        Assert.assertEquals("Modelltransformation", key4.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Codegenerierung", key4.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Objektorientierung", key4.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 5 & distractors in sentence 6
        Key key5 = clozeText.getClozeSentences().get(5).getKeys().get(0); 
        Assert.assertEquals("Hardware", key5.getKeyWord().getContent());
        Assert.assertEquals("Treiber", key5.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Betriebssystem", key5.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("PCs", key5.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 6 & distractors in sentence 6
        Key key6 = clozeText.getClozeSentences().get(5).getKeys().get(1); 
        Assert.assertEquals("Software", key6.getKeyWord().getContent());
        Assert.assertEquals("Applikation", key6.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Tool", key6.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Anwender", key6.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 7 & distractors in sentence 8
        Key key7 = clozeText.getClozeSentences().get(7).getKeys().get(0); 
        Assert.assertEquals("Prinzip", key7.getKeyWord().getContent());
        Assert.assertEquals("Grundgedanke", key7.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Postulat", key7.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Grundsatz", key7.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 8 & distractors in sentence 8
        Key key8 = clozeText.getClozeSentences().get(7).getKeys().get(1); 
        Assert.assertEquals("Vergleich", key8.getKeyWord().getContent());
        Assert.assertEquals("Gegensatz", key8.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Unterschied", key8.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Promillebereich", key8.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 9 & distractors in sentence 8
        Key key9 = clozeText.getClozeSentences().get(7).getKeys().get(2); 
        Assert.assertEquals("oft", key9.getKeyWord().getContent());
        Assert.assertEquals("meistens", key9.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("mitunter", key9.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("manchmal", key9.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 10 & distractors in sentence 9
        Key key10 = clozeText.getClozeSentences().get(8).getKeys().get(0); 
        Assert.assertEquals("Programm", key10.getKeyWord().getContent());
        Assert.assertEquals("Softwarepaket", key10.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Tool", key10.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Anwender", key10.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 11 & distractors in sentence 9
        Key key11 = clozeText.getClozeSentences().get(8).getKeys().get(1); 
        Assert.assertEquals("Form", key11.getKeyWord().getContent());
        Assert.assertEquals("Element", key11.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("Dimension", key11.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("Ausprägung", key11.getBestDistractors(3).get(2).getDistractorWord().getContent());
    }
}
