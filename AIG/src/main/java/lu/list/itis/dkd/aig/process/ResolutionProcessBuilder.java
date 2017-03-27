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
package lu.list.itis.dkd.aig.process;

import lu.list.itis.dkd.aig.TemplateResourceBuilder;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;

import org.jdom2.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public class ResolutionProcessBuilder extends TemplateResourceBuilder {

    /**
     * There is no need for an instance of this class.
     */
    private ResolutionProcessBuilder() {
        // Helper class featuring only static methods.
    }

    /**
     * @param parentElement
     *        The parent element of the process holding the process tag and as child nodes the
     *        parameters necessary to instantiate a concrete process.
     * @return An instance of the process as defined by the type element of the provided parent
     *         element.
     * @throws TemplateParseException
     *         Thrown when the instantiation failed due to the type of process being unknown or
     *         unsupported.
     */
    @SuppressWarnings("null")
    public static ResolutionProcess buildProcessFrom(final Element parentElement) throws TemplateParseException {
        final ArrayListMultimap<String, String> parameters = TemplateResourceBuilder.getParameters(parentElement);

        final String type = parameters.get(Externalization.TYPE_NODE).get(0);
        if (Strings.isNullOrEmpty(type)) {
            throw new TemplateParseException("Process instantiation failed. No node named \"type\" containing a valid process type found!"); //$NON-NLS-1$
        }

        try {
            final Class<?> processClass = Class.forName(ResolutionProcessBuilder.class.getPackage().getName() + Externalization.NAMESPACE_SEPARATOR + type);
            final Constructor<?> constructor = processClass.getConstructor(new Class[] {ArrayListMultimap.class});
            return (ResolutionProcess) constructor.newInstance(new Object[] {parameters});
        } catch (final ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new TemplateParseException(e.getCause().getMessage(), e.getCause());
        }
    }
}