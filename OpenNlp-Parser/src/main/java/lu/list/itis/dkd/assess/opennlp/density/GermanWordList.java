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
public class GermanWordList {    
    private static final  String[] SEIN = {"bin", "bist", "ist", "sind", "seid", "sind",
                                           "war", "warst", "waren", "wart", 
                                           "wäre", "wärst", "wärest", "wärt", "wäret",
                                           "sei", "seien", "sein", "seist", "seiest", "seiet", "seit"
    };
    
    private static final String[] HABEN = {"hab", "habe", "hast", "hat", "haben", "habt", 
                                           "hatte", "hattest", "hatten", "hattet", 
                                           "habest", "habet", "habend",
                                           "hätte", "hättest", "hätte", "hätten", "hättet", "hätten"
    };
    private static final String[] WERDEN = {"werd", "werde", "wirst", "wird", "werden", "werdet", "werden",
                                            "wurde", "wurdest", "wurden", "wurdet", "wurden", 
                                            "ward", "wardest", "warden", "wardet", 
                                            "werde", "werdest", "werdend",
                                            "würde", "würdest", "würden", "würdet", "würden"};
    
    private List<Item> items = new ArrayList<>();
    
    GermanWordList() {
        Item item = new Item("", "", "", false, false, 0);
        
        for (int i = 0; i < 10; i++) {
            this.items.add(item); 
        }
    }

    GermanWordList(String taggedtext) {
        super();
        //Split input string at the blanks, giving token/tag, token/tag etc.
        String[] tokenTags = taggedtext.split(" ");
        
        for (String tokenTag : tokenTags) {
            if (tokenTag.contains("_")) {
                String token = StringHelper.replaceNonWordSymbols(tokenTag.substring(0, tokenTag.indexOf("_")));
                String tag = StringHelper.replaceNonWordSymbols(tokenTag.substring(tokenTag.indexOf("_")+1, tokenTag.length()));
                String lemma = Lemmatizer.getLemma(token.toLowerCase(), tag, Language.DE);
                items.add(new Item(token, tag, lemma, false, false, 0));
            }
        }
    }
    
    List<Item> getItems() {
        return items;
    }
    
    private boolean Equals(String[] set, String item) {
        for (String string : set) {
            if (item.equals(string))
                return true;
        }
        return false;
    }
    
    private boolean Contains(String[] set, String item) {
        for (String string : set) {
            if (item.contains(string))
                return true;
        }
        return false;
    }    
    
    private boolean isLetterOrDigit(char character) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(Character.toString(character));
      
        while (m.find()) {
            return true;
        }
      
