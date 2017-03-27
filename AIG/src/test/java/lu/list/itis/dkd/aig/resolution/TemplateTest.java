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
package lu.list.itis.dkd.aig.resolution;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Test class for {@link Template}.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.7.2
 */
public class TemplateTest {

    private final static String templateString = "/testTemplate.xml"; //$NON-NLS-1$
    private final HashMap<String, String> inputs = new HashMap<>();

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        inputs.put("http://list.lu/conceptURI", "http://dbpedia.org/resource/Nigeria"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        inputs.clear();
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.resolution.Template#Template(org.jdom2.Document)}
     * .
     *
     * @throws Exception
     */
    @Test
    public final void testTemplate() throws Exception {

        final ItemFactoryBuilder builder = new ItemFactoryBuilder();
        builder.withTemplate(this.getClass().getResourceAsStream(TemplateTest.templateString));
        builder.withInput(inputs);
        final ItemFactory factory = builder.build();

        Assert.fail("Not implemented yet!"); //$NON-NLS-1$
    }
}