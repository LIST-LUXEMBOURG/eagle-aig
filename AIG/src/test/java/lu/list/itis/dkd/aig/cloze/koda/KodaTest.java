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
package lu.list.itis.dkd.aig.cloze.koda;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.cloze.util.UrlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KodaTest {
    @Test
    public void frenchKoda() {
    	Assert.fail("Not yet implemented"); // TODO getSource accept with content type
        final String url = "http://smartdocs.list.lu/kodaweb/rest/koda-1.0/annotate?text=Un%20ordinateur%20est%20une%20machine%20%C3%A9lectronique%20qui%20fonctionne%20par%20la%20lecture%20s%C3%A9quentielle%20d%27un%20ensemble%20d%27instructions,%20organis%C3%A9es%20en%20programmes,%20qui%20lui%20font%20ex%C3%A9cuter%20des%20op%C3%A9rations%20logiques%20et%20arithm%C3%A9tiques%20sur%20des%20chiffres%20binaires.&ontology=DBPEDIA_EN_FR";
        final String source = UrlHelper.getSource(url);

        // Retrieve Annotations
        final List<String> annotations = new ArrayList<>();
        final Pattern annotationPattern = Pattern.compile("\"[a-zA-Z\\s]*\"");
        final Matcher annotationMatcher = annotationPattern.matcher(source);

        // Read source code and output result
        while (annotationMatcher.find()) {
            String annotationMatch = annotationMatcher.group(0);
            annotationMatch = annotationMatch.replace("\"", "");
            annotations.add(annotationMatch);
        }

        Assert.assertTrue(annotations.contains("ordinateur"));
        Assert.assertTrue(annotations.contains("programmes"));
        Assert.assertTrue(annotations.contains("lecture"));
        Assert.assertTrue(annotations.contains("logiques"));
        Assert.assertTrue(annotations.contains("machine"));
        Assert.assertTrue(annotations.contains("chiffres"));
    }


    @Test
    public void germanKoda() {
    	Assert.fail("Not yet implemented"); // TODO getSource accept with content type
        final String url = "http://smartdocs.list.lu/kodaweb/rest/koda-1.0/annotate?text=Jede%20Software%20ist%20im%20Prinzip%20eine%20definierte,%20funktionale%20Anordnung%20der%20oben%20geschilderten%20Bausteine%20Berechnung,%20Vergleich%20und%20bedingter%20Sprung,%20wobei%20die%20Bausteine%20beliebig%20oft%20verwendet%20werden%20k%C3%B6nnen.%20Diese%20Anordnung%20der%20Bausteine,%20die%20als%20Programm%20bezeichnet%20wird,%20wird%20in%20Form%20von%20Daten%20im%20Speicher%20des%20Computers%20abgelegt.&ontology=DBPEDIA_EN_DE";

        final String source = UrlHelper.getSource(url);

        // Retrieve Annotations
        final List<String> annotations = new ArrayList<>();
        final Pattern annotationPattern = Pattern.compile("\"[a-zA-Z\\s]*\"");
        final Matcher annotationMatcher = annotationPattern.matcher(source);

        // Read source code and output result
        while (annotationMatcher.find()) {
            String annotationMatch = annotationMatcher.group(0);
            annotationMatch = annotationMatch.replace("\"", "");
            annotations.add(annotationMatch);
        }

        Assert.assertTrue(annotations.contains("Software"));
        Assert.assertTrue(annotations.contains("Prinzip"));
        Assert.assertTrue(annotations.contains("Vergleich"));
        Assert.assertTrue(annotations.contains("Anordnung"));
        Assert.assertTrue(annotations.contains("Sprung"));
        Assert.assertTrue(annotations.contains("Anordnung"));
        Assert.assertTrue(annotations.contains("Programm"));
        Assert.assertTrue(annotations.contains("Form"));
        Assert.assertTrue(annotations.contains("Daten"));
    }


    @Test
    public void enlishKoda() {
    	Assert.fail("Not yet implemented"); // TODO getSource accept with content type
        final String url = "http://smartdocs.list.lu/kodaweb/rest/koda-1.0/annotate?text=A%20computer%20is%20a%20general-purpose%20device%20that%20can%20be%20programmed%20to%20carry%20out%20a%20set%20of%20arithmetic%20or%20logical%20operations%20automatically.&ontology=DBPEDIA_EN_EN";
        final String source = UrlHelper.getSource(url);

        // Retrieve Annotations
        final List<String> annotations = new ArrayList<>();
        final Pattern annotationPattern = Pattern.compile("\"[a-zA-Z\\s]*\"");
        final Matcher annotationMatcher = annotationPattern.matcher(source);

        // Read source code and output result
        while (annotationMatcher.find()) {
            String annotationMatch = annotationMatcher.group(0);
            annotationMatch = annotationMatch.replace("\"", "");
            annotations.add(annotationMatch);
            // System.out.println(annotationMatch);
        }

        Assert.assertTrue(annotations.contains("arithmetic or logical"));
        Assert.assertTrue(annotations.contains("computer"));
        Assert.assertTrue(annotations.contains("device"));
    }
}
