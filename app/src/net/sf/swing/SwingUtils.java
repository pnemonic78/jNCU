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
package net.sf.swing;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

/**
 * Swing utilities.
 *
 * @author Moshe
 */
public class SwingUtils {

    private static boolean initialised;
    private static FileSystemView fileSystemView;

    static {
        init();
    }

    /**
     * Constructs a new object.
     */
    private SwingUtils() {
        super();
    }

    /**
     * Initialise.
     */
    public static void init() {
        if (initialised)
            return;

        // Set the system L&F.
        try {
            String systemLAF = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(systemLAF);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialised = true;
    }

    /**
     * Get the file system view.
     *
     * @return the view.
     */
    public static FileSystemView getFileSystemView() {
        if (fileSystemView == null) {
            fileSystemView = FileSystemView.getFileSystemView();
        }
        return fileSystemView;
    }

    /**
     * Get all root partitions on this system.
     *
     * @return the roots.
     */
    public static File[] getRoots() {
        return getFileSystemView().getRoots();
    }

    /**
     * Post a window closing event to the window.
     *
     * @param window the window to close.
     */
    public static void postWindowClosing(Window window) {
        WindowEvent closingEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
    }
}
