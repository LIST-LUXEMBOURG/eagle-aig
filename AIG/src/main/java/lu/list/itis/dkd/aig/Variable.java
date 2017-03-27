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

import lu.list.itis.dkd.aig.process.ResolutionProcess;
import lu.list.itis.dkd.aig.resolution.Dependency;
import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * A class used to encapsule the results from a {@link ResolutionProcess}. The {@link Variable}
 * features a unique identifier and encapsules values by key. This means that a {@link Variable} can
 * hold many distinct and unique values. This is needed to make sure that values for variables
 * obtained from a {@link ResolutionProcess} are semantically tied together and retrievable from a
 * coherent value space, the {@link Variable} instance.
 * </p>
 * <p>
 * Note that values for a variable may not share a value or type domain, that is, all values must be
 * distinct regarding their key used as an identifier, and their type. For example, a variable may
 * hold one URI and one TEXT type values buy may not hold two text type values.
 * </p>
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.1
 */
@NonNullByDefault
public final class Variable implements Comparable<Variable> {
    private URI identifier;
    private Multimap<URI, Dependency> dependencies = HashMultimap.create();
    private URI initializationProcessReference;
    private Locale locale;

    private Map<URI, Value> valuesByIdentifier = new HashMap<URI, Value>();
    private ArrayListMultimap<ValueType, Value> valuesByType = ArrayListMultimap.create();

