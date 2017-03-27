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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lu.list.itis.dkd.assess.opennlp.lemmatizer.Lemmatizer;
import lu.list.itis.dkd.assess.opennlp.util.GrammarHelper;
import lu.list.itis.dkd.assess.opennlp.util.StringHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.1.0
 */
public class EnglishWordList {
    private List<Item> items = new ArrayList<>();

    private static final String[] Punct = {":", ",", "."};
    private static final String[] Nt = {
                    "didn't", "didnt",
                    "don't", "dont",
                    "can't", "cant",
                    "couldn't", "couldnt",
                    "won't", "wont",
                    "wouldn't", "wouldnt"
    };
    private static final String[] Aux = {
                    "be", "am", "is", "are", "was", "were", "being", "been",
                    "have", "has", "had", "having",
                    "do", "does", "did", // "doing" and "done" are not aux forms
                    "need", // "needs" is not an aux
                    "dare" // "dares" is not an aux
    };
    private static final String[] Interrogative = {"WDT", "WP", "WPS", "WRB"};

    // First elements of correlating conjunctions
    private static final String[] Correl = {"both", "either", "neither"};
    private static final String[] NegPol1 = {"yet", "much", "many", "any", "anymore"};
    private static final String[] NegPol2 = {"unless"};

    /// Linking verbs: all forms of all verbs that take an adjective after them.
    private static final String[] Link = {
                    // Being
                    "be", "am", "is", "are", "was", "were", "been", "being",
                    // Becoming
                    "become", "becomes", "became", "becoming",
                    "get", "gets", "got", "gotten", "getting",
                    // Seeming visually
                    "look", "looks", "looked", "looking",
                    "seem", "seems", "seemed", "seeming",
                    "appear", "appears", "appeared", "appearing",
                    // Seeming through other senses
                    "sound", "sounds", "sounded", "sounding",
                    "feel", "feels", "felt", "feeling",
                    "smell", "smells", "smelled", "smelling",
                    "taste", "tastes", "tasted", "tasting"
    };

    private static final String[] Be = {"am", "is", "are", "was", "were", "being", "been"};

    // Causative linking verbs
    private static final String[] CLink = {
                    "make", "makes", "made", "making",
                    "turn", "turns", "turned", "turning",
                    "paint", "paints", "painted", "painting"
    };

    private static final String[] ComeGo = {
                    "come", "comes", "came", "coming",
                    "return", "returns", "returned", "returning",
                    "arrive", "arrives", "arrived", "arriving",
                    "go", "goes", "went", "gone", "going",
                    "depart", "departs", "departed", "departing",
                    "emanate", "emanates", "emanated", "emanating"
    };

    private static final String[] Filler = {"and", "or", "but", "if", "that", "just", "you", "know"};

    EnglishWordList(String taggedtext) {
        // Split input string at the blanks, giving token/tag, token/tag etc.
        String[] tokenTags = taggedtext.split(" ");

        for (String tokenTag : tokenTags) {
            if (tokenTag.contains("_")) {
                String token = StringHelper.replaceNonWordSymbols(tokenTag.substring(0, tokenTag.indexOf("_")));
                String tag = StringHelper.replaceNonWordSymbols(tokenTag.substring(tokenTag.indexOf("_") + 1, tokenTag.length()));
                String lemma = Lemmatizer.getLemma(token.toLowerCase(), tag, Language.EN);
                items.add(new Item(token, tag, lemma, false, false, 0));
            }
        }
    }

    List<Item> getItems() {
        return items;
    }

    private boolean Contains(String[] set, String item) {
        for (String string : set) {
            if (item.contains(string))
                return true;
        }
        return false;
    }
    
    private boolean Equals(String[] set, String item) {
        for (String string : set) {
            if (item.equals(string))
                return true;
        }
        return false;
    }

