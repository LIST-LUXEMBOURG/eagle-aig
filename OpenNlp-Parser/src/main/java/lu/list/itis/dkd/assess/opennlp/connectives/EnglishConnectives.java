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
public class EnglishConnectives {
   private static List<String> contrast = Arrays.asList(
                    "however", "in contrast", "nevertheless", "nonetheless", "on the other hand", 
                    "on the contrary", "in spite of");
            
   private static List<String> similarity = Arrays.asList(
                    "likewise", "similarly", "correspondingly", "in the same way", "also");
            
   //Removed "so"
   private static List<String> result = Arrays.asList(
                    "although", "as a result", "as a consequence", "therefore", "thus",
                    "accordingly", "but", "hence", "consequently", "Due to", "implies", "imply");
         
   private static List<String> sequence = Arrays.asList(
                    "to begin with", "besides", "in addition", "furthermore", "moreover", "finally");
          
   private static List<String> order = Arrays.asList(
                    "primarily", "above all", "significantly", "essentially", 
                    "basically", "to begin with", "then", "last of all", 
                    "after", "before", "as soon as");

   private static List<String> particulation = Arrays.asList("in particular", "particularly");
            
   private static List<String> examplification = Arrays.asList("for example", "for instance");
          
   private static List<String> explanation = Arrays.asList("namely", "in other words", "otherwise");
          
   private static List<String> emphasising = Arrays.asList("in fact", "actually", "indeed");
            
   private static List<String> linking = Arrays.asList(
                    "with respect to", "regarding", "with regard to", "as regards", 
                    "talking of", "by comparision");

   private static List<String> conclusion = Arrays.asList(
                    "in conclusion", "in brief", "in summary", "to sum up", "all in all");

   private static List<String> correction = Arrays.asList("rather", "to be more precise");

   private static List<String> time = Arrays.asList(
                    "at first", "later", "in the meantime", "meanwhile", "afterwards", 
                    "sometimes", "then", "often");
         
   private static List<String> dismissial = Arrays.asList(
                    "anyway", "anyhow", "at any rate");
           
   private static List<String> causality = Arrays.asList("because", "since", "when");
   
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
