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
import lu.list.itis.dkd.aig.util.MatrixFormatter;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Class representing a cluster as used in match items. Clusters are composed of item instances and
 * contain meta data about the cluster.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.7.1
 */
@NonNullByDefault
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Cluster {
    private static final Logger logger = Logger.getLogger(Cluster.class.getSimpleName());
    private List<Match> matches;
    /** The inverse of coherence. (More or less) */
    @XmlAttribute(name = "dispersity_Distance_Sum")
    private float dispersityByDistanceSum = 0f;
    @XmlAttribute(name = "dispersity_Standard_Deviation")
    private float dispersityByStandardDeviation = 0f;
    @XmlTransient
    private Table<Match, Match, Float> distanceMatrix;


    /**
     * Constructor initialising all fields. The constructor will make a copy of the provided
     * {@link List}. The construction will fail if the provided list is empty, resulting in an
     * {@link IllegalArgumentException}.
     *
     * @param items
     *        The initial list of items to add.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     * @pre items.size() > 0
     */
    public Cluster(final List<Match> items) {
        Preconditions.checkArgument(items.size() > 0, "No cluster can be build as no items were provided!"); //$NON-NLS-1$
        matches = new ArrayList<>(items);
        computeMetrics();
    }

    /**
     * Constructor initialising all fields. The constructor will make a copy of the provided
     * {@link List}.
     *
     * @param items
     *        The initial list of items to add.
     * @param distanceMatrix
     *        The matrix containing inter-match distances.
     */
    public Cluster(final List<Match> items, final Table<Match, Match, Float> distanceMatrix) {
        matches = new ArrayList<>(items);
        computeMetrics(distanceMatrix);
    }

    /**
     * Constructor initialising all fields.
     *
     * @param item
     *        The initial item to add.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    public Cluster(final Match item)  {
        matches = new ArrayList<>();
        matches.add(item);
        computeMetrics();
    }

    /**
     * Constructor initialising all fields but leaving matches unpopulated.
     *
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    public Cluster()  {
        matches = new ArrayList<>();
        computeMetrics();
    }

    /**
     * Method for adding an item to the cluster. The method will also recompute the disparity. The
     * addition to the cluster will add the item to the underlying list.<br>
     * <br>
     *
     * The addition will not be made if the cluster already contains the item.
     *
     * @param item
     *        The item to add.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    public void addMatch(final Match item)  {
        if (matches.contains(item)) {
            return;
        }
        matches.add(item);
        computeMetrics();
    }


    /**
     * Method returning the number of items in the cluster.
     *
     * @return The size of the cluster.
     */
    public int size() {
        return matches.size();
    }

    /**
     * Simple getter method for items.
     *
     * @return A list containing all items contained in this cluster.
     */
    public List<Match> getMatches() {
        return matches;
    }

    /**
     * Setter method for items in the cluster. Used mainly by JAXB. It is advised to use the
     * constructor instead. The method will also call for a calculation of the cluster dispersity.
     *
     * @param items
     *        The {@link Match} instances to add.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    public void setMatches(final List<Match> items)  {
        matches = items;
        computeMetrics();
    }

    /**
     * Helper method used to populate the distance matrix.
     *
     * @param matches
     *        The matches used to populate the symmetric distance matrix.
     * @return The populated matrix.
     * @throws SimilarityComputationException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    private Table<Match, Match, Float> computeDistances()  {
        distanceMatrix = ArrayTable.create(matches, matches);
        for (final Match match : matches) {
            for (final Match that : matches) {
                if (distanceMatrix.get(match, that) != null) {
                    continue;
                }
                if (match == that) {
                    distanceMatrix.put(match, that, 1f);
                    distanceMatrix.put(that, match, 1f);
                    continue;
                }
                distanceMatrix.put(match, that, SimilarityProvider.getInstance().compare(match.getAnswerVariable(), that.getAnswerVariable()));
                distanceMatrix.put(that, match, SimilarityProvider.getInstance().compare(that.getAnswerVariable(), match.getAnswerVariable()));
            }
        }
        return distanceMatrix;
    }

    /**
     * Method for computing the dispersity of a cluster; the measure of dispersity of the cluster's
     * items. The lower the number the more similar included items are. The method will
     * automatically set the dispersity of the provided cluster.
     *
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    private void computeMetrics(){
        computeDistances();
        Cluster.logger.log(Level.FINE, MatrixFormatter.format((ArrayTable<Match, Match, Float>) distanceMatrix));
        dispersityByDistanceSum = getNormalisedDistanceSum();
        dispersityByStandardDeviation = getStandardDeviation();
    }

    /**
     * Method for computing the dispersity of a cluster; the measure of dispersity of the cluster's
     * items. The lower the number the more similar included items are. The method will
     * automatically set the dispersity of the provided cluster.<br>
     * This method uses a precomputed distance matrix.
     *
     * @param distances
     *        The matrix holding all inter-match distances.
     */
    private void computeMetrics(final Table<Match, Match, Float> distances) {
        distanceMatrix = distances;
        dispersityByDistanceSum = getNormalisedDistanceSum();
        dispersityByStandardDeviation = getStandardDeviation();
    }

    /**
     * Helper method used to compute the standard deviation of a clusters' item distances.
     *
     * @return The standard deviation of distances of the cluster's items.
     */
    private float getStandardDeviation() {
        if (matches.size() <= 1) {
            return 0;
        }
        /**
         * The mean is dividing the sum by the number of distance comparisons the cluster requires,
         * excluding the comparisons to self. Hence a cluster with 4 items does 6 meaningful, non-0
         * comparisons [ size*(size-1)/2 ].
         */
        final float mean = getNormalisedDistanceSum();
        float variance = 0f;
        for (int i = 0; i < matches.size(); i++) {
            for (int j = i + 1; j < matches.size(); j++) {
                variance += Math.pow(Math.abs(mean - distanceMatrix.get(matches.get(i), matches.get(j))), 2);
            }
        }
        variance /= ((matches.size() * (matches.size() - 1)) / 2f);
        return (float) Math.sqrt(variance);
    }

    /**
     * Simple getter method for dispersityByDistanceSum.
     *
     * @return Returns the cluster dispersity using a distance sum algorithm.
     */
    public float getDispersityByDistanceSum() {
        return dispersityByDistanceSum;
    }

    /**
     * Simple getter method for dispersityByStandardDeviation.
     *
     * @return Returns the cluster dispersity using a standard deviation.
     */
    public float getDispersityByStandardDeviation() {
        return dispersityByStandardDeviation;
    }

    /**
     * Helper method for computing the sum of distances of a cluster's items. The function excludes
     * a comparison to self and normalises the sum. This is accomplished by dividing by the number
     * of meaningful comparisons:<br>
     * <code>size*(size-1)/2</code>
     *
     * @return The sum of distances of the cluster's items.
     */
    private float getNormalisedDistanceSum() {
        if (matches.size() <= 1) {
            return 0;
        }


        float distanceSum = 0f;
        for (int i = 0; i < matches.size(); i++) {
            for (int j = i + 1; j < matches.size(); j++) {
                distanceSum += distanceMatrix.get(matches.get(i), matches.get(j));
            }
        }
        return distanceSum / (((matches.size() * (matches.size() - 1)) / 2f));
    }

    /**
     * Method used to order items in the Cluster's item list by their distance to other items.
     *
     * @return A list of {@link Match} instances ordered by their distance to all other items in the
     *         {@link Cluster}.
     */
    public List<Match> getOrderedMatches() {
        final List<Match> orderedMatches = new ArrayList<>(matches);
        Collections.sort(orderedMatches, new DistanceComparator());
        return orderedMatches;
    }

    /**
     * Helper method computing the sum of distances for each row of the distance matrix.
     *
     * @return The {@link Map} of distances per {@link Match}.
     * @post rowDistanceSums.size() == distanceMatrix.rowMap().size()
     */
    private Map<Match, Float> getRowDistanceSums() {
        final Map<Match, Float> rowDistanceSums = new HashMap<>();

        for (int i = 0; i < matches.size(); i++) {
            float distance = 0f;
            for (int j = 0; j < matches.size(); j++) {
                distance += distanceMatrix.get(matches.get(i), matches.get(j));
            }

            rowDistanceSums.put(matches.get(i), distance - 1);
        }
        assert rowDistanceSums.size() == matches.size();
        return rowDistanceSums;
    }

    private class DistanceComparator implements Comparator<Match> {
        private final Map<Match, Float> rowDistanceSums;

        /** Constructor initialising all local fields. */
        public DistanceComparator() {
            rowDistanceSums = getRowDistanceSums();
        }

        /**
         * {@inheritDoc}<br>
         * <br>
         *
         * This implementation compares {@link Match} instances by their similarity to all other
         * {@link Match} instances in this {@link Cluster}. If the similarity is equal, the method
         * will return 0. If the first parameter features a greater similarity, the result is
         * positive, the result is negative if the similarity is smaller.
         */
        @Override
        public int compare(final @Nullable Match _this, final @Nullable Match that) {
            if (_this == that) {
                return 0;
            }

            return (int) Math.signum(rowDistanceSums.get(_this) - rowDistanceSums.get(that));
        }
    }
}