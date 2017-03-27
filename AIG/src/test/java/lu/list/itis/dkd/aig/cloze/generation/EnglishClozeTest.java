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
package lu.list.itis.dkd.aig.cloze.generation;

import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

import org.junit.Assert;
import org.junit.Test;

public class EnglishClozeTest {

    // @Test
    // public void definitionApproach() throws IOException {
    // final String html = Wrapper.loadTextFile("test\\resources\\Glossary (EN).xml");
    // final ClozeText clozeText = new ClozeText(html, "wiki-body", Language.EN,
    // Approach.DEFINITION);
    //
    // for (final ClozeSentence clozeSentence : clozeText.getClozeSentences()) {
    // System.out.print("Definition: " + clozeSentence.getClozeSentence() + "[");
    // for (final Key key : clozeSentence.getKeys()) {
    // System.out.print(key.getKeyWord().getContent() + ": ");
    // for (final Distractor distractor : key.getBestDistractors(3)) {
    // System.out.print(distractor.getDistractorWord().getContent() + ", ");
    // }
    // }
    // System.out.println("]");
    // }
    // }

    @Test
    public void nounApproach() {
        final ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.NOUN, 3, false);

        final String expectedCloze =
                        "A ___1___ is a general-purpose ___2___ that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one ___3___ of problem! Conventionally, a computer consists of at least one processing ___4___ typically a central processing unit (CPU), and some form of memory. The processing element carries out ___5___ and logic operations, and a ___6___ and control ___7___ can change the ___8___ of operations in response to stored information. Peripheral devices allow ___9___ to be retrieved from an external ___10___ and the ___11___ of operations saved and retrieved. Mechanical analog computers started appearing in the first ___12___ and were later used in the medieval ___13___ for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating ___14___ aiming. During this ___15___ the first electronic digital computers were developed. Originally they were the ___16___ of a large room, consuming as much power as several hundred modern personal computers (PCs). The Soviet MIR ___17___ of computers was developed from 1965 to 1969 in a group headed by Pierre Vinken and Thomas Edison.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());

