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

import com.google.common.base.Strings;

/**
 * Class holding all pieces of information relevant to a distractor.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.8.0
 */
@NonNullByDefault
public class McqDistractor implements Comparable<McqDistractor> {
    private Variable variable;
    private float similarityToCorrectResponse;


    /**
     * Constructor initialising all fields.
     *
     * @param variable
     *        The {@link Variable} associated to this {@link McqDistractor} instance.
     */
    public McqDistractor(final Variable variable) {
        this.variable = variable;
        similarityToCorrectResponse = 0f;
    }

    /**
     * Constructor initialising all fields.
     *
     * @param variable
     *        The {@link Variable} associated to this {@link McqDistractor} instance.
     * @param similarity
     *        The similarity quotient of this {@link McqDistractor} to other {@link McqDistractor}
     *        instances in the same context. Set by external mechanisms.
     */
    public McqDistractor(final Variable variable, final float similarity) {
        this.variable = variable;
        similarityToCorrectResponse = similarity;
    }

    /**
     * Constructor initialising the similarityToCorrectResponse value to 0f.
     */
    public McqDistractor() {
        similarityToCorrectResponse = 0f;
    }


    /**
     * Simple getter method for variable.
     *
     * @return The value of variable.
     */
    public Variable getVariable() {
        return variable;
    }

    /**
     * Simple getter method for the similarity to the correct response.
     *
     * @return The value of similarityToCorrectResponse.
     */
    public float getSimilarityToCorrectResponse() {
        return similarityToCorrectResponse;
    }

    /**
     * Simple setter method for the similarity of the distractor to the correct response.
     *
     * @param similarity
     *        The value to set similarityToCorrectResponse to.
     */
    public void setSimilarityToCorrectResponse(final float similarity) {
        similarityToCorrectResponse = similarity;
    }

    /**
     * {@inheritDoc} <br>
     * This implementation of {@link Object#equals(Object)} will return <code>true</code> iff the
     * label (name) of this {@link McqDistractor} in lower case is equal to the label of the
     * parameterised {@link McqDistractor} and false in all other cases.
     */
    @Override
    public boolean equals(@Nullable final Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }

        if (that instanceof McqDistractor) {
            return (variable.equals(((McqDistractor) that).variable)) && (Float.compare(similarityToCorrectResponse, ((McqDistractor) that).getSimilarityToCorrectResponse()) == 0);
        }
        return false;
    }

    /**
     * {@inheritDoc} <br>
     * This implementation of {@link Object#hashCode()} will return the hash code of the lower case
     * string that is the label of this {@link McqDistractor} instance.
     */
    @Override
    public int hashCode() {
        return variable.hashCode();
    }

    /**
     * Method comparing {@link McqDistractor} instances using {@link Float} comparison. The method
     * will always return 0 iff the labels are equal regardless of similarityToCorrectResponse
     * value.
     *
     * @see Float#compare(float, float)
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(@Nullable final McqDistractor that) {
        if (that == null) {
            throw new NullPointerException("Cannot compare to null!"); //$NON-NLS-1$
        }
        if (variable.compareTo(that.variable) == 0) {
            return 0;
        }

        final int result = Float.compare(similarityToCorrectResponse, that.similarityToCorrectResponse);
        if (result == 0) {
            return variable.compareTo(that.variable);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable String toString() {
        return Strings.nullToEmpty(variable.getTextContent());
    }
}