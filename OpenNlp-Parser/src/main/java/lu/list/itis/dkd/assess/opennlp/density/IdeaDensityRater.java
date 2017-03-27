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
package lu.list.itis.dkd.assess.opennlp.density;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
import java.util.ArrayList;
import java.util.List;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

// TODO To be tested!!
public class IdeaDensityRater {
    private int numberOfpropositions = 0;
    private List<Item> propositions = new ArrayList<>();
    
    public IdeaDensityRater(Text text) {
        numberOfpropositions = RateTaggedText(text.getTaggedText(), text.getLanguage(), false);
    }
    
    public IdeaDensityRater(Text text, boolean speechMode) {
        numberOfpropositions = RateTaggedText(text.getTaggedText(), text.getLanguage(), true);
    }

    public IdeaDensityRater(String taggedText, Language language) {
        numberOfpropositions = RateTaggedText(taggedText, language, false);
    }

    private int RateTaggedText(String taggedText, Language language, boolean speechMode) {
        int pc = 0;
        switch (language) {
            case DE:
                GermanWordList germanWordList = new GermanWordList(taggedText);
                germanWordList.applyIdeaCountingRules(false);
                for (Item i : germanWordList.getItems()) {
                    if (i.isProp()) {
                        propositions.add(i);
                        pc++;
                    }
                }
                return pc;
            case FR:
                FrenchWordList frenchWordList;
                frenchWordList = new FrenchWordList(taggedText);
                frenchWordList.applyIdeaCountingRules(speechMode);
                
                for (Item i : frenchWordList.getItems()) {
                    if (i.isProp()) {
                        propositions.add(i);
                        pc++;
                    }
                }
                return pc;
            case EN:
                EnglishWordList englishWordList = new EnglishWordList(taggedText);
                englishWordList.applyIdeaCountingRules(speechMode);

                for (Item i : englishWordList.getItems()) {
                    if (i.isProp()) {
                        propositions.add(i);
                        pc++;
                    }
                }
                return pc;
            default:
                return 0;
        }
    }

    /**
     * @return The number of propositions or 0 otherwise.
     */
    public int getNumberOfpropositions() {
        return numberOfpropositions;
    }

    /**
     * @return A list of propositions or an empty list otherwise.
     */
    public List<Item> getPropositions() {
        return propositions;
    }
    
    
}
