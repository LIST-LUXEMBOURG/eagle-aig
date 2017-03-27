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

import org.junit.Assert;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author Eric TOBIAS [eric.tobias@list.lu]
 * @since 2 Dec 2014
 * @version 150108
 */
public class ClusterServiceTest
{
    private static final String XML =
                    "<items> <item> <stemVariable> <label>Mali</label> <uri>http://dbpedia.org/resource/Mali</uri> <language>en</language> </stemVariable> <answerVariable> <label>Bamako</label> <uri>http://dbpedia.org/resource/Bamako</uri> <language>en</language> </answerVariable> </item> <item> <stemVariable> <label>Guinea-Bissau</label> <uri>http://dbpedia.org/resource/Guinea-Bissau</uri> <language>en</language> </stemVariable> <answerVariable> <label>Bissau</label> <uri>http://dbpedia.org/resource/Bissau</uri> <language>en</language> </answerVariable> </item> <item> <stemVariable> <label>Tanzania</label> <uri>http://dbpedia.org/resource/Tanzania</uri> <language>en</language> </stemVariable> <answerVariable> <label>Dodoma</label> <uri>http://dbpedia.org/resource/Dodoma</uri> <language>en</language> </answerVariable> </item> <item> <stemVariable> <label>Senegal</label> <uri>http://dbpedia.org/resource/Senegal</uri> <language>en</language> </stemVariable> <answerVariable> <label>Dakar</label> <uri>http://dbpedia.org/resource/Dakar</uri><language>en</language></answerVariable></item><item><stemVariable><label>Pakistan</label><uri>http://dbpedia.org/resource/Pakistan</uri><language>en</language></stemVariable><answerVariable><label>Islamabad</label><uri>http://dbpedia.org/resource/Islamabad</uri><language>en</language></answerVariable></item><item><stemVariable><label>Zimbabwe</label><uri>http://dbpedia.org/resource/Zimbabwe</uri><language>en</language></stemVariable><answerVariable><label>Harare</label><uri>http://dbpedia.org/resource/Harare</uri><language>en</language></answerVariable></item><item><stemVariable><label>Barbados</label><uri>http://dbpedia.org/resource/Barbados</uri><language>en</language></stemVariable><answerVariable><label>Bridgetown</label><uri>http://dbpedia.org/resource/Bridgetown</uri><language>en</language></answerVariable></item><item><stemVariable><label>Madagascar</label><uri>http://dbpedia.org/resource/Madagascar</uri><language>en</language></stemVariable><answerVariable><label>Antananarivo</label><uri>http://dbpedia.org/resource/Antananarivo</uri><language>en</language></answerVariable></item><item><stemVariable><label>Venezuela</label><uri>http://dbpedia.org/resource/Venezuela</uri><language>en</language></stemVariable><answerVariable><label>Caracas</label><uri>http://dbpedia.org/resource/Caracas</uri><language>en</language></answerVariable></item><item><stemVariable><label>Nigeria</label><uri>http://dbpedia.org/resource/Nigeria</uri><language>en</language></stemVariable><answerVariable><label>Abuja</label><uri>http://dbpedia.org/resource/Abuja</uri><language>en</language></answerVariable></item><item><stemVariable><label>Thailand</label><uri>http://dbpedia.org/resource/Thailand</uri><language>en</language></stemVariable><answerVariable><label>Bangkok</label><uri>http://dbpedia.org/resource/Bangkok</uri><language>en</language></answerVariable></item><item><stemVariable><label>Botswana</label><uri>http://dbpedia.org/resource/Botswana</uri><language>en</language></stemVariable><answerVariable><label>Gaborone</label><uri>http://dbpedia.org/resource/Gaborone</uri><language>en</language></answerVariable></item><item><stemVariable><label>Saint Lucia</label><uri>http://dbpedia.org/resource/Saint_Lucia</uri><language>en</language></stemVariable><answerVariable><label>Castries</label><uri>http://dbpedia.org/resource/Castries</uri><language>en</language></answerVariable></item><item><stemVariable><label>Rwanda</label><uri>http://dbpedia.org/resource/Rwanda</uri><language>en</language></stemVariable><answerVariable><label>Kigali</label><uri>http://dbpedia.org/resource/Kigali</uri><language>en</language></answerVariable></item><item><stemVariable><label>Brunei</label><uri>http://dbpedia.org/resource/Brunei</uri><language>en</language></stemVariable><answerVariable><label>Bandar Seri Begawan</label><uri>http://dbpedia.org/resource/Bandar_Seri_Begawan</uri><language>en</language></answerVariable></item><item><stemVariable><label>Gabon</label><uri>http://dbpedia.org/resource/Gabon</uri>language>en</language></stemVariable><answerVariable><label>Libreville</label><uri>http://dbpedia.org/resource/Libreville</uri><language>en</language></answerVariable></item><item><stemVariable><label>Serbia</label><uri>http://dbpedia.org/resource/Serbia</uri><language>en</language></stemVariable><answerVariable><label>Belgrade</label><uri>http://dbpedia.org/resource/Belgrade</uri><language>en</language></answerVariable></item><item><stemVariable><label>Australia</label><uri>http://dbpedia.org/resource/Australia</uri><language>en</language></stemVariable><answerVariable><label>Canberra</label><uri>http://dbpedia.org/resource/Canberra</uri><language>en</language></answerVariable></item><item><stemVariable><label>Central African Republic</label><uri>http://dbpedia.org/resource/Central_African_Republic</uri><language>en</language></stemVariable><answerVariable><label>Bangui</label><uri>http://dbpedia.org/resource/Bangui</uri><language>en</language></answerVariable></item><item><stemVariable><label>Democratic Republic of the Congo</label><uri>http://dbpedia.org/resource/Democratic_Republic_of_the_Congo</uri><language>en</language></stemVariable><answerVariable><label>Kinshasa</label><uri>http://dbpedia.org/resource/Kinshasa</uri><language>en</language></answerVariable></item><item><stemVariable><label>Malawi</label><uri>http://dbpedia.org/resource/Malawi</uri><language>en</language></stemVariable><answerVariable><label>Lilongwe</label><uri>http://dbpedia.org/resource/Lilongwe</uri><language>en</language></answerVariable></item><item><stemVariable><label>Moldova</label><uri>http://dbpedia.org/resource/Moldova</uri><language>en</language></stemVariable><answerVariable><label>Chișinău</label><uri>http://dbpedia.org/resource/Chi%C8%99in%C4%83u</uri><language>en</language></answerVariable></item><item><stemVariable><label>Paraguay</label><uri>http://dbpedia.org/resource/Paraguay</uri><language>en</language></stemVariable><answerVariable><label>Asunción</label><uri>http://dbpedia.org/resource/Asunci%C3%B3n</uri><language>en</language></answerVariable></item><item><stemVariable><label>Sierra Leone</label><uri>http://dbpedia.org/resource/Sierra_Leone</uri><language>en</language></stemVariable><answerVariable><label>Freetown</label><uri>http://dbpedia.org/resource/Freetown</uri><language>en</language></answerVariable></item></items>";

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.ClusterService#createClusters(java.lang.String, java.lang.String, int, int)}
     * .
     */
    public void testCreateClustersStringStringIntInt()
    {
        Assert.fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link lu.list.itis.dkd.aig.ClusterService#createClusters(java.io.InputStream, int, String)}.
     *
     * @throws IOException
     */
    public void testCreateClustersStringListOfMatchInt() throws IOException
    {
        final URL url = new URL("http://localhost:8080/SIREN/clusters");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        conn.setRequestMethod("POST");
        conn.setRequestProperty("size", "4");
        conn.setRequestProperty("mode", "BISECTING_KMEANS");
        conn.setRequestProperty("list", ClusterServiceTest.XML);

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        System.out.println(conn.getURL());

        // Send response
        final DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(ClusterServiceTest.XML);
        wr.flush();
        wr.close();

        Assert.fail("Not yet implemented");
    }
}