        return false;
    }
    
    /**
     * Helper method to check if the verb is an auxilary verb
     * @return
     */
    private boolean isAV(String item) {
        if (Equals(HABEN, item) || Equals(SEIN, item) || Equals(WERDEN, item)) {
            return true;
        }
        
        return false;
    }
    
    void applyIdeaCountingRules(boolean speechMode) {
        List<Item> b = this.items;  
        
        for (int i = 1; i < b.size(); i++) {
            Item item = b.get(i);
            Item previousItem = b.get(i-1); 
            
            if (item.getToken().equals("")) {
                continue;
            }
    
            // 001 - The symbol  ^  used to mark broken-off spoken sentences is an end-of-sentence marker.
            if (item.getToken().equals("^")) {
                item.setTag(".");
                item.setRulenumber(1);
            }
            
            //002 - The item is a word if its token starts with a letter or digit and its tag is not SYM (symbol).
            if (isLetterOrDigit(item.getToken().charAt(0)) && !item.getTag().equals("XY")) {
                item.setIsword(true);
                item.setRulenumber(2);
            }
            
            //012 - sein and (ART+) noun and ADJD count as one proposition. 
            if (Contains(SEIN, item.getToken()) && i+1 < b.size()) {
                int j = i+1;
                if (b.get(j).getTag().equals("ART")) {
                    j = i+2;
                }
                    
                if (GrammarHelper.isNoun(b.get(j).getTag(), Language.DE)) {
                    if (j+1 < b.size()) {
                        if (b.get(j+1).getTag().equals("ADJD")) {
                            b.get(j+1).setIsprop(true);
                            b.get(j+1).setRulenumber(12);
                        }
                    }
                }
            }
            
            //013 - NN + verb or NE + verb. Count verb as one preposition
            if (GrammarHelper.isNoun(previousItem.getTag(), Language.DE) && GrammarHelper.isVerb(item.getTag(), Language.DE)) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(13);
                item.setIsprop(true);
                item.setRulenumber(13);
            }
            
            //14 - NN
            if (GrammarHelper.isNoun(item.getTag(), Language.DE)) {
                item.setIsprop(true);
                item.setRulenumber(14);
            }
                            
            
            //015 - ''sein'' and APPR as one proposition. Proposition is the APPR
            if (Equals(SEIN, item.getToken())) {
                for (int j = i; j < b.size(); j++) {
                    if (b.get(j).getTag().contains("APPR")) {
                        item.setIsprop(false);
                        item.setRulenumber(15);
                        b.get(j).setIsprop(true);
                        b.get(j).setRulenumber(15);
                        break;
                    }
                }
            }
            
            //16 - APPR + NE or APPR + NN then APR is one proposition
            if (item.getTag().contains("APPR")) {
                for (int j = i; j < b.size(); j++) {
                    if (GrammarHelper.isNoun(b.get(j).getTag(), Language.DE)) {
                        item.setIsprop(true);
                        item.setRulenumber(16);
                        break;
                    }
                }
            }
                                        
            //Rule 21 - "wie" + ADJD are 1 proposition, not two.
            if (previousItem.getToken().equals("wie") && item.getToken().equals("ADJD")) {
                previousItem.setIsprop(true);
                previousItem.setRulenumber(21);
                item.setIsprop(false);
                item.setRulenumber(21);
            }
            
            
            //22 - zu + ADJD or zu + ADV --> zu is a proposition
            if (item.getTag().equals("PTKZU") && i+1 < b.size()) {
                Item nextItem = b.get(i+1);
                if (GrammarHelper.isAdj(nextItem.getTag(), Language.DE) || GrammarHelper.isAdv(nextItem.getTag(), Language.DE)) {
                    item.setIsprop(true);
                    item.setRulenumber(22); 
                }
            }
            
            //23 - sein + Adv --> sein is a propsition
            if (Contains(SEIN, item.getToken()) && i+1 < b.size()) {
                Item nextItem = b.get(i+1);
                if (GrammarHelper.isAdv(nextItem.getTag(), Language.DE)) {
                    item.setIsprop(true);
                    item.setRulenumber(23);
                }
            }
            
            
            //Rule 31 - Cardinal number is a proposition iff CARD + noun
            if (b.get(i).getTag().equals("CARD")) {
                boolean isProp = false;
                int j = 0;
                for (j = i; j < b.size(); j++) {
                    if (GrammarHelper.isNoun(b.get(j).getTag(), Language.DE)) {
                        isProp = true;
                        break;
                    }
                }
                
                if (isProp) {
                    b.get(i).setIsprop(true);
                    b.get(i).setRulenumber(31);
                }
            }
            
            //TODO WTF???
            //040 - Count Auxilary verb if noun is following
            if (isAV(b.get(i).getToken()) && i+1 < b.size()) {
                boolean isProp = true;
                
                //Is a noun behind
                int j = 0;
                for (j = i+1; j < b.size(); j++) {
                    //VA + verb --> auxilary is no proposition
                    if (b.get(j).getTag().contains("VA") || b.get(j).getTag().contains("VV")) {
                        isProp = false;
                        break;
                    }
                    
                    if (GrammarHelper.isNoun(b.get(j).getTag(), Language.DE)) {
                        isProp = true;
                        break;
                    }
                }
                
                //Is a noun before
                for (int k = i; k > 0; k--) {
                    int nounPos = 0;
                    if (GrammarHelper.isNoun(b.get(k).getTag(), Language.DE)) {
                        nounPos = k;
                    }
                    
                    for (int l = nounPos; l < i; l++) {
                        if (GrammarHelper.isAdj(b.get(k).getTag(), Language.DE) || GrammarHelper.isAdv(b.get(k).getTag(), Language.DE)) {
                            isProp = true;
                            break;
                        }
                    }
                }                
                
                if (isProp) {
                    b.get(i).setIsprop(true);
                    b.get(i).setRulenumber(40);
                }
            }

            //044 - VM VV is one proposition, not two
            if (item.getTag().equals("VV") && previousItem.getToken().equals("VM")) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(44);
                item.setIsprop(true);
                item.setRulenumber(44);
            }
            
            //045 - VA NICHT VV or VM NICHT VV   (NICHT and VV are propositions)
            if (i+1 < b.size()) {
                Item nextItem = b.get(i+1);
                if (item.getToken().equals("nicht") && nextItem.getTag().equals("VV")) {
                    if (isAV(previousItem.getToken()) || previousItem.getTag().equals("VM")) {
                        item.setIsprop(true);
                        item.setRulenumber(45);
                        nextItem.setIsprop(true);
                        nextItem.setRulenumber(45);
                    }
                }
            }
            
            //TODO 47 - Reflexive Verben PRF + VV is one token, not two
            if (item.getTag().equals("PRF") && i+1 > b.size()) {
                if (b.get(i+1).getTag().equals("VV")) {
                    item.setIsprop(true);
                    item.setRulenumber(47);
                }   
            }
            
            //O51 - ART are not propositions
