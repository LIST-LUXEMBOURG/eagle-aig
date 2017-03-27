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
package lu.list.itis.dkd.aig.util;

import lu.list.itis.dkd.aig.Variable;
import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.text.Normalizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Method for computing the Soundex and Soundex differences given two {@link Variable} instances.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.8.0
 */
@SuppressWarnings("nls")
@NonNullByDefault
public final class Soundex {
    private static Map<Character, Character> valueMap;
    private static Map<Character, Character> frenchValueMap;

    private static final String FRENCH = Locale.FRENCH.getLanguage();
    private static final String GERMAN = Locale.GERMAN.getLanguage();
    private static final String ENGLISH = Locale.ENGLISH.getLanguage();

    static {
        Soundex.valueMap = new HashMap<>();
        Soundex.valueMap.put('A', '0');
        Soundex.valueMap.put('B', '1');
        Soundex.valueMap.put('C', '2');
        Soundex.valueMap.put('D', '3');
        Soundex.valueMap.put('E', '0');
        Soundex.valueMap.put('F', '1');
        Soundex.valueMap.put('G', '2');
        Soundex.valueMap.put('H', '0');
        Soundex.valueMap.put('I', '0');
        Soundex.valueMap.put('J', '2');
        Soundex.valueMap.put('K', '2');
        Soundex.valueMap.put('L', '4');
        Soundex.valueMap.put('M', '5');
        Soundex.valueMap.put('N', '5');
        Soundex.valueMap.put('O', '0');
        Soundex.valueMap.put('P', '1');
        Soundex.valueMap.put('Q', '2');
        Soundex.valueMap.put('R', '6');
        Soundex.valueMap.put('S', '2');
        Soundex.valueMap.put('T', '3');
        Soundex.valueMap.put('U', '0');
        Soundex.valueMap.put('V', '1');
        Soundex.valueMap.put('W', '0');
        Soundex.valueMap.put('X', '2');
        Soundex.valueMap.put('Y', '0');
        Soundex.valueMap.put('Z', '2');
        Soundex.valueMap = Collections.unmodifiableMap(Soundex.valueMap);

        Soundex.frenchValueMap = new HashMap<>(Soundex.valueMap);
        Soundex.frenchValueMap.put('G', '7');
        Soundex.frenchValueMap.put('J', '7');
        Soundex.frenchValueMap.put('S', '8');
        Soundex.frenchValueMap.put('X', '8');
        Soundex.frenchValueMap.put('Z', '8');
        Soundex.frenchValueMap.put('F', '9');
        Soundex.frenchValueMap.put('V', '9');
        Soundex.frenchValueMap = Collections.unmodifiableMap(Soundex.frenchValueMap);
    }

    /** Private constructor to "prevent" instantiation of class with only static methods. */
    private Soundex() {}

    /**
     * Method for computing the Soundex for a given {@link Variable} dependent on the variable's
     * language and its label. Supported languages are English, German, and French. For unsupported
     * languages, the Soundex is computed in English.
     *
     * @param variable
     *        The {@link Variable} for which to compute the Soundex for.
     * @return The Soundex for the given variable.
     */
    public synchronized static @Nullable String getSoundex(final Variable variable) {
        return Soundex.getSoundex(variable, variable.getLanguage());
    }

    /**
     * Method used to remove accents and code points from a text.
     *
     * @param text
     *        The text to clean.
     * @return The cleaned text.
     */
    private synchronized static @Nullable String cleanText(final String text) {
        return CharMatcher.JAVA_LETTER.retainFrom(Normalizer.normalize(text, Normalizer.Form.NFD));
    }

