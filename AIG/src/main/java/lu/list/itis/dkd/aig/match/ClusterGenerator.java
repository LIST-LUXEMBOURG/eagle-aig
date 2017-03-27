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
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class used to find stem-answer pairs respectively generate clusters of pairs.
 *
 * @author Muriel Foulonneau [muriel.foulonneau@list.lu]
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.8.0
 */
@NonNullByDefault
public class ClusterGenerator {
    private static final Logger logger = Logger.getLogger(ClusterGenerator.class.getSimpleName());
    @Nullable
    private String dataSource;

    /** An enumeration specifying different operation modes for the cluster generator. */
    public enum OperationMode {
        /** Mode for building random clusters. */
        RANDOM,
        /** Mode for building clusters based on the k-medoid algorithm. */
        KMEDOID,
        /** Mode for generating clusters based on the bisecting k-means algorithm. */
        BISECTING_KMEANS;
    }

    private final OperationMode mode;

    /**
     * Constructor initialising the generator to use similarity measures to build the clusters. The
     * similarity calculations are based on a weighted sum of all enabled similarity criteria as
     * determined by a properties file.
     *
     * @param datasource
     *        The data source to get the pairs from and compute similarity against.
     * @param mode
     *        The {@link OperationMode} specifying the algorithm to use to generate clusters.
     */
    public ClusterGenerator(@Nullable final String datasource, final OperationMode mode) {
        dataSource = datasource;
        this.mode = mode;
    }

    /**
     * Constructor initialising the generator to use similarity measures to build the clusters. The
     * similarity calculations are based on a weighted sum of all enabled similarity criteria as
     * determined by a properties file.
     *
     * @param datasource
     *        The data source to get the pairs from and compute similarity against.
     * @param mode
     *        The {@link OperationMode} specifying the algorithm to use to generate clusters given
     *        as a {@link String}. Note that providing a {@link String} which is not a
     *        {@link OperationMode} will throw an {@link IllegalArgumentException}.
     */
    public ClusterGenerator(@Nullable final String datasource, final String mode) {
        dataSource = datasource;
        this.mode = OperationMode.valueOf(mode);
    }

    /**
     * Constructor initialising the mode by which clusters are to be build. This constructor will
     * result in random cluster generation based on the randomness associated with
     * {@link Collections#shuffle(List)}.
     *
     * @see Collections#shuffle(List)
     */
    public ClusterGenerator() {
        mode = OperationMode.RANDOM;
    }

    /**
     * Method used to compute clusters based on the mode the generator was initialised in. The
     * method will generate clusters of a given size out of the provided matches that may or may not
     * be trimmed down to a provided limit to prevent exhaustive cluster generation. Should the
     * limit or the number of provided matches not result in a division with rest of the matches
     * given the cluster size, the last cluster will be padded with random matches, respectively the
     * most similar matches not already in said last cluster.
     *
     * @param query
     *        The query used to retrieve the pairs (and feedback) from the data source.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @param searchLimit
     *        The number to which the provided matches should be trimmed should exhaustive cluster
     *        generation not be wanted. The parameter may be left out. Only the first limit
     *        parameter will be considered.
     * @pre clusterSize >= 1
     * @pre clusterSize <= searchLimit[0] && clusterSize <= matches.size()
     * @return A {@link List} of {@link Cluster} instances built from the provided {@link Match}
     *         instances of given length.
     * @throws ClusterCompositionException
     *         Thrown when no clusters with equal size could be built.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    public ClusterList computeClusters(final String query, final int clusterSize, final int... searchLimit) throws ClusterCompositionException {
        return computeClusters(findAllUniquePairs(query), clusterSize, searchLimit);
    }

    /**
     * Method used to compute clusters based on the mode the generator was initialised in. The
     * method will generate clusters of a given size out of the provided matches that may or may not
     * be trimmed down to a provided limit to prevent exhaustive cluster generation. Should the
     * limit or the number of provided matches not result in a division with rest of the matches
     * given the cluster size, the last cluster will be padded with random matches, respectively the
     * most similar matches not already in said last cluster.
     *
     * @param matches
     *        The {@link Match} instances used to establish clusters of matches.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @param searchLimit
     *        The number to which the provided matches should be trimmed should exhaustive cluster
     *        generation not be wanted. The parameter may be left out. Only the first limit
     *        parameter will be considered.
     * @pre clusterSize >= 1
     * @pre clusterSize <= searchLimit[0] && clusterSize <= matches.size()
     * @return A {@link List} of {@link Cluster} instances built from the provided {@link Match}
     *         instances of given length.
     * @throws ClusterCompositionException
     *         Thrown when no clusters with equal size could be built.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    public ClusterList computeClusters(final List<Match> matches, final int clusterSize, @Nullable int... searchLimit) throws ClusterCompositionException {
        /**
         * If the searchLimit is not null and either has no elements or only one elements which is 0
         * then set its first element to the match size, deleting all other elements.
         */
        if ((searchLimit != null) && ((searchLimit.length == 0) || ((searchLimit.length == 1) && (searchLimit[0] == 0)))) {
            searchLimit = new int[] {matches.size()};
        }
        Preconditions.checkArgument(clusterSize <= searchLimit[0], "The search limit must allow for at least one cluster to be generated!"); //$NON-NLS-1$
        Preconditions.checkArgument(clusterSize <= matches.size(), "The provided matches must allow for at least one cluster to be generated!"); //$NON-NLS-1$
        Preconditions.checkArgument(clusterSize > 0, "The cluster size must be greater than 0!"); //$NON-NLS-1$

