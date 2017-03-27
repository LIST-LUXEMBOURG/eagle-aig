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
import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.aig.CompositeVariable;
import lu.list.itis.dkd.aig.util.ConfigurationChanger;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for testing all functionality exposed by {@link Cluster}.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.7.1
 */

public class ClusterTest {
    private static Variable germany, france, luxembourg, england, finnland, estonia, scotland;
    private static Variable berlin, paris, luxembourgCity, london, helsinki, tallinn, edinburgh;
    private static Match german, french, luxembourgian, english, finnish, estonian, scottish;

    /**
     * Method initialising all fields.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ClusterTest.germany = CompositeVariable.buildVariable("Germany", "en", new URI("http://www.germany.de"));
        ClusterTest.france = CompositeVariable.buildVariable("France", "en", new URI("http://www.france.fr"));
        ClusterTest.luxembourg = CompositeVariable.buildVariable("Luxembourg", "en", new URI("http://www.luxembourg.lu"));
        ClusterTest.england = CompositeVariable.buildVariable("England", "en", new URI("http://www.england.co.uk"));
        ClusterTest.finnland = CompositeVariable.buildVariable("Finnland", "en", new URI("http://www.finnland.fi"));
        ClusterTest.estonia = CompositeVariable.buildVariable("Estonia", "en", new URI("http://www.estonia.ee"));
        ClusterTest.scotland = CompositeVariable.buildVariable("Scotland", "en", new URI("http://www.scotland.scot"));

        ClusterTest.berlin = CompositeVariable.buildVariable("Berlin", "en", new URI("http://www.germany.de"));
        ClusterTest.paris = CompositeVariable.buildVariable("Paris", "en", new URI("http://www.france.fr"));
        ClusterTest.luxembourgCity = CompositeVariable.buildVariable("LuxembourgCity", "en", new URI("http://www.luxembourg.lu"));
        ClusterTest.london = CompositeVariable.buildVariable("London", "en", new URI("http://www.england.co.uk"));
        ClusterTest.helsinki = CompositeVariable.buildVariable("Helsinki", "en", new URI("http://www.finnland.fi"));
        ClusterTest.tallinn = CompositeVariable.buildVariable("Tallinn", "en", new URI("http://www.estonia.ee"));
        ClusterTest.edinburgh = CompositeVariable.buildVariable("Edinburgh", "en", new URI("http://www.scotland.scot"));

        ClusterTest.german = new Match(ClusterTest.germany, ClusterTest.berlin, null);
        ClusterTest.french = new Match(ClusterTest.france, ClusterTest.paris, null);
        ClusterTest.luxembourgian = new Match(ClusterTest.luxembourg, ClusterTest.luxembourgCity, null);
        ClusterTest.english = new Match(ClusterTest.england, ClusterTest.london, null);
        ClusterTest.finnish = new Match(ClusterTest.finnland, ClusterTest.helsinki, null);
        ClusterTest.estonian = new Match(ClusterTest.estonia, ClusterTest.tallinn, null);
        ClusterTest.scottish = new Match(ClusterTest.scotland, ClusterTest.edinburgh, null);

        final SimilarityProvider provider = SimilarityProvider.getInstance();
        ConfigurationChanger.configureProvider(provider);
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Cluster#Cluster(java.util.List)}.
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testClusterListOfMatch()  {
        final ArrayList<Match> matches = new ArrayList<>();
        matches.add(ClusterTest.german);
        matches.add(ClusterTest.french);
        matches.add(ClusterTest.finnish);
        matches.add(ClusterTest.scottish);
        final Cluster cluster = new Cluster(matches);
        Assert.assertNotNull(cluster);
        Assert.assertNotNull(cluster.getMatches());
        Assert.assertEquals(4, cluster.getMatches().size());
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.match.Cluster#Cluster(lu.list.itis.dkd.aig.match.Match)}.
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testClusterMatch() {
        final Cluster cluster = new Cluster(ClusterTest.german);
        Assert.assertNotNull(cluster);
        cluster.addMatch(ClusterTest.french);
        cluster.addMatch(ClusterTest.finnish);
        cluster.addMatch(ClusterTest.scottish);
        Assert.assertNotNull(cluster.getMatches());
        Assert.assertEquals(4, cluster.getMatches().size());
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.match.Cluster#addMatch(lu.list.itis.dkd.aig.match.Match)}.
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testAddItem() {
        final Cluster cluster = new Cluster(ClusterTest.german);
        Assert.assertTrue(cluster.getMatches().contains(ClusterTest.german));
        Assert.assertEquals(1, cluster.getMatches().size());
        cluster.addMatch(ClusterTest.french);
        Assert.assertTrue(cluster.getMatches().contains(ClusterTest.french));
        Assert.assertEquals(2, cluster.getMatches().size());
        cluster.addMatch(ClusterTest.finnish);
        Assert.assertTrue(cluster.getMatches().contains(ClusterTest.finnish));
        Assert.assertEquals(3, cluster.getMatches().size());
        cluster.addMatch(ClusterTest.scottish);
        Assert.assertTrue(cluster.getMatches().contains(ClusterTest.scottish));
        Assert.assertEquals(4, cluster.getMatches().size());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Cluster#size()}.
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testSize()  {
        final ArrayList<Match> matches = new ArrayList<>();
        matches.add(ClusterTest.german);
        matches.add(ClusterTest.french);
        matches.add(ClusterTest.finnish);
        matches.add(ClusterTest.scottish);
        final Cluster cluster = new Cluster(matches);
        Assert.assertEquals(matches.size(), cluster.size());
        cluster.addMatch(ClusterTest.estonian);
        Assert.assertEquals(matches.size() + 1, cluster.size());
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Cluster#getMatches()}.
     *
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testGetItems() {
        final ArrayList<Match> matches = new ArrayList<>();
        matches.add(ClusterTest.german);
        matches.add(ClusterTest.french);
        matches.add(ClusterTest.finnish);
        matches.add(ClusterTest.luxembourgian);
        final Cluster cluster = new Cluster(matches);

        Assert.assertNotNull(cluster.getMatches());
        Assert.assertTrue(matches.containsAll(cluster.getMatches()));
        Assert.assertTrue(cluster.getMatches().containsAll(matches));
    }

    //@formatter:off
    /**
     * Test method testing whether the metrics are compiled correctly.
     *
     * The method relies on the implementation of the distance matrix computation. For
     * this specific test, the matrix is supposed to give (using Soundex):
     * Header omitted. Matrix is symmetric.
     * Germany -> Berlin			1.0	 	 0.25	 0.25	 0.0	 0.25	 0.0	 0.0
     * France -> Paris				0.25	 1.0	 0.25	 0.0	 0.0	 0.25	 0.0
     * Finland -> Helsinki			0.25	 0.25	 1.0	 0.0	 0.25	 0.25	 0.0
     * Luxembourg -> LuxembourgCity	0.0	 	 0.0	 0.0	 1.0	 0.25	 0.25	 0.5
     * England -> London			0.25	 0.0	 0.25	 0.25	 1.0	 0.0	 0.0
     * Estonia -> Tallinn			0.0	 	 0.25	 0.25	 0.25	 0.0	 1.0	 0.25
     * Scotland -> Edinburgh		0.0	 	 0.0	 0.0	 0.5	 0.0	 0.25	 1.0
     *
     * @throws SecurityException
     * 		Thrown when access to the requested field is prohibited.
     * @throws NoSuchFieldException
     * 		Thrown when the requested field cannot be found.
     * @throws IllegalAccessException
     * 		Thrown when access to the field is prohibited.
     * @throws IllegalArgumentException
     * 		Thrown when the argument passed to the field cannot be interpreted.
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test//formatter:on
    public void testGetDispercity() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException  {
        final ArrayList<Match> matches = new ArrayList<>();
        matches.add(ClusterTest.german);
        matches.add(ClusterTest.french);
        matches.add(ClusterTest.finnish);
        matches.add(ClusterTest.luxembourgian);
        matches.add(ClusterTest.english);

        Cluster cluster = new Cluster(matches);

        Assert.assertTrue(Float.compare(cluster.getDispersityByDistanceSum(), 0.15f) == 0);
        Assert.assertTrue(Float.compare(cluster.getDispersityByStandardDeviation(), 0.1224745f) == 0);

        cluster.addMatch(ClusterTest.scottish);

        Assert.assertTrue(Float.compare(cluster.getDispersityByDistanceSum(), 0.13333334f) == 0);
        Assert.assertTrue(Float.compare(cluster.getDispersityByStandardDeviation(), 0.15456031f) == 0);

        matches.clear();
        matches.add(ClusterTest.estonian);
        cluster = new Cluster(matches);

        Assert.assertTrue(Float.compare(cluster.getDispersityByDistanceSum(), 0f) == 0);
        Assert.assertTrue(Float.compare(cluster.getDispersityByStandardDeviation(), 0f) == 0);

        cluster.addMatch(ClusterTest.scottish);

        Assert.assertTrue(Float.compare(cluster.getDispersityByDistanceSum(), 0.25f) == 0);
        Assert.assertTrue(Float.compare(cluster.getDispersityByStandardDeviation(), 0f) == 0);
    }


    /**
     * Test method for {@link lu.list.itis.dkd.aig.match.Cluster#getOrderedMatches()}.
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testGetOrderedMatches() 	{
        final ArrayList<Match> matches = new ArrayList<>();
        matches.add(ClusterTest.german);
        matches.add(ClusterTest.french);
        matches.add(ClusterTest.finnish);
        matches.add(ClusterTest.luxembourgian);
        matches.add(ClusterTest.english);

        final Cluster cluster = new Cluster(matches);
        List<Match> orderedMatches = cluster.getOrderedMatches();
        Assert.assertEquals(ClusterTest.luxembourgian, orderedMatches.get(0));
        Assert.assertEquals(ClusterTest.french, orderedMatches.get(1));
        Assert.assertTrue(orderedMatches.get(2).equals(ClusterTest.german) || orderedMatches.get(2).equals(ClusterTest.finnish) || orderedMatches.get(2).equals(ClusterTest.english));
        Assert.assertTrue(orderedMatches.get(3).equals(ClusterTest.german) || orderedMatches.get(3).equals(ClusterTest.finnish) || orderedMatches.get(3).equals(ClusterTest.english));
        Assert.assertTrue(orderedMatches.get(4).equals(ClusterTest.german) || orderedMatches.get(4).equals(ClusterTest.finnish) || orderedMatches.get(4).equals(ClusterTest.english));

        cluster.addMatch(ClusterTest.scottish);
        orderedMatches = cluster.getOrderedMatches();
        Assert.assertTrue(orderedMatches.get(0).equals(ClusterTest.french) || orderedMatches.get(0).equals(ClusterTest.scottish));
        Assert.assertTrue(orderedMatches.get(1).equals(ClusterTest.french) || orderedMatches.get(1).equals(ClusterTest.scottish));
        Assert.assertTrue(orderedMatches.get(2).equals(ClusterTest.german) || orderedMatches.get(2).equals(ClusterTest.finnish) || orderedMatches.get(2).equals(ClusterTest.english) || orderedMatches.get(2).equals(ClusterTest.luxembourgian));
        Assert.assertTrue(orderedMatches.get(3).equals(ClusterTest.german) || orderedMatches.get(3).equals(ClusterTest.finnish) || orderedMatches.get(3).equals(ClusterTest.english) || orderedMatches.get(3).equals(ClusterTest.luxembourgian));
        Assert.assertTrue(orderedMatches.get(4).equals(ClusterTest.german) || orderedMatches.get(4).equals(ClusterTest.finnish) || orderedMatches.get(4).equals(ClusterTest.english) || orderedMatches.get(4).equals(ClusterTest.luxembourgian));
        Assert.assertTrue(orderedMatches.get(5).equals(ClusterTest.german) || orderedMatches.get(5).equals(ClusterTest.finnish) || orderedMatches.get(5).equals(ClusterTest.english) || orderedMatches.get(5).equals(ClusterTest.luxembourgian));
    }
}