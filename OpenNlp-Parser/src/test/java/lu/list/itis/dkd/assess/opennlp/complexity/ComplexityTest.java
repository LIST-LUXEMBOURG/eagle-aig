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
package lu.list.itis.dkd.assess.opennlp.complexity;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Type.DifficultyLevel;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class ComplexityTest {

	@Test
	public void EnglishComplexity() throws FileNotFoundException {
		Text text = new Text(TestResources.class.getResourceAsStream("resources/PC(EN) - UTF8.txt"), Language.EN);

		Assert.assertEquals(DifficultyLevel.EASY, text.getDifficulty());
	}
	
	@Test
    public void EnglishEasyPandaComplexity() throws FileNotFoundException {
	    String panda = "In China, while a panda was snoozing, an intruder entered its pen to touch its head, which interrupted the 12-year-old panda’s sleep, and it did not take kindly to the interruption! The 112-kilogram panda ran towards the man, grabbed his leg, and wrestled him to the ground. After 5 minutes of grappling, the intruder managed to escape without being hurt and without hurting the bear. The man was lucky to leave without two black eyes like his opponent’s!";
        Text text = new Text(panda, Language.EN);
        
        Assert.assertEquals(DifficultyLevel.EASY, text.getDifficulty());
    }
		
	@Test
    public void EnglishMediumCanadaComplexity() throws FileNotFoundException {
	    String canada = "Imagine yourself sitting in a café one day in your home town, when on the next table you hear some people speaking English with a strong North American accent. Being a friendly person, you lean over and say, \"Hi! Are you American?\" \"No,\" comes the immediate answer. Canadian! Calling a English-speaking Canadian an American can be as bad as telling a Scotsman that he's English or a Swiss person he's German. In spite of a common language, there are differences in culture and national feeling. Many Canadians will tell you with insistence. We're not Americans! We're Canadians! In the same way as Quebecers are determined to keep their identity, Canadians from the other provinces are determined to keep Canada's identity. Although the Canadian way of life is more and more like the American way of life, lots of details are different, and many Canadians, particularly Quebecers, are worried about the survival of their own differences. Canadians use metres and kilometres and measure temperatures in Celsius; Americans use feet and miles, and measure temperature in Fahrenheit. The USA has states, Canada has provinces. Yet about 80% of Canadians live within 150 km. of the U.S. border, and this has had a bad effect on the Canadian economy. Like most European countries, Canada has a national health service, and a good social security system; but good welfare services have to be paid for by high taxes, so the cost of living in Canada is high. Because of this, hundreds of thousands of Canadians often get in their cars and drive over to the USA to go shopping. This is one cause of economic problems in Canada. Over half of Canada's imports come from the United States, and Canada has a trade deficit with the USA. But the American influence is not just a question of shopping. Lots of Canadians drive American cars, and cars are almost as important in Canada as they are in the USA. There is television too. While Quebecers tend to watch their own French-language TV stations, English-speaking Canadians have a choice between local English-speaking channels, national programmes from CBC, and dozens of American channels brought to them by cable or satellite. Unless they specifically want to watch local stations, they're just as likely to tune in to one of the big American channels as they are to a Canadian channel. Perhaps it is not surprising if some Canadians are afraid that their country will soon be just like another part of the USA. If, one day, Quebec becomes independent, many Canadians fear that the rest of Canada could break up. Perhaps that's an exaggeration; many Canadians feel it is a real risk.";
        Text text = new Text(canada, Language.EN);
        
        System.out.println(text.getDifficulty());

        Assert.assertEquals(DifficultyLevel.MEDIUMEASY, text.getDifficulty());
    }

	@Test
	public void GermanComplexity() throws FileNotFoundException {
		Text text = new Text(TestResources.class.getResourceAsStream("resources/PC(DE) - UTF8.txt"), Language.DE);

		Assert.assertEquals(DifficultyLevel.MEDIUM, text.getDifficulty());
	}

	@Test
	public void FrenchComplexity() throws FileNotFoundException {
		Text text = new Text(TestResources.class.getResourceAsStream("resources/PC(FR) - UTF8.txt"), Language.FR);

		Assert.assertEquals(DifficultyLevel.EASY, text.getDifficulty());
	}
}
