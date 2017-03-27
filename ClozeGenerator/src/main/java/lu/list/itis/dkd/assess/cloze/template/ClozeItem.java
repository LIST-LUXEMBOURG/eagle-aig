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
package lu.list.itis.dkd.assess.cloze.template;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Element;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.Distractor;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class ClozeItem {
	private String clozeText;
	private List<ClozeSentence> clozeSentences = new ArrayList<>();
	private List<Gap> gaps = new ArrayList<>();

	/**
	 * Creates a cloze item out of cloze sentence objects.
	 * 
	 * @param clozeSentences:
	 *            List of cloze sentence objects.
	 * @param gaps:
	 *            List of gaps
	 */
	public ClozeItem(final List<ClozeSentence> clozeSentences, final List<Gap> gaps) {
		this.clozeSentences = clozeSentences;
		this.gaps = gaps;
	}

	public String getClozeText() {
		return clozeText;
	}

	public List<ClozeSentence> getClozeSentences() {
		return clozeSentences;
	}

	public List<Gap> getGaps() {
		return gaps;
	}

	/**
	 * Returns the cloze item block element for the template, which is based on
	 * qti structure.
	 * 
	 * @return
	 */
	public Element getClozeBlock() {
		final Element p = new Element("p");
		for (final ClozeSentence clozeSentence : clozeSentences) {
			String temp = clozeSentence.getContent();

			// Serach gap(s) in the clozeSentene.
			final Pattern gapPattern = Pattern.compile("___[0-9]+___");
			final Matcher gapMatcher = gapPattern.matcher(temp);

			while (gapMatcher.find()) {
				// Find gap and transform it into a number
				final String gap = gapMatcher.group(0);
				int identifierIndex = Integer.parseInt(gap.replace("_", "")) - 1;

				// Cut sentence until gap
				final int begin = temp.indexOf(gap);
				final int end = temp.indexOf(gap) + gap.length();

				final String sentencePart = temp.substring(0, begin);
				temp = temp.substring(end, temp.length());
				p.addContent(sentencePart);

				// Create QTI inlineChoiceInteraction block from gap
				final Gap qtiGap = gaps.get(identifierIndex);
				final Element inlineChoiceInteraction = new Element("inlineChoiceInteraction");
				inlineChoiceInteraction.setAttribute("responseIdentifier", "RESPONSE" + (identifierIndex + 1));
				inlineChoiceInteraction.setAttribute("shuffle", "true");
				p.addContent(inlineChoiceInteraction);

				final Element keyInlineChoice = new Element("inlineChoice");
				keyInlineChoice.setAttribute("identifier", qtiGap.getKey().getKeyWord().getContent() + "1");
				keyInlineChoice.setText(qtiGap.getKey().getKeyWord().getContent());
				inlineChoiceInteraction.addContent(keyInlineChoice);

				int optionIndex = 2;
				for (final Distractor distractor : qtiGap.getDistractors()) {
					final String distractorValue = distractor.getDistractorWord().getContent();
					final Element distractorInlineChoice = new Element("inlineChoice");
					distractorInlineChoice.setAttribute("identifier",
							qtiGap.getKey().getKeyWord().getContent() + optionIndex);
					distractorInlineChoice.setText(distractorValue);
					inlineChoiceInteraction.addContent(distractorInlineChoice);
					optionIndex++;
				}
			}

			// Add rest of text
			p.addContent(temp);
		}

		return p;
	}

	/**
	 * Returns the correct response item block elements (Hence the keys) for the
	 * template, which is based on qti structure.
	 * 
	 * @return
	 */
	public List<Element> getCorrectResponseBlocks() {
		ArrayList<Element> correctResponseBlocks = new ArrayList<Element>();
		for (int i = 0; i < gaps.size(); i++) {
			final Gap gap = gaps.get(i);

			final Element responseDeclaration = new Element("responseDeclaration");
			correctResponseBlocks.add(responseDeclaration);
			responseDeclaration.setAttribute("identifier", "RESPONSE" + (i + 1));
			responseDeclaration.setAttribute("cardinality", "single");
			responseDeclaration.setAttribute("baseType", "identifier");

			final Element correctResponse = new Element("correctResponse");
			responseDeclaration.addContent(correctResponse);
			final Element ResponseValue = new Element("value");
			ResponseValue.setText(gap.getKey().getKeyWord().getContent() + "1");
			correctResponse.addContent(ResponseValue);

		}

		return correctResponseBlocks;
	}

	protected static Element createOutcomeDeclaration(String variableIdentifier, String variableType,
			String variableValue) {
		final Element outcomeDeclaration = new Element("outcomeDeclaration");
		outcomeDeclaration.setAttribute("identifier", variableIdentifier);
		outcomeDeclaration.setAttribute("cardinality", "single");
		outcomeDeclaration.setAttribute("baseType", variableType);

		final Element defaultValue = new Element("defaultValue");
		outcomeDeclaration.addContent(defaultValue);

		final Element value = new Element("value");
		defaultValue.addContent(value);
		value.setText(variableValue);

		return outcomeDeclaration;
	}

	public List<Element> getOutcomeDeclarationBlocks(boolean feedback) {
		ArrayList<Element> outcomeDeclarationBlocks = new ArrayList<Element>();

		outcomeDeclarationBlocks.add(createOutcomeDeclaration("SCORE", "float", "0"));
		outcomeDeclarationBlocks.add(createOutcomeDeclaration("MAXSCORE", "float", Integer.toString(gaps.size())));
		outcomeDeclarationBlocks.add(createOutcomeDeclaration("PASSED", "boolean", "false"));

		if (feedback) {
			outcomeDeclarationBlocks.add(createOutcomeDeclaration("FEEDBACK", "identifier", "INCORRECT"));
		}

		return outcomeDeclarationBlocks;
	}

	protected static Element createModalFeedbackBlock(String identifier, String content) {
		final Element modalFeedback = new Element("modalFeedback");
		modalFeedback.setAttribute("identifier", identifier);
		modalFeedback.setAttribute("outcomeIdentifier", "FEEDBACK");
		modalFeedback.setAttribute("showHide", "show");
		modalFeedback.setText(content);

		return modalFeedback;
	}

	public List<Element> getModalFeedbackBlocks(boolean generateFeedback) {
		ArrayList<Element> modalFeedbackBlocks = new ArrayList<Element>();
		if (generateFeedback) {
			modalFeedbackBlocks.add(createModalFeedbackBlock("CORRECT", "Correct !"));
			modalFeedbackBlocks.add(createModalFeedbackBlock("INCORRECT", "Incorrect !"));
		}
		return modalFeedbackBlocks;
	}

	protected static Element createScoreIncrementSetOutcomeValue() {
		final Element setOutcomeValue = new Element("setOutcomeValue");
		setOutcomeValue.setAttribute("identifier", "SCORE");
		final Element sum = new Element("sum");
		setOutcomeValue.addContent(sum);
		final Element variable = new Element("variable");
		sum.addContent(variable);
		variable.setAttribute("identifier", "SCORE");
		final Element baseValue = new Element("baseValue");
		sum.addContent(baseValue);
		baseValue.setAttribute("baseType", "float");
		baseValue.setText("1");
		return setOutcomeValue;
	}

	protected static Element createScoreIncrementResponseCondition(String variableIdentifier) {
		final Element responseCondition = new Element("responseCondition");
		final Element responseIf = new Element("responseIf");
		responseCondition.addContent(responseIf);

		final Element match = new Element("match");
		responseIf.addContent(match);
		final Element variable = new Element("variable");
		match.addContent(variable);
		variable.setAttribute("identifier", variableIdentifier);
		final Element correct = new Element("correct");
		match.addContent(correct);
		correct.setAttribute("identifier", variableIdentifier);

		responseIf.addContent(createScoreIncrementSetOutcomeValue());

		return responseCondition;
	}

	protected static Element createSuccessResponseCondition(boolean feedback) {
		final Element responseCondition = new Element("responseCondition");
		final Element responseIf = new Element("responseIf");
		responseCondition.addContent(responseIf);

		final Element match = new Element("match");
		responseIf.addContent(match);
		Element variable = new Element("variable");
		match.addContent(variable);
		variable.setAttribute("identifier", "SCORE");
		variable = new Element("variable");
		match.addContent(variable);
		variable.setAttribute("identifier", "MAXSCORE");

		Element setOutcomeValue = new Element("setOutcomeValue");
		responseIf.addContent(setOutcomeValue);
		setOutcomeValue.setAttribute("identifier", "PASSED");
		Element baseValue = new Element("baseValue");
		setOutcomeValue.addContent(baseValue);
		baseValue.setAttribute("baseType", "boolean");
		baseValue.setText("true");

		if (feedback) {
			setOutcomeValue = new Element("setOutcomeValue");
			responseIf.addContent(setOutcomeValue);
			setOutcomeValue.setAttribute("identifier", "FEEDBACK");
			baseValue = new Element("baseValue");
			setOutcomeValue.addContent(baseValue);
			baseValue.setAttribute("baseType", "identifier");
			baseValue.setText("CORRECT");
		}

		return responseCondition;
	}

	public Element getResponseProcessingBlock(boolean feedback) {
		final Element responseProcessing = new Element("responseProcessing");

		for (int i = 0; i < gaps.size(); i++) {
			responseProcessing.addContent(createScoreIncrementResponseCondition("RESPONSE" + (i + 1)));
		}

		responseProcessing.addContent(createSuccessResponseCondition(feedback));

		return responseProcessing;
	}

}
