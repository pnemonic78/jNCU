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
package net.sf.jncu.newton;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Newton system error.
 *
 * @author moshew
 */
public class NewtonError extends Exception {

    private static final Map<Integer, String> errors = new TreeMap<Integer, String>();

    private int errorCode;

    /**
     * Creates a new error.
     */
    protected NewtonError() {
        super();
        initErrors();
    }

    /**
     * Creates a new error.
     *
     * @param errorCode the error code.
     */
    public NewtonError(int errorCode) {
        this();
        this.errorCode = errorCode;
    }

    /**
     * Creates a new error.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public NewtonError(int errorCode, Throwable cause) {
        super(cause);
        initErrors();
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errors.get(errorCode);
    }

    /**
     * Initialise the errors.
     */
    private void initErrors() {
        synchronized (errors) {
            if (errors.isEmpty()) {
                populateErrors(errors);
            }
        }
    }

    /**
     * Populate the errors.
     *
     * @param errors the list of errors.
     */
    protected void populateErrors(Map<Integer, String> errors) {
        ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName());
        Enumeration<String> enu = bundle.getKeys();
        String key;
        String value;
        Integer errId;
        while (enu.hasMoreElements()) {
            key = enu.nextElement();
            value = bundle.getString(key);
            errId = Integer.valueOf(key);
            errors.put(errId, value);
        }
    }

    /**
     * Get the error code.
     *
     * @return the error code.
     */
    public int getErrorCode() {
        return errorCode;
    }
}
