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
package lu.list.itis.dkd.assess.opennlp.stemmer;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class StemmerTest {
    @Test
    public void germanStemmerTest() {
        //Examples from http://snowball.tartarus.org/algorithms/german/stemmer.html
        Word word1 = new Word("aufeinander", "NN", 1, Language.DE);
        Assert.assertEquals("aufeinand", word1.getStem());
        
        Word word2 = new Word("kategorischer", "NN", 1, Language.DE);
        Assert.assertEquals("kategor", word2.getStem());
    }
    
    @Test
    public void frenchStemmerTest() {
        //Examples from http://snowball.tartarus.org/algorithms/french/stemmer.html
        Word word1 = new Word("continuation", "NN", 1, Language.FR);
        Assert.assertEquals("continu", word1.getStem());
        
        Word word2 = new Word("majestueusement", "NN", 1, Language.FR);
        Assert.assertEquals("majestu", word2.getStem());
    }
    
    @Test
    public void englishStemmerTest() {
        //Examples from http://snowball.tartarus.org/algorithms/french/stemmer.html
        Word word1 = new Word("consistently", "NN", 1, Language.EN);
        Assert.assertEquals("consist", word1.getStem());
        
        Word word2 = new Word("knocking", "NN", 1, Language.EN);
        Assert.assertEquals("knock", word2.getStem());
    }
}   
