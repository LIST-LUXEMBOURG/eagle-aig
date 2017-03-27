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

import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.template.ClozeItem;
import lu.list.itis.dkd.assess.cloze.template.ClozeItems;
import lu.list.itis.dkd.assess.cloze.template.Template;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

public class TestTenseTemplate {
    
    public static void main(String[] args) throws IOException {
        String text = Wrapper.loadTextFile(TestResources.class.getResourceAsStream("RAW/PC(EN) - UTF8.txt"));
        ClozeText clozeText = new ClozeText(text, Language.EN, Approach.VERB);
        ClozeItems clozeItems = new ClozeItems(clozeText);
        
        int templateNumber = 1;
        for (ClozeItem clozeItem : clozeItems.getClozeItems()) {
            String name = "VerbClozePC" + templateNumber;
            Template template = new Template(name , clozeItem, false);
            template.save("Examples\\Template\\Verb\\", name);
            templateNumber++;
        }
    }

}
