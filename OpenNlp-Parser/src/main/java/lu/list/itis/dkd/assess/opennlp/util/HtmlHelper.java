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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lu.list.itis.dkd.assess.opennlp.Paragraph;
import lu.list.itis.dkd.assess.opennlp.Sentence;
import lu.list.itis.dkd.assess.opennlp.Word;
import lu.list.itis.dkd.assess.opennlp.util.Type.Font;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import opennlp.tools.postag.POSTaggerME;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0.0
 * @version 2.0.0
 */
public class HtmlHelper {

    public static String cleanHtml(String html) {        
        String paragraph = html.replaceAll("&nbsp;", " ");
        paragraph = paragraph.replaceAll("&nbsp", " ");
        paragraph = paragraph.replaceAll("&amp;", "&");
        paragraph = paragraph.replaceAll("&amp", "&");
        return StringHelper.replaceMultiSpaces(paragraph);
    }
    
    public static String removeTags(String html) {
        String paragraph = html.replaceAll("<[^>]*>\\/[a-zA-Z]*", "");
        paragraph = paragraph.replaceAll("<[^>]*>", "");
        paragraph = paragraph.replaceAll("\\s\\s+", " ");        
        return paragraph;
    }

    private static String listTypeBlock(String taggedParagraph, String beginningListTag, String endListTag, String beginningRowType, String endRowType, String mark) {
        if (taggedParagraph.contains(beginningListTag) && taggedParagraph.contains(endListTag)) {
            String frontText = taggedParagraph.substring(0, taggedParagraph.indexOf(beginningListTag));
            String temp = taggedParagraph.substring(taggedParagraph.indexOf(beginningListTag) + 4, taggedParagraph.indexOf(endListTag));
            String endText = taggedParagraph.substring(taggedParagraph.indexOf(endListTag) + 5, taggedParagraph.length());

            String middleText = "";
            while (temp.contains(beginningRowType)) {
                int beginning = temp.indexOf(beginningRowType);
                int ending = temp.indexOf(endRowType);

                // Find list block
                String listBlock = temp.substring(beginning + 4, ending);
                String replacement = temp.substring(beginning, ending + 5);
                temp = temp.replace(replacement, "");

                // Transform list block for later use
                int pos = 1;
                for (String sentence : transform2Sentences(listBlock)) {
                    middleText += mark + pos + " " + sentence;
                    
                    pos++;
                }
            }

            // Reconstruct temp
            return frontText + middleText + endText;
        }

        return taggedParagraph;
    }

    private static String findListings(String taggedParagraph) {
        String temp = taggedParagraph;

        // Definiotn List
        temp = listTypeBlock(temp, "<dl>", "</dl>", "<dt>", "</dt>", "@definition");

        // Unordered List
        temp = listTypeBlock(temp, "<ul>", "</ul>", "<il>", "</il>", "@unordered");

        // Ordered List
        temp = listTypeBlock(temp, "<ol>", "</ol>", "<li>", "</li>", "@ordered");

        return temp;
    }

    private static List<String> transform2Sentences(String text) {
        List<String> sentences = new ArrayList<>();
        Pattern sentencePattern = Pattern.compile("\\s*([^.!?]+)\\s*");
        Matcher sentenceMatcher = sentencePattern.matcher(text);

        String temp = text;
        while (sentenceMatcher.find()) {
            // Find sentence ending.
            String sentenceMatch = sentenceMatcher.group(1);
            temp = temp.replace(sentenceMatch, "").trim();
            Pattern sentenceEndingPattern = Pattern.compile("(^[.?!])");
            Matcher sentenceEndingMatcher = sentenceEndingPattern.matcher(temp);

            // Add sentence ending. If none is found, add "."
            String sentenceEnding = ".";
            while (sentenceEndingMatcher.find()) {
                sentenceEnding = sentenceEndingMatcher.group(1);
            }

            sentenceMatch = sentenceMatch + sentenceEnding;
            int sentenceEndingIndex = temp.indexOf(sentenceEnding);

            if (temp.length() > sentenceEndingIndex + 1) {
                temp = temp.substring(sentenceEndingIndex + 1, temp.length());
            }

            if (sentenceMatch.length() > 2) {
                sentences.add(sentenceMatch);
            }
        }

        return sentences;
    }
    
