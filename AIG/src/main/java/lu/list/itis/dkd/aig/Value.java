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
package lu.list.itis.dkd.aig;

import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import org.apache.jena.ext.com.google.common.base.Preconditions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to bind an identifier, a Type, and a concrete object representing the actual value.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.8
 * @version 0.8.0
 */
@NonNullByDefault
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Value implements Comparable<Value> {

    private final URI identifier;
    private final ValueType type;
    @Nullable
    private Object value;

    private @Nullable Set<Object> domain = new HashSet<>();


    /**
     * Constructor initializing all fields,
     *
     * @param identifier
     *        The identifier to give to the value.
     * @param type
     *        The type of the value.
     * @param value
     *        The actual value.
     */
    public Value(final URI identifier, final ValueType type, final @Nullable Object value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    /**
     * Constructor initializing all fields,
     *
     * @param identifier
     *        The identifier to give to the value.
     * @param type
     *        The type of the value.
     */
    public Value(final URI identifier, final ValueType type) {
        this.identifier = identifier;
        this.type = type;
        value = null;
    }

    /**
     * Simple getter method for identifier.
     *
     * @return The value of identifier.
     */
    @SuppressWarnings("null")
    public URI getIdentifier() {
        return identifier;
    }

    /**
     * Simple getter method for type.
     *
     * @return The value of type.
     */
    @SuppressWarnings("null")
    public ValueType getType() {
        return type;
    }

    /**
     * Simple getter method for value.
     *
     * @return The value of value.
     */
    public @Nullable Object getValue() {
        return value;
    }

    /**
     * Method used for checking whether a provided value is valid according to the Value's domain.
     * The method will always return <code>true</code> if the set of possible values is
     * <code>null</code> or empty.
     *
     * @param value
     *        The value to check validity for. The concrete type of the value depends on the
     *        concrete implementation.
     *
     * @return <code>true</code> if the set of possible values is empty or if the provided value is
     *         contained in the set of possible values. Note that the check will use
     *         {@link Set#contains(Object)}.
     */
    @SuppressWarnings("null")
    public boolean isValid(final Object value) {
        if (domain.isEmpty()) {
            switch (type) {
                case MEDIA:
                    // Media accepts and object.
                    // @FALL-THROUGH
                case TEXT:
                    /**
                     * No check is made on textual content as any object can be converted to text.
                     * The correctness of the content is left for the user to check.
                     */
                    return true;
                case NUMBER:
                    if (value instanceof Double) {
                        return true;
                    }
                    return false;
                case URI:
                    if (value instanceof URI) {
                        return true;
                    }
                    // If it is not already a URI, see if we can make one out of it.
                    try {
                        new URI(value.toString());
                    } catch (final URISyntaxException e) {
                        return false;
                    }
                    return true;
                case CODE:
                    /**
                     * Values of type code are generated once and not settable. Hence, no value is
                     * ever valid.
                     */
                    return false;
                default:
                    throw new IllegalStateException("A switch statement reached a default that is, according to the design, never reachable!"); //$NON-NLS-1$
            }
        }

        if (domain.contains(value)) {
            return true;
        }
        return false;
    }

    /**
     * Method used to add a value that can be legally set for this instance.
     *
     * @param value
     *        The value to add to the domain of concrete values this instance may take.
     */
    @SuppressWarnings("null")
    public void enlargeDomain(final Object value) {
        domain.add(value);
    }

    /**
     * Method used to remove a value from the domain of values that can be legally set for this
     * instance.
     *
     * @param value
     *        The value to remove from the domain of concrete values this instance may take.
     */
    @SuppressWarnings("null")
    public void restrictDomain(final Object value) {
        domain.remove(value);
    }

    /**
     * Method used to make a copy of this instance.
     *
     * @param cloneValue
     *        Whether the clone should include the value. If <code>true</code>, the value is set to
     *        reference this instance's value.
     * @return A clone of this instance.
     */
    @SuppressWarnings("null")
    public Value clone(final boolean cloneValue) {
        final Value clone = new Value(identifier, type);
        clone.domain = domain;

        if (cloneValue) {
            clone.value = value;
        }
        return clone;
    }

    /**
     * Method used to set the inner value contained by the {@link Value} to the given parameter. The
     * method will first make a validity check using {@link #isValid(Object)}.
     *
     * @param concreteValue
     *        The concrete value to set.
     * @return <code>true</code> if the value was accepted, <code>false</code> otherwise.
     * @see #isValid(Object)
     */
    public boolean setInnerValueTo(final Object concreteValue) {
        if (isValid(concreteValue)) {
            setInnerValue(concreteValue);
            return true;
        }
        return false;
    }

    private void setInnerValue(final Object concreteValue) {
        switch (type) {
            case MEDIA:
                // $FALL-THROUGH$
            case TEXT:
                // $FALL-THROUGH$
            case NUMBER:
                value = concreteValue;
                return;
            case URI:
                if (value instanceof URI) {
                    value = concreteValue;
                    return;
                }
                try {
                    value = new URI(concreteValue.toString());
                    return;
                } catch (final URISyntaxException e) {
                    // As the test succeeded before, it should not fail now!
                    throw new Error("Logical fallacy, this should never happen!", e); //$NON-NLS-1$
                }
            default:
                throw new IllegalStateException("A switch statement reached a default that is, according to the design, never reachable!"); //$NON-NLS-1$
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public int compareTo(@Nullable final Value that) {
        if ((that == null) || (value == null)) {
            throw new NullPointerException("Cannot compare to null!"); //$NON-NLS-1$
        }

        if (!type.equals(that.type)) {
            throw new IllegalArgumentException("Only values of the same type are comparable!"); //$NON-NLS-1$
        }

        switch (type) {
            case MEDIA:
                return value.equals(that.value) ? 0 : value.toString().compareToIgnoreCase(that.value.toString());
            case URI:
                // $FALL-THROUGH$
            case CODE:
                // $FALL-THROUGH$
            case TEXT:
                return value.toString().compareToIgnoreCase(that.value.toString());
            case NUMBER:
                return Double.compare(new Double(value.toString()), new Double(that.value.toString()));

            default:
                throw new IllegalStateException("A switch statement reached a default that is, according to the design, never reachable!"); //$NON-NLS-1$
        }
    }

    @SuppressWarnings("null")
    @Override
    public String toString() {
        return Objects.toString(value);
    }

    /**
     * Method for assimilating the domain and value associated with the parameterized value.
     *
     * @param that
     *        A value to copy the domain and value from. The currently stored domain and value will
     *        be overwritten. The identifier and type will remain. Note that the type of the
     *        parameterized value is required to be equal to the value of this instance.<br>
     *        <br>
     *        If <code>null</code> is passed as a parameter, the assimilation is without consequence
     *        as no changes are being made.
     * @pre <code>this.type == that.type iff null != that</code>
     * @return Reference to self for chain-calling.
     */
    @SuppressWarnings("null")
    public Value assimilate(final @Nullable List<Value> that) {
        if (Objects.isNull(that) || that.isEmpty()) {
            return this;
        }

        Preconditions.checkArgument(that.size() == 1);
        final Value thatValue = that.get(0);
        Preconditions.checkArgument(type.equals(thatValue.type));
        domain = thatValue.domain;
        value = thatValue.value;
        return this;
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(final @Nullable Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }

        if (that instanceof Value) {
            if (type.equals(((Value) that).type) &&
                            identifier.equals(((Value) that).identifier) &&
                            domain.equals(((Value) that).domain) &&
                            Objects.equals(value, ((Value) that).value)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("null")
    @Override
    public int hashCode() {
        return (type.toString() + identifier.toString() + Objects.toString(value) + domain.hashCode()).hashCode();
    }
}