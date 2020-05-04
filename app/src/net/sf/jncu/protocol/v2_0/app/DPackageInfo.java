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
package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This command is sent in response to a <tt>kDGetPackageInfo</tt> command. An
 * array is returned that contains a frame for each package with the specified
 * name (there may be more than one package with the same name but different
 * package id). The returned frame looks like this:<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;name: "The name passed in",<br>
 * &nbsp;&nbsp;packagesize: 123,<br>
 * &nbsp;&nbsp;packageid: 123,<br>
 * &nbsp;&nbsp;packageversion: 1,<br>
 * &nbsp;&nbsp;format: 1,<br>
 * &nbsp;&nbsp;devicekind: 1,<br>
 * &nbsp;&nbsp;devicenumber: 1,<br>
 * &nbsp;&nbsp;deviceid: 1,<br>
 * &nbsp;&nbsp;modtime: 123213213,<br>
 * &nbsp;&nbsp;iscopyprotected: true,<br>
 * &nbsp;&nbsp;length: 123,<br>
 * &nbsp;&nbsp;safetoremove: true<br>
 * }</code>
 *
 * <pre>
 * 'pinf'
 * length
 * info ref
 * </pre>
 *
 * @author Moshe
 */
public class DPackageInfo extends BaseDockCommandFromNewton {

    /**
     * <tt>kDPackageInfo</tt>
     */
    public static final String COMMAND = "pinf";

    private final List<PackageInfo> info = new ArrayList<PackageInfo>();

    /**
     * Creates a new command.
     */
    public DPackageInfo() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        info.clear();

        NSOFDecoder decoder = new NSOFDecoder();
        NSOFObject o = decoder.inflate(data);
        if (!NSOFImmediate.isNil(o)) {
            NSOFArray arr = (NSOFArray) o;
            int size = arr.length();
            PackageInfo pkg;
            for (int i = 0; i < size; i++) {
                pkg = new PackageInfo((NSOFFrame) arr.get(i));
                info.add(pkg);
            }
        }
    }

    /**
     * Get the packages information.
     *
     * @return the information.
     */
    public List<PackageInfo> getPackages() {
        return info;
    }
}
