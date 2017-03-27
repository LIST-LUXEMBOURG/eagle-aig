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
package lu.list.itis.dkd.aig.match;

import lu.list.itis.dkd.aig.CompositeVariable;
import lu.list.itis.dkd.aig.Variable;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;

/**
 * Test class for assessing the behaviour of {@link Match}.
 *
 * @author Eric TOBIAS [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.2.0
 */
public class MatchTest {
    private static Variable kitty, bunny, turtle, cat, redPanda, catFeedback, turtleFeedback, bunnyFeedback;
    private static Match catKitty, catKitty2, catRedPanda, turtleKitty, bunnyKitty;

    /**
     * Method initialising all fields.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        MatchTest.kitty = CompositeVariable.buildVariable("Kitty", "kitty", new URI("http://www.kitty.com"));
        MatchTest.bunny = CompositeVariable.buildVariable("Bunny", "bunny", new URI("http://www.bunny.com"));
        MatchTest.turtle = CompositeVariable.buildVariable("Turtle", "turtle", new URI("http://www.turtle.com"));
        MatchTest.cat = CompositeVariable.buildVariable("Kitty", "cat", new URI("http://www.kitty.com"));
        MatchTest.redPanda = CompositeVariable.buildVariable("kitty", "redPanda", null);

        MatchTest.catFeedback = CompositeVariable.buildVariable("catFeedback", "en", new URI("http://cat.com"));
        MatchTest.turtleFeedback = CompositeVariable.buildVariable("turtleFeedback", "en", new URI("http://turtle.com"));
        MatchTest.bunnyFeedback = CompositeVariable.buildVariable("bunnyFeedback", "en", new URI("http://bunny.com"));


        MatchTest.catKitty = new Match(MatchTest.cat, MatchTest.kitty, MatchTest.catFeedback);
        MatchTest.catKitty2 = new Match(MatchTest.cat, MatchTest.kitty, MatchTest.catFeedback);
        MatchTest.catRedPanda = new Match(MatchTest.cat, MatchTest.redPanda, MatchTest.catFeedback);
        MatchTest.turtleKitty = new Match(MatchTest.turtle, MatchTest.kitty, MatchTest.turtleFeedback);
        MatchTest.bunnyKitty = new Match(MatchTest.bunny, MatchTest.kitty, MatchTest.bunnyFeedback);
    }


    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.match.Match#Match(lu.list.itis.dkd.aig.CompositeVariable, lu.list.itis.dkd.aig.CompositeVariable, lu.list.itis.dkd.aig.CompositeVariable)}
     * .
     */
    @Test
    public void testMatch() {
        Assert.assertEquals(MatchTest.cat, MatchTest.catKitty.getKeyVariable());
        Assert.assertEquals(MatchTest.kitty, MatchTest.catKitty.getAnswerVariable());
        Assert.assertEquals(MatchTest.catFeedback, MatchTest.catKitty.getFeedback());
        Assert.assertEquals(MatchTest.turtleFeedback, MatchTest.turtleKitty.getFeedback());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Match#getKeyVariable()}.
     */
    @Test
    public void testGetStemVariable() {
        Assert.assertEquals(MatchTest.cat, MatchTest.catKitty.getKeyVariable());
        Assert.assertEquals(MatchTest.cat, MatchTest.catRedPanda.getKeyVariable());
        Assert.assertEquals(MatchTest.bunny, MatchTest.bunnyKitty.getKeyVariable());
        Assert.assertEquals(MatchTest.turtle, MatchTest.turtleKitty.getKeyVariable());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Match#getAnswerVariable()}.
     */
    @Test
    public void testGetAnswerVariable() {
        Assert.assertEquals(MatchTest.kitty, MatchTest.catKitty.getAnswerVariable());
        Assert.assertEquals(MatchTest.redPanda, MatchTest.catRedPanda.getAnswerVariable());
        Assert.assertEquals(MatchTest.kitty, MatchTest.bunnyKitty.getAnswerVariable());
        Assert.assertEquals(MatchTest.kitty, MatchTest.turtleKitty.getAnswerVariable());
    }


    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Match#getFeedback()}.
     */
    @Test
    public void testGetFeedback() {
        Assert.assertEquals(MatchTest.catFeedback, MatchTest.catKitty.getFeedback());
        Assert.assertEquals(MatchTest.bunnyFeedback, MatchTest.bunnyKitty.getFeedback());
        Assert.assertEquals(MatchTest.turtleFeedback, MatchTest.turtleKitty.getFeedback());
        Assert.assertEquals(MatchTest.catFeedback, MatchTest.catRedPanda.getFeedback());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Match#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Assert.assertEquals((MatchTest.cat.getValue().toLowerCase() + MatchTest.kitty.getValue().toLowerCase()).hashCode(), MatchTest.catKitty.hashCode());
        Assert.assertEquals((MatchTest.turtle.getValue().toLowerCase() + MatchTest.kitty.getValue().toLowerCase()).hashCode(), MatchTest.turtleKitty.hashCode());
        Assert.assertFalse((MatchTest.kitty.getValue().toLowerCase() + MatchTest.turtle.getValue().toLowerCase()).hashCode() == MatchTest.turtleKitty.hashCode());
        Assert.assertEquals(MatchTest.catKitty.hashCode(), MatchTest.catRedPanda.hashCode());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Match#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        Assert.assertFalse(MatchTest.catKitty.equals(null));
        Assert.assertFalse(MatchTest.catKitty.equals("bla"));
        Assert.assertTrue(MatchTest.catKitty.equals(MatchTest.catKitty2));
        Assert.assertTrue(MatchTest.catKitty2.equals(MatchTest.catKitty));
    }
}