//            if (b.get(i).getToken().equals("ART")) {
//                b.get(i).setIsprop(false);
//                b.get(i).setRulenumber(51);
//            }
            
            //52 - Des + noun (genetive) if there is no APPR in front of.
            if (item.getToken().toLowerCase().equals("des")) {
                int j = 0;
                boolean appr = false;
                if (i > 0) {
                    if (!previousItem.getTag().equals("APPR")) {
                        appr = true;
                    }
                }
                
                if (appr) {
                    for (j = i; j < b.size(); j++) {
                        if (GrammarHelper.isNoun(b.get(j).getTag(), Language.DE)) {
                            item.setIsprop(true);
                            item.setRulenumber(52);
                            break;
                        }
                    }
                }
            }
            
            //62 - wieviel count as one proposition
            if (item.getToken().equals("wieviel")) {
                item.setIsprop(true);
                item.setTag("ADV");
                item.setRulenumber(62);
            }
            
            //063 - APPR PWAT count as one proposition. PWAT is the proposition
            if (item.getTag().equals("APPR") && i+1 < b.size()) {
                Item nextItem = b.get(i+1);
                if (nextItem.getTag().equals("PWAT")) {
                    nextItem.setIsprop(true);
                    nextItem.setRulenumber(63);
                }
            }
            
            //064 - PWS APPR PWS count as one proposition. Last PWS as proposition (e.g. was für welche...)
            if (b.get(i).getTag().equals("APPR") && i+1 < b.size()) {
                Item nextItem = b.get(i+1);
                if (nextItem.getTag().equals("PWAT") && (previousItem.getTag().equals("PWS"))) {
                    nextItem.setIsprop(true);
                    nextItem.setRulenumber(64);
                }
            }
            
            //065 - PWS APPR ART count as one proposition. Count PWS as proposition (e.g. was für ein)
            if (b.get(i).getTag().equals("APPR") && i+1 < b.size()) {
                Item nextItem = b.get(i+1);
                if (nextItem.getTag().equals("ART") && (previousItem.getTag().equals("PWS"))) {
                    nextItem.setIsprop(true);
                    nextItem.setRulenumber(65);
                }    
            }
            
            //071 - "wie" + ADV
            if (item.getTag().equals("ADV") && previousItem.getToken().equals("wie")) {
                item.setIsprop(false);
                item.setTag(previousItem.getTag());
                item.setRulenumber(71);
            }
            
            
            //072 - "wie viel"="wieviel"
            if (b.get(i).getToken().equals("wie") && i+1 < b.size()) {
                Item nextItem = b.get(i+1);                
                if (nextItem.getToken().contains("viel")) {
                    item.setIsprop(false);
                    nextItem.setRulenumber(72);
                    nextItem.setIsprop(true);
                    nextItem.setRulenumber(72);
                }
            }
            
            //073 - ADV + ADV count as one proposition
            if (GrammarHelper.isAdv(item.getTag(), Language.DE) && GrammarHelper.isAdv(previousItem.getTag(), Language.DE)) {
                previousItem.setIsprop(false);
                previousItem.setRulenumber(73);
            }
            
            
            //074 - ADV + APPR (APPR + ADV) or ADJD + APPR (APPR + ADJD) --> APPR is the proposition 
            if (GrammarHelper.isAdv(item.getTag(), Language.DE) || GrammarHelper.isAdj(item.getTag(), Language.DE)) {
                if (i+1 < b.size()) {
                    Item nextItem = b.get(i+1);
                    if (nextItem.getTag().contains("APPR")) {
                        nextItem.setIsprop(true);
                        nextItem.setRulenumber(74);
                    }
                }
                else if (previousItem.getTag().equals("APPR")) {
                    previousItem.setIsprop(true);
                    previousItem.setRulenumber(74);
                }
            }
            
            //75 - ADV + ADJ count as one proposition
