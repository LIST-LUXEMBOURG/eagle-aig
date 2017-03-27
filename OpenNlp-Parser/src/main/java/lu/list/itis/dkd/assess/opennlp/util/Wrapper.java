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
package lu.list.itis.dkd.assess.opennlp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Wrapper {
	protected static final Logger logger = Logger.getLogger(Wrapper.class.getSimpleName());

	/**
	 * @param is
	 * @return The content of a text file as String.
	 */
	public static String loadTextFile(InputStream is) {
		String fileContent = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
			final StringBuilder sb = new StringBuilder();
			String line = "";

			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}

			fileContent = sb.toString();
			br.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Text extension not supported.", e);
		}

		return fileContent;
	}
	
	/**
	 * @param is
	 * @param lowerCase: If true, every entry is lowercased.
	 * @return The content of a file as a list.
	 */
	public static List<String> loadListedTextFile(InputStream is, boolean lowerCase) {
        List<String> content = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
            String line = "";

            while ((line = br.readLine()) != null) {
                if (lowerCase){
                    content.add(line.trim().toLowerCase());
                }
                else {
                    content.add(line.trim());
                }
            }
;
            br.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Text extension not supported.", e);
        }

        return content;
    }

	/**
	 * @param wikiUrl
	 * @return Source Page of an url.
	 * @throws IOException 
	 */
	public static String getWebPageSource(String url) throws IOException {
		URL urlObject = new URL(url);
		URLConnection urlCon = urlObject.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "UTF8"));
		String inputLine;
		StringBuilder sb = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();

		return sb.toString();
	}

	public static Document getDocument(String xml) throws IOException {
		Document doc = null;
		final SAXBuilder saxBuilder = new SAXBuilder();
		try {
			doc = saxBuilder.build(new StringReader(xml));
		} catch (JDOMException e) {
			logger.log(Level.SEVERE, "Problem creating document", e);
			e.printStackTrace();
		}

		return doc;
	}

	// TODO Load other extensions (e.g. word, pdf, ...)
}
