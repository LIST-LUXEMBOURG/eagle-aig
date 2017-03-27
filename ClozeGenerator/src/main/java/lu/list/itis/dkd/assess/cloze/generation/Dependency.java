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
package lu.list.itis.dkd.assess.cloze.generation;

import java.util.ArrayList;
import java.util.List;

import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.util.UrlHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Dependency {
    static String dependencyWebserviceUrl = "http://eagle.list.lu:8080/Dependency_Webservice/";
    
    /**
     * Connects to the Dependency webservice and save the distractor if the key sentence grammar structure is equal with 
     * the distractor sentence grammar structure.
     * @param sentence
     * @param key
     * @param distractors
     * @param language
     * @return Returns a list of distractors that match with the key.
     */
    public static List<Distractor> checkDependency(String sentence, String key, List<Distractor> distractors, Language language){
        List<Distractor> finalDistractors = new ArrayList<>();
        String url = dependencyWebserviceUrl;
        
        //Transform language object to string language.
        String languageString;
        switch (language) {
            case DE:
                languageString = "de";
                break;
            case FR:
                languageString = "fr";
                break;
            case EN:
                languageString = "en";
                break;
            default:
                languageString = "en";
                break;
        }
        
        //Connect to the Dependency webservice and save the distractor if the key sentence grammar structure is equal with 
        //the distractor sentence grammar structure.
        for (Distractor distractor : distractors) {
            url += "dependency/match?sentence=" + sentence + "&key=" + key + "&distractor=" + distractor.getDistractorWord().getContent() + "&language=" + languageString;
            
            String result = UrlHelper.getSource(url);
            if (result.equals("true")) {
                finalDistractors.add(distractor);
            }
        }
        
        return finalDistractors;
    }
}
