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
package lu.list.itis.dkd.assess.cloze.gap.approach;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.list.itis.dkd.assess.cloze.gap.EnglishGapFinder;
import lu.list.itis.dkd.assess.cloze.gap.FrenchGapFinder;
import lu.list.itis.dkd.assess.cloze.gap.GermanGapFinder;
import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.opennlp.Sentence;

public class VerbMatch {
    protected static final Logger logger = Logger.getLogger(Verb.class.getSimpleName());

    /**
     * Returns cloze sentences where certain verbs are considered as key-distractor pairs.
     * @param sentences
     * @param numberOfDistractors
     * @param firstSentence
     * @return
     */
    public static List<ClozeSentence> getClozeSentences(List<Sentence> sentences, int firstSentence) {
        List<ClozeSentence> clozeSentences = new ArrayList<>();
        switch (sentences.get(0).getLanguage()) {
            case DE:
                GermanGapFinder germanGapFinder = new GermanGapFinder(0);
                for (int i = firstSentence; i < sentences.size(); i++) {
                    clozeSentences.add(germanGapFinder.findVerbs(sentences.get(i), false));
                }
                return clozeSentences;
            case FR:
                FrenchGapFinder frenchGapFinder = new FrenchGapFinder(0);
                for (int i = firstSentence; i < sentences.size(); i++) {
                    clozeSentences.add(frenchGapFinder.findVerbs(sentences.get(i), false));
                }
                return clozeSentences;
            case EN:
                EnglishGapFinder englishGapFinder = new EnglishGapFinder(0);
                for (int i = firstSentence; i < sentences.size(); i++) {
                    clozeSentences.add(englishGapFinder.findVerbs(sentences.get(i), false));
                }
                return clozeSentences;
            default:
                logger.log(Level.INFO, "The language was not registered. Hence, no clozeSentences could be generated.");
                return clozeSentences;
        }
    }
}
