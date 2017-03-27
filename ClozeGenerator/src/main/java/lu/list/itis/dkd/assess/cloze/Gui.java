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
package lu.list.itis.dkd.assess.cloze;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import lu.list.itis.dkd.assess.cloze.option.ClozeSentence;
import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.cloze.option.Key;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.Resources;
import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.connectives.questions.Question;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;
import lu.list.itis.dkd.assess.opennlp.util.LanguageHelper;
import lu.list.itis.dkd.assess.opennlp.util.Wrapper;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class Gui extends Frame implements ActionListener {
    private JFrame frame = new JFrame("TextArea");
    private JFrame processFrame = new JFrame("Progressing");
    private JTextArea tArea = new JTextArea(10, 20);
    private JButton bOpen = new JButton("Search Text");
    private final JScrollPane pane = new JScrollPane(tArea);
    private String text;
    private Language language;
    private int numberOfDistractors = 3;
    private static String startingText = "Select a text by pressing on the \"Search Text\" button or copy paste a text in the here.";
    
    private final String[] options = new String[] {"Pick an option", "Cloze - Noun", "Cloze - Annotation", "Cloze - Tense", "Cloze - C-Test", "Open Questions", "MCQ - Definition", "ClozeMatch - Noun", "ClozeMatch - Annotation", "ClozeMatch - Verb"};
    @SuppressWarnings({"rawtypes", "unchecked"})
    private final JComboBox cbGenerate = new JComboBox(options);

    protected static final Logger logger = Logger.getLogger(Gui.class.getSimpleName());

    public void prepareAndShowGUI() throws IOException {
        final java.awt.Container container = frame.getContentPane();
        container.add(pane);
        container.add(bOpen, BorderLayout.NORTH);
        container.add(cbGenerate, BorderLayout.SOUTH);

        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);

        bOpen.addActionListener(this);
        cbGenerate.addActionListener(this);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void openText() throws IOException {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// +"/workspace/templateBuilder")); //$NON-NLS-1$
        final int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();
            selectedFile.getName();
            String content = Wrapper.loadTextFile(new FileInputStream(selectedFile.toString()));
            tArea.setText(content);
            text = content;
            Gui.logger.log(Level.INFO, "Selected file: " + selectedFile.getAbsolutePath());
        }
    }
    
    private String ClozeApproach(String selectedOption){
        String result = "";
        switch (selectedOption) {
            case "Cloze - C-Test":
                ClozeText cText = new ClozeText(text, language, Approach.CTEST, true);
                tArea.setText("");
                return cText.getClozeText();
            case "Cloze - Tense":
                ClozeText tenseText = new ClozeText(text, language, Approach.VERB);
                tArea.setText("");
                result = tenseText.getClozeText() + "\n\n";
                
                for (ClozeSentence clozeSentence : tenseText.getClozeSentences()) {
                    for (Key key : clozeSentence.getKeys()) {
                        result += "Option " + key.getKeyNumber() + ": " + key.getKeyWord().getContent();// + "(" + key.getKey().getTag() + ")";
                        for (Distractor distractor : key.getDistractors()) {
                            result += ", " + distractor.getDistractorWord().getContent();// + "(" + distractor.getDistractor().getTag() + ")";
                        }
                        result = result.trim() + "\n";
                    }
                }
                return result;
            case "Cloze - Noun":
                ClozeText nounText = new ClozeText(text, language, Approach.NOUN);
                tArea.setText("");
                result = nounText.getClozeText() + "\n\n";
                
                for (ClozeSentence clozeSentence : nounText.getClozeSentences()) {
                    for (Key key : clozeSentence.getKeys()) {
                        result += "Option " + key.getKeyNumber() + ": " + key.getKeyWord().getContent();// + "(" + key.getKey().getTag() + ")";
                        for (Distractor distractor : key.getBestDistractors(numberOfDistractors)) {
                            result += ", " + distractor.getDistractorWord().getContent();// + "(" + distractor.getDistractor().getTag() + ")";
                        }
                        result = result.trim() + "\n";
                    }
                }
                return result;
            case "Open Questions":
                Text textObject = new Text(text, language);
                tArea.setText("");
                result = textObject.getContent() + "\n\n";

                int i = 1;
                for (Question question : textObject.getQuestions()) {
                    result += i + ": " + question.getContent() + "\n";
                    i++;
                }
                return result;
            case "MCQ - Definition":
                //TODO Use url!
                String html = Wrapper.loadTextFile(Resources.class.getResourceAsStream("Glossary (EN).xml"));
                ClozeText definitionText = new ClozeText(html, "wiki-body", Language.EN, Approach.DEFINITION);
                tArea.setText("");
                result = "";

                int keyNumber = 1;
                for (ClozeSentence clozeSentence : definitionText.getClozeSentences()) {
                    result += keyNumber + ": " + clozeSentence.getContent() + "\n";
                    keyNumber++;
                }
                result += "\n";
                
                for (ClozeSentence clozeSentence : definitionText.getClozeSentences()) {
                    for (Key key : clozeSentence.getKeys()) {
                        result += "Option " + key.getKeyNumber() + ": " + key.getKeyWord().getContent();// + "(" + key.getKey().getTag() + ")";
                        for (Distractor distractor : key.getBestDistractors(numberOfDistractors)) {
                            result += ", " + distractor.getDistractorWord().getContent();// + "(" + distractor.getDistractor().getTag() + ")";
                        }
                        result = result.trim() + "\n";
                    }
                }
                
                return result;
            case "Cloze - Annotation":
                ClozeText comprehensionText = new ClozeText(text, language, Approach.ANNOTATION);
                tArea.setText("");
                result = comprehensionText.getClozeText() + "\n\n";
                
                for (ClozeSentence clozeSentence : comprehensionText.getClozeSentences()) {
                    for (Key key : clozeSentence.getKeys()) {
                        result += "Option " + key.getKeyNumber() + ": " + key.getKeyWord().getContent();// + "(" + key.getKey().getTag() + ")";
                        for (Distractor distractor : key.getBestDistractors(numberOfDistractors)) {
                            result += ", " + distractor.getDistractorWord().getContent();// + "(" + distractor.getDistractor().getTag() + ")";
                        }
                        result = result.trim() + "\n";
                    }
                }
                return result;
            case "ClozeMatch - Noun":
                ClozeText clozeNounMatcHText = new ClozeText(text, language, Approach.NOUNMATCH);
                tArea.setText("");
                result = clozeNounMatcHText.getClozeText() + "\n\n Enter one of the listed words into a gap:\n\n";
                
                for (ClozeSentence clozeSentence : clozeNounMatcHText.getClozeSentences()) {
                    for (Key key : clozeSentence.getKeys()) { 
                        result += key.getKeyWord().getContent() + "\n";
                    }
                }
                return result;
            case "ClozeMatch - Annotation":
                ClozeText clozeAnnotationMatcHText = new ClozeText(text, language, Approach.ANNOTATIONMATCH);
                tArea.setText("");
                result = clozeAnnotationMatcHText.getClozeText() + "\n\n Enter one of the listed words into a gap:\n\n";
                
                for (ClozeSentence clozeSentence : clozeAnnotationMatcHText.getClozeSentences()) {
                    for (Key key : clozeSentence.getKeys()) { 
                        result += key.getKeyWord().getContent() + "\n";
                    }
                }
                return result;
            case "ClozeMatch - Verb":
                ClozeText clozeVerbMatcHText = new ClozeText(text, language, Approach.VERBMATCH);
                tArea.setText("");
                result = clozeVerbMatcHText.getClozeText() + "\n\n Enter one of the listed words into a gap:\n\n";
                
                for (ClozeSentence clozeSentence : clozeVerbMatcHText.getClozeSentences()) {
                    for (Key key : clozeSentence.getKeys()) { 
                        result += key.getKeyWord().getContent() + "\n";
                    }
                }
                return result;
            default: 
                return startingText;
        }
    }
    
    private void outputCloze(String selectedOption) throws IOException  {
        //Create new Thread for a processing message... TODO Real processing bar!
        Thread t=new Thread(new Runnable(){
            public void run(){                
                Dialog dialog = new Dialog(processFrame, "Processing...");
                dialog.setSize(200, 10);
                dialog.setLocationRelativeTo(tArea);
                dialog.setVisible(true);

            }
        });
        
        //Start threat and create cloze
        t.start();
        tArea.setText(ClozeApproach(selectedOption));
        
        //Close progress bar and threat
        processFrame.setVisible(false);
        t.interrupt();
        Gui.logger.log(Level.INFO, "Cloze generation from Text successfully terminated.");
    }
    
    @Override
    public void actionPerformed(final ActionEvent evt) {
        if (evt.getSource() == bOpen) {
            tArea.setText(startingText);
            try {
                openText();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Problem loading text. Please try again.", e);
                e.printStackTrace();
            }
        } 
        
        if (evt.getSource() == cbGenerate) {
            final String selectedOption = (String) cbGenerate.getSelectedItem();
            
            if (selectedOption.equals("Pick an option")) {
                JOptionPane.showMessageDialog(frame, "Select an option to contiune.");
                logger.log(Level.INFO, "Select an option to contiune.");
            }
            
            if (!tArea.getText().equals(startingText) && !tArea.getText().equals("")) {
                try {
                    text = tArea.getText();
                    this.language = LanguageHelper.detectLanguage(text);
                    logger.log(Level.INFO, "The text language is in " + this.language);
                    outputCloze(selectedOption);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Problem generatring Cloze text.", e);
                    e.printStackTrace();
                }
            }          
                
//                QtiTemplate template = new QtiTemplate("GuiText", clozeText);
//                template.save("Examples\\Template\\", "GuiText");
//                Gui.logger.log(Level.INFO, "Template successfully saved to " + "Examples\\Template\\GuiText.xml");
        }
    }

    public static void main(final String st[]) {
        final Gui dyna = new Gui();
        dyna.tArea.setText(startingText);
        try {
            dyna.prepareAndShowGUI();
        } catch (final IOException e) {
            Gui.logger.log(Level.SEVERE, "Some sort of I/O exception occured by preparing the GUI.");
            e.printStackTrace();
        }
    }
}
