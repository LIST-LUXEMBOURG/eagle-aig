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
package lu.list.itis.dkd.aig.cloze;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.junit.Test;

import lu.list.itis.dkd.aig.Common;
import lu.list.itis.dkd.aig.resolution.ResolutionException;
import lu.list.itis.dkd.aig.resolution.TemplateConsistencyException;
import lu.list.itis.dkd.aig.resolution.TemplateParseException;
//import lu.list.itis.dkd.semantic.exception.InitializationException;
//import lu.list.itis.dkd.semantic.exception.SimilarityComputationException;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 0.8
 * @version 0.8.0
 */
public class ClozeItemResolutionTest {

	@Test
	public void testClozeItemResolution1() throws FileNotFoundException, TemplateParseException, ResolutionException,
		TemplateConsistencyException {

		final String text = "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem!Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved. Mechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs). The Soviet MIR series of computers was developed from 1965 to 1969 in a group headed by Pierre Vinken and Thomas Edison."; //$NON-NLS-1$
		final String templateFilename = "ClozeInputTemplate.xml";
		final String templatePath = "resources/templates/cloze/";

		final HashMap<String, String> input = new HashMap<>();
		input.put("text", "<div class=\"body\">" + text + "</div>"); //$NON-NLS-1$

		Common.generateItems(input, new FileInputStream(new File(templatePath + templateFilename)),
				templatePath + "results/" + templateFilename + ".txt");
	}

	@Test
	public void testClozeItemResolution2() throws FileNotFoundException, TemplateParseException, ResolutionException,
			  TemplateConsistencyException {

		final String text = "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem!Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved. Mechanical analog computers started appearing in the first century and were later used in the medieval era for astronomical calculations. In World War II, mechanical analog computers were used for specialized military applications such as calculating torpedo aiming. During this time the first electronic digital computers were developed. Originally they were the size of a large room, consuming as much power as several hundred modern personal computers (PCs). The Soviet MIR series of computers was developed from 1965 to 1969 in a group headed by Pierre Vinken and Thomas Edison."; //$NON-NLS-1$
		final String templateFilename = "ClozeInputTemplateModified.xml";
		final String templatePath = "resources/templates/cloze/";

		final HashMap<String, String> input = new HashMap<>();
		input.put("text", "<div class=\"body\">" + text + "</div>"); //$NON-NLS-1$

		Common.generateItems(input, new FileInputStream(new File(templatePath + templateFilename)),
				templatePath + "results/" + templateFilename + "2" + ".txt");

	}

	@Test
	public void testClozeItemResolution3() throws FileNotFoundException, TemplateParseException, ResolutionException,
			  TemplateConsistencyException {

		final String text = "<p>This is a simple example of structured text.</p>\n<h1>This is the paragraph one</h1>\n<p>In this paragraph is written some english text. Just two sentences are written.</p>\n<h1>Paragraph two</h1>\n<p>This paragraph is splitted in two sections.</p>\n<h2>Section 1</h2>\n<p>This section is a subpart of paragraph two.</p>\n<h2>Section 2</h2>\n<p>This section is the second and last section of paragraph two.</p>\n<p> </p>"; //$NON-NLS-1$
		final String templateFilename = "ClozeInputTemplateModified.xml";
		final String templatePath = "resources/templates/cloze/";

		final HashMap<String, String> input = new HashMap<>();
		input.put("text", "<div class=\"body\">" + text + "</div>"); //$NON-NLS-1$

		Common.generateItems(input, new FileInputStream(new File(templatePath + templateFilename)),
				templatePath + "results/" + templateFilename + "3" + ".txt");

	}

}
