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
package lu.list.itis.dkd.assess.opennlp.syllibification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Younes Djaghloul [younes.djaghloul@list.lu]
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.1
 */
public class FrenchSyllabification {
	protected static final String DB_LOGIN = "TextComplexity";
	protected static final String DB_PWD = "TextComplexity";
	protected static final String DB_URL = "jdbc:mariadb://aig.list.lu:3306/TextComplexity";

	private static Connection connection;
	private static PreparedStatement preparedStatement;
	protected static final Logger logger = Logger.getLogger(FrenchSyllabification.class.getName());

	static {
		try {
			Class.forName("org.mariadb.jdbc.Driver").newInstance();
			connect();
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.log(Level.SEVERE, "MySQL connection failed", e);
		}
	}

	private static void connect() throws SQLException {
		logger.log(Level.INFO, "Try to connect to" + DB_URL);

		connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PWD);
		preparedStatement = connection.prepareStatement("select * from lexique3lite where token like ? LIMIT 1",
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

	}

	private static void checkConnection() throws SQLException {
		if (!preparedStatement.getConnection().isValid(1)) {
			logger.log(Level.WARNING, "MySQL connection was invalid");
			connect();
		}
	}

	static int getNumberOfSyllabes(String word) {
		try {
			checkConnection();
			preparedStatement.setString(1, word);
			ResultSet result = preparedStatement.executeQuery();
			int res = (result.first()) ? result.getInt("syllabe") : 1;
			result.close();
			return res;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "MySql connection could not be established!", e);
			e.printStackTrace();
			return 1;
		}
	}
}
