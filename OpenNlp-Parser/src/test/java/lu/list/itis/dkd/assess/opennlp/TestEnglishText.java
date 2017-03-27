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

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.util.TestResources;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class TestEnglishText {
    
    //TODO use 3 different text lengths 10, 100 and 1000
    
    private static List<String> WORDS = Arrays.asList(
                    "A", "computer", "is", "a", "general-purpose", "device", "that", "can", "be",
                    "programmed", "to", "carry", "out", "a", "set", "of", "arithmetic", "or", 
                    "logical", "operations", "automatically", "Since", "a", "sequence", "of",
                    "operations", "can", "be", "readily", "changed", "the", "computer", "can",
                    "solve", "more", "than", "one", "kind", "of", "problem", "Conventionally",
                    "a", "computer", "consists", "of", "at", "least", "one", "processing", "element",
                    "typically", "a", "central", "processing", "unit", "CPU", "and", "some", "form",
                    "of", "memory", "The", "processing", "element", "carries", "out", "arithmetic",
                    "and", "logic", "operations", "and", "a", "sequencing", "and", "control", "unit",
                    "can", "change", "the", "order", "of", "operations", "in", "response", "to",
                    "stored", "information", "Peripheral", "devices", "allow", "information", "to",
                    "be", "retrieved", "from", "an", "external", "source", "and", "the", "result",
                    "of", "operations", "saved", "and", "retrieved", "Mechanical", "analog",
                    "computers", "started", "appearing", "in", "the", "first", "century", "and",
                    "were", "later", "used", "in", "the", "medieval", "era", "for", "astronomical",
                    "calculations", "In", "World", "War", "II", "mechanical", "analog", "computers",
                    "were", "used", "for", "specialized", "military", "applications", "such", "as",
                    "calculating", "torpedo", "aiming", "During", "this", "time", "the", "first",
                    "electronic", "digital", "computers", "were", "developed", "Originally", "they",
                    "were", "the", "size", "of", "a", "large", "room", "consuming", "as", "much",
                    "power", "as", "several", "hundred", "modern", "personal", "computers", "PCs"); 

    @Test
    public void plainTest() {
        String english = "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved. Mechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs).";
        Text englishText = new Text(english, Language.EN);
        Assert.assertEquals(
                        "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved. Mechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs).",
                        englishText.getContent());

        //Test words
        int i = 0;
        for (Sentence sentence : englishText.getSentences()) {
            for (Word word : sentence.getWords()) {
                Assert.assertEquals(word.getContent(), WORDS.get(i));
                i++;
            }
        }
    }
    
    @Test
    public void fileTest() throws FileNotFoundException {
        Text englishText = new Text(TestResources.class.getResourceAsStream("resources/PC(EN) - UTF8.txt"), Language.EN);
        
        //Paragraph 1
        Paragraph paragraph1 = englishText.getParagraph(1);
        Assert.assertEquals("A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically.", paragraph1.getSentences().get(0).getContent());
        Assert.assertEquals("Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem!", paragraph1.getSentences().get(1).getContent());
        
        //Paragraph2
        Paragraph paragraph2 = englishText.getParagraph(2);
        Assert.assertEquals("Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory.", paragraph2.getSentences().get(0).getContent());
        Assert.assertEquals("The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information.", paragraph2.getSentences().get(1).getContent());
        Assert.assertEquals("Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved.", paragraph2.getSentences().get(2).getContent());
        
        //Paragraph2
        Paragraph paragraph3 = englishText.getParagraph(3);
        Assert.assertEquals("Mechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations.", paragraph3.getSentences().get(0).getContent());
        Assert.assertEquals("In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming.", paragraph3.getSentences().get(1).getContent());
        Assert.assertEquals("During this time the first electronic digital computers were developed.", paragraph3.getSentences().get(2).getContent());
        Assert.assertEquals("Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs).", paragraph3.getSentences().get(3).getContent());
        
        //Paragraph Sentence has to be the same than text sentence
        Assert.assertEquals(paragraph1.getSentences().get(0).getContent(), englishText.getSentences().get(0).getContent());
        Assert.assertEquals(paragraph1.getSentences().get(1).getContent(), englishText.getSentences().get(1).getContent());
        Assert.assertEquals(paragraph2.getSentences().get(0).getContent(), englishText.getSentences().get(2).getContent());
        Assert.assertEquals(paragraph2.getSentences().get(1), englishText.getSentences().get(3));
        Assert.assertEquals(paragraph2.getSentences().get(2), englishText.getSentences().get(4));
        Assert.assertEquals(paragraph3.getSentences().get(0), englishText.getSentences().get(5));
        Assert.assertEquals(paragraph3.getSentences().get(1), englishText.getSentences().get(6));
        Assert.assertEquals(paragraph3.getSentences().get(2), englishText.getSentences().get(7));
        Assert.assertEquals(paragraph3.getSentences().get(3), englishText.getSentences().get(8));
    }

    @Test
    public void htmlTest() {
        String html = Wrapper.loadTextFile(TestResources.class.getResourceAsStream("resources/Glossary (EN).xml"));
        //TODO "header-title", 
        Text englishText = new Text(html, "wiki-body", Language.EN);        
        
        //TODO Don't understand why he does not remove &nbsp;. at the end...
        Assert.assertEquals("Change management: Approach and activities aimed at the management of change within an organisation.&nbsp;.", englishText.getSentences().get(0).getContent());
        Assert.assertEquals("Change: An act or process through which something becomes different.", englishText.getSentences().get(1).getContent());
        Assert.assertEquals("Change in an organisational context occurs when organisational strategy, major processes, structures, resources or procedures are altered.", englishText.getSentences().get(2).getContent());
        Assert.assertEquals("Radical change: fundamental, thoroughgoing or extreme change, usually taking place in a short time span.", englishText.getSentences().get(3).getContent());
        Assert.assertEquals("Incremental change: Relatively minor adjustment made toward an end result, often taking place gradually over a period of time.", englishText.getSentences().get(4).getContent());
        Assert.assertEquals("Change management stakeholder: Any person or group with an interest or concern in the implementation of change in your organisation.", englishText.getSentences().get(5).getContent());
        Assert.assertEquals("Stakeholders can be internal or external, individual or collective actors.", englishText.getSentences().get(6).getContent());
        Assert.assertEquals("Change management responsible: A person in charge of the change management in the organisation.", englishText.getSentences().get(7).getContent());
        Assert.assertEquals("His or her activities typically involve: identification, selection and involvement of key stakeholders throughout the change management process; facilitation of the analysis of initial situation to understand the context and potential levers for change; facilitation of the visioning process of the target situation; identification of key barriers to change management; organisation of a goal setting session; co-creation of a concrete change management Action Plan; supervision of the proper implementation and continuous progress monitoring of the Action Plan; ongoing and final evaluation of the change management success.", englishText.getSentences().get(8).getContent());
        Assert.assertEquals("Communication responsible: A person in charge of the communication activities related to the specific change management process.", englishText.getSentences().get(9).getContent());
        Assert.assertEquals("He or she is responsible for the development and monitoring of the communication on the change process toward the different actors of the organisation.", englishText.getSentences().get(10).getContent());
        Assert.assertEquals("Learning & development responsible ('Learning Concierge'): A person who is in charge of design, planning and implementation of learning and development activities in the organisation.", englishText.getSentences().get(11).getContent());
        Assert.assertEquals("Technology responsible: A person who manages the technical dimensions of the learning and knowledge sharing platform, and provides technical support to the users.", englishText.getSentences().get(12).getContent());
        Assert.assertEquals("Change management team: A team led by the change management responsible, including all persons actively involved in the change management process.", englishText.getSentences().get(13).getContent());
        Assert.assertEquals("Change management team in the context of new learning and knowledge sharing would typically involve team members responsible for communication, learning & development and technology.", englishText.getSentences().get(14).getContent());
        Assert.assertEquals("Top management: Highest management layer in the organisation, responsible for organisational strategy.", englishText.getSentences().get(15).getContent());
        Assert.assertEquals("Open Educational Practices (OEP): ”Practices which support the (re)use and production of open educational resources (OER) through institutional policies, promote innovative pedagogical models, and respect and empower learners as co- producers on their lifelong learning path” (OPAL, 2012, p.4).", englishText.getSentences().get(16).getContent());
        Assert.assertEquals("Organisational culture: Often referred to as “the way things are done around here”.", englishText.getSentences().get(17).getContent());
        Assert.assertEquals("It is a set of behaviours typical for the specific organisation and the meaning that people attach to those behaviours.", englishText.getSentences().get(18).getContent());
        Assert.assertEquals("Organisational culture includes elements such as values, norms, systems, symbols, language, assumptions, beliefs, and habits.", englishText.getSentences().get(19).getContent());
        Assert.assertEquals("Change management objectives: Specific objectives (goals) defined to successfully manage change in the organisation.", englishText.getSentences().get(20).getContent());
        Assert.assertEquals("Change management actions: All change management activities designed to help reach change management objectives.", englishText.getSentences().get(21).getContent());
    }
}
