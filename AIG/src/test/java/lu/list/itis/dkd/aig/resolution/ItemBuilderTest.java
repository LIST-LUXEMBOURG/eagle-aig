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

import lu.list.itis.dkd.aig.util.DocumentConverter;

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.jdom2.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;


/**
 * Test class for automatically testing the functionality of {@link ItemFactory}.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.7.1
 */
public class ItemBuilderTest extends XMLTestCase {
    private static final String MCQ_TEMPLATE =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><template><metadata><identifier>http://list.lu/assessment/itemTemplate/ChoiceVocabulary</identifier><taskModel>http://list.lu/assessment/TaskModel/TranslatingStatisticsConcepts</taskModel><interactionType>choiceInteraction</interactionType><constructType>vocabulary</constructType><keyVariable>http://list.lu/concept</keyVariable><correctResponseVariable>http://list.lu/correctResponse</correctResponseVariable><distractorVariable>http://list.lu/distractor</distractorVariable><correctResponseAttributionMode>RANDOM</correctResponseAttributionMode><distractorAttributionMode>RANDOM</distractorAttributionMode></metadata><layer><itemMetadata><dummyData>This is dummy data!</dummyData><dummyVariable>The correct response is {http://list.lu/correctResponseLabel}!</dummyVariable></itemMetadata><assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\" identifier=\"choice\" title=\"Concept translation from English into French {http://list.lu/concept1}\" adaptive=\"false\" timeDependent=\"false\"><responseDeclaration identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\"><correctResponse><value>{http://list.lu/correctResponseCode}</value></correctResponse></responseDeclaration><outcomeDeclaration identifier=\"SCORE\" cardinality=\"single\" baseType=\"integer\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><itemBody><choiceInteraction responseIdentifier=\"RESPONSE\" shuffle=\"false\" maxChoices=\"1\"><prompt>What is the translation of {http://list.lu/conceptLabel} into French?</prompt><simpleChoice identifier=\"{http://list.lu/optionCode1}\">{http://list.lu/optionLabel1}</simpleChoice><simpleChoice identifier=\"{http://list.lu/optionCode2}\">{http://list.lu/optionLabel2}</simpleChoice><simpleChoice identifier=\"{http://list.lu/optionCode3}\">{http://list.lu/optionLabel3}</simpleChoice></choiceInteraction></itemBody><responseProcessing template=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\" /></assessmentItem></layer><variableDefinitions><variable><identifier>http://list.lu/concept</identifier><language>en</language><initialization>http://list.lu/process1</initialization><values><value><key>http://list.lu/conceptURI</key><type>URI</type></value><value><key>http://list.lu/conceptLabel</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/distractor</identifier><language>en</language><range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range><initialization>http://list.lu/process2</initialization><values><value><key>http://list.lu/distractorURI</key><type>URI</type></value><value><key>http://list.lu/distractorLabel</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/correctResponse</identifier><language>en</language><range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range><initialization>http://list.lu/process1</initialization><values><value><key>http://list.lu/correctResponseURI</key><type>URI</type></value><value><key>http://list.lu/correctResponseLabel</key><type>TEXT</type></value><value><key>http://list.lu/correctResponseCode</key><type>CODE</type></value></values></variable><variable><identifier>http://list.lu/option1</identifier><values><value><key>http://list.lu/optionCode1</key><type>CODE</type></value><value><key>http://list.lu/optionLabel1</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/option2</identifier><values><value><key>http://list.lu/optionCode2</key><type>CODE</type></value><value><key>http://list.lu/optionLabel2</key><type>TEXT</type></value></values></variable><variable><identifier>http://list.lu/option3</identifier><values><value><key>http://list.lu/optionCode3</key><type>CODE</type></value><value><key>http://list.lu/optionLabel3</key><type>TEXT</type></value></values></variable><!--<variable> <identifier>http://list.lu/optionCode1</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/optionCode2</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/optionCode3</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/distractorCode, http://list.lu/correctResponseCode</range> </variable> <variable> <identifier>http://list.lu/correctResponseURI</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</range> <initialization>http://list.lu/process1</initialization> </variable> <variable> <identifier>http://list.lu/option1</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option2</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option3</identifier> <type>TextVariable</type> <language>en</language> <range>http://list.lu/correctResponseOption, http://list.lu/distractorLabel</range> </variable> <variable> <identifier>http://list.lu/option1Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/option2Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/option3Uri</identifier> <type>SemanticVariable</type> <language>en</language> <range>http://list.lu/correctResponseURI, http://list.lu/distractorURI</range> </variable> <variable> <identifier>http://list.lu/conceptUri</identifier> <type>SemanticVariable</type> <language>en</language> </variable> <variable> <identifier>http://list.lu/correctResponse</identifier> <type>IdentifierVariable</type> </variable> <variable> <identifier>http://list.lu/distractorURI</identifier> <type>SemanticVariable</type> <language>en</language> <initialization>http://list.lu/process2</initialization> </variable> <variable> <identifier>http://list.lu/distractorLabel</identifier> <type>TextVariable</type> <language>en</language> <initialization>http://list.lu/process2</initialization> </variable> <variable> <identifier>http://list.lu/distractorCode</identifier> <type>TextVariable</type> <language>en</language> <initialization>http://list.lu/process3</initialization> </variable> <variable> <identifier>http://list.lu/multimedia1</identifier> <type>MultimediaVariable</type> <dcmiType>dummyDcmiType</dcmiType> <language>en</language> <initialization>http://list.lu/process3</initialization> <supportedMimeTypes> <supportedMimeType>text/plain</supportedMimeType> <supportedMimeType>audio/aiff</supportedMimeType> </supportedMimeTypes> </variable> <variable> <identifier>http://list.lu/created1</identifier> <type>TextVariable</type> <language>en</language> </variable> --></variableDefinitions><variableDependencies><dependency><priority>1</priority><head>http://list.lu/distractor</head><type>ASSIGNMENT</type><cardinality>2</cardinality><tail>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</tail></dependency><dependency><priority>0</priority><head>http://list.lu/correctResponse</head><type>ASSIGNMENT</type><cardinality>1</cardinality><tail>http://list.lu/option1, http://list.lu/option2, http://list.lu/option3</tail></dependency></variableDependencies><processes><process><identifier>http://list.lu/process1</identifier><type>SemanticRetrievalProcess</type><datasource>http://dbpedia.org/sparql</datasource><graph /><query>select ?http://list.lu/conceptLabel ?http://list.lu/correctResponseURI ?http://list.lu/correctResponseLabel where {[http://list.lu/conceptURI] ?p ?o . [http://list.lu/conceptURI]&lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?http://list.lu/conceptLabel .[http://list.lu/conceptURI] &lt;http://www.w3.org/2002/07/owl#sameAs&gt;?http://list.lu/correctResponseURI . ?http://list.lu/correctResponseURI&lt;http://www.w3.org/2000/01/rdf-schema#label&gt;?http://list.lu/correctResponseLabel FILTER ( lang(?http://list.lu/conceptLabel) = \"en\" ) FILTER (lang(?http://list.lu/correctResponseLabel) = \"fr\" )}</query><input><inputIdentifier>http://list.lu/conceptURI</inputIdentifier></input><outcome><outcomeIdentifier>http://list.lu/correctResponse</outcomeIdentifier><outcomeIdentifier>http://list.lu/concept</outcomeIdentifier></outcome></process><process><identifier>http://list.lu/process2</identifier><type>SemanticRetrievalProcess</type><datasource>http://dbpedia.org/sparql</datasource><query>select ?http://list.lu/distractorURI ?http://list.lu/distractorLabel where { [http://list.lu/conceptURI]&lt;http://purl.org/dc/terms/subject&gt; ?subject . ?http://list.lu/distractorURI&lt;http://purl.org/dc/terms/subject&gt; ?subject . ?http://list.lu/distractorURI&lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?http://list.lu/distractorLabel FILTER ( ?http://list.lu/distractorURI != [http://list.lu/conceptURI] ) FILTER (lang(?http://list.lu/distractorLabel ) = \"fr\" )}</query><input><inputIdentifier>http://list.lu/conceptURI</inputIdentifier></input><outcome><outcomeIdentifier>http://list.lu/distractor</outcomeIdentifier></outcome></process><!-- <process> <identifier>http://list.lu/creation1</identifier> <type>CreationProcess</type> <parameters> <parameter>one=1</parameter> <parameter>two=2</parameter> <parameter>three=three</parameter> </parameters> <outcome> <outcomeIdentifier>http://list.lu/created</outcomeIdentifier> </outcome> </process> --></processes></template>";
    private static final String MATCH_TEMPLATE =
                    "<template> <!-- vocabulary test MCQ -->    <metadata>      <identifier>http://list.lu/assessment/itemTemplate/ChoiceVocabulary</identifier>        <taskModel>http://list.lu/assessment/TaskModel/TranslatingStatisticsConcepts</taskModel>        <interactionType>matchInteraction</interactionType>        <constructType>vocabulary</constructType>   </metadata> <layer>     <!-- this section represents an XML-QTI item with placeholders for variables -->        <assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\" identifier=\"choice\" title=\"Concept translation from English into French {concept1}\" adaptive=\"false\" timeDependent=\"false\">          <responseDeclaration identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\">                <correctResponse>                   <value>{correctResponseCode}</value>                </correctResponse>          </responseDeclaration>          <outcomeDeclaration identifier=\"SCORE\" cardinality=\"single\" baseType=\"integer\">               <defaultValue>                  <value>0</value>                </defaultValue>         </outcomeDeclaration>           <itemBody>              <choiceInteraction responseIdentifier=\"RESPONSE\" shuffle=\"false\" maxChoices=\"1\">                  <prompt>What is the translation of {concept1} into French?</prompt>                 <simpleChoice identifier=\"{optionCode1}\">{option1}</simpleChoice>                 <simpleChoice identifier=\"{optionCode2}\">{option2}</simpleChoice>                 <simpleChoice identifier=\"{optionCode3}\">{option3}</simpleChoice>             </choiceInteraction>            </itemBody>         <responseProcessing template=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\" />        </assessmentItem>   </layer>    <variableDefinitions>       <!-- this section describes the list of variables necessary for the resolution of  the template, including their type and interdependancies-->      <variable identifier=\"concept1\">          <language>en</language>         <type>String</type>     </variable>     <variable identifier=\"correctResponseCode\">           <type>String</type>         <range>optionCode1, optionCode2, optionCode3</range>            <dependence>As correctResponseCode=optionCode1, then correctResponseOption=option1, correctResponseURI=option1URI</dependence>          <dependence>As correctResponseCode=optionCode2, then correctResponseOption=option2, correctResponseURI=option2URI</dependence>          <dependence>As correctResponseCode=optionCode3, then correctResponseOption=option3, correctResponseURI=option3URI</dependence>      </variable>     <variable identifier=\"optionCode1\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"optionCode2\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"optionCode3\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"option1\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option2\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option3\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option1URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <variable identifier=\"option2URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <variable identifier=\"option3URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <!--hidden variables-->     <variable identifier=\"conceptURI\">            <type>URI</type>        </variable>     <variable identifier=\"correctResponseURI\">            <type>URI</type>        </variable>     <variable identifier=\"correctResponseOption\">         <type>String</type>     </variable>     <variable identifier=\"distractorOption\">          <type>String</type>     </variable>     <variable identifier=\"distractorCode\">            <type>String</type>     </variable>     <variable identifier=\"distractorURI\">         <type>URI</type>        </variable> </variableDefinitions>  <variableResolutions>       <!-- this section describes the variable resolution mechanisms associated with the template-->      <variable identifier=\"concept1\">          <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"correctResponseCode\">           <source>generated</source>      </variable>     <!--hidden variables-->     <variable identifier=\"conceptURI\">            <source>inputs</source>      </variable>     <variable identifier=\"correctResponseURI\">            <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"correctResponseOption\">         <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"distractorOption\">          <source>variableResolutionProcess2</source>     </variable>     <variable identifier=\"distractorCode\">            <source>generated</source>      </variable>     <variable identifier=\"distractorURI\">         <source>variableResolutionProcess2</source>     </variable>     <variableResolution>            <process identifier=\"variableResolutionProcess1\">             <datasource>http://dbpedia.org/sparql</datasource>              <query>select ?labelSource ?equivalentConcept ?labelTarget  where {[conceptURI] ?p ?o . [conceptURI] &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?labelSource  . [conceptURI] &lt;http://www.w3.org/2002/07/owl#sameAs&gt; ?equivalentConcept . ?equivalentConcept &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?labelSource FILTER ( lang(?labelSource) = \"en\" ) FILTER ( lang(?labelTarget) = \"fr\" )}</query>                <outcome variableIdentifier=\"concept1\"/>              <!--warning it is ordered according to what comes out of the query -->              <outcome variableIdentifier=\"correctResponseURI\"/>                <outcome variableIdentifier=\"correctResponseOption\"/>         </process>          <process identifier=\"variableResolutionProcess2\">             <datasource>http://dbpedia.org/sparql</datasource>              <query>select ?candidate ?labelCandidate where { [conceptURI] &lt;http://purl.org/dc/terms/subject&gt; ?subject . ?candidate &lt;http://purl.org/dc/terms/subject&gt; ?subject . ?candidate &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?labelCandidate  FILTER ( ?candidate !=  [conceptURI] ) FILTER ( lang(?labelCandidate ) = \"fr\" )}</query>                <outcome variableIdentifier=\"distractorURI\"/>             <outcome variableIdentifier=\"distractorOption\"/>          </process>      </variableResolution>   </variableResolutions></template>";
    // private static final String CLOZE_TEMPLATE =
    // "<?xml version=\"1.0\" encoding=\"UTF-8\"?><template> <metadata>
    // <identifier>http://list.lu/assessment/itemTemplate/Cloze</identifier>
    // <taskModel>http://list.lu/assessment/TaskModel/ClozeGeneration</taskModel>
    // <interactionType>gapMatchInteraction</interactionType>
    // <constructType>comprehension</constructType>
    // <correctResponseAttributionMode>RANDOM</correctResponseAttributionMode>
    // <distractorAttributionMode>RANDOM</distractorAttributionMode> <!-- number of distractors?-->
    // </metadata> <layer> <!-- TODO What should be the title? --> <assessmentItem
    // xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\"
    // xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
    // xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\"
    // identifier=\"choice\" title=\"Concept translation from English into French
    // {http://list.lu/concept1}\" adaptive=\"false\" timeDependent=\"false\"> <responseDeclaration
    // identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\">
    // {http://list.lu/correctResponseBlock} </responseDeclaration> <itemBody> <!-- What is supposed
    // to be printed here? --> <p> Identify the gaps in the text. </p> <blockquote>
    // <p>{http://list.lu/text}</p> </blockquote> </itemBody>
    // <responseProcessing></responseProcessing> </assessmentItem> </layer>
    // <variableDefinitions></variableDefinitions> <variableDependencies></variableDependencies>
    // <processes> <process> <identifier>http://list.lu/process1</identifier>
    // <type>GapGenerationProcess</type> <firstSentence>true</firstSentence> <nGram>flase</nGram>
    // <koda>true</koda> <dependency>false</dependency> <soundex>true</soundex>
    // <feedback>false</feedback> </process> </processes></template>";
    private static final String OPEN_TEMPLATE =
                    "<template> <!-- vocabulary test MCQ -->    <metadata>      <identifier>http://list.lu/assessment/itemTemplate/ChoiceVocabulary</identifier>        <taskModel>http://list.lu/assessment/TaskModel/TranslatingStatisticsConcepts</taskModel>        <interactionType>textEntryInteraction</interactionType>        <constructType>vocabulary</constructType>   </metadata> <layer>     <!-- this section represents an XML-QTI item with placeholders for variables -->        <assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\" identifier=\"choice\" title=\"Concept translation from English into French {concept1}\" adaptive=\"false\" timeDependent=\"false\">          <responseDeclaration identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\">                <correctResponse>                   <value>{correctResponseCode}</value>                </correctResponse>          </responseDeclaration>          <outcomeDeclaration identifier=\"SCORE\" cardinality=\"single\" baseType=\"integer\">               <defaultValue>                  <value>0</value>                </defaultValue>         </outcomeDeclaration>           <itemBody>              <choiceInteraction responseIdentifier=\"RESPONSE\" shuffle=\"false\" maxChoices=\"1\">                  <prompt>What is the translation of {concept1} into French?</prompt>                 <simpleChoice identifier=\"{optionCode1}\">{option1}</simpleChoice>                 <simpleChoice identifier=\"{optionCode2}\">{option2}</simpleChoice>                 <simpleChoice identifier=\"{optionCode3}\">{option3}</simpleChoice>             </choiceInteraction>            </itemBody>         <responseProcessing template=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\" />        </assessmentItem>   </layer>    <variableDefinitions>       <!-- this section describes the list of variables necessary for the resolution of  the template, including their type and interdependancies-->      <variable identifier=\"concept1\">          <language>en</language>         <type>String</type>     </variable>     <variable identifier=\"correctResponseCode\">           <type>String</type>         <range>optionCode1, optionCode2, optionCode3</range>            <dependence>As correctResponseCode=optionCode1, then correctResponseOption=option1, correctResponseURI=option1URI</dependence>          <dependence>As correctResponseCode=optionCode2, then correctResponseOption=option2, correctResponseURI=option2URI</dependence>          <dependence>As correctResponseCode=optionCode3, then correctResponseOption=option3, correctResponseURI=option3URI</dependence>      </variable>     <variable identifier=\"optionCode1\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"optionCode2\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"optionCode3\">           <range>distractorCode, correctResponseCode</range>          <type>String</type>     </variable>     <variable identifier=\"option1\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option2\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option3\">           <type>String</type>         <range>correctResponseOption, distractorOption</range>      </variable>     <variable identifier=\"option1URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <variable identifier=\"option2URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <variable identifier=\"option3URI\">            <type>String</type>         <range>correctResponseURI, distractorURI</range>        </variable>     <!--hidden variables-->     <variable identifier=\"conceptURI\">            <type>URI</type>        </variable>     <variable identifier=\"correctResponseURI\">            <type>URI</type>        </variable>     <variable identifier=\"correctResponseOption\">         <type>String</type>     </variable>     <variable identifier=\"distractorOption\">          <type>String</type>     </variable>     <variable identifier=\"distractorCode\">            <type>String</type>     </variable>     <variable identifier=\"distractorURI\">         <type>URI</type>        </variable> </variableDefinitions>  <variableResolutions>       <!-- this section describes the variable resolution mechanisms associated with the template-->      <variable identifier=\"concept1\">          <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"correctResponseCode\">           <source>generated</source>      </variable>     <!--hidden variables-->     <variable identifier=\"conceptURI\">            <source>inputs</source>      </variable>     <variable identifier=\"correctResponseURI\">            <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"correctResponseOption\">         <source>variableResolutionProcess1</source>     </variable>     <variable identifier=\"distractorOption\">          <source>variableResolutionProcess2</source>     </variable>     <variable identifier=\"distractorCode\">            <source>generated</source>      </variable>     <variable identifier=\"distractorURI\">         <source>variableResolutionProcess2</source>     </variable>     <variableResolution>            <process identifier=\"variableResolutionProcess1\">             <datasource>http://dbpedia.org/sparql</datasource>              <query>select ?labelSource ?equivalentConcept ?labelTarget  where {[conceptURI] ?p ?o . [conceptURI] &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?labelSource  . [conceptURI] &lt;http://www.w3.org/2002/07/owl#sameAs&gt; ?equivalentConcept . ?equivalentConcept &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?labelSource FILTER ( lang(?labelSource) = \"en\" ) FILTER ( lang(?labelTarget) = \"fr\" )}</query>                <outcome variableIdentifier=\"concept1\"/>              <!--warning it is ordered according to what comes out of the query -->              <outcome variableIdentifier=\"correctResponseURI\"/>                <outcome variableIdentifier=\"correctResponseOption\"/>         </process>          <process identifier=\"variableResolutionProcess2\">             <datasource>http://dbpedia.org/sparql</datasource>              <query>select ?candidate ?labelCandidate where { [conceptURI] &lt;http://purl.org/dc/terms/subject&gt; ?subject . ?candidate &lt;http://purl.org/dc/terms/subject&gt; ?subject . ?candidate &lt;http://www.w3.org/2000/01/rdf-schema#label&gt; ?labelCandidate  FILTER ( ?candidate !=  [conceptURI] ) FILTER ( lang(?labelCandidate ) = \"fr\" )}</query>                <outcome variableIdentifier=\"distractorURI\"/>             <outcome variableIdentifier=\"distractorOption\"/>          </process>      </variableResolution>   </variableResolutions></template>";
    private static final String QTI_ORACLE =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><assessmentItem adaptive=\"false\" identifier=\"choice\" timeDependent=\"false\" title=\"Concept translation from English into French {concept1}\" xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\"><responseDeclaration baseType=\"identifier\" cardinality=\"single\" identifier=\"RESPONSE\"><correctResponse><value>{correctResponseCode}</value></correctResponse></responseDeclaration><outcomeDeclaration baseType=\"integer\" cardinality=\"single\" identifier=\"SCORE\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><itemBody><choiceInteraction maxChoices=\"1\" responseIdentifier=\"RESPONSE\" shuffle=\"false\"><prompt>What is the translation of {concept1} into French?</prompt><simpleChoice identifier=\"{optionCode1}\">{option1}</simpleChoice><simpleChoice identifier=\"{optionCode2}\">{option2}</simpleChoice><simpleChoice identifier=\"{optionCode3}\">{option3}</simpleChoice></choiceInteraction></itemBody><responseProcessing template=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\"/></assessmentItem>";
    private final HashMap<String, String> input = new HashMap<>();

