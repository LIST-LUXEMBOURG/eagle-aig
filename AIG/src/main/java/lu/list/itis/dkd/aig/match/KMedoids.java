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

import lu.list.itis.dkd.aig.resolution.ItemFactory;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implementing the PAM algorithm for generating clusters using medoids. This particular
 * implementation will only compute if the number of members in each cluster can be equal. This
 * precondition is enforced.
 *
 * @author Valentin [departed]
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.7.1
 */
public class KMedoids {
    private int clustersToBuild;
    private final int elementCount;
    private int[] medoids;
    private final float[][] distances;
    private float[] mindistances;
    private int[] clusterID;
    private int[] clusterSize;
    private final Random generator = new Random();
    private boolean[] changes;
    private boolean[] done;
    private boolean equal;
    private int idealCount;
    private Match[] map;
    private List<Cluster> clusters;

    protected static final Logger logger = Logger.getLogger(ItemFactory.class.getSimpleName());

    static {
        KMedoids.logger.setLevel(Level.parse(PropertiesFetcher.getProperties().getProperty(Externalization.LOGGER_LEVEL_PROEPRTY, Level.INFO.toString())));
    }

    /**
     * Method for retrieving a random index.
     *
     * @param length
     *        The total number of possible indexes.
     * @param exclude
     *        The indexes to exclude.
     * @return A random index knowing that only unique indexes are returned.
     */
    private int getRandomIndex(final int length, final Set<Integer> exclude) {
        while (true) {
            final int random = generator.nextInt(length);
            if (!exclude.contains(random)) {
                return random;
            }
        }
    }

    /**
     * Constructor initialising all fields.
     *
     * @param matrix
     *        The distance matrix to initialise the KMedoids with.
     */
    public KMedoids(final Table<Match, Match, Float> matrix) {
        elementCount = matrix.rowKeySet().size();
        map = new Match[elementCount];
        int i = 0;
        distances = new float[elementCount][elementCount];
        mindistances = new float[elementCount];
        map = matrix.rowKeySet().toArray(map);

        for (i = 0; i < elementCount; i++) {
            for (int j = 0; j < elementCount; j++) {
                distances[i][j] = 1 - matrix.get(map[i], map[j]);
            }
        }
        clusterID = new int[elementCount];
        Arrays.fill(clusterID, -1);
    }


    /**
     * Constructor initialising all fields.
     *
     * @param distances
     *        The distance matrix to initialise the KMedoids with.
     */
    public KMedoids(final float[][] distances) {
        elementCount = distances.length;
        this.distances = distances;
        mindistances = new float[elementCount];
        clusterID = new int[elementCount];
        Arrays.fill(clusterID, -1);
    }


    /**
     * Method for building clusters by a modified KMedoid algorithm. The modification consists in
     * limiting the number of items per cluster.
     *
     * @param clustersCount
     *        The number of clusters to build.
     * @param equi
     *        TODO Muriel? *
     * @pre elementCount % clustersToBuild == 0
     */
    public void buildClusters(final int clustersCount, final boolean equi) {
        Preconditions.checkState((elementCount % clustersCount) == 0, "Clusters cannot be build with an equal number of members!"); //$NON-NLS-1$
        clustersToBuild = clustersCount;
        clusterSize = new int[clustersToBuild];
        medoids = new int[clustersToBuild];
        clusters = new ArrayList<>(medoids.length);
        changes = new boolean[clustersToBuild];
        Arrays.fill(changes, true);
        equal = equi;
        done = new boolean[clustersToBuild];
        idealCount = elementCount / clustersToBuild;
        setRandomMedoids();
        int move = assignElements();
        float state = computeState();
        int iteration = 0;
        final int maxIterations = 1000;

        KMedoids.logger.log(Level.FINE, "Starting cluster generation using the modified KMedoid algorithm!"); //$NON-NLS-1$

        while ((move > 0) && (iteration++ < maxIterations)) {
            for (int i = 0; i < clustersToBuild; i++) {
                for (int j = 0; j < elementCount; j++) {
                    if (Float.compare(mindistances[j], 0f) == 0) {
                        continue;
                    }
                    final int origmed = medoids[i];
                    medoids[i] = j;
                    final int[] temp_membership = Arrays.copyOf(clusterID, clusterID.length);
                    final float[] temp_mindists = Arrays.copyOf(mindistances, mindistances.length);
                    final boolean[] temp_changes = Arrays.copyOf(changes, changes.length);
                    final boolean[] temp_done = Arrays.copyOf(done, done.length);
                    final int[] temp_sizes = Arrays.copyOf(clusterSize, clusterSize.length);
                    move = assignElements();
                    final float temp_state = computeState();
                    if (temp_state < state) {
                        state = temp_state;
                    } else {
                        clusterID = Arrays.copyOf(temp_membership, temp_membership.length);
                        mindistances = Arrays.copyOf(temp_mindists, temp_mindists.length);
                        changes = Arrays.copyOf(temp_changes, temp_changes.length);
                        done = Arrays.copyOf(temp_done, temp_done.length);
                        clusterSize = Arrays.copyOf(temp_sizes, temp_sizes.length);
                        medoids[i] = origmed;
                    }
                }
            }
        }
        KMedoids.logger.log(Level.FINE, "Finished cluster generation using the modified KMedoid algorithm!"); //$NON-NLS-1$
    }


