/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package net.sf.jncu.newton;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Read a NSOF frame.
 *
 * @author Moshe
 */
public class FrameReader {

    public FrameReader() {
        super();
    }

    /**
     * Main method.
     *
     * @param args the array of arguments.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("args: file");
            System.exit(1);
        }

        FrameReader reader = new FrameReader();
        reader.read(args[0]);
    }

    /**
     * Read a frame file.
     *
     * @param path the file path.
     */
    public void read(String path) {
        read(new File(path));
    }

    /**
     * Read a frame file.
     *
     * @param file the file.
     */
    public void read(File file) {
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            NSOFDecoder decoder = new NSOFDecoder();
            NSOFObject o = decoder.inflate(in);
            System.out.println(o);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

}
