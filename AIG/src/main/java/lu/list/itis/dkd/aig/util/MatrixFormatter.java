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

import lu.list.itis.dkd.aig.match.Match;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayTable;

/**
 * Helper class to output the distance matrix in a CSV format.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.0
 * @version 0.8.0
 */
public class MatrixFormatter {

    /** Private constructor of class exposing only static methods! */
    private MatrixFormatter() {}

    /**
     * Method returning the matrix in CSV format.
     *
     * @param matrix
     *        The matrix to format.
     * @return A string representation in CSV format of the matrix.
     */
    public static String format(final ArrayTable<Match, Match, Float> matrix) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Joiner joiner = Joiner.on("; ").useForNull("0"); //$NON-NLS-1$ //$NON-NLS-2$
        final Float[][] values = matrix.toArray(Float.class);

        stringBuilder.append(joiner.join(matrix.columnKeySet()));
        stringBuilder.append("\n"); //$NON-NLS-1$

        for (final Float[] value : values) {
            stringBuilder.append(joiner.join(value));
            stringBuilder.append("\n"); //$NON-NLS-1$
        }

        return stringBuilder.toString();
    }
}