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
package lu.list.itis.dkd.assess.opennlp.soundex;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class SoundexText {

    @Test
    public void englishSoundexTest() {
        //Examples from http://www.highprogrammer.com/alan/numbers/soundex.html
        Assert.assertEquals("W252",  EnglishSoundex.getSoundex("Washington"));
        Assert.assertEquals("W000", EnglishSoundex.getSoundex("Wu"));
        Assert.assertEquals("D253", EnglishSoundex.getSoundex("DeSmet"));
        Assert.assertEquals("G362", EnglishSoundex.getSoundex("Gutierrez"));
        Assert.assertEquals("P236", EnglishSoundex.getSoundex("Pfister"));
        Assert.assertEquals("J250", EnglishSoundex.getSoundex("Jackson"));
        Assert.assertEquals("T522", EnglishSoundex.getSoundex("Tymczak"));
        Assert.assertEquals("A261", EnglishSoundex.getSoundex("Ashcraft"));
    }

    @Test
    public void germanSoundexTest() {
        //Examples from http://stevemorse.org/phonetics/bmpm2.htm
        Assert.assertEquals("S163", GermanSoundex.getSoundex("Schwarz"));
        Assert.assertEquals("G620", GermanSoundex.getSoundex("Grüße"));
    }
    
    @Test
    public void germanKoelnerPhonetikTest() {
        //Examples from https://github.com/deezaster/germanphonetic
        Assert.assertEquals("67", GermanKoelnerPhonetik.getOriginal("Meier"));
        Assert.assertEquals("67", GermanKoelnerPhonetik.getOriginal("Maier"));
        Assert.assertEquals("67", GermanKoelnerPhonetik.getOriginal("Mayer"));
        Assert.assertEquals("67", GermanKoelnerPhonetik.getOriginal("Mayr"));

        Assert.assertEquals("17863", GermanKoelnerPhonetik.getOriginal("Breschnew"));
        Assert.assertEquals("65752682", GermanKoelnerPhonetik.getOriginal("Müller-Lüdenscheidt"));
        Assert.assertEquals("3412", GermanKoelnerPhonetik.getOriginal("Wikipedia"));
        
    }

    public void frenchSoundexTest() {
        //TODO Find examples or ask soemone to deliver examples...
    }
}
