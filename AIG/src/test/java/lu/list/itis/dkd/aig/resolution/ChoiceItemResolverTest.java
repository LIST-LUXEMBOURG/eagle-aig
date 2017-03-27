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

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Eric TOBIAS [eric.tobias@list.lu]
 * @since 2 Feb 2015
 * @version 1
 */
public class ChoiceItemResolverTest {
    private static final String MCQ_TEMPLATE =
                    " <template> <!-- vocabulary test MCQ -->    <metadata>      <identifier>http://list.lu/assessment/itemTemplate/ChoiceVocabulary</identifier>        <taskModel>http://list.lu/assessment/TaskModel/TranslatingStatisticsConcepts</taskModel>        <interactionType>choiceInteraction</interactionType>        <constructType>vocabulary</constructType>   </metadata> <layer>     <!-- this section represents an XML-QTI item with placeholders for variables -->        <assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\" identifier=\"choice\" title=\"Concept translation from English into French {concept1}\" adaptive=\"false\" timeDependent=\"false\">          <responseDeclaration identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\">                <correctResponse>                   <value>{correctResponseCode}</value>                </correctResponse>          </responseDeclaration>          <outcomeDeclaration identifier=\"SCORE\" cardinality=\"single\" baseType=\"integer\">               <defaultValue>                  <value>0</value>                </defaultValue>         </outcomeDeclaration>           <itemBody>              <choiceInteraction responseIdentifier=\"RESPONSE\" shuffle=\"false\" maxChoices=\"1\">                  <prompt>What is the translation of {concept1} into French?</prompt>                 <simpleChoice identifier=\"{optionCode1}\">{option1}</simpleChoice>                 <simpleChoice identifier=\"{optionCode2}\">{option2}</simpleChoice>                 <simpleChoice identifier=\"{optionCode3}\">{option3}</simpleChoice>             </choiceInteraction>            </itemBody>         <responseProcessing template=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\" />        </assessmentItem>   </layer>    <variableDefinitions>       <!-- this section describes the list of variables necessary for the resolution of  the template, including their type and interdependancies-->      <variable identifier=\"concept1\">          <language>en</language>         <type>String</type>     </variable>     <variable identifier=\"correctResponseCode\">           <type>String</type>         <range>optionCode1, optionCode2, optionCode3</range>            <dependency>                <head>optionCode1</head>                <tail>correctResponseOption=option1, correctResponseURI=option1URI</tail>           </dependency>           <dependency>                <head>optionCode2</head>                <tail>correctResponseOption=option2, correctResponseURI=option2URI</tail>           </dependency>           <dependency>                <head>optionCode3</head>                <tail>correctResponseOption=option3, correctResponseURI=option3URI</tail>           </dependency>       </variable>     <variable identifier=\"optionCode1\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"optionCode2\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"optionCode3\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"option1\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option2\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option3\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option1URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <variable identifier=\"option2URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <variable identifier=\"option3URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <!--hidden variables-->     <variable identifier=\"conceptURI\">            <type>URI</type>        </variable>     <variable identifier=\"correctResponseURI\">            <type>URI</type>        </variable>     <variable identifier=\"correctResponseOption\">         <type>String</type>     </variable>     <variable identifier=\"distractorOption\">          <type>String</type>     </variable>     <variable identifier=\"distractorCode\">            <type>String</type>     </variable>     <variable identifier=\"distractorURI\">         <type>URI</type>        </variable> </variableDefinitions>  <variableResolutions>       <!-- this section describes the variable resolution mechanisms associated with the template-->      <variable identifier=\"concept1\">          <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"correctResponseCode\">           <source>generated</source>      </variable>     <!--hidden variables-->     <variable identifier=\"conceptURI\">            <source>inputs</source>      </variable>     <variable identifier=\"correctResponseURI\">            <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"correctResponseOption\">         <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"distractorOption\">          <source>variableResolutionProcess2</source>     </variable>     <variable identifier=\"distractorCode\">            <source>generated</source>      </variable>     <variable identifier=\"distractorURI\">         <source>variableResolutionProcess2</source>     </variable>     <variableResolution>            <process identifier=\"variableResolutionProcess1\">             <datasource>http://dbpedia.org/sparql</datasource>              <query>select ?concept1 ?correctResponseURI ?correctResponseOption  where {[conceptURI] ?p ?o . [conceptURI] &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?concept1  . [conceptURI] &lt;http://www.w3.org/2002/07/owl#sameAs&gt; ?correctResponseURI . ?correctResponseURI &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?correctResponseOption FILTER ( lang(?concept1) = \"en\" ) FILTER ( lang(?correctResponseOption) = \"fr\" )}</query>                <outcome variableIdentifier=\"concept1\"/>              <!--warning it is ordered according to what comes out of the query -->              <outcome variableIdentifier=\"correctResponseURI\"/>                <outcome variableIdentifier=\"correctResponseOption\"/>         </process>          <process identifier=\"variableResolutionProcess2\">             <datasource>http://dbpedia.org/sparql</datasource>              <query>select ?distractorURI ?distractorOption where { [conceptURI] &lt;http://purl.org/dc/terms/subject&gt; ?subject . ?distractorURI &lt;http://purl.org/dc/terms/subject&gt; ?subject . ?distractorURI &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?distractorOption  FILTER ( ?distractorURI !=  [conceptURI] ) FILTER ( lang(?distractorOption ) = \"fr\" )}</query>                <outcome variableIdentifier=\"distractorURI\"/>             <outcome variableIdentifier=\"distractorOption\"/>          </process>      </variableResolution>   </variableResolutions></template>";
    private static HashMap<String, String> input = new HashMap<>();
    private ChoiceItemFactory resolver;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ChoiceItemResolverTest.input.put("conceptURI", "http://dbpedia.org/resource/Nigeria");
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        resolver = new ChoiceItemFactory(builder.parse(new InputSource(new StringReader(ChoiceItemResolverTest.MCQ_TEMPLATE))), ChoiceItemResolverTest.input);
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.resolution.ChoiceItemFactory#groundFreeVariables()} .
     */
    @Test
    public void testGroundFreeVariables() {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.resolution.ChoiceItemFactory#resolveVariables()}.
     */
    @Test
    public void testResolveVariables() {
        resolver.groundFreeVariables();
        resolver.resolveVariables();
    }

}
