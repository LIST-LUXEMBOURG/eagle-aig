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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class FrenchDependency {

    /**
     * Returns the relation number (Hence the word number that relates to a Word) in an ordered list.
     * The number 0 is the root word.
     * E.g. Word1 relation number, Word2 relation number, ... 
     * @return
     */
    static List<Integer> getRelationNumbers(String graph){
        List<Integer> relations = new ArrayList<>();
        if (graph.equals("")) {
            return relations;
        }
             
        Pattern depPattern = Pattern.compile("\\((.*)->");
        Matcher depMatcher = depPattern.matcher(graph);
        while (depMatcher.find()) {
            int relation = Integer.parseInt(depMatcher.group(1).trim());            
            relations.add(relation);
        }
        
        return relations;
    }

    /**
     * @return Returns the relation tags of each word in a ordered list.
     * The tag root is the beginning.
     * E.g. Word1 tag, Word2 tag, ...
     */
    static List<String> getRelationTags(String graph){
        List<String> relations = new ArrayList<>();
        if (graph.equals("")) {
            return relations;
        }
        
        Pattern depPattern = Pattern.compile("I:(.*)\\)");
        Matcher depMatcher = depPattern.matcher(graph);
        
        while (depMatcher.find()) {
            String relation = depMatcher.group(1);
            
            if (relation.contains("DEPREL:")) {
                int beginning = relation.indexOf("DEPREL:");                
                relation = relation.substring(beginning, relation.length());
                
                if (relation.contains(" ")) {
                    relation = relation.substring(7, relation.indexOf(" "));
                    
                }
                else {
                    relation = "";
                }
            }
            //In the French parsing tree nothing is the root element. 
            //Hence, add "root" to relation list instead of "null"
            else {
                relation = "root";
            }
            
            relations.add(relation);
        }
            
        return relations;
    }
}
