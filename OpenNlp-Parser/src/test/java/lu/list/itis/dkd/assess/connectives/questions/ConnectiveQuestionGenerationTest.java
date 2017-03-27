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

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.connectives.questions.Questions;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class ConnectiveQuestionGenerationTest {
    //Since...
    private static String since1 = "Half-court games require less cardiovascular stamina, since players do not need to run back and forth a full court.";
    private static String since2 = "Single wicket has rarely been played since limited overs cricket began.";

    //When
    private static String when1 = "The San−Francisco earthquake hit when resources in the field already were stetched.";
    private static String when2 = "Venice’s long decline started in the 15th century, when it first made an unsuccessful attempt to hold Thessalonica against the Ottomans (1423−1430).";
    private static String when3 = "Earthquake mainly occurs when the different blocks or plates that make up the Earth’s surface move relative to each other, causing distortion in the rock.";
    private static String when4 = "A one-point shot can be earned when shooting from the foul line after a foul is made.";
    
    //because
    private static String because1 = "I went there because I wanted to.";
    private static String because2 = "One-handed backhand players move to the net with greater ease than two-handed players because the shot permits greater forward momentum and has greater similarities in muscle memory to the preferred type of backhand volley (one-handed, for greater reach ).";
//  String because3 = "The United States sleep deprivation is common with students because almost all schools begin early in the morning and many of these students either choose to stay up awake late into the night or cannot do otherwise due to delayed sleep phase syndrome.";
    
    //As a result
    private static String result1 = "Someone called in a bomb threat. As a result, school was cancelled.";
    private static String result2 = "The United States sleep deprivation is common with students because almost all schools begin early in the morning and many of these students either choose to stay up awake late into the night or cannot do otherwise due to delayed sleep phase syndrome. As a result, students that should be getting between 8.5 and 9.25 hours of sleep are getting only 7 hours.";
    private static String result3 = "Studies showing the effects of sleep-deprivation on grades , and the different sleep patterns for teenagers. As a result a school in New Zealand , changed its start time to 10:30, in 2006, to allow students to keep to a schedule that allowed more sleep.";
    private static String result4 = "I have done a pranic healing course. As a result, I have been able to cure my neighbour’s sick cat.";
    
    //As a consequence
    private static String consequence1 = "Zack has skipped school on many occasions. As a consequence, he has failed his French test.";
    
    //Therefore
    private static String therefore1 = "We’re going to experience some meteor showers in the next few days. Therefore, the number of miraculous self-healings will rise.";
    
    //Thus - Problem (his, her, ...)
//  String string = "You didn’t tell me you wanted to come. Thus, we will not be taking you with us.";
    
    //For instance
    private static String instance1 = "In the case of the great grey kangaroo, for instance, the period of gestation is less than forty days.";
    
    //For example - Maybe answer can not be found within the text!
    private static String example1 = "I can play quite a few musical instruments, for example, the flute, the guitar, and the piano.";
    private static String example2 = "Slicing also causes the shuttlecock to travel much slower than the arm movement suggests, for example, a good cross court sliced drop shot will use a hitting action that suggests a straight clear or smash, deceiving the opponent about both the power and direction of the shuttlecock.";
    
    //However - Maybe answer can not be found within the text!
    private static String however1 = "His brother, Darian, however, tended to follow her around like a lost puppy.";
    private static String however2 = "This restaurant has the best kitchen in town. However, their staff are quite rude.";
    private static String however3 = "I’ve asked you a thousand times not to leave your dirty socks on the floor. However, you keep doing it.";
    
    //Also - Answer may not be fount within the text.
    private static String also1 = "Within a few minutes Fritz and Davis also joined them.";
    private static String also2 = "I want to talk to Prince Harry when I’m in England. Also, I want to meet his sister-in-law.";
    private static String also3 = "Also, they do not eat much.";
    private static String also4 = "Slicing also causes the shuttlecock to travel much slower than the arm movement suggests, for example, a good cross court sliced drop shot will use a hitting action that suggests a straight clear or smash, deceiving the opponent about both the power and direction of the shuttlecock.";
    
    //But - Answer may not be fount within the text.
    private static String but1 = "Alex was supposed to be sterile, but they had been wrong about that.";
    
    //Or
//  String string = "He said it would work for a girl or a boy.";
    
    //Although
    private static String although = "A bowler cannot bowl two successive overs, although a bowler can bowl unchanged at end for several overs.";
    
    //At first
    private static String first1 = "It was not a piece of cake to learn English. At first, I couldn’t pronounce all the words correctly. Afterwards, I had a hard time understanding the tenses.";
    
	@Test
    public void testSince1() {
		Text text = new Text(since1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why do half-court games require less cardiovascular stamina?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testSince2() {	
		Text text = new Text(since2, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why has single wicket rarely been played?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testAlso1() {	
		Text text = new Text(also1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why did within a few minutes Fritz and Davis joined them?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testAlso2() {	
		Text text = new Text(also2, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("When do you want to talk to Prince Harry?", questions.getQuestions().get(0).getContent());
		Assert.assertEquals("Why do you want to meet his sister-in-law?", questions.getQuestions().get(1).getContent());
	}
	
	@Test
	public void testAlso3() {	
		Text text = new Text(also3, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why do they not eat much?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testAlso4() {	
		Text text = new Text(also4, Language.EN);
		Questions questions = new Questions(text);
		
        //New TODO Why not will?
		Assert.assertEquals("Give an example why slicing also causes the shuttlecock to travel much slower than the arm movement suggests.", questions.getQuestions().get(0).getContent());
		Assert.assertEquals("Why do slicing causes the shuttlecock to travel much slower than the arm movement suggests?", questions.getQuestions().get(1).getContent());
		
		//Old 
//		Assert.assertEquals("Why will slicing causes the shuttlecock to travel much slower than the arm movement suggests?", connectives.getQuestions().get(1));
	}
	
	@Test
	public void testAlthough() {	
		Text text = new Text(although, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Can a bowler bowl unchanged at end for several overs?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testBecause1() {	
		Text text = new Text(because1, Language.EN);
		Questions questions = new Questions(text);
		
		//New TODO Why not will?
		Assert.assertEquals("Why did you went there?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testBecause2() {	
		Text text = new Text(because2, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why do one-handed backhand players move to the net with greater ease than two-handed players?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testBut1() {	
		Text text = new Text(but1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why had they been wrong about that?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testConsequence1() {	
		Text text = new Text(consequence1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why has he failed his French test?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testExample1() {	
		Text text = new Text(example1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Give an example why you can play quite a few musical instruments.", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testExample2() {	
		Text text = new Text(example2, Language.EN);
		Questions questions = new Questions(text);

		//New TODO Why not will
		Assert.assertEquals("Why do slicing causes the shuttlecock to travel much slower than the arm movement suggests?", questions.getQuestions().get(1).getContent());
		Assert.assertEquals("Give an example why slicing also causes the shuttlecock to travel much slower than the arm movement suggests.", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testHowever1() {	
		Text text = new Text(however1, Language.EN);
		Questions questions = new Questions(text);
		
		//New TODO Why not tended
		Assert.assertEquals("Why did his brother Darian tend to follow her around like a lost puppy?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testHowever2() {	
		Text text = new Text(however2, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why are their staff quite rude?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testHowever3() {	
		Text text = new Text(however3, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why do you keep doing it?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testInstance1() {	
		Text text = new Text(instance1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Give an instance where the period of gestation is less than forty days.", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testResult1() {	
		Text text = new Text(result1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why was school cancelled?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testResult2() {	
		Text text = new Text(result2, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why is the United States sleep deprivation common with students?", questions.getQuestions().get(0).getContent());
		Assert.assertEquals("Why should students that be getting between 8.5 and 9.25 hours of sleep are getting only 7 hours?", questions.getQuestions().get(1).getContent());
	}
	
	@Test
	public void testResult3() {	
		Text text = new Text(result3, Language.EN);
		Questions questions = new Questions(text);
		
		//New TODO Why changed?
		Assert.assertEquals("Why did a school in New Zealand changed its start time to 10:30, in 2006, to allow students to keep to a schedule that allowed more sleep?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testResult4() {	
		Text text = new Text(result4, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why have you been able to cure your neighbour’s sick cat?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testFirst1() {	
		Text text = new Text(first1, Language.EN);
		Questions questions = new Questions(text);			
		Assert.assertEquals("Why couldn’t you pronounce all the words correctly?", questions.getQuestions().get(0).getContent());
		Assert.assertEquals("Why did you had a hard time understanding the tenses?", questions.getQuestions().get(1).getContent());
		//TODO check why this was generated? Assert.assertTrue(connectives.getQuestions().contains("Why was it not a piece of cake to learn English?"));
	}
	
	@Test
	public void testTherefore1() {	
		Text text = new Text(therefore1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("Why will the number of miraculous self-healings rise?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testWhen1() {	
		Text text = new Text(when1, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("When did the San−Francisco earthquake hit?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testWhen2()  {	
		Text text = new Text(when2, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("When did venice’s long decline start in the 15th century?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testWhen3()  {	
		Text text = new Text(when3, Language.EN);
		Questions questions = new Questions(text);
		Assert.assertEquals("When do earthquake mainly occurs?", questions.getQuestions().get(0).getContent());
	}
	
	@Test
	public void testWhen4() {	
		Text text = new Text(when4, Language.EN);
		Questions questions = new Questions(text);		
		Assert.assertEquals("When can a one-point shot be earned?", questions.getQuestions().get(0).getContent());
	}

}
