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
package lu.list.itis.dkd.assess.opennlp;

import lu.list.itis.dkd.assess.opennlp.util.ModelLoader;

public final class OpenNlp {
	public static void init() throws ClassNotFoundException {
		// init all dependencies
		forceClassInitialization(ModelLoader.class);
	}

	protected static <T> void forceClassInitialization(Class<T> clazz) throws ClassNotFoundException {
		Class.forName(clazz.getName(), true, clazz.getClassLoader());
	}
}
