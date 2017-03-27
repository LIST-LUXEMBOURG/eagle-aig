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

import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class holding a match, a pair of {@link Variable} instances and the corresponding feedback.
 *
 * @author Muriel Foulonneau [muriel.foulonneau@list.lu]
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.2
 * @version 0.8.0
 */
@NonNullByDefault
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Match {
    @XmlElement
    private Variable keyVariable;
    @XmlElement
    private Variable answerVariable;
    @Nullable
    @XmlElement
    private Variable feedback;

    /**
     * Constructor initialising all fields.
     *
     * @param keyVariable
     *        The stem variable of the pair.
     * @param answerVariable
     *        The answer variable of the pair.
     * @param feedback
     *        The feedback attached to the variable pair.
     */
    public Match(final Variable keyVariable, final Variable answerVariable, @Nullable final Variable feedback) {
        this.keyVariable = keyVariable;
        this.answerVariable = answerVariable;
        this.feedback = feedback;
    }

    /**
     * No-argument constructor for use with JAXB. It is recommended to use the argumented
     * constructor instead as no setters are provided. Using this constructor will result in a
     * violation of some null checks.
     */
    public Match() {}


    /**
     * Simple getter method for keyVariable.
     *
     * @return The value of keyVariable.
     */
    @SuppressWarnings("null")
    public Variable getKeyVariable() {
        return keyVariable;
    }

    /**
     * Simple getter method for answerVariable.
     *
     * @return The value of answerVariable.
     */
    @SuppressWarnings("null")
    public Variable getAnswerVariable() {
        return answerVariable;
    }

    /**
     * Simple getter method for feedback.
     *
     * @return The value of feedback.
     */
    public @Nullable Variable getFeedback() {
        return feedback;
    }

    /**
     * {@inheritDoc} <br>
     * <br>
     * This implementation will concatenate the hash codes of stem and answer variables and compute
     * the hash of the resulting string.
     *
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return (Integer.toString(keyVariable.hashCode()) + Integer.toString(answerVariable.hashCode())).hashCode();
    }

    /**
     * {@inheritDoc} <br>
     * This implementation of {@link Object#equals(Object)} will return <code>true</code> iff this
     * {@link Match} instance's stem {@link Variable} and answer {@link Variable} are equal and
     * false in all other cases.
     *
     * @see #hashCode()
     */
    @Override
    public boolean equals(@Nullable final Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }

        if (that instanceof Match) {
            return keyVariable.equals(((Match) that).getKeyVariable()) && answerVariable.equals(((Match) that).getAnswerVariable());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return keyVariable.getLabel() + " -> " + answerVariable.getLabel(); //$NON-NLS-1$
    }
}