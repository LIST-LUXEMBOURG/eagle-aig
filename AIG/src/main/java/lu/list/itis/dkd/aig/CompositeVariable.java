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
package lu.list.itis.dkd.aig;

import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

import com.google.common.collect.ArrayListMultimap;

import lu.list.itis.dkd.aig.resolution.TemplateParseException;
import lu.list.itis.dkd.aig.util.Externalization;

public class CompositeVariable {

	public static Variable buildVariable(String label, String language, URI uri) throws TemplateParseException {

		ArrayListMultimap<String, String> parameters = ArrayListMultimap.create();
		ArrayList<Value> values = new ArrayList<>();

		Locale locale = Locale.forLanguageTag(language);

		parameters.put(Externalization.IDENTIFIER_ELEMENT, uri.toString());
		parameters.put(Externalization.LANGUAGE_ELEMENT, locale.getLanguage());
		parameters.put(Externalization.INITIALIZATION_ELEMENT, uri.toString());

		values.add(new Value(uri, ValueType.TEXT, label));

		Variable variable = new Variable(parameters, values);

		return variable;
	}

}
