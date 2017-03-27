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
import lu.list.itis.dkd.assess.cloze.util.Resources;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

public class EnglishClozeTest {
    
    @Test
    public void clozeMatchAnnotationApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.ANNOTATIONMATCH, false);
        String expectedCloze = "A ___1___ is ___2___ general-purpose ___3___ that can be programmed to carry out a set of ___4___ ___5___ ___6___ operations automatically. Since a ___7___ of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one ___8___ element, typically a ___9___ processing ___10___ ___11___ and some form of memory. The processing element carries out arithmetic ___12___ ___13___ operations, and a ___14___ and ___15___ unit can change the order of operations ___16___ response ___17___ ___18___ information. ___19___ ___20___ allow ___21___ to be retrieved from an external source, and the ___22___ of operations saved and retrieved. Mechanical ___23___ ___24___ started appearing in the first century and were later used in the ___25___ ___26___ for astronomical calculations. In ___27___ ___28___ ___29___ mechanical analog computers were used for specialized ___30___ ___31___ such as calculating torpedo aiming. During this time the first electronic ___32___ computers were developed. Originally they were the size of a large ___33___ consuming as much power as several hundred modern ___34___ computers (PCs). The Soviet ___35___ series of computers was developed from ___36___ to ___37___ in a group headed by Pierre Vinken and ___38___ Edison.";
        
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());
        
        //Keys in sentence 1
        Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0); 
        Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1); 
        Key key3 = clozeText.getClozeSentences().get(0).getKeys().get(2); 
        Key key4 = clozeText.getClozeSentences().get(0).getKeys().get(3); 
        Key key5 = clozeText.getClozeSentences().get(0).getKeys().get(4); 
        Key key6 = clozeText.getClozeSentences().get(0).getKeys().get(5); 
        
        Assert.assertEquals("computer", key1.getKeyWord().getContent());
        Assert.assertEquals("a", key2.getKeyWord().getContent());
        Assert.assertEquals("device", key3.getKeyWord().getContent());
        Assert.assertEquals("arithmetic", key4.getKeyWord().getContent());
        Assert.assertEquals("or", key5.getKeyWord().getContent());
        Assert.assertEquals("logical", key6.getKeyWord().getContent());
        
        //Keys in sentence 2
        Key key7 = clozeText.getClozeSentences().get(1).getKeys().get(0);
        Assert.assertEquals("sequence", key7.getKeyWord().getContent());
        
        //Keys in sentence 3
        key1 = clozeText.getClozeSentences().get(2).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(2).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(2).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(2).getKeys().get(3); 
        
        Assert.assertEquals("processing", key1.getKeyWord().getContent());
        Assert.assertEquals("central", key2.getKeyWord().getContent());
        Assert.assertEquals("unit", key3.getKeyWord().getContent());
        Assert.assertEquals("CPU", key4.getKeyWord().getContent());
        
        //Keys in sentence 4
        key1 = clozeText.getClozeSentences().get(3).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(3).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(3).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(3).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(3).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(3).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(3).getKeys().get(6); 
        
        Assert.assertEquals("and", key1.getKeyWord().getContent());
        Assert.assertEquals("logic", key2.getKeyWord().getContent());
        Assert.assertEquals("sequencing", key3.getKeyWord().getContent());
        Assert.assertEquals("control", key4.getKeyWord().getContent());
        Assert.assertEquals("in", key5.getKeyWord().getContent());
        Assert.assertEquals("to", key6.getKeyWord().getContent());
        Assert.assertEquals("stored", key7.getKeyWord().getContent());

        //Keys in sentence 5
        key1 = clozeText.getClozeSentences().get(4).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(4).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(4).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(4).getKeys().get(3); 
        
        Assert.assertEquals("Peripheral", key1.getKeyWord().getContent());
        Assert.assertEquals("devices", key2.getKeyWord().getContent());
        Assert.assertEquals("information", key3.getKeyWord().getContent());
        Assert.assertEquals("result", key4.getKeyWord().getContent());

        //Keys in sentence 6
        key1 = clozeText.getClozeSentences().get(5).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(5).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(5).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(5).getKeys().get(3); 
        
        Assert.assertEquals("analog", key1.getKeyWord().getContent());
        Assert.assertEquals("computers", key2.getKeyWord().getContent());
        Assert.assertEquals("medieval", key3.getKeyWord().getContent());
        Assert.assertEquals("era", key4.getKeyWord().getContent());

        //Keys in sentence 7
        key1 = clozeText.getClozeSentences().get(6).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(6).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(6).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(6).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(6).getKeys().get(4); 
        
        Assert.assertEquals("World", key1.getKeyWord().getContent());
        Assert.assertEquals("War", key2.getKeyWord().getContent());
        Assert.assertEquals("II", key3.getKeyWord().getContent());
        Assert.assertEquals("military", key4.getKeyWord().getContent());
        Assert.assertEquals("applications", key5.getKeyWord().getContent());

        //Keys in sentence 8        
        key1 = clozeText.getClozeSentences().get(7).getKeys().get(0); 
        Assert.assertEquals("digital", key1.getKeyWord().getContent());

        //Keys in sentence 9
        key1 = clozeText.getClozeSentences().get(8).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(8).getKeys().get(1); 
        
        Assert.assertEquals("room", key1.getKeyWord().getContent());
        Assert.assertEquals("personal", key2.getKeyWord().getContent());

        //Keys in sentence 10
        key1 = clozeText.getClozeSentences().get(9).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(9).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(9).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(9).getKeys().get(3); 
        
        Assert.assertEquals("MIR", key1.getKeyWord().getContent());
        Assert.assertEquals("1965", key2.getKeyWord().getContent());
        Assert.assertEquals("1969", key3.getKeyWord().getContent());
        Assert.assertEquals("Thomas", key4.getKeyWord().getContent());
    }
    
    @Test
    public void cApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.CTEST, true);
        String expectedCloze = "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since ________ sequence o________ operations c________ be rea________ changed, t________ computer c________ solve mo________ than o________ kind o________ problem! Conventionally, ________ computer cons________ of a________ least o________ processing ele________ typically ________ central proce________ unit C________ and so________ form o________ memory. The proce________ element car________ out arith________ and lo________ operations, a________ a seque________ and con________ unit c________ change t________ order o________ operations i________ response t________ stored information. Peripheral dev________ allow infor________ to b________ retrieved fr________ an exte________ source, a________ the res________ of opera________ saved a________ retrieved. Mechanical ana________ computers sta________ appearing i________ the fi________ century a________ were la________ used i________ the medi________ era f________ astronomical calculations. In Wo________ War I________ mechanical ana________ computers we________ used f________ specialized mili________ applications su________ as calcu________ torpedo aiming. During th________ time t________ first elect________ digital comp________ were developed. Originally th________ were t________ size o________ a la________ room, cons________ as mu________ power a________ several hun________ modern pers________ computers (PCs). The Sov________ MIR ser________ of comp________ was deve________ from 19________ to 19________ in ________ group hea________ by Pie________ Vinken a________ Thomas Edison.";

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
        
        Assert.assertEquals("a", key1.getKeyWord().getContent());
        Assert.assertEquals("of", key2.getKeyWord().getContent());
        Assert.assertEquals("can", key3.getKeyWord().getContent());
        Assert.assertEquals("readily", key4.getKeyWord().getContent());
        Assert.assertEquals("the", key5.getKeyWord().getContent());
        Assert.assertEquals("can", key6.getKeyWord().getContent());
        Assert.assertEquals("more", key7.getKeyWord().getContent());
        Assert.assertEquals("one", key8.getKeyWord().getContent());
        Assert.assertEquals("of", key9.getKeyWord().getContent());
        
        //Keys in sentence 3
        key1 = clozeText.getClozeSentences().get(2).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(2).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(2).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(2).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(2).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(2).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(2).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(2).getKeys().get(7); 
        key9 = clozeText.getClozeSentences().get(2).getKeys().get(8); 
        Key key10 = clozeText.getClozeSentences().get(2).getKeys().get(9); 
        
        Assert.assertEquals("a", key1.getKeyWord().getContent());
        Assert.assertEquals("consists", key2.getKeyWord().getContent());
        Assert.assertEquals("at", key3.getKeyWord().getContent());
        Assert.assertEquals("one", key4.getKeyWord().getContent());
        Assert.assertEquals("element", key5.getKeyWord().getContent());
        Assert.assertEquals("a", key6.getKeyWord().getContent());
        Assert.assertEquals("processing", key7.getKeyWord().getContent());
        Assert.assertEquals("CPU", key8.getKeyWord().getContent());
        Assert.assertEquals("some", key9.getKeyWord().getContent());
        Assert.assertEquals("of", key10.getKeyWord().getContent());
        
        //Keys in sentence 4
        key1 = clozeText.getClozeSentences().get(3).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(3).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(3).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(3).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(3).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(3).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(3).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(3).getKeys().get(7); 
        key9 = clozeText.getClozeSentences().get(3).getKeys().get(8); 
        key10 = clozeText.getClozeSentences().get(3).getKeys().get(9);
        Key key11 = clozeText.getClozeSentences().get(3).getKeys().get(10);
        Key key12 = clozeText.getClozeSentences().get(3).getKeys().get(11); 
        
        Assert.assertEquals("processing", key1.getKeyWord().getContent());
        Assert.assertEquals("carries", key2.getKeyWord().getContent());
        Assert.assertEquals("arithmetic", key3.getKeyWord().getContent());
        Assert.assertEquals("logic", key4.getKeyWord().getContent());
        Assert.assertEquals("and", key5.getKeyWord().getContent());
        Assert.assertEquals("sequencing", key6.getKeyWord().getContent());
        Assert.assertEquals("control", key7.getKeyWord().getContent());
        Assert.assertEquals("can", key8.getKeyWord().getContent());
        Assert.assertEquals("the", key9.getKeyWord().getContent());
        Assert.assertEquals("of", key10.getKeyWord().getContent());
        Assert.assertEquals("in", key11.getKeyWord().getContent());
        Assert.assertEquals("to", key12.getKeyWord().getContent());

        //Keys in sentence 5
        key1 = clozeText.getClozeSentences().get(4).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(4).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(4).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(4).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(4).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(4).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(4).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(4).getKeys().get(7); 
        key9 = clozeText.getClozeSentences().get(4).getKeys().get(8); 
        
        Assert.assertEquals("devices", key1.getKeyWord().getContent());
        Assert.assertEquals("information", key2.getKeyWord().getContent());
        Assert.assertEquals("be", key3.getKeyWord().getContent());
        Assert.assertEquals("from", key4.getKeyWord().getContent());
        Assert.assertEquals("external", key5.getKeyWord().getContent());
        Assert.assertEquals("and", key6.getKeyWord().getContent());
        Assert.assertEquals("result", key7.getKeyWord().getContent());
        Assert.assertEquals("operations", key8.getKeyWord().getContent());
        Assert.assertEquals("and", key9.getKeyWord().getContent());

        //Keys in sentence 6
        key1 = clozeText.getClozeSentences().get(5).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(5).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(5).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(5).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(5).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(5).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(5).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(5).getKeys().get(7); 
        key9 = clozeText.getClozeSentences().get(5).getKeys().get(8); 
        
        Assert.assertEquals("analog", key1.getKeyWord().getContent());
        Assert.assertEquals("started", key2.getKeyWord().getContent());
        Assert.assertEquals("in", key3.getKeyWord().getContent());
        Assert.assertEquals("first", key4.getKeyWord().getContent());
        Assert.assertEquals("and", key5.getKeyWord().getContent());
        Assert.assertEquals("later", key6.getKeyWord().getContent());
        Assert.assertEquals("in", key7.getKeyWord().getContent());
        Assert.assertEquals("medieval", key8.getKeyWord().getContent());
        Assert.assertEquals("for", key9.getKeyWord().getContent());

        //Keys in sentence 7
        key1 = clozeText.getClozeSentences().get(6).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(6).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(6).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(6).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(6).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(6).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(6).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(6).getKeys().get(7); 
        
        Assert.assertEquals("World", key1.getKeyWord().getContent());
        Assert.assertEquals("II", key2.getKeyWord().getContent());
        Assert.assertEquals("analog", key3.getKeyWord().getContent());
        Assert.assertEquals("were", key4.getKeyWord().getContent());
        Assert.assertEquals("for", key5.getKeyWord().getContent());
        Assert.assertEquals("military", key6.getKeyWord().getContent());
        Assert.assertEquals("such", key7.getKeyWord().getContent());
        Assert.assertEquals("calculating", key8.getKeyWord().getContent());

        //Keys in sentence 8
        key1 = clozeText.getClozeSentences().get(7).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(7).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(7).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(7).getKeys().get(3); 
        
        Assert.assertEquals("this", key1.getKeyWord().getContent());
        Assert.assertEquals("the", key2.getKeyWord().getContent());
        Assert.assertEquals("electronic", key3.getKeyWord().getContent());
        Assert.assertEquals("computers", key4.getKeyWord().getContent());

        //Keys in sentence 9
        key1 = clozeText.getClozeSentences().get(8).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(8).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(8).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(8).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(8).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(8).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(8).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(8).getKeys().get(7); 
        key9 = clozeText.getClozeSentences().get(8).getKeys().get(8); 
        
        Assert.assertEquals("they", key1.getKeyWord().getContent());
        Assert.assertEquals("the", key2.getKeyWord().getContent());
        Assert.assertEquals("of", key3.getKeyWord().getContent());
        Assert.assertEquals("large", key4.getKeyWord().getContent());
        Assert.assertEquals("consuming", key5.getKeyWord().getContent());
        Assert.assertEquals("much", key6.getKeyWord().getContent());
        Assert.assertEquals("as", key7.getKeyWord().getContent());
        Assert.assertEquals("hundred", key8.getKeyWord().getContent());
        Assert.assertEquals("personal", key9.getKeyWord().getContent());

        //Keys in sentence 10
        key1 = clozeText.getClozeSentences().get(9).getKeys().get(0); 
        key2 = clozeText.getClozeSentences().get(9).getKeys().get(1); 
        key3 = clozeText.getClozeSentences().get(9).getKeys().get(2); 
        key4 = clozeText.getClozeSentences().get(9).getKeys().get(3); 
        key5 = clozeText.getClozeSentences().get(9).getKeys().get(4); 
        key6 = clozeText.getClozeSentences().get(9).getKeys().get(5); 
        key7 = clozeText.getClozeSentences().get(9).getKeys().get(6); 
        key8 = clozeText.getClozeSentences().get(9).getKeys().get(7); 
        key9 = clozeText.getClozeSentences().get(9).getKeys().get(8); 
        key10 = clozeText.getClozeSentences().get(9).getKeys().get(9); 
        
        Assert.assertEquals("Soviet", key1.getKeyWord().getContent());
        Assert.assertEquals("series", key2.getKeyWord().getContent());
        Assert.assertEquals("computers", key3.getKeyWord().getContent());
        Assert.assertEquals("developed", key4.getKeyWord().getContent());
        Assert.assertEquals("1965", key5.getKeyWord().getContent());
        Assert.assertEquals("1969", key6.getKeyWord().getContent());
        Assert.assertEquals("a", key7.getKeyWord().getContent());
        Assert.assertEquals("headed", key8.getKeyWord().getContent());
        Assert.assertEquals(key9.getKeyWord().getContent(), "Pierre");
        Assert.assertEquals(key10.getKeyWord().getContent(), "and");
        
