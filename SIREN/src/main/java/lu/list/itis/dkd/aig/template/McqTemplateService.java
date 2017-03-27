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

import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Class implementing a REST end point that exposes functionality to deal with MCQ templates and
 * generate MCQ items from them.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 1.1
 * @version 2.0.0
 */
@Path("/templates/mcq")
@NonNullByDefault
public class McqTemplateService extends TemplateService {
    private static final String TEMPLATE_FOLDER = "templates" + TemplateService.FILE_SEPARATOR + "mcq" + TemplateService.FILE_SEPARATOR; //$NON-NLS-1$ //$NON-NLS-2$
    @Context
    private ServletContext context;

    @Override
    @POST
    @Consumes({MediaType.TEXT_XML})
    public Response storeTemplate(final InputStream stream, @QueryParam(value = "name") final String name) {
        return storeTemplate(context, stream, name, McqTemplateService.TEMPLATE_FOLDER);
    }

    @Override
    @GET
    @Produces({MediaType.TEXT_XML, MediaType.TEXT_PLAIN})
    public Response getTemplate(@QueryParam(value = "name") final String name) {
        return getTemplate(context, name, McqTemplateService.TEMPLATE_FOLDER);
    }

    @Override
    @DELETE
    @Produces({MediaType.TEXT_PLAIN})
    public Response deleteTemplate(@QueryParam(value = "name") final String name) {
        return deleteTemplate(context, name, McqTemplateService.TEMPLATE_FOLDER);
    }

    @Override
    @GET
    @Path("/generate")
    @Produces({MediaType.TEXT_XML})
    public Response generateItems(@QueryParam(value = "name") final String name, @QueryParam(value = "input") final String jsonInput) {
        return generateItems(context, name, jsonInput, McqTemplateService.TEMPLATE_FOLDER, TemplateService.defaultMaximumNumberOfItems);
    }
}