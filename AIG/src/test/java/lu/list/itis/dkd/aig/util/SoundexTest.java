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
package lu.list.itis.dkd.aig.util;

import lu.list.itis.dkd.aig.Value;
import lu.list.itis.dkd.aig.ValueType;
import lu.list.itis.dkd.aig.Variable;

import com.google.common.collect.ArrayListMultimap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Class for testing all static methods of {@link Soundex};
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.8.0
 */
public class SoundexTest {
    private static Variable english, englishToo, german, germanToo, french, frenchToo, fabricated, fabricatedFrench, codePoint, accented;

    /**
     * Method setting up variables for test execution.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        final ArrayListMultimap<String, String> parameters = ArrayListMultimap.create();
        final ArrayList<Value> values = new ArrayList<>();
        final String french = Locale.FRENCH.getLanguage();
        final String german = Locale.GERMAN.getLanguage();
        final String english = Locale.ENGLISH.getLanguage();

        parameters.put(Externalization.LANGUAGE_ELEMENT, english);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://mydog.com"); //$NON-NLS-1$
        values.add(new Value(new URI("http://mydog.com"), ValueType.TEXT, "labrador")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.english = new Variable(parameters, values); // L163

        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, english);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://mydog.com"); //$NON-NLS-1$
        values.add(new Value(new URI("http://mydog.com"), ValueType.TEXT, "retriever")); // R361 //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.englishToo = new Variable(parameters, values); // fr=R369

        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, german);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://mybunny.de"); //$NON-NLS-1$
        values.add(new Value(new URI("http://mybunny.de"), ValueType.TEXT, "hollandLop")); // H453 //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.german = new Variable(parameters, values);

        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, german);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://mybunny.de"); //$NON-NLS-1$
        values.add(new Value(new URI("http://mytools.de"), ValueType.TEXT, "Großegießkanne")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.germanToo = new Variable(parameters, values); // G622
                                                                  // AND
                                                                  // fr=G687
        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, french);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://mypoultry.fr"); //$NON-NLS-1$
        values.add(new Value(new URI("http://mypoultry.fr"), ValueType.TEXT, "poulet")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.french = new Variable(parameters, values); // P430

        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, french);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://myvegetable.fr"); //$NON-NLS-1$
        values.add(new Value(new URI("http://myvegetable.fr"), ValueType.TEXT, "gourgette")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.frenchToo = new Variable(parameters, values); // G673 AND en=G623

        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, french);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://mybla.fr"); //$NON-NLS-1$
        values.add(new Value(new URI("http://mybla.fr"), ValueType.TEXT, "RGZV")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.fabricatedFrench = new Variable(parameters, values); // fr=R789 AND en=R210

        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, english);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://mybla.com"); //$NON-NLS-1$
        values.add(new Value(new URI("http://mybla.com"), ValueType.TEXT, "RGZV")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.fabricated = new Variable(parameters, values); // R210 AND fr=R789


        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, english);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://codepoint.co.uk"); //$NON-NLS-1$
        values.add(new Value(new URI("http://codepoint.co.uk"), ValueType.TEXT, "my-space")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.codePoint = new Variable(parameters, values);

        values.clear();
        parameters.clear();
        parameters.put(Externalization.LANGUAGE_ELEMENT, english);
        parameters.put(Externalization.IDENTIFIER_ELEMENT, "http://accented.co.uk"); //$NON-NLS-1$
        values.add(new Value(new URI("http://accented.co.uk"), ValueType.TEXT, "àccented")); //$NON-NLS-1$ //$NON-NLS-2$
        SoundexTest.accented = new Variable(parameters, values);
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.util.Soundex#getSoundex(lu.list.itis.dkd.aig.Variable)}.
     */
    @SuppressWarnings("null")
    @Test
    public void testGetSoundexVariable() {
        Assert.assertEquals("L163", Soundex.getSoundex(SoundexTest.english)); //$NON-NLS-1$
        Assert.assertEquals("R361", Soundex.getSoundex(SoundexTest.englishToo)); //$NON-NLS-1$
        Assert.assertEquals("H453", Soundex.getSoundex(SoundexTest.german)); //$NON-NLS-1$
        Assert.assertEquals("G622", Soundex.getSoundex(SoundexTest.germanToo)); //$NON-NLS-1$
        Assert.assertEquals("P430", Soundex.getSoundex(SoundexTest.french)); //$NON-NLS-1$
        Assert.assertEquals("G673", Soundex.getSoundex(SoundexTest.frenchToo)); //$NON-NLS-1$
        Assert.assertEquals("A253", Soundex.getSoundex(SoundexTest.accented)); //$NON-NLS-1$
        Assert.assertEquals("M212", Soundex.getSoundex(SoundexTest.codePoint)); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.util.Soundex#getSoundex(lu.list.itis.dkd.aig.Variable, java.lang.String)}
     * .
     */
    @SuppressWarnings("null")
    @Test
    public void testGetSoundexVariableString() {
        Assert.assertEquals("L163", Soundex.getSoundex(SoundexTest.english, SoundexTest.english.getLanguage())); //$NON-NLS-1$
        Assert.assertEquals("R361", Soundex.getSoundex(SoundexTest.englishToo, SoundexTest.english.getLanguage())); //$NON-NLS-1$
        Assert.assertEquals("H453", Soundex.getSoundex(SoundexTest.german, SoundexTest.english.getLanguage())); //$NON-NLS-1$
        Assert.assertEquals("G687", Soundex.getSoundex(SoundexTest.germanToo, SoundexTest.french.getLanguage())); //$NON-NLS-1$
        Assert.assertEquals("P430", Soundex.getSoundex(SoundexTest.french, SoundexTest.english.getLanguage())); //$NON-NLS-1$
        Assert.assertEquals("G623", Soundex.getSoundex(SoundexTest.frenchToo, SoundexTest.english.getLanguage())); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.util.Soundex#difference(Variable, Variable, String...)}.
     * 
     * @throws URISyntaxException
     */
    @SuppressWarnings("null")
    @Test
    public void testDifference() throws URISyntaxException {

        Assert.assertEquals(3, Soundex.difference(SoundexTest.english, SoundexTest.englishToo));
        Assert.assertEquals(3, Soundex.difference(SoundexTest.englishToo, SoundexTest.fabricated));
        Assert.assertEquals(0, Soundex.difference(SoundexTest.fabricated, SoundexTest.fabricatedFrench));
        Assert.assertEquals(0, Soundex.difference(SoundexTest.fabricatedFrench, SoundexTest.fabricated));
        Assert.assertEquals(3, Soundex.difference(SoundexTest.fabricated, SoundexTest.french));
        Assert.assertEquals(4, Soundex.difference(SoundexTest.english, SoundexTest.french));

        Assert.assertEquals(4, Soundex.difference(SoundexTest.fabricated, SoundexTest.french, "fr", "en")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals(3, Soundex.difference(SoundexTest.germanToo, SoundexTest.fabricated, "fr", "en")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals(3, Soundex.difference(SoundexTest.fabricated, SoundexTest.germanToo, "fr", "de")); //$NON-NLS-1$ //$NON-NLS-2$


        // // Rerun of original text from used library.
        // Assert.assertEquals("R163", Soundex.getSoundex(new Variable("Robert", "en", new
        // URI("http://bla.de"))));
        // Assert.assertEquals("R163", Soundex.getSoundex(new Variable("Rupert", "en", new
        // URI("http://bla.de"))));
        // Assert.assertEquals("R150", Soundex.getSoundex(new Variable("Rubin", "en", new
        // URI("http://bla.de"))));
        // Assert.assertEquals("A261", Soundex.getSoundex(new Variable("Ashcraft", "en", new
        // URI("http://bla.de"))));
        // Assert.assertEquals("A261", Soundex.getSoundex(new Variable("Ashcroft", "en", new
        // URI("http://bla.de"))));
        // Assert.assertEquals("T522", Soundex.getSoundex(new Variable("Tymczak", "en", new
        // URI("http://bla.de"))));
        // Assert.assertEquals("P236", Soundex.getSoundex(new Variable("Pfister", "en", new
        // URI("http://bla.de"))));
    }
}