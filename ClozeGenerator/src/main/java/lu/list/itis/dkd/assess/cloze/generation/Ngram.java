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

import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ArticleHelper;
import lu.list.itis.dkd.assess.cloze.util.UrlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0.0
 * @version 1.0.1
 */
public class Ngram {
    protected static final Logger logger = Logger.getLogger(Ngram.class.getSimpleName());
    static boolean useKeyArticleSearch = false;
    
    private static String googleNgramBody(Key key) {
        String sourcePage = UrlHelper.getSource(key.getKeyWord().getContent(), key.getKeyWord().getLanguage());
        if (sourcePage.equals("")) {
            return "";
        }
        
        //Google Ngram Body beginning and end.
        int beginning = sourcePage.indexOf("content=\"Google Ngram Viewer:")+30;
        int ending = sourcePage.indexOf("<meta itemprop=\"image\"");
        return sourcePage.substring(beginning, ending);
    }
    
    private static String parseNgram(Key key){
        String sourcePage = googleNgramBody(key);
        
        //Article pattern
        Pattern articlePattern = Pattern.compile("\\[" + "[a-zA-Z]+_DET" + "\\s" + key.getKeyWord().getContent() + "\\]");
        Matcher articleMatcher = articlePattern.matcher(sourcePage);
        
        //Searching best article and remove tag if found
        String articleMatch = "";
        while (articleMatcher.find()) {
            articleMatch = articleMatcher.group(0);
            articleMatch = articleMatch.substring(1, articleMatch.indexOf(" "));
            break;
        }
        return articleMatch.replace("_DET", "");
    }
    
    private static List<Distractor> parseNgram(Key key, List<Distractor> maxDistractors){
        List<Distractor> nGramDistractors = new ArrayList<>();
        
        String sourcePage = googleNgramBody(key);
        //Check article
        for (Distractor distractor : maxDistractors) {
            Pattern pattern = Pattern.compile("\\[" + "[a-zA-Z]+_DET" + "\\s" + distractor.getDistractorWord().getContent() + "\\]");
            Matcher matcher = pattern.matcher(sourcePage);
            
            //Searching best article and remove tag if found
            String articleMatch = "";
            while (matcher.find()) {
                articleMatch = matcher.group(0);
                articleMatch = articleMatch.substring(1, articleMatch.indexOf(" "));
                break;
            }
            articleMatch = articleMatch.replace("_DET", "");
            
            //Add distractor if most used article is the same as key article
            if (ArticleHelper.isSameArticle(key.getArticle(), articleMatch, key.getKeyWord().getLanguage())) {
                nGramDistractors.add(distractor);
            }
        }
        return nGramDistractors;
    }
    
    private static boolean keyArticle(Key key){
        String article = key.getArticle(); 
        if (!article.equals("")) {
            return true;
        }
        
        //Key has no article, try to get the most used one from Google Ngram Viewer!
        if (useKeyArticleSearch) {
            article = parseNgram(key); 
            //Set the Google Ngram Viever article to the key object.
            if (!article.equals("")) {
                key.setArticle(article);
                return true;
            }
            return false;    
        }
        return false; 
    }
    
    /**
     * Checks if the distractor article matches the key article by using a 2-Gram approach to find the most
     * used article for the distractor word by Google Ngram Viewer in a period of 200 years (1800 - 2000).
     * @param key
     * @param distractors
     * @param language
     * @return The list of distractors (List<Distractor>) which are left after the article check or 
     * null if all distractors in the list has a different most used article than the key. 
     */
    static List<Distractor> checkArticles(Key key, List<Distractor> distractors, int numberOfDistractors) {
        if (distractors.size() == 0 || distractors == null) {
            return distractors;
        }
        
        //Find most used article if no direct article was fount in the text.
        if (!keyArticle(key)) {
            return distractors;
        }
        
        List<Distractor> finalDistractors = new ArrayList<>();
        
        //Max 5 distractors can be checked at the same time.
        int max = 5;
        List<Distractor> maxDistractors = new ArrayList<>(); 
        for (int i = 0; i < distractors.size(); i++) {
            maxDistractors.add(distractors.get(i));
            max--;
            if (max == 0) {
                //connect to Google Ngram and parse the result.
                List<Distractor> nGramDistractors = parseNgram(key, maxDistractors);
                if (nGramDistractors == null) {
                    //Reset & continue
                    max = 5;
                    maxDistractors.clear();
                    continue;
                }
                finalDistractors.addAll(nGramDistractors);
                if (finalDistractors.size() >=3) {
                    logger.log(Level.INFO, finalDistractors.size() + " distractors articles already match the word " + key.getArticle() + " " + key.getKeyWord().getContent() + ". No need for further checks.");
                    return finalDistractors;
                }
                
                //Reset & continue
                max = 5;
                maxDistractors.clear();
                continue;
            }
        }
        
        //if max is not 0, check rest of the distractors
        if (maxDistractors.size() != 0 && maxDistractors != null) {
            if (numberOfDistractors - finalDistractors.size() - maxDistractors.size() >= 0) {
                return null;
            }
            
            List<Distractor> nGramDistractors = parseNgram(key, maxDistractors);
            if (nGramDistractors == null) {
                return null;
            }
            
            finalDistractors.addAll(nGramDistractors);
            return finalDistractors;
        }
                
        return null;
    }
}