    public static List<Sentence> breakTaggedHTMLParagraphIntoSentences(String htmlParagraph, Language language) {
        final List<Sentence> sentences = new ArrayList<>();        
        String taggedTemp = htmlParagraph;

        //Mark list types and clean most html tags
        taggedTemp = findListings(taggedTemp);

        // Find sentences
        Pattern sentencePattern = Pattern.compile("\\s*([^.!?]+)\\s*");
        Matcher sentenceMatcher = sentencePattern.matcher(taggedTemp);

        int pos = 1;
        String temp = taggedTemp;
        while (sentenceMatcher.find()) {
            // Find sentence ending.
            String sentenceMatch = sentenceMatcher.group(1).trim();
            temp = temp.replace(sentenceMatch, "").trim();
            Pattern sentenceEndingPattern = Pattern.compile("(^[.?!])");
            Matcher sentenceEndingMatcher = sentenceEndingPattern.matcher(temp);
        
            // Add sentence ending. If none is found, add "."
            String sentenceEnding = ".";
            while (sentenceEndingMatcher.find()) {
                sentenceEnding = sentenceEndingMatcher.group(1);
            }

            sentenceMatch = sentenceMatch + sentenceEnding;
            int sentenceEndingIndex = temp.indexOf(sentenceEnding);

            if (temp.length() > sentenceEndingIndex + 1) {
                temp = temp.substring(sentenceEndingIndex + 1, temp.length());
            }

            // Look for sentence List typ
            String type = "default";
            if (sentenceMatch.contains("@definition")) {
                int beginning = sentenceMatch.indexOf("@definition");                
                String definitionBeginning = sentenceMatch.substring(beginning, sentenceMatch.length());
                String ListType = definitionBeginning.substring(0, definitionBeginning.indexOf(" "));
                sentenceMatch = sentenceMatch.replace(ListType, "").trim();
                type += ListType;
            }
            if (sentenceMatch.contains("@ordered")) {
                int beginning = sentenceMatch.indexOf("@ordered");
                String definitionBeginning = sentenceMatch.substring(beginning, sentenceMatch.length());
                String ListType = definitionBeginning.substring(0, definitionBeginning.indexOf(" "));
                sentenceMatch = sentenceMatch.replace(ListType, "").trim();
                type += ListType;
            }
            if (sentenceMatch.contains("@unordered")) {
                int beginning = sentenceMatch.indexOf("@unordered");
                String definitionBeginning = sentenceMatch.substring(beginning, sentenceMatch.length());
                String ListType = definitionBeginning.substring(0, definitionBeginning.indexOf(" "));
                sentenceMatch = sentenceMatch.replace(ListType, "").trim();
                type += ListType;
            }

            //Add sentence part to the previous sentence
            String words[] = sentenceMatch.split(" ");
            if (words.length < 3 && pos > 1) {
                //Extent previous sentence object
                String previousSentence = sentences.get(pos-2).getContent() + sentenceMatch;
                sentences.get(pos-2).setContent(previousSentence);
                continue;
            }
            
            Sentence sentence = new Sentence(sentenceMatch, pos, type, language);
            if(!sentence.getWords().isEmpty()){
                sentences.add(sentence);
            }
            pos++;
        }

        return sentences;
    }
    
    public static String replaceFontTags(String html) {
        String temp = html;
        temp = temp.replace("<u>", "").replace("</u>", "");
        temp = temp.replace("<strong>", "").replace("</strong>", "");
        temp = temp.replace("<em>", "").replace("</em>", "");        
        return temp;
    }
    
    private static Font wordFont(String font){ 
        switch (font) {
            case "default":
                return Font.DEFAULT;
            case "default_underline":
                return Font.UNDERLINE;
            case "default_bold":
                return Font.BOLD;
            case "default_italic":
                return Font.ITALIC;
            case "default_underline_bold":
                return Font.BOLD_UNDERLINE;
            case "default_underline_italic":
                return Font.ITALIC_UNDERLINE;
            case "default_bold_italic":
                return Font.ITALIC_BOLD;
            case "default_underline_bold_italic":
                return Font.ITALIC_BOLD_UNDERLINE;
            default:
                return Font.DEFAULT;
        }
    }

    @SuppressWarnings("deprecation")
    public static List<Word> breakHtmlSentenceIntoWords(String htmlSentence, Language language) {
        String temp = cleanHtml(htmlSentence);        
        temp = StringHelper.replaceNonWordSymbols(temp);
        String[] tokens = temp.split(" ");
        List<Word> words = new ArrayList<>();
        
        int pos = 1;
        String font = "default";
        Font finalFont = Font.DEFAULT;
        for (String token : tokens) {
            if (token.equals("")) {
                continue;
            }
            
            finalFont = wordFont(font);
            
            //Remember underline font
            if (token.contains("<u>")) {
                font += "_underline";
                finalFont = wordFont(font);
            }
            if (token.contains("</u>")) {
                finalFont = wordFont(font);
                font = font.replace("_underline", "");
            }
            
            //Remember bold font
            if (token.contains("<strong>")) {
                font += "_bold";
                finalFont = wordFont(font);
            }
            if (token.contains("</strong>")) { 
                finalFont = wordFont(font); 
                font = font.replace("_bold", "");
            }
            
            //Remember italic
            if (token.contains("<em>")) {
                font += "_italic";
                finalFont = wordFont(font);
            }
            if (token.contains("</em>")) {
                finalFont = wordFont(font);
                font = font.replace("_italic", "");
            }

            String wordString = replaceFontTags(token);
            POSTaggerME posTagger = ModelLoader.getPosModel(language);
            String tag = posTagger.tag(wordString);
            tag = tag.substring(tag.indexOf("/")+1, tag.length());
            Word word = new Word(wordString.trim(), tag, finalFont, pos, language);
            words.add(word);
            pos++;
        }
        
        return words;
        
    }

