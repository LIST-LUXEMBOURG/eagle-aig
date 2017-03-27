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
package lu.list.itis.dkd.aig.template;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lu.list.itis.dkd.aig.resolution.ItemFactory;
import lu.list.itis.dkd.aig.resolution.ItemFactoryBuilder;
import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.TemplateManager;

/**
 * Abstract implementation of all the signatures a template service is required to provide.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 2.0
 * @version 2.0.0
 */
public abstract class TemplateService {

    private static final Logger logger = Logger.getLogger(TemplateService.class.getSimpleName());
    
    protected static final String FILE_SEPARATOR = System.getProperties().get("file.separator").toString(); //$NON-NLS-1$
    public static int defaultMaximumNumberOfItems = Integer.parseInt(TemplateManager.getProperty("items.build.limit"));//$NON-NLS-1$

    /**
     * Method defining an endpoint that allows to receive and store an XML-based item template.
     *
     * @param stream
     *        An XML stream of data containing the item template. See the referenced schema for
     *        details on form and layout. TODO Generate and reference schema.
     * @param name
     *        The name to store the template as.
     * @return A {@link Response} indicating the success or failure of the operation.
     */
    public abstract Response storeTemplate(final InputStream stream, final String name);

    protected Response storeTemplate(final ServletContext context, final InputStream stream, final String name, final String folder) {
        try {
            TemplateManager.store(context, stream, name, folder);
            return Response.ok().build();
        } catch (final UnsupportedEncodingException e) {
            Logger.getLogger(TemplateService.class.getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().entity("UTF-8 was not supported by the stream reader!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        } catch (final IOException e) {
            Logger.getLogger(TemplateService.class.getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().entity("An error occured while attempting to store the template!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        }
    }

    /**
     * Method defining an endpoint that allows to retrieve an XML-based item template.
     *
     * @param name
     *        The name of the template to retrieve.
     * @return A {@link Response} indicating the success or failure of the operation.
     */
    public abstract Response getTemplate(final String name);

    protected Response getTemplate(final ServletContext context, final String name, final String folder) {
        try {
            return Response.status(200).type(MediaType.TEXT_XML).entity(TemplateManager.fetch(context, name, folder)).build();
        } catch (final NoSuchFileException e) {
            return Response.status(404).type(MediaType.TEXT_PLAIN).entity("No template with given name was found!").build(); //$NON-NLS-1$
        } catch (final IOException e) {
            Logger.getLogger(TemplateService.class.getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().entity("Error while attempting to retrieve the template!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        }
    }

    /**
     * Method defining an endpoint that allows for the deletion of a stored item template.
     *
     * @param name
     *        The name of the template to delete.
     * @return A {@link Response} holding a message indicating whether the deletion was successful,
     *         that is, if there was such a template to delete.
     */
    public abstract Response deleteTemplate(final String name);

    protected Response deleteTemplate(final ServletContext context, final String name, final String folder) {
        try {
            final boolean result = TemplateManager.delete(context, name, folder);
            return Response.status(200).type(MediaType.TEXT_PLAIN).entity(result ? "Template deleted!" : "No such template to delete!").build(); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (final IOException e) {
            Logger.getLogger(TemplateService.class.getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().entity("The template could not be deleted due to an internal server error!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        }
    }

    /**
     * Method called to generate a {@link List} of QTI items corresponding to the template with the
     * given name.
     *
     * @param name
     *        The name of the template to load.
     * @param jsonInput
     *        The inputs map as Json, for example: <code>{"key":"value"}</code
     * @return An XML with a root node <code>items</code> holding <code>assessmentItem</code> child
     *         nodes which contain the generated test items.
     */
    public abstract Response generateItems(@QueryParam(value = "name") final String name, @QueryParam(value = "input") final String jsonInput);

    protected Response generateItems(final ServletContext context, final String name, final String jsonInput, final String folder, final int maxNumberOfItems) {
        
        final Gson gson = new Gson();
        final Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();

        
        final HashMap<String, String> input = gson.fromJson(jsonInput, mapType);
        
        String template;
        try {
            template = TemplateManager.fetch(context, name, folder);
        } catch (final IOException e) {
            Logger.getLogger(TemplateService.class.getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().entity("An error occured while trying to retrieve the template!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        }

        if (null == template) {
            return Response.status(412).entity("The named template could not be found. No items generated!").build(); //$NON-NLS-1$
        }

        final ItemFactoryBuilder itemFactoryBuilder = new ItemFactoryBuilder();
        try {
            itemFactoryBuilder.withTemplate(template);
        } catch (final TemplateParseException e) {
            return Response.status(500).entity("The provided template contained errors and could not be used to build items!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        }
        itemFactoryBuilder.withItemLimit(maxNumberOfItems);
        itemFactoryBuilder.withInput(input);
        ItemFactory factory = null;
        try {
            factory = itemFactoryBuilder.build();
        } catch (final TemplateParseException e) {
            return Response.status(500).entity("Internal server error. Internal state conflicted due to logical fallacy! Contact your administrator!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        } catch (final ResolutionException e) {
            return Response.status(500).entity("The inputs could not be interpreted or were not provided!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        } catch (final NotImplementedException e) {
            return Response.status(500).entity("The initialization of a required component (process) failed!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        } catch (final TemplateConsistencyException e) {
            return Response.status(500).entity("Inconsistencies in the provided variables, their identifiers, or their mapping caused an exception while trying to construct an item!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        }

        List<String> items;
        try {
            items = factory.buildItems();
        } catch (final TemplateParseException e) {
            return Response.status(500).entity("Variables could not be resolved to the template!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        } catch (final TemplateConsistencyException e) {
            return Response.status(500).entity("Variables could not be resolved as by their specification in the template!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        } catch (final ResolutionException e) {
            return Response.status(500).entity("The resolution of one or more variables failed!\n" + ExceptionUtils.getStackTrace(e)).build(); //$NON-NLS-1$
        }

        return Response.status(200).type(MediaType.TEXT_XML).entity("<layers>" + String.join("", items) + "</layers>").build(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}