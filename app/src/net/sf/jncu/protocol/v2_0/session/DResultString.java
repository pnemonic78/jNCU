/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Reports a desktop error to the Newton. The string is included since the
 * Newton doesn't know how to decode all the desktop errors (especially since
 * the Macintosh and Windows errors are different). <tt>ErrorString</tt> is a
 * ref.
 *
 * <pre>
 * 'ress'
 * length
 * errorNumber
 * errorStringRef
 * </pre>
 *
 * @author moshew
 */
public class DResultString extends BaseDockCommandToNewton {

    /**
     * <tt>kDResultString</tt>
     */
    public static final String COMMAND = "ress";

    private int errorNumber;
    private String errorString;

    /**
     * Creates a new command.
     */
    public DResultString() {
        super(COMMAND);
    }

    /**
     * Get the error number.
     *
     * @return the error.
     */
    public int getErrorNumber() {
        return errorNumber;
    }

    /**
     * Set the error number.
     *
     * @param errorNumber the error.
     */
    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    /**
     * Get the error string.
     *
     * @return the error.
     */
    public String getErrorString() {
        return errorString;
    }

    /**
     * Set the error string.
     *
     * @param errorString the error.
     */
    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getErrorNumber(), data);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(new NSOFString(getErrorString()), data);
    }
}