    /**
     * TODO description
     * @param html
     * @return
     */
    public static String findHeadings(String html) {   
        // Header 1
        if (html.contains("<h1>") && html.contains("</h1>")) {
            int beginning = html.indexOf("<h1>");
            int ending = html.indexOf("</h1>") + 5;
            return html.substring(beginning, ending).trim();
        }

        // Header 2
        if (html.contains("<h2>") && html.contains("</h2>")) {
            int beginning = html.indexOf("<h2>");
            int ending = html.indexOf("</h2>") + 5;            
            return html.substring(beginning, ending).trim();
        }

        // Header 3
        if (html.contains("<h3>") && html.contains("</h3>")) {
            int beginning = html.indexOf("<h3>");
            int ending = html.indexOf("</h3>") + 5;
            return html.substring(beginning, ending).trim();
        }

        // Header 4
        if (html.contains("<h4>") && html.contains("</h4>")) {
            int beginning = html.indexOf("<h4>");
            int ending = html.indexOf("</h4>") + 5;
            return html.substring(beginning, ending).trim();
        }

        return "";
    }
    
    private static String getHtmlBody(String html, String body){
        String temp = html;
        
        int begin = temp.indexOf(body);
        temp = temp.substring(begin, temp.length());
        
        int end = temp.length();
        if (temp.contains("class=")) {
            end = temp.indexOf("class=");
        }
        
        temp = temp.substring(temp.indexOf(">")+1, end);
        temp = temp.substring(0, temp.lastIndexOf("<"));
        temp = temp.replaceAll("</div>", "");
        temp = temp.replaceAll("<div>", "");
        
        return temp.trim();
    }
    
    private static Paragraph createParagraph(String paragraphString, String header, int pos, Language language) {        
        return new Paragraph(paragraphString, header, pos, language);
    }

    public static List<Paragraph> breakHtmlTextIntoParagraph(String xhtml, String bodyClassName, Language language) {
        String html = getHtmlBody(xhtml, bodyClassName);
        List<Paragraph> paragraphs = new ArrayList<>();
        int pos = 1;
        
        int index = html.indexOf("<p");
        int possibleHeaderIndex = 0;
        String header = "";
            
        //Stuff in front of first paragraph
        if (index > 0) {
            String paragraphInFront = html.substring(0, index);
            header = findHeadings(paragraphInFront);
            // if header exists
            if (!header.equals("")) {
            	// remove header
                paragraphInFront = paragraphInFront.replaceFirst(header, "").trim();
                possibleHeaderIndex = index + 3;
            }
                
            // if paragraph exists
            if (!paragraphInFront.equals("")) {
                paragraphs.add(createParagraph(paragraphInFront, header, pos, language));
                pos++;
            }
        }  
            
        //Find all paragraphs with "p" tag.
        while (index >= 0) {
            int endIndex = html.indexOf("/p>", index +3);
            String ending = html.substring(index + 3, endIndex - 1);
            String paragraphString = html.substring(index, index + 3) + " " + ending;
                
            paragraphString = paragraphString.replaceAll("<p[^>]*>", "");
            if (header.equals("")) {
                paragraphs.add(createParagraph(paragraphString, findHeadings(html.substring(possibleHeaderIndex, index)), pos, language));
                possibleHeaderIndex = endIndex+3;
            }
            else {
                paragraphs.add(createParagraph(paragraphString, header, pos, language));
                header = "";
            }
                
            pos++;          
            index = html.indexOf("<p", index + 1);
                
            //Is there a paragraph between 2 "p" tags
            if (index-1 > endIndex+3) {
                String paragraphInBetween = html.substring(endIndex+3, index-1);
                    
                //isHeader?
                header = findHeadings(paragraphInBetween);
                if (!header.equals("")) {
                    paragraphInBetween = paragraphInBetween.replaceFirst(header, "").trim();
                }
                    
                if (!paragraphInBetween.equals("")){
                    paragraphs.add(createParagraph(paragraphInBetween, header, pos, language));
                    header = "";
                    possibleHeaderIndex = endIndex;
                    pos++;
                }
            }
        }
        
        if (paragraphs.isEmpty()) {
            Paragraph paragraph = new Paragraph(html, "", pos, language);
            paragraphs.add(paragraph);
        }        

        return paragraphs;
    }

}
