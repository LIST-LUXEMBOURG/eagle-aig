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
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Test class testing all methods of {@link Options}.
 *
 * @author Eric TOBIAS [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.6.44
 */
public class Options2Test
{
    private String name1, name2, name3;
    private String langEN, langFR, langDE;
    private URI uri1, uri2, uri3;
    private McqDistractor distractorCat, distractorCAT, distractorBunny;
    private final TreeSet<McqDistractor> cats = new TreeSet<>();
    private final TreeSet<McqDistractor> bunnies = new TreeSet<>();
    private Options catOption, bunnyOption;
    private Variable catKey, bunnyKey;

    /**
     * Method for setting up test resources before each method call.
     *
     * @throws URISyntaxException
     *         Thrown when an URI was malformed.
     * @throws TemplateParseException 
     */
    @Before
    public void setUp() throws URISyntaxException, TemplateParseException
    {
        name1 = "Cat";
        name2 = "CAT";
        name3 = "Bunny";
        langEN = "en";
        langFR = "fr";
        langDE = "de";
        uri1 = new URI("http://mycat.com");
        uri1 = new URI("http://yourcat.com");
        uri1 = new URI("http://mybunny.com");
        distractorCat = new McqDistractor(CompositeVariable.buildVariable(name1, langEN, uri1));
        distractorCAT = new McqDistractor(CompositeVariable.buildVariable(name2, langDE, uri2));
        distractorBunny = new McqDistractor(CompositeVariable.buildVariable(name3, langFR, uri3));
        cats.add(distractorCat);
        cats.add(distractorCAT);
        bunnies.add(distractorBunny);
        catKey = CompositeVariable.buildVariable("Cat", langEN, new URI("http://cats.com"));
        bunnyKey = CompositeVariable.buildVariable("Bunny", langEN, new URI("http://bunnies.com"));
        catOption = new Options(Arrays.asList(catKey),cats);
        bunnyOption = new Options(Arrays.asList(bunnyKey), bunnies);
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#Options(CompositeVariable, TreeSet)}.
     */
    @Test
    public void testOptions()
    {
        Assert.assertNotNull(catOption);
        Assert.assertEquals(cats, catOption.getDistractors());
        Assert.assertTrue(catOption.getCorrectResponses().contains(catKey));
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#getCorrectResponse()}.
     *
     * @throws URISyntaxException
     */
    @Test
    public void testGetKey() throws URISyntaxException
    {
    	Assert.assertTrue(catOption.getCorrectResponses().contains(catKey));
    	Assert.assertTrue(bunnyOption.getCorrectResponses().contains(catKey));
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#getDistractors()}.
     */
    @Test
    public void testGetDistractors()
    {
        Assert.assertEquals(cats, catOption.getDistractors());
        Assert.assertEquals(bunnies, bunnyOption.getDistractors());
    }

    /**
     * Test aiming to assess the correctness of distractor collection containment in regard to
     * uniqueness of distractors according to their labels.
     */
    @Test
    public void testDistractorCollection()
    {
        Assert.assertEquals(1, catOption.getDistractors().size());
        Assert.assertEquals(1, bunnyOption.getDistractors().size());
        Assert.assertTrue(catOption.getDistractors().contains(distractorCat));
        Assert.assertTrue(bunnyOption.getDistractors().contains(distractorBunny));
        bunnyOption.getDistractors().add(distractorCAT);
        Assert.assertEquals(2, bunnyOption.getDistractors().size());
        Assert.assertTrue(bunnyOption.getDistractors().contains(distractorCAT));
        Assert.assertTrue(bunnyOption.getDistractors().contains(distractorBunny));
    }

    /**
     * Test method for {@link Options#setDistractors(TreeSet)}.
     */
    @Test
    public void testSetDistractors()
    {
        Assert.assertEquals(1, catOption.getDistractors().size());
        catOption.setDistractors(new TreeSet<McqDistractor>());
        Assert.assertEquals(0, catOption.getDistractors().size());
        catOption.setDistractors(bunnies);
        Assert.assertTrue(catOption.getDistractors().contains(distractorBunny));
    }

    /**
     * Test method for {@link Options#trim(int)}.
     *
     * @throws URISyntaxException
     *         Thrown when an URI was malformed.
     * @throws TemplateParseException 
     */
    @Test
    public void testTrim() throws URISyntaxException, TemplateParseException
    {
        final TreeSet<McqDistractor> distractors = new TreeSet<>();
        final McqDistractor bla = new McqDistractor(CompositeVariable.buildVariable("bla", "gr", new URI("http://test.com")));
        final McqDistractor mia = new McqDistractor(CompositeVariable.buildVariable("mia", "lu", new URI("http://test2.com")));
        final McqDistractor numbered = new McqDistractor(CompositeVariable.buildVariable("mi12a", "lu", new URI("http://test23.com")));
        distractorBunny.setSimilarityToCorrectResponse(0.5f);
        distractorCat.setSimilarityToCorrectResponse(0.4f);
        bla.setSimilarityToCorrectResponse(0.39f);
        mia.setSimilarityToCorrectResponse(0.19f);
        numbered.setSimilarityToCorrectResponse(0.001f);
        distractors.add(distractorBunny);
        distractors.add(distractorCat);
        distractors.add(bla);
        distractors.add(mia);
        distractors.add(numbered);
        catOption.setDistractors(distractors);
        Assert.assertEquals(5, catOption.getDistractors().size());
        catOption.trim(2);
        Assert.assertEquals(2, catOption.getDistractors().size());
        Assert.assertTrue(catOption.getDistractors().contains(distractorBunny));
        Assert.assertTrue(catOption.getDistractors().contains(distractorCat));
    }

    /**
     * 
     * Test method for {@link Options#explode(int, boolean)}.
     *
     * @throws URISyntaxException
     *         Thrown when an URI was malformed.
     * @throws TemplateParseException 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testExplode() throws URISyntaxException, TemplateParseException
    {
        final TreeSet<McqDistractor> distractors = new TreeSet<>();
        final McqDistractor bla = new McqDistractor(CompositeVariable.buildVariable("bla", "gr", new URI("http://test.com")));
        final McqDistractor mia = new McqDistractor(CompositeVariable.buildVariable("mia", "lu", new URI("http://test2.com")));
        final McqDistractor numbered = new McqDistractor(CompositeVariable.buildVariable("mi12a", "lu", new URI("http://test23.com")));
        distractorBunny.setSimilarityToCorrectResponse(0.5f);
        distractorCat.setSimilarityToCorrectResponse(0.4f);
        bla.setSimilarityToCorrectResponse(0.39f);
        mia.setSimilarityToCorrectResponse(0.19f);
        numbered.setSimilarityToCorrectResponse(0.001f);
        distractors.add(distractorBunny);
        distractors.add(distractorCat);
        distractors.add(bla);
        distractors.add(mia);
        distractors.add(numbered);
        catOption.setDistractors(((TreeSet<McqDistractor>) distractors.clone()));
        Assert.assertEquals(5, catOption.getDistractors().size());
        Assert.assertEquals(1, catOption.explode(3, false).size());

        catOption.setDistractors(((TreeSet<McqDistractor>) distractors.clone()));
        Assert.assertEquals(5, catOption.getDistractors().size());
        Assert.assertEquals(2, catOption.explode(2, false).size());

        catOption.setDistractors(((TreeSet<McqDistractor>) distractors.clone()));
        Assert.assertEquals(5, catOption.getDistractors().size());
        Assert.assertEquals(5, catOption.explode(1, false).size());

        catOption.setDistractors(((TreeSet<McqDistractor>) distractors.clone()));
        Assert.assertEquals(5, catOption.getDistractors().size());
        Assert.assertEquals(2, catOption.explode(3, true).size());

        catOption.setDistractors(((TreeSet<McqDistractor>) distractors.clone()));
        Assert.assertEquals(5, catOption.getDistractors().size());
        Assert.assertEquals(3, catOption.explode(2, true).size());

        catOption.setDistractors(((TreeSet<McqDistractor>) distractors.clone()));
        Assert.assertEquals(5, catOption.getDistractors().size());
        Assert.assertEquals(5, catOption.explode(1, true).size());
    }
}