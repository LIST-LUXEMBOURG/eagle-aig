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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FusekiTest {

	protected static final String DUMMY_ONTOLOGY = "dummyLittleOntology.owl";
	protected static final String dataSetName = createRandomDataSetName();

	@Before
	@After
	public void cleanDataSet() throws IOException, HttpException {
		if (FusekiHttpHelper.chechIfExist(dataSetName)) {
			FusekiHttpHelper.deleteDataSet(dataSetName);
		}
	}

	@Test
	public void checkIfDataSetExists() throws IOException, HttpException {
		// check not exists
		Assert.assertFalse("DataSet already exists", FusekiHttpHelper.chechIfExist(dataSetName));

		// create it
		FusekiHttpHelper.createDataSet(dataSetName);

		// check if exists
		Assert.assertTrue("DataSet not exists", FusekiHttpHelper.chechIfExist(dataSetName));
	}

	@Test
	public void deleteDataSet() throws IOException, HttpException {
		// create it
		FusekiHttpHelper.createDataSet(dataSetName);

		// check if exists
		Assert.assertTrue("DataSet not exists", FusekiHttpHelper.chechIfExist(dataSetName));

		// delete it
		FusekiHttpHelper.deleteDataSet(dataSetName);

		// check not exists
		Assert.assertFalse("DataSet still exists", FusekiHttpHelper.chechIfExist(dataSetName));
	}

	@Test
	public void createDataSet() throws IOException, HttpException {

		// create it
		FusekiHttpHelper.createDataSet(dataSetName);

		// check if exists
		Assert.assertTrue("DataSet not exists", FusekiHttpHelper.chechIfExist(dataSetName));
	}

	@Test
	public void uploadFileInDefaultGraph() throws HttpException, IOException {

		// create dataset
		FusekiHttpHelper.createDataSet(dataSetName);

		// uploadOntology
		FusekiHttpHelper.uploadOntology(FusekiTest.class.getResourceAsStream(DUMMY_ONTOLOGY), dataSetName, null);
	}

	@Test
	public void uploadFileInSpecifiedGraph() throws HttpException, IOException {

		// create dataset
		FusekiHttpHelper.createDataSet(dataSetName);

		// uploadOntology
		FusekiHttpHelper.uploadOntology(FusekiTest.class.getResourceAsStream(DUMMY_ONTOLOGY), dataSetName, "GraphTest");
	}

	protected static String createRandomDataSetName() {

		String dataSetPrefix = PropertiesFetcher.getProperties().getProperty(Externalization.FUSEKI_DATASET);
		String className = FusekiTest.class.getSimpleName();

		return FusekiHttpHelper.createRandomDataSetName(dataSetPrefix + '_' + className + '_');
	}

}