//            if (i != 0) {
//                if (Contains(Adj, b.get(i).getTag()) && Contains(Adv, b.get(i-1).getTag())) {
//                    b.get(i-1).setIsprop(false);
//                    b.get(i-1).setRulenumber(201);
//                }
//            }
            
            //102 - Constructions involving PTKZU "zu"
            if (item.getTag().equals("PTKZU")) {
                boolean isProp = false;                
                for (int j = i; j > 0; j--) {
                    if (i-j > 5) {
                        break;
                    }
                    if (b.get(j).getTag().equals("VAINF") || b.get(j).getTag().equals("VVINF")) {
                        isProp = true;
                        break;
                    }
                }
                    
                if (isProp) {
                    item.setIsprop(true);
                    item.setRulenumber(102);
                }
            }
            
            //103 - "UM...ZU VVINF", "OHNE...ZU VVINF", 
            //"(AN)STATT...ZU VVINF: "um", "ohne", "(an)statt" is not a proposition
            if (item.getTag().equals("VVINF") && previousItem.getTag().equals("PTKZU")) {
                for (int j = i-1; j > 0; j--) {
                    if (b.get(j).getToken().equals("um") || b.get(j).getToken().equals("ohne") ||
                        b.get(j).getToken().equals("statt") || b.get(j).getToken().equals("anstatt")) {
                            b.get(j).setIsprop(false);
                            b.get(j).setRulenumber(103);
                            break;
                    }
                }
            }
            
            //104 - VVFIN PTKVZ is one proposition, not two
            if (item.getTag().equals("VVFIN")) {
                for (int j = i-1; j > 0; j--) {
                    if (b.get(j).getToken().equals("um") || b.get(j).getToken().equals("ohne") ||
                        b.get(j).getToken().equals("statt") || b.get(j).getToken().equals("anstatt")) {
                            item.setIsprop(true);
                            item.setRulenumber(104);
                            b.get(j).setIsprop(false);
                            b.get(j).setRulenumber(104);
                            break;
                    }
                }
            }
        
            //112 - TRUNC is not proposition
//            if (i > 3 && i < b.size()-3) {
//                if (b.get(i).getTag().equals("TRUNC")) {
//                    for (int j = i-3; j < i+3; j++) {
//                        if (Equals(Noun, b.get(j).getTag()) || Contains(Verb, b.get(j).getTag())) {
//                            b.get(j).setTag("");
//                            b.get(j).setIsprop(false);
//                            b.get(j).setRulenumber(112);
//                        }
//                    }
//                }
//            }
            
            //113 - XY and FM are not propositions
            if (item.getTag().equals("XY") || item.getTag().equals("FM")) {
                item.setIsprop(false);
                item.setRulenumber(113);
            }
            
            //200 - The tags in Prop are taken to indicate propositions.
            if (GrammarHelper.isProp(item.getTag(), Language.DE)) {
                item.setIsprop(true);
                item.setRulenumber(200);
            }
        }
    }
}
