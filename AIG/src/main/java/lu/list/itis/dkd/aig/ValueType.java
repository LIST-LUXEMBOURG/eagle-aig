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

/**
 * Enumeration containing all valid types that values can have.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.8
 * @version 0.8.0
 */
public enum ValueType {
    /** The type given to a value that will contain a textual representation. */
    TEXT,
    /** The type given to a value that will contain a numeric representation. */
    NUMBER,
    /** The type given to a value that will contain a semantic representation. */
    URI,
    /** The type given to a value that will contain media such as an image or video. */
    MEDIA,
    /**
     * The type given to a value that will contain a generated, unique, code that is used for
     * identification and not used in any representation to the user.
     */
    CODE;
}