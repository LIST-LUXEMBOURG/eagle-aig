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

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.1
 * @version 2.1.0
 */
public class LanguageHelper {

	protected static final Logger logger = Logger.getLogger(LanguageHelper.class.getName());

	final static Properties properties = NLPPropertiesFetcher.fetchProperties();

	static {
		// Load language profiles
		String dir = properties.getProperty("language.recognition.directory");
		String en = dir + '/' + properties.getProperty("language.recognition.en");
		File enTmp = Resources.getFile(en.toString());
		try {
			DetectorFactory.loadProfile(enTmp.getParentFile());
		} catch (LangDetectException e) {
			logger.log(Level.SEVERE, "Lang detection init failed", e);
		}
	}

	// TODO folder not accepted at server!!!

	/**
	 * Guesses the language of a text by statistical means and returns a
	 * Language object.
	 * 
	 * @param text
	 * @return language object (EN, DE, FR)
	 */
	public static Language detectLanguage(String text) {
		Language guessedLanguage;
		try {
			Detector languageDetector = DetectorFactory.create();
			languageDetector.append(text);

			String language = languageDetector.detect();
			switch (language) {
			case "de":
				guessedLanguage = Language.DE;
				break;
			case "fr":
				guessedLanguage = Language.FR;
				break;
			default:
				guessedLanguage = Language.EN;
				break;
			}
		} catch (final LangDetectException e) {
			e.printStackTrace();
			return Language.EN;
		}
		return guessedLanguage;
	}
}
