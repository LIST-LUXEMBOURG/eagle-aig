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
package lu.list.itis.dkd.assess.cloze.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.DISCO;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class DiscoLoader {
	private static Properties properties = ClozePropertiesFetcher.fetchProperties("cloze.properties");
	protected static final Logger logger = Logger.getLogger(DiscoLoader.class.getName());

	private static DISCO englishDisco;
	private static DISCO germanDisco;
	private static DISCO frenchDisco;

	// TODO real singleton
	static {
		initDisco();
	}

	private static String getEnvironmentVariable(String envVarName) {
		String envVarValue = System.getenv(envVarName);
		logger.log(Level.INFO, "Use Environment Variable: " + envVarName);
		if (envVarValue == null) {
			logger.log(Level.SEVERE, "Environment Variable not set: " + envVarName);
		}
		return envVarValue;
	}

	private static void initDisco() {
		String discoDePath = getEnvironmentVariable(properties.getProperty("disco.new.german"));
		String discoEnPath = getEnvironmentVariable(properties.getProperty("disco.new.english"));
		String discoFrPath = getEnvironmentVariable(properties.getProperty("disco.new.french"));

		try {
			germanDisco = new DISCO(discoDePath, false);
			frenchDisco = new DISCO(discoFrPath, false);
			englishDisco = new DISCO(discoEnPath, false);
		} catch (IOException | CorruptConfigFileException e) {
			logger.log(Level.SEVERE,
					"Problem loading Disco model. Please be sure that the models are correctly placed!", e);
		}
	}

	/**
	 * Returns the previously loaded DISCO model in the chosen language.
	 * 
	 * @param language
	 */
	public static DISCO getDiscoModel(Language language) {
		switch (language) {
		case DE:
			return germanDisco;
		case FR:
			return frenchDisco;
		default:
			return englishDisco;
		}
	}

}
