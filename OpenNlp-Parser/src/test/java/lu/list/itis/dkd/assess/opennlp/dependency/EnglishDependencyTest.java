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

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class EnglishDependencyTest {

    @Test
    public void test1(){
        String phrase = "My dog also likes eating sausage.";
        Sentence sentence = new Sentence(phrase, 1, Language.EN);
        
        String relationTag1 = sentence.getWords().get(0).getRelationTag();
        String relationTag2 = sentence.getWords().get(1).getRelationTag();
        String relationTag3 = sentence.getWords().get(2).getRelationTag();
        String relationTag4 = sentence.getWords().get(3).getRelationTag();
        String relationTag5 = sentence.getWords().get(4).getRelationTag();
        String relationTag6 = sentence.getWords().get(5).getRelationTag();
        
        Assert.assertEquals("poss", relationTag1);
        Assert.assertEquals("nsubj", relationTag2);
        Assert.assertEquals("advmod", relationTag3);
        Assert.assertEquals("root", relationTag4);
        Assert.assertEquals("nn", relationTag5);
        Assert.assertEquals("dobj", relationTag6);
        
        int relationNumber1 = sentence.getWords().get(0).getRelationTo();
        int relationNumber2 = sentence.getWords().get(1).getRelationTo();
        int relationNumber3 = sentence.getWords().get(2).getRelationTo();
        int relationNumber4 = sentence.getWords().get(3).getRelationTo();
        int relationNumber5 = sentence.getWords().get(4).getRelationTo();
        int relationNumber6 = sentence.getWords().get(5).getRelationTo();
        
        Assert.assertEquals(2, relationNumber1);
        Assert.assertEquals(4, relationNumber2);
        Assert.assertEquals(4, relationNumber3);
        Assert.assertEquals(0, relationNumber4);
        Assert.assertEquals(6, relationNumber5);
        Assert.assertEquals(4, relationNumber6);
    }
}