//        for (ClozeSentence clozeSentence : clozeText.getClozeSentences()) {
//            System.out.println(clozeSentence.getClozeSentence() + "\n");
//            for (Key key : clozeSentence.getKeys()) {
//                System.out.println(key.getKeyNumber() + ": " + key.getKey().getWord());
//            }
//            System.out.println();
//        }
    }

    //TODO Fix the 2 sentence which looks strange
    @Test
    public void definitionApproach() {
        String html = Wrapper.loadTextFile(Resources.class.getResourceAsStream("Glossary (EN).xml"));
        ClozeText clozeText = new ClozeText(html, "wiki-body", Language.EN, Approach.DEFINITION);
        
        Assert.assertEquals("Approach and activities aimed at the ___1___ within an organisation.", clozeText.getClozeSentences().get(0).getContent());
        Assert.assertEquals("An act or process through which something becomes different.", clozeText.getClozeSentences().get(1).getContent());
        Assert.assertEquals("___3___ in an organisational context occurs when organisational strategy major processes structures resources or procedures are altered.", clozeText.getClozeSentences().get(2).getContent());
        Assert.assertEquals("Fundamental thoroughgoing or extreme ___4___ usually taking place in a short time span.", clozeText.getClozeSentences().get(3).getContent());
        Assert.assertEquals("Relatively minor adjustment made toward an end result often taking place gradually over a period of time.", clozeText.getClozeSentences().get(4).getContent());
        Assert.assertEquals("Any person or group with an interest or concern in the implementation of ___6___ in your organisation.", clozeText.getClozeSentences().get(5).getContent());
        Assert.assertEquals("___7___s can be internal or external individual or collective actors.", clozeText.getClozeSentences().get(6).getContent());
        Assert.assertEquals("A person in charge of the ___8___ in the organisation.", clozeText.getClozeSentences().get(7).getContent());
        Assert.assertEquals("His or her activities typically involve identification selection and involvement of key stakeholders throughout the ___9___ success.", clozeText.getClozeSentences().get(8).getContent());
        Assert.assertEquals("A person in charge of the ___10___ activities related to the specific change management process.", clozeText.getClozeSentences().get(9).getContent());
        Assert.assertEquals("He or she is ___11___ on the change process toward the different actors of the organisation.", clozeText.getClozeSentences().get(10).getContent());
        Assert.assertEquals("A person who manages the technical dimensions of the learning and knowledge sharing platform and provides technical support to the users.", clozeText.getClozeSentences().get(11).getContent());
        Assert.assertEquals("___13___ members responsible for communication learning & development and technology.", clozeText.getClozeSentences().get(12).getContent());
        Assert.assertEquals("Highest ___14___ layer in the organisation responsible for organisational strategy.", clozeText.getClozeSentences().get(13).getContent());
        //        Assert.assertEquals("Oep ___17___ resources oer through institutional policies promote innovative pedagogical models and respect and empower learners as co- producers on their lifelong learning path opal 2012 p.", clozeText.getClozeSentences().get(16).getContent()); //Should be p.bla
        Assert.assertEquals("Often referred to as the way things are done around here.", clozeText.getClozeSentences().get(15).getContent());
        Assert.assertEquals("It is a set of behaviours typical for the specific organisation and the meaning that people attach to those behaviours.", clozeText.getClozeSentences().get(16).getContent());
        Assert.assertEquals("___18___ includes elements such as values norms systems symbols language assumptions beliefs and habits.", clozeText.getClozeSentences().get(17).getContent());
    }
    
    @Test
    public void verbApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.VERB);
        