        if ((searchLimit.length == 0) || (searchLimit[0] > matches.size())) {
            searchLimit = new int[] {matches.size()};
        }

        switch (mode) {
            case RANDOM:
                return getClustersFromList(matches, clusterSize, ((searchLimit != null) && (searchLimit.length > 0)) ? searchLimit[0] : matches.size());
            case KMEDOID:
                return getClustersUsingKMedoid(((searchLimit != null) && (searchLimit.length > 0)) ? matches.subList(0, searchLimit[0]) : matches, clusterSize);
            case BISECTING_KMEANS:
                return getClustersUsingBisectingKMeans(((searchLimit != null) && (searchLimit.length > 0) && (matches.size() > searchLimit[0])) ? matches.subList(0, searchLimit[0]) : matches, clusterSize);
            default:
                ClusterGenerator.logger.log(Level.SEVERE, "The cluster generator could not handle the provided operational mode (" + mode + ")!"); //$NON-NLS-1$ //$NON-NLS-2$
                return new ClusterList();
        }
    }

    /**
     * Method for computing clusters based on a modified bisecting k-means algorithm. The
     * modifications consist in filling cluster to a certain cluster size once the bisection would
     * have their size decrease to much.
     *
     * @param matches
     *        The {@link Match} instances used to establish clusters of matches.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @return A {@link List} of {@link Cluster} instances built from the provided {@link Match}
     *         instances of given length.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     * @pre clusterSize <= searchLimit[0] && clusterSize <= matches.size()
     */
    private ClusterList getClustersUsingBisectingKMeans(final List<Match> matches, final int clusterSize)  {
        List<Cluster> clusters = new ArrayList<>();
        clusters.add(new Cluster(matches));
        boolean bisecting = true;
        final Table<Match, Match, Float> distanceMatrix = computeDistances(matches);

        do {
            final List<Cluster> nextGeneration = new ArrayList<>();

            for (final Cluster cluster : clusters) {
                if (cluster.size() >= (2 * clusterSize)) {
                    nextGeneration.addAll(splitCluster(cluster, clusterSize, distanceMatrix));
                } else {
                    nextGeneration.add(cluster);
                }
            }

            if (nextGeneration.size() == clusters.size()) {
                bisecting = false;
            }
            clusters = nextGeneration;
        } while (bisecting);

        return new ClusterList(splitToSize(clusters, clusterSize, distanceMatrix));
    }

    /**
     * Method used to split clusters of size < 2*clusterSize into two clusters including some
     * repetition. The splitting algorithm is similar to the
     * {@link #splitCluster(Cluster, int, Table)} algorithm.
     *
     * @param clusters
     *        A {@link List} of clusters to split according to the size given by one of the
     *        parameters.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @param distanceMatrix
     *        The distance matrix holding all distances of {@link Match} instances in the cluster.
     * @return A {@link List} holding all {@link Cluster} instances.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     * @pre for(cluster : clusters) cluster.size() < 2 * clusterSize
     * @pre for(cluster : clusters) cluster.size() >= clusterSize
     */
    private List<Cluster> splitToSize(final List<Cluster> clusters, final int clusterSize, final Table<Match, Match, Float> distanceMatrix)  {
        final List<Cluster> homogenisedClusters = new ArrayList<>();
        for (final Cluster cluster : clusters) {
            Preconditions.checkState(cluster.size() >= clusterSize, "The cluster cannot be of size smaller than " + clusterSize + "!"); //$NON-NLS-1$ //$NON-NLS-2$
            if (cluster.size() == clusterSize) {
                homogenisedClusters.add(cluster);
                continue;
            }

            final List<Match> matches = cluster.getOrderedMatches();
            Collections.reverse(matches);
            final List<Match> clones = new ArrayList<>();
            final int difference = matches.size() - clusterSize;

            for (int index = 0; index < difference; index++) {
                clones.add(matches.get(index));
                matches.remove(index);
            }
            Collections.reverse(matches);
            for (int index = 0; clones.size() != clusterSize; index++) {
                clones.add(matches.get(index));
            }

            homogenisedClusters.add(new Cluster(matches));
            homogenisedClusters.add(new Cluster(clones));
        }

        for (final Cluster cluster : homogenisedClusters) {
            assert cluster.size() == clusterSize;
        }
        return homogenisedClusters;
    }

    /**
     * Method used for splitting clusters. The initial cluster will be split into two separate
     * clusters with n, respectively n+1 items where n is the half the size of the initial cluster
     * rounded down. To split the clusters, the bisecting k-means algorithm will be used.
     *
     * @param cluster
     *        The cluster to split.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @param distanceMatrix
     *        The distance matrix holding all distances of {@link Match} instances in the cluster.
     * @return A collection holding two {@link Cluster} instances.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    private List<Cluster> splitCluster(final Cluster cluster, final int clusterSize, final Table<Match, Match, Float> distanceMatrix)  {
        return clusterMatches(cluster.getOrderedMatches(), clusterSize, distanceMatrix);
    }

    /**
     * Method for splitting a list of ordered matches into two lists depending on the matches'
     * distance given by a distance matrix.
     *
     * @param orderedMatches
     *        A list of ordered matches from highest to lowest in regard to distance.
     * @param clusterSize
     *        The minimum size of the outputted clusters.
     * @param distanceMatrix
     *        The distance matrix holding all distances of {@link Match} instances in the cluster.
     * @return A {@link List} holding two {@link Cluster} instances.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     * @pre orderedMatches.size() >= 2 * clusterSize
     * @post for(cluster : result) cluster.size() >= clusterSize
     * @post result.size() == 2
     */
    private List<Cluster> clusterMatches(final List<Match> orderedMatches, final int clusterSize, final Table<Match, Match, Float> distanceMatrix)  {
        final List<Cluster> clusters = new ArrayList<>();
        final List<Match> alphaList = new ArrayList<>();
        final List<Match> betaList = new ArrayList<>();
        final Match alpha = orderedMatches.get(orderedMatches.size() - 1);
        final Match beta = orderedMatches.get(orderedMatches.size() - 2);
        alphaList.add(alpha);
        betaList.add(beta);

        for (final Match match : orderedMatches) {
            if (match.equals(alpha) || match.equals(beta)) {
                continue;
            }

            if (Float.compare(distanceMatrix.get(match, alpha), distanceMatrix.get(match, beta)) == 0) {
                final Random random = new Random();
                if (random.nextInt(2) == 0) {
                    alphaList.add(match);
                } else {
                    betaList.add(match);
                }
                continue;
            }

            if (Float.compare(distanceMatrix.get(match, alpha), distanceMatrix.get(match, beta)) > 0) {
                alphaList.add(match);
            } else {
                betaList.add(match);
            }
        }

        if ((alphaList.size() < clusterSize) || (betaList.size() < clusterSize)) {
            clusters.addAll(equaliseFutureClusters(alphaList, betaList, clusterSize, distanceMatrix));
        } else {
            clusters.add(new Cluster(alphaList));
            clusters.add(new Cluster(betaList));
        }

        for (final Cluster cluster : clusters) {
            assert cluster.size() >= clusterSize;
        }
        assert clusters.size() == 2;
        return clusters;
    }

    /**
     * Method to equalise the members of two lists in regard to size. The method will remove
     * elements from the bigger list and add them to the smaller list until both lists are of equal
     * size.
     *
     * @param alphaList
     *        The first list of {@link Match} instances that was the result of a preliminary
     *        clustering attempt.
     * @param betaList
     *        The second list akin to the first list.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @param distanceMatrix
     *        The distance matrix holding all distances of {@link Match} instances in the cluster.
     * @return
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     * @pre alphaList.size() + betaList.size() >= clusterSize
     * @pre alphaList.size() != clusterSize || betaList.size() != clusterSize
     */
    private Collection<Cluster> equaliseFutureClusters(final List<Match> alphaList, final List<Match> betaList, final int clusterSize, final Table<Match, Match, Float> distanceMatrix)  {
        Preconditions.checkState((alphaList.size() + betaList.size()) >= clusterSize, "Both clusters' size summed must be at least equal to " + clusterSize + "!"); //$NON-NLS-1$ //$NON-NLS-2$
        final List<Cluster> clusters = new ArrayList<>();

        if (alphaList.size() > betaList.size()) {
            equaliseLists(alphaList, betaList, clusterSize, distanceMatrix);
        } else {
            equaliseLists(betaList, alphaList, clusterSize, distanceMatrix);
        }

        clusters.add(new Cluster(alphaList));
        clusters.add(new Cluster(betaList));
        return clusters;
    }

    /**
     * The method will remove elements from bigList and add them to smallList. The removal will
     * first remove those elements which are furthest removed from all other elements in the the
     * list by using {@link Cluster#getOrderedMatches()}. <br>
     * <br>
     * Should the bigList not contain enough elements to have smallList grow to the clusterSize
     * before being reduced to less than the desired size itself, the method will start copying
     * elements using the same strategy as noted before to smallList.
     *
     * @param bigList
     *        The largest list of the two from which to cut, respectively copy elements to the
     *        smaller list.
     * @param smallList
     *        The smaller list to grow up to clusterSize using elements from bigList.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @param distanceMatrix
     *        The distance matrix holding all distances of {@link Match} instances in the cluster.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     * @post bigList.size() == smallList.size()
     */
    private void equaliseLists(final List<Match> bigList, final List<Match> smallList, final int clusterSize, final Table<Match, Match, Float> distanceMatrix)  {
        List<Match> orderedMatches = new Cluster(bigList).getOrderedMatches();
        for (int index = 0; (smallList.size() < clusterSize) && (bigList.size() > clusterSize); index++) {
            /**
             * OPTIMISATION
             *
             * Note for future optimisation. This method can be rather expensive with large cluster
             * sizes. It should be seen if randomly picking elements to remove is not a better
             * option. Incidentally, maybe an exhaustive cluster comparison in the dimension of
             * disparity produces better results in regard to comparability. This has to be seen.
             */
            bigList.remove(orderedMatches.get(index));
            smallList.add(orderedMatches.get(index));
        }

        /**
         * Compensate for uneven number of matches resulting in bigList still being bigger than
         * smallList. This will only happen if bigList hits the minimal clusterSize before smallList
         * has enough match elements.
         */
        if (bigList.size() > smallList.size()) {
            orderedMatches = new Cluster(bigList).getOrderedMatches();
            for (int index = 0; smallList.size() < clusterSize; index++) {
                smallList.add(orderedMatches.get(index));
            }
        }

        assert (bigList.size() == smallList.size()) && (bigList.size() == clusterSize) && (smallList.size() == clusterSize);
    }

    /**
     * Method for computing clusters based on a modified k-medoid algorithm. The method will use a
     * loop variant to iterate n times or until produced clusters are of equal size. Note that
     * should the loop terminate without cluster size equality, an exception will be thrown.
     *
     * @param matches
     *        The {@link Match} instances used to establish clusters of matches.
     * @param clusterSize
     *        The size of the outputted clusters.
     * @return A {@link List} of {@link Cluster} instances built from the provided {@link Match}
     *         instances of given length.
     * @throws ClusterCompositionException
     *         Thrown when no clusters with equal size could be built.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     * @pre clusterSize <= searchLimit[0] && clusterSize <= matches.size()
     * @pre elementCount % clusterSize == 0
     */
    private ClusterList getClustersUsingKMedoid(final List<Match> matches, final int clusterSize) throws ClusterCompositionException {
        final int loopVariant = Integer.parseInt(PropertiesFetcher.getProperties().getProperty(Externalization.CLUSETER_LOOP_VARIANT_PROPERTY, Externalization.TEN_INTEGER));

        final ArrayTable<Match, Match, Float> distanceMatrix = computeDistances(matches);

        for (int i = 0; i < loopVariant; i++) {
            final KMedoids medoids = new KMedoids(distanceMatrix);
            medoids.buildClusters(clusterSize, true);
            boolean equalSize = true;

            for (final Cluster cluster : medoids.getClusters()) {
                if (cluster.size() != clusterSize) {
                    equalSize = false;
                    break;
                }
            }

            if (equalSize) {
                return new ClusterList(medoids.getClusters());
            }
        }

        throw new ClusterCompositionException("The K-Medoid algorithm was unable to procude clusters of equal size after " + loopVariant + " iterations!"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Method for generating clusters from a provided list by merely forming sub lists of a specific
     * size. The method will truncate the list of pairs to only take into account the given number
     * of pairs. To meet the criterion for cluster size, the method might append to the last cluster
     * the first matches of the list to meet the requirement.
     *
     * @param matches
     *        The matches to compute random clusters from.
     * @param clusterSize
     *        The size of each cluster.
     * @param searchLimit
     *        The maximum amount of matches to take into account.
     * @return The list of generated clusters.
     * @throws SimilarityComputationException
     *         thrown when one or more of the cluster's elements required a semantic similarity
     *         computation (in order to sort the elements) which failed.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    private ClusterList getClustersFromList(final List<Match> matches, final int clusterSize, int searchLimit)  {
        if (searchLimit > matches.size()) {
            searchLimit = matches.size();
        }
        Collections.shuffle(matches);
        final ArrayList<Cluster> clusters = new ArrayList<>();

        for (int i = 0; i < (searchLimit / clusterSize); i++) {
            clusters.add(new Cluster(matches.subList(i * clusterSize, (i * clusterSize) + clusterSize)));
        }

        if ((searchLimit % clusterSize) != 0) {
            final int tail = searchLimit % clusterSize;
            final List<Match> matchesTail = matches.subList(searchLimit - tail, searchLimit);
            for (int i = 0; matchesTail.size() < clusterSize; i++) {
                matchesTail.add(matches.get(i));
            }
            clusters.add(new Cluster(matchesTail));
        }
        return new ClusterList(clusters);
    }

    /**
     * Method for retrieving all stem-answer pairs (with optional feedback) from the data source
     * given the parameterised query. The source query must include the stem and answer pairs with
     * their human readable and answer forms. The query may include feedback in those forms as well.
     * <br>
     * The method returns only unique pairs using a set conversion before the result is returned.
     *
     * @param query
     *        The query used to retrieve the pairs (and feedback) from the data source.
     * @return A {@link List} of {@link Match} instances corresponding to the result of the query on
     *         the data source.
     */
    @Deprecated
    public List<Match> findAllUniquePairs(final String query) {
        final Query pairQuery = QueryFactory.create(query);
        final QueryExecution queryExecution = QueryExecutionFactory.sparqlService(dataSource, pairQuery);
        final HashSet<Match> pairs = new HashSet<>();
        final ResultSet result = queryExecution.execSelect();
        while (result.hasNext()) {
            final QuerySolution solution = result.nextSolution();
            // final Match match = extractMatch(solution);
            final Match match = null;
            if (match != null) {
                pairs.add(match);
            }
        }
        queryExecution.close();
        return new ArrayList<Match>(pairs);
    }

    /**
     * Method for extracting a match from a solution.
     *
     * @param solution
     *        The solution to extract variables from and form the Match.
     * @return A {@link Match} represented by {@link CompositeVariable} instances extracted from the
     *         provided solution. Will return <code>null</code> if one or more of the {@link URI}
     *         for the variables could not be build.
     */
    @Deprecated
    private @Nullable Match extractMatch(final QuerySolution solution) {
        String stemLabel, answerLabel, feedbackLabel;
        String stemLanguage, answerLanguage, feedbackLanguage;
        String uri = null;
        URI stemURI, answerURI, feedbackURI;
        final Properties properties = PropertiesFetcher.getProperties();
        try {
            stemLabel = solution.get(properties.getProperty("stem.hr", "stem_HR")).toString().replace("&", "&amp;").split("@")[0]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            stemLanguage = solution.get(properties.getProperty("stem.hr", "stem_HR")).toString().replace("&", "&amp;").split("@")[1]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            uri = solution.get(properties.getProperty("stem.uri", "stem_URI")).toString(); //$NON-NLS-1$ //$NON-NLS-2$
            stemURI = uri != null ? new URI(uri) : null;

            answerLabel = solution.get(properties.getProperty("answer.hr", "answer_HR")).toString().replace("&", "&amp;").split("@")[0]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            answerLanguage = solution.get(properties.getProperty("answer.hr", "answer_HR")).toString().replace("&", "&amp;").split("@")[1]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-4$ //$NON-NLS-5$
            uri = solution.get(properties.getProperty("answer.uri", "answer_URI")).toString(); //$NON-NLS-1$ //$NON-NLS-2$
            answerURI = uri != null ? new URI(uri) : null;

            if (solution.get(properties.getProperty("feedback.hr", "feedback_HR")) != null) { //$NON-NLS-1$ //$NON-NLS-2$
                feedbackLabel = solution.get(properties.getProperty("feedback.hr", "feedback_HR")).toString().replace("&", "&amp;").split("@")[0]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                feedbackLanguage = solution.get(properties.getProperty("feedback.hr", "feedback_HR")).toString().replace("&", "&amp;").split("@")[1]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                uri = solution.get(properties.getProperty("feedback.uri", "feedback_URI")).toString(); //$NON-NLS-1$ //$NON-NLS-2$
                feedbackURI = uri != null ? new URI(uri) : null;
            } else {
                feedbackLabel = ""; //$NON-NLS-1$
                feedbackLanguage = ""; //$NON-NLS-1$
                feedbackURI = null;
            }
        } catch (final URISyntaxException e) {
            ClusterGenerator.logger.log(Level.WARNING, "One or more of the URI were not valid!", e); //$NON-NLS-1$
            return null;
        }
        return null; // new Match(new CompositeVariable(stemLabel, stemLanguage, stemURI), new
                     // CompositeVariable(answerLabel, answerLanguage, answerURI), feedbackURI !=
                     // null ? new CompositeVariable(feedbackLabel, feedbackLanguage, feedbackURI) :
                     // null);
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
    public ArrayTable<Match, Match, Float> computeDistances(final List<Match> matches)  {
        final ArrayTable<Match, Match, Float> distanceMatrix = ArrayTable.create(matches, matches);
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
                final float similarity = SimilarityProvider.getInstance().compare(match.getAnswerVariable(), that.getAnswerVariable());
                distanceMatrix.put(match, that, similarity);
                distanceMatrix.put(that, match, similarity);
            }
        }
        return distanceMatrix;
    }
}