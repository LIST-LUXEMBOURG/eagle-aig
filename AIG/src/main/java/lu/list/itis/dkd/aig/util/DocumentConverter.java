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
package lu.list.itis.dkd.aig.util;

import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * Class used to convert XMl as {@link String} and {@link Document} into their
 * respective other forms.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.3
 * @version 0.8.0
 */
@NonNullByDefault
public final class DocumentConverter {

	/**
	 * Method for converting a {@link Document} instance into its XML
	 * {@link String} representation.
	 *
	 * @param document
	 *            The {@link Document} instance to convert.
	 * @return The {@link String} representation of the document using an
	 *         {@link XMLOutputter} and setting {@link Format} to pretty print.
	 */
	@SuppressWarnings("null")
	public static String convertDocumentToString(final Document document) {

		final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(document);
	}

	/**
	 * Helper method to build a {@link Document} from an provided XML string.
	 *
	 * @param xml
	 *            The XML to build the document from.
	 * @return The created {@link Document} instance.
	 * @throws TemplateParseException
	 *             Thrown when conversion of the string to a document fails.
	 */
	@SuppressWarnings("null")
	public static Document convertStringToDocument(final String xml) throws TemplateParseException {
		final SAXBuilder saxBuilder = new SAXBuilder();
		try {
			Document document = saxBuilder.build(new StringReader(xml));
			return saxBuilder.build(new StringReader(removeTrailingEmptyElements(document)));
		} catch (final JDOMException e) {
			throw new TemplateParseException("The provided XML contains errors and cannot be parsed!", e); //$NON-NLS-1$
		} catch (final IOException e) {
			throw new TemplateParseException("An I/O error occured during parsing!", e); //$NON-NLS-1$
		}
	}

	/**
	 * PATCH: as template content is not using CDATA try to remove trailing
	 * white spaces and blank lines.
	 * 
	 * @param document
	 * @return well formated XML string
	 */
	private static String removeTrailingEmptyElements(final Document document) {
		XMLOutputter format = new XMLOutputter();
		format.setFormat(Format.getCompactFormat());
		return format.outputString(document);
	}

	/**
	 * Method used to build a {@link Document} from a provided input stream.
	 *
	 * @param inputStream
	 *            The stream to read the document from.
	 * @return The created {@link Document} instance.
	 * @throws TemplateParseException
	 *             Thrown when conversion of the stream contents to document
	 *             fails.
	 */
	@SuppressWarnings("null")
	public static Document convertStreamToDocument(final InputStream inputStream) throws TemplateParseException {
		final SAXBuilder saxBuilder = new SAXBuilder();
		saxBuilder.setIgnoringElementContentWhitespace(true);
		try {
			return saxBuilder.build(new InputStreamReader(inputStream));
		} catch (final JDOMException e) {
			throw new TemplateParseException("The provided XML contains errors and cannot be parsed!", e); //$NON-NLS-1$
		} catch (final IOException e) {
			throw new TemplateParseException("An I/O error occured during parsing!", e); //$NON-NLS-1$
		}
	}
}