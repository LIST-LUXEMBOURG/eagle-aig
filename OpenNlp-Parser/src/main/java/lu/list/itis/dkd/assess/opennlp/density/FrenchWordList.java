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
public class FrenchWordList {

    private List<Item> items = new ArrayList<>();

    FrenchWordList(String taggedtext) {
        super();
        // Split input string at the blanks, giving token/tag, token/tag etc.
        String[] tokenTags = taggedtext.split(" ");

        for (String tokenTag : tokenTags) {
            if (tokenTag.contains("_")) {
                String token = StringHelper.replaceNonWordSymbols(tokenTag.substring(0, tokenTag.indexOf("_")));
                String tag = StringHelper.replaceNonWordSymbols(tokenTag.substring(tokenTag.indexOf("_") + 1, tokenTag.length()));
                String lemma = Lemmatizer.getLemma(token.toLowerCase(), tag, Language.FR);                
                items.add(new Item(token, tag, lemma, false, false, 0));
            }
        }
    }

    private boolean isLink(String lemma) {
        if (lemma.equals("apparaître") || lemma.equals("suivre") || lemma.equals("être") ||
                        lemma.equals("sembler") || lemma.equals("devenir") || lemma.equals("paraître") ||
                        lemma.equals("rester") || lemma.equals("demeurer")) {
            return true;
        }
        return false;

    }

    List<Item> getItems() {
        return items;
    }
    
