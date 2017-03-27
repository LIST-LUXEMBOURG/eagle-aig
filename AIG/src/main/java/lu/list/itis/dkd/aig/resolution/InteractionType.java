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

import org.apache.jena.ext.com.google.common.base.Strings;

/**
 * Interaction types that can be processes by the resolution and optimization processes.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.7.2
 */
@SuppressWarnings("nls")
public enum InteractionType {
    CHOICE_INTERACTION("choiceInteraction"), ORDER_INTERACTION("orderInteraction"), ASSOCIATE_INTERACTION("associateInteraction"), MATCH_INTERACTION("matchInteraction"), GAP_MATCH_INTERACTION("gapMatchInteraction"), INLINE_CHOICE_INTERACTION("inlineChoiceInteraction"), TEXT_ENTRY_INTERACTION("textEntryInteraction"), EXTENDED_TEXT_INTERACTION("extendedTextInteraction"), HOT_TEXT_INTERACTION("hotTextInteraction"), HOT_SPOT_INTERACTION("hotSpotInteraction"), SELECT_POINT_INTERACTION("selectPointInteraction"), GRAPHIC_ORDER_INTERACTION("graphicOrderInteraction"), GRAPHIC_ASSOCIATE_INTERACTION("graphicAssociateInteraction"), GRAPHIC_GAP_MATCH_INTERACTION("graphicGapMatchInteraction"), POSITION_OBJECT_INTERACTION("positionObjectInteraction"), SLIDER_INTEARCTION("sliderInteraction"), DRAWING_INTERACTION("drawingInteraction"), FILE_UPLOAD("fileUpload");

    private String text;

    InteractionType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * Method used to instantiate the {@link InteractionType} from a given string.
     *
     * @param text
     *        The string from which to build the {@link InteractionType} from. The expected text is
     *        a camel-case string of the interaction type, i.e.: <tt>choiceInteraction</tt> or
     *        <tt>garMatchInteraction</tt>.
     * @pre text != null && ! test.isEmpty()
     * @return The {@link InteractionType} corresponding, in value, to the provided text bit.
     * @throws ResolutionException
     *         Thrown when the provided text is <code>null</code>, empty (<code>""</code>), or if
     *         the text did not match the camel-case representation of any known
     *         {@link InteractionType}.
     */
    public static InteractionType fromString(final String text) throws ResolutionException {
        if (Strings.isNullOrEmpty(text)) {
            throw new ResolutionException("The provided string \"" + text + "\" could not be associated to any InteractionType.");
        }

        for (final InteractionType type : InteractionType.values()) {
            if (text.equalsIgnoreCase(type.text)) {
                return type;
            }
        }

        throw new ResolutionException("The provided string \"" + text + "\" could not be associated to any InteractionType.");
    }
}