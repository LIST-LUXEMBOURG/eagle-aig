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
package lu.list.itis.dkd.assess.cloze.gap.approach;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class GapApproach {
    private int firstSentence = 0;
    private int numberOfDistractors = 3;
    private Language language;
    private List<Sentence> sentences = new ArrayList<>();
    private List<ClozeSentence> clozeSentences = new ArrayList<>();
    
    protected static final Logger logger = Logger.getLogger(GapApproach.class.getSimpleName());

    public GapApproach(Text text, int numberOfDistractors, boolean skipFirstSentence) {
        this.sentences = text.getSentences();
        this.language = sentences.get(0).getLanguage();  
        this.numberOfDistractors = numberOfDistractors;
        if (skipFirstSentence) {
            firstSentence = 1;
            logger.log(Level.INFO, "First sentence ignored for cloze generation.");
            ClozeSentence clozeSentence = new ClozeSentence(sentences.get(0));
            clozeSentences.add(clozeSentence);
        }
    }
    
    public GapApproach(Text text, int numberOfDistractors) {
        this.sentences = text.getSentences();
        this.language = sentences.get(0).getLanguage();  
        this.numberOfDistractors = numberOfDistractors;
    }
    
    public GapApproach(Text text) {
        this.sentences = text.getSentences();
        this.language = sentences.get(0).getLanguage();  
    }
    
    public List<ClozeSentence> getAnnotatedClozeSentences() {
        return Annotation.getClozeSentences(sentences, numberOfDistractors, firstSentence);
    }
    
    public List<ClozeSentence> getAnnotatedClozeMatchSentences() {
        return AnnotationMatch.getClozeSentences(sentences, firstSentence);
    }
    
    public List<ClozeSentence> getNounClozeSentences() {
        return Noun.getClozeSentences(sentences, numberOfDistractors, firstSentence);
    }
    
    public List<ClozeSentence> getNounClozeMatchSentences() {
        return NounMatch.getClozeSentences(sentences, firstSentence);
    }
    
    public List<ClozeSentence> getVerbClozeSentences() {
        return Verb.getClozeSentences(sentences, numberOfDistractors, firstSentence);
    }
    
    public List<ClozeSentence> getVerbClozeMatchSentences() {
        return VerbMatch.getClozeSentences(sentences, firstSentence);
    }
    
    public List<ClozeSentence> getCTest() {
        for (int i = firstSentence; i < sentences.size(); i++) {
            clozeSentences.add(CTest.getClozeSentence(sentences.get(i)));
        }
        return clozeSentences;
    }
    
    public List<ClozeSentence> getDefinitionClozeSentences() {
        clozeSentences = Definition.getClozeSentences(sentences, numberOfDistractors, language);
        if (clozeSentences.isEmpty()) {
            logger.log(Level.INFO, "No definitions found in the text. Be sure you entered an html based text. Hence, nouns are used!");
            return getNounClozeSentences();
        }
        return clozeSentences;
    }
}