    //TODO speechMode
//    private void speechMode(Item item, Item item1, Item item2, Item item3) {
//        // 020 - Répétition ou correction d'un mot (mode oral)
//        if (!item.getToken().contains(item1.getToken())) {
//            item1.setIsword(false);
//            item1.setIsprop(false);
//            item1.setRulenumber(20);
//        }
//
//        // 023 - Répétition ou correction de 2 mots (mode oral)
//        if (!item.getToken().contains(item2.getToken()) &&
//                        !item1.getToken().contains(b.get(i - 3).getToken())) {
//            b.get(i - 3).setIsword(false);
//            b.get(i - 3).setIsprop(false);
//            b.get(i - 3).setRulenumber(23);
//            b.get(i - 2).setIsword(false);
//            b.get(i - 2).setIsprop(false);
//            b.get(i - 2).setRulenumber(23);
//        }
//
//        // 024 - Répétition ou correction de 3 mots (mode oral)
//        if (speechMode && !b.get(i).getToken().contains(b.get(i - 3).getToken()) &&
//                        !b.get(i - 1).getToken().contains(b.get(i - 4).getToken()) &&
//                        !b.get(i - 2).getToken().contains(b.get(i - 5).getToken())) {
//            b.get(i - 5).setIsword(false);
//            b.get(i - 5).setIsprop(false);
//            b.get(i - 5).setRulenumber(24);
//            b.get(i - 4).setIsword(false);
//            b.get(i - 4).setIsprop(false);
//            b.get(i - 4).setRulenumber(24);
//            b.get(i - 3).setIsword(false);
//            b.get(i - 3).setIsprop(false);
//            b.get(i - 3).setRulenumber(24);
//        }
//
//        // 101 - est-ce que ... Ne pas compter comme proposition en mode oral
//        if (speechMode && b.get(i - 2).getLemma().equals("être") &&
//                        b.get(i - 1).getLemma().equals("ce") &&
//                        b.get(i).getLemma().equals("que")) {
//            b.get(i - 2).setIsword(true);
//            b.get(i - 2).setIsprop(false);
//            b.get(i - 2).setRulenumber(101);
//            b.get(i - 1).setIsword(true);
//            b.get(i - 1).setIsprop(false);
//            b.get(i - 1).setRulenumber(101);
//            b.get(i).setIsword(true);
//            b.get(i).setIsprop(false);
//            b.get(i).setRulenumber(101);
//        }
//
//
//        if (speechMode && b.get(i - 2).getToken().equals("est") &&
//                        b.get(i - 1).getToken().equals("vrai") &&
//                        b.get(i).getLemma().equals("que")) {
//            b.get(i).setIsword(true);
//            b.get(i).setIsprop(false);
//            b.get(i).setRulenumber(207);
//        }
//
//
//        // 210 - Oui et non (mode oral)
//        if (speechMode && (b.get(i).getToken().equals("oui") || b.get(i).getToken().equals("non"))) {
//            b.get(i).setIsword(true);
//            b.get(i).setIsprop(false);
//            b.get(i).setRulenumber(210);
//        }
//        
//     // 600 - Marqueurs discursifs
//        if (speechMode && (b.get(i).getToken().equals("admettons") || b.get(i).getToken().equals("allez") ||
//                        b.get(i).getToken().equals("allons") || b.get(i).getToken().equals("attends") ||
//                        b.get(i).getToken().equals("attendez") || b.get(i).getToken().equals("comprenez") ||
//                        b.get(i).getToken().equals("disons") || b.get(i).getToken().equals("écoute") ||
//                        b.get(i).getToken().equals("écoutez") || b.get(i).getToken().equals("mettons") ||
//                        b.get(i).getToken().equals("mettez") || b.get(i).getToken().equals("regarde") ||
//                        b.get(i).getToken().equals("regardez") || b.get(i).getToken().equals("voyons") ||
//                        b.get(i).getToken().equals("enfin") || b.get(i).getToken().equals("voilà"))) {
//            b.get(i).setIsword(true);
//            b.get(i).setIsprop(false);
//            b.get(i).setRulenumber(600);
//        }
//
//        if (speechMode && (b.get(i - 1).getToken().equals("va") && b.get(i).getToken().equals("donc")) ||
//                        (b.get(i - 1).getToken().equals("ça") && b.get(i).getToken().equals("va")) ||
//                        (b.get(i - 1).getToken().equals("attends") && b.get(i).getToken().equals("voir")) ||
//                        (b.get(i - 1).getToken().equals("tu") && b.get(i).getToken().equals("comprends")) ||
//                        (b.get(i - 1).getToken().equals("vous") && b.get(i).getToken().equals("comprenez")) ||
//                        (b.get(i - 1).getToken().equals("comprends") && b.get(i).getToken().equals("tu")) ||
//                        (b.get(i - 1).getToken().equals("comprenez") && b.get(i).getToken().equals("vous")) ||
//                        (b.get(i - 1).getToken().equals("dis") && b.get(i).getToken().equals("donc")) ||
//                        (b.get(i - 1).getToken().equals("tu") && b.get(i).getToken().equals("vois")) ||
//                        (b.get(i - 1).getToken().equals("vous") && b.get(i).getToken().equals("voyez")) ||
//                        (b.get(i - 1).getToken().equals("figure") && b.get(i).getToken().equals("toi")) ||
//                        (b.get(i - 1).getToken().equals("figurez") && b.get(i).getToken().equals("vous")) ||
//                        (b.get(i - 1).getToken().equals("je") && b.get(i).getToken().equals("imagine"))) {
//            b.get(i - 1).setIsword(true);
//            b.get(i - 1).setIsprop(false);
//            b.get(i - 1).setRulenumber(600);
//            b.get(i).setIsword(true);
//            b.get(i).setIsprop(false);
//            b.get(i).setRulenumber(600);
//        }
//
//        // 601 - "bien" n'est alors pas proposition
//        if (speechMode && b.get(i).getToken().equals("bien") && (b.get(i - 1).getLemma().equals("penser") ||
//                        b.get(i - 1).getLemma().equals("regarder") ||
//                        b.get(i - 1).getLemma().equals("écouter") ||
//                        b.get(i - 1).getLemma().equals("voir"))) {
//            b.get(i).setIsword(true);
//            b.get(i).setIsprop(false);
//            b.get(i).setRulenumber(601);
//        }
//
//        // 602 - "donc" n'est alors pas proposition
//        if (speechMode && b.get(i).getToken().equals("donc") && (b.get(i - 1).getLemma().equals("dire") ||
//                        b.get(i - 1).getLemma().equals("comprendre") ||
//                        b.get(i - 1).getLemma().equals("aller"))) {
//            b.get(i).setIsword(true);
//            b.get(i).setIsprop(false);
//            b.get(i).setRulenumber(602);
//        }
//    }
    
    private boolean isNumber(String word){
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(word);

        while (numberMatcher.find()) {
            // Find sentence ending.
            String numberMatch = numberMatcher.group(0);
            if (numberMatch.length() == word.length()) {
                return true;
            }
        }
        
        return false;
    }

