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
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.assess.cloze.util.TestResources;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Properties;

/**
 * Test class for asserting the functionality of {@link McqDistractorGenerator}.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.7.1
 */
public class McqDistractorGeneratorTest {
    private static Properties properties;
    private static Variable capitalKey;

    /**
     * Method for setting up a variable valid throughout test execution.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        McqDistractorGeneratorTest.properties = PropertiesFetcher.getProperties(TestResources.class, "test.properties");
        McqDistractorGeneratorTest.capitalKey = CompositeVariable.buildVariable("Paris", "en", new URI("http://dbpedia.org/resource/Paris"));
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.mcq.McqDistractorGenerator#McqDistractorGenerator(java.lang.String)}
     * .
     *
     * @throws IllegalAccessException
     *         Thrown when this Field object is enforcing Java language access control and the
     *         underlying field is inaccessible.
     * @throws IllegalArgumentException
     *         Thrown when the specified object is not an instance of the class or interface
     *         declaring the underlying field (or a subclass or implementor thereof).
     */
    @Test
    public void testDistractorGenerator() throws IllegalArgumentException, IllegalAccessException {
        final McqDistractorGenerator generator = new McqDistractorGenerator("test.source");
        final Field[] fields = McqDistractorGenerator.class.getDeclaredFields();
        for (final Field field : fields) {
            field.setAccessible(true);
            Assert.assertNotNull(field.get(generator));
            if (field.getName().equals("dataSource")) {
                Assert.assertEquals("test.source", field.get(generator));
            }
        }
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.mcq.McqDistractorGenerator#findDistractorsFor(lu.list.itis.dkd.aig.CompositeVariable, java.lang.String, int)}
     * .
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testFindDistractorsFor() {
        final McqDistractorGenerator generator = new McqDistractorGenerator(McqDistractorGeneratorTest.properties.getProperty("datasource"));
        final Options options = generator.findDistractorsFor(McqDistractorGeneratorTest.capitalKey, McqDistractorGeneratorTest.properties.getProperty("source.query"), 10);
        Assert.assertTrue(options.getDistractors().size() <= 10);
        Assert.assertEquals(McqDistractorGeneratorTest.capitalKey, options.getCorrectResponse());

        Assert.fail("Test not complete and semantic similarity only produces errors!");
    }
}