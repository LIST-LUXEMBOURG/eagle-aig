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
package lu.list.itis.dkd.assess.opennlp.connectives;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lu.list.itis.dkd.assess.opennlp.util.Type;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class FrenchConnectives {

    private static List<String> contrast = Arrays.asList(
                    "cependant", "pourtout", "d'un coté", "de l'autre coté", "pare contre", "évidemment",
                    "contraste avec", "quand même", "pourtant", "néanmoins", "au contraire", "malgré");
    
    private static List<String> similarity = Arrays.asList(
                    "parail", "même façon", "façon similaire", "proportionnellement", "conséquence");
    
    private static List<String> result = Arrays.asList(
                    "par conséquent", "ainsi", "donc", "alors", "bien que", "quoique", "par conséquent",
                    "en conséquence", "en raison de", "à cause de");
    
    private static List<String> sequence = Arrays.asList(
                    "enfin", "à la fin", "ailleurs", "aussi", "pour commencer", "tout d'abord", "en plus");
    
    private static List<String> order = Arrays.asList(
                    "surtout", "essentiellement", "principalement", "considérablement", "significativement",
                    "fondamentalement", "en gros", "dès que");
    
    private static List<String> particulation = Arrays.asList("particulièrement ", "surtout");
    
    private static List<String> examplification = Arrays.asList("par example", "par contre");
    
    private static List<String> explanation = Arrays.asList(
                    "c'est-à-dire", "en d'autres termes", "autrement", "sinon", "différemment");
    
    private static List<String> emphasising = Arrays.asList("en fait", "en général", "en réalité", "effectivement");
    
    private static List<String> linking = Arrays.asList(
                    "même si", "à part", "alors que", "regarding", "à propos", "au sujet de");
    
    private static List<String> conclusion = Arrays.asList(
                    "pour conclure", "en conclusion", "pour résumer", "dans l'ensemble");
    
    private static List<String> correction = Arrays.asList("à cause de", "plutôt", "plus précisément");
    
    private static List<String> time = Arrays.asList(
                    "d'abort", "ensuite", "puis", "après", "aussitôt", "dès", "entretemps", 
                    "pendant", "parfois", "quelquefois");
    
    private static List<String> dismissial = Arrays.asList("quand même", "de toute façon");
    
    private static List<String> causality = Arrays.asList(
                    "car", "parce que", "puisque", "comme", "lorsque", "quand", "pendant que",
                    "tandis que", "pendant que");
    
    static Map<Type.Connective, String> getConnectives(String sentence){
        Map<Type.Connective, String> connectiveTypes = new HashMap<>();
        
        String lowerCaseSentence = sentence.toLowerCase();
        for (String type : causality) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.CAUSALITY, type);
            }
        }
        
        for (String type : conclusion) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.CONCLUSION, type);
            }
        }
        
        for (String type : contrast) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.CONTRAST, type);
            }
        }
        
        for (String type : correction) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.CORRECTION, type);
            }
        }
        
        for (String type : dismissial) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.DISMISSIAL, type);
            }
        }
        
        for (String type : emphasising) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.EMPHASISING, type);
            }
        }
        
        for (String type : examplification) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.EXAMPLIFICATION, type);
            }
        }
        
        for (String type : explanation) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.EXPLANATION, type);
            }
        }
        
        for (String type : linking) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.LINKING, type);
            }
        }
        
        for (String type : order) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.ORDER, type);
            }
        }
        
        for (String type : particulation) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.PARTICULATION, type);
            }
        }
        
        for (String type : result) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.RESULT, type);
            }
        }
        
        for (String type : sequence) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.SEQUENCE, type);
            }
        }
        
        for (String type : similarity) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.SIMILARITY, type);
            }
        }
        
        for (String type : time) {
            if (lowerCaseSentence.contains(type)) {
                connectiveTypes.put(Type.Connective.TIME, type);
            }
        }
        
        return connectiveTypes;
    }
}
