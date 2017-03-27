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
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Class implementing options as used in AIG contexts. Options are composed of multiple variables,
 * one of which acts as the correct response while the others act as distractors.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.8.0
 */
@NonNullByDefault
public class Options implements Cloneable {
    private final List<Variable> correctResponses;
    private TreeSet<McqDistractor> distractors;
    private float disparity = 0f;

    /**
     * Constructor initialising all fields.
     *
     * @param correctResponses
     *        The correct response(s) of the option linked to the distractor(s).
     * @param distractors
     *        The {@link TreeSet} of distractors for the given correct response.
     */
    public Options(final List<Variable> correctResponses, final TreeSet<McqDistractor> distractors) {
        this.correctResponses = correctResponses;
        this.distractors = distractors;
        computeDisparity();
    }

    /**
     * Simple getter method for the correct response.
     *
     * @return The variable holding the correct response.
     */
    public List<Variable> getCorrectResponses() {
        return correctResponses;
    }

    /**
     * Simple getter method for distractors.
     *
     * @return The value of distractors.
     */
    public TreeSet<McqDistractor> getDistractors() {
        return distractors;
    }

    /**
     * Simple setter method for distractors.
     *
     * @param distractors
     *        The value to set distractors to.
     */
    public void setDistractors(final TreeSet<McqDistractor> distractors) {
        this.distractors = distractors;
        computeDisparity();
    }

    /**
     * Method used to trim the size of the distractors to a given number.
     *
     * @param size
     *        The size to trim the distractor collection to.
     * @return This instance for chain calling.
     */
    public Options trim(final int size) {
        while (distractors.size() > size) {
            distractors.remove(distractors.first());
        }
        computeDisparity();
        return this;
    }

    /**
     * Method for computing the disparity of all distractors contained in the set of distractors.
     */
    private void computeDisparity() {
        disparity = 0f;
        for (final McqDistractor distractor : distractors) {
            disparity += (1f - distractor.getSimilarityToCorrectResponse());
        }
        disparity /= distractors.size();
    }

    /**
     * Simple getter method for disparity. The disparity is a measure of how close the distractors
     * are to the correct response. The greater the disparity, the less similar, and thus probably
     * good, are the distractors for their correct response.
     *
     * @return The value of the disparity.
     */
    public float getDisparity() {
        return disparity;
    }

    /**
     * Method used to explode this {@link Options} instance into instances each containing only the
     * parameterised number of {@link McqDistractor} instances. Note that the call will return an
     * empty list should the parameter be superior to the number of total distractors held by this
     * instance and the lossless parameter is set to <code>false</code>.
     *
     * @param distractorSize
     *        The desired distractor list size.
     * @param lossless
     *        Indicates whether trailing distractors that cannot be used to generate a full
     *        {@link Options} instance as given by the distractorSize parameter will be discarded or
     *        whether an instance with an incomplete distractor set will be appended.
     * @return A {@link List} of {@link Options} instances with the same correctResponse as this
     *         instance and a reduced set of {@link McqDistractor} instances as stated by the given
     *         parameter. The number of returned {@link Options} instances depends on the number of
     *         available distractors and the lossless parameter. Should it be <code>false</code>,
     *         the method will only generate {@link Options} such that they are complete, hence, any
     *         remaining distractors that cannot be used to generate complete {@link Options} will
     *         be "lost".
     */
    public List<Options> explode(final int distractorSize, final boolean lossless) {
        final List<Options> options = new ArrayList<>();
        final TreeSet<McqDistractor> explodedDistractors = new TreeSet<>();

        while (distractors.size() >= distractorSize) {
            for (int i = 0; i < distractorSize; i++) {
                explodedDistractors.add(distractors.pollLast());
            }

            final Options explodedOption = new Options(correctResponses, explodedDistractors);
            options.add(explodedOption);
        }

        if (lossless && !distractors.isEmpty()) {
            options.add(new Options(correctResponses, distractors));
        }

        return options;
    }

    /**
     * Method used to get a variable held internally by this instance and removing it. Note that
     * this method should be used carefully as it changes the internal model held by this instance.
     * Removal in this way should only be necessary when values of the option instance are
     * attributed to placeholder variables. <b> Even then a copy should be used!</b>
     *
     * @param identifier
     *        The identifier to look for.
     * @return The variable with the parameterized identifier.
     * @see #clone()
     */
    public @Nullable Variable getAndRemoveVariable(final URI identifier) {
        for (final Variable correctResponse : correctResponses) {
            if (correctResponse.getIdentifier().equals(identifier)) {
                correctResponses.remove(correctResponse);
                return correctResponse;
            }
        }

        for (final McqDistractor distractor : distractors) {
            if (distractor.getVariable().getIdentifier().equals(identifier)) {
                distractors.remove(distractor);
                return distractor.getVariable();
            }
        }

        return null;
    }

    @Override
    public Options clone() {
        return new Options(new ArrayList<>(correctResponses), new TreeSet<>(distractors));
    }
}