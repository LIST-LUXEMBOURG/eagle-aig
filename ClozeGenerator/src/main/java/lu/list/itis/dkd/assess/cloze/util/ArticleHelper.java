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

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author pfeiffer
 * @since 1.0
 * @version 1.0.0
 */
public class ArticleHelper {
    protected static final Logger logger = Logger.getLogger(ArticleHelper.class.getSimpleName());
    
    private static List<String> germanArticles = Arrays.asList("ein", "der", "das", "eine", "die", "eines", "des", "einem", "dem", "einen", "den", "einer", "der", "dessen", "deren");
    private static List<String> frenchArticles = Arrays.asList("un", "le", "une", "la", "les", "des");
    
    public static boolean isArticle(Word word) {
        return isArticle(word.getContent(), word.getLanguage());
    }
    
    public static boolean isArticle(String word, Language language) {
        String temp = word.toLowerCase();
        switch (language) {
            case DE:
                if (germanArticles.contains(temp)){
                    return true;
                }
                return false;
            case FR:
                if (frenchArticles.contains(temp)){
                    return true;
                }
                return false;
            case EN:
                if (temp.equals("a") || temp.equals("the")) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
    
    private static boolean germanArticle(String lowerCaseKeyArticle, String lowerCaseDistractorArticle){
        if (lowerCaseDistractorArticle.equals("")) {
            return false;
        }
        
        switch (lowerCaseKeyArticle) {
            case "":
                return false;
            case "ein":
                if (lowerCaseDistractorArticle.equals("der") || lowerCaseDistractorArticle.equals("das") || lowerCaseDistractorArticle.equals("ein")) {
                    return true;
                }
                return false;
            case "eine":
                if (lowerCaseDistractorArticle.equals("die") || lowerCaseDistractorArticle.equals("eine")) {
                    return true;
                }
                return false;
            case "eines":
                if (lowerCaseDistractorArticle.equals("des") || lowerCaseDistractorArticle.equals("eines")) {
                    return true;
                }
                return false;
            case "einem":
                if (lowerCaseDistractorArticle.equals("dem") || lowerCaseDistractorArticle.equals("einem")) {
                    return true;
                }
                return false;
            case "einen":
                if (lowerCaseDistractorArticle.equals("den") || lowerCaseDistractorArticle.equals("einen")) {
                    return true;
                }
                return false;
            case "einer":
                if (lowerCaseDistractorArticle.equals("der") || lowerCaseDistractorArticle.equals("einer")) {
                    return true;
                }
                return false;
            case "dessen":
                if (lowerCaseDistractorArticle.equals("der") || lowerCaseDistractorArticle.equals("die") || lowerCaseDistractorArticle.equals("das")) {
                    return true;
                }
                return false;
            case "deren":
                if (lowerCaseDistractorArticle.equals("der") || lowerCaseDistractorArticle.equals("die") || lowerCaseDistractorArticle.equals("das")) {
                    return true;
                }
                return false;
            case "denen":
                if (lowerCaseDistractorArticle.equals("die")) {
                    return true;
                }
                return false;
            default:
                if (lowerCaseDistractorArticle.equals(lowerCaseKeyArticle)) {
                    return true;
                }
                logger.log(Level.INFO, lowerCaseKeyArticle + " does not match " + lowerCaseDistractorArticle);
                return false;
        }
    }    
    
    private static boolean frenchArticle(String lowerCaseKeyArticle, String lowerCaseDistractorArticle){
        if (lowerCaseDistractorArticle.equals("")) {
            return false;
        }
        
        switch (lowerCaseKeyArticle) {
            case "":
                return false;
            case "un":
                if (lowerCaseDistractorArticle.equals("le") || lowerCaseDistractorArticle.equals("un")) {
                    return true;
                }
                return false;
            case "une":
                if (lowerCaseDistractorArticle.equals("la") || lowerCaseDistractorArticle.equals("une")) {
                    return true;
                }
                return false;
            case "les":
                if (lowerCaseDistractorArticle.equals("les") || lowerCaseDistractorArticle.equals("des")) {
                    return true;
                }
                return false;
        }
        
        if (lowerCaseDistractorArticle.equals(lowerCaseKeyArticle)) {
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the key Article and the distractor article could be seen as equal. This algorithm also
     * consider indefinite articles the same as definite articles (e.g. eine (de) and die (de)).  
     * @param keyArticle
     * @param distractorArticle
     * @param language
     * @return Returns true if both article match or false otherwise.
     */
    public static boolean isSameArticle(String keyArticle, String distractorArticle, Language language){
        String lowerCaseKeyArticle = keyArticle.toLowerCase();
        String lowerCaseDistractorArticle = distractorArticle.toLowerCase();
        
        switch (language) {
            case DE:
                return germanArticle(lowerCaseKeyArticle, lowerCaseDistractorArticle);
            case FR:
                return frenchArticle(lowerCaseKeyArticle, lowerCaseDistractorArticle);
            default:
                return true;
        }
    }

}
