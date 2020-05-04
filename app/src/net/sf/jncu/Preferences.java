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
package net.sf.jncu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Application preferences.
 *
 * @author Moshe
 */
public class Preferences {

    /**
     * Properties file folder.
     */
    private static final String FOLDER = "jNCU";
    /**
     * Properties file name.
     */
    private static final String NAME = "jncu.xml";

    private static Preferences instance;
    private final Properties props = new Properties();
    private File file;

    /**
     * Constructs a new preferences.
     */
    protected Preferences() {
        super();
    }

    /**
     * Get the preferences instance. Loads the preferences from a file.
     *
     * @return the preferences.
     */
    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
            instance.load();
        }
        return instance;
    }

    /**
     * Load the properties from storage.
     */
    protected void load() {
        InputStream in = null;
        try {
            in = new FileInputStream(getFile());
            props.loadFromXML(in);
        } catch (FileNotFoundException fnfe) {
            // File does not exist yet.
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Persist the properties to storage.
     */
    public void save() {
        OutputStream out = null;
        try {
            out = new FileOutputStream(getFile());
            props.storeToXML(out, null);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Get the property.
     *
     * @param key the property key.
     * @return the property value - {@code null} otherwise.
     */
    public String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Get the property.
     *
     * @param key          the property key.
     * @param defaultValue the default value if none is found.
     * @return the property value - {@code defaultValue} otherwise.
     */
    public String get(String key, String defaultValue) {
        if (props.containsKey(key))
            return props.getProperty(key);
        return defaultValue;
    }

    /**
     * Get the boolean property.
     *
     * @param key          the property key.
     * @param defaultValue the default value if none is found.
     * @return the property value - {@code defaultValue} otherwise.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (props.containsKey(key))
            return Boolean.parseBoolean(props.getProperty(key));
        return defaultValue;
    }

    /**
     * Set the property.
     *
     * @param key   the property key.
     * @param value the property value.
     */
    public void set(String key, String value) {
        if (value == null) {
            props.remove(key);
            return;
        }
        props.setProperty(key, value);
    }

    /**
     * Set the property.
     *
     * @param key   the property key.
     * @param value the property value.
     */
    public void set(String key, boolean value) {
        set(key, Boolean.toString(value));
    }

    /**
     * Get the properties file.
     *
     * @return the file.
     */
    protected File getFile() {
        if (file == null) {
            File userFolder = new File(System.getProperty("user.home"));
            File jncuFolder = new File(userFolder, FOLDER);
            jncuFolder.mkdirs();
            file = new File(jncuFolder, NAME);
        }
        return file;
    }

    /**
     * Remove the property.
     *
     * @param key the property key.
     */
    public void remove(String key) {
        props.remove(key);
    }
}