    /**
     * Constructor initializing all fields from a map of values.
     *
     * @param parameters
     *        A list of parameters keyed by the parameter name to draw all fields values from.
     * @param values
     *        The values the variable can handle by keys and type.
     * @throws TemplateParseException
     *         Thrown when one or more of the required parameter values was missing or if a
     *         constraint regarding values is violated.
     */
    public Variable(final ArrayListMultimap<String, String> parameters, final List<Value> values) throws TemplateParseException {
        try {
            identifier = new URI(parameters.get(Externalization.IDENTIFIER_ELEMENT).get(0));
        } catch (final URISyntaxException e) {
            throw new TemplateParseException("The identifier provided for the variable was not a legal URI!", e); //$NON-NLS-1$
        }

        if (parameters.containsKey(Externalization.LANGUAGE_ELEMENT)) {
            locale = new Locale(parameters.get(Externalization.LANGUAGE_ELEMENT).get(0));
        } else {
            locale = new Locale(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
        }

        if (parameters.containsKey(Externalization.INITIALIZATION_ELEMENT)) {
            try {
                initializationProcessReference = new URI(parameters.get(Externalization.INITIALIZATION_ELEMENT).get(0));
            } catch (final URISyntaxException e) {
                throw new TemplateParseException("The identifier provided for the initialization process reference was not a legal URI!", e); //$NON-NLS-1$
            }
        }
        registerValues(values);
    }

    /** Private constructor that is only used for cloning. */
    private Variable() {}

    /**
     * Method used to register all values with the maps referring to them by key and value.
     *
     * @param values
     *        A list containing all values to register.
     * @throws TemplateParseException
     *         Thrown when the provided list did not contain any values.
     */
    private void registerValues(final List<Value> values) throws TemplateParseException {
        if (values.isEmpty()) {
            throw new TemplateParseException("The variable needs to map to at least one value!"); //$NON-NLS-1$
        }

        for (final Value value : values) {
            valuesByIdentifier.put(value.getIdentifier(), value);
            valuesByType.put(value.getType(), value);
        }
    }


    /**
     * Method used to set the value of the {@link Variable}. The key will be extracted from the
     * value. Should the key already have an associated value, it will be overwritten by the new
     * value provided that the value is valid. The method will add the value to both reference maps.
     * <br>
     * <br>
     *
     * <b>Note</b>: The method will clear all values held for the type identical to the given
     * value's type and set it anew.
     *
     * @param value
     *        The value to add for the key.
     * @return <code>true</code> if the value was added and <code>false</code> otherwise.
     */
    public boolean setValue(final Value value) {
        if (valuesByIdentifier.containsKey(value.getIdentifier())) {
            valuesByIdentifier.put(value.getIdentifier(), value);
            valuesByType.removeAll(value.getType());
            valuesByType.put(value.getType(), value);
            return true;
        }
        return false;
    }

    /**
     * Simple getter method for a specific value.
     *
     * @param key
     *        The key to retrieve the value for.
     * @return The value or <code>null</code> if the value hasn't been set, respectively, if the key
     *         is not mapped.
     */
    public @Nullable Value getValue(final URI key) {
        return valuesByIdentifier.get(key);
    }

    /**
     * Method used to get a specific value by type. The method will only return the first value.
     *
     * @param type
     *        The type of the value to fetch.
     * @return The first value or <code>null</code> if the value hasn't been set, respectively, if
     *         the key is not mapped.
     */
    public @Nullable Value getOneValue(final ValueType type) {
        return valuesByType.containsKey(type) ? valuesByType.get(type).get(0) : null;
    }

    /**
     * Method used to get specific values by type.
     *
     * @param type
     *        The type of the value to fetch.
     * @return The values mapped by the type key.
     */
    @SuppressWarnings("null")
    public List<Value> getValues(final ValueType type) {
        return valuesByType.get(type);
    }

    /**
     * Method used to retrieve all values held by this variable instance.
     *
     * @return All values held by this variable.
     */
    @SuppressWarnings("null")
    public Collection<Value> getValues() {
        return valuesByIdentifier.values();
    }


    /**
     * Simple getter method for a specific value by identifier. This is an ease of access method
     * that wraps the {@link #getValue(URI)} method.
     *
     * @param key
     *        The key to retrieve the value for.
     * @return The value or <code>null</code> if the value hasn't been set, respectively the key is
     *         not mapped.
     * @throws TemplateConsistencyException
     *         Thrown when the provided key string was not a valid {@link URI}.
     * @see #getValue(URI)
     */
    public @Nullable Value getValueByIdentifier(final @Nullable String key) throws TemplateConsistencyException {
        try {
            return getValue(new URI(Strings.nullToEmpty(key)));
        } catch (final URISyntaxException e) {
            throw new TemplateConsistencyException("The provided key \"" + Strings.nullToEmpty(key) + "\" was not a valid URI and could, therefore, not be mapped to a value!", e); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * Simple getter method for the variable's stored language. If no locale was set then the
     * default is English.
     *
     * @return The language the variable holds variables in according to
     *         {@link Locale#getLanguage()}.
     */
    @SuppressWarnings("null")
    public String getLanguage() {
        return locale.getLanguage();
    }

    /**
     * This is an ease of access method that wraps {@link #getOneValue(ValueType)} while setting the
     * parameter to {@link ValueType#TEXT}.
     *
     * @return The value or <code>null</code> if the value hasn't been set, respectively, if the key
     *         is not mapped.
     *
     * @see #getOneValue(ValueType)
     */
    @SuppressWarnings("null")
    public @Nullable String getTextContent() {
        if ((getOneValue(ValueType.TEXT) != null) && (getOneValue(ValueType.TEXT).getValue() != null)) {
            return getOneValue(ValueType.TEXT).getValue().toString();
        }
        return null;
    }

    /**
     * This is an ease of access method that wraps {@link #getOneValue(ValueType)} while setting the
     * parameter to {@link ValueType#URI}.
     *
     * @return The value or <code>null</code> if the value hasn't been set, respectively, if the key
     *         is not mapped.
     *
     * @see #getOneValue(ValueType)
     */
    @SuppressWarnings("null")
    public @Nullable URI getSemanticContent() {
        if ((getOneValue(ValueType.URI) != null) && (getOneValue(ValueType.URI).getValue() != null)) {
            return (URI) getOneValue(ValueType.URI).getValue();
        }
        return null;
    }

    /**
     * This is an ease of access method that wraps {@link #getOneValue(ValueType)} while setting the
     * parameter to {@link ValueType#NUMBER}.
     *
     * @return The value or <code>null</code> if the value hasn't been set, respectively, if the key
     *         is not mapped.
     *
     * @see #getOneValue(ValueType)
     */
    @SuppressWarnings("null")
    public @Nullable Double getNumericalContent() {
        if ((getOneValue(ValueType.NUMBER) != null) && (getOneValue(ValueType.NUMBER).getValue() != null)) {
            return (Double) getOneValue(ValueType.NUMBER).getValue();
        }
        return null;
    }

    /**
     * This is an ease of access method that wraps {@link #getOneValue(ValueType)} while setting the
     * parameter to {@link ValueType#MEDIA}.
     *
     * @return The value or <code>null</code> if the value hasn't been set, respectively, if the key
     *         is not mapped.
     *
     * @see #getOneValue(ValueType)
     */
    @SuppressWarnings("null")
    public @Nullable Object getMediaContent() {
        if ((getOneValue(ValueType.MEDIA) != null) && (getOneValue(ValueType.MEDIA).getValue() != null)) {
            return getOneValue(ValueType.MEDIA).getValue();
        }
        return null;
    }

    /**
     * Method used to retrieve the identifier of the variable.
     *
     * @return The identifier of the variable.
     */
    @SuppressWarnings("null")
    public URI getIdentifier() {
        return identifier;
    }

    /**
     * Simple getter method for initializationProcessReference.
     *
     * @return The value of initializationProcessReference.
     */
    public @Nullable URI getInitializationProcessReference() {
        return initializationProcessReference;
    }

    /**
     * {@inheritDoc} <br>
     * This implementation of {@link Object#equals(Object)} will return <code>true</code> iff the
     * identifier and language are identical.
     */
    @Override
    public boolean equals(final @Nullable Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }

        if (that instanceof Variable) {
            if (identifier.equals(((Variable) that).identifier) &&
                            getLanguage().equals(((Variable) that).getLanguage()) &&
                            Objects.equals(initializationProcessReference, ((Variable) that).initializationProcessReference) &&
                            areValuesEqual((Variable) that) &&
                            areDependenciesEqual()) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Test the equality of the maps containing the values by type held by this and the
     * parameterized variable.
     *
     * @param that
     *        The variable to compare the values with.
     * @return The result of the equality test of both maps.
     * @see ArrayListMultimap#equals(Object)
     */
    private boolean areValuesEqual(final Variable that) {
        return valuesByType.equals(that.valuesByType);
    }

    /**
     * @return
     */
    private boolean areDependenciesEqual() {
        // TODO
        return true;
    }

    /**
     * {@inheritDoc} <br>
     * <br>
     * This implementation of {@link Object#hashCode()} will return the has code of the string
     * obtained from concatenating the language with the identifier.
     */
    @Override
    public int hashCode() {
        return getLanguage().concat(getIdentifier().toString()).hashCode();
    }


    /**
     * Method for computing the similarity between this instance and the provided instance. The
     * similarity is computed by a {@link SimilarityProvider}.
     *
     * @param that
     *        The {@link Variable} instance to get the similarity to.
     * @return A {@link Float} value between 0 and 1 with 1 meaning complete similarity.
     * @throws SimilarityComputationException
     *         Thrown when an error occurs during the computation of the semantic similarity.
     * @throws InitializationException
     *         Thrown when the similarity provider (or one of its components) needed to calculate
     *         the semantic similarity failed to initialize correctly.
     */
    public float getSimilarityTo(final Variable that) {
        return SimilarityProvider.getInstance().compare(this, that);
    }

    /**
     * Method for retrieving the key set of the values as stored by their identifiers.
     *
     * @return A set of all unique value identifiers.
     */
    @SuppressWarnings("null")
    public Set<URI> getMappingByIdentifiers() {
        return valuesByIdentifier.keySet();
    }

    /**
     * Method for retrieving the key set of the value types.
     *
     * @return The set of all value types mapped by this variable.
     */
    @SuppressWarnings("null")
    public Set<ValueType> getMappingByTypes() {
        return valuesByType.keySet();
    }

    /**
     * Method used to create a copy of this instance.
     *
     * @param cloneValues
     *        Parameter indicating whether the stored values are to be cloned as well.
     * @return A copy of this instance.
     */
    public Variable clone(final boolean cloneValues) {
        final Variable clone = new Variable();

        clone.dependencies = dependencies;
        clone.identifier = identifier;
        clone.initializationProcessReference = initializationProcessReference;
        clone.locale = locale;
        clone.valuesByIdentifier = valuesByIdentifier;
        clone.valuesByType = valuesByType;

        for (final URI key : valuesByIdentifier.keySet()) {
            valuesByIdentifier.put(key, valuesByIdentifier.get(key).clone(cloneValues));
        }

        for (final ValueType type : valuesByType.keySet()) {
            valuesByType.get(type).forEach(v -> valuesByType.put(type, v.clone(cloneValues)));
        }

        return clone;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public int compareTo(@Nullable final Variable that) {
        if (null == that) {
            return 1;
        }

        if ((getOneValue(ValueType.TEXT) != null) && (that.getOneValue(ValueType.TEXT) != null)) {
            return getOneValue(ValueType.TEXT).compareTo((that.getOneValue(ValueType.TEXT)));
        }

        if ((getOneValue(ValueType.NUMBER) != null) && (that.getOneValue(ValueType.NUMBER) != null)) {
            return getOneValue(ValueType.NUMBER).compareTo((that.getOneValue(ValueType.NUMBER)));
        }

        if ((getOneValue(ValueType.URI) != null) && (that.getOneValue(ValueType.URI) != null)) {
            return getOneValue(ValueType.URI).compareTo((that.getOneValue(ValueType.URI)));
        }

        return identifier.compareTo(that.identifier);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable String toString() {
        return Strings.nullToEmpty(getTextContent());
    }

    /**
     * Method used by variables defined as placeholder or dependencies to easily assimilate the
     * values of a provided variable that is to serve as master for imprinting values.<br>
     * <br>
     *
     * <b>Note</b>: This method will only map the first of each value type, that is, if more than
     * one value is mapped to a type, this method will fail.
     *
     * @param that
     *        The variable to duplicate the values from.
     * @throws TemplateConsistencyException
     *         Thrown when the variable to assimilate values from was <code>null</code>.
     */
    public void assimilateValuesOf(final @Nullable Variable that) throws TemplateConsistencyException {
        if (null == that) {
            throw new TemplateConsistencyException("A placeholder variable has been passed a null variable! A placeholder cannot assume a void value!"); //$NON-NLS-1$
        }

        // All values that can be mapped are contained
        for (final Value value : valuesByIdentifier.values()) {
            value.assimilate(that.valuesByType.get(value.getType()));
        }
    }

    /**
     * Method used to retrieve a textual or numerical values that typically serves as a label.
     *
     * @return The textual value held by the variable if present, the numerical value otherwise. The
     *         method may return the actual value as a string, an empty string, or <code>null</code>
     *         as a string.
     */
    @SuppressWarnings("null")
    public String getLabel() {
        if (Strings.isNullOrEmpty(getTextContent())) {
            return Objects.toString(getNumericalContent());
        } else {
            return Strings.nullToEmpty(getTextContent());
        }
    }
}