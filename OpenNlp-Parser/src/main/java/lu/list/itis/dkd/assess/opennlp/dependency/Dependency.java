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
package lu.list.itis.dkd.assess.opennlp.dependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

//import is2.data.InstancesTagger;
//import is2.data.SentenceData09;
//import is2.io.CONLLReader09;
//import is2.lemmatizer.MFO;
//import is2.parser.Parser;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.ModelLoader;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Dependency {
//    private String graph = "";
    private List<String> relationTags = new ArrayList<>();
    private List<Integer> relationNumbers = new ArrayList<>();
    
    protected static final Logger logger = Logger.getLogger(ModelLoader.class.getName());
    
    /**
     * Transforms the words into French conllx format.
     * @param sentence
     * @param numberOfWords
     */
    private void frenchFormat(Sentence sentence) {
        int numberOfWords = sentence.getNumberOfWords();
        String[] tokens = new String[numberOfWords];
        for (int i = 0; i < numberOfWords; i++) {
            String token = sentence.getWords().get(i).getContent();
            String tag = sentence.getWords().get(i).getTag();            
            tokens[i] = (i+1) + "\t" + token + "\t_\t" + tag + "\t" + tag;
            //Transform specific French noun and verb tags to a simple form "V" and "N".
            //The French Maltparser ressource does not understand the others!
            if (GrammarHelper.isNoun(tag, Language.FR)) {
                tokens[i] = (i+1) + "\t" + token + "\t_\tN\t" + tag;
            }
            else if (GrammarHelper.isVerb(tag, Language.FR)) {
                tokens[i] = (i+1) + "\t" + token + "\t_\tV\t" + tag;
            }
        }
        
        MaltParserService maltparser = ModelLoader.getDependencyModels(Language.FR);
        try {
            String graph = maltparser.parse(tokens).toString();
            relationTags = FrenchDependency.getRelationTags(graph);
            relationNumbers = FrenchDependency.getRelationNumbers(graph);
        }
        catch (MaltChainedException e) {
            logger.log(Level.SEVERE, "Dependency Parsing failed");
            e.printStackTrace();
        }
    }
    
    /**
     * Transforms the words into English conllx format.
     * @param sentence
     * @param numberOfWords
     */
    private void englishFormat(Sentence sentence) {
        int numberOfWords = sentence.getNumberOfWords();
        String[] tokens = new String[numberOfWords];
        
        //Form a MaltParser token
        for (int i = 0; i < numberOfWords; i++) {
            String token = sentence.getWords().get(i).getContent();
            String tag = sentence.getWords().get(i).getTag();
            tokens[i] = (i+1) + "\t" + token + "\t_\t" + tag + "\t" + tag;
        }
        
        MaltParserService maltparser = ModelLoader.getDependencyModels(sentence.getLanguage());
        try {
            String graph = maltparser.parse(tokens).toString();
            relationTags = EnglishDependency.getRelationTags(graph);
            relationNumbers = EnglishDependency.getRelationNumbers(graph);
        }
        catch (MaltChainedException e) {
            logger.log(Level.SEVERE, "Dependency Parsing failed", e);
        }
    }
    
    /**
     * Transforms the words into German Mate Parser format.
     * @param sentence
     * @param numberOfWords
     */
    private void germanFormat(Sentence sentence){
//        Parser parser = ModelLoader.getGermanDependencyModel(Language.DE);

        //Transform List of word to String array of Tokens and add a Root element.
//        CONLLReader09 reader = new CONLLReader09(CONLLReader09.NO_NORMALIZE);
        int numberOfWords = sentence.getNumberOfWords();
        String[] forms = new String[numberOfWords+1];
        String[] lemmas = new String[numberOfWords+1];
        String[] tags = new String[numberOfWords+1];
//        forms[0] = CONLLReader09.ROOT;
//        lemmas[0] = CONLLReader09.ROOT;
//        tags[0] = CONLLReader09.ROOT;
        for (int i = 1; i < numberOfWords+1; i++) {
            forms[i] = sentence.getWord(i).getContent();
            lemmas[i] = sentence.getWord(i).getLemma();
            tags[i] = sentence.getWord(i).getTag();
        }
        
        //Initialize the tagger and create a sen09 object 
//        InstancesTagger instanceTagger = new InstancesTagger();
//        instanceTagger.init(1, new MFO());        
//        SentenceData09 instance = new SentenceData09();
//        instance.init(forms);
//        instance.setLemmas(lemmas);
//        instance.setPPos(tags);
//        reader.insert(instanceTagger, instance);  
//        SentenceData09 result = parser.parse(instance, parser.params, false, parser.options);

//        relationTags = Arrays.asList(result.plabels);
//        for (int i = 0; i < result.pheads.length; i++) {
//            int head = result.pheads[i];
//            if (head == 0) {
//                relationTags.set(i, "root");
//            }
//            relationNumbers.add(head);
//        }
        
//        System.out.println("PHEADS: " + Arrays.toString(result.pheads));
//        System.out.println("PLABELS: " + Arrays.toString(result.plabels));
    }
    
    public Dependency(Sentence sentence){
        switch (sentence.getLanguage()) {
            case FR:
                frenchFormat(sentence);
                break;
            case EN:
                englishFormat(sentence);
                break;
            case DE:
                germanFormat(sentence);
                break;
        }
    }
    
    public List<String> getRelationTags(){
        return relationTags;
    }
    
    public List<Integer> getRelationNumbers(){
        return relationNumbers;
    }
    
}
