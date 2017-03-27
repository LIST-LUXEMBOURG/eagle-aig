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
package lu.list.itis.dkd.aig.util;

import lu.list.itis.dkd.dbc.annotation.NonNullByDefault;
import lu.list.itis.dkd.dbc.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * Helper class providing static method to load and fetch templates.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 2.0.0
 */
@NonNullByDefault
public class TemplateManager {
    private static final String PROPERTIES = "siren.properties"; //$NON-NLS-1$

    private static Properties properties = new Properties();

    static {
        try {
            TemplateManager.properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(TemplateManager.PROPERTIES));
        } catch (final IOException e) {
            Logger.getLogger(TemplateManager.class.getSimpleName()).log(Level.SEVERE, "The porperties file could not be loaded!", e); //$NON-NLS-1$
        }
    }

    /**
     * Method used for storing a template file.
     *
     * @param context
     *        The context of the servlet calling the {@link TemplateManager} used to determine the
     *        path to store the file.
     * @param stream
     *        The inputs stream from which the template to be store is read.
     * @param name
     *        The name of the template to store. The template will be stored as:
     *        [subFolder]/name.xml
     * @param subFolder
     *        The sub-folder from the template directory defined in the properties, to store the
     *        template file in. If you specify a subFolder you need to provide a trailing slash.
     * @throws IOException
     *         If an I/O error occurs while reading from the stream.
     * @throws UnsupportedEncodingException
     *         If the encoding of the stream is not supported.
     */
    public static void store(final ServletContext context, final InputStream stream, final String name, @Nullable final String subFolder) throws UnsupportedEncodingException, IOException {
        final File template = new File(context.getRealPath("/"), Strings.nullToEmpty(subFolder) + name + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
        if (!template.exists()) {
            template.getParentFile().mkdirs();
            template.createNewFile();
        }

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(template), "UTF-8"))) { //$NON-NLS-1$
            final String stringFromStream = CharStreams.toString(new InputStreamReader(stream, "UTF-8")); //$NON-NLS-1$
            writer.println(stringFromStream);
        }
    }

    public static String getProperty(final String propertyName) {
        return TemplateManager.properties.getProperty(propertyName);
    }

    /**
     * Method used to retrieve a template file with a given name from a specified sub-folder.
     *
     * @param context
     *        The context of the servlet calling the {@link TemplateManager} used to determine the
     *        path to store the file.
     * @param name
     *        The name of the template to retrieve.
     * @param subFolder
     *        The sub-folder to retrieve the template from.
     * @return The template as a {@link String}
     * @throws NoSuchFileException
     *         Thrown when the specified file could not be fetched.
     * @throws IOException
     *         Thrown when the template could not be read.
     */
    public static String fetch(final ServletContext context, final String name, @Nullable final String subFolder) throws NoSuchFileException, IOException {
        final byte[] bytes = Files.readAllBytes(Paths.get(context.getRealPath("/") + Strings.nullToEmpty(subFolder) + name + ".xml")); //$NON-NLS-1$ //$NON-NLS-2$
        return new String(bytes, "UTF-8"); //$NON-NLS-1$
    }

    /**
     * Method used to delete a template file with a given name from a specific sub-folder.
     *
     * @param context
     *        The context of the servlet calling the {@link TemplateManager} used to determine the
     *        path to store the file.
     * @param name
     *        The name of the template to delete.
     * @param subFolder
     *        The sub-folder to delete the template from.
     * @return <code>true</code> if the file was deleted, <code>false</code> if the file did not
     *         exist.
     * @throws IOException
     *         Thrown when the template could not be deleted.
     */
    public static boolean delete(final ServletContext context, final String name, @Nullable final String subFolder) throws IOException {
        return Files.deleteIfExists(Paths.get(context.getRealPath("/") + Strings.nullToEmpty(subFolder) + name + ".xml")); //$NON-NLS-1$ //$NON-NLS-2$
    }
}