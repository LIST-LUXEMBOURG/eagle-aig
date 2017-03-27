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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import lu.list.itis.dkd.assess.cloze.option.Distractor;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class UrlHelper {
    protected static final Logger logger = Logger.getLogger(UrlHelper.class.getSimpleName());
      
    /**
     * This method requires an proper UTF8 encoded url to connect to a website and returns
     * the source code of the page or an empty String if the connection failed.
     * @param encodedUrl
     * @return
     */
    public static String getSource(String encodedUrl) {        
        URL myURL;
        try {            
            //Connect to url
            myURL = new URL(encodedUrl);
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    
            HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
            myURLConnection.connect();
            
            int status = myURLConnection.getResponseCode();
            if (status == 500){ 
                return "";
            }
    
            final InputStream is = myURLConnection.getInputStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
            final StringBuilder sb = new StringBuilder();
            
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            
            return sb.toString();
        } catch (IOException e) {
            //TODO Stop when ...
            //https://cloud.google.com/storage/docs/exponential-backoff            
            logger.log(Level.INFO, encodedUrl + " is busy. Waiting 1 minute and retry.");
            try {
                Thread.sleep(64000);
                getSource(encodedUrl);
            } catch (InterruptedException e1) {
                logger.log(Level.SEVERE, "Connection to " + encodedUrl + " failed permanently.");
                return "";
            }
            return "";
        }
    }
    
    /**
     * This method requires an proper UTF8 encoded url to connect to a website and returns
     * the source code of the page or an empty String if the connection failed.
     * @param encodedUrl
     * @param A mediatype. E.g. JSON_UTF_8 or MediaType.APPLICATION_XML_UTF_8
     * @return
     */
    public static String getSource(String encodedUrl, MediaType mediatype) {        
        URL myURL;
        try {            
            //Connect to url
            myURL = new URL(encodedUrl);
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    
            HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
            
            //Set mediatype
            myURLConnection.setRequestProperty(HttpHeaders.ACCEPT.toString(), mediatype.toString());
            myURLConnection.connect();
            
            int status = myURLConnection.getResponseCode();
            if (status == 500){ 
                return "";
            }
    
            final InputStream is = myURLConnection.getInputStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
            final StringBuilder sb = new StringBuilder();
            
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            
            return sb.toString();
        } catch (IOException e) {
            //TODO Stop when ...
            //https://cloud.google.com/storage/docs/exponential-backoff            
            logger.log(Level.INFO, encodedUrl + " is busy. Waiting 1 minute and retry.");
            try {
                Thread.sleep(64000);
                getSource(encodedUrl);
            } catch (InterruptedException e1) {
                logger.log(Level.SEVERE, "Connection to " + encodedUrl + " failed permanently.");
                return "";
            }
            return "";
        }
    }
    
    /**
     * Forms an URL to ask the Google Ngram Viewer for an article for the key.
     * Warning: More than 5 distractors may cause that the Google Ngram Viewer don't deliver any result. 
     * @param distractors
     * @param language
     * @return Returns the result (html source code) as String for the request. 
     */
    public static String getSource(String key, Language language) {
        String phrase = "*_DET+" + key + "%2C";
        String url = "https://books.google.com/ngrams/graph?content=" + phrase;
        String corpus = "&year_start=1800&year_end=2000";
        String corpus_language = "";
        switch (language) {
            case DE:
                corpus_language = "&corpus=20";
                break;
            case FR:
                corpus_language = "&corpus=19";
                break;
            default:
                return "";
        }
        
        return getSource(url + corpus + corpus_language + "&smoothing=3&share=");
    }
    
    /**
     * Forms an URL to ask the Google Ngram Viewer for an article for each distractor in the list.
     * Warning: More than 5 distractors may cause that the Google Ngram Viewer don't deliver any result. 
     * @param distractors
     * @param language
     * @return Returns the result (html source code) as String for the request. 
     */
    public static String getSource(List<Distractor> distractors, Language language){
        String phrase = "";
        for (Distractor distractor : distractors) {
            phrase += "*_DET+" + distractor.getDistractorWord().getContent() + "%2C";
        }
        //Remove last comma (%2C)
        phrase = phrase.substring(0, phrase.length()-3);        
        
        String url = "https://books.google.com/ngrams/graph?content=" + phrase;// + "&case_insensitive=on";
        String corpus = "&year_start=1800&year_end=2000";
        String corpus_language = "";
        switch (language) {
            case DE:
                corpus_language = "&corpus=20";
                break;
            case FR:
                corpus_language = "&corpus=19";
                break;
            default:
                return "";
        }

        return getSource(url + corpus + corpus_language + "&smoothing=3&share=");
    }
}