    private int BeginningOfSentence(List<Item> w, int i) {
        int j = i - 1;
        while (j > 0 && !w.get(j).getTag().equals(".") && !w.get(j).getTag().equals("")) {
            j--;
        }
        return j + 1;
    }

    private boolean Match(String first, String second) {
        if (first.length() == 0 || second.length() == 0) {
            return false;
        }
        if (first.equals(second)) {
            return true;
        }
        if (first.endsWith("-")) {
            first = first.substring(0, first.length() - 1); // final hyphen drop
        }
        if (second.length() > 3 && !first.equals("a") && !first.equals("an") && second.startsWith(first)) {
            return true;
        }
        return false;
    }

    private boolean isLetterOrDigit(char character) {
        Pattern p = Pattern.compile("[a-z,A-Z,0-9]");
        Matcher m = p.matcher(Character.toString(character));

        while (m.find()) {
            return true;
        }

        return false;
    }
    
    private void speechMode(List<Item> b, int i){
        Item item = b.get(i);
        Item previousItem = b.get(i-1);
        
        // 020 - Repetition of the form "A A" is simplified to "A". The first A can be an 
        // initial substring of the second one. Both remain in the word count.
        if (Match(previousItem.getToken(), previousItem.getToken())) {
            // Mark the first A as to be ignored
            previousItem.setIsprop(false);
            previousItem.setIsword(false);
            previousItem.setTag("");
            previousItem.setRulenumber(20);
        }

        // 021, 022 - Repetition of the form "A Punct A" is simplified to "A". Repetition of the
        // form "A B A" is simplified to "A B". Both A's remain in the word count. The first A can be
        // an initial substring of the second one. Punct is anything with tag "." or "," or ":".
        if (i-2 >= 0) {
            Item prePrecedingItem = b.get(i-2);
            if (Match(prePrecedingItem.getToken(), prePrecedingItem.getToken()) && !Contains(Punct, item.getTag())) {
                // Mark the first A to be ignored
                prePrecedingItem.setTag("");
                prePrecedingItem.setIsword(false);
                prePrecedingItem.setIsprop(false);
                prePrecedingItem.setRulenumber(22);
                // Mark the punctuation mark to be ignored
                if (Contains(Punct, previousItem.getTag())) {
                    previousItem.setTag("");
                    previousItem.setIsword(false);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(21);
                }
            }
        }

        // 023 - Repetition of the form "A B Punct A B" is simplified to "A B". Both A's and B's
        // remain in the word count. The first A (or B) can be an initial substring of the second one.
        // Punct is anything with tag "." or "," or ":".
        if (i - 4 >= 0) {
            Item pre2Item = b.get(i-2);
            Item pre3Item = b.get(i-3);
            Item pre4Item = b.get(i-4);
                
            if (Match(pre3Item.getToken(), item.getToken()) && Match(pre4Item.getToken(), previousItem.getToken()) && Contains(Punct, pre2Item.getTag())) {     
                pre4Item.setTag(pre3Item.getTag());
                pre3Item.setTag(pre2Item.getTag());
                pre2Item.setTag("");

                pre4Item.setIsword(pre3Item.isWord());
                pre3Item.setIsword(pre2Item.isWord());
                pre2Item.setIsword(false);

                pre4Item.setIsprop(pre3Item.isProp());
                pre3Item.setIsprop(pre2Item.isProp());
                pre2Item.setIsprop(false);

                pre4Item.setRulenumber(pre3Item.getRulenumber());
                pre3Item.setRulenumber(pre2Item.getRulenumber());
                pre2Item.setRulenumber(23);
            }
        }
        
        // 610 - A sentence consisting entirely of probable filler words is propositionless
        if (item.getTag().equals(".")) {
            int bos = BeginningOfSentence(b, i);
            int k = 0;
            for (int j = bos; j < i; j++) {
                if (!b.get(j).getTag().equals("UH") && !Contains(Filler, b.get(j).getToken())) {
                    k++;
                }
            }
            if (k == 0) {
                for (int j = bos; j < i; j++) {
                    b.get(j).setTag("");
                    b.get(j).setIsprop(false);
                    b.get(j).setRulenumber(610);
                }
            }
        }

        // 632 - In speech mode, "like" is a filler when not immediately preceded by a form of
        // "be".
        if (item.getToken().equals("like") && !Contains(Be, previousItem.getToken())) {
            item.setTag("");
            item.setIsprop(false);
            item.setRulenumber(632);
        }

        // 634 - In speech mode, "you know" is a filler and counts as one word, not two.
        if (previousItem.getToken().equals("you") && item.getToken().equals("know")) {
            // back up one
            i--;
            // delete forward one
            b.remove(i + 1);
            // reset data for the current item
            item.setToken("you_know");
            item.setTag("");
            item.setIsprop(false);
            item.setIsword(true);
            item.setRulenumber(634);
        }
    }
    
