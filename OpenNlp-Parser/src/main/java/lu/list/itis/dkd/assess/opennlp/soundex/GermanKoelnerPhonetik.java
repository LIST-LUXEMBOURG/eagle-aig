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

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class GermanKoelnerPhonetik {

    // Kölner Phonetik https://de.wikipedia.org/wiki/K%C3%B6lner_Phonetik 
    //Beispiel: "Wikipedia" = "3412" 
    
    //TODO Ask winfried.hoehn@uni.lu
    /**Ich verwende immer den kompletten Code und nur wenn der übereinstimmt nehme ich das als ähnlich klingende
     * Namen. Da ich das nur mit Ortsnamen mache würde sonst auch XY-hausen und XY-burg gleich klingen 
     * (wenn XY lang genug ist um 4 Symbole zu produzieren). Wenn man das Wortende ignorieren will, dann kann man 
     * das sicher analog zum Soundex machen und nach 4 Zeichen abschneiden. Ich denke aber durch die 
     * zusammengesetzten Wörter im Deutschen bringt das mehr Probleme als Vorteile.
    **/
    
    static String getOriginal(String word) {
        return koelnerPhonetik(word);
    }
    
    static String getFixedLength(String word, int length) {
        String phonetik = koelnerPhonetik(word);
        
        //Larger
        if (phonetik.length() > length) {
            String res = "";
            for (int i = 0; i < length; i++) {
                res += phonetik.charAt(i);
            }
            
            return res;
        }
        
        //Smaller
        if (phonetik.length() < length) {
            int numberOfZerosToAdd = length - phonetik.length();
            for (int i = 0; i < numberOfZerosToAdd; i++) {
                phonetik += '0';
            }
            return phonetik;
        }
        
        //Same size
        return phonetik;
    }
    
    private static String koelnerPhonetik(String word) {
        String res1 = "";
        String temp = word.toUpperCase();
        
        char previousChar = ' ';
        char nextChar = ' ';
        for (int i = 0; i < temp.length(); i++) {
            char currentChar = temp.charAt(i);
            
            if ((i-1) > 0) {
                previousChar = temp.charAt(i-1);
            }
            
            if ((i+1) < temp.length()) {
                nextChar = temp.charAt(i+1);
            }
            
            char replacedChar = replaceCharacter(currentChar, previousChar, nextChar);
            if (replacedChar == 'X') {
                res1 += "48";
                continue;
            }
            
//            System.out.println(currentChar + " - " + replacedChar);
            
            res1 += replacedChar;
        }
        
        //Remove all multiple same numbers.
        String res2 = "";
        res2 += res1.charAt(0);
        for (int i = 1; i < res1.length(); i++) {
            previousChar = res1.charAt(i-1);
            char currentChar = res1.charAt(i);
            
            if (previousChar != currentChar) {
                res2 += currentChar;
            }
        }
        
        //Remove all 0 but not the first one
        String res3 = "";
        res3 += res2.charAt(0);
        for (int i = 1; i < res2.length(); i++) {
            char currentChar = res2.charAt(i);
            
            if (currentChar != '0') {
                res3 += currentChar;
            }
        }
            
        return res3;
        
    }

    private static char replaceCharacter(char character, char previousCharacter, char nextCharacter){
        switch (character) {
            case 'B':
                return '1';
            case 'C':
                if (previousCharacter == 'S' || previousCharacter == 'Z') {
                    return '8';
                }
                if (nextCharacter == 'A' || nextCharacter == 'H' || nextCharacter == 'K' || 
                    nextCharacter == 'L' || nextCharacter == 'O' || nextCharacter == 'Q' || 
                    nextCharacter == 'R' || nextCharacter == 'U' || nextCharacter == 'X') {
                    return '4';
                }
            case 'D':
                if (previousCharacter != 'C' && previousCharacter != 'S' && previousCharacter != 'Z') {
                    return '2';
                }
                return character;
            case 'F':
                return '3';
            case 'G':
                return '4';
            case 'K':
                return '4';
            case 'L':
                return '5';
            case 'M':
                return '6';
            case 'N':
                return '6';
            case 'P':
                if (previousCharacter != 'H') {
                    return '1';
                }
                return '3';
            case 'Q':
                return '4';
            case 'R':
                return '7';    
            case 'S':
                return '8';
            case 'T':
                if (previousCharacter != 'C' && previousCharacter != 'S' && previousCharacter != 'Z') {
                    return '2';
                }
                return '8';
            case 'V':
                return '3';
            case 'W':
                return '3';
            case 'X':
                if (previousCharacter != 'C' && previousCharacter != 'K' && previousCharacter != 'Q') {
                    return character;
                }
                return '8';
            case 'Z':
                return '8';
            case 'ß':
                return '8';
            default:
                //A, E, I, J, O, U, Y
                return '0';
        }
    }
    
    //P nicht 1 vor H
    //D & T nicht 2 vor C, S, Z
    
}