    /**
     * This method configures {@link XMLUnit}.
     */
    @Override
    @BeforeClass
    public void setUp() {
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreWhitespace(true);
        input.put("http://list.lu/conceptURI", "http://dbpedia.org/resource/Nigeria");
    }

    /**
     * @throws Exception
     * @throws TemplateParseException
     *         Test method checking whether the QTI XML can be extracted correctly from the
     *         template. While not exhaustive, this test gives a first hint whether the QTI XML is
     *         accepted by the constructor and correctly extracted. The test also checks whether the
     *         template rests unaffected by the extraction of the QTI XML. Note that the extraction
     *         is actually cloning the XML rather than really extracting it. @throws
     */
    @Test
    public void testQtiExtraction() throws Exception {

        final Document document = DocumentConverter.convertStringToDocument(ItemBuilderTest.MCQ_TEMPLATE);
        final ItemFactoryBuilder itemFactoryBuilder = new ItemFactoryBuilder();
        itemFactoryBuilder.withTemplate(document);
        itemFactoryBuilder.withInput(input);
        final ItemFactory factory = itemFactoryBuilder.build();

        Assert.assertTrue(factory instanceof ChoiceItemFactory);

        final List<String> items = factory.buildItems();
    }

    /**
     * Test method checking whether the correct {@link ItemFactory} is instantiated for a given
     * template's <code>interactionType</code>.
     *
     * @throws ParserConfigurationException
     *         Thrown when a DocumentBuilder cannot be created which satisfies the configuration
     *         requested.
     * @throws SAXException
     *         Thrown when an error occurs during parsing.
     * @throws IOException
     *         Thrown if an I/O error occurs during parsing.
     * @throws SecurityException
     *         Please @see {@link Class#getDeclaredField(String)}.
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
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    // @SuppressWarnings("null")
    // @Test
    // public void testConcreteResolverInstantiation() throws ParserConfigurationException,
    // SAXException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException,
    // IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
    // InstantiationException, InvocationTargetException, WrongWordspaceTypeException,
    // CorruptConfigFileException, SQLException, InitializationException {
    // Document document =
    // DocumentConverter.convertStringToDocument(ItemBuilderTest.CLOZE_TEMPLATE);
    // if (document == null) {
    // Assert.fail("The document could not be parsed correctly!");
    // } else {
    // ItemFactory itemBuilder = new ItemFactory(document, input);
    // final Field resolverField = ItemFactory.class.getDeclaredField("resolver");
    // resolverField.setAccessible(true);
    // Assert.assertTrue(resolverField.get(itemBuilder) instanceof ClozeItemFactory);
    //
    // document = DocumentConverter.convertStringToDocument(ItemBuilderTest.MATCH_TEMPLATE);
    // if (document == null) {
    // Assert.fail("The document could not be parsed correctly!");
    // } else {
    // itemBuilder = new ItemFactory(document, input);
    // Assert.assertTrue(resolverField.get(itemBuilder) instanceof MatchItemFactory);
    // }
    // document = DocumentConverter.convertStringToDocument(ItemBuilderTest.MCQ_TEMPLATE);
    // if (document == null) {
    // Assert.fail("The document could not be parsed correctly!");
    // } else {
    // itemBuilder = new ItemFactory(document, input);
    // Assert.assertTrue(resolverField.get(itemBuilder) instanceof ChoiceItemFactory);
    // }
    // document = DocumentConverter.convertStringToDocument(ItemBuilderTest.OPEN_TEMPLATE);
    // if (document == null) {
    // Assert.fail("The document could not be parsed correctly!");
    // } else {
    // itemBuilder = new ItemFactory(document, input);
    // Assert.assertTrue(resolverField.get(itemBuilder) instanceof OpenItemFactory);
    // }
    // }
    // }


    /**
     * Test method checking whether the inputs is correctly stored.
     *
     * @throws SecurityException
     *         Please @see {@link Class#getDeclaredField(String)}.
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
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    // @Test
    // public void testInputInitialisation() throws NoSuchFieldException, SecurityException,
    // IllegalArgumentException, IllegalAccessException, ClassNotFoundException,
    // NoSuchMethodException, InstantiationException, InvocationTargetException,
    // WrongWordspaceTypeException, CorruptConfigFileException, SQLException,
    // InitializationException {
    // final Document document =
    // DocumentConverter.convertStringToDocument(ItemBuilderTest.CLOZE_TEMPLATE);
    // if (document == null) {
    // Assert.fail("The document could not be parsed correctly!");
    // } else {
    // @SuppressWarnings("null")
    // final ItemFactory itemBuilder = new ItemFactory(document, input);
    // final Field inputField = ItemFactory.class.getDeclaredField("inputs");
    // inputField.setAccessible(true);
    // Assert.assertNotNull(inputField.get(itemBuilder));
    // @SuppressWarnings("unchecked")
    // final HashMap<String, String> inputFieldValue = (HashMap<String, String>)
    // inputField.get(itemBuilder);
    // Assert.assertTrue(inputFieldValue.size() == 1);
    // Assert.assertTrue(inputFieldValue.containsKey("conceptURI"));
    // }
    // }

    /**
     * Test method asserting whether the {@link ItemFactory} instance can generate items and returns
     * a list with multiple viable QTI items. Note that the method cannot assert whether the item
     * resolution process as such is valid.
     *
     * @throws SQLException
     * @throws CorruptConfigFileException
     * @throws WrongWordspaceTypeException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws InitializationException
     *         Thrown when the initialization of the bridge failed. This is most likely due to
     *         either the connection to the knowledge base failing or the engine not properly
     *         initializing.
     */
    // @Test
    // public void testBuildItems() throws ClassNotFoundException, NoSuchMethodException,
    // SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
    // InvocationTargetException, NoSuchFieldException, WrongWordspaceTypeException,
    // CorruptConfigFileException, SQLException, InitializationException {
    // final Document document =
    // DocumentConverter.convertStringToDocument(ItemBuilderTest.MCQ_TEMPLATE);
    // if (document == null) {
    // Assert.fail("The document could not be parsed correctly!");
    // } else {
    // @SuppressWarnings("null")
    // final ItemFactory itemBuilder = new ItemFactory(document, input);
    // final List<Document> items = itemBuilder.buildItems();
    // Assert.assertNotNull(items);
    // Assert.assertTrue(items.size() > 0);
    // }
    // }
}