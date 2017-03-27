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
package lu.list.itis.dkd.assess.opennlp.util;

import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class GrammarHelper {
    
    public static boolean isPluralArticle(String article, Language language){
        switch (language) {
            case FR:
                //TODO
                return false;
            case DE:
                if (article.toLowerCase().equals("die")) {
                    return true;
                }
                if (article.toLowerCase().equals("den")) {
                    return true;
                }
                if (article.toLowerCase().equals("eine")) {
                    return true;
                }
                if (article.toLowerCase().equals("einer")) {
                    return true;
                }
                return false;
            default:
              //TODO
                return false;
        }
    }
    
    public static boolean isPluralArticle(Word word){
        return isPluralArticle(word.getContent(), word.getLanguage());
    }
    
    public static boolean isPlural(String noun, String tag, Language language) {
        switch (language) {
            case FR:
                String token = noun;
                if (token.charAt(token.length()-1) == 's') {
                    return true;
                }   
                return false;
            case DE: 
                //http://www.dietz-und-daf.de/GD_DkfA/Gramminfo/txt_MII1/Plural%20Q1%20Info.pdf
                //http://www.germanveryeasy.com/plural
                String ending = "";                 
                //Ending: nisse
                if (noun.length() > 5) {
                    ending = noun.substring(noun.length()-5, noun.length()); 
                    if (ending.equals("nisse")) {
                        return true;
                    }
                }
                //Ending: äuse, usse
                if (noun.length() > 4) {
                    ending = noun.substring(noun.length()-4, noun.length()); 
                    if (ending.equals("äuse") || ending.equals("usse")) {
                        return true;
                    }
                }
                //Ending: ern, eln, ten
                if (noun.length() > 3) {
                    ending = noun.substring(noun.length()-3, noun.length()); 
                    if (ending.equals("ern") || ending.equals("eln") || ending.equals("ten")) {
                        return true;
                    }
                }
                //Ending: en, er, as, os, is, us
                if (noun.length() > 2) {
                    ending = noun.substring(noun.length()-2, noun.length()); 
                    if (ending.equals("en") || ending.equals("er") || ending.equals("as") ||
                        ending.equals("os") || ending.equals("is") || ending.equals("us")) {
                        return true;
                    }
                }
                //Ending: e
                ending = Character.toString(noun.charAt(noun.length()-1)); 
                if (ending.equals("e")) {
                    return true;
                }
                return false;
            default:
                if (tag.equals("NNS") || tag.equals("NNPS")) {
                    return true;
                }
                //NN || NNP
                return false;
        }
    }
    
    public static boolean isPlural(Word word) {
        return isPlural(word.getContent(), word.getTag(), word.getLanguage());
    }
    
    public static boolean isProp(Word word) {
        return isProp(word.getTag(), word.getLanguage());
    }
    
    public static boolean isNumber(Word word) {
        return isNumber(word.getTag(), word.getLanguage());
    }
    
    public static boolean isNumber(String tag, Language language) {
        switch (language) {
            case FR:
                if (tag.equals("CARD")) {
                    return true;
                }
                return false;
            case DE: 
                if (tag.equals("CARD")) {
                    return true;
                }
                return false;
            default:
                if (tag.equals("CD")) {
                    return true;
                }
                return false;
        }       
    }
   
    
    public static boolean isProp(String tag, Language language) {
        switch (language) {
            case FR:
                if (tag.equals("KON") || tag.equals("NUM")) {
                    return true;
                }
//                if (tag.contains("DET")) {
//                    return true;
//                }
                if (tag.contains("PREF")) {
                    return true;
                }
//                if (tag.equals("ADJ") || tag.equals("ADV")) {
//                    return true;
//                }
                if (isVerb(tag, language)) {
                    return true;
                }
                if (tag.equals("PROREL")) {
                    return true;
                }
                return false;
            case DE:
                //coordinating conjunction
                if (tag.equals("CC")) {
                    return true;
                }
                //determiner
                if (tag.equals("DT")) {
                    return true;
                }
                //adjective
                if (tag.equals("ADJA") || tag.equals("ADJD")) {
                    return true;
                }
                //possessive pronoun
                if (tag.equals("PPOSS") || tag.equals("PPOSAT")) {
                    return true;
                }
                //Demonstrativpronomina
                if(tag.equals("PDAT") || tag.equals("PDS")) {
                    return true;
                }
                if (isAdv(tag, language)) {
                    return true;
                }
                //Negationspartikel
                if (tag.equals("PTKNEG")) {
                    return true;
                }
                if (tag.equals("PROAV")) {
                    return true;
                }
                //Doppelkonjunktion
//                if (tag.equals("KON") || tag.equals("KOUS") || tag.equals("KOUI")) {
//                    return true;
//                }
                //Indefinitpronomina
                if (tag.equals("PIAT") || tag.equals("PIDAT")) {
                    return true;
                }
                if (isVerb(tag, language)) {
                    return true;
                }
                //interrogatives/relatives
                if (tag.equals("WDT") || tag.equals("WP") || tag.equals("WPS") || tag.equals("WRB")) {
                    return true;
                }
                //Infinitiv verb with "zu"
                if (tag.equals("VVIZU")) {
                    return true;
                }
                //Interrogativ
                if (tag.equals("PWS") || tag.equals("PWAT") || tag.equals("PWAV") || tag.equals("PRELS")) {
                    return true;
                }
                return false;
            default:
                //coordinating conjunction
                if (tag.equals("CC")) {
                    return true;
                }
                //cardinal numeral
                if (tag.equals("CD")) {
                    return true;
                }
                //determiner
                if (tag.equals("DT")) {
                    return true;
                }
                //preposition/subordinating conj.
//                if (tag.equals("IN")) {
//                    return true;
//                }
                if (isAdj(tag, language)) {
                    return true;
                }
                //predeterminer
                if (tag.equals("PDT")) {
                    return true;
                }
                //possessive "'s" (which is not counted as a word)
                if (tag.equals("POS")) {
                    return true;
                }
//                //possessive pronoun
//                if (tag.equals("PRP") || tag.equals("PRP$")) {
//                    return true;
//                }
//                //possessive pronoun
//                if (tag.equals("PP$") || tag.equals("PP")) {
//                    return true;
//                }
                if (isAdv(tag, language)) {
                    return true;
                }
                //"to" whether prep. or infinitival
                if (tag.equals("TO")) {
                    return true;
                }
                if (isVerb(tag, language)) {
                    return true;
                }
                if (tag.equals("WDT") || tag.equals("WP") || tag.equals("WPS") || tag.equals("WRB")) {
                    return true;
                }
                return false;
        }
    }

    public static boolean isNoun(String tag, Language language) {
        switch (language) {
            case DE:
                if (tag.equals("NE") || tag.equals("NN")) {
                    return true;
                }
                return false;
            case FR:
                if (tag.equals("NC") || tag.equals("NP") || tag.equals("NPP")) {
                    return true;
                }
                return false;
            case EN:
                if (tag.equals("NN") || tag.equals("NNS") || tag.equals("NNP") || tag.equals("NNPS")) {
                    return true;
                }
                return false;
            default:
                //TODO logger!!!
                return false;
        }
    }
    
    public static boolean isNoun(Word word) {
        return isNoun(word.getTag(), word.getLanguage());
    }

    public static boolean isVerb(final Word word) {
        return isVerb(word.getTag(), word.getLanguage());
    }
    
    public static boolean isVerb(String tag, Language language) {
        switch (language) {
            case DE:
                if (tag.contains("VA") || tag.contains("VV") || tag.contains("VM")) {
                    return true;
                }
                return false;
            case FR:
                if (tag.equals("V") || tag.equals("VINF") || tag.equals("VPP")) {
                    return true;
                }
                return false;
            default:
                if (tag.contains("VB")) {
                    return true;
                }
                return false;
        }
    }

    public static boolean isAdj(Word word) {
        return isAdj(word.getTag(), word.getLanguage());
    }
    
    public static boolean isAdj(String tag, Language language) {
        switch (language) {
            case DE:
                if (tag.contains("ADJ")) {
                    return true;
                }
                return false;
            case FR:
                if (tag.equals("A") || tag.contains("ADJ")) {
                    return true;
                }
                return false;
            default:
                if (tag.contains("JJ")) {
                    return true;
                }
                return false;
        }
    }

    public static boolean isAdv(final Word word) {
        return isAdv(word.getTag(), word.getLanguage());
    }
    
    public static boolean isAdv(String tag, Language language) {
        switch (language) {
            case DE:
                if (tag.equals("ADV")) {
                    return true;
                }
                return false;
            case FR:
                if (tag.equals("Adv")) {
                    return true;
                }
                return false;
            default:
                if (tag.contains("RB")) {
                    return true;
                }
                return false;
        }
    }
}
