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

import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Wrapper class to hold a list of {@link Cluster} instances as well as the inter-instance metrics
 * for easy marshalling using JAXB.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.8.0
 */
@NonNullByDefault
@XmlRootElement(name = "clusters")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ClusterList {
    @XmlElement(name = "cluster")
    private List<Cluster> clusters = new ArrayList<>();
    @XmlAttribute
    private float interClusterDispercity = 0f;

    /**
     * Constructor initialising the list of cluster instances as well as calculating the
     * inter-cluster metrics.
     *
     * @param clusters
     *        The {@link Cluster} instance this wrapper class should manage.
     */
    public ClusterList(final List<Cluster> clusters) {
        super();
        this.clusters = clusters;
        interClusterDispercity = computeMetrics();
    }

    /** Constructor for use with JAXB. For other purposes, use {@link #ClusterList(ArrayList)}. */
    public ClusterList() {}

    /**
     * Simple getter method for clusters.
     *
     * @return The value of clusters.
     */
    @SuppressWarnings("null")
    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * Simple getter method for interClusterDispercity.
     *
     * @return The value of interClusterDispercity.
     */
    public float getInterClusterDispercity() {
        return interClusterDispercity;
    }


    /**
     * Method used to compute inter-cluster metrics. For now the standard deviation of all cluster
     * dispersities is computed using the intra-cluster cluster dispersity (as standard deviation).
     *
     * @return
     */
    private float computeMetrics() {
        float mean = 0f;
        for (final Cluster cluster : clusters) {
            mean += cluster.getDispersityByStandardDeviation();
        }
        mean /= clusters.size();

        float variance = 0f;
        for (final Cluster cluster : clusters) {
            variance += Math.pow(Math.abs(cluster.getDispersityByStandardDeviation() - mean), 2);
        }

        return (float) Math.sqrt(variance /= clusters.size());
    }

    /**
     * Getter method returning the size of the wrapped cluster instance list.
     *
     * @return The count of elements in the wrapped list.
     */
    public int size() {
        return clusters.size();
    }
}