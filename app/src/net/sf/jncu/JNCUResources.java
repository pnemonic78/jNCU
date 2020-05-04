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

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

/**
 * jNCU resource bundle.
 *
 * @author Moshe
 */
public class JNCUResources {

    private static final String BUNDLE = "net.sf.jncu.jncu";

    private static ResourceBundle bundle;

    private JNCUResources() {
        // Utility class, hide constructor.
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its
     * parents.
     *
     * @param key          the key name.
     * @param defaultValue the default value if the property is not found.
     * @return the string for the given key.
     */
    public static String getString(String key, String defaultValue) {
        if (key == null || key.isEmpty())
            return defaultValue;
        if (bundle == null)
            bundle = ResourceBundle.getBundle(BUNDLE);
        if (!bundle.containsKey(key))
            return defaultValue;
        String value = bundle.getString(key);
        if ("@null".equals(value))
            return null;
        if (value.startsWith("@AWT.")) {
            key = value.substring(1);
            return Toolkit.getProperty(key, defaultValue);
        }
        if (value.startsWith("@OptionPane.")) {
            key = value.substring(1);
            return UIManager.getString(key);
        }
        return value;
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its
     * parents.
     *
     * @param key the key name.
     * @return the string for the given key - {@code null} otherwise.
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * Gets a character for the given key from this resource bundle or one of
     * its parents.
     *
     * @param key          the key name.
     * @param defaultValue the default value if the property is not found.
     * @return the character for the given key.
     */
    public static int getChar(String key, int defaultValue) {
        String value = getString(key);
        if (value == null)
            return defaultValue;
        if (value.length() > 0)
            return value.codePointAt(0);
        return defaultValue;
    }

    /**
     * Gets a character for the given key from this resource bundle or one of
     * its parents.
     *
     * @param key          the key name.
     * @param defaultValue the default value if the property is not found.
     * @return the character for the given key.
     */
    public static int getChar(String key, char defaultValue) {
        return getChar(key, (int) defaultValue);
    }

    /**
     * Get an icon.
     *
     * @param path the icon path.
     * @return the icon.
     */
    public static ImageIcon getIcon(String path) {
        URL url = JNCUResources.class.getResource(path);
        return new ImageIcon(url);
    }

}