    /**
     * Method for computing the Soundex for a given {@link Variable}. Supported languages are
     * English, German, and French. For unsupported languages, the Soundex is computed in English.
     * However, all languages with the same character-value equivalence map as English would produce
     * accurate results as long as these languages do not feature any additional rules.
     *
     * @param variable
     *        The {@link Variable} for which to compute the Soundex for.
     * @param language
     *        The language the variable's text content is provided in.
     * @return The Soundex for the given variable. The method will return an empty string should the
     *         provided variable not contain any valid non-null text to work with.
     */
    public synchronized static @Nullable String getSoundex(final Variable variable, final String language) {
        final Map<Character, Character> characterValueMap = Soundex.determineMapToUse(language);
        String text = Strings.nullToEmpty(Soundex.cleanText(new String(variable.getTextContent()))).toUpperCase();

        if (text.isEmpty()) {
            return text;
        }

        // Retain first character.
        // String soundex = Character.toString(text.charAt(0));
        final Character first = text.charAt(0);
        // Remove all instances of "H" and "W".
        text = text.replaceAll("H", "").replaceAll("W", "");
        String soundex = "";

        for (final char character : text.toCharArray()) {
            if (!Character.isLetter(character)) {
                continue; // TODO Maybe throwing an error would be more prudent!
            }
            soundex += characterValueMap.get(character);
        }

        // Remove all occurrences of subsequent digits (there is only one letter at the front).
        soundex = soundex.replaceAll("(.)\\1", "$1");
        // Replace the first digit with the saves first character and remove all "0" digits from the
        // remainder of the soundex string.
        soundex = Character.toString(first) + soundex.substring(1).replaceAll("0", "");

        // Add padding if necessary.
        while (soundex.length() < 4) {
            soundex += "0";
        }

        return soundex.substring(0, 4);
    }

    /**
     * Method used to retrieve the character-value map to use to build the soundex based on an input
     * language.
     *
     * @param language
     *        The language to check the soundex for.
     * @return An immutable map containing the character-value mapping for the specified language.
     */
    @SuppressWarnings("null")
    private static Map<Character, Character> determineMapToUse(final String language) {
        if (language == Soundex.FRENCH) {
            return Soundex.frenchValueMap;
        }

        if ((language == Soundex.ENGLISH) || (language == Soundex.GERMAN)) {
            return Soundex.valueMap;
        }

        Logger.getLogger(Soundex.class.getSimpleName()).log(Level.WARNING, "Language not recognised, computing soundex for English!");
        return Soundex.valueMap;
    }

    /**
     * Method for computing the difference, in regard to individual Soundex, of two {@link Variable}
     * instances. The method will encode the two labels of the {@link Variable} instances using this
     * class' {@link #getSoundex(Variable)} method. Should the languages of both {@link Variable}
     * instances be different, the method will use the first class' language to determine the
     * encoding language.
     *
     * @param first
     *        The first {@link Variable}.
     * @param second
     *        The first {@link Variable}.
     * @pre getSoundex(first).length() == getSoundex(second).length() == 4
     * @param forceLanguage
     *        If set, the language of the first parameter in the array will be used to force the
     *        language of the Soundex computation. Supported choices are:<br>
     *        <ul>
     *        <li><code>Locale.FRENCH.getLanguage();</code> which maps to fr,</li>
     *        <li><code>Locale.GERMAN.getLanguage();</code> which maps to de,</li>
     *        <li><code>Locale.ENGLISH.getLanguage();</code> which maps to en.</li>
     *        </ul>
     *        All other inputs default to using the English Soundex.
     * @return The difference in Soundex ranging from 0 to 4 such that the difference is 0 if
     *         <code>first.equals(second)</code> and the difference is 4 if all characters in both
     *         labels are different.
     * @see #getSoundex(Variable)
     */
    @SuppressWarnings("null")
    public synchronized static int difference(final Variable first, final Variable second, final String... forceLanguage) {
        final String firstSoundex = Soundex.getSoundex(first, forceLanguage.length != 0 ? forceLanguage[0] : first.getLanguage());
        final String secondSoundex = Soundex.getSoundex(second, forceLanguage.length != 0 ? forceLanguage[0] : first.getLanguage());

        Strings.nullToEmpty(firstSoundex);
        Strings.nullToEmpty(secondSoundex);

        Preconditions.checkState((firstSoundex.length() == secondSoundex.length()) && (firstSoundex.length() == 4));

        if (firstSoundex.equals(secondSoundex)) {
            return 0;
        }

        int difference = 0;
        for (int i = 0; i < 4; i++) {
            if (firstSoundex.charAt(i) != secondSoundex.charAt(i)) {
                difference++;
            }
        }

        return difference;
    }
}