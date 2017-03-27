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

import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import com.google.common.collect.ArrayListMultimap;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.jdom2.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Class used to regroup some core functionality of resource builders.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.8
 * @version 0.8.0
 */
@NonNullByDefault
public abstract class TemplateResourceBuilder {

    private static HashMap<URI, Element> valueBlueprints = new HashMap<>();

    /**
     * Constructor for class containing only static methods.
     */
    protected TemplateResourceBuilder() {
        // Nothing to do here!
    }

    @SuppressWarnings("null")
    protected static ArrayListMultimap<String, String> getParameters(final Element parentElement) {
        final ArrayListMultimap<String, String> parameters = ArrayListMultimap.create();

        for (final Element element : parentElement.getChildren()) {
            if (element.getChildren().isEmpty()) {
                parameters.put(element.getName(), element.getText());
            } else {
                if (element.getName().equalsIgnoreCase(Externalization.VALUES_ELEMENT)) {
                    continue;
                }
                parameters.putAll(TemplateResourceBuilder.getParameters(element));
            }
        }
        return parameters;
    }

    /**
     * Method used to construct a list of values
     *
     * @param parentElement
     *        The parent element for which to extract all possible values.
     * @return A list of all values listed for the given parent element.
     * @throws TemplateParseException
     */
    protected static List<Value> getValues(final Element parentElement) throws TemplateParseException {
        final List<Value> values = new ArrayList<>();

        for (final Element element : parentElement.getChild(Externalization.VALUES_ELEMENT).getChildren()) {
            if (Strings.isNullOrEmpty(element.getChildText(Externalization.TYPE_ELEMENT))) {
                throw new TemplateParseException("The type element must be provided and cannot be empty!"); //$NON-NLS-1$
            }
            values.add(TemplateResourceBuilder.buildValue(element));
        }
        return values;
    }

    private static Value buildValue(final Element element) throws TemplateParseException {
        URI identifier;
        try {
            identifier = new URI(element.getChildText(Externalization.KEY_ELEMENT));
        } catch (final URISyntaxException e) {
            throw new TemplateParseException("The key of the value element was not a valid URI!", e); //$NON-NLS-1$
        }

        final ValueType type = ValueType.valueOf(element.getChildText(Externalization.TYPE_ELEMENT).toUpperCase());
        if (type.equals(ValueType.CODE)) {
            return new Value(identifier, type, UUID.randomUUID());
        } else {
            return new Value(identifier, type);
        }
    }

    /**
     * Method used to instantiate a new value given an identifier. The blueprint is an
     * {@link Element} that was previously stored when the value with said identifier was first
     * created. The method will return null if no blueprint is mapped to the given identifier.
     *
     * @param identifier
     *        The identifier for which to build a new value for.
     * @return A new instance of the value or <code>null</code> should the identifier not be mapped
     *         to a blueprint.
     * @throws TemplateParseException
     */
    @SuppressWarnings("null")
    public static @Nullable Value getValueFromBlueprint(final URI identifier) throws TemplateParseException {
        if (null == TemplateResourceBuilder.valueBlueprints.get(identifier)) {
            return null;
        }
        return TemplateResourceBuilder.buildValue(TemplateResourceBuilder.valueBlueprints.get(identifier));
    }

    /**
     * Method used to store the blueprint of a variable that is later used to build variables.
     *
     * @param blueprint
     *        The blueprint which contains all the required elements (pardon the pun) to build a
     *        variable instance.
     * @return The identifier of the registered blueprint.
     * @throws TemplateParseException
     *         Thrown when the instantiation failed due to the type of variable being unknown or
     *         unsupported.
     */
    public static URI registerBlueprint(final Element blueprint) throws TemplateParseException {
        final Value value = TemplateResourceBuilder.buildValue(blueprint);
        TemplateResourceBuilder.valueBlueprints.putIfAbsent(value.getIdentifier(), blueprint);
        return value.getIdentifier();
    }
}