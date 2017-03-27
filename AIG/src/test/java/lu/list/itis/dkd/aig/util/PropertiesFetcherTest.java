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

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.cloze.util.TestResources;

import java.util.Properties;

/**
 * Class holding two test methods for assessing whether all property files are accessible by the
 * software as deployed.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.8.0
 */
public class PropertiesFetcherTest {
    /**
     * Test method for {@link lu.list.itis.dkd.aig.util.PropertiesFetcher#fetchProperties()}.
     */
    @Test
    public void testFetchRootProperties() {
        final Properties properties = PropertiesFetcher.getProperties();
        Assert.assertNotNull(properties);
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.util.PropertiesFetcher#fetchProperties(String)}.
     */
    @Test
    public void testFetchProperties() {
        final Properties properties = PropertiesFetcher.getProperties(TestResources.class, "test.properties"); //$NON-NLS-1$
        Assert.assertNotNull(properties);
    }

    /**
     * Test method for {@link lu.list.itis.dkd.aig.util.PropertiesFetcher#fetchProperties(String)}
     * using an non-existing file as inputs. This should test whether an exception is correctly
     * thrown instead of swallowed or simply any Properties object returned.
     */
    @Test(expected = NullPointerException.class)
    public void testfetchPropertiesException() {
        PropertiesFetcher.getProperties(TestResources.class, "ultron.properties"); //$NON-NLS-1$
    }
}