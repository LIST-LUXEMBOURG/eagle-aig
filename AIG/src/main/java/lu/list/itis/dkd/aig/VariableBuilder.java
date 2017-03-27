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

import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.dbc.annotation.NonNull;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.hp.hpl.jena.iri.impl.Force;

import org.jdom2.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Helper class featuring static method to build {@link Variable} instances retrieved from child
 * element data retrieved from a provided parent element.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public class VariableBuilder extends TemplateResourceBuilder {

    private static HashMap<URI, Element> variableBlueprints = new HashMap<>();

    /**
     * There is no need for an instance of this class.
     */
    private VariableBuilder() {
        // Helper class featuring only static methods.
    }

    /**
     * Method used to build a variable by extracting information on the variable from a parent node.
     *
     * @param parentElement
     *        The parent element of the variable holding the variable tag and as child nodes the
     *        parameters necessary to instantiate a concrete variable.
     * @return An instance of the variable as defined by the type element of the provided parent
     *         node.
     * @throws TemplateParseException
     *         Thrown when the instantiation failed due to the type of variable being unknown or
     *         unsupported.
     */
    public static Variable buildVariableFrom(final Element parentElement) throws TemplateParseException {
        final ArrayListMultimap<String, String> parameters = TemplateResourceBuilder.getParameters(parentElement);
        final List<Value> values = TemplateResourceBuilder.getValues(parentElement);

        return new Variable(parameters, values);
    }

    /**
     * Method used to store the blueprint of a variable that is later used to build variables.
     *
     * @param blueprint
     *        the blueprint which contains all the required elements (pardon the pun) to build a
     *        variable instance.
     * @return The identifier of the registered blueprint.
     *
     * @throws TemplateParseException
     *         Thrown when the instantiation failed due to the type of variable being unknown or
     *         unsupported.
     */
    public static URI registerBlueprint(final Element blueprint) throws TemplateParseException {
        final Variable variable = buildVariableFrom(blueprint);
        variableBlueprints.putIfAbsent(variable.getIdentifier(), blueprint);
        return variable.getIdentifier();
    }

    /**
     * Method used to instantiate a new variable given an identifier. The blueprint is an
     * {@link Element} that was previously stored when the variable with said identifier was first
     * created. The method will return <code>null</code> if no blueprint is mapped to the given
     * identifier.
     *
     * @param identifier
     *        The identifier for which to build a new value for.
     * @return A new instance of the value or <code>null</code> should the identifier not be mapped
     *         to a blueprint.
     * @throws ResolutionException
     *         Thrown when an attempt is made to build a variable from an unknown identifier.
     */
    public static Variable getVariableFromBlueprint(final @NonNull URI identifier) throws ResolutionException {
        if (null == variableBlueprints.get(identifier)) {
            throw new ResolutionException("The provided identifier " +  identifier.toString()  + " was not mapped to any variable!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        try {
            return buildVariableFrom(variableBlueprints.get(identifier));
        } catch (final TemplateParseException e) {
            /**
             * Under the assumption that the building succeeded at least once before, this should
             * never happen.
             */
            throw new Error("Critical exception. One of the core assumptions failed due to a logical fallacy!", e); //$NON-NLS-1$
        }
    }
   

    /**
     * Method used to instantiate a new variable given an identifier. The blueprint is an
     * {@link Element} that was previously stored when the variable with said identifier was first
     * created. The method will return <code>null</code> if no blueprint is mapped to the given
     * identifier.
     *
     * @param identifier
     *        The identifier for which to build a new value for.
     * @return A new instance of the value or <code>null</code> should the identifier not be mapped
     *         to a blueprint.
     * @throws TemplateParseException
     *         Thrown when the instantiation failed due to the type of variable being unknown or
     *         unsupported, respectively the provided identifier not being a valid URI.
     * @throws ResolutionException
     *         Thrown when an attempt is made to build a variable from an unknown identifier.
     */
    public static @Nullable Variable getVariableFromBlueprint(final String identifier) throws TemplateParseException, ResolutionException {
        try {
            return getVariableFromBlueprint(new URI(identifier));
        } catch (final URISyntaxException e) {
            throw new TemplateParseException("The provided identifier was not a valid URI!", e); //$NON-NLS-1$
        }
    }
}