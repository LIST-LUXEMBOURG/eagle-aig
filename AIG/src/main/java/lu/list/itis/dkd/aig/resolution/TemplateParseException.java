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
package lu.list.itis.dkd.aig.resolution;

/**
 * Exception used to wrap exceptions occurring during the template construction, that is, during the
 * parsing of the provided XML document into a template instance.
 *
 * @author Eric Tobias [eric.tobias@list.lu]
 * @since 0.7
 * @version 0.7.2
 */
public class TemplateParseException extends Exception {

    private static final long serialVersionUID = -6524958219847930275L;

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause(Throwable)}.
     *
     * @param message
     *        The detailed message. The detail message is saved for later retrieval by the
     *        {@link #getMessage()} method.
     */
    public TemplateParseException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * <code>(cause==null ? null : cause.toString())</code> (which typically contains the class and
     * detail message of <code>cause</code>). This constructor is useful for exceptions that are
     * little more than wrappers for other throwables (for example,
     * {@link java.security.PrivilegedActionException}).
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the {@link #getCause()} method). (A
     *        <code>null</code> value is permitted, and indicates that the cause is nonexistent or
     *        unknown.)
     */
    public TemplateParseException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i> automatically
     * incorporated in this exception's detail message.
     *
     * @param message
     *        the detail message (which is saved for later retrieval by the {@link #getMessage()}
     *        method).
     * @param cause
     *        the cause (which is saved for later retrieval by the {@link #getCause()} method). (A
     *        <code>null</code> value is permitted, and indicates that the cause is nonexistent or
     *        unknown.)
     */
    public TemplateParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}