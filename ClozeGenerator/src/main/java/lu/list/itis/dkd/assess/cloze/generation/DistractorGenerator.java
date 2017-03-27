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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.linguatools.disco.DISCO;
import de.linguatools.disco.ReturnDataBN;
import de.linguatools.disco.WrongWordspaceTypeException;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.DiscoLoader;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class DistractorGenerator {
    protected static final Logger logger = Logger.getLogger(DistractorGenerator.class.getSimpleName());
    private static boolean useDistractorArticleSearch = false;
    private static boolean useDependencies = false;
    
    private static List<Distractor> reduceDistractorSize(List<Distractor> distractors, int numberOfDistractors) {
        if (distractors == null) {
            return null;
        }
              
        if (distractors.size() < numberOfDistractors) {
            return null;
        }
          
        return distractors;
    }
    
    private static List<Distractor> evaluateDistractors(Key key, List<Distractor> distractors, Sentence sentence, int numberOfDistractors){
        if (distractors.size() >= numberOfDistractors) {
            //Order distractors
            Language language = sentence.getLanguage();
            List<Distractor> orderedDistractors = orderDistractors(distractors);
            List<Distractor> finalDistractors = orderedDistractors;
            
            //Remove distractors with a high potential bad article in German and French!
            if (useDistractorArticleSearch && language != Language.EN) {    
                logger.log(Level.INFO, orderedDistractors.size() + " found for " + key.getArticle() + " " + key.getKeyWord().getContent() + ". Appling article check.");
                finalDistractors = reduceDistractorSize(Ngram.checkArticles(key, orderedDistractors, numberOfDistractors), numberOfDistractors);
            }
            
            //Dependency Grammar check
            if (useDependencies) {
                finalDistractors = Dependency.checkDependency(sentence.getContent(), key.getKeyWord().getContent(), orderedDistractors, language);
            }
            
            return reduceDistractorSize(finalDistractors, numberOfDistractors);
        }
        return null;
    }

    private static List<Distractor> retrieveDistractors(Sentence sentence, Key key, int numberOfDistractors) {
        ArrayList<Distractor> distractors = new ArrayList<>();
        Language language = sentence.getLanguage();
        
        DISCO disco = DiscoLoader.getDiscoModel(language);
        ReturnDataBN keyResult;
        try {
            String keyWord = key.getKeyWord().getContent();
            keyResult = disco.similarWords(keyWord);

            // Compute second order similarity between the input word and its most similar words
            if (keyResult == null) {
                return null;
            }
            
            for ( String word : keyResult.words) {
                //Reject the word if evaluation fails
                if (!Evaluation.evaluateDistractor(key.getKeyWord().getContent(), word, language)) {
                    continue;
                }

                //TODO play test with the similarity measure from disco to improve the results! Start with 0.55
                double similarity = disco.secondOrderSimilarity(keyWord, word);
                if ((similarity > 0.50)) {
                    //Reject the distractor if similar distractor is in the list
                    if (!Evaluation.evaluateList(word, distractors)){
                        continue;
                    }
                
                    //If the distractor tag is different from the key tag, ignore the distractor.
                    Word distractorWord = new Word(word, 0, language);
                    Distractor distractor = new Distractor(distractorWord, similarity);
                    if (Tag.isSame(key, distractor)) {
                        distractors.add(distractor);
                    }                    
                }
            }
        } catch (WrongWordspaceTypeException e) {
            logger.log(Level.SEVERE, "WordspaceType mismatch!");
            e.printStackTrace();
        } catch (IOException e) {
            //TODO Better log!
            logger.log(Level.SEVERE, "DISCO could not be loaded properly.");
            e.printStackTrace();
        }
        
        return evaluateDistractors(key, distractors, sentence, numberOfDistractors);
    }

    /**
     * Returns a list of distractors if enough (numberOfDistractors) have been found or null otherwise.
     * @param sentence
     * @param key
     * @param numberOfDistractors
     * @return List<Distractor>
     * @throws IOException
     */
    public static List<Distractor> getDistractors(Sentence sentence, Key key, int numberOfDistractors) {
        List<Distractor> distractors = retrieveDistractors(sentence, key, numberOfDistractors);
        
        if (distractors == null || distractors.size() == 0) {
            return null;
        }
        
        return distractors;
        //TODO Not enough distractors were found, use the best distractor found as key to search for sdistractors
    }

    private static List<Distractor> orderDistractors(List<Distractor> distractors) {
        if (distractors == null || distractors.size() == 0) {
            return distractors;
        }
        
        List<Distractor> orderedDistractors = new ArrayList<>();
        List<Distractor> unorderedDistractors = new ArrayList<>();
        unorderedDistractors.addAll(distractors);
        
        Distractor bestDistractor;
        while (orderedDistractors.size() < distractors.size()) {
            bestDistractor = unorderedDistractors.get(0);
            for (int i = 1; i < unorderedDistractors.size(); i++) {
                double similarity = unorderedDistractors.get(i).getSimilarity(); 
                if (similarity > bestDistractor.getSimilarity()) {
                    bestDistractor = unorderedDistractors.get(i);
                }
            }
            
            orderedDistractors.add(bestDistractor);
            unorderedDistractors.remove(bestDistractor); 
            bestDistractor = null;
        }
        
        return orderedDistractors;
    }
    
    public static boolean usesDistractorArticleSearch() {
        return useDistractorArticleSearch;
    }
    
    public static void setDistractorArticleSearch(boolean distractorArticleSearch) {
        useDistractorArticleSearch = distractorArticleSearch;
    }
    
    public static boolean usesDependency() {
        return useDependencies;
    }
    
    public static void setDependencyUsage(boolean dependencyUsage) {
        useDependencies = dependencyUsage;
    }
    
    public static void useSoundex(boolean soundexUsage) {
        Evaluation.useSoundex = soundexUsage;
    }
    
    public static boolean usesSoundex() {
        return Evaluation.useSoundex;
    }
    
    public static void setDependencyWebserviceUrl(String url) {
        Dependency.dependencyWebserviceUrl = url;
    }
    
    public static String getDependencyWebserviceUrl() {
        return Dependency.dependencyWebserviceUrl;
    }
    
    public static void useKeyArticleSearch(boolean keyArticleSearch) {
        Ngram.useKeyArticleSearch = keyArticleSearch;
    }
    
    public static boolean usesKeyArticleSearch() {
        return Ngram.useKeyArticleSearch;
    }
    
}