    /**
     * Method which applies all the idea-counting rules to a WordList. The rules are
     * numbered. Rule numbering is not consecutive so that there can be room to add others in each group. 
     * Some rules apply only if speechMode is true.
     * @param speechMode
     */
    void applyIdeaCountingRules(boolean speechMode) {
        List<Item> b = this.items;

        for (int i = 1; i < b.size(); i++) {
            Item item = b.get(i);
            Item previousItem = b.get(i-1); 
            
            if (item.getToken().equals("")) {
                continue;
            }
            
            if (speechMode) {
                speechMode(b, i);
            }

            // 001 - The symbol ^ used to mark broken-off spoken sentences is an end-of-sentence
            // marker.
            if (item.getToken().equals("^")) {
                item.setTag(".");
                item.setRulenumber(1);
            }

            // 002 - The item is a word if its token starts with a letter or digit and its tag is
            // not SYM (symbol).
            if (isLetterOrDigit(item.getToken().charAt(0)) && !item.getTag().equals("SYM")) {
                item.setIsword(true);
                item.setRulenumber(2);
            }

            // 003 - Two cardinal numbers in immediate succession are combined into one.
            if (item.getTag().equals("CD") && previousItem.getTag().equals("CD")) {
                previousItem.setToken(previousItem.getToken() + " " + item.getToken());
                previousItem.setRulenumber(3);
                i--;
                b.remove(i + 1);
            }

            // 004 - Cardinal + nonalphanumeric + cardinal are combined into one token
            if (i-2 >= 0) {
                Item prePrecedingItem = b.get(i-2);
                if (GrammarHelper.isNumber(item.getTag(), Language.EN) && !isLetterOrDigit(previousItem.getToken().charAt(0)) && GrammarHelper.isNumber(prePrecedingItem.getTag(), Language.EN)) {
                    prePrecedingItem.setToken(prePrecedingItem.getToken() + previousItem.getToken() + previousItem.getToken());                                                                                                           // token
                    prePrecedingItem.setRulenumber(4);
                    i = i - 2; // step backward 2
                    b.remove(i + 1); // delete forward 2
                    b.remove(i + 1);
                }
            }

            

            // 050 - 'not' and any word ending in "n't" are putatively propositions and their tag is changed to NOT.
            if (item.getToken().equals("not") || item.getToken().endsWith("n't") || Contains(Nt, item.getToken())) {
                item.setIsprop(true);
                item.setTag("NOT");
                item.setRulenumber(50);
            }

            // 054 - 'that/DT' or 'this/DT' is a pronoun, not a determiner.
            if (GrammarHelper.isVerb(item.getTag(), Language.EN) || GrammarHelper.isAdv(item.getTag(), Language.EN)) {
                if (previousItem.getToken().equals("that") || previousItem.getToken().equals("this")) {                               
                    previousItem.setTag("PRP");
                    previousItem.setRulenumber(54);
                    previousItem.setIsprop(false);
                }
            }

            // 101 - Subject-Aux inversion
            // If the current word is an Aux and the current word is the first word of the sentence
            // or the sentence begins with an interrogative, move the current word rightward to put
            // it in front of the first verb, or the end of the sentence. In some cases this will move a word
            // too far to the right, but the effect on proposition counting is benign.
            if (Contains(Aux, item.getToken())) {
                int bos = BeginningOfSentence(b, i);
                if ((bos == i) || Contains(Interrogative, b.get(bos).getTag())) {
                    // find out where to move to
                    int dest;
                    for (dest = i + 1; dest < b.size(); dest++) {
                        if (b.get(dest).getTag().equals(".") || GrammarHelper.isVerb(b.get(dest).getTag(), Language.EN)) {
                            break;
                        }
                    }
                    // if movement is called for,
                    if (dest > i + 1 && dest < b.size()) {
                        // insert a copy in the new location
                        b.set(dest, new Item(item.getToken(), item.getTag(), item.getLemma(), true, true, 101));
                        // mark the old item as to be ignored
                        item.setTag("");
                        item.setIsprop(false);
                        item.setIsword(false);
                        item.setToken(b.get(i).getToken() + "/moved");
                    }
                }
            }

            // Rule group 200 - Preliminary identification of propositions
            if (GrammarHelper.isProp(item.getTag(), Language.EN)) {
                item.setIsprop(true);
                item.setRulenumber(200);
            }

            // 201 - 'The', 'a', and 'an' are not propositions.
            if (item.getToken().equals("the") || item.getToken().equals("an") || item.getToken().equals("a")) {
                item.setIsprop(false);
                item.setRulenumber(201);
            }
            
//            if (GrammarHelper.isNoun(item.getTag(), Language.EN)) {
//                item.setIsprop(true);
//                item.setRulenumber(1000);
//            }

            // 202 - An attributive noun (such as 'lion' in 'lion tamer') is a proposition - Maybe
            // better without this rule
            if (GrammarHelper.isNoun(item.getTag(), Language.EN) && GrammarHelper.isNoun(previousItem.getTag(), Language.EN)) {
                item.setIsprop(true);
                item.setRulenumber(202);
                previousItem.setIsprop(false);
                previousItem.setRulenumber(202);
            }

            // 203 - The first word in a correlating conjunction such as "either...or",
            // "neither...nor", "both...and" is not a proposition.
            if (item.getTag().equals("CC") && !Contains(Correl, item.getToken())) {
                int ending = 0;
                if (i-10 > 0) {
                    ending = i-10;
                }
                
                // Search back up to 10 words, but not across a sentence end.
                for (int j = i - 1; j > ending; j--) {
                    if (Contains(Correl, b.get(j).getToken())) {
                        b.get(j).setIsprop(false);
                        b.get(j).setRulenumber(203);
                        break;
                    }
                }
             }

            // 204 - "And then" and "or else" are each a single proposition
            if ((previousItem.getToken().equals("and") && item.getToken().equals("then")) ||
                (previousItem.getToken().equals("or") && item.getToken().equals("else"))) {
                item.setIsprop(false);
                item.setRulenumber(204);
            }


            // 206 - "To" is not a proposition when it is last word in sentence.
            if (item.getTag().equals(".") && previousItem.getTag().equals("TO")) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(206);
            }

            // 207 - Modal is a proposition when it is last word in sentence.
            if (item.getTag().equals(".") && previousItem.getTag().equals("MD")) {
                previousItem.setIsprop(true);
                previousItem.setRulenumber(207);
            }

            // 210 - Cardinal number is a proposition only if there is a noun within 5 words after it 
            // This is so "in 3 parts" is 2 props but "in 1941" is only one.
            if (GrammarHelper.isNoun(item.getTag(), Language.EN)) {
                item.setIsprop(false);
                item.setRulenumber(210);

                // This rule looks forward up to 5 words
                int ending = i + 6;
                if (i + 6 > b.size()) {
                    ending = b.size();
                }
                
                for (int j = i + 1; (j < b.size()) && (j < ending); j++) {
                    if (GrammarHelper.isNoun(b.get(j).getTag(), Language.EN)) {
                        item.setIsprop(true);
                        break;
                    }
                }
            }

            // 211 - 'Not...unless' and similar pairs count as one proposition
            if (Contains(NegPol2, item.getToken())) {
                int ending = 0;
                if (i-10 > 0) {
                    ending = i-10;
                }                
                
                for (int j = i - 1; j > ending; j--) {
                    if (b.get(j).getTag().equals(".")) {
                        break;
                    }
                    if (b.get(j).getTag().equals("NOT")) {
                        b.get(j).setIsprop(false);
                        b.get(j).setRulenumber(211);
                        break;
                    }
                }
            }

            // 212 - 'Not...any' and similar pairs count as one proposition
            if (Contains(NegPol1, item.getToken())) {
                int ending = 0;
                if (i-10 > 0) {
                    ending = i-10;
                }  
                
                for (int j = i - 1; j > ending; j--) {
                    if (b.get(j).getTag().equals(".")) {
                        break;
                    }
                    if (b.get(j).getTag().equals("NOT")) {
                        item.setIsprop(false);
                        item.setRulenumber(212);
                        break;
                    }
                }
             }

            // 213 - "Going to" is not a proposition when immediately preceding a verb.
            if (i-2 >= 0) {
                Item prePrecedingItem = b.get(i - 2);
                
                if (GrammarHelper.isVerb(item.getTag(), Language.EN) && previousItem.getToken().equals("to") && prePrecedingItem.getToken().equals("going")) {
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(213);
                    previousItem.setIsprop(false);
                    prePrecedingItem.setRulenumber(213);
                }

                // 214 - "If ... then" is 1 conjunction, not two.
                if (item.isWord() && previousItem.getToken().equals("then")) {
                    int ending = 0;
                    if (i-10 > 0) {
                        ending = i-10;
                    }  
                    
                    for (int j = i - 1; j > ending; j--) {
                        if (b.get(j).getTag().equals(".")) {
                            break;
                        }
                        if (b.get(j).getToken().equals("if")) {
                            previousItem.setIsprop(false);
                            previousItem.setRulenumber(214);
                            break;
                        }
                    }
                }
            }
            
            // 225 - "each other" is a pronoun (to be tagged as PRP PRP).
            if (item.getToken().equals("other") && previousItem.getToken().equals("each")) {
                item.setTag(previousItem.getTag());
                previousItem.setTag("PRP");
                item.setIsprop(previousItem.isProp());
                previousItem.setIsprop(false);
                item.setRulenumber(previousItem.getRulenumber());
                previousItem.setRulenumber(225);
            }


            // 230 - "how come" and "how many" are each 1 proposition, not two.
            if ((item.getToken().equals("come") || item.getToken().equals("many")) && previousItem.getToken().equals("how")) {
                item.setIsprop(false);
                item.setTag(previousItem.getTag());
                item.setRulenumber(230);
            }

            // 301 - Linking verb is not a proposition if followed by adj. or adv.
            if ((GrammarHelper.isAdj(item.getTag(), Language.EN) || GrammarHelper.isAdv(item.getTag(), Language.EN)) && Contains(Link, previousItem.getToken())) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(301);
            }

