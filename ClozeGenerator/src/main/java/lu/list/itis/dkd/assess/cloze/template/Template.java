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

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Template {
    private static String defaultURI = "http://list.lu/";
    private String title;
    private Document doc;
    private ClozeItem clozeItem;
    private List<Gap> gaps;
    private boolean useFeedback = false;

    protected static final Logger logger = Logger.getLogger(Template.class.getSimpleName());

    /**
     * Automatically creates a qti template out of a qti cloze item.
     * @param title: The name of the template how it should be saved.
     * @param clozeItem: cloze item object
     * @param useFeedback: Feedback is embedded in the template if this variable is set to true.
     */
    public Template(String title, ClozeItem clozeItem, boolean useFeedback){
        this.title = title;
        this.clozeItem = clozeItem;
        this.gaps = clozeItem.getGaps();
        this.useFeedback = useFeedback;
    }
    
    private void outcomeDeclarationFeedback(Document doc, Element assessmentItem) {
        for (int i = 0; i < gaps.size(); i++) {
            Element correctFeedbackDeclaration = new Element("outcomeDeclaration");
            assessmentItem.addContent(correctFeedbackDeclaration);
            correctFeedbackDeclaration.setAttribute("identifier", "FEEDBACK_CORRECT" + (i+1));
            correctFeedbackDeclaration.setAttribute("cardinality", "single");
            correctFeedbackDeclaration.setAttribute("baseType", "identifier");
            
            Element incorrectFeedbackDeclaration = new Element("outcomeDeclaration");
            assessmentItem.addContent(incorrectFeedbackDeclaration);
            incorrectFeedbackDeclaration.setAttribute("identifier", "FEEDBACK_INCORRECT" + (i+1));
            incorrectFeedbackDeclaration.setAttribute("cardinality", "single");
            incorrectFeedbackDeclaration.setAttribute("baseType", "identifier");
        }
    }
    
    private Element metaData(){
        Element metaData = new Element("metadata");
        String identifier = defaultURI + "assessment/itemTemplate/Cloze</identifier>";
        String taskModel = defaultURI + "assessment/TaskModel/ClozeGeneration";
        String interactionType = "gapMatchInteraction";
        String constructType = "comprehension";
        
        Element metaIdentifier = new Element("identifier");
        metaIdentifier.setText(identifier);
        metaData.addContent(metaIdentifier);
        
        Element metaTaskModel = new Element("taskModel");
        metaTaskModel.setText(taskModel);
        metaData.addContent(metaTaskModel);
        
        Element metaInteractionType = new Element("interactionType");
        metaInteractionType.setText(interactionType);
        metaData.addContent(metaInteractionType);
        
        Element metaConstructType = new Element("constructType");
        metaConstructType.setText(constructType);
        metaData.addContent(metaConstructType);
        
        return metaData;
    }
    
    private Element layer() {
        Element layerElement = new Element("layer");
        Comment layerComment = new Comment("this section represents an XML-QTI item with placeholders for variables");
        layerElement.addContent(layerComment);

        Namespace xmlnsNamespace = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsqti_v2p0");
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
//        Namespace schemaLocation = Namespace.getNamespace("xsi:schemaLocation", "http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd");
        Element assessmentItem = new Element("assessmentItem", xmlnsNamespace);
        layerElement.addContent(assessmentItem);

        assessmentItem.addNamespaceDeclaration(xsi);
//        assessmentItem.addNamespaceDeclaration(schemaLocation);
        assessmentItem.setAttribute("identifier", "ModalFeedback");
        assessmentItem.setAttribute("title", title);
        assessmentItem.setAttribute("adaptive", "false");
        assessmentItem.setAttribute("timeDependent", "false");
        
        assessmentItem.addContent(clozeItem.getCorrectResponseBlocks());
        if (useFeedback) {
            outcomeDeclarationFeedback(doc, assessmentItem);
        }
        
        //outcomeScoreDeclaration Score Element
        Element outcomeScoreDeclaration = new Element("outcomeDeclaration");
        assessmentItem.addContent(outcomeScoreDeclaration);
        outcomeScoreDeclaration.setAttribute("identifier", "SCORE");
        outcomeScoreDeclaration.setAttribute("cardinality", "single");
        outcomeScoreDeclaration.setAttribute("baseType", "float");
        
        //defaultScoreValue Element 
        Element defaultScoreValue = new Element("defaultValue");
        outcomeScoreDeclaration.addContent(defaultScoreValue);
        
        //scoreValue element
        Element scoreValue = new Element("value");
        scoreValue.setText("0");
        defaultScoreValue.addContent(scoreValue);
        
        Element itemBody = new Element("itemBody");
        assessmentItem.addContent(itemBody);
        
        Element blockquote = new Element("blockquote");
        itemBody.addContent(blockquote);
        
        //Add qti cloze text
        blockquote.addContent(clozeItem.getClozeBlock());

        Element responseProcessing = new Element("responseProcessing");
        assessmentItem.addContent(responseProcessing);
        
        //Add feedback
        if (useFeedback) {
            for (ClozeSentence clozeSentence : clozeItem.getClozeSentences()) {
                int keyIndex = 1;
                for (Key key : clozeSentence.getKeys()) {
                    Element correctModalFeedback = new Element("modalFeedback");
                    correctModalFeedback.setAttribute("outcomeIdentifier", "FEEDBACK_CORRECT" + keyIndex);
                    correctModalFeedback.setAttribute("showHide", "show");
                    correctModalFeedback.setAttribute("identifier", "correctFeedback");
                    correctModalFeedback.setText(key.getFeedback());
                    assessmentItem.addContent(correctModalFeedback);
                    
                    int distractorIndex = 2;
                    for (Distractor distractor : key.getDistractors()) {
                        Element incorrectModalFeedback = new Element("modalFeedback");
                        incorrectModalFeedback.setAttribute("outcomeIdentifier", "FEEDBACK_INCORRECT" + (distractorIndex));
                        incorrectModalFeedback.setAttribute("showHide", "show");
                        incorrectModalFeedback.setAttribute("identifier", "incorrectFeedback");
                        incorrectModalFeedback.setText(distractor.getFeedback());
                        assessmentItem.addContent(incorrectModalFeedback);
                        distractorIndex++;
                    }
                    distractorIndex++;
                }
            }
        }
        
        return layerElement;
    }
    
    public Document getTemplateDocument() {
        Document doc = new Document();
        Element template = new Element("template");
        doc.addContent(template);
            
        template.addContent(metaData());
        template.addContent(layer());
            
        return doc;            
    }
    
    
    public void save(String path, String name) {
        Document doc = new Document();
        Element template = new Element("template");
        template.addContent(metaData());
        template.addContent(layer());
        doc.addContent(template);
        
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new FileWriter(path + name + ".xml"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Problem saving template", e);
            e.printStackTrace();
        }
     
        logger.log(Level.INFO, "File saved to " + path + name + ".xml."); 
    }
}