    private float computeState() {
        float sum = 0f;
        for (final float score : mindistances) {
            sum += score;
        }
        return sum;
    }


    private int assignElements() {
        int move = 0;
        Arrays.fill(clusterSize, 0);
        for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
            final int nearestCentroid = nearestCentroid(elementIndex);
            if (nearestCentroid == -1) {
                continue;
            }
            if (clusterID[elementIndex] != nearestCentroid) {
                if (clusterID[elementIndex] != -1) {
                    changes[clusterID[elementIndex]] = true;
                }

                changes[nearestCentroid] = true;
                clusterID[elementIndex] = nearestCentroid;
                move++;
            }
            mindistances[elementIndex] = distances[elementIndex][medoids[clusterID[elementIndex]]];
            clusterSize[clusterID[elementIndex]]++;
            if (equal && (clusterSize[nearestCentroid] > idealCount)) {
                move += remakeAssignments(nearestCentroid);
            }
        }
        return move;
    }


    private int remakeAssignments(final int cc) {
        int move = 0;
        double md = Double.MAX_VALUE;
        int nc = -1;
        int np = -1;
        for (int p = 0; p < elementCount; p++) {
            if (clusterID[p] != cc) {
                continue;
            }
            for (int c = 0; c < clustersToBuild; c++) {
                if ((c == cc) || done[c]) {
                    continue;
                }
                final double d = distances[medoids[c]][p];
                if (d < md) {
                    md = d;
                    nc = c;
                    np = p;
                }
            }
        }
        if ((nc != -1) && (np != -1)) {
            if (clusterID[np] != nc) {
                if (clusterID[np] != -1) {
                    changes[clusterID[np]] = true;
                }
                changes[nc] = true;
                clusterID[np] = nc;
                mindistances[np] = distances[np][medoids[nc]];
                move++;
            }
            clusterSize[cc]--;
            clusterSize[nc]++;
            if (clusterSize[nc] > idealCount) {
                done[cc] = true;
                move += remakeAssignments(nc);
                done[cc] = false;
            }
        }
        return move;
    }

    /**
     * Method for finding the nearest centroid index. If two centroids are equally distance, the
     * first one to be found will be kept.
     *
     * @param elementIndex
     *        The index of the element to determine the nearest centroid for.
     * @return The nearest centroid by index in the clusterIndex array.
     */
    private int nearestCentroid(final int elementIndex) {
        float minimalDistance = Float.MAX_VALUE;
        int nearestCentroid = -1;
        for (int clusterIndex = 0; clusterIndex < clustersToBuild; clusterIndex++) {
            final float distance = distances[medoids[clusterIndex]][elementIndex];
            if (Float.compare(distance, minimalDistance) < 0) {
                minimalDistance = distance;
                nearestCentroid = clusterIndex;
            }
        }
        return nearestCentroid;
    }

    /**
     * Method for determining a set number of random elements as medoid indexes.
     */
    private void setRandomMedoids() {
        final Set<Integer> exclude = new HashSet<Integer>();
        for (int i = 0; i < clustersToBuild; i++) {
            final int element = getRandomIndex(elementCount, exclude);
            exclude.add(element);
            medoids[i] = element;
        }
    }

    /**
     * Simple getter method for medoids.
     *
     * @return The value of medoids.
     */
    public int[] getMedoids() {
        return medoids;
    }

    /**
     * Getter method for returning all clusters.
     *
     * @return A list containing all clusters computed by the KMedoids.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    public List<Cluster> getClusters() {
        if (!clusters.isEmpty()) {
            return clusters;
        }

        for (final int medoid : medoids) {
            clusters.add(new Cluster(map[medoid]));
        }

        for (int i = 0; i < clusterID.length; i++) {
            final Cluster cluster = clusters.get(clusterID[i]);
            cluster.addMatch(map[i]);
        }

        return clusters;
    }

    /**
     * Simple getter method for membership.
     *
     * @return The value of membership.
     */
    public int[] getMembership() {
        return clusterID;
    }
}