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

import lu.list.itis.dkd.assess.opennlp.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class TestFrenchText {
    
    @Test
    public void plainTest2() {
        String french = "Dans le cadre d’une relation de travail, le salaire constitue la contrepartie directe du travail effectué par le salarié au profit de son employeur";
        Text frenchText = new Text(french, Language.FR);
        
        Assert.assertEquals(french, frenchText.getContent());
        
        //TODO Test words
    }

    @Test
    public void plainTest() {
        String french = "Un ordinateur est une machine électronique qui fonctionne par la lecture séquentielle d'un ensemble d'instructions, organisées en programmes, qui lui font exécuter des opérations logiques et arithmétiques sur des chiffres binaires. Dès sa mise sous tension, un ordinateur exécute, l'une après l'autre, des instructions qui lui font lire, manipuler, puis réécrire un ensemble de données. Des tests et des sauts conditionnels permettent de changer d'instruction suivante, et donc d'agir différemment en fonction des données ou des nécessités du moment! Les données à manipuler sont obtenues, soit par la lecture de mémoires, soit par la lecture de composants d'interface (périphériques) qui représentent des données physiques extérieures en valeurs binaires (déplacement d'une souris, touche appuyée sur un clavier, température, vitesse, compression…). Une fois utilisées, ou manipulées, les données sont réécrites, soit dans des mémoires, soit dans des composants qui peuvent transformer une valeur binaire en une action physique (écriture sur une imprimante ou sur un moniteur, accélération ou freinage d'un véhicule, changement de température d'un four…). L'ordinateur peut aussi répondre à des interruptions qui lui permettent d’exécuter des programmes de réponses spécifiques à chacune, puis de reprendre l’exécution séquentielle du programme interrompu.";
        Text frenchText = new Text(french, Language.FR);
        Assert.assertEquals(french, frenchText.getContent());
        
        //TODO Test words
    }
    
    @Test
    public void fileTest() throws FileNotFoundException {
        Text frenchText = new Text(TestResources.class.getResourceAsStream("resources/PC(FR) - UTF8.txt"), Language.FR);
        
        //Paragraph 1
        Paragraph paragraph1 = frenchText.getParagraph(1);
        Assert.assertEquals("Un ordinateur est une machine électronique qui fonctionne par la lecture séquentielle d'un ensemble d'instructions, organisées en programmes, qui lui font exécuter des opérations logiques et arithmétiques sur des chiffres binaires.", paragraph1.getSentences().get(0).getContent());
        Assert.assertEquals("Dès sa mise sous tension, un ordinateur exécute, l'une après l'autre, des instructions qui lui font lire, manipuler, puis réécrire un ensemble de données.", paragraph1.getSentences().get(1).getContent());
        Assert.assertEquals("Des tests et des sauts conditionnels permettent de changer d'instruction suivante, et donc d'agir différemment en fonction des données ou des nécessités du moment!", paragraph1.getSentences().get(2).getContent());
        
        //Paragraph2
        Paragraph paragraph2 = frenchText.getParagraph(2);
        Assert.assertEquals("Les données à manipuler sont obtenues, soit par la lecture de mémoires, soit par la lecture de composants d'interface (périphériques) qui représentent des données physiques extérieures en valeurs binaires (déplacement d'une souris, touche appuyée sur un clavier, température, vitesse, compression…).", paragraph2.getSentences().get(0).getContent());
        Assert.assertEquals("Une fois utilisées, ou manipulées, les données sont réécrites, soit dans des mémoires, soit dans des composants qui peuvent transformer une valeur binaire en une action physique (écriture sur une imprimante ou sur un moniteur, accélération ou freinage d'un véhicule, changement de température d'un four…).", paragraph2.getSentences().get(1).getContent());
        Assert.assertEquals("L'ordinateur peut aussi répondre à des interruptions qui lui permettent d’exécuter des programmes de réponses spécifiques à chacune, puis de reprendre l’exécution séquentielle du programme interrompu.", paragraph2.getSentences().get(2).getContent());
        
        //Paragraph Sentence has to be the same than text sentence
        Assert.assertEquals(paragraph1.getSentences().get(0).getContent(), frenchText.getSentences().get(0).getContent());
        Assert.assertEquals(paragraph1.getSentences().get(1).getContent(), frenchText.getSentences().get(1).getContent());
        Assert.assertEquals(paragraph1.getSentences().get(2).getContent(), frenchText.getSentences().get(2).getContent());
        Assert.assertEquals(paragraph2.getSentences().get(0).getContent(), frenchText.getSentences().get(3).getContent());
        Assert.assertEquals(paragraph2.getSentences().get(1), frenchText.getSentences().get(4));
        Assert.assertEquals(paragraph2.getSentences().get(2), frenchText.getSentences().get(5)); 
        
        //TODO Test words
    }    
}
