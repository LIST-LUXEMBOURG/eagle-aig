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
package lu.list.itis.dkd.assess.opennlp.lemmatizer;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class LemmatizerTest {

    @Test
    public void frenchLemmaTest() {
        Text text = new Text("Un ordinateur est une machine électronique qui fonctionne par la lecture séquentielle d'un ensemble d'instructions, organisées en programmes, qui lui font exécuter des opérations logiques et arithmétiques sur des chiffres binaires! Dès sa mise sous tension, un ordinateur exécute, l'une après l'autre, des instructions qui lui font lire, manipuler, puis réécrire un ensemble de données.", Language.FR);
        String lemmaText = text.getLemmaText();
        Assert.assertEquals("Un ordinateur être une machine électronique qui fonctionner par la lecture séquentiel d'un ensemble d'instructions organiser en programme qui lui faire exécuter des opération logique et arithmétique sur des chiffre binaire! Dès sa mise sous tension un ordinateur exécuter l'une après l'autre des instruction qui lui faire lire manipuler puis réécrire un ensemble de donnée.", lemmaText);
    }

    @Test
    public void germanLemmaTest() {
        Text text = new Text("Welches hauptsächliche Thema hatte die Wahl. Ich hatte die Wahl.", Language.DE);
        String lemmaText = text.getLemmaText();
        Assert.assertEquals("Welches haupt Thema haben die Wahl. Ich haben die Wahl.", lemmaText);
    }

    @Test
    public void englishLemmaTest() {
        Text text = new Text("Humans live outer space. But not here.", Language.EN);
        String lemmaText = text.getLemmaText();
        Assert.assertEquals("Human live outer space. But not here.", lemmaText);
    }
}
