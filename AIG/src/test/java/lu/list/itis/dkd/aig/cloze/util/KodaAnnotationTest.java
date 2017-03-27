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
package lu.list.itis.dkd.aig.cloze.util;

import lu.list.itis.dkd.aig.util.Externalization;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.9
 * @version 0.9.0
 */
public class KodaAnnotationTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {}


    /**
     * Test whether the string has correctly been externalized.
     */
    @Test
    public final void testExternalization() {

        Assert.assertEquals("\"", Externalization.REGEX_SINGLE_QUOTE); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.cloze.util.KodaAnnotation#getAnnotations(lu.list.itis.dkd.assess.opennlp.Sentence)}
     * .
     */
    @Test
    public final void testGetAnnotationsSentence() {
        Assert.fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.cloze.util.KodaAnnotation#getAnnotations(java.lang.String, lu.list.itis.dkd.assess.opennlp.util.Type.Language)}
     * .
     */
    @Test
    public final void testGetAnnotationsStringLanguage() {
        Assert.fail("Not yet implemented"); // TODO
    }

}
