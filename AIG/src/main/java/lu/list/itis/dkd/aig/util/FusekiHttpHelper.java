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

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.apache.http.HttpException;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

import lu.list.itis.dkd.dbc.annotation.NonNull;
import lu.list.itis.dkd.dbc.annotation.Nullable;

/**
 * Class dedicated to the communication with a Fuseki Server instance
 * 
 * @author pedretti
 *
 */
public class FusekiHttpHelper {

	private static final Logger logger = Logger.getLogger(FusekiHttpHelper.class.getSimpleName());

	protected final static String EOL = "\r\n";
	protected final static String BOUNDARY_DECORATOR = "--";
	protected final static String HOST = PropertiesFetcher.getProperties().getProperty(Externalization.FUSEKI_HOST);

	/**
	 * Check if specified dataset already exists on server
	 * 
	 * @param dataSetName
	 * @return boolean true if the dataset already exits, false if not
	 * @throws IOException
	 * @throws HttpException
	 */
	public static boolean chechIfExist(String dataSetName) throws IOException, HttpException {
		boolean exists = false;
		URL url = new URL(HOST + "/$/datasets/" + dataSetName);
		final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setUseCaches(false);
		httpConnection.setRequestMethod("GET");

		// handle HTTP/HTTPS strange behaviour
		httpConnection.connect();
		httpConnection.disconnect();

		// handle response
		switch (httpConnection.getResponseCode()) {
		case HttpURLConnection.HTTP_OK:
			exists = true;
			break;
		case HttpURLConnection.HTTP_NOT_FOUND:
			break;
		default:
			throw new HttpException(
					httpConnection.getResponseCode() + " message: " + httpConnection.getResponseMessage());
		}
		return exists;
	}

	/**
	 * Delete specified dataset
	 * 
	 * @param dataSetName
	 * @throws IOException
	 * @throws HttpException
	 */
	public static void deleteDataSet(@NonNull String dataSetName) throws IOException, HttpException {
		logger.info("delete dataset: " + dataSetName);

		URL url = new URL(HOST + "/$/datasets/" + dataSetName);
		final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setUseCaches(false);
		httpConnection.setRequestMethod("DELETE");
		httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// handle HTTP/HTTPS strange behaviour
		httpConnection.connect();
		httpConnection.disconnect();

		// handle response
		switch (httpConnection.getResponseCode()) {
		case HttpURLConnection.HTTP_OK:
			break;
		default:
			throw new HttpException(
					httpConnection.getResponseCode() + " message: " + httpConnection.getResponseMessage());
		}
	}

	/**
	 * Create a new dataset
	 * 
	 * @param dataSetName
	 * @throws IOException
	 * @throws HttpException
	 */
	public static void createDataSet(@NonNull String dataSetName) throws IOException, HttpException {
		logger.info("create dataset: " + dataSetName);

		URL url = new URL(HOST + "/$/datasets");
		final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setUseCaches(false);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// set content
		httpConnection.setDoOutput(true);
		final OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
		out.write("dbName=" + URLEncoder.encode(dataSetName, "UTF-8") + "&dbType=mem");
		out.close();

		// handle HTTP/HTTPS strange behaviour
		httpConnection.connect();
		httpConnection.disconnect();

		// handle response
		switch (httpConnection.getResponseCode()) {
		case HttpURLConnection.HTTP_OK:
			break;
		default:
			throw new HttpException(
					httpConnection.getResponseCode() + " message: " + httpConnection.getResponseMessage());
		}
	}

	/**
	 * Upload ontology content on specified dataset. Graph used is the default
	 * one except if specified
	 * 
	 * @param ontology
	 * @param datasetName
	 * @param graphName
	 * @throws IOException
	 * @throws HttpException
	 */
	public static void uploadOntology(InputStream ontology, String datasetName, @Nullable String graphName)
			throws IOException, HttpException {
		graphName = Strings.emptyToNull(graphName);

		logger.info("upload ontology in dataset: " + datasetName + " graph:" + Strings.nullToEmpty(graphName));

		boolean createGraph = (graphName != null) ? true : false;
		String dataSetEncoded = URLEncoder.encode(datasetName, "UTF-8");
		String graphEncoded = createGraph ? URLEncoder.encode(graphName, "UTF-8") : null;

		URL url = new URL(HOST + '/' + dataSetEncoded + "/data" + (createGraph ? "?graph=" + graphEncoded : ""));

		final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

		String boundary = "------------------" + System.currentTimeMillis()
				+ Long.toString(Math.round(Math.random() * 1000));

		httpConnection.setUseCaches(false);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		httpConnection.setRequestProperty("Connection", "keep-alive");
		httpConnection.setRequestProperty("Cache-Control", "no-cache");

		// set content
		httpConnection.setDoOutput(true);
		final OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
		out.write(BOUNDARY_DECORATOR + boundary + EOL);
		out.write("Content-Disposition: form-data; name=\"files[]\"; filename=\"ontology.owl\"" + EOL);
		out.write("Content-Type: application/octet-stream" + EOL + EOL);
		out.write(CharStreams.toString(new InputStreamReader(ontology)));
		out.write(EOL + BOUNDARY_DECORATOR + boundary + BOUNDARY_DECORATOR + EOL);
		out.close();

		// handle HTTP/HTTPS strange behaviour
		httpConnection.connect();
		httpConnection.disconnect();

		// handle response
		switch (httpConnection.getResponseCode()) {
		case HttpURLConnection.HTTP_CREATED:
			checkState(createGraph, "bad state - code:" + httpConnection.getResponseCode() + " message: "
					+ httpConnection.getResponseMessage());
			break;
		case HttpURLConnection.HTTP_OK:
			checkState(!createGraph, "bad state - code:" + httpConnection.getResponseCode() + " message: "
					+ httpConnection.getResponseMessage());
			break;
		default:
			throw new HttpException(
					httpConnection.getResponseCode() + " message: " + httpConnection.getResponseMessage());
		}

	}

	/**
	 * Create a random dataset name with an optionnal prefix
	 * 
	 * @param prefix
	 * @return a random dataset name
	 */
	public static String createRandomDataSetName(@Nullable String prefix) {

		String random = Long.toString(Math.round(Math.random() * 1000));
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'mm'm'ss's'S"));

		String dataSet = Strings.nullToEmpty(prefix) + date + '_' + random;

		return dataSet;
	}

	/**
	 * Build the SPARQL endpoint URL depending on the dataset name
	 * 
	 * @param datasetname
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL getSparqlEndPoint(String datasetname) throws MalformedURLException {
		return new URL(HOST + '/' + datasetname + "/query");
	}
}
