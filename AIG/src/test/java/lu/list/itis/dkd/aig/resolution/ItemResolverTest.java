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

//import lu.list.itis.dkd.semantic.exception.InitializationException;

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.WrongWordspaceTypeException;
import junit.framework.TestCase;

/**
 * Class implementing test cases for the abstract {@link ItemFactory} class, most notably whether
 * the constructor is called from all implementing classes.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.7.1
 */
@Deprecated
public class ItemResolverTest extends XMLTestCase {
    private static final String MCQ_TEMPLATE =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><template><metadata><identifier>http://list.lu/assessment/itemTemplate/ChoiceVocabulary</identifier><taskModel>http://list.lu/assessment/TaskModel/TranslatingStatisticsConcepts</taskModel><interactionType>choiceInteraction</interactionType><constructType>vocabulary</constructType><keyVariable>http://list.lu/concept</keyVariable><correctResponseVariable>http://list.lu/correctResponse</correctResponseVariable><distractorVariable>http://list.lu/distractor</distractorVariable><correctResponseAttributionMode>RANDOM</correctResponseAttributionMode><distractorAttributionMode>RANDOM</distractorAttributionMode></metadata><layer><itemMetadata><dummyData>This is dummy data!</dummyData><dummyVariable>The correct response is {http://list.lu/correctResponseLabel}!</dummyVariable></itemMetadata><assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\" identifier=\"choice\" title=\"Concept translation from English into French {http://list.lu/concept1}\" adaptive=\"false\" timeDependent=\"false\"><responseDeclaration identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\"><correctResponse><value>{http://list.lu/correctResponseCode}</value></correctResponse></responseDeclaration><outcomeDeclaration identifier=\"SCORE\" cardinality=\"single\" baseType=\"integer\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><itemBody><choiceInteraction responseIdentifier=\"RESPONSE\" shuffle=\"false\" maxChoices=\"1\"><prompt>What is the translation of {http://list.lu/conceptLabel} into French?</prompt><simpleChoice identifier=\"{http://list.lu/optionCode1}\">{http://list.lu/optionLabel1}</simpleChoice><simpleChoice identifier=\"{http://list.lu/optionCode2}\">{http://list.lu/optionLabel2}</simpleChoice><simpleChoice identifier=\"{http://list.lu/optionCode3}\">{http://list.lu/optionLabel3}</simpleChoice></choiceInteraction></itemBody><responseProcessing template=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\" /></assessmentItem></layer><variableDefinitions><variable><identifier>http://list.lu/concept</identifier><language>en</language><initialization>http://list.lu/process1</initialization><values><value><key>http://list.lu/conceptURI</key><type>URI</type></value><value><key>http://list.lu/conceptLabel</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/distractor</identifier><language>en</language><range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range><initialization>http://list.lu/process2</initialization><values><value><key>http://list.lu/distractorURI</key><type>URI</type></value><value><key>http://list.lu/distractorLabel</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/correctResponse</identifier><language>en</language><range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range><initialization>http://list.lu/process1</initialization><values><value><key>http://list.lu/correctResponseURI</key><type>URI</type></value><value><key>http://list.lu/correctResponseLabel</key><type>TEXT</type></value><value><key>http://list.lu/correctResponseCode</key><type>CODE</type></value></values></variable><variable><identifier>http://list.lu/option1</identifier><values><value><key>http://list.lu/optionCode1</key><type>CODE</type></value><value><key>http://list.lu/optionLabel1</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/option2</identifier><values><value><key>http://list.lu/optionCode2</key><type>CODE</type></value><value><key>http://list.lu/optionLabel2</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/option3</identifier><values><value><key>http://list.lu/optionCode3</key><type>CODE</type></value><value><key>http://list.lu/optionLabel3</key><type>TEXT</type></value></values></variable><!--<variable> <identifier>http://list.lu/optionCode1</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/optionCode2</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/optionCode3</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/correctResponseURI</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range> <initialization>http://list.lu/process1</initialization> </variable> <variable> <identifier>http://list.lu/option1</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option2</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option3</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option1Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/option2Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/option3Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/conceptUri</identifier> <type>SemanticVariable</type> <language>en</language> </variable> <variable> <identifier>http://list.lu/correctResponse</identifier> <type>IdentifierVariable</type> </variable> <variable> <identifier>http://list.lu/distractorURI</identifier> <type>SemanticVariable</type> <language>en</language> <initialization>http://list.lu/process2</initialization> </variable> <variable> <identifier>http://list.lu/distractorLabel</identifier> <type>TextVariable</type> <language>en</language> <initialization>http://list.lu/process2</initialization> </variable> <variable> <identifier>http://list.lu/distractorCode</identifier> <type>TextVariable</type> <language>en</language> <initialization>http://list.lu/process3</initialization> </variable> <variable> <identifier>http://list.lu/multimedia1</identifier> <type>MultimediaVariable</type> <dcmiType>dummyDcmiType</dcmiType> <language>en</language> <initialization>http://list.lu/process3</initialization> <supportedMimeTypes> <supportedMimeType>text/plain</supportedMimeType> <supportedMimeType>audio/aiff</supportedMimeType> </supportedMimeTypes> </variable> <variable> <identifier>http://list.lu/created1</identifier> <type>TextVariable</type> <language>en</language> </variable> --></variableDefinitions><variableDependencies><dependency><priority>1</priority><head>http://list.lu/distractor</head><type>ASSIGNMENT</type><cardinality>2</cardinality><tail>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</tail></dependency><dependency><priority>0</priority><head>http://list.lu/correctResponse</head><type>ASSIGNMENT</type><cardinality>1</cardinality><tail>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</tail></dependency></variableDependencies><processes><process><identifier>http://list.lu/process1</identifier><type>SemanticRetrievalProcess</type><datasource>http://dbpedia.org/sparql</datasource><graph /><query>select ?http://list.lu/conceptLabel ?http://list.lu/correctResponseURI ?http://list.lu/correctResponseLabel where {[http://list.lu/conceptURI] ?p ?o . [http://list.lu/conceptURI]&lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?http://list.lu/conceptLabel .[http://list.lu/conceptURI] &lt;http://www.w3.org/2002/07/owl#sameAs&gt;?http://list.lu/correctResponseURI . ?http://list.lu/correctResponseURI&lt;http://www.w3.org/2000/01/rdf-schema#label&gt;?http://list.lu/correctResponseLabel FILTER ( lang(?http://list.lu/conceptLabel) = \"en\" ) FILTER (lang(?http://list.lu/correctResponseLabel) = \"fr\" )}</query><input><inputIdentifier>http://list.lu/conceptURI</inputIdentifier></input><outcome><outcomeIdentifier>http://list.lu/correctResponse</outcomeIdentifier><outcomeIdentifier>http://list.lu/concept</outcomeIdentifier></outcome></process><process><identifier>http://list.lu/process2</identifier><type>SemanticRetrievalProcess</type><datasource>http://dbpedia.org/sparql</datasource><query>select ?http://list.lu/distractorURI ?http://list.lu/distractorLabel where { [http://list.lu/conceptURI]&lt;http://purl.org/dc/terms/subject&gt; ?subject . ?http://list.lu/distractorURI&lt;http://purl.org/dc/terms/subject&gt; ?subject . ?http://list.lu/distractorURI&lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?http://list.lu/distractorLabel FILTER ( ?http://list.lu/distractorURI != [http://list.lu/conceptURI] ) FILTER (lang(?http://list.lu/distractorLabel ) = \"fr\" )}</query><input><inputIdentifier>http://list.lu/conceptURI</inputIdentifier></input><outcome><outcomeIdentifier>http://list.lu/distractor</outcomeIdentifier></outcome></process><!-- <process> <identifier>http://list.lu/creation1</identifier> <type>CreationProcess</type> <parameters> <parameter>one=1</parameter> <parameter>two=2</parameter> <parameter>three=three</parameter> </parameters> <outcome> <outcomeIdentifier>http://list.lu/created</outcomeIdentifier> </outcome> </process> --></processes></template>";
    // private static final String QUERY = "select ?concept1 ?correctResponseURI
    // ?correctResponseOption where {[conceptURI] ?p ?o . [conceptURI]
    // &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?concept1 . [conceptURI]
    // &lt;http://www.w3.org/2002/07/owl#sameAs&gt; ?correctResponseURI . ?correctResponseURI
    // &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?correctResponseOption FILTER (
    // lang(?concept1) = \"en\" ) FILTER ( lang(?correctResponseOption) = \"fr\" )}";
    private final HashMap<String, String> input = new HashMap<>();

