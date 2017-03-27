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
package lu.list.itis.dkd.assess.opennlp.soundex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class FrenchSoundex {
    private static char replaceCharacter(char character){
        switch (character) {
            case 'B':
                return '1';
            case 'C':
                return '2';
            case 'D':
                return '3';
            case 'F':
                return '9';
            case 'G':
                return '7';
            case 'J':
                return '7';
            case 'K':
                return '2';
            case 'L':
                return '4';
            case 'M':
                return '5';
            case 'N':
                return '5';
            case 'P':
                return '1';
            case 'Q':
                return '2';
            case 'R':
                return '6';
            case 'S':
                return '8';
            case 'T':
                return '3';
            case 'V':
                return '9';
            case 'X':
                return '8';
            case 'Z':
                return '8';
            default:
                //A, E, H, O, U, W, Y, I
                return '0';
        }
    }
    
    private static List<Character> remove0(List<Character> characters) {
        List<Character> res = new ArrayList<>();
        res.add(characters.get(0));
        for (int i = 1; i < characters.size(); i++) {
            if (characters.get(i) != '0') {
                res.add(characters.get(i));
            }
        }
        return res;
    }
    
    private static List<Character> removeAdjencentSoundex(List<Character> characters){
        List<Character> res = new ArrayList<>();
        for (int i = 0; i < characters.size(); i++) {
            if (i+1 < characters.size()) {
                char firstChar = characters.get(i);
                char secondChar = characters.get(i+1);
                
                if (firstChar != secondChar) {
                    res.add(firstChar);
                }
            }
            else {
                res.add(characters.get(i));
            }
        }
        return res;
    }
        
    static String getSoundex(String word) {
        String temp = word.toUpperCase();
        char firstCharacter = temp.charAt(0);
        
        List<Character> step1 = new ArrayList<>();
        for (int i = 0; i < temp.length(); i++) {
            char character = replaceCharacter(temp.charAt(i));
            step1.add(character);
        }
        
        
        //Remove adjencent Soundex
        List<Character> step2 = removeAdjencentSoundex(step1);        
        
        //Remove 0
        List<Character> step3 = remove0(step2);
        
        //Remove adjencent Soundex if soundex is longer than 4.
        List<Character> step4 = new ArrayList<>();
        if (step3.size() > 4) {
            step4.add(step3.get(0));
            step3.remove(0);
            step4.addAll(removeAdjencentSoundex(step3));
        }
        else {
            step4.addAll(step3);
        }
                
        String soundex = firstCharacter + "";
        if (step4.size() < 4) {
            for (int i = 1; i < step4.size(); i++) {
                soundex += step4.get(i);
            }
            for (int i = 0; i < 4-step4.size(); i++) {
                soundex += '0';
            }
        }
        else {
            for (int i = 1; i < 4; i++) {
                soundex += step4.get(i);
            }
        }
        
        return soundex;
    }
}
