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
public class GermanConnectives {

    private static List<String> contrast = Arrays.asList(
                    "jedoch", "trotz allem", "offen gesagt", "ebenso", "im gegensatz zu", 
                    "dennoch", "trotzdem", "allerdings", "einerseits", "nichtsdestotrotz",
                    "nichtsdestoweniger", "dessen ungeachtet");
    
    private static List<String> similarity = Arrays.asList(
                    "ebenfalls", "ebenso", "ähnlich", "in ähnlicher Weise", "in gleicher weise", 
                    "auch", "entsprechend");
    
    private static List<String> result = Arrays.asList(
                    "obwohl", "aber", "das bedeutet", "das hat zur folge", "deshalb", "deswegen", 
                    "daher", "folglich", "somit", "demzufolge", "aus diesem grund");
    
    private static List<String> sequence = Arrays.asList(
                     "überdies", "zusätzlich", "ergänzend", "weiterhin", "ausserdem", 
                     "zudem", "im übrigen", "schließlich", "letztlich", "ebenfalls");
    
    private static List<String> order = Arrays.asList(
                    "vor allem", "zunächst", "als nächstes", "anfangs", "fürs erste", 
                    "für den anfang", "im wesentlichen");
    
    private static List<String> particulation = Arrays.asList(
                    "insbesondere", "besonders", "ausdrücklich", "vorzugsweise", "zumal");
    
    private static List<String> examplification = Arrays.asList("etwa", "beispielsweise", "zum beispiel");
    
    private static List<String> explanation = Arrays.asList(
                    "mit anderen worten", "sonst", "ansonsten", "nämlich", "namentlich", "zwar");
    
    private static List<String> emphasising = Arrays.asList(
                    "in wirklichkeit", "eigentlich", "sogar", "in der tat", "genau genommen", "von haus aus");
    
    private static List<String> linking = Arrays.asList(
                    "im zusammenhang mit", "in bezug auf", "hinsichtlich", "angesichtss", "im hinblick auf");
    
    private static List<String> conclusion = Arrays.asList(
                    "schließlich", "abschließend", "schlussendlich", "zum abschluss", 
                    "kurz gesagt", "kurz dargestellt", "mit wenigen worten", "Zusammenfassend", 
                    "im großen und ganzen", "im großen ganzen");
    
    private static List<String> correction = Arrays.asList(
                    "eher", "vielmehr", "ziemlich", "eigentlich", "streng genommen", "genauer");
    
    private static List<String> time = Arrays.asList(
                    "zuerst", "später", "nachher", "früher", "inzwischen", "mittlerweile", 
                    "in der zwischenzeit", "zwischenzeitlich", "währenddessen", "zwischendurch",
                    "allmählich", "nach und nach", "manchmal", "dann", "oft", "seither", "seitdem");
    
    private static List<String> dismissial = Arrays.asList(
                    "jedenfalls", "trotzdem", "in jedem fall", "wie auch immer");
    
    private static List<String> causality = Arrays.asList("weil", "inzwischen", "zumal");
    
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


