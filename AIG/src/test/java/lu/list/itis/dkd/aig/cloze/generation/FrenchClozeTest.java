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

import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

import org.junit.Assert;
import org.junit.Test;

public class FrenchClozeTest {

    @Test
    public void verbApproach() {
        final ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(FR) - UTF8.txt")), Language.FR, Approach.VERB, 3, false);
        final String expectedCloze =
                        "Un ordinateur ___1___ une machine électronique qui ___2___ par la lecture séquentielle d'un ensemble d'instructions organisées en programmes qui lui ___3___ des opérations logiques et arithmétiques sur des binaires. Dès sa mise sous tension un ordinateur exécute l'une après des instructions qui lui ___4___ un ensemble données. Des tests et des sauts conditionnels ___5___ différemment en fonction des données ou des nécessités moment! Les données à manipuler ___6___ soit par la lecture de mémoires soit par la lecture de composants périphériques qui ___7___ des données physiques extérieures en valeurs binaires déplacement d'une souris ___8___ sur un clavier température compression…. Une fois utilisées ou les données ___9___ réécrites soit dans des mémoires soit dans des composants qui ___10___ une valeur binaire en une action physique écriture sur une imprimante ou sur un moniteur accélération ou freinage d'un véhicule changement de température four…. L'ordinateur ___11___ à des interruptions qui lui ___12___ des programmes de réponses spécifiques à chacune puis de l’exécution séquentielle du interrompu.";

        Assert.assertEquals(expectedCloze, clozeText.getClozeText());

        // Keys in sentence 1
        Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0);
        Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1);
        Key key3 = clozeText.getClozeSentences().get(0).getKeys().get(2);

        Assert.assertEquals("est", key1.getKeyWord().getContent());
        Assert.assertEquals("sont", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("as été", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("seras", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());

        Assert.assertEquals("fonctionne", key2.getKeyWord().getContent());
        Assert.assertEquals("fonctionnent", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        // Assert.assertEquals(key2.getBestDistractors(3).get(1).getDistractor().getWord(), "a
        // fonctionn");
        Assert.assertEquals("fonctionnera", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());


        Assert.assertEquals("font exécuter", key3.getKeyWord().getContent());
        Assert.assertEquals("fait exécuter", key3.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("avez fait exécuter", key3.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("ferez exécuter", key3.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key in sentence 2
        key1 = clozeText.getClozeSentences().get(1).getKeys().get(0);

        Assert.assertEquals("font réécrire", key1.getKeyWord().getContent());
        Assert.assertEquals("fait réécrire", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("avez fait réécrire", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("ferez réécrire", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key in sentence 3
        key1 = clozeText.getClozeSentences().get(2).getKeys().get(0);

        Assert.assertEquals("permettent d'agir", key1.getKeyWord().getContent());
        Assert.assertEquals("permet d'agir", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("avez permis d'agir", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("permettrez d'agir", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Keys in sentence 4
        key1 = clozeText.getClozeSentences().get(3).getKeys().get(0);
        key2 = clozeText.getClozeSentences().get(3).getKeys().get(1);
        key3 = clozeText.getClozeSentences().get(3).getKeys().get(2);

        Assert.assertEquals("sont obtenues", key1.getKeyWord().getContent());
        Assert.assertEquals("est obtenues", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("avez été obtenues", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("serez obtenues", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());

        Assert.assertEquals("représentent", key2.getKeyWord().getContent());
        Assert.assertEquals("représente", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        // Assert.assertEquals(key2.getBestDistractors(3).get(1).getDistractor().getWord(), "avez
        // représenté");
        Assert.assertEquals("représenterez", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());


        Assert.assertEquals("touche appuyée", key3.getKeyWord().getContent());
        Assert.assertEquals("touchent appuyée", key3.getBestDistractors(3).get(0).getDistractorWord().getContent());
        // Assert.assertEquals(key3.getBestDistractors(3).get(1).getDistractor().getWord(), "as
        // touché appuyée");
        Assert.assertEquals("toucheras appuyée", key3.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Keys in sentence 5
        key1 = clozeText.getClozeSentences().get(4).getKeys().get(0);
        key2 = clozeText.getClozeSentences().get(4).getKeys().get(1);

        Assert.assertEquals("sont", key1.getKeyWord().getContent());
        Assert.assertEquals("est", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("avez été", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("serez", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());

        Assert.assertEquals("peuvent transformer", key2.getKeyWord().getContent());
        Assert.assertEquals("peut transformer", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("avez pouvu transformer", key2.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("pourrez transformer", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Keys in sentence 6
        key1 = clozeText.getClozeSentences().get(5).getKeys().get(0);
        key2 = clozeText.getClozeSentences().get(5).getKeys().get(1);

        Assert.assertEquals("peut répondre", key1.getKeyWord().getContent());
        Assert.assertEquals("peuvent répondre", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("as pouvu répondre", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("pourras répondre", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());

        Assert.assertEquals("permettent d’exécuter", key2.getKeyWord().getContent());
        Assert.assertEquals("permet d’exécuter", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("avez permis d’exécuter", key2.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("permettrez d’exécuter", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());
    }

    @Test
    public void cApproach() {
        final ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(FR) - UTF8.txt")), Language.FR, Approach.CTEST, true);
        final String expectedCloze =
                        "Un ordinateur est une machine électronique qui fonctionne par la lecture séquentielle d'un ensemble d'instructions, organisées en programmes, qui lui font exécuter des opérations logiques et arithmétiques sur des chiffres binaires. Dès s________ mise so________ tension, u________ ordinateur exé________ l'une ap________ l'autre, d________ instructions q________ lui fo________ lire, mani________ puis rééc________ un ense________ de données. Des te________ et d________ sauts condit________ permettent d________ changer d'inst________ suivante, e________ donc d'a________ différemment e________ fonction d________ données o________ des néces________ du moment! Les don________ à mani________ sont obte________ soit p________ la lec________ de mémo________ soit p________ la lec________ de compo________ d'interface périph________ qui représ________ des don________ physiques extér________ en val________ binaires dépla________ d'une sou________ touche app________ sur u________ clavier, tempé________ vitesse, compression…). Une fo________ utilisées, o________ manipulées, l________ données so________ réécrites, so________ dans d________ mémoires, so________ dans d________ composants q________ peuvent trans________ une val________ binaire e________ une act________ physique écri________ sur u________ imprimante o________ sur u________ moniteur, accélé________ ou frei________ d'un véhi________ changement d________ température d'________ four…). L'ordinateur pe________ aussi répo________ à d________ interruptions q________ lui perme________ d’exécuter d________ programmes d________ réponses spéci________ à cha________ puis d________ reprendre l’exé________ séquentielle d________ programme interrompu.";

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
        Key key10 = clozeText.getClozeSentences().get(1).getKeys().get(9);
        Key key11 = clozeText.getClozeSentences().get(1).getKeys().get(10);

        Assert.assertEquals("sa", key1.getKeyWord().getContent());
        Assert.assertEquals("sous", key2.getKeyWord().getContent());
        Assert.assertEquals("un", key3.getKeyWord().getContent());
        Assert.assertEquals("exécute", key4.getKeyWord().getContent());
        Assert.assertEquals("après", key5.getKeyWord().getContent());
        Assert.assertEquals("des", key6.getKeyWord().getContent());
        Assert.assertEquals("qui", key7.getKeyWord().getContent());
        Assert.assertEquals("font", key8.getKeyWord().getContent());
        Assert.assertEquals("manipuler", key9.getKeyWord().getContent());
        Assert.assertEquals("réécrire", key10.getKeyWord().getContent());
        Assert.assertEquals("ensemble", key11.getKeyWord().getContent());

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
        key10 = clozeText.getClozeSentences().get(2).getKeys().get(9);
        key11 = clozeText.getClozeSentences().get(2).getKeys().get(10);

        Assert.assertEquals("tests", key1.getKeyWord().getContent());
        Assert.assertEquals("des", key2.getKeyWord().getContent());
        Assert.assertEquals("conditionnels", key3.getKeyWord().getContent());
        Assert.assertEquals("de", key4.getKeyWord().getContent());
        Assert.assertEquals("d'instruction", key5.getKeyWord().getContent());
        Assert.assertEquals("et", key6.getKeyWord().getContent());
        Assert.assertEquals("d'agir", key7.getKeyWord().getContent());
        Assert.assertEquals("en", key8.getKeyWord().getContent());
        Assert.assertEquals("des", key9.getKeyWord().getContent());
        Assert.assertEquals("ou", key10.getKeyWord().getContent());
        Assert.assertEquals("nécessités", key11.getKeyWord().getContent());

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
        key11 = clozeText.getClozeSentences().get(3).getKeys().get(10);
        Key key12 = clozeText.getClozeSentences().get(3).getKeys().get(11);
        Key key13 = clozeText.getClozeSentences().get(3).getKeys().get(12);
        Key key14 = clozeText.getClozeSentences().get(3).getKeys().get(13);
        Key key15 = clozeText.getClozeSentences().get(3).getKeys().get(14);
        Key key16 = clozeText.getClozeSentences().get(3).getKeys().get(15);
        Key key17 = clozeText.getClozeSentences().get(3).getKeys().get(16);
        Key key18 = clozeText.getClozeSentences().get(3).getKeys().get(17);
        Key key19 = clozeText.getClozeSentences().get(3).getKeys().get(18);

        Assert.assertEquals("données", key1.getKeyWord().getContent());
        Assert.assertEquals("manipuler", key2.getKeyWord().getContent());
        Assert.assertEquals("obtenues", key3.getKeyWord().getContent());
        Assert.assertEquals("par", key4.getKeyWord().getContent());
        Assert.assertEquals("lecture", key5.getKeyWord().getContent());
        Assert.assertEquals("mémoires", key6.getKeyWord().getContent());
        Assert.assertEquals("par", key7.getKeyWord().getContent());
        Assert.assertEquals("lecture", key8.getKeyWord().getContent());
        Assert.assertEquals("composants", key9.getKeyWord().getContent());
        Assert.assertEquals("périphériques", key10.getKeyWord().getContent());
        Assert.assertEquals("représentent", key11.getKeyWord().getContent());
        Assert.assertEquals("données", key12.getKeyWord().getContent());
        Assert.assertEquals("extérieures", key13.getKeyWord().getContent());
        Assert.assertEquals("valeurs", key14.getKeyWord().getContent());
        Assert.assertEquals("déplacement", key15.getKeyWord().getContent());
        Assert.assertEquals("souris", key16.getKeyWord().getContent());
        Assert.assertEquals("appuyée", key17.getKeyWord().getContent());
        Assert.assertEquals("un", key18.getKeyWord().getContent());
        Assert.assertEquals("température", key19.getKeyWord().getContent());

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
        key10 = clozeText.getClozeSentences().get(4).getKeys().get(9);
        key11 = clozeText.getClozeSentences().get(4).getKeys().get(10);
        key12 = clozeText.getClozeSentences().get(4).getKeys().get(11);
        key13 = clozeText.getClozeSentences().get(4).getKeys().get(12);
        key14 = clozeText.getClozeSentences().get(4).getKeys().get(13);
        key15 = clozeText.getClozeSentences().get(4).getKeys().get(14);
        key16 = clozeText.getClozeSentences().get(4).getKeys().get(15);
        key17 = clozeText.getClozeSentences().get(4).getKeys().get(16);
        key18 = clozeText.getClozeSentences().get(4).getKeys().get(17);
        key19 = clozeText.getClozeSentences().get(4).getKeys().get(18);
        final Key key20 = clozeText.getClozeSentences().get(4).getKeys().get(19);
        final Key key21 = clozeText.getClozeSentences().get(4).getKeys().get(20);
        final Key key22 = clozeText.getClozeSentences().get(4).getKeys().get(21);

        Assert.assertEquals("fois", key1.getKeyWord().getContent());
        Assert.assertEquals("ou", key2.getKeyWord().getContent());
        Assert.assertEquals("les", key3.getKeyWord().getContent());
        Assert.assertEquals("sont", key4.getKeyWord().getContent());
        Assert.assertEquals("soit", key5.getKeyWord().getContent());
        Assert.assertEquals("des", key6.getKeyWord().getContent());
        Assert.assertEquals("soit", key7.getKeyWord().getContent());
        Assert.assertEquals("des", key8.getKeyWord().getContent());
        Assert.assertEquals("qui", key9.getKeyWord().getContent());
        Assert.assertEquals("transformer", key10.getKeyWord().getContent());
        Assert.assertEquals("valeur", key11.getKeyWord().getContent());
        Assert.assertEquals("en", key12.getKeyWord().getContent());
        Assert.assertEquals("action", key13.getKeyWord().getContent());
        Assert.assertEquals("écriture", key14.getKeyWord().getContent());
        Assert.assertEquals("une", key15.getKeyWord().getContent());
        Assert.assertEquals("ou", key16.getKeyWord().getContent());
        Assert.assertEquals("un", key17.getKeyWord().getContent());
        Assert.assertEquals("accélération", key18.getKeyWord().getContent());
        Assert.assertEquals("freinage", key19.getKeyWord().getContent());
        Assert.assertEquals("véhicule", key20.getKeyWord().getContent());
        Assert.assertEquals("de", key21.getKeyWord().getContent());
        Assert.assertEquals("d'un", key22.getKeyWord().getContent());

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
        key10 = clozeText.getClozeSentences().get(5).getKeys().get(9);
        key11 = clozeText.getClozeSentences().get(5).getKeys().get(10);
        key12 = clozeText.getClozeSentences().get(5).getKeys().get(11);

        Assert.assertEquals("peut", key1.getKeyWord().getContent());
        Assert.assertEquals("répondre", key2.getKeyWord().getContent());
        Assert.assertEquals("des", key3.getKeyWord().getContent());
        Assert.assertEquals("qui", key4.getKeyWord().getContent());
        Assert.assertEquals("permettent", key5.getKeyWord().getContent());
        Assert.assertEquals("des", key6.getKeyWord().getContent());
        Assert.assertEquals("de", key7.getKeyWord().getContent());
        Assert.assertEquals("spécifiques", key8.getKeyWord().getContent());
        Assert.assertEquals("chacune", key9.getKeyWord().getContent());
        Assert.assertEquals("de", key10.getKeyWord().getContent());
        Assert.assertEquals("l’exécution", key11.getKeyWord().getContent());
        Assert.assertEquals("du", key12.getKeyWord().getContent());
    }

    @Test
    public void kodaApproach() {
        final ClozeText clozeText = new ClozeText(Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(FR) - UTF8.txt")), Language.FR, Approach.ANNOTATION);

        final String expectedCloze =
                        "Un ___1___ est une ___2___ électronique qui fonctionne par la ___3___ séquentielle d'un ensemble d'instructions, organisées en programmes, qui lui font exécuter des opérations logiques et arithmétiques sur des chiffres binaires. Dès sa ___4___ sous ___5___ un ordinateur exécute, l'une après l'autre, des instructions qui lui font lire, manipuler, puis réécrire un ensemble de données. Des tests et des sauts conditionnels permettent de changer d'instruction suivante, et donc d'agir différemment en ___6___ des données ou des nécessités du moment! Les données à manipuler sont obtenues, soit par la lecture de mémoires, soit par la lecture de composants d'interface (périphériques) qui représentent des données physiques extérieures en valeurs binaires (déplacement d'une souris, touche appuyée sur un ___7___ température, ___8___ compression…). Une fois utilisées, ou manipulées, les données sont réécrites, soit dans des mémoires, soit dans des composants qui peuvent transformer une valeur binaire en une ___9___ physique ___10___ sur une ___11___ ou sur un ___12___ ___13___ ou freinage d'un ___14___ ___15___ de ___16___ d'un four…). L'ordinateur peut aussi répondre à des interruptions qui lui permettent d’exécuter des programmes de réponses spécifiques à chacune, puis de reprendre l’exécution séquentielle du ___17___ interrompu.";
        Assert.assertEquals(expectedCloze, clozeText.getClozeText());

        // Key 1 & distractors in sentence 1
        final Key key1 = clozeText.getClozeSentences().get(0).getKeys().get(0);
        Assert.assertEquals("ordinateur", key1.getKeyWord().getContent());
        Assert.assertEquals("calculatrice", key1.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("trackpad", key1.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("copieur", key1.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 2 & distractors in sentence 1
        final Key key2 = clozeText.getClozeSentences().get(0).getKeys().get(1);
        Assert.assertEquals("machine", key2.getKeyWord().getContent());
        Assert.assertEquals("ventilateur", key2.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("chaudière", key2.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("mélangeur", key2.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 3 & distractors in sentence 1
        final Key key3 = clozeText.getClozeSentences().get(0).getKeys().get(2);
        Assert.assertEquals("lecture", key3.getKeyWord().getContent());
        Assert.assertEquals("écriture", key3.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("texte", key3.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("oral", key3.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 4 & distractors in sentence 2
        final Key key4 = clozeText.getClozeSentences().get(1).getKeys().get(0);
        Assert.assertEquals("mise", key4.getKeyWord().getContent());
        Assert.assertEquals("élaboration", key4.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("harmonisation", key4.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("rationalisation", key4.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 5 & distractors in sentence 2
        final Key key5 = clozeText.getClozeSentences().get(1).getKeys().get(1);
        Assert.assertEquals("tension", key5.getKeyWord().getContent());
        Assert.assertEquals("friction", key5.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("remous", key5.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("agitation", key5.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 6 & distractors in sentence 3
        final Key key6 = clozeText.getClozeSentences().get(2).getKeys().get(0);
        Assert.assertEquals("fonction", key6.getKeyWord().getContent());
        Assert.assertEquals("compétence", key6.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("tâche", key6.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("postes", key6.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 7 & distractors in sentence 4
        final Key key7 = clozeText.getClozeSentences().get(3).getKeys().get(0);
        Assert.assertEquals("clavier", key7.getKeyWord().getContent());
        Assert.assertEquals("synthétiseur", key7.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("fretless", key7.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("concertina", key7.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 8 & distractors in sentence 4
        final Key key8 = clozeText.getClozeSentences().get(3).getKeys().get(1);
        Assert.assertEquals("vitesse", key8.getKeyWord().getContent());
        Assert.assertEquals("portance", key8.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("débattement", key8.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("roulis", key8.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 9 & distractors in sentence 5
        final Key key9 = clozeText.getClozeSentences().get(4).getKeys().get(0);
        Assert.assertEquals("action", key9.getKeyWord().getContent());
        Assert.assertEquals("initiative", key9.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("consolidation", key9.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("stratégie", key9.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 10 & distractors in sentence 5
        final Key key10 = clozeText.getClozeSentences().get(4).getKeys().get(1);
        Assert.assertEquals("écriture", key10.getKeyWord().getContent());
        Assert.assertEquals("relecture", key10.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("narration", key10.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("poétique", key10.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 11 & distractors in sentence 5
        final Key key11 = clozeText.getClozeSentences().get(4).getKeys().get(2);
        Assert.assertEquals("imprimante", key11.getKeyWord().getContent());
        Assert.assertEquals("portable", key11.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("enfichable", key11.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("programmable", key11.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 12 & distractors in sentence 5
        final Key key12 = clozeText.getClozeSentences().get(4).getKeys().get(3);
        Assert.assertEquals("moniteur", key12.getKeyWord().getContent());
        Assert.assertEquals("chauffagiste", key12.getBestDistractors(3).get(0).getDistractorWord().getContent());
        // Assert.assertEquals("cariste",
        // key12.getBestDistractors(3).get(1).getDistractorWord().getContent());
        // Assert.assertEquals("électromécanicien",
        // key12.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 13 & distractors in sentence 5
        final Key key13 = clozeText.getClozeSentences().get(4).getKeys().get(4);
        Assert.assertEquals("accélération", key13.getKeyWord().getContent());
        Assert.assertEquals("décélération", key13.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("retournement", key13.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("contraction", key13.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 14 & distractors in sentence 5
        final Key key14 = clozeText.getClozeSentences().get(4).getKeys().get(5);
        Assert.assertEquals("véhicule", key14.getKeyWord().getContent());
        Assert.assertEquals("fardier", key14.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("surfaceuse", key14.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("remorqueuse", key14.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 15 & distractors in sentence 5
        final Key key15 = clozeText.getClozeSentences().get(4).getKeys().get(6);
        Assert.assertEquals("changement", key15.getKeyWord().getContent());
        Assert.assertEquals("rééquilibrage", key15.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("évolution", key15.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("basculement", key15.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 16 & distractors in sentence 5
        final Key key16 = clozeText.getClozeSentences().get(4).getKeys().get(7);
        Assert.assertEquals("température", key16.getKeyWord().getContent());
        Assert.assertEquals("hygrométrie", key16.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("humidité", key16.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("débit", key16.getBestDistractors(3).get(2).getDistractorWord().getContent());

        // Key 17 & distractors in sentence 6
        final Key key17 = clozeText.getClozeSentences().get(5).getKeys().get(0);
        Assert.assertEquals("programme", key17.getKeyWord().getContent());
        Assert.assertEquals("stratégie", key17.getBestDistractors(3).get(0).getDistractorWord().getContent());
        Assert.assertEquals("plan", key17.getBestDistractors(3).get(1).getDistractorWord().getContent());
        Assert.assertEquals("initiative", key17.getBestDistractors(3).get(2).getDistractorWord().getContent());
    }
}