    /**
     * This method configures {@link XMLUnit}.
     */
    @Override
    @BeforeClass
    public void setUp() {
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreWhitespace(true);
        input.put("conceptURI", "http://dbpedia.org/resource/Nigeria"); //$NON-NLS-1$ //$NON-NLS-2$
    }


    /**
     * Test method for {@link ItemFactory#ItemResolver(org.w3c.dom.Document, HashMap)}. The method
     * uses the concrete sub-class {@link ChoiceItemFactory} to check whether the abstract
     * constructor initialises the template correctly and proceeds with the extraction of all
     * variables. Note that this test only checks whether the map of variables is not null. Another
     * test checks whether all variables are present per concrete interaction type and their
     * {@link ItemFactory} implementation.
     *
     * @throws ParserConfigurationException
     *         Thrown when a DocumentBuilder cannot be created which satisfies the configuration
     *         requested.
     * @throws SAXException
     *         Thrown when an error occurs during parsing.
     * @throws IOException
     *         Thrown if an I/O error occurs during parsing.
     * @throws SecurityException
     * @see Field
     * @throws NoSuchFieldException
     *         Thrown if a field with the specified name is not found.
     * @throws IllegalAccessException
     *         Thrown if this Field object is enforcing Java language access control and the
     *         underlying field is inaccessible.
     * @throws IllegalArgumentException
     *         Thrown if the specified object is not an instance of the class or interface declaring
     *         the underlying field (or a subclass or implementor thereof).
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testItemResolver() throws ParserConfigurationException, NoSuchFieldException, SecurityException, SAXException, IOException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException, WrongWordspaceTypeException, CorruptConfigFileException, SQLException {
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final ItemFactory itemBuilder = new ItemFactory(builder.parse(new InputSource(new StringReader(ItemResolverTest.MCQ_TEMPLATE))), input);
        final Field resolverField = ItemFactory.class.getDeclaredField("resolver");
        resolverField.setAccessible(true);
        final ChoiceItemFactory resolver = (ChoiceItemFactory) resolverField.get(itemBuilder);
        TestCase.assertNotNull(resolver.template);
        this.assertXMLEqual(builder.parse(new InputSource(new StringReader(ItemResolverTest.MCQ_TEMPLATE))), resolver.template);
        TestCase.assertTrue(resolver.variables != null);
        TestCase.assertFalse(resolver.variables.isEmpty());
        TestCase.assertTrue(resolver.inputDependentVariables != null);
        TestCase.assertFalse(resolver.inputDependentVariables.isEmpty());
        TestCase.assertTrue(resolver.dependentVariables != null);
        TestCase.assertFalse(resolver.dependentVariables.isEmpty());
        TestCase.assertTrue(resolver.processDependentVariables != null);
        TestCase.assertFalse(resolver.processDependentVariables.isEmpty());
        TestCase.assertTrue(resolver.variables.keySet().containsAll(resolver.inputDependentVariables));
        TestCase.assertTrue(resolver.variables.keySet().containsAll(resolver.dependentVariables));
        TestCase.assertTrue(resolver.variables.keySet().containsAll(resolver.processDependentVariables));
    }

    /**
     * Method testing whether the resolution of the query is correctly done.
     *
     * @throws ParserConfigurationException
     *         Thrown when a DocumentBuilder cannot be created which satisfies the configuration
     *         requested.
     * @throws SAXException
     *         Thrown when an error occurs during parsing.
     * @throws IOException
     *         Thrown if an I/O error occurs during parsing.
     * @throws SecurityException
     * @see Field
     * @throws NoSuchFieldException
     *         Thrown if a field with the specified name is not found.
     * @throws IllegalAccessException
     *         Thrown if this Field object is enforcing Java language access control and the
     *         underlying field is inaccessible.
     * @throws IllegalArgumentException
     *         Thrown if the specified object is not an instance of the class or interface declaring
     *         the underlying field (or a subclass or implementor thereof).
     * @throws SQLException
     * @throws CorruptConfigFileException
     * @throws WrongWordspaceTypeException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     *
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testQueryInputResolution() throws ParserConfigurationException, SAXException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException, WrongWordspaceTypeException, CorruptConfigFileException, SQLException {
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final ItemFactory itemBuilder = new ItemFactory(builder.parse(new InputSource(new StringReader(ItemResolverTest.MCQ_TEMPLATE))), input);
        final Field resolverField = ItemFactory.class.getDeclaredField("resolver");
        resolverField.setAccessible(true);
        final ChoiceItemFactory resolver = (ChoiceItemFactory) resolverField.get(itemBuilder);
        final String resolvedQuery = resolver.resolveQueryInput(ItemResolverTest.QUERY);
        TestCase.assertFalse(resolvedQuery.contains("["));
        TestCase.assertFalse(resolvedQuery.contains("]"));
        TestCase.assertFalse(resolvedQuery.contains("conceptURI"));
    }

    /**
     * Method testing whether the extraction of variables for the interaction type
     * <code>"choiceInteraction"</code> are correctly extracted. The XML constant defined in this
     * class serves as the model.
     *
     * @throws ParserConfigurationException
     *         Thrown when a DocumentBuilder cannot be created which satisfies the configuration
     *         requested.
     * @throws SAXException
     *         Thrown when an error occurs during parsing.
     * @throws IOException
     *         Thrown if an I/O error occurs during parsing.
     * @throws SecurityException
     * @see Field
     * @throws NoSuchFieldException
     *         Thrown if a field with the specified name is not found.
     * @throws IllegalAccessException
     *         Thrown if this Field object is enforcing Java language access control and the
     *         underlying field is inaccessible.
     * @throws IllegalArgumentException
     *         Thrown if the specified object is not an instance of the class or interface declaring
     *         the underlying field (or a subclass or implementor thereof).
     * @throws XPathExpressionException
     * @throws SQLException
     * @throws CorruptConfigFileException
     * @throws WrongWordspaceTypeException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testMcqVariableExtraction() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, XPathExpressionException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException, WrongWordspaceTypeException, CorruptConfigFileException, SQLException {

        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final ItemFactory itemBuilder = new ItemFactory(builder.parse(new InputSource(new StringReader(ItemResolverTest.MCQ_TEMPLATE))), input);
        final Field resolverField = ItemFactory.class.getDeclaredField("resolver");
        resolverField.setAccessible(true);
        final Map<String, TemplateVariable> variables = ((ChoiceItemFactory) resolverField.get(itemBuilder)).variables;
        TestCase.assertTrue(variables.size() == 17);

        final XPath xPath = XPathFactory.newInstance().newXPath();
        final XPathExpression expression = xPath.compile("//variableDefinitions/variable/@identifier");
        final NodeList nodes = (NodeList) expression.evaluate(builder.parse(new InputSource(new StringReader(ItemResolverTest.MCQ_TEMPLATE))), XPathConstants.NODESET);
        TestCase.assertTrue(nodes.getLength() > 0);
        for (int i = 0; i < nodes.getLength(); i++) {
            TestCase.assertTrue(variables.containsKey(nodes.item(i).getTextContent()));
        }

        final TemplateVariable concept1 = variables.get("concept1");
        TestCase.assertEquals("en", concept1.getLanguage());
        TestCase.assertEquals("concept1", concept1.getIdentifier());
        TestCase.assertEquals("String", concept1.getType());
        TestCase.assertTrue(concept1.getDependencies().isEmpty());
        TestCase.assertTrue(concept1.getRange().isEmpty());

        final TemplateVariable correctResponseCode = variables.get("correctResponseCode");
        TestCase.assertEquals("correctResponseCode", correctResponseCode.getIdentifier());
        TestCase.assertEquals("en", correctResponseCode.getLanguage());
        TestCase.assertEquals("String", correctResponseCode.getType());
        TestCase.assertEquals(3, correctResponseCode.getRange().size());
        TestCase.assertTrue(correctResponseCode.getRange().contains(variables.get("optionCode1")));
        TestCase.assertTrue(correctResponseCode.getRange().contains(variables.get("optionCode2")));
        TestCase.assertTrue(correctResponseCode.getRange().contains(variables.get("optionCode3")));

        final List<ObsoleteDependency> dependencies = correctResponseCode.getDependencies();
        for (final ObsoleteDependency dependency : dependencies) {
            switch (dependency.getTarget()) {
                case "optionCode1":
                    TestCase.assertTrue(dependency.getTail().containsKey("correctResponseOption"));
                    TestCase.assertTrue(dependency.getTail().containsKey("correctResponseURI"));
                    TestCase.assertEquals("option1", dependency.getTail().get("correctResponseOption"));
                    TestCase.assertEquals("option1URI", dependency.getTail().get("correctResponseURI"));
                    break;
                case "optionCode2":
                    TestCase.assertTrue(dependency.getTail().containsKey("correctResponseOption"));
                    TestCase.assertTrue(dependency.getTail().containsKey("correctResponseURI"));
                    TestCase.assertEquals("option2", dependency.getTail().get("correctResponseOption"));
                    TestCase.assertEquals("option2URI", dependency.getTail().get("correctResponseURI"));
                    break;
                case "optionCode3":
                    TestCase.assertTrue(dependency.getTail().containsKey("correctResponseOption"));
                    TestCase.assertTrue(dependency.getTail().containsKey("correctResponseURI"));
                    TestCase.assertEquals("option3", dependency.getTail().get("correctResponseOption"));
                    TestCase.assertEquals("option3URI", dependency.getTail().get("correctResponseURI"));
                    break;
                default:
                    TestCase.fail("This state of the switch should never have been reached!");
            }
        }

        final TemplateVariable option3Uri = variables.get("option3URI");
        TestCase.assertEquals("en", option3Uri.getLanguage());
        TestCase.assertEquals("option3URI", option3Uri.getIdentifier());
        TestCase.assertEquals("String", option3Uri.getType());
        TestCase.assertTrue(option3Uri.getDependencies().isEmpty());
        TestCase.assertFalse(option3Uri.getRange().isEmpty());
        TestCase.assertTrue(option3Uri.getRange().contains(variables.get("correctResponseURI")));
        TestCase.assertTrue(option3Uri.getRange().contains(variables.get("distractorURI")));
    }

    /**
     * Method testing whether the extraction of variable resolution definitions for the interaction
     * type <code>"choiceInteraction"</code> are correctly extracted. The XML constant defined in
     * this class serves as the model.
     *
     * @throws ParserConfigurationException
     *         Thrown when a DocumentBuilder cannot be created which satisfies the configuration
     *         requested.
     * @throws SAXException
     *         Thrown when an error occurs during parsing.
     * @throws IOException
     *         Thrown if an I/O error occurs during parsing.
     * @throws SecurityException
     * @see Field
     * @throws NoSuchFieldException
     *         Thrown if a field with the specified name is not found.
     * @throws IllegalAccessException
     *         Thrown if this Field object is enforcing Java language access control and the
     *         underlying field is inaccessible.
     * @throws IllegalArgumentException
     *         Thrown if the specified object is not an instance of the class or interface declaring
     *         the underlying field (or a subclass or implementor thereof).
     * @throws XPathExpressionException
     * @throws SQLException
     * @throws CorruptConfigFileException
     * @throws WrongWordspaceTypeException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    @Test
    public void testMcqVariableResolutionExtraction() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, XPathExpressionException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException, WrongWordspaceTypeException, CorruptConfigFileException, SQLException {

        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final ItemFactory itemBuilder = new ItemFactory(builder.parse(new InputSource(new StringReader(ItemResolverTest.MCQ_TEMPLATE))), input);
        final Field resolverField = ItemFactory.class.getDeclaredField("resolver");
        resolverField.setAccessible(true);
        final List<String> dependentVariables = ((ChoiceItemFactory) resolverField.get(itemBuilder)).dependentVariables;
        TestCase.assertTrue(dependentVariables.size() == 2);
        final List<String> inputDependentVariables = ((ChoiceItemFactory) resolverField.get(itemBuilder)).inputDependentVariables;
        TestCase.assertTrue(inputDependentVariables.size() == 1);
        final List<String> processDependentVariables = ((ChoiceItemFactory) resolverField.get(itemBuilder)).processDependentVariables;
        TestCase.assertTrue(processDependentVariables.size() == 5);

        TestCase.assertTrue(inputDependentVariables.contains("conceptURI"));
        TestCase.assertTrue(dependentVariables.contains("correctResponseCode"));
        TestCase.assertTrue(processDependentVariables.contains("correctResponseURI"));
    }
}
