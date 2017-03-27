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
package lu.list.itis.dkd.assess.cloze.gap;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.cloze.gap.approach.GapApproach;
import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.opennlp.Text;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class GapFinder {
    protected static final Logger logger = Logger.getLogger(GapFinder.class.getSimpleName());
    private int numberOfDistractors = 3;
    private boolean skipFirstSentence = false;
    private List<ClozeSentence> clozeSentences = new ArrayList<>();
    
    /**
     * Returns gap sentences if one or more keys were fount based on the approach and configuration.
     * The number of distractors is three and the first sentence is used for gap generation.
     * @param sentence
     * @param approach: ANNOTATION, DEFINITION, VERB, CTEST or NOUN
     * @return gap sentence
     * @throws IOException
     */
    public GapFinder (Text text, Approach approach) {       
        createGaps(text, approach);
    }
    
    /**
     * Returns gap sentences if one or more keys were fount based on the approach and configuration.
     * The first sentence is used for gap generation.
     * @param sentence
     * @param approach: ANNOTATION, DEFINITION, VERB, CTEST or NOUN
     * @param numberOfDistractors: The number of distractors generated per key.
     * @return gap sentence
     * @throws IOException
     */
    public GapFinder (Text text, Approach approach, int numberOfDistractors) {
        this.numberOfDistractors = numberOfDistractors;
        createGaps(text, approach);
    }
    
    /**
     * Returns gap sentences if one or more keys were fount based on the approach and configuration.
     * The number of distractors is 3.
     * @param sentence
     * @param approach: ANNOTATION, DEFINITION, VERB, CTEST or NOUN
     * @param skipFirstSentence: The first sentence is skipped if set to true.
     * @return gap sentence
     * @throws IOException
     */
    public GapFinder (Text text, Approach approach, boolean skipFirstSentence) {
        this.skipFirstSentence = skipFirstSentence;
        createGaps(text, approach);
    }
    
    /**
     * Returns gap sentences if one or more keys were fount based on the approach and configuration.
     * @param sentence
     * @param approach: ANNOTATION, DEFINITION, VERB, CTEST or NOUN
     * @param numberOfDistractors: The number of distractors generated per key.
     * @param skipFirstSentence: The first sentence is skipped if set to true.
     * @return gap sentence
     * @throws IOException
     */
    public GapFinder (Text text, Approach approach, int numberOfDistractors, boolean skipFirstSentence) {
        this.numberOfDistractors = numberOfDistractors;
        this.skipFirstSentence = skipFirstSentence;
        createGaps(text, approach);
    }
    
    private void createGaps(Text text, Approach approach){
        GapApproach gapApproach = new GapApproach(text, numberOfDistractors, skipFirstSentence); 
        switch (approach) {
            case ANNOTATION:
                logger.log(Level.INFO, "Koda Annotations are used to find gaps.");
                clozeSentences = gapApproach.getAnnotatedClozeSentences();
                break;
            case DEFINITION:
                logger.log(Level.INFO, "Definitions (underlined words) are used to find gaps.");
                clozeSentences = gapApproach.getDefinitionClozeSentences();
                break;
            case VERB:
                logger.log(Level.INFO, "Verbs are used to find gaps.");
                clozeSentences = gapApproach.getVerbClozeSentences();
                break;
            case CTEST:
                logger.log(Level.INFO, "Every second word is shortened by half its length.");
                clozeSentences = gapApproach.getCTest();
                break;
            case NOUN:
                logger.log(Level.INFO, "Nouns are used to find gaps.");
                clozeSentences = gapApproach.getNounClozeSentences();
                break;
            case ANNOTATIONMATCH:
                logger.log(Level.INFO, "Koda Annotations are used to find gaps.");
                clozeSentences = gapApproach.getAnnotatedClozeMatchSentences();
                break;
            case NOUNMATCH:
                logger.log(Level.INFO, "Nouns are used to find gaps.");
                clozeSentences = gapApproach.getNounClozeMatchSentences();
                break;
            case VERBMATCH:
                logger.log(Level.INFO, "Verbs are used to find gaps.");
                clozeSentences = gapApproach.getVerbClozeMatchSentences();
                break;
        }
    }
    
    public List<ClozeSentence> getClozeSentences(){
        return clozeSentences;
    }
}