//        for (ClozeSentence clozeSentence : clozeText.getClozeSentences()) {
//            System.out.println(clozeSentence.getContent());
//            for (final Key key : clozeSentence.getKeys()) {
//                System.out.print(key.getKeyWord().getContent() + ": ");
//                for (final Distractor distractor : key.getDistractors()) {
//                    System.out.print(distractor.getDistractorWord().getContent() + ", ");
//                }
//                System.out.print(")\n\n");
//            }
//        }
        
        String expectedCloze = "A computer ___1___ a general-purpose device that ___2___ to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations ___3___ the computer ___4___ more than one kind of problem! Conventionally a computer ___5___ of at least one processing element typically a central processing unit CPU and some form of memory. The processing element ___6___ out arithmetic and logic operations and a sequencing and control unit ___7___ the order of operations in response to stored information. Peripheral devices ___8___ information to be retrieved from an external source and the result of operations and retrieved. Mechanical analog computers ___9___ in the first century and ___10___ in the medieval era for astronomical calculations. In World War II mechanical analog computers ___11___ for specialized military applications such as torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room consuming as much power as several hundred modern personal computers PCs. The Soviet MIR series of computers ___12___ from 1965 to 1969 in a group by Pierre Vinken and Thomas Edison.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());
        
        //Key 1 & distractors in sentence 1
        Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0); 
        Assert.assertEquals("is", key1.getKeyWord().getContent());        
        Assert.assertEquals("are", key1.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("was", key1.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be", key1.getDistractors().get(2).getDistractorWord().getContent());
        
        //Key 2 & distractors in sentence 1
        Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1); 
        Assert.assertEquals("can be programmed", key2.getKeyWord().getContent());        
        Assert.assertEquals("is programmed", key2.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("was programmed", key2.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be programmed", key2.getDistractors().get(2).getDistractorWord().getContent()); 

        //Key 3 & distractors in sentence 2
        Key key3 = clozeText.getClozeSentences().get(1).getKeys().get(0); 
        Assert.assertEquals("can be readily changed", key3.getKeyWord().getContent());        
        Assert.assertEquals("are readily changed", key3.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("were readily changed", key3.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be readily changed", key3.getDistractors().get(2).getDistractorWord().getContent());

        //Key 4 & distractors in sentence 2
        Key key4 = clozeText.getClozeSentences().get(1).getKeys().get(1); 
        Assert.assertEquals("can solve", key4.getKeyWord().getContent());        
        Assert.assertEquals("solves", key4.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("solved", key4.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will solve", key4.getDistractors().get(2).getDistractorWord().getContent());

        //Key 5 & distractors in sentence 3
        Key key5 = clozeText.getClozeSentences().get(2).getKeys().get(0); 
        Assert.assertEquals("consists", key5.getKeyWord().getContent());        
        Assert.assertEquals("consist", key5.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("consisted", key5.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will consist", key5.getDistractors().get(2).getDistractorWord().getContent());

        //Key 6 & distractors in sentence 4
        Key key6 = clozeText.getClozeSentences().get(3).getKeys().get(0); 
        Assert.assertEquals("carries", key6.getKeyWord().getContent());        
        Assert.assertEquals("carry", key6.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("carried", key6.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will carry", key6.getDistractors().get(2).getDistractorWord().getContent());

        //Key 7 & distractors in sentence 4
        Key key7 = clozeText.getClozeSentences().get(3).getKeys().get(1); 
        Assert.assertEquals("can change", key7.getKeyWord().getContent());        
        Assert.assertEquals("changes", key7.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("changed", key7.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will change", key7.getDistractors().get(2).getDistractorWord().getContent());

        //Key 8 & distractors in sentence 5 --> to check... Text has allows
        Key key8 = clozeText.getClozeSentences().get(4).getKeys().get(0); 
        Assert.assertEquals("allow", key8.getKeyWord().getContent());        
        Assert.assertEquals("allows", key8.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("allowed", key8.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will allow", key8.getDistractors().get(2).getDistractorWord().getContent());

        //Key 9 & distractors in sentence 6
        Key key9 = clozeText.getClozeSentences().get(5).getKeys().get(0); 
        Assert.assertEquals("started appearing", key9.getKeyWord().getContent());        
        Assert.assertEquals("start appearing", key9.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("started appearing", key9.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will start appearing", key9.getDistractors().get(2).getDistractorWord().getContent());

        //Key 10 & distractors in sentence 6
        Key key10 = clozeText.getClozeSentences().get(5).getKeys().get(1); 
        Assert.assertEquals("were later used", key10.getKeyWord().getContent());        
        Assert.assertEquals("is later used", key10.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("was later used", key10.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be later used", key10.getDistractors().get(2).getDistractorWord().getContent());

        //Key 11 & distractors in sentence 7
        Key key11 = clozeText.getClozeSentences().get(6).getKeys().get(0); 
        Assert.assertEquals("were used", key11.getKeyWord().getContent());        
        Assert.assertEquals("are used", key11.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("were used", key11.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be used", key11.getDistractors().get(2).getDistractorWord().getContent());

        //Key 12 & distractors in sentence 10
        Key key12 = clozeText.getClozeSentences().get(9).getKeys().get(0); 
        Assert.assertEquals("was developed", key12.getKeyWord().getContent());        
        Assert.assertEquals("are developed", key12.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("were developed", key12.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be developed", key12.getDistractors().get(2).getDistractorWord().getContent());
        
    }
    
    @Test
    public void nounApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.NOUN);
       
        String expectedCloze = "A ___1___ is a general-purpose ___2___ that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one ___3___ of problem! Conventionally, a computer consists of at least one processing ___4___ typically a central processing unit (CPU), and some form of memory. The processing element carries out ___5___ and logic operations, and a ___6___ and control ___7___ can change the ___8___ of operations in response to stored information. Peripheral devices allow ___9___ to be retrieved from an external ___10___ and the ___11___ of operations saved and retrieved. Mechanical analog computers started appearing in the first ___12___ and were later used in the medieval ___13___ for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating ___14___ aiming. During this ___15___ the first electronic digital computers were developed. Originally they were the ___16___ of a large room, consuming as much power as several hundred modern personal computers (PCs). The Soviet MIR ___17___ of computers was developed from 1965 to 1969 in a group headed by Pierre Vinken and Thomas Edison.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());
        
        //Key 1 & distractors in sentence 1
        Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0); 
        Assert.assertEquals("computer", key1.getKeyWord().getContent());        
        Assert.assertEquals("calculator", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("hardware", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("mainframe", key1.getBestDistractors(3).get(2).getDistractorWord().getContent()); 
      
        //Key 2 & distractors in sentence 1
        Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1); 
        Assert.assertEquals("device", key2.getKeyWord().getContent());        
        Assert.assertEquals("setup", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("sensor", key2.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("circuitry", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 3 & distractors in sentence 2
        Key key3 = clozeText.getClozeSentences().get(1).getKeys().get(0); 
        Assert.assertEquals("kind", key3.getKeyWord().getContent());
        Assert.assertEquals("sort", key3.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("sense", key3.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("manner", key3.getBestDistractors(3).get(2).getDistractorWord().getContent());  
      
        //Key 4 & distractors in sentence 3
        Key key4 = clozeText.getClozeSentences().get(2).getKeys().get(0); 
        Assert.assertEquals("element", key4.getKeyWord().getContent());
        Assert.assertEquals("combination", key4.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("object", key4.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("example", key4.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 5 & distractors in sentence 4
        Key key5 = clozeText.getClozeSentences().get(3).getKeys().get(0); 
        Assert.assertEquals("arithmetic", key5.getKeyWord().getContent());
        Assert.assertEquals("recursion", key5.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("algebraic", key5.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("calculus", key5.getBestDistractors(3).get(2).getDistractorWord().getContent());
      
        //Key 6 & distractors in sentence 4
        Key key6 = clozeText.getClozeSentences().get(3).getKeys().get(1); 
        Assert.assertEquals("sequencing", key6.getKeyWord().getContent());
        Assert.assertEquals("microarray", key6.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("pcr", key6.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("illumina", key6.getBestDistractors(3).get(2).getDistractorWord().getContent());
      
        //Key 7 & distractors in sentence 4
        Key key7 = clozeText.getClozeSentences().get(3).getKeys().get(2); 
        Assert.assertEquals("unit", key7.getKeyWord().getContent());
        Assert.assertEquals("detachment", key7.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("deployable", key7.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("marsoc", key7.getBestDistractors(3).get(2).getDistractorWord().getContent());
      
        //Key 8 & distractors in sentence 4
        Key key8 = clozeText.getClozeSentences().get(3).getKeys().get(3); 
        Assert.assertEquals("order", key8.getKeyWord().getContent());
        Assert.assertEquals("oblige", key8.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("intent", key8.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("attempt", key8.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 9 & distractors in sentence 5
        Key key9 = clozeText.getClozeSentences().get(4).getKeys().get(0); 
        Assert.assertEquals("information", key9.getKeyWord().getContent());
        Assert.assertEquals("documentation", key9.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("database", key9.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("content", key9.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 10 & distractors in sentence 5
        Key key10 = clozeText.getClozeSentences().get(4).getKeys().get(1); 
        Assert.assertEquals("source", key10.getKeyWord().getContent());
        Assert.assertEquals("account", key10.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("attribution", key10.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("explanation", key10.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 11 & distractors in sentence 5
        Key key11 = clozeText.getClozeSentences().get(4).getKeys().get(2); 
        Assert.assertEquals("result", key11.getKeyWord().getContent());
        Assert.assertEquals("consequent", key11.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("cause", key11.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("consequence", key11.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 12 & distractors in sentence 6
        Key key12 = clozeText.getClozeSentences().get(5).getKeys().get(0); 
        Assert.assertEquals("century", key12.getKeyWord().getContent());
        Assert.assertEquals("period", key12.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("datable", key12.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("mediaeval", key12.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 13 & distractors in sentence 6
        Key key13 = clozeText.getClozeSentences().get(5).getKeys().get(1); 
        Assert.assertEquals("era", key13.getKeyWord().getContent());
        Assert.assertEquals("period", key13.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("decade", key13.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("heyday", key13.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 14 & distractors in sentence 7
        Key key14 = clozeText.getClozeSentences().get(6).getKeys().get(0); 
        Assert.assertEquals("torpedo", key14.getKeyWord().getContent());
        Assert.assertEquals("kormoran", key14.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("submarine", key14.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("sink", key14.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 15 & distractors in sentence 8
        Key key15 = clozeText.getClozeSentences().get(7).getKeys().get(0); 
        Assert.assertEquals("time", key15.getKeyWord().getContent());
        Assert.assertEquals("footballdatabase", key15.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("blueseum", key15.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("onsoranje", key15.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 16 & distractors in sentence 9
        Key key16 = clozeText.getClozeSentences().get(8).getKeys().get(0); 
        Assert.assertEquals("size", key16.getKeyWord().getContent());
        Assert.assertEquals("thickness", key16.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("density", key16.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("proportion", key16.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 17 & distractors in sentence 10
        Key key17 = clozeText.getClozeSentences().get(9).getKeys().get(0); 
        Assert.assertEquals("series", key17.getKeyWord().getContent());
        Assert.assertEquals("spinoff", key17.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("serial", key17.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("dramedy", key17.getBestDistractors(3).get(2).getDistractorWord().getContent());
    }

    @Test
    public void kodaApproach() {
        ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.ANNOTATION);
        
        String expectedCloze = "A ___1___ is a general-purpose ___2___ that can be programmed to carry out a set of ___3___ or ___4___ operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one ___5___ element, typically a ___6___ processing ___7___ (CPU), and some form of memory. The processing element carries out arithmetic and ___8___ operations, and a ___9___ and control unit can change the order of operations in response to stored information. Peripheral devices allow ___10___ to be retrieved from an external source, and the ___11___ of operations saved and retrieved. Mechanical ___12___ computers started appearing in the first century and were later used in the ___13___ ___14___ for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large ___15___ consuming as much power as several hundred modern personal computers (PCs). The Soviet MIR series of computers was developed from 1965 to 1969 in a group headed by Pierre Vinken and Thomas Edison.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());
        
        //Key 1 & distractors in sentence 1
        Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0); 
        Assert.assertEquals("computer", key1.getKeyWord().getContent());        
        Assert.assertEquals("calculator", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("hardware", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("mainframe", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());   

        //Key 2 & distractors in sentence 1
        Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1); 
        Assert.assertEquals("device", key2.getKeyWord().getContent());        
        Assert.assertEquals("setup", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("sensor", key2.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("circuitry", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 3 & distractors in sentence 1
        Key key3 = clozeText.getClozeSentences().get(0).getKeys().get(2); 
        Assert.assertEquals("arithmetic", key3.getKeyWord().getContent());
        Assert.assertEquals("intuitionistic", key3.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("equational", key3.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("propositional", key3.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 4 & distractors in sentence 1
        Key key4 = clozeText.getClozeSentences().get(0).getKeys().get(3); 
        Assert.assertEquals("logical", key4.getKeyWord().getContent());
        Assert.assertEquals("deductive", key4.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("propositional", key4.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("implicit", key4.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 5 & distractors in sentence 3
        Key key5 = clozeText.getClozeSentences().get(2).getKeys().get(0); 
        Assert.assertEquals("processing", key5.getKeyWord().getContent());
        Assert.assertEquals("application", key5.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("optimize", key5.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("retrieval", key5.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 6 & distractors in sentence 3
        Key key6 = clozeText.getClozeSentences().get(2).getKeys().get(1); 
        Assert.assertEquals("central", key6.getKeyWord().getContent());
        Assert.assertEquals("eastern", key6.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("northern", key6.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("southern", key6.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 7 & distractors in sentence 3
        Key key7 = clozeText.getClozeSentences().get(2).getKeys().get(2); 
        Assert.assertEquals("unit", key7.getKeyWord().getContent());
        Assert.assertEquals("detachment", key7.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("deployable", key7.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("marsoc", key7.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 8 & distractors in sentence 4
        Key key8 = clozeText.getClozeSentences().get(3).getKeys().get(0); 
        Assert.assertEquals("logic", key8.getKeyWord().getContent());
        Assert.assertEquals("paraconsistent", key8.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("analytic", key8.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("ontology", key8.getBestDistractors(3).get(2).getDistractorWord().getContent());        
        
        //Key 9 & distractors in sentence 4
        Key key9 = clozeText.getClozeSentences().get(3).getKeys().get(1);        
        Assert.assertEquals("sequencing", key9.getKeyWord().getContent());
        Assert.assertEquals("microarray", key9.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("pcr", key9.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("illumina", key9.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 10 & distractors in sentence 5
        Key key10 = clozeText.getClozeSentences().get(4).getKeys().get(0); 
        Assert.assertEquals("information", key10.getKeyWord().getContent());
        Assert.assertEquals("documentation", key10.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("database", key10.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("content", key10.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 11 & distractors in sentence 5
        Key key11 = clozeText.getClozeSentences().get(4).getKeys().get(1); 
        Assert.assertEquals("result", key11.getKeyWord().getContent());
        Assert.assertEquals("consequent", key11.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("cause", key11.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("consequence", key11.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 12 & distractors in sentence 6
        Key key12 = clozeText.getClozeSentences().get(5).getKeys().get(0); 
        Assert.assertEquals("analog", key12.getKeyWord().getContent());
        Assert.assertEquals("demodulator", key12.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("subcarrier", key12.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("nicam", key12.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 13 & distractors in sentence 6
        Key key13 = clozeText.getClozeSentences().get(5).getKeys().get(1); 
        Assert.assertEquals("medieval", key13.getKeyWord().getContent());
        Assert.assertEquals("ancient", key13.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("modern", key13.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("postclassical", key13.getBestDistractors(3).get(2).getDistractorWord().getContent());

        //Key 14 & distractors in sentence 6
        Key key14 = clozeText.getClozeSentences().get(5).getKeys().get(2); 
        Assert.assertEquals("era", key14.getKeyWord().getContent());
        Assert.assertEquals("period", key14.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("decade", key14.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("heyday", key14.getBestDistractors(3).get(2).getDistractorWord().getContent());
        
        //Key 15 & distractors in sentence 9
        Key key15 = clozeText.getClozeSentences().get(8).getKeys().get(0); 
        Assert.assertEquals("room", key15.getKeyWord().getContent());
        Assert.assertEquals("cubicle", key15.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("hallway", key15.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("basement", key15.getBestDistractors(3).get(2).getDistractorWord().getContent());
    }
}
