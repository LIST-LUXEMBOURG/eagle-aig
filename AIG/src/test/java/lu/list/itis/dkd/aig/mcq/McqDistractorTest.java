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
package lu.list.itis.dkd.aig.mcq;

import lu.list.itis.dkd.aig.CompositeVariable;
import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Test class for testing the {@link McqDistractor}'s methods.
 *
 * @author Eric TOBIAS [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.4.11
 */
public class McqDistractorTest {
    private String name1, name2, name3;
    private String langEN, langFR, langDE;
    private URI uri1, uri2, uri3;
    private McqDistractor distractorCat, distractorCAT, distractorBunny;
    private Variable var1;

    /**
     * Method for setting up test resources before each method call.
     *
     * @throws URISyntaxException
     *         Thrown when an URI was malformed.
     * @throws TemplateParseException 
     */
    @Before
    public void setUp() throws URISyntaxException, TemplateParseException {
        name1 = "Cat";
        name2 = "CAT";
        name3 = "Bunny";
        langEN = "en";
        langFR = "fr";
        langDE = "de";
        uri1 = new URI("http://mycat.com");
        uri2 = new URI("http://yourcat.com");
        uri3 = new URI("http://mybunny.com");
        var1 = CompositeVariable.buildVariable(name1, langEN, uri1);
        distractorCat = new McqDistractor(var1);
        distractorCAT = new McqDistractor(CompositeVariable.buildVariable(name2, langDE, uri2));
        distractorBunny = new McqDistractor(CompositeVariable.buildVariable(name3, langFR, uri3));
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.McqDistractor#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Assert.assertEquals(name1.toLowerCase().hashCode(), distractorCat.hashCode());
        Assert.assertEquals(name3.toLowerCase().hashCode(), distractorBunny.hashCode());
        Assert.assertEquals(distractorCat.hashCode(), distractorCAT.hashCode());
        Assert.assertFalse(distractorCat.hashCode() == distractorBunny.hashCode());
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.mcq.McqDistractor#McqDistractor(java.lang.String, java.lang.String, java.net.URI)}
     * .
     */
    @Test
    public void testDistractor() {
        Assert.assertNotNull(distractorCat);
        Assert.assertNotNull(distractorCat.getVariable());
        Assert.assertNotNull(distractorCat.getVariable().getTextContent());
        Assert.assertNotNull(distractorCat.getVariable().getLanguage());
        Assert.assertNotNull(distractorCat.getVariable().getIdentifier());

        Assert.assertNotNull(distractorCAT.getVariable());
        Assert.assertNotNull(distractorCAT.getVariable().getTextContent());
        Assert.assertNotNull(distractorCAT.getVariable().getLanguage());
        Assert.assertNotNull(distractorCAT.getVariable().getIdentifier());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.McqDistractor#getVariable()}.
     */
    @Test
    public void testGetVariable() {
        Assert.assertEquals(name1, distractorCat.getVariable().getTextContent());
        Assert.assertEquals(name2, distractorCAT.getVariable().getTextContent());
        Assert.assertEquals(name3, distractorBunny.getVariable().getTextContent());
        Assert.assertEquals(langEN, distractorCat.getVariable().getLanguage());
        Assert.assertEquals(langDE, distractorCAT.getVariable().getLanguage());
        Assert.assertEquals(langFR, distractorBunny.getVariable().getLanguage());
        Assert.assertEquals(uri1, distractorCat.getVariable().getIdentifier());
        Assert.assertEquals(uri2, distractorCAT.getVariable().getIdentifier());
        Assert.assertEquals(uri3, distractorBunny.getVariable().getIdentifier());
    }


    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.McqDistractor#equals(java.lang.Object)}.
     *
     * @throws URISyntaxException
     * @throws TemplateParseException 
     */
    @Test
    public void testEqualsObject() throws URISyntaxException, TemplateParseException {
        final McqDistractor distractorKitty = new McqDistractor(CompositeVariable.buildVariable("cat", langFR, new URI("http://mykitty.com")));
        Assert.assertTrue(distractorCat.equals(distractorCat));

        Assert.assertFalse(distractorCat.equals(distractorCAT));// not same language
        Assert.assertFalse(distractorCAT.equals(distractorCat));// not same language
        Assert.assertFalse(distractorCat.equals(distractorKitty));// not same URI
        Assert.assertFalse(distractorKitty.equals(distractorCAT));// not same URI
        Assert.assertFalse(distractorBunny.equals(distractorCat));
        Assert.assertFalse(distractorCat.equals(distractorBunny));
    }

    /**
     * Test method for {@link McqDistractor#compareTo(McqDistractor)}.
     */
    @Test
    public void testComparison() {
        Assert.assertEquals(0, distractorBunny.compareTo(distractorBunny));
        Assert.assertEquals("bunny".compareTo("cat"), distractorBunny.compareTo(distractorCat));
        Assert.assertEquals(0, distractorCat.compareTo(distractorCAT));

        distractorBunny.setSimilarityToCorrectResponse(0.5f);
        distractorCAT.setSimilarityToCorrectResponse(1f);

        Assert.assertTrue(distractorCat.compareTo(distractorBunny) < 0);
        Assert.assertTrue(distractorBunny.compareTo(distractorCAT) < 0);
        Assert.assertTrue(distractorCAT.compareTo(distractorCat) == 0);
        Assert.assertTrue(distractorCAT.compareTo(distractorBunny) > 0);
    }

    /**
     * Test method for {@link McqDistractor#setSimilarityToCorrectResponse(float)}.
     */
    @Test
    public void testsetSimilarity() {
        Assert.assertEquals(0, Float.compare(0f, distractorCat.getSimilarityToCorrectResponse()));
        Assert.assertEquals(0, Float.compare(0f, distractorCAT.getSimilarityToCorrectResponse()));
        Assert.assertEquals(0, Float.compare(0f, distractorBunny.getSimilarityToCorrectResponse()));

        distractorBunny.setSimilarityToCorrectResponse(0.5f);
        distractorCAT.setSimilarityToCorrectResponse(1f);

        Assert.assertEquals(0.5f, distractorBunny.getSimilarityToCorrectResponse(), 0.01);
        Assert.assertEquals(1f, distractorCAT.getSimilarityToCorrectResponse(), 0.01);
    }
}