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

//import is2.data.InstancesTagger;
//import is2.data.SentenceData09;
//import is2.io.CONLLReader09;
//import is2.lemmatizer.MFO;
//import is2.parser.Parser;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.util.ModelLoader;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class GermanDependency {
    private List<Integer> relationNumbers = new ArrayList<>();
    private List<String> relationTags = new ArrayList<>();
    
    public GermanDependency(Sentence sentence) {
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
        
//        for (int head : result.pheads) {
//            relationNumbers.add(head);
//        }
//        relationTags = Arrays.asList(result.plabels);
        
//        System.out.println("Forms: " + Arrays.toString(result.forms));
//        System.out.println("Lemmas: " + Arrays.toString(result.plemmas));
//        System.out.println("PPOS: " + Arrays.toString(result.ppos));
//        System.out.println("HEADS: " + Arrays.toString(result.heads));
//        System.out.println("PHEADS: " + Arrays.toString(result.pheads));
//        System.out.println("PLABELS: " + Arrays.toString(result.plabels));
//        System.out.println();
    }
    
    public List<Integer> getRelationNumbers() {
        return relationNumbers;
    }
    
    public List<String> getRelationTags(){
        return relationTags;
    }
}
