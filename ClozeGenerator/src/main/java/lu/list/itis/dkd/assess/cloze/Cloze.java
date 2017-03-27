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

import lu.list.itis.dkd.assess.cloze.option.ClozeText;
import lu.list.itis.dkd.assess.cloze.util.ClozePropertiesFetcher;
import lu.list.itis.dkd.assess.cloze.util.ClozeVariable.Approach;
import lu.list.itis.dkd.assess.cloze.util.DiscoLoader;
import lu.list.itis.dkd.assess.cloze.util.NameLoader;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

public final class Cloze {
	public static void init() throws ClassNotFoundException {
		// init some dependencies that take time to load
		forceClassInitialization(DiscoLoader.class);
		forceClassInitialization(ClozePropertiesFetcher.class);
		forceClassInitialization(NameLoader.class);

		// let's analyse some dummy text to initialize some other classes like
		// the maltparser
		new ClozeText("<div class=\"body\"><p>This is a dummy paragraph.</p><p>This is a dummy paragraph.</p></div>", "body",
				Language.EN, Approach.ANNOTATION, 1, false);
		new ClozeText("<div class=\"body\"><p>Ceci est un paragraphe fictif.</p><p>Ceci est un paragraphe fictif.</p></div>", "body", Language.FR,
				Approach.ANNOTATION, 1, false);
		new ClozeText("<div class=\"body\"><p>Dies ist ein fiktiver Absatz.</p><p>Dies ist ein fiktiver Absatz.</p></div>", "body", Language.DE,
				Approach.ANNOTATION, 1, false);
	}

	protected static <T> void forceClassInitialization(Class<T> clazz) throws ClassNotFoundException {
		Class.forName(clazz.getName(), true, clazz.getClassLoader());
	}
}
