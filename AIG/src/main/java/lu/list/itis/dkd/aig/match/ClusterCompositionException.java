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

/**
 * Exception used to signal that an error occurred during the composition of one or more clusters.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.7.0
 */
public class ClusterCompositionException extends Exception {

    private static final long serialVersionUID = 6527797226412608534L;

    /**
     * @see Exception #Exception(String)
     */
    public ClusterCompositionException(final String message) {
        super(message);
    }

    /**
     * @see Exception#Exception(Throwable)
     */
    public ClusterCompositionException(final Throwable t) {
        super(t);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public ClusterCompositionException(final String message, final Throwable t) {
        super(message, t);
    }
}