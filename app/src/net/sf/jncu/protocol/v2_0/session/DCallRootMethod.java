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
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This command asks the Newton to call the specified root method. The return
 * value from the method is sent to the desktop with a <tt>kDCallResult</tt>
 * command.
 *
 * <pre>
 * 'crmd' ('crmf')
 * length
 * method name symbol
 * args array
 * </pre>
 *
 * @author moshew
 */
public class DCallRootMethod extends BaseDockCommandToNewton {

    /**
     * <tt>kDCallRootMethod</tt>
     */
    public static final String COMMAND = "crmf";

    private String methodName;
    private Object[] args;

    /**
     * Creates a new command.
     */
    public DCallRootMethod() {
        super(COMMAND);
    }

    /**
     * Get the method name.
     *
     * @return the method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Set the method name.
     *
     * @param methodName the method name.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Get the array of arguments.
     *
     * @return the arguments.
     */
    public Object[] getArguments() {
        return args;
    }

    /**
     * Set the array of arguments.
     *
     * @param args the arguments.
     */
    public void setArguments(Object[] args) {
        this.args = args;
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        writeString(getMethodName(), data);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(NSOFEncoder.toNS(getArguments()), data);
    }
}
