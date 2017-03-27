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
package lu.list.itis.dkd.assess.opennlp.syllibification;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class GermanSyllibificationTest {
    
    public void germanSyllibificationTest() {
        int numberOfSyllables = (int) Syllabification.getNumberOfSyllables("Welches hauptsächliche Thema hatte die Wahl?", Language.DE);
        Assert.assertEquals(11, numberOfSyllables);
    }
    
    public int syl(String word) {
        return (int) Syllabification.getNumberOfSyllables(word, Language.DE);
    }
    
    @Test
    //Examples from http://www.oxforddictionaries.com/words/german-general-advice-on-writing-in-german-4-syllable-division-in-german
    public void testWords() {       
        
        //First part
        Assert.assertEquals(2, syl("Freunde"));
        Assert.assertEquals(2, syl("Männer"));
        Assert.assertEquals(2, syl("fordern"));
        Assert.assertEquals(2, syl("weiter"));
        Assert.assertEquals(2, syl("Orgel"));
        Assert.assertEquals(2, syl("kalkig"));
        Assert.assertEquals(3, syl("Besserung"));
        Assert.assertEquals(2, syl("Balkon"));
        Assert.assertEquals(2, syl("Fiskus"));
        Assert.assertEquals(2, syl("Hotel"));
        Assert.assertEquals(2, syl("Planet"));
        Assert.assertEquals(3, syl("Kontinent"));
        Assert.assertEquals(3, syl("Faszikel"));
        Assert.assertEquals(4, syl("Reminiszenz"));
        Assert.assertEquals(3, syl("Ellipse"));
        Assert.assertEquals(2, syl("Berlin"));
        Assert.assertEquals(2, syl("Elba"));
        Assert.assertEquals(2, syl("Türkei"));
        Assert.assertEquals(2, syl("lasten"));
        Assert.assertEquals(2, syl("Dienstes"));
        
        //Second Part
        Assert.assertEquals(2, syl("treten"));
        Assert.assertEquals(2, syl("nähen"));
        Assert.assertEquals(2, syl("Ruder"));
        Assert.assertEquals(2, syl("reißen"));
        Assert.assertEquals(2, syl("boxen"));
        Assert.assertEquals(2, syl("Kokon"));
        Assert.assertEquals(2, syl("Kreta"));
        Assert.assertEquals(2, syl("China"));
        Assert.assertEquals(2, syl("Anker"));
        Assert.assertEquals(2, syl("Finger"));
        Assert.assertEquals(2, syl("warten"));
        Assert.assertEquals(3, syl("Füllungen"));
        Assert.assertEquals(2, syl("Ritter"));
        Assert.assertEquals(2, syl("Wasser"));
        Assert.assertEquals(2, syl("Knospen"));
        Assert.assertEquals(2, syl("kämpfen"));
        Assert.assertEquals(2, syl("Achsel"));
        Assert.assertEquals(2, syl("steckten"));
        Assert.assertEquals(2, syl("Katzen"));
        Assert.assertEquals(2, syl("Städter"));
        Assert.assertEquals(2, syl("Drechsler"));
        Assert.assertEquals(2, syl("dunkle"));
        Assert.assertEquals(2, syl("gestrig"));
        Assert.assertEquals(3, syl("andere"));
        Assert.assertEquals(2, syl("nehmen"));
        Assert.assertEquals(2, syl("Arsen"));
        Assert.assertEquals(2, syl("Hippie"));
        Assert.assertEquals(2, syl("Kasko"));
        Assert.assertEquals(2, syl("Pektin"));
        Assert.assertEquals(2, syl("Ungarn"));
        Assert.assertEquals(2, syl("Hessen"));
        Assert.assertEquals(3, syl("Atlantik"));
        
        //Third part
        Assert.assertEquals(2, syl("Freundin"));
        Assert.assertEquals(2, syl("Bäckerei"));
        Assert.assertEquals(2, syl("Lüftung"));
        
        //Forth part
        Assert.assertEquals(2, syl("Bücher"));
        Assert.assertEquals(2, syl("Flasche"));
        Assert.assertEquals(2, syl("Machete"));
        Assert.assertEquals(2, syl("Prophet"));
        Assert.assertEquals(2, syl("Myrrhe"));
        Assert.assertEquals(3, syl("Cashewnuß"));
        Assert.assertEquals(3, syl("katholisch"));
        Assert.assertEquals(2, syl("Grüsse"));
        Assert.assertEquals(2, syl("heissen"));
        
        //Fifth part
        Assert.assertEquals(2, syl("Zucker"));
        Assert.assertEquals(2, syl("backen"));
        Assert.assertEquals(2, syl("Senckenberg"));
        Assert.assertEquals(2, syl("Francke"));
        Assert.assertEquals(2, syl("bismarckisch"));
        
        //Sixth part
        Assert.assertEquals(2, syl("Wiese"));
        Assert.assertEquals(2, syl("Coesfeld"));
        
        //Compound part
        Assert.assertEquals(3, syl("einarmig")); //Should be 2... ein-armig
        Assert.assertEquals(3, syl("beinhalten")); //Should be 2... be-inhalten
        Assert.assertEquals(2, syl("Episode"));
        Assert.assertEquals(2, syl("abstrakt"));
        Assert.assertEquals(3, syl("Spargelder")); //Should be 2... be-inhalten
        Assert.assertEquals(3, syl("beinhalten")); //Should be 2... be-inhalten
        
        
        
    }
}
