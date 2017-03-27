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

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;

/**
 * @author pfeiffer
 * @since 1.0
 * @version 1.0.0
 */
public class NameLoader {
    private static Properties properties = ClozePropertiesFetcher.fetchProperties("cloze.properties");
    protected static final Logger logger = Logger.getLogger(NameLoader.class.getSimpleName());

    private static NameFinderME nameFinder;
    private static NameFinderME organizationFinder;
    private static NameFinderME locationFinder;
    
    static {
        try {
            initFinders();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Model initialisation failed!", e);
        }
    }

    private static void initFinders() throws IOException{
        TokenNameFinderModel nameModel = new TokenNameFinderModel(Resources.class.getResourceAsStream(properties.getProperty("name.finder.english")));
        nameFinder = new NameFinderME(nameModel);
        
        TokenNameFinderModel organizationModel = new TokenNameFinderModel(Resources.class.getResourceAsStream(properties.getProperty("organization.finder.english")));
        organizationFinder = new NameFinderME(organizationModel);
        
        TokenNameFinderModel locationModel = new TokenNameFinderModel(Resources.class.getResourceAsStream(properties.getProperty("location.finder.english")));
        locationFinder = new NameFinderME(locationModel);
    }
    
    /**
     * Returns the previously loaded opennlp name identification Model.
     */
    public static NameFinderME getNameModel() {
        return nameFinder;
    }
    
    /**
     * Returns the previously loaded opennlp organization identification Model.
     */
    public static NameFinderME getOrganizationModel() {
        return organizationFinder;
    }
    
    /**
     * Returns the previously loaded opennlp location identification Model.
     */
    public static NameFinderME getLocationModel() {
        return locationFinder;
    }
    
    
}
