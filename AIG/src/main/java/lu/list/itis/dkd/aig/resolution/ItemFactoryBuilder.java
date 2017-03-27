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
package lu.list.itis.dkd.aig.resolution;

import lu.list.itis.dkd.aig.util.DocumentConverter;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

import org.jdom2.Document;

//import com.hp.hpl.jena.sparql.function.library.max;

import java.io.InputStream;
import java.util.Map;

/**
 * Class used to build the concrete item factory provided a template and an input map.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public class ItemFactoryBuilder {

    private Template template;
    private Map<String, String> input;
    protected int maximumNumberOfItems = Integer.MAX_VALUE;

    /**
     * Method used to set the {@link Template} the builder will build the {@link ItemFactory} with.
     *
     * @param template
     *        The template instance to set.
     */
    public void withTemplate(final Template template) {
        this.template = template;
    }

    /**
     * Method used to set the {@link Template} the builder will build the {@link ItemFactory} with.
     *
     * @param inputStream
     *        The input stream to build the template document from.
     * @throws TemplateParseException
     *         Thrown when building the template from the input stream encountered an exception.
     */
    public void withTemplate(final InputStream inputStream) throws TemplateParseException {
        template = new Template(DocumentConverter.convertStreamToDocument(inputStream));
    }

    /**
     * Method used to set the {@link Template} the builder will build the {@link ItemFactory} with.
     *
     * @param templateDocument
     *        The document that represents the template in DOM format.
     * @throws TemplateParseException
     *         Thrown if building the template from the provided document encountered an exception.
     */
    public void withTemplate(final Document templateDocument) throws TemplateParseException {
        template = new Template(templateDocument);
    }

    /**
     * Method used to set the {@link Template} the builder will build the {@link ItemFactory} with.
     *
     * @param templateString
     *        The template document provided ad string.
     * @throws TemplateParseException
     *         Thrown when either building the DOM from the provided string or the {@link Template}
     *         from the DOm encountered an exception.
     */
    public void withTemplate(final String templateString) throws TemplateParseException {
        template = new Template(DocumentConverter.convertStringToDocument(templateString));
    }

    /**
     * Method used to set the map containing inputs, that is, key-value pairs for the generation of
     * items, to the provided map.
     *
     * @param input
     *        The map containing, as key-value pairs, the inputs to the item generation process.
     */
    public void withInput(final Map<String, String> input) {
        this.input = input;
    }

    /**
     * Method used to build a concrete item factory instance as defined by the information contained
     * in the metadata header of the template. Note that both the template and input need to have
     * been provided less this method throw an exception.
     *
     * @return An concrete factory instance as defined by the metadata header of the template.
     * @throws TemplateParseException
     *         Thrown when the template was not specified. A template needs to be specified in order
     *         for items to be built!
     * @throws ResolutionException
     *         Thrown when no input was defined.
     * @throws TemplateConsistencyException
     *         Thrown when inconsistencies in the provided variables, their identifiers, or their
     *         mapping caused an exception while trying to construct an item.
     * @throws SimilarityComputationException
     *         Thrown when an error arises when trying to compute the similarity between variables.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @SuppressWarnings("nls")
    public ItemFactory build() throws TemplateParseException, ResolutionException, TemplateConsistencyException {
        if (null == template) {
            throw new TemplateParseException("The template must be provided first!");
        }

        if (null == input) {
            throw new ResolutionException("The input may not be null!");
        }

        return createConcreteFactory();
    }

    /**
     * Method used to instantiate the concrete factory as defined by information contained in the
     * template metadata header.
     *
     * @return An instance of the concrete {@link ItemFactory} that can handle the building of items
     *         according to the template's {@link InteractionType}.
     * @throws SimilarityComputationException
     *         Thrown when an error arises when trying to compute the similarity between variables.
     * @throws ResolutionException
     *         Thrown when not enough resources could be resolved to allow for the complete
     *         resolution of the template.
     * @throws TemplateConsistencyException
     *         Thrown when inconsistencies in the provided variables, their identifiers, or their
     *         mapping caused an exception while trying to construct an item.
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @SuppressWarnings("null")
    private ItemFactory createConcreteFactory() throws ResolutionException, TemplateConsistencyException {
        switch (template.getInteractionType()) {

            case CHOICE_INTERACTION:
                return new ChoiceItemFactory(template, input, maximumNumberOfItems);
            case GAP_MATCH_INTERACTION:
                return new ClozeItemFactory(template, input, maximumNumberOfItems);
            case MATCH_INTERACTION:
                return new MatchItemFactory(template, input, maximumNumberOfItems);
            case TEXT_ENTRY_INTERACTION:
                return new OpenItemFactory(template, input, maximumNumberOfItems);

            case ASSOCIATE_INTERACTION:
                // $FALL-THROUGH$
            case DRAWING_INTERACTION:
                // $FALL-THROUGH$
            case EXTENDED_TEXT_INTERACTION:
                // $FALL-THROUGH$
            case FILE_UPLOAD:
                // $FALL-THROUGH$
            case GRAPHIC_ASSOCIATE_INTERACTION:
                // $FALL-THROUGH$
            case GRAPHIC_GAP_MATCH_INTERACTION:
                // $FALL-THROUGH$
            case GRAPHIC_ORDER_INTERACTION:
                // $FALL-THROUGH$
            case HOT_SPOT_INTERACTION:
                // $FALL-THROUGH$
            case HOT_TEXT_INTERACTION:
                // $FALL-THROUGH$
            case INLINE_CHOICE_INTERACTION:
                // $FALL-THROUGH$
            case ORDER_INTERACTION:
                // $FALL-THROUGH$
            case POSITION_OBJECT_INTERACTION:
                // $FALL-THROUGH$
            case SELECT_POINT_INTERACTION:
                // $FALL-THROUGH$
            case SLIDER_INTEARCTION:
                // $FALL-THROUGH$
            default:
                throw new ResolutionException("The provided construct type did not match any recognized or treatable type: " + template.getInteractionType()); //$NON-NLS-1$
        }
    }

	public void withItemLimit(int maximumNumberOfItems) {
		this.maximumNumberOfItems = maximumNumberOfItems;	
	}
}