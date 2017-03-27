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
package lu.list.itis.dkd.assess.opennlp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Resources {
	private static final Path tmpDirectory = Paths.get(System.getProperty("java.io.tmpdir"));
	protected static final Logger logger = Logger.getLogger(Resources.class.getName());

	/**
	 * 
	 * @param resourceRelativePath
	 * @return a temporary file
	 */
	public static File getFile(String resourceRelativePath) {
		Path filePath = Paths.get(resourceRelativePath);
		Path target = tmpDirectory.resolve(filePath);
		logger.log(Level.INFO, "Copy src: " + resourceRelativePath + " to: " + target);
		try {
			Files.createDirectories(tmpDirectory.resolve(filePath.getParent()));
			Files.copy(Resources.class.getResourceAsStream(resourceRelativePath), target,
					StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to handle: " + e.getMessage() + " for: " + resourceRelativePath);
		}

		return target.toFile();

	}

	public static Path createTmpDirectory(String resourceRelativePath) {
		Path filePath = Paths.get(resourceRelativePath);
		Path target = tmpDirectory.resolve(filePath);
		try {
			Files.createDirectories(tmpDirectory.resolve(target));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to handle: " + e.getMessage() + " for: " + resourceRelativePath);
		}
		return target;
	}

	public static FileInputStream getFileInputStream(String resourceRelativePath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Resources.getFile(resourceRelativePath));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Resource not found: " + e.getMessage() + " for: " + resourceRelativePath);
		}

		return fis;

	}

}