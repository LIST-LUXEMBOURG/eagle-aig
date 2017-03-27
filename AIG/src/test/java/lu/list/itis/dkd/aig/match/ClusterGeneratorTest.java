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

import lu.list.itis.dkd.aig.SimilarityProvider;
import lu.list.itis.dkd.aig.Value;
import lu.list.itis.dkd.aig.ValueType;
import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.VariableTest;
import lu.list.itis.dkd.aig.CompositeVariable;
import lu.list.itis.dkd.aig.match.ClusterGenerator.OperationMode;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.ConfigurationChanger;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.assess.cloze.util.TestResources;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

/**
 * Class for testing the functionality of the {@link ClusterGenerator}.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.7.1
 */
public class ClusterGeneratorTest {

    private static Table<Match, Match, Float> matrix;
    private static Match matchOne, matchTwo, matchThree, matchFour;
    private static List<Match> matches;
    private static Properties properties;

    /**
     * Method for setting up fields before test execution.
     *
     * @throws java.lang.Exception
     *         Thrown when an exception occurs during the instantiation of all fields necessary for
     *         the test to run or during any of the configuration steps.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ClusterGeneratorTest.matchOne = new Match(CompositeVariable.buildVariable("rowOne", "en", new URI("http://row.com")), CompositeVariable.buildVariable("rowOne", "en", new URI("http://row.com")),  CompositeVariable.buildVariable("rowOneFeedback", "en", new URI("http://row.com")));
        ClusterGeneratorTest.matchTwo = new Match(CompositeVariable.buildVariable("rowTwo", "en", new URI("http://row.com")), CompositeVariable.buildVariable("rowTwo", "en", new URI("http://row.com")), CompositeVariable.buildVariable("rowTwoFeedback", "en", new URI("http://row.com")));
        ClusterGeneratorTest.matchThree = new Match(CompositeVariable.buildVariable("rowThree", "en", new URI("http://row.com")),  CompositeVariable.buildVariable("rowThree", "en", new URI("http://row.com")), CompositeVariable.buildVariable("rowThreeFeedback", "en", new URI("http://row.com")));
        ClusterGeneratorTest.matchFour = new Match(CompositeVariable.buildVariable("rowFour", "en", new URI("http://row.com")), CompositeVariable.buildVariable("rowFour", "en", new URI("http://row.com")), CompositeVariable.buildVariable("rowFourFeedback", "en", new URI("http://row.com")));
        ClusterGeneratorTest.matches = new ArrayList<>();
        ClusterGeneratorTest.matches.add(ClusterGeneratorTest.matchOne);
        ClusterGeneratorTest.matches.add(ClusterGeneratorTest.matchTwo);
        ClusterGeneratorTest.matches.add(ClusterGeneratorTest.matchThree);
        ClusterGeneratorTest.matches.add(ClusterGeneratorTest.matchFour);

        ClusterGeneratorTest.properties = PropertiesFetcher.getProperties(TestResources.class, "test.properties");
        final SimilarityProvider provider = SimilarityProvider.getInstance();
        ConfigurationChanger.configureProvider(provider);
    }

    /**
     * Method setting up fields before each test method call.
     */
    @Before
    public void setUp() {
        ClusterGeneratorTest.matrix = ArrayTable.create(ClusterGeneratorTest.matches, ClusterGeneratorTest.matches);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchOne, ClusterGeneratorTest.matchOne, 11f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchOne, ClusterGeneratorTest.matchTwo, 12f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchOne, ClusterGeneratorTest.matchThree, 13f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchOne, ClusterGeneratorTest.matchFour, 14f);

        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchTwo, ClusterGeneratorTest.matchOne, 21f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchTwo, ClusterGeneratorTest.matchTwo, 22f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchTwo, ClusterGeneratorTest.matchThree, 23f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchTwo, ClusterGeneratorTest.matchFour, 24f);

        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchThree, ClusterGeneratorTest.matchOne, 31f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchThree, ClusterGeneratorTest.matchTwo, 32f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchThree, ClusterGeneratorTest.matchThree, 33f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchThree, ClusterGeneratorTest.matchFour, 34f);

        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchFour, ClusterGeneratorTest.matchOne, 41f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchFour, ClusterGeneratorTest.matchTwo, 42f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchFour, ClusterGeneratorTest.matchThree, 43f);
        ClusterGeneratorTest.matrix.put(ClusterGeneratorTest.matchFour, ClusterGeneratorTest.matchFour, 44f);
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.match.ClusterGenerator#ClusterGenerator(java.lang.String, lu.list.itis.dkd.aig.match.ClusterGenerator.OperationMode)}
     * .
     *
     * @throws SecurityException
     *         Thrown when the method could not be accessed.
     * @throws NoSuchFieldException
     *         Thrown when the field could not be found.
     * @throws IllegalArgumentException
     *         Thrown when the provided arguments were not accepted.
     * @throws IllegalAccessException
     *         Thrown when access to the method was restricted.
     */
    @Test
    public void testClusterGeneratorStringOperationMode() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ClusterGenerator generator = new ClusterGenerator("datasource", OperationMode.BISECTING_KMEANS);
        Assert.assertNotNull(generator);

        final Field datasource = generator.getClass().getDeclaredField("dataSource");
        datasource.setAccessible(true);
        final Field mode = generator.getClass().getDeclaredField("mode");
        mode.setAccessible(true);

        Assert.assertNotNull(datasource.get(generator));
        Assert.assertNotNull(mode.get(generator));
        Assert.assertEquals("datasource", datasource.get(generator));
        Assert.assertEquals(OperationMode.BISECTING_KMEANS, mode.get(generator));

        generator = new ClusterGenerator("pony", OperationMode.RANDOM);
        Assert.assertNotNull(datasource.get(generator));
        Assert.assertNotNull(mode.get(generator));
        Assert.assertEquals("pony", datasource.get(generator));
        Assert.assertEquals(OperationMode.RANDOM, mode.get(generator));
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.ClusterGenerator#ClusterGenerator()}.
     *
     * @throws SecurityException
     *         Thrown when the method could not be accessed.
     * @throws NoSuchFieldException
     *         Thrown when the field could not be found.
     * @throws IllegalArgumentException
     *         Thrown when the provided arguments were not accepted.
     * @throws IllegalAccessException
     *         Thrown when access to the method was restricted.
     */
    @Test
    public void testClusterGenerator() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        final ClusterGenerator generator = new ClusterGenerator();
        Assert.assertNotNull(generator);

        final Field datasource = generator.getClass().getDeclaredField("dataSource");
        datasource.setAccessible(true);
        final Field mode = generator.getClass().getDeclaredField("mode");
        mode.setAccessible(true);

        Assert.assertNull(datasource.get(generator));
        Assert.assertNotNull(mode.get(generator));
        Assert.assertEquals(OperationMode.RANDOM, mode.get(generator));
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.match.ClusterGenerator#computeClusters(java.util.List, int, int[])}
     * using the BISECTING_KMEANS mode. .
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testComputeClusters_BisectingKmeans()  {
        final ClusterGenerator generator = new ClusterGenerator(ClusterGeneratorTest.properties.getProperty("datasource"), OperationMode.BISECTING_KMEANS);
        final List<Match> _matches = generator.findAllUniquePairs(ClusterGeneratorTest.properties.getProperty("source.query"));
        ClusterList clusters = null;
        try {
            clusters = generator.computeClusters(_matches, 4);
        } catch (final ClusterCompositionException e) {
            Assert.fail("Unexpected exception occurred!");
            e.printStackTrace();
        }
        Assert.assertNotNull(clusters);
        Assert.assertTrue(clusters.size() > 0);

        for (final Cluster cluster : clusters.getClusters()) {
            Assert.assertEquals(4, cluster.size());
        }
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.match.ClusterGenerator#computeClusters(java.util.List, int, int[])}
     * using the KMEDOID mode. .
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testComputeClusters_Kmedoid() {
        final ClusterGenerator generator = new ClusterGenerator(ClusterGeneratorTest.properties.getProperty("datasource"), OperationMode.KMEDOID);
        final List<Match> _matches = generator.findAllUniquePairs(ClusterGeneratorTest.properties.getProperty("source.query"));

        while ((_matches.size() % 4) != 0) {
            _matches.remove(0);
        }

        ClusterList clusters = null;
        try {
            clusters = generator.computeClusters(_matches, 4);
        } catch (final ClusterCompositionException e) {
            /** There are no clusters to validate as the computation did not manage to complete. */
            System.out.println("No clusters generated, algorithm was unable to compute equal sized clusters!");
        }
        Assert.assertNotNull(clusters);
        Assert.assertTrue(clusters.size() > 0);

        for (final Cluster cluster : clusters.getClusters()) {
            Assert.assertEquals(4, cluster.size());
        }
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.match.ClusterGenerator#findAllUniquePairs(java.lang.String)}.
     * Please note that the test depends heavily on the query and data source. Even if those
     * parameters remain constant, changes to the knowledge base of the data source might influence
     * the outcome of the test. Please refer to dbpedia_test_full.htm in the res folder for the
     * output from the dbpedia data source that was used when the test was designed.
     */
    @Test
    public void testFindAllUniquePairs() {
        final ClusterGenerator generator = new ClusterGenerator(ClusterGeneratorTest.properties.getProperty("datasource"), OperationMode.RANDOM);
        final List<Match> _matches = generator.findAllUniquePairs(ClusterGeneratorTest.properties.getProperty("source.query"));

        Assert.assertEquals(147, _matches.size());
        final HashSet<Match> noDuplicates = new HashSet<>(_matches);
        // Nigeria figures twice in the original query but the method correctly removes duplicates..
        Assert.assertEquals(147, noDuplicates.size());

        /**
         * While this test is surely not 100% accurate, it gives an idea whether the process
         * recovers the list of countries as it should or at least the number of key -> answer pairs
         * it should.
         */
    }

    /**
     * Test method for {@link ClusterGenerator#computeDistances(List)}. The method used the data
     * source and source.query properties form the test property file. Please note that this methods
     * depends heavily on the stability of the output from the data source regarding the provided
     * query. The resource folder contains a query output from the time the test was written as a
     * reference.
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testComputeDistances()  {
        final ClusterGenerator generator = new ClusterGenerator(ClusterGeneratorTest.properties.getProperty("datasource"), OperationMode.BISECTING_KMEANS);
        final List<Match> _matches = generator.findAllUniquePairs(ClusterGeneratorTest.properties.getProperty("source.query"));
        final ArrayTable<Match, Match, Float> distanceMatrix = generator.computeDistances(_matches);

        Assert.assertNotNull(distanceMatrix);
        Assert.assertNotNull(distanceMatrix.get(_matches.get(0), _matches.get(0)));
        Assert.assertTrue(isMatrixSymmetric(distanceMatrix));

        final Random randomGenerator = new Random();

        for (int i = 0; i < 10; i++) {
            final Match rowMatch = distanceMatrix.rowKeyList().get(randomGenerator.nextInt(distanceMatrix.rowKeyList().size()));
            final Match columnMatch = distanceMatrix.columnKeyList().get(randomGenerator.nextInt(distanceMatrix.columnKeyList().size()));

            Assert.assertEquals(SimilarityProvider.getInstance().compare(rowMatch.getAnswerVariable(), columnMatch.getAnswerVariable()), distanceMatrix.get(rowMatch, columnMatch), 0.0001f);
        }
    }

    /**
     * Method for checking whether a matrix is symmetric.
     *
     * @param distanceMatrix
     *        The matrix to test.
     * @return <code>True</code> iff the matrix is symmetric with either 0 or 1 as diagonal.
     *         <code>False</code> otherwise.
     */
    private boolean isMatrixSymmetric(final ArrayTable<Match, Match, Float> distanceMatrix) {
        final ImmutableList<Match> rowKeyList = distanceMatrix.rowKeyList();
        final ImmutableList<Match> columnKeyList = distanceMatrix.columnKeyList();
        if (!rowKeyList.equals(columnKeyList)) {
            return false;
        }

        for (int row = 0; row < rowKeyList.size(); row++) {
            for (int column = row; column < columnKeyList.size(); column++) {
                if (Float.compare(distanceMatrix.get(rowKeyList.get(row), columnKeyList.get(column)), distanceMatrix.get(columnKeyList.get(column), rowKeyList.get(row))) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}