    void applyIdeaCountingRules(boolean speechMode) {
        List<Item> b = this.items;

        // Preprocessing
        for (int i = 1; i < b.size(); i++) {
            Item item = b.get(i);
            Item previousItem = b.get(i-1);
            String lemma = previousItem.getLemma();
            
            if (item.getLemma().equals("oui")) {
                item.setTag("ADV");
            }
            
            if (item.getTag().equals("VPP")) {
                if (lemma.equals("être") || lemma.equals("suivre") || lemma.equals("avoir")) {
                    previousItem.setTag("VER:aux");
                }
                else if (i-2 > 0) {
                    Item item2 = b.get(i-2);
                    if (item2.getLemma().equals("être") || item2.getLemma().equals("suivre") || item2.getLemma().equals("avoir")) {
                        item2.setTag("VER:aux");
                    }
                }
            }
            
            if (isNumber(item.getToken())) {
                item.setTag("CARD");
            }
            
        }
        
        for (int i = 1; i < b.size(); i++) {
            Item item = b.get(i);
            Item previousItem = b.get(i-1);

            // 001 - Interjections non reconnues par TreeTagger => pas mot, pas proposition
            if (speechMode && (item.getToken().equals("tiens") || item.getToken().equals("heu"))) {
                item.setTag("INT");
                item.setRulenumber(1);
            }
            if (item.getTag().equals("ADV") && item.getToken().equals("ben")) {
                item.setTag("INT");
                item.setRulenumber(1);
            }
            if (item.getToken().equals("parce")) {
                item.setTag("INT");
                item.setRulenumber(1);
            }

            // 002 - Ponctuation et symboles (mode oral)
            if (!item.getTag().equals("SYM") && !item.getTag().equals("PUNC") && !item.getTag().equals("SENT)") && !item.getTag().equals("INT")) {
                item.setIsword(true);
                item.setRulenumber(2);
            }

            // 102 - C'est + (au plus 5 mots) + "que" ou "qui" : "est" non compté comme proposition
            if (i-5 > 0) {
                if (b.get(i).getLemma().equals("que") || b.get(i).getLemma().equals("qui") && i-5 > 0) {
                    Item item2 = b.get(i - 2);
                    Item item3 = b.get(i - 3);
                    Item item4 = b.get(i - 4);
                    Item item5 = b.get(i - 5);
                    
                    if (item2.getLemma().equals("ce") && previousItem.getLemma().equals("être")) {
                        previousItem.setIsword(true);
                        previousItem.setIsprop(false);
                        previousItem.setRulenumber(102);
                    }

                    if (item3.getLemma().equals("ce") && item2.getLemma().equals("être")) {
                        item2.setIsword(true);
                        item2.setIsprop(false);
                        item2.setRulenumber(102);
                    }

                    if (item4.getLemma().equals("ce") && item3.getLemma().equals("être")) {
                        item3.setIsword(true);
                        item3.setIsprop(false);
                        item3.setRulenumber(102);
                    }

                    if (item5.getLemma().equals("ce") && item4.getLemma().equals("être")) {
                        item4.setIsword(true);
                        item4.setIsprop(false);
                        item4.setRulenumber(102);
                    }
                }
            }

            // 200 - Les tags correspondant à des propositions sont marqués comme propositions
            // KON,NUM,DET*,PREF*,ADJ,PRO:POS,PRO:IND,ADV,VER*,PROREL
            if (GrammarHelper.isProp(item.getTag(), Language.FR)) {
                item.setIsword(true);
                item.setIsprop(true);
                item.setRulenumber(200);
            }

            // 054 - Déterminants démonstratifs
            // "cet", "cette", "ces" -> comptés comme proposition
            // "ça" pas compté comme proposition
            // celle, celui, ceux
            if (item.getLemma().equals("ce") && !item.getTag().equals("DET")) {
                item.setIsword(true);
                item.setIsprop(true);
                item.setRulenumber(54);
            }

            if (item.getToken().equals("cet") || item.getToken().equals("cette") || item.getToken().equals("ces")) {
                item.setIsword(true);
                item.setIsprop(true);
                item.setRulenumber(54);
            }

            if (item.getToken().equals("Ca")) {
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(54);
            }

            if (previousItem.getLemma().equals("celui") && (item.getLemma().equals("là") || item.getLemma().equals("ci"))) {
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(54);
            }

            // 201 - Déterminants non propositions
            if (item.getLemma().equals("un") || item.getLemma().equals("le") || item.getLemma().equals("du")) {
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(201);
            }

            // 202 - Complément du nom introduit par "du"
            if (previousItem.getLemma().equals("du") && GrammarHelper.isNoun(item.getTag(), Language.FR) && i-3 > 0) {
                Item item2 = b.get(i - 2);
                Item item3 = b.get(i - 3);
                
                if (GrammarHelper.isAdj(item2.getTag(), Language.FR) || GrammarHelper.isNoun(item2.getTag(), Language.FR)) {
                    if (GrammarHelper.isNoun(item3.getTag(), Language.FR) && GrammarHelper.isAdj(item2.getTag(), Language.FR)) {
                        previousItem.setIsword(true);
                        previousItem.setIsprop(true);
                        previousItem.setRulenumber(202);
                    }
                }
            }    

            // 203 - soit + 1 à 3 mots + soit : seul le premier "soit" est compré comme proposition
            if (item.getToken().equals("soit") && i-4 > 0) {
                Item item2 = b.get(i-2);
                Item item3 = b.get(i-3);
                Item item4 = b.get(i-4);
                
                if (item2.getToken().equals("soit") || item3.equals("soit") || item4.getToken().equals("soit")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(203);
                }            
            }

            // 204 - !Conjonctions "ou" ou "et" superflues avant adverbe
            // adverbes après "et" : "puis", "alors", "donc", "ensuite", "finalement" => "et" pas
            // proposition
            // adverbes après "ou" : "alors" "bien" => "ou" pas proposition
            if (previousItem.getLemma().equals("et")) {
                String lemma = item.getLemma();
                if (lemma.equals("puis") || lemma.equals("alors") || lemma.equals("donc") || lemma.equals("ensuite") || lemma.equals("finalement")) {
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(204);
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(204);
                }
            }
            
            //205 - noun = OK --> test!!
            if (GrammarHelper.isNoun(item.getTag(), Language.FR)) {
                item.setIsword(true);
                item.setIsprop(true);
                item.setRulenumber(205);
            }
            
            // 206 - "de" n'est pas proposition après "falloir", "agir", "arriver", "paraître"
            if (item.getLemma().equals("de") && i-3 > 0) {
                Item item2 = b.get(i - 2);
                Item item3 = b.get(i - 3);
                String lemma = previousItem.getLemma();
                String lemma2 = item2. getLemma();
                String lemma3 = item3.getLemma();
                
                if (lemma.equals("agir") || lemma.equals("arrivier") || lemma.equals("falloir") || lemma.equals("paraître")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(206);
                } 
                
                if (previousItem.getToken().equals("envie") || previousItem.getToken().equals("lieu") || previousItem.getToken().equals("pitié") || previousItem.getToken().equals("soin")) {
                    if (lemma2.equals("avoir") || lemma3.equals("avoir")) {
                        item.setIsword(true);
                        item.setIsprop(false);
                        item.setRulenumber(206);
                    }
                }
                
                if (lemma.equals("près") || lemma.equals("auprès") || lemma.equals("lors")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(206);
                }
                
                if (lemma.equals("beaucoup") || lemma.equals("plein") || lemma.equals("énormément") || lemma.equals("tellement")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(206);
                }

                if (item2.getToken().equals("à") && previousItem.getToken().equals("coté")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(206);
                }
                
                if (item2.getToken().equals("en") && previousItem.getToken().equals("face")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(206);
                }
            }

            // 207 - "que" n'est pas proposition après "falloir", "sembler", "arriver", "paraître"
            if (item.getLemma().equals("que")) {
                String lemma = previousItem.getLemma();
                if (lemma.equals("falloir") || lemma.equals("sembler") || lemma.equals("arriver") || lemma.equals("paraître")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(207);
                }            
            }

            // 208 - "que" n'est pas proposition après "autant", "moins", "pire", "plus"
            if (item.getLemma().equals("que")) {
                if (previousItem.getLemma().equals("autant") && i-3 > 0) {
                    String lemma1 = previousItem.getLemma();
                    if (lemma1.equals("moins") || lemma1.equals("plus")) {
                        String lemma2 = b.get(i - 2).getLemma();
                        if (lemma2.equals("autant") || lemma2.equals("moins") || lemma2.equals("plus") || lemma2.equals("aussi")) {
                            String lemma3 = b.get(i - 3).getLemma();
                            if (lemma3.equals("autant") || lemma3.equals("moins")   || lemma3.equals("plus")  || lemma3.equals("aussi")) {
                                item.setIsword(true);
                                item.setIsprop(false);
                                item.setRulenumber(208);
                            }
                        }    
                    }   
                } 

                if (previousItem.getLemma().equals("pire") || previousItem.getLemma().equals("mieux")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(207);
                }
            }
            
            //209 - Test...
            if (!item.getToken().equals("nous") && !item.getToken().equals("vous") && (GrammarHelper.isAdj(item.getTag(), Language.FR) || GrammarHelper.isAdv(item.getTag(), Language.FR))) {
                if (GrammarHelper.isNoun(previousItem.getTag(), Language.FR)) {
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(209);
                }
                else if (i+1 < b.size()) {
                    Item nextItem = b.get(i+1);
                    if (GrammarHelper.isNoun(nextItem.getTag(), Language.FR)) {
                        item.setIsword(true);
                        item.setIsprop(true);
                        item.setRulenumber(209);
                    }
                }
            }

            // 211 - "aucun" "guère" "jamais" "nul" "pas" "plus" "point" "que" "rien" précédé
            // (à distance 1, 2 ou 3) par "ne" : seul "ne" proposition
            if (i-3 > 0) {
                String lemma = item.getLemma();
                String token = item.getToken();
                
                if (lemma.equals("aucun") || lemma.equals("nul") || token.equals("guère") || token.equals("pas") || token.equals("point") || lemma.equals("que") || token.equals("rien")) {
                    String lemma1 = b.get(i - 1).getLemma();
                    String lemma2 = b.get(i - 2).getLemma();
                    String lemm3 = b.get(i - 3).getLemma();
                    
                    if (lemma1.equals("ne") || lemma2.equals("ne") || lemm3.equals("ne")) {
                        item.setIsword(true);
                        item.setIsprop(false);
                        item.setRulenumber(211);
                    }
                }
            }

            // 212 - "de" n'est pas une proposition si précédée par négation
            if (b.get(i).getLemma().equals("de")) {
                String token = previousItem.getToken();
                if (token.equals("pas") || token.equals("plus") || token.equals("guère") || token.equals("point") || token.equals("jamais") || token.equals("rien")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(212);
                }            
            }

            // 213 - lemme="aller" + infinitif = futur proche : aller n'est pas une proposition
            if (previousItem.getLemma().equals("aller") && item.getTag().equals("VINF")) {
                previousItem.setIsword(true);
                previousItem.setIsprop(false);
                previousItem.setRulenumber(213);
            }

            // 214 - "si" + 1 à 9 mots + "alors" : ne pas compter "alors" comme proposition,
            // seulement "si".
            if (item.getLemma().equals("alors")) {
                int stop = 9;
                for (int j = i; j > 0; j--) {
                    Item itemJ = b.get(j);
                    if (itemJ.getLemma().equals("si")) {
                        item.setIsword(true);
                        item.setIsprop(false);
                        item.setRulenumber(214);
                        break;
                    }
                    
                    stop--;
                    if (stop == 0) {
                        break;
                    }
                }
            }

            // 301 - Verbe de liaison pas proposition si suivi d'un adjectif ou d'un adverbe
            if (GrammarHelper.isAdj(item.getTag(), Language.FR) || GrammarHelper.isAdv(item.getTag(), Language.FR)) {
               if (isLink(previousItem.getLemma())) {
                   previousItem.setIsword(true);
                   previousItem.setIsprop(false);
                   previousItem.setRulenumber(301);
               }  
            }

            if (item.getLemma().equals("air") && i-2 > 0) {
                Item item2 = b.get(i - 2); 
                if (item2.getLemma().equals("avoir") && previousItem.getLemma().equals("le")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(301);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(301);
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(301);
                }
                
            }

            // 302 - "être" non proposition si suivi d'une préposition
            if (item.getTag().equals("PREF")) {
                if (previousItem.getLemma().equals("être") || previousItem.getLemma().equals("suivre")) {
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(302);
                }
                
                if (GrammarHelper.isAdv(previousItem.getTag(), Language.FR) && i-2 > 0) {
                    Item item2 = b.get(i - 2); 
                    if (item2.getLemma().equals("être") || item2.getLemma().equals("suivre")) {
                        item2.setIsword(true);
                        item2.setIsprop(false);
                        item2.setRulenumber(302);
                    }
                }
            }

            // 402 - AUX + VERBE => une seule proposition
            if (GrammarHelper.isVerb(item.getTag(), Language.FR) && previousItem.getTag().equals("VER:aux")) {
                previousItem.setIsword(true);
                previousItem.setIsprop(false);
                previousItem.setRulenumber(402);
            }

            // 405 - AUX + mot + VERBE => une seule proposition
            if (GrammarHelper.isVerb(item.getTag(), Language.FR) && i-2 > 0) {
                Item item2 = b.get(i - 2); 
                if (item2.getTag().contains("VER:aux")) {
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(402);
                }
            }

            // 500 - participe passé + "par" => "par" non proposition
            if (previousItem.getTag().equals("VPP") && item.getToken().equals("par")) {
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(500);
            }

            // 509 - "à" + infinitif => "à" non proposition
            if (previousItem.getTag().equals("VINF") && previousItem.getToken().equals("à")) {
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(509);
            }

            // 510 - "en" + "participe présent" => "en" non proposition
            if (item.getTag().equals("VPP") && previousItem.getToken().equals("en")) {
                previousItem.setIsword(true);
                previousItem.setIsprop(false);
                previousItem.setRulenumber(510);
            }

            // 512 - "de" non prop si précédé de venir; "à" non prop si précédé de aller, voyager
            if (item.getToken().equals("à") || item.getToken().equals("au")) {
                String lemma = previousItem.getLemma(); 
                if (lemma.equals("aller") || lemma.equals("sortir") || lemma.equals("rentrer") || lemma.equals("entrer") || lemma.equals("rendre")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(512);
                }
            }
            else if (item.getToken().equals("de")) {
                String lemma = previousItem.getLemma();
                if (lemma.equals("arriver") || lemma.equals("entrer") || lemma.equals("rentrer") || lemma.equals("revenir") || lemma.equals("venir") || b.get(i - 1).getLemma().equals("occuper") || lemma.equals("essayer")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(512);
                }
            }

            // 701 - Mots composés
            if (item.getLemma().equals("jusque")) {
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(701);
            }
            else if (previousItem.getToken().equals("bien") && item.getToken().equals("sûr")) {
                previousItem.setIsword(true);
                previousItem.setIsprop(true);
                previousItem.setRulenumber(701);
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(701);
            }
            else if (previousItem.getToken().equals("de") || (item.getToken().equals("plus") && previousItem.getToken().equals("en"))) {
                previousItem.setIsword(true);
                previousItem.setIsprop(false);
                previousItem.setRulenumber(701);
                item.setIsword(true);
                item.setIsprop(true);
                item.setRulenumber(701);
            }
            else if (previousItem.getLemma().equals("parce") && item.getToken().equals("que")) {
                item.setIsword(true);
                item.setIsprop(true);
                item.setRulenumber(701);
            }
            else if (previousItem.getLemma().equals("tant") && (item.getToken().equals("pis") || item.equals("mieux"))) {
                previousItem.setIsword(true);
                previousItem.setIsprop(true);
                previousItem.setRulenumber(701);
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(701);
            }
            else if (i-2 > 0) {
                Item item2 = b.get(i - 2);
                if (item2.getLemma().equals("petit") && previousItem.getLemma().equals("pour") && item.getTag().contains("NC")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                }
                else if (item2.getLemma().equals("petit") && previousItem.equals("à") && item.getLemma().equals("petit")) {
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(701);
                }
                else if (item2.getToken().equals("tout") && (previousItem.getToken().equals("de") && item.getToken().equals("même"))) {
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                }
                else if (item2.getLemma().equals("de") && previousItem.getToken().equals("toute") && item.getToken().equals("façon")) {
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                }
                else if (item2.getToken().equals("à") && previousItem.getToken().equals("peu") && item.equals("près")) {
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                }
                else if ((item2.getToken().equals("du") && previousItem.getToken().equals("fait")) ||
                         (item2.getToken().equals("en") && previousItem.getToken().equals("tant")) ||
                         (item2.getToken().equals("en") && previousItem.getToken().equals("tant")) && item.getLemma().equals("que")) {
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(701);
                }
                else if (item2.getToken().equals("en") && previousItem.getToken().equals("train") && item.getLemma().equals("de")) {
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(701);
                }
            }
            else if (previousItem.getToken().equals("tout")) {
                if (item.getToken().equals("à") || item.getToken().equals("même")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                }
            }
            else if (previousItem.getToken().equals("quand")) {
                if (item.equals("-même") || item.equals("même")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                }
            }
            else if (previousItem.getLemma().equals("entre") && item.getLemma().equals("autre")) {
                item.setIsword(true);
                item.setIsprop(false);
                item.setRulenumber(701);
            }
            else if (item.getLemma().equals("que")) {
                String token = previousItem.getToken();
                if (token.equals("alors") || token.equals("bien") || token.equals("dès") || token.equals("pendant")) {
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                } 
            }
            else if (previousItem.getToken().equals("vis-à-vis") && item.getLemma().equals("de")) {
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(701);
            }
            else if (i-3 > 0) {
                Item item2 = b.get(i-2);
                Item item3 = b.get(i-3);
                
                if (item3.getLemma().equals("tout") && item2.getLemma().equals("de") && previousItem.getToken().equals("un") && item.getToken().equals("coup")) {
                    item3.setIsword(true);
                    item3.setIsprop(false);
                    item3.setRulenumber(701);
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                }
                else if (item3.getToken().equals("vis") && item2.getToken().equals("à") && previousItem.getToken().equals("vis") && item.getLemma().equals("de")) {
                    item3.setIsword(true);
                    item3.setIsprop(false);
                    item3.setRulenumber(701);
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(701);
                }
                else if (item3.getToken().equals("de") && item2.getToken().equals("temps") && previousItem.getToken().equals("en") && item.getLemma().equals("temps")) {
                    item3.setIsword(true);
                    item3.setIsprop(true);
                    item3.setRulenumber(701);
                    item2.setIsword(true);
                    item2.setIsprop(false);
                    item2.setRulenumber(701);
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(701);
                    item.setIsword(true);
                    item.setIsprop(false);
                    item.setRulenumber(701);
                }
            }

            //TODO table
            // 702 - Mots composés avec "par"
            if (previousItem.getToken().equals("par")) {
                String token = item.getToken();
                if (token.equals("ailleurs") || token.equals("après") || token.equals("avance") ||
                    token.equals("bonheur") || token.equals("chance") || token.equals("conséquent") ||
                    token.equals("contre") || token.equals("derrière") || token.equals("ici") ||
                    token.equals("là") || token.equals("là-bas") || token.equals("malheur") || 
                    token.equals("suite")) {
                    previousItem.setIsword(true);
                    previousItem.setIsprop(false);
                    previousItem.setRulenumber(702);
                    item.setIsword(true);
                    item.setIsprop(true);
                    item.setRulenumber(702);
                }
            }
            else if (previousItem.getToken().equals("de") && item.getToken().equals("par")) {
                previousItem.setIsword(true);
                previousItem.setIsprop(false);
                previousItem.setRulenumber(702);
                item.setIsword(true);
                item.setIsprop(true);
                item.setRulenumber(702);
            }
            else if (i-2 > 0) {
                Item item2 = b.get(i - 2);
                if (item2.getToken().equals("par") && (previousItem.getToken().equals("rapport"))) {
                    if (item.getToken().equals("à") || (previousItem.getToken().equals("la") && item.equals("suite"))) {
                        previousItem.setIsword(true);
                        previousItem.setIsprop(false);
                        previousItem.setRulenumber(702);
                        item.setIsword(true);
                        item.setIsprop(true);
                        item.setRulenumber(702);
                    }
                    else if (item.getToken().equals("à") || (previousItem.getToken().equals("la") && item.getToken().equals("suite"))) {
                        item2.setIsword(true);
                        item2.setIsprop(false);
                        item2.setRulenumber(701);
                        previousItem.setIsword(true);
                        previousItem.setIsprop(false);
                        previousItem.setRulenumber(702);
                        item.setIsword(true);
                        item.setIsprop(true);
                        item.setRulenumber(702);
                    }
                }  
            }

             // 703 - Mots composés avec "avoir"
             if (item.getToken().equals("après") || item.getToken().equals("contre") && i-2 > 0) {
                 Item item2 = b.get(i - 2); 
                 if (item2.getLemma().equals("en") && previousItem.getLemma().equals("avoir")) {
                     item2.setIsword(true);
                     item2.setIsprop(false);
                     item2.setRulenumber(703);
                     previousItem.setIsword(true);
                     previousItem.setIsprop(false);
                     previousItem.setRulenumber(703);
                     item.setIsword(true);
                     item.setIsprop(true);
                     item.setRulenumber(703);
                }
            }
        }
    }
}
