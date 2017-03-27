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
package lu.list.itis.dkd.assess.connectives.questions;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class QuestionGenerationTest {

    @Test
    public void plainQuestionTest() {
        String english = "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved. Mechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs).";
        Text englishText = new Text(english, Language.EN);
        Assert.assertEquals("When can a sequence of operations be readily changed?", englishText.getQuestions().get(0).getContent());
        Assert.assertEquals("Why were mechanical analog computers started appearing in the first century and used in the medieval era for astronomical calculations?", englishText.getQuestions().get(1).getContent());
    }
    
    @Test
    public void sentenceQuestionTest() {
        String phrase = "I go home to my wife.";
        Sentence sentence = new Sentence(phrase, 1, Language.EN);
        
        for (Word word : sentence.getWords()) {
            System.out.println(word.getContent() + " - " + word.getTag());
        }
        
        Assert.assertEquals("Why do you go home to your wife?", sentence.generateQuestionFromSentence("Why").getContent());
        Assert.assertEquals("When do you go home to your wife?", sentence.generateQuestionFromSentence("When").getContent());
    }
}
