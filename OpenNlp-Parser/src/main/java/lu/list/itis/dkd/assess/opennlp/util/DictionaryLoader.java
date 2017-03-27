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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.1.1
 * @version 1.0.0
 */
public class DictionaryLoader {
    private static List<String> germanWordList = new ArrayList<>();
    private static List<String> frenchWordList = new ArrayList<>();
    private static List<String> englishWordList = new ArrayList<>();
    
    protected static final Logger logger = Logger.getLogger(DictionaryLoader.class.getName());

    static {
        initDictionies();
    }
    
    private static void initDictionies (){
        germanWordList = Wrapper.loadListedTextFile(DictionaryLoader.class.getResourceAsStream("dictionaire/de/german.dic"), true);
        englishWordList = Wrapper.loadListedTextFile(DictionaryLoader.class.getResourceAsStream("dictionaire/en/english.txt"), true);
        frenchWordList = Wrapper.loadListedTextFile(DictionaryLoader.class.getResourceAsStream("dictionaire/fr/francais.txt"), true); 
    }
    
    public static List<String> getWordList(Language language){
        switch (language) {
            case EN:
                return getEnglishWordList();
            case DE:
                return getGermanWordList();
            case FR:
                return getFrenchWordList();
            default:
                return getEnglishWordList();
        }
    }
    
    public static List<String> getGermanWordList(){
        return germanWordList;
    }
    
    public static List<String> getEnglishWordList(){
        return englishWordList;
    }
    
    public static List<String> getFrenchWordList(){
        return frenchWordList;
    }
}
