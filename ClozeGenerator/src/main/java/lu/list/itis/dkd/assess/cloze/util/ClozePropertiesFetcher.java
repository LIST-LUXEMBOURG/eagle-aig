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

/**
 * Helper class used to fetch properties for different classes or occasions.
 *
 * @author Eric TOBIAS [eric.tobias@list.lu]
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ClozePropertiesFetcher {
	private static final String CLOZE_PROPERTIES_FILE = "cloze.properties";
	 private static final Properties properties = new java.util.Properties();
	
	static {
		Logger.getLogger(ClozePropertiesFetcher.class.getSimpleName()).log(Level.INFO, "Load: "+CLOZE_PROPERTIES_FILE);
        try {
        	properties.load(Resources.class.getResourceAsStream(CLOZE_PROPERTIES_FILE));
        } catch (final IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
	
	
    /**
     * Method loading a given property file. The properties must be located at <code>config/</code>
     * for the class loader to find.
     *
     * @param location
     *        The name of the properties file to load.
     * @return The property file.
     */
    public static Properties fetchProperties(final String location) {  
    	return properties;
    }
}