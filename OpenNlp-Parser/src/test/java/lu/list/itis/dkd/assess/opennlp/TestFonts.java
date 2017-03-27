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
package lu.list.itis.dkd.assess.opennlp;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Font;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.1.0
 * @version 2.1.0
 */
public class TestFonts {
   
    @Test
    public void fontTest() {
        String html = Wrapper.loadTextFile(TestResources.class.getResourceAsStream("resources/Glossary (EN).xml"));
        Text englishText = new Text(html, "wiki-body", Language.EN);        
        Font defaultFont = Font.DEFAULT;
        Font underline = Font.UNDERLINE;
        
        //Fonts in sentence 1
        Assert.assertEquals(underline, englishText.getSentences().get(0).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(0).getWords().get(1).getFont());
        for (int i = 2; i < englishText.getSentences().get(0).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(0).getWords().get(i).getFont());
        }
        
        //Fonts in sentnece 2
        Assert.assertEquals(underline, englishText.getSentences().get(1).getWords().get(0).getFont());
        for (int i = 1; i < englishText.getSentences().get(1).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(1).getWords().get(i).getFont());
        }
        
        //No Fonts in sentence 3
        for (int i = 0; i < englishText.getSentences().get(2).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(2).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 4
        Assert.assertEquals(underline, englishText.getSentences().get(3).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(3).getWords().get(1).getFont());
        for (int i = 2; i < englishText.getSentences().get(3).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(3).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 5
        Assert.assertEquals(underline, englishText.getSentences().get(4).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(4).getWords().get(1).getFont());
        for (int i = 2; i < englishText.getSentences().get(4).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(4).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 6
        Assert.assertEquals(underline, englishText.getSentences().get(5).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(5).getWords().get(1).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(5).getWords().get(2).getFont());
        for (int i = 3; i < englishText.getSentences().get(5).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(5).getWords().get(i).getFont());
        }
        
        //No fonts in sentence 7
        for (int i = 0; i < englishText.getSentences().get(6).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(6).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 8
        Assert.assertEquals(underline, englishText.getSentences().get(7).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(7).getWords().get(1).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(7).getWords().get(2).getFont());
        for (int i = 3; i < englishText.getSentences().get(7).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(7).getWords().get(i).getFont());
        }
        
        //No fonts in sentence 9
        for (int i = 0; i < englishText.getSentences().get(8).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(8).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 10
        Assert.assertEquals(underline, englishText.getSentences().get(9).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(9).getWords().get(1).getFont());
        for (int i = 2; i < englishText.getSentences().get(9).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(9).getWords().get(i).getFont());
        }
        
        //No fonts in sentence 11
        for (int i = 0; i < englishText.getSentences().get(10).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(10).getWords().get(i).getFont());
        }        
        
        //Fonts in sentence 12
        Assert.assertEquals(underline, englishText.getSentences().get(11).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(11).getWords().get(1).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(11).getWords().get(2).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(11).getWords().get(3).getFont());
        for (int i = 4; i < englishText.getSentences().get(11).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(11).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 13
        Assert.assertEquals(underline, englishText.getSentences().get(12).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(12).getWords().get(1).getFont());
        for (int i = 2; i < englishText.getSentences().get(12).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(12).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 14
        Assert.assertEquals(underline, englishText.getSentences().get(13).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(13).getWords().get(1).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(13).getWords().get(2).getFont());
        for (int i = 3; i < englishText.getSentences().get(13).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(13).getWords().get(i).getFont());
        }
        
        //No fonts in sentence 15
        for (int i = 0; i < englishText.getSentences().get(14).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(14).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 16
        Assert.assertEquals(underline, englishText.getSentences().get(15).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(15).getWords().get(1).getFont());
        for (int i = 2; i < englishText.getSentences().get(15).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(15).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 17
        Assert.assertEquals(underline, englishText.getSentences().get(16).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(16).getWords().get(1).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(16).getWords().get(2).getFont());
        for (int i = 3; i < englishText.getSentences().get(16).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(16).getWords().get(i).getFont());
        }
        
        //Fonts in sentenec 18
        Assert.assertEquals(underline, englishText.getSentences().get(17).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(17).getWords().get(1).getFont());
        for (int i = 2; i < englishText.getSentences().get(17).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(17).getWords().get(i).getFont());
        }
        
        //No fonts in sentence 19
        for (int i = 0; i < englishText.getSentences().get(18).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(18).getWords().get(i).getFont());
        }
        
        //No fonts in sentence 20
        for (int i = 0; i < englishText.getSentences().get(19).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(19).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 21
        Assert.assertEquals(underline, englishText.getSentences().get(20).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(20).getWords().get(1).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(20).getWords().get(2).getFont());
        for (int i = 3; i < englishText.getSentences().get(20).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(20).getWords().get(i).getFont());
        }
        
        //Fonts in sentence 22
        Assert.assertEquals(underline, englishText.getSentences().get(21).getWords().get(0).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(21).getWords().get(1).getFont());
        Assert.assertEquals(underline, englishText.getSentences().get(21).getWords().get(2).getFont());
        for (int i = 3; i < englishText.getSentences().get(21).getWords().size(); i++) {
            Assert.assertEquals(defaultFont, englishText.getSentences().get(21).getWords().get(i).getFont());
        }   
    }

}
