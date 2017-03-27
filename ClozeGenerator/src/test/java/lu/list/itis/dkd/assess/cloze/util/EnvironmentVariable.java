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
package lu.list.itis.dkd.assess.cloze.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class EnvironmentVariable {
	private static Properties properties = ClozePropertiesFetcher.fetchProperties("cloze.properties");

	@Test
	public void envTest() {

		checkProperty("disco.new.english");
		checkProperty("disco.new.german");
		checkProperty("disco.new.french");

	}

	private void checkProperty(String property) {
		String envVariable = properties.getProperty(property);
		Assert.assertNotNull(envVariable);
		String envVariableValue = System.getenv(envVariable);
		Assert.assertNotNull(envVariableValue);
		Files.isDirectory(Paths.get(envVariableValue));
	}
}
