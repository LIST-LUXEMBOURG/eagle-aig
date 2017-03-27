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
package lu.list.itis.dkd.assess.cloze.template;

import java.io.IOException;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.template.ClozeItem;
import lu.list.itis.dkd.assess.cloze.template.ClozeItems;
import lu.list.itis.dkd.assess.cloze.template.Template;
import lu.list.itis.dkd.assess.cloze.util.TestResources;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Difficulty;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

public class TestComprehensionTemplate {
    
    protected static final Logger logger = Logger.getLogger(Template.class.getSimpleName());
    private static int numberOfDistractors = 3;
    
    private static void testEasy(ClozeText clozeText) throws IOException{
        ClozeItems clozeItems = new ClozeItems(clozeText, numberOfDistractors, Difficulty.EASY);
        
        int templateNumber = 1;
        for (ClozeItem clozeItem : clozeItems.getClozeItems()) {
            String name = "EasyClozePC" + templateNumber;
            Template template = new Template(name , clozeItem, false);
            template.save("Examples\\Template\\Easy\\", name);
            templateNumber++;
        }
    }
    
    private static void testMedium(ClozeText clozeText) throws IOException{
        ClozeItems clozeItems = new ClozeItems(clozeText, numberOfDistractors, Difficulty.MEDIUM);
        
        int templateNumber = 1;
        for (ClozeItem clozeItem : clozeItems.getClozeItems()) {
            String name = "MediumClozePC" + templateNumber;
            Template template = new Template(name , clozeItem, false);
            template.save("Examples\\Template\\Medium\\", name);
            templateNumber++;
        }
    }
    
    private static void testHard(ClozeText clozeText) throws IOException{
        ClozeItems clozeItems = new ClozeItems(clozeText, numberOfDistractors, Difficulty.HARD);
        
        int templateNumber = 1;
        for (ClozeItem clozeItem : clozeItems.getClozeItems()) {
            String name = "HardClozePC" + templateNumber;
            Template template = new Template(name , clozeItem, false);
            template.save("Examples\\Template\\Hard\\", name);
            templateNumber++;
        }
    }
    
    public static void main(String[] args) throws IOException {
        String text = Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt"));
        ClozeText clozeText = new ClozeText(text, Language.EN, Approach.ANNOTATION);
        testEasy(clozeText);
        testMedium(clozeText);
        testHard(clozeText);
    }
    
//    private static void print(QtiClozeItems clozeItems){
//        for (QtiClozeItem clozeItem : clozeItems.getClozeItems()) {
//            for (ClozeSentence clozeSentence : clozeItem.getClozeSentences()) {
//                System.out.println(clozeSentence.getClozeSentence());
//            }
//            System.out.println("\n");
//            for (QtiGap gap : clozeItem.getGaps()) {
//                System.out.print(gap.getKey().getKey().getWord() + ": ");
//                for (Distractor distractor : gap.getDistractors()) {
//                    System.out.print(distractor.getDistractor().getWord() + ", ");
//                }
//                System.out.println();
//            }
//            
//            System.out.println("\n\n ----------------------- \n\n");
//        }
//    }

}
