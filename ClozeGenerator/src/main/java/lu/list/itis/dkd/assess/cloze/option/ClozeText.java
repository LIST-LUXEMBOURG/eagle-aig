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
package lu.list.itis.dkd.assess.cloze.option;

import lu.list.itis.dkd.assess.cloze.gap.GapFinder;
import lu.list.itis.dkd.assess.cloze.generation.DistractorGenerator;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class ClozeText extends Text {
    private String clozeText = ""; 
    private Approach approach;
    private int numberOfDistractors = 3;
    private boolean skipFirstSentence = false;
    private List<Key> clozeKeys = new ArrayList<>();
    private List<ClozeSentence> clozeSentences = new ArrayList<>();

    protected static final Logger logger = Logger.getLogger(ClozeText.class.getSimpleName());
    
    /**
     * Creates a cloze text of a String.
     * @param text: A chosen String or .txt file.
     * @param language: EN, FR or DE
     * @param approach: Koda or Nouns
     */
    public ClozeText(String text, Language language, Approach approach) {
        super(text, language); 
        this.approach = approach;
        
        logger.log(Level.INFO, "Transforming the input text to a cloze text with the following configurations:\n" + loggerOutput());   
        GapFinder gapFinder = new GapFinder(this, approach);
        createClozeText(gapFinder);
    }
    
    /**
     * Creates a cloze text of a String.
     * @param text
     * @param language
     * @param approach
     * @param skipFirstSentence
     */
    public ClozeText(String text, Language language, Approach approach, boolean skipFirstSentence) {       
        super(text, language);        
        this.approach = approach; 
        
        this.skipFirstSentence = skipFirstSentence;
        logger.log(Level.INFO, "Transforming the input text to a cloze text with the following configurations:\n" + loggerOutput());   
        GapFinder gapFinder = new GapFinder(this, approach, skipFirstSentence);
        createClozeText(gapFinder);
    }
    
    /**
     * Creates a cloze text of a String.
     * @param text: A chosen String or .txt file.
     * @param language: EN, FR or DE
     * @param approach: Koda or Nouns
     * @param numberOfDistractors: A value to specifiy how many distractors per gap are needed. Warning, 
     * the larger the number, the less gaps may be created (Standard is 3). 
     * @param skipFirstSentence: First sentence is not considered if this is set to true.
     */
    public ClozeText(String text, Language language, Approach approach, int numberOfDistractors, boolean skipFirstSentence) {
        super(text, language);       
        this.approach = approach;
        
        this.numberOfDistractors = numberOfDistractors;
        this.skipFirstSentence = skipFirstSentence;
        logger.log(Level.INFO, "Transforming the input text to a cloze text with the following configurations:\n" + loggerOutput());   
        GapFinder gapFinder = new GapFinder(this, approach, numberOfDistractors, skipFirstSentence);
        createClozeText(gapFinder);
    }
    
    /**
     * Creates a cloze text of a String.
     * @param text: A Html based String.
     * @param bodyClassName: The body class name where the main text begins.
     * @param language: EN, FR or DE
     * @param approach: Definition, Nouns, Koda, Verb
     */
    public ClozeText(String text, String bodyClassName, Language language, Approach approach) {
        super(text, bodyClassName, language);
        this.approach = approach;
        
        logger.log(Level.INFO, "Transforming the input text to a cloze text with the following configurations:\n" + loggerOutput());
        GapFinder gapFinder = new GapFinder(this, approach);
        createClozeText(gapFinder);
    }
    
    /**
     * Creates a cloze text of a String.
     * @param text: A Html based String.
     * @param bodyClassName: The body class name where the main text begins.
     * @param language: EN, FR or DE
     * @param approach: Definition, Nouns, Koda, Verb
     * the larger the number, the less gaps may be created (Standard is 3). 
     * @param skipFirstSentence: First sentence is not considered if this is set to true.
     */
    public ClozeText(String text, String bodyClassName, Language language, Approach approach, boolean skipFirstSentence) {
        super(text, bodyClassName, language);
        this.approach = approach;
        
        this.skipFirstSentence = skipFirstSentence;
        logger.log(Level.INFO, "Transforming the input text to a cloze text with the following configurations:\n" + loggerOutput());   
        GapFinder gapFinder = new GapFinder(this, approach, skipFirstSentence);
        createClozeText(gapFinder);
    }
    
    /**
     * Creates a cloze text of a String.
     * @param text: A Html based String.
     * @param bodyClassName: The body class name where the main text begins.
     * @param language: EN, FR or DE
     * @param approach: Definition, Nouns, Koda, Verb
     * @param numberOfDistractors: A value to specifiy how many distractors per gap are needed. Warning, 
     * the larger the number, the less gaps may be created (Standard is 3). 
     * @param skipFirstSentence: First sentence is not considered if this is set to true.
     */
    public ClozeText(String text, String bodyClassName, Language language, Approach approach, int numberOfDistractors, boolean skipFirstSentence) {
        super(text, bodyClassName, language);
        this.approach = approach;
        
        this.numberOfDistractors = numberOfDistractors;
        this.skipFirstSentence = skipFirstSentence;
        logger.log(Level.INFO, "Transforming the input text to a cloze text with the following configurations:\n" + loggerOutput());   
        GapFinder gapFinder = new GapFinder(this, approach, numberOfDistractors, skipFirstSentence);
        createClozeText(gapFinder);
    }
    
    private void createClozeText(GapFinder gapFinder) {
        clozeSentences.addAll(gapFinder.getClozeSentences());
        for (ClozeSentence clozeSentence : clozeSentences) {
            clozeText += clozeSentence.getContent() + " ";
            clozeKeys.addAll(clozeSentence.getKeys());
        }
        clozeText = clozeText.trim();
    }
    
    private String loggerOutput() {
        int numberOfDisabledOptions = 0;
        String disabledOptions = "";
        String output = "\t-" + numberOfDistractors + " distractors per gap.\n";
        
        
        if (!skipFirstSentence) {
            output += "\t-The first sentence is considered in the gap generation process.\n";
        }
        
        if (DistractorGenerator.usesDependency()) {
            output += "\t-A Dependency Webservice (" + DistractorGenerator.getDependencyWebserviceUrl() + ") is used to verify the grammatical structure of the created cloze sentence.\n";
        }
        else {
            numberOfDisabledOptions++;
            disabledOptions += "dependency grammer check, ";
        }
        
        if (DistractorGenerator.usesDistractorArticleSearch()) {
            output += "\t-NGrams are used to find the most used distractor article. Then the distractor article can be compared to the extracted key's article.\n";
        }
        else {
            numberOfDisabledOptions++;
            disabledOptions += "distractor article search, ";
        }
        
        if (DistractorGenerator.usesKeyArticleSearch()) {
            output += "\t-NGrams are used to find the most used key article if no article is found in the text. This option is only worth in combination with the distractor article search enabled.\n";
        }
        else {
            numberOfDisabledOptions++;
            disabledOptions += "key article search, ";
        }
        
        if (DistractorGenerator.usesSoundex()) {
            output += "\t-Soundex is used to remove similar distractors with the same phoenetic pattern as the extracted key.\n";
        }
        else {
            numberOfDisabledOptions++;
            disabledOptions += "Soundex, ";
        }
        
        if (disabledOptions.contains(",")) {
            disabledOptions = disabledOptions.substring(0, disabledOptions.length()-1);
        }

        output += numberOfDisabledOptions + " options (" + disabledOptions + ") are disabled. Each option may increase the quality of the cloze item but also increases the computational time needed.";
        return output;
    }

    public List<Key> getClozeKeys() {
        return clozeKeys;
    }

    public List<ClozeSentence> getClozeSentences() {
        return clozeSentences;
    }

    public String getClozeText() {
        return clozeText;
    }
    
    public boolean isFirstSentenceSkipped(){
        return skipFirstSentence;
    }
    
    public Approach getApproach(){
        return approach;
    }

	public int getNumberOfDistractors() {
		return numberOfDistractors;
	}
}
