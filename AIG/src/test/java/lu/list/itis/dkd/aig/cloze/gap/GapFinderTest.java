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
package lu.list.itis.dkd.aig.cloze.gap;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.9
 * @version 0.9.0
 */
public class GapFinderTest {

    private static String text;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        GapFinderTest.text =
                        "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem!\nConventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved.\nMechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs).\nThe Soviet MIR series of computers was developed from 1965 to 1969 in a group headed by Pierre Vinken and Thomas Edison.";
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {}

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.cloze.gap.GapFinder#GapFinder(java.util.List, int, lu.list.itis.dkd.aig.cloze.util.ClozeVariable.Approach, boolean)}
     * .
     */
    @Test
    public final void testGapFinder() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.cloze.gap.GapFinder#findAnnotations()}.
     */
    @Test
    public final void testFindAnnotations() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.cloze.gap.GapFinder#populateExclusionWords(lu.list.itis.dkd.assess.opennlp.Sentence)}
     * .
     *
     * @throws IOException
     */
    @Test
    public final void testPopulateExclusionWords() throws IOException {
        final ClozeText clozeText = new ClozeText(GapFinderTest.text, Language.EN, Approach.NOUN);
        Assert.assertNotNull(clozeText);

        String result = clozeText.getClozeText() + "\n\n";
        for (final ClozeSentence clozeSentence : clozeText.getClozeSentences()) {
            for (final Key key : clozeSentence.getKeys()) {
                result += "Option " + key.getKeyNumber() + ": " + key.getKeyWord() + "(" +
                                key.getKeyWord().getTag() + ")";
                for (final Distractor distractor : key.getBestDistractors(3)) {
                    result += ", " + distractor.getDistractorWord() + "(" +
                                    distractor.getDistractorWord().getTag() + ")";
                }
                result = result.trim() + "\n";
            }
        }

        System.out.println(result);
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.cloze.gap.GapFinder#getClozeSentences()}.
     */
    @Test
    public final void testGetClozeSentences() {
        Assert.fail("Not yet implemented"); // TODO
    }

}