        // Key 1 & distractors in sentence 1
        final Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0);
        Assert.assertEquals(key1.getKeyWord().getContent(), "computer");
        Assert.assertEquals(key1.getBestDistractors(3).get(0).getDistractorWord().getContent(), "calculator");
        Assert.assertEquals(key1.getBestDistractors(3).get(1).getDistractorWord().getContent(), "hardware");
        Assert.assertEquals(key1.getBestDistractors(3).get(2).getDistractorWord().getContent(), "mainframe");

        // Key 2 & distractors in sentence 1
        final Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1);
        Assert.assertEquals(key2.getKeyWord().getContent(), "device");
        Assert.assertEquals(key2.getBestDistractors(3).get(0).getDistractorWord().getContent(), "setup");
        Assert.assertEquals(key2.getBestDistractors(3).get(1).getDistractorWord().getContent(), "sensor");
        Assert.assertEquals(key2.getBestDistractors(3).get(2).getDistractorWord().getContent(), "circuitry");

        // Key 3 & distractors in sentence 2
        final Key key3 = clozeText.getClozeSentences().get(1).getKeys().get(0);
        Assert.assertEquals(key3.getKeyWord().getContent(), "kind");
        Assert.assertEquals(key3.getBestDistractors(3).get(0).getDistractorWord().getContent(), "sort");
        Assert.assertEquals(key3.getBestDistractors(3).get(1).getDistractorWord().getContent(), "sense");
        Assert.assertEquals(key3.getBestDistractors(3).get(2).getDistractorWord().getContent(), "manner");

        // Key 4 & distractors in sentence 3
        final Key key4 = clozeText.getClozeSentences().get(2).getKeys().get(0);
        Assert.assertEquals(key4.getKeyWord().getContent(), "element");
        Assert.assertEquals(key4.getBestDistractors(3).get(0).getDistractorWord().getContent(), "combination");
        Assert.assertEquals(key4.getBestDistractors(3).get(1).getDistractorWord().getContent(), "object");
        Assert.assertEquals(key4.getBestDistractors(3).get(2).getDistractorWord().getContent(), "example");

        // Key 5 & distractors in sentence 4
        final Key key5 = clozeText.getClozeSentences().get(3).getKeys().get(0);
        Assert.assertEquals(key5.getKeyWord().getContent(), "arithmetic");
        Assert.assertEquals(key5.getBestDistractors(3).get(0).getDistractorWord().getContent(), "recursion");
        Assert.assertEquals(key5.getBestDistractors(3).get(1).getDistractorWord().getContent(), "algebraic");
        Assert.assertEquals(key5.getBestDistractors(3).get(2).getDistractorWord().getContent(), "calculus");

        // Key 6 & distractors in sentence 4
        final Key key6 = clozeText.getClozeSentences().get(3).getKeys().get(1);
        Assert.assertEquals(key6.getKeyWord().getContent(), "sequencing");
        Assert.assertEquals(key6.getBestDistractors(3).get(0).getDistractorWord().getContent(), "microarray");
        Assert.assertEquals(key6.getBestDistractors(3).get(1).getDistractorWord().getContent(), "pcr");
        Assert.assertEquals(key6.getBestDistractors(3).get(2).getDistractorWord().getContent(), "illumina");

        // Key 7 & distractors in sentence 4
        final Key key7 = clozeText.getClozeSentences().get(3).getKeys().get(2);
        Assert.assertEquals(key7.getKeyWord().getContent(), "unit");
        Assert.assertEquals(key7.getBestDistractors(3).get(0).getDistractorWord().getContent(), "detachment");
        Assert.assertEquals(key7.getBestDistractors(3).get(1).getDistractorWord().getContent(), "deployable");
        Assert.assertEquals(key7.getBestDistractors(3).get(2).getDistractorWord().getContent(), "marsoc");

        // Key 8 & distractors in sentence 4
        final Key key8 = clozeText.getClozeSentences().get(3).getKeys().get(3);
        Assert.assertEquals(key8.getKeyWord().getContent(), "order");
        Assert.assertEquals(key8.getBestDistractors(3).get(0).getDistractorWord().getContent(), "oblige");
        Assert.assertEquals(key8.getBestDistractors(3).get(1).getDistractorWord().getContent(), "intent");
        Assert.assertEquals(key8.getBestDistractors(3).get(2).getDistractorWord().getContent(), "attempt");

        // Key 9 & distractors in sentence 5
        final Key key9 = clozeText.getClozeSentences().get(4).getKeys().get(0);
        Assert.assertEquals(key9.getKeyWord().getContent(), "information");
        Assert.assertEquals(key9.getBestDistractors(3).get(0).getDistractorWord().getContent(), "documentation");
        Assert.assertEquals(key9.getBestDistractors(3).get(1).getDistractorWord().getContent(), "database");
        Assert.assertEquals(key9.getBestDistractors(3).get(2).getDistractorWord().getContent(), "content");

        // Key 10 & distractors in sentence 5
        final Key key10 = clozeText.getClozeSentences().get(4).getKeys().get(1);
        Assert.assertEquals(key10.getKeyWord().getContent(), "source");
        Assert.assertEquals(key10.getBestDistractors(3).get(0).getDistractorWord().getContent(), "account");
        Assert.assertEquals(key10.getBestDistractors(3).get(1).getDistractorWord().getContent(), "attribution");
        Assert.assertEquals(key10.getBestDistractors(3).get(2).getDistractorWord().getContent(), "explanation");

        // Key 11 & distractors in sentence 5
        final Key key11 = clozeText.getClozeSentences().get(4).getKeys().get(2);
        Assert.assertEquals(key11.getKeyWord().getContent(), "result");
        Assert.assertEquals(key11.getBestDistractors(3).get(0).getDistractorWord().getContent(), "consequent");
        Assert.assertEquals(key11.getBestDistractors(3).get(1).getDistractorWord().getContent(), "cause");
        Assert.assertEquals(key11.getBestDistractors(3).get(2).getDistractorWord().getContent(), "consequence");

        // Key 12 & distractors in sentence 6
        final Key key12 = clozeText.getClozeSentences().get(5).getKeys().get(0);
        Assert.assertEquals(key12.getKeyWord().getContent(), "century");
        Assert.assertEquals(key12.getBestDistractors(3).get(0).getDistractorWord().getContent(), "period");
        Assert.assertEquals(key12.getBestDistractors(3).get(1).getDistractorWord().getContent(), "datable");
        Assert.assertEquals(key12.getBestDistractors(3).get(2).getDistractorWord().getContent(), "mediaeval");

        // Key 13 & distractors in sentence 6
        final Key key13 = clozeText.getClozeSentences().get(5).getKeys().get(1);
        Assert.assertEquals(key13.getKeyWord().getContent(), "era");
        Assert.assertEquals(key13.getBestDistractors(3).get(0).getDistractorWord().getContent(), "period");
        Assert.assertEquals(key13.getBestDistractors(3).get(1).getDistractorWord().getContent(), "decade");
        Assert.assertEquals(key13.getBestDistractors(3).get(2).getDistractorWord().getContent(), "heyday");

        // Key 14 & distractors in sentence 7
        final Key key14 = clozeText.getClozeSentences().get(6).getKeys().get(0);
        Assert.assertEquals(key14.getKeyWord().getContent(), "torpedo");
        Assert.assertEquals(key14.getBestDistractors(3).get(0).getDistractorWord().getContent(), "kormoran");
        Assert.assertEquals(key14.getBestDistractors(3).get(1).getDistractorWord().getContent(), "submarine");
        Assert.assertEquals(key14.getBestDistractors(3).get(2).getDistractorWord().getContent(), "sink");

        // Key 15 & distractors in sentence 8
        final Key key15 = clozeText.getClozeSentences().get(7).getKeys().get(0);
        Assert.assertEquals(key15.getKeyWord().getContent(), "time");
        Assert.assertEquals(key15.getBestDistractors(3).get(0).getDistractorWord().getContent(), "footballdatabase");
        Assert.assertEquals(key15.getBestDistractors(3).get(1).getDistractorWord().getContent(), "blueseum");
        Assert.assertEquals(key15.getBestDistractors(3).get(2).getDistractorWord().getContent(), "onsoranje");

        // Key 16 & distractors in sentence 9
        final Key key16 = clozeText.getClozeSentences().get(8).getKeys().get(0);
        Assert.assertEquals(key16.getKeyWord().getContent(), "size");
        Assert.assertEquals(key16.getBestDistractors(3).get(0).getDistractorWord().getContent(), "thickness");
        Assert.assertEquals(key16.getBestDistractors(3).get(1).getDistractorWord().getContent(), "density");
        Assert.assertEquals(key16.getBestDistractors(3).get(2).getDistractorWord().getContent(), "proportion");

        // Key 17 & distractors in sentence 10
        final Key key17 = clozeText.getClozeSentences().get(9).getKeys().get(0);
        Assert.assertEquals(key17.getKeyWord().getContent(), "series");
        Assert.assertEquals(key17.getBestDistractors(3).get(0).getDistractorWord().getContent(), "spinoff");
        Assert.assertEquals(key17.getBestDistractors(3).get(1).getDistractorWord().getContent(), "serial");
        Assert.assertEquals(key17.getBestDistractors(3).get(2).getDistractorWord().getContent(), "dramedy");
    }

    @Test
    public void kodaApproach() {
        final ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.ANNOTATION, 3, false);

        final String expectedCloze =
                        "A ___1___ is a general-purpose ___2___ that can be programmed to carry out a set of ___3___ or ___4___ operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one ___5___ element, typically a ___6___ processing ___7___ (CPU), and some form of memory. The processing element carries out arithmetic and ___8___ operations, and a ___9___ and control unit can change the order of operations in response to stored information. Peripheral devices allow ___10___ to be retrieved from an external source, and the ___11___ of operations saved and retrieved. Mechanical ___12___ computers started appearing in the first century and were later used in the ___13___ ___14___ for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large ___15___ consuming as much power as several hundred modern personal computers (PCs). The Soviet MIR series of computers was developed from 1965 to 1969 in a group headed by Pierre Vinken and Thomas Edison.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());

        // Key 1 & distractors in sentence 1
        final Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0);
        Assert.assertEquals(key1.getKeyWord().getContent(), "computer");
        Assert.assertEquals(key1.getBestDistractors(3).get(0).getDistractorWord().getContent(), "calculator");
        Assert.assertEquals(key1.getBestDistractors(3).get(1).getDistractorWord().getContent(), "hardware");
        Assert.assertEquals(key1.getBestDistractors(3).get(2).getDistractorWord().getContent(), "mainframe");

        // Key 2 & distractors in sentence 1
        final Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1);
        Assert.assertEquals(key2.getKeyWord().getContent(), "device");
        Assert.assertEquals(key2.getBestDistractors(3).get(0).getDistractorWord().getContent(), "setup");
        Assert.assertEquals(key2.getBestDistractors(3).get(1).getDistractorWord().getContent(), "sensor");
        Assert.assertEquals(key2.getBestDistractors(3).get(2).getDistractorWord().getContent(), "circuitry");

        // Key 3 & distractors in sentence 1
        final Key key3 = clozeText.getClozeSentences().get(0).getKeys().get(2);
        Assert.assertEquals(key3.getKeyWord().getContent(), "arithmetic");
        Assert.assertEquals(key3.getBestDistractors(3).get(0).getDistractorWord().getContent(), "intuitionistic");
        Assert.assertEquals(key3.getBestDistractors(3).get(1).getDistractorWord().getContent(), "equational");
        Assert.assertEquals(key3.getBestDistractors(3).get(2).getDistractorWord().getContent(), "propositional");

        // Key 4 & distractors in sentence 1
        final Key key4 = clozeText.getClozeSentences().get(0).getKeys().get(3);
        Assert.assertEquals(key4.getKeyWord().getContent(), "logical");
        Assert.assertEquals(key4.getBestDistractors(3).get(0).getDistractorWord().getContent(), "deductive");
        Assert.assertEquals(key4.getBestDistractors(3).get(1).getDistractorWord().getContent(), "propositional");
        Assert.assertEquals(key4.getBestDistractors(3).get(2).getDistractorWord().getContent(), "implicit");

        // Key 5 & distractors in sentence 3
        final Key key5 = clozeText.getClozeSentences().get(2).getKeys().get(0);
        Assert.assertEquals(key5.getKeyWord().getContent(), "processing");
        Assert.assertEquals(key5.getBestDistractors(3).get(0).getDistractorWord().getContent(), "application");
        Assert.assertEquals(key5.getBestDistractors(3).get(1).getDistractorWord().getContent(), "optimize");
        Assert.assertEquals(key5.getBestDistractors(3).get(2).getDistractorWord().getContent(), "retrieval");

        // Key 6 & distractors in sentence 3
        final Key key6 = clozeText.getClozeSentences().get(2).getKeys().get(1);
        Assert.assertEquals(key6.getKeyWord().getContent(), "central");
        Assert.assertEquals(key6.getBestDistractors(3).get(0).getDistractorWord().getContent(), "eastern");
        Assert.assertEquals(key6.getBestDistractors(3).get(1).getDistractorWord().getContent(), "northern");
        Assert.assertEquals(key6.getBestDistractors(3).get(2).getDistractorWord().getContent(), "southern");

        // Key 7 & distractors in sentence 3
        final Key key7 = clozeText.getClozeSentences().get(2).getKeys().get(2);
        Assert.assertEquals(key7.getKeyWord().getContent(), "unit");
        Assert.assertEquals(key7.getBestDistractors(3).get(0).getDistractorWord().getContent(), "detachment");
        Assert.assertEquals(key7.getBestDistractors(3).get(1).getDistractorWord().getContent(), "deployable");
        Assert.assertEquals(key7.getBestDistractors(3).get(2).getDistractorWord().getContent(), "marsoc");

        // Key 8 & distractors in sentence 4
        final Key key8 = clozeText.getClozeSentences().get(3).getKeys().get(0);
        Assert.assertEquals(key8.getKeyWord().getContent(), "logic");
        Assert.assertEquals(key8.getBestDistractors(3).get(0).getDistractorWord().getContent(), "paraconsistent");
        Assert.assertEquals(key8.getBestDistractors(3).get(1).getDistractorWord().getContent(), "analytic");
        Assert.assertEquals(key8.getBestDistractors(3).get(2).getDistractorWord().getContent(), "ontology");

        // Key 9 & distractors in sentence 5
        final Key key9 = clozeText.getClozeSentences().get(4).getKeys().get(0);
        Assert.assertEquals(key9.getKeyWord().getContent(), "information");
        Assert.assertEquals(key9.getBestDistractors(3).get(0).getDistractorWord().getContent(), "documentation");
        Assert.assertEquals(key9.getBestDistractors(3).get(1).getDistractorWord().getContent(), "database");
        Assert.assertEquals(key9.getBestDistractors(3).get(2).getDistractorWord().getContent(), "content");

        // Key 10 & distractors in sentence 5
        final Key key10 = clozeText.getClozeSentences().get(4).getKeys().get(1);
        Assert.assertEquals(key10.getKeyWord().getContent(), "result");
        Assert.assertEquals(key10.getBestDistractors(3).get(0).getDistractorWord().getContent(), "consequent");
        Assert.assertEquals(key10.getBestDistractors(3).get(1).getDistractorWord().getContent(), "cause");
        Assert.assertEquals(key10.getBestDistractors(3).get(2).getDistractorWord().getContent(), "consequence");

        // Key 11 & distractors in sentence 6
        final Key key11 = clozeText.getClozeSentences().get(5).getKeys().get(0);
        Assert.assertEquals(key11.getKeyWord().getContent(), "analog");
        Assert.assertEquals(key11.getBestDistractors(3).get(0).getDistractorWord().getContent(), "demodulator");
        Assert.assertEquals(key11.getBestDistractors(3).get(1).getDistractorWord().getContent(), "subcarrier");
        Assert.assertEquals(key11.getBestDistractors(3).get(2).getDistractorWord().getContent(), "nicam");

        // Key 12 & distractors in sentence 6
        final Key key12 = clozeText.getClozeSentences().get(5).getKeys().get(1);
        Assert.assertEquals(key12.getKeyWord().getContent(), "medieval");
        Assert.assertEquals(key12.getBestDistractors(3).get(0).getDistractorWord().getContent(), "ancient");
        Assert.assertEquals(key12.getBestDistractors(3).get(1).getDistractorWord().getContent(), "modern");
        Assert.assertEquals(key12.getBestDistractors(3).get(2).getDistractorWord().getContent(), "postclassical");

        // Key 13 & distractors in sentence 6
        final Key key13 = clozeText.getClozeSentences().get(5).getKeys().get(2);
        Assert.assertEquals(key13.getKeyWord().getContent(), "era");
        Assert.assertEquals(key13.getBestDistractors(3).get(0).getDistractorWord().getContent(), "period");
        Assert.assertEquals(key13.getBestDistractors(3).get(1).getDistractorWord().getContent(), "decade");
        Assert.assertEquals(key13.getBestDistractors(3).get(2).getDistractorWord().getContent(), "heyday");

        // Key 14 & distractors in sentence 9
        final Key key14 = clozeText.getClozeSentences().get(8).getKeys().get(0);
        Assert.assertEquals(key14.getKeyWord().getContent(), "room");
        Assert.assertEquals(key14.getBestDistractors(3).get(0).getDistractorWord().getContent(), "cubicle");
        Assert.assertEquals(key14.getBestDistractors(3).get(1).getDistractorWord().getContent(), "hallway");
        Assert.assertEquals(key14.getBestDistractors(3).get(2).getDistractorWord().getContent(), "basement");
    }

    @Test
    public void cApproach() {
        final ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.CTEST, true);
        final String expectedCloze =
                        "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since ________ sequence o________ operations c________ be rea________ changed, t________ computer c________ solve mo________ than o________ kind o________ problem! Conventionally, ________ computer cons________ of a________ least o________ processing ele________ typically ________ central proce________ unit C________ and so________ form o________ memory. The proce________ element car________ out arith________ and lo________ operations, a________ a seque________ and con________ unit c________ change t________ order o________ operations i________ response t________ stored information. Peripheral dev________ allow infor________ to b________ retrieved fr________ an exte________ source, a________ the res________ of opera________ saved a________ retrieved. Mechanical ana________ computers sta________ appearing i________ the fi________ century a________ were la________ used i________ the medi________ era f________ astronomical calculations. In Wo________ War I________ mechanical ana________ computers we________ used f________ specialized mili________ applications su________ as calcu________ torpedo aiming. During th________ time t________ first elect________ digital comp________ were developed. Originally th________ were t________ size o________ a la________ room, cons________ as mu________ power a________ several hun________ modern pers________ computers (PCs). The Sov________ MIR ser________ of comp________ was deve________ from 19________ to 19________ in ________ group hea________ by Pie________ Vinken a________ Thomas Edison.";

        Assert.assertEquals(expectedCloze, clozeText.getClozeText());

        // Keys in sentence 2
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

        // Keys in sentence 3
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

        // Keys in sentence 4
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
        final Key key11 = clozeText.getClozeSentences().get(3).getKeys().get(10);
        final Key key12 = clozeText.getClozeSentences().get(3).getKeys().get(11);

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

        // Keys in sentence 5
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

        // Keys in sentence 6
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

        // Keys in sentence 7
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

        // Keys in sentence 8
        key1 = clozeText.getClozeSentences().get(7).getKeys().get(0);
        key2 = clozeText.getClozeSentences().get(7).getKeys().get(1);
        key3 = clozeText.getClozeSentences().get(7).getKeys().get(2);
        key4 = clozeText.getClozeSentences().get(7).getKeys().get(3);

        Assert.assertEquals("this", key1.getKeyWord().getContent());
        Assert.assertEquals("the", key2.getKeyWord().getContent());
        Assert.assertEquals("electronic", key3.getKeyWord().getContent());
        Assert.assertEquals("computers", key4.getKeyWord().getContent());

        // Keys in sentence 9
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

        // Keys in sentence 10
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
    }

    @Test
    public void verbApproach() {
        final ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt")), Language.EN, Approach.VERB);

        // for (final ClozeSentence clozeSentence : clozeText.getClozeSentences()) {
        // System.out.println(clozeSentence.getContent());
        // for (final Key key : clozeSentence.getKeys()) {
        // System.out.print(key.getKeyWord().getContent() + ": ");
        // for (final Distractor distractor : key.getDistractors()) {
        // System.out.print(distractor.getDistractorWord().getContent() + ", ");
        // }
        // System.out.print(")\n\n");
        // }
        // }

        final String expectedCloze =
                        "A computer ___1___ a general-purpose device that ___2___ to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations ___3___ the computer ___4___ more than one kind of problem! Conventionally a computer ___5___ of at least one processing element typically a central processing unit CPU and some form of memory. The processing element ___6___ out arithmetic and logic operations and a sequencing and control unit ___7___ the order of operations in response to stored information. Peripheral devices ___8___ information to be retrieved from an external source and the result of operations and retrieved. Mechanical analog computers ___9___ in the first century and ___10___ in the medieval era for astronomical calculations. In World War II mechanical analog computers ___11___ for specialized military applications such as torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room consuming as much power as several hundred modern personal computers PCs. The Soviet MIR series of computers ___12___ from 1965 to 1969 in a group by Pierre Vinken and Thomas Edison.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());


        // Key 1 & distractors in sentence 1
        final Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0);
        Assert.assertEquals("is", key1.getKeyWord().getContent());
        Assert.assertEquals("are", key1.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("was", key1.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be", key1.getDistractors().get(2).getDistractorWord().getContent());

        // Key 2 & distractors in sentence 1
        final Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1);
        Assert.assertEquals("can be programmed", key2.getKeyWord().getContent());
        Assert.assertEquals("is programmed", key2.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("was programmed", key2.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be programmed", key2.getDistractors().get(2).getDistractorWord().getContent());

        // Key 3 & distractors in sentence 2
        final Key key3 = clozeText.getClozeSentences().get(1).getKeys().get(0);
        Assert.assertEquals("can be readily changed", key3.getKeyWord().getContent());
        Assert.assertEquals("are readily changed", key3.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("were readily changed", key3.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be readily changed", key3.getDistractors().get(2).getDistractorWord().getContent());

        // Key 4 & distractors in sentence 2
        final Key key4 = clozeText.getClozeSentences().get(1).getKeys().get(1);
        Assert.assertEquals("can solve", key4.getKeyWord().getContent());
        Assert.assertEquals("solves", key4.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("solved", key4.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will solve", key4.getDistractors().get(2).getDistractorWord().getContent());

        // Key 5 & distractors in sentence 3
        final Key key5 = clozeText.getClozeSentences().get(2).getKeys().get(0);
        Assert.assertEquals("consists", key5.getKeyWord().getContent());
        Assert.assertEquals("consist", key5.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("consisted", key5.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will consist", key5.getDistractors().get(2).getDistractorWord().getContent());

        // Key 6 & distractors in sentence 4
        final Key key6 = clozeText.getClozeSentences().get(3).getKeys().get(0);
        Assert.assertEquals("carries", key6.getKeyWord().getContent());
        Assert.assertEquals("carry", key6.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("carried", key6.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will carry", key6.getDistractors().get(2).getDistractorWord().getContent());

        // Key 7 & distractors in sentence 4
        final Key key7 = clozeText.getClozeSentences().get(3).getKeys().get(1);
        Assert.assertEquals("can change", key7.getKeyWord().getContent());
        Assert.assertEquals("changes", key7.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("changed", key7.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will change", key7.getDistractors().get(2).getDistractorWord().getContent());

        // Key 8 & distractors in sentence 5 --> to check... Text has allows
        final Key key8 = clozeText.getClozeSentences().get(4).getKeys().get(0);
        Assert.assertEquals("allow", key8.getKeyWord().getContent());
        Assert.assertEquals("allows", key8.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("allowed", key8.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will allow", key8.getDistractors().get(2).getDistractorWord().getContent());

        // Key 9 & distractors in sentence 6
        final Key key9 = clozeText.getClozeSentences().get(5).getKeys().get(0);
        Assert.assertEquals("started appearing", key9.getKeyWord().getContent());
        Assert.assertEquals("start appearing", key9.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("started appearing", key9.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will start appearing", key9.getDistractors().get(2).getDistractorWord().getContent());

        // Key 10 & distractors in sentence 6
        final Key key10 = clozeText.getClozeSentences().get(5).getKeys().get(1);
        Assert.assertEquals("were later used", key10.getKeyWord().getContent());
        Assert.assertEquals("is later used", key10.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("was later used", key10.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be later used", key10.getDistractors().get(2).getDistractorWord().getContent());

        // Key 11 & distractors in sentence 7
        final Key key11 = clozeText.getClozeSentences().get(6).getKeys().get(0);
        Assert.assertEquals("were used", key11.getKeyWord().getContent());
        Assert.assertEquals("are used", key11.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("were used", key11.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be used", key11.getDistractors().get(2).getDistractorWord().getContent());

        // Key 12 & distractors in sentence 10
        final Key key12 = clozeText.getClozeSentences().get(9).getKeys().get(0);
        Assert.assertEquals("was developed", key12.getKeyWord().getContent());
        Assert.assertEquals("are developed", key12.getDistractors().get(0).getDistractorWord().getContent());
        Assert.assertEquals("were developed", key12.getDistractors().get(1).getDistractorWord().getContent());
        Assert.assertEquals("will be developed", key12.getDistractors().get(2).getDistractorWord().getContent());

    }
}
