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

public class GermanDependencyTest {

    @Test
    public void test1(){
        String phrase = "Mein Hund frisst auch gerne Salami.";
        Sentence sentence = new Sentence(phrase, 1, Language.DE);
        
        String relationTag1 = sentence.getWords().get(0).getRelationTag();
        String relationTag2 = sentence.getWords().get(1).getRelationTag();
        String relationTag3 = sentence.getWords().get(2).getRelationTag();
        String relationTag4 = sentence.getWords().get(3).getRelationTag();
        String relationTag5 = sentence.getWords().get(4).getRelationTag();
        String relationTag6 = sentence.getWords().get(5).getRelationTag();
        
        Assert.assertEquals("NK", relationTag1);
        Assert.assertEquals("SB", relationTag2);
        Assert.assertEquals("root", relationTag3);
        Assert.assertEquals("MO", relationTag4);
        Assert.assertEquals("MO", relationTag5);
        Assert.assertEquals("OA", relationTag6);
        
        int relationNumber1 = sentence.getWords().get(0).getRelationTo();
        int relationNumber2 = sentence.getWords().get(1).getRelationTo();
        int relationNumber3 = sentence.getWords().get(2).getRelationTo();
        int relationNumber4 = sentence.getWords().get(3).getRelationTo();
        int relationNumber5 = sentence.getWords().get(4).getRelationTo();
        int relationNumber6 = sentence.getWords().get(5).getRelationTo();
        
        Assert.assertEquals(2, relationNumber1);
        Assert.assertEquals(3, relationNumber2);
        Assert.assertEquals(0, relationNumber3);
        Assert.assertEquals(5, relationNumber4);
        Assert.assertEquals(3, relationNumber5);
        Assert.assertEquals(3, relationNumber6);
    }

    @Test
    public void test2(){
        String phrase = "Ein Computer ist ein Digitalcomputer wenn er mit digitalen Geräteeinheiten digitale Daten verarbeitet also Zahlen und Textzeichen.";
        Sentence sentence = new Sentence(phrase, 1, Language.DE);
        
        String relationTag1 = sentence.getWords().get(0).getRelationTag();
        String relationTag2 = sentence.getWords().get(1).getRelationTag();
        String relationTag3 = sentence.getWords().get(2).getRelationTag();
        String relationTag4 = sentence.getWords().get(3).getRelationTag();
        String relationTag5 = sentence.getWords().get(4).getRelationTag();
        String relationTag6 = sentence.getWords().get(5).getRelationTag();
        String relationTag7 = sentence.getWords().get(6).getRelationTag();
        String relationTag8 = sentence.getWords().get(7).getRelationTag();
        String relationTag9 = sentence.getWords().get(8).getRelationTag();
        String relationTag10 = sentence.getWords().get(9).getRelationTag();
        String relationTag11 = sentence.getWords().get(10).getRelationTag();
        String relationTag12 = sentence.getWords().get(11).getRelationTag();
        String relationTag13 = sentence.getWords().get(12).getRelationTag();
        String relationTag14 = sentence.getWords().get(13).getRelationTag();
        String relationTag15 = sentence.getWords().get(14).getRelationTag();
        String relationTag16 = sentence.getWords().get(15).getRelationTag();
        String relationTag17 = sentence.getWords().get(16).getRelationTag();
        
        Assert.assertEquals("NK", relationTag1);
        Assert.assertEquals("PD", relationTag2);
        Assert.assertEquals("root", relationTag3);
        Assert.assertEquals("NK", relationTag4);
        Assert.assertEquals("PD", relationTag5);
        Assert.assertEquals("CP", relationTag6);
        Assert.assertEquals("SB", relationTag7);
        Assert.assertEquals("MO", relationTag8);
        Assert.assertEquals("NK", relationTag9);
        Assert.assertEquals("NK", relationTag10);
        Assert.assertEquals("NK", relationTag11);
        Assert.assertEquals("AG", relationTag12);
        Assert.assertEquals("CJ", relationTag13);
        Assert.assertEquals("MO", relationTag14);
        Assert.assertEquals("SB", relationTag15);
        Assert.assertEquals("CD", relationTag16);
        Assert.assertEquals("CJ", relationTag17);
        
        int relationNumber1 = sentence.getWords().get(0).getRelationTo();
        int relationNumber2 = sentence.getWords().get(1).getRelationTo();
        int relationNumber3 = sentence.getWords().get(2).getRelationTo();
        int relationNumber4 = sentence.getWords().get(3).getRelationTo();
        int relationNumber5 = sentence.getWords().get(4).getRelationTo();
        int relationNumber6 = sentence.getWords().get(5).getRelationTo();
        int relationNumber7 = sentence.getWords().get(6).getRelationTo();
        int relationNumber8 = sentence.getWords().get(7).getRelationTo();
        int relationNumber9 = sentence.getWords().get(8).getRelationTo();
        int relationNumber10 = sentence.getWords().get(9).getRelationTo();
        int relationNumber11 = sentence.getWords().get(10).getRelationTo();
        int relationNumber12 = sentence.getWords().get(11).getRelationTo();
        int relationNumber13 = sentence.getWords().get(12).getRelationTo();
        int relationNumber14 = sentence.getWords().get(13).getRelationTo();
        int relationNumber15 = sentence.getWords().get(14).getRelationTo();
        int relationNumber16 = sentence.getWords().get(15).getRelationTo();
        int relationNumber17 = sentence.getWords().get(16).getRelationTo();
        
        Assert.assertEquals(2, relationNumber1);
        Assert.assertEquals(3, relationNumber2);
        Assert.assertEquals(0, relationNumber3);
        Assert.assertEquals(5, relationNumber4);
        Assert.assertEquals(3, relationNumber5);
        Assert.assertEquals(8, relationNumber6);
        Assert.assertEquals(8, relationNumber7);
        Assert.assertEquals(13, relationNumber8);
        Assert.assertEquals(10, relationNumber9);
        Assert.assertEquals(8, relationNumber10);
        Assert.assertEquals(12, relationNumber11);
        Assert.assertEquals(10, relationNumber12);
        Assert.assertEquals(3, relationNumber13);
        Assert.assertEquals(13, relationNumber14);
        Assert.assertEquals(13, relationNumber15);
        Assert.assertEquals(15, relationNumber16);
        Assert.assertEquals(16, relationNumber17);
        
//        System.out.println("Tags");
//        for (String relation : sentence.getgetRelationTags()) {
//            System.out.println(relation);
//        }
//        
//        System.out.println("Numbers");
//        for (Integer relationNumber : dependency.getRelationNumbers()) {
//            System.out.println(relationNumber);
//        }
        
    }
}
