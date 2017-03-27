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
package lu.list.itis.dkd.aig.resolution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import lu.list.itis.dkd.aig.Common;
import lu.list.itis.dkd.aig.util.Externalization;
import lu.list.itis.dkd.aig.util.FusekiHttpHelper;
import lu.list.itis.dkd.aig.util.PropertiesFetcher;
import lu.list.itis.dkd.assess.cloze.ontology.ClozeOntology;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.opennlp.ontology.TextOntology;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since [major].[minor]
 * @version [major].[minor].[micro]
 */
public class ItemFactoryBuilderTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link lu.list.itis.dkd.aig.resolution.ItemFactoryBuilder#withTemplate(lu.list.itis.dkd.aig.resolution.Template)}
	 * .
	 */
	@Test
	public final void testWithTemplateTemplate() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link lu.list.itis.dkd.aig.resolution.ItemFactoryBuilder#withTemplate(java.io.InputStream)}.
	 */
	@Test
	public final void testWithTemplateInputStream() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link lu.list.itis.dkd.aig.resolution.ItemFactoryBuilder#withTemplate(org.jdom2.Document)}.
	 */
	@Test
	public final void testWithTemplateDocument() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link lu.list.itis.dkd.aig.resolution.ItemFactoryBuilder#withTemplate(java.lang.String)}.
	 */
	@Test
	public final void testWithTemplateString() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link lu.list.itis.dkd.aig.resolution.ItemFactoryBuilder#withInput(java.util.Map)}.
	 */
	@Test
	public final void testWithInput() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link lu.list.itis.dkd.aig.resolution.ItemFactoryBuilder#build()}.
	 *
	 * @throws TemplateParseException
	 * @throws TemplateConsistencyException
	 * @throws SimilarityComputationException
	 * @throws InitializationException
	 * @throws ResolutionException
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testBuild() throws TemplateParseException, ResolutionException, 
			 TemplateConsistencyException, FileNotFoundException {

		final String mcq_template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><template><metadata><identifier>http://list.lu/assessment/itemTemplate/ChoiceVocabulary</identifier><taskModel>http://list.lu/assessment/TaskModel/TranslatingStatisticsConcepts</taskModel><interactionType>choiceInteraction</interactionType><constructType>vocabulary</constructType><keyVariable>http://list.lu/concept</keyVariable><correctResponseVariable>http://list.lu/correctResponse</correctResponseVariable><distractorVariable>http://list.lu/distractor</distractorVariable><correctResponseAttributionMode>RANDOM</correctResponseAttributionMode><distractorAttributionMode>RANDOM</distractorAttributionMode></metadata><layer><itemMetadata><dummyData>This is dummy data!</dummyData><dummyVariable>The correct response is {http://list.lu/correctResponseLabel}!</dummyVariable></itemMetadata><assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\" identifier=\"choice\" title=\"Concept translation from English into French {http://list.lu/concept1}\" adaptive=\"false\" timeDependent=\"false\"><responseDeclaration identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\"><correctResponse><value>{http://list.lu/correctResponseCode}</value></correctResponse></responseDeclaration><outcomeDeclaration identifier=\"SCORE\" cardinality=\"single\" baseType=\"integer\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><itemBody><choiceInteraction responseIdentifier=\"RESPONSE\" shuffle=\"false\" maxChoices=\"1\"><prompt>What is the translation of {http://list.lu/conceptLabel} into French?</prompt><simpleChoice identifier=\"{http://list.lu/optionCode1}\">{http://list.lu/optionLabel1}</simpleChoice><simpleChoice identifier=\"{http://list.lu/optionCode2}\">{http://list.lu/optionLabel2}</simpleChoice><simpleChoice identifier=\"{http://list.lu/optionCode3}\">{http://list.lu/optionLabel3}</simpleChoice></choiceInteraction></itemBody><responseProcessing template=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\" /></assessmentItem></layer><variableDefinitions><variable><identifier>http://list.lu/concept</identifier><language>en</language><initialization>http://list.lu/process1</initialization><values><value><key>http://list.lu/conceptURI</key><type>URI</type></value><value><key>http://list.lu/conceptLabel</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/distractor</identifier><language>en</language><range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range><initialization>http://list.lu/process2</initialization><values><value><key>http://list.lu/distractorURI</key><type>URI</type></value><value><key>http://list.lu/distractorLabel</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/correctResponse</identifier><language>en</language><range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range><initialization>http://list.lu/process1</initialization><values><value><key>http://list.lu/correctResponseURI</key><type>URI</type></value><value><key>http://list.lu/correctResponseLabel</key><type>TEXT</type></value><value><key>http://list.lu/correctResponseCode</key><type>CODE</type></value></values></variable><variable><identifier>http://list.lu/option1</identifier><values><value><key>http://list.lu/optionCode1</key><type>CODE</type></value><value><key>http://list.lu/optionLabel1</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/option2</identifier><values><value><key>http://list.lu/optionCode2</key><type>CODE</type></value><value><key>http://list.lu/optionLabel2</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/option3</identifier><values><value><key>http://list.lu/optionCode3</key><type>CODE</type></value><value><key>http://list.lu/optionLabel3</key><type>TEXT</type></value></values></variable><!--<variable> <identifier>http://list.lu/optionCode1</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/optionCode2</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/optionCode3</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/correctResponseURI</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range> <initialization>http://list.lu/process1</initialization> </variable> <variable> <identifier>http://list.lu/option1</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option2</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option3</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option1Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/option2Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/option3Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/conceptUri</identifier> <type>SemanticVariable</type> <language>en</language> </variable> <variable> <identifier>http://list.lu/correctResponse</identifier> <type>IdentifierVariable</type> </variable> <variable> <identifier>http://list.lu/distractorURI</identifier> <type>SemanticVariable</type> <language>en</language> <initialization>http://list.lu/process2</initialization> </variable> <variable> <identifier>http://list.lu/distractorLabel</identifier> <type>TextVariable</type> <language>en</language> <initialization>http://list.lu/process2</initialization> </variable> <variable> <identifier>http://list.lu/distractorCode</identifier> <type>TextVariable</type> <language>en</language> <initialization>http://list.lu/process3</initialization> </variable> <variable> <identifier>http://list.lu/multimedia1</identifier> <type>MultimediaVariable</type> <dcmiType>dummyDcmiType</dcmiType> <language>en</language> <initialization>http://list.lu/process3</initialization> <supportedMimeTypes> <supportedMimeType>text/plain</supportedMimeType> <supportedMimeType>audio/aiff</supportedMimeType> </supportedMimeTypes> </variable> <variable> <identifier>http://list.lu/created1</identifier> <type>TextVariable</type> <language>en</language> </variable> --></variableDefinitions><variableDependencies><dependency><priority>1</priority><head>http://list.lu/distractor</head><type>ASSIGNMENT</type><cardinality>2</cardinality><tail>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</tail></dependency><dependency><priority>0</priority><head>http://list.lu/correctResponse</head><type>ASSIGNMENT</type><cardinality>1</cardinality><tail>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</tail></dependency></variableDependencies><processes><process><identifier>http://list.lu/process1</identifier><type>SemanticRetrievalProcess</type><datasource>http://dbpedia.org/sparql</datasource><graph /><query>select ?http://list.lu/conceptLabel ?http://list.lu/correctResponseURI ?http://list.lu/correctResponseLabel where {[http://list.lu/conceptURI] ?p ?o . [http://list.lu/conceptURI]&lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?http://list.lu/conceptLabel .[http://list.lu/conceptURI] &lt;http://www.w3.org/2002/07/owl#sameAs&gt;?http://list.lu/correctResponseURI . ?http://list.lu/correctResponseURI&lt;http://www.w3.org/2000/01/rdf-schema#label&gt;?http://list.lu/correctResponseLabel FILTER ( lang(?http://list.lu/conceptLabel) = \"en\" ) FILTER (lang(?http://list.lu/correctResponseLabel) = \"fr\" )}</query><input><inputIdentifier>http://list.lu/conceptURI</inputIdentifier></input><outcome><outcomeIdentifier>http://list.lu/correctResponse</outcomeIdentifier><outcomeIdentifier>http://list.lu/concept</outcomeIdentifier></outcome></process><process><identifier>http://list.lu/process2</identifier><type>SemanticRetrievalProcess</type><datasource>http://dbpedia.org/sparql</datasource><query>select ?http://list.lu/distractorURI ?http://list.lu/distractorLabel where { [http://list.lu/conceptURI]&lt;http://purl.org/dc/terms/subject&gt; ?subject . ?http://list.lu/distractorURI&lt;http://purl.org/dc/terms/subject&gt; ?subject . ?http://list.lu/distractorURI&lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?http://list.lu/distractorLabel FILTER ( ?http://list.lu/distractorURI != [http://list.lu/conceptURI] ) FILTER (lang(?http://list.lu/distractorLabel ) = \"fr\" )}</query><input><inputIdentifier>http://list.lu/conceptURI</inputIdentifier></input><outcome><outcomeIdentifier>http://list.lu/distractor</outcomeIdentifier></outcome></process><!-- <process> <identifier>http://list.lu/creation1</identifier> <type>CreationProcess</type> <parameters> <parameter>one=1</parameter> <parameter>two=2</parameter> <parameter>three=three</parameter> </parameters> <outcome> <outcomeIdentifier>http://list.lu/created</outcomeIdentifier> </outcome> </process> --></processes></template>"; //$NON-NLS-1$
		final HashMap<String, String> input = new HashMap<>();

		input.put("http://list.lu/conceptURI", "http://dbpedia.org/resource/Nigeria");

		Common.generateItems(input, mcq_template, "resources/templates/mcq/" + "results/"
				+ ItemFactoryBuilderTest.class.getSimpleName() + "2.xml" + ".txt");

	}

	@Test
	public final void testBuild2() throws TemplateParseException, ResolutionException, 
			 TemplateConsistencyException, FileNotFoundException {

		final String templateFilename = "testTemplateMcqSimplified.xml";
		final String templatePath = "resources/templates/mcq/";
		final HashMap<String, String> input = new HashMap<>();

		input.put("http://list.lu/conceptURI", "http://dbpedia.org/resource/Nigeria");

		Common.generateItems(input, new FileInputStream(new File(templatePath + templateFilename)),
				templatePath + "results/" + templateFilename + ".txt");

	}

	@Test
	public final void testBuild3() throws TemplateParseException, ResolutionException, 
			 TemplateConsistencyException, FileNotFoundException {

		final String templateFilename = "testTemplateMcqSimplified2-one query.xml";
		final String templatePath = "resources/templates/mcq/";
		final HashMap<String, String> input = new HashMap<>();

		input.put("http://list.lu/conceptURI", "http://dbpedia.org/resource/Nigeria");

		Common.generateItems(input, new FileInputStream(new File(templatePath + templateFilename)),
				templatePath + "results/" + templateFilename + ".txt");

	}

	public final void testCloze() {

		final String text = "<p><b>Organisational culture</b>: Often referred to as the way things are done around here.</p>";
		final String body = "<div class=\"body\">" + text + "</div>";

		ClozeText clozeText = new ClozeText(body, "body", Language.EN, Approach.DEFINITION, 3, false);

		TextOntology textOntology = new TextOntology(clozeText, "onDemandCloze");
		ClozeOntology clozeOntology = new ClozeOntology(clozeText, textOntology);
		clozeOntology.save("C:\\Temp", "testCloze");
	}

	@Test
	public final void testBuild4() throws TemplateParseException, ResolutionException, 
			 TemplateConsistencyException, FileNotFoundException {

		final String text = "<p><b>Organisational culture</b>: Often referred to as the way things are done around here.</p><p><b>Organisational culture</b>: Often referred to as the way things are done around here.</p>"; //$NON-NLS-1$

		final String templateFilename = "ChoiceTermDefinition2-dummy.xml";
		final String templatePath = "resources/templates/mcq/";
		final HashMap<String, String> input = new HashMap<>();

		input.put("text", "<div class=\"body\">" + text + "</div>");

		Common.generateItems(input, new FileInputStream(new File(templatePath + templateFilename)),
				templatePath + "results/" + templateFilename + ".txt");

	}
}