            // 302 - "Be" is not a proposition when followed by a preposition.
//            if (item.getTag().equals("IN") && Contains(Be, previousItem.getToken())) {
            if (item.getTag().equals("IN") && Equals(Be, previousItem.getToken())) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(302);
            }
            
//            //303 - IN is a followed by a noun
//            if (item.getTag().equals("IN")) {
//                int stop = 5;
//                for (int j = i; j < b.size(); j++) {
//                    if (GrammarHelper.isNoun(b.get(j).getTag(), Language.EN)) {
//                        item.setIsprop(true);
//                        item.setRulenumber(303);
//                    }
//                    stop--;
//                    if (stop == 0) {
//                        break;
//                    }
//                }
//            }
            
            //304 - Noun is prop... test http://www.analyzemywriting.com/lexical_density.html
            if (GrammarHelper.isNoun(item.getTag(), Language.EN)) {
                item.setIsprop(true);
                item.setRulenumber(304);
            }

            // 310 - Linking verb + Adverb + { PDT, DT } is 2 propositions
            if (item.getTag().equals("DT") || item.getTag().equals("PDT")) {
                if (i - 2 >= 0) {
                    Item prePrecedingItem = b.get(i - 2);
                    
                    if (GrammarHelper.isAdv(previousItem.getTag(), Language.EN) && Contains(Link, prePrecedingItem.getToken())) {
                        previousItem.setIsprop(true);
                        previousItem.setRulenumber(310);
                        prePrecedingItem.setIsprop(true);
                        prePrecedingItem.setRulenumber(310);
                    }
                }
            }

            // 311 - Causative linking verbs: 'make it better' and similar phrases do not count the
            // adjective as a new proposition (since the verb was counted).
            if (GrammarHelper.isAdj(item.getTag(), Language.EN)) {
                int ending = 0;
                if (i-10 > 0) {
                    ending = i-10;
                }
                 
                for (int j = i - 1; j > ending; j--) {
                    if (b.get(j).getTag().equals(".")) {
                        break;
                    }
                    if (Contains(CLink, b.get(j).getToken())) {
                        item.setIsprop(false);
                        item.setRulenumber(311);
                        break;
                    }
                }
            }

            // 401 - // Aux not is one proposition, not two
            if (item.getToken().equals("not") && Contains(Aux, previousItem.getToken())) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(401);
            }

            // 402 - Aux Verb is one proposition, not two
            if (GrammarHelper.isVerb(item.getTag(), Language.EN) && Contains(Aux, previousItem.getToken())) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(402);
            }

            // 405 - Aux NOT Verb
            if (i-2 >= 0) {
                Item prePrecedingItem = b.get(i - 2);
                
                if (GrammarHelper.isVerb(item.getTag(), Language.EN) && (previousItem.getTag().equals("NOT") || 
                    GrammarHelper.isAdv(previousItem.getTag(), Language.EN)) && Contains(Aux, prePrecedingItem.getToken())) {
                    
                    prePrecedingItem.setIsprop(false);
                    prePrecedingItem.setRulenumber(405);
                }
            }
            

            // 510 - TO VB is one proposition, not two
            if (item.getTag().equals("VB") && previousItem.getTag().equals("TO")) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(510);
            }

            // 511 - "for ... TO VB": "for" is not a proposition
             if (item.getTag().equals("VB") && previousItem.getTag().equals("TO")) {
                 //Search back up to 10 words, but not across a sentence end.
                 int ending = 0;
                 if (i-10 > 0) {
                     ending = i-10;
                 }
                 
                 for (int j = i - 1; j > ending; j--) {
                     if (b.get(j).getTag().equals(".")) {
                         break;
                     }
                     if (b.get(j).getToken().equals("for")) {
                         b.get(j).setIsprop(false);
                         b.get(j).setRulenumber(511);
                         break;
                     }
                 }
             }

            // 512 - "From" and "to" form a single proposition with preceding "go", "come", or their
            // synonyms. Maybe better results without this rule.
            if (i-2 >= 0) {
                Item prePrecedingItem = b.get(i - 2);
                
                if ((item.getToken().equals("to") || item.getToken().equals("from")) &&
                    (Contains(ComeGo, previousItem.getToken()) || Contains(ComeGo, prePrecedingItem.getToken()))) {
                    item.setIsprop(false);
                    item.setRulenumber(512);
                }
                
            }
        }
    }
}
