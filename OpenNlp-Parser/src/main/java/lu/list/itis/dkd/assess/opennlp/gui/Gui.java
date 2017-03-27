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
package lu.list.itis.dkd.assess.opennlp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.util.logging.Logger;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.LanguageHelper;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Younes Djaghloul [younes.djaghloul@list.lu]
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 2.0
 * @version 4.0.1
 */
public class Gui {
    private JFrame frmComplexityMetricsList;
    private JTextArea taTextToAnalyse = new JTextArea(2, 5);
    private JTextArea txtLem = new JTextArea();
    private Text text;
    
    protected static final Logger logger = Logger.getLogger(Text.class.getSimpleName());
    
    /**
     * Launch the application.
     * @throws LangDetectException 
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Gui window = new Gui();
                window.frmComplexityMetricsList.setVisible(true);
            }
        });
    }
    
    /**
     * Create the application.
     */
    public Gui() {
        frmComplexityMetricsList = new JFrame();
        frmComplexityMetricsList.setTitle("Complexity Metrics (LIST) V4.0.1");
        frmComplexityMetricsList.setBounds(100, 100, 828, 539);
        frmComplexityMetricsList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmComplexityMetricsList.getContentPane().setLayout(null);
        
        taTextToAnalyse.setLineWrap(true);
        txtLem.setLineWrap(true);

        final JTextArea txtResults = new JTextArea();
        txtResults.setEditable(false);
        txtResults.setBackground(new Color(255, 204, 204));
        txtResults.setForeground(Color.BLACK);
        txtResults.setText("results");
        txtResults.setBounds(557, 57, 242, 351);

        frmComplexityMetricsList.getContentPane().add(txtResults);

        JButton btnGetMetrics = new JButton("Get Metrics");
        btnGetMetrics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String content = taTextToAnalyse.getText();
                    //Detect language
                    Language language = LanguageHelper.detectLanguage(content);
                    text = new Text(content, language);
                    txtResults.setText(analyse(text));
                } catch (IOException e1) {
                    Gui.logger.log(Level.SEVERE, "Problem generating text object.", e);
                    e1.printStackTrace();
                }
            }
        });

        btnGetMetrics.setBounds(10, 441, 139, 23);
        frmComplexityMetricsList.getContentPane().add(btnGetMetrics);

        JLabel lblTextToAnalyse = new JLabel("Text to analyse");
        lblTextToAnalyse.setBounds(10, 11, 89, 14);
        frmComplexityMetricsList.getContentPane().add(lblTextToAnalyse);

        JLabel lblResults = new JLabel("Results");
        lblResults.setBounds(557, 11, 46, 14);
        frmComplexityMetricsList.getContentPane().add(lblResults);

        JButton btnInfo = new JButton("Infos");
        btnInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WaitDialog waitDialog = new WaitDialog();
                waitDialog.setVisible(true);
            }
        });
        btnInfo.setBounds(710, 441, 89, 23);
        frmComplexityMetricsList.getContentPane().add(btnInfo);

        JList<String> list = new JList<>();
        list.setBounds(495, 28, -82, -17);
        frmComplexityMetricsList.getContentPane().add(list);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(10, 36, 507, 374);
        frmComplexityMetricsList.getContentPane().add(tabbedPane);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Test To Analye", null, panel, null);
        panel.setLayout(null);
        JScrollPane taScrollPane = new JScrollPane();
        taScrollPane.setBounds(10, 11, 482, 324);
        panel.add(taScrollPane);
        taScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        taTextToAnalyse.setBackground(new Color(127, 255, 212));
        taScrollPane.setViewportView(taTextToAnalyse);
        panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { taScrollPane, taTextToAnalyse }));

        JPanel lemmatizePanel = new JPanel();
        tabbedPane.addTab("Lemmatized Test", null, lemmatizePanel, null);
        lemmatizePanel.setLayout(null);

        JScrollPane lemmatizeScrollPane = new JScrollPane();
        lemmatizeScrollPane.setBounds(10, 11, 482, 324);
        lemmatizePanel.add(lemmatizeScrollPane);

        txtLem.setBackground(Color.CYAN);
        lemmatizeScrollPane.setViewportView(txtLem);
    }

    private final String analyse(Text text) throws IOException {
        WaitDialog waitDialog = new WaitDialog();
        waitDialog.setVisible(true);
        String metrcis = "";
        
        Complexity complexity = new Complexity(text);
        metrcis = complexity.getComplexity();
        waitDialog.setVisible(false);
        
        
        
        String lemText = text.getLemmaText();
        txtLem.setText(lemText);
        return metrcis;
    }
}

