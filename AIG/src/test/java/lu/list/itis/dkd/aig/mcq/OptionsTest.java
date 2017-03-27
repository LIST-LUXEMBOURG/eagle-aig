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

import lu.list.itis.dkd.aig.Variable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.util.TreeSet;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.8.0
 */
public class OptionsTest {

    private String name1, name2, name3;
    private String langEN, langFR, langDE;
    private URI uri1, uri2, uri3;
    private McqDistractor distractorCat, distractorCAT, distractorBunny;
    private final TreeSet<McqDistractor> cats = new TreeSet<>();
    private final TreeSet<McqDistractor> bunnies = new TreeSet<>();
    private Options catOption, bunnyOption;
    private Variable catKey, bunnyKey;


    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {}

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.mcq.Options#Options(java.util.List, java.util.TreeSet)}.
     */
    @Test
    public final void testOptions() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#getCorrectResponses()}.
     */
    @Test
    public final void testGetCorrectResponses() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#getDistractors()}.
     */
    @Test
    public final void testGetDistractors() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#setDistractors(java.util.TreeSet)}.
     */
    @Test
    public final void testSetDistractors() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#trim(int)}.
     */
    @Test
    public final void testTrim() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#getDisparity()}.
     */
    @Test
    public final void testGetDisparity() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#explode(int, boolean)}.
     */
    @Test
    public final void testExplode() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#getAndRemoveVariable(java.net.URI)}.
     */
    @Test
    public final void testGetAndRemoveVariable() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.mcq.Options#clone()}.
     */
    @Test
    public final void testClone() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link java.lang.Object#hashCode()}.
     */
    @Test
    public final void testHashCode() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
     */
    @Test
    public final void testEquals() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link java.lang.Object#clone()}.
     */
    @Test
    public final void testClone1() {
        Assert.fail("Not yet implemented"); // TODO
    }

}
