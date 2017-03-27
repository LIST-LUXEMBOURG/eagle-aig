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

import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import com.google.common.base.Splitter;

import org.jdom2.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.8.0
 */
@NonNullByDefault
public final class Dependency implements Comparable<Dependency> {

    private final URI head;
    private final int priority;
    private final int cardinality;
    private final Set<URI> tail = new HashSet<>();
    private final List<URI> unmappedTailElements = new ArrayList<>();

    /**
     * Constructor initializing all fields from a provided element.
     *
     * @param dependencyElement
     *
     * @throws TemplateParseException
     */
    public Dependency(final Element dependencyElement) throws TemplateParseException {
        try {
            head = new URI(dependencyElement.getChildText(Externalization.HEAD_ELEMENT));
        } catch (final URISyntaxException e) {
            throw new TemplateParseException("The \"head\" element did not refer to a valid URI!", e); //$NON-NLS-1$
        }

        try {
            priority = Integer.parseInt(dependencyElement.getChildText(Externalization.PRIORITY_ELEMENT));
        } catch (final NumberFormatException e) {
            throw new TemplateParseException("The provided \"priority\" was not a valid integer!", e); //$NON-NLS-1$
        }

        try {
            cardinality = Integer.parseInt(dependencyElement.getChildText(Externalization.CARDINALITY_ELEMENT));
        } catch (final NumberFormatException e) {
            throw new TemplateParseException("The provided \"cardinality\" was not a valid integer!", e); //$NON-NLS-1$
        }

        for (final String identifier : Splitter.on(",").omitEmptyStrings().trimResults().splitToList(dependencyElement.getChildText(Externalization.TAIL_ELEMENT))) { //$NON-NLS-1$
            try {
                tail.add(new URI(identifier));
            } catch (final URISyntaxException e) {
                throw new TemplateParseException("The \"tail\" element " + identifier + " did not refer to a valid URI!", e); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        unmappedTailElements.addAll(tail);
    }

    /**
     * This method compares the priorities assigned to each dependency.<br>
     * <br>
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("null")
    @Override
    public int compareTo(final @Nullable Dependency that) {
        // A dependency must have a legal priority due to its initialization.
        return Integer.compareUnsigned(priority, that.priority);
    }

    /**
     * Simple getter method for head.
     *
     * @return The value of head.
     */
    @SuppressWarnings("null")
    public URI getHead() {
        return head;
    }

    /**
     * Simple getter method for priority.
     *
     * @return The value of priority.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Simple getter method for cardinality.
     *
     * @return The value of cardinality.
     */
    public int getCardinality() {
        return cardinality;
    }

    /**
     * Method used to retrieve an unmapped tail element. The method will remove the element from the
     * tail elements such that it can only be retrieved once.
     *
     * @return A tail element that has previously not been retrieved and, potentially, mapped.
     * @throws TemplateConsistencyException
     *         Thrown when a tail element was to be retrieved while the tail contained no more
     *         elements. This error indicates an issue with the cardinality or dependency mapping in
     *         the template.
     */
    public @Nullable URI getUnmappedTailElement() throws TemplateConsistencyException {
        try {
            return unmappedTailElements.remove(0);
        } catch (final IndexOutOfBoundsException e) {
            throw new TemplateConsistencyException("All tail elements were already mapped. No tail element could be mapped to the head!", e); //$NON-NLS-1$
        }
    }

    /**
     * Returns true if the tail contains the specified identifier.
     *
     * @param identifier
     *        The identifier to search for.
     * @return <code>True</code> if and only if the tail contains the identifier e such that
     *         <code>(identifier==null ? e==null : identifier.equals(e))</code>.
     */
    public boolean tailContains(final URI identifier) {
        return tail.contains(identifier);
    }

    /**
     * The method tests equality based on priorities.<br>
     * <br>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final @Nullable Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }

        if (that instanceof Dependency) {
            if (priority == ((Dependency) that).priority) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getPriority();
    }
}