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
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.v2_0.app.PackageInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This command sends a list of package frames to the desktop. For each package
 * the information sent is this:
 * <ol>
 * <li>ULong packageSize;
 * <li>ULong packageId;
 * <li>ULong packageVersion;
 * <li>ULong format;
 * <li>ULong deviceKind;
 * <li>ULong deviceNumber;
 * <li>ULong deviceId;
 * <li>ULong modifyDate;
 * <li>ULong isCopyProtected;
 * <li>ULong length;
 * <li>Character name; (length bytes of Unicode string)
 * </ol>
 * Note that this is not sent as an array! It's sent as binary data. Note that
 * this finds packages only in the current store.
 *
 * <pre>
 * 'pids'
 * length
 * count
 * package info
 * </pre>
 */
public class DPackageIDList extends BaseDockCommandFromNewton {

    /**
     * <tt>kDPackageIDList</tt>
     */
    public static final String COMMAND = "pids";

    private final List<PackageInfo> packages = new ArrayList<PackageInfo>();

    /**
     * Creates a new command.
     */
    public DPackageIDList() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        packages.clear();
        int count = ntohl(data);
        PackageInfo pkg;
        for (int i = 0; i < count; i++) {
            pkg = new PackageInfo();
            pkg.setPackageSize(ntohl(data));
            pkg.setPackageId(ntohl(data));
            pkg.setPackageVersion(ntohl(data));
            pkg.setFormat(ntohl(data));
            pkg.setDeviceKind(ntohl(data));
            pkg.setDeviceNumber(ntohl(data));
            pkg.setDeviceId(ntohl(data));
            pkg.setModifyDate(ntohl(data));
            pkg.setCopyProtected(ntohl(data) != FALSE);
            pkg.setName(readString(data));
            packages.add(pkg);
        }
    }

    /**
     * Get the list of packages.
     *
     * @return the packages.
     */
    public List<PackageInfo> getPackages() {
        return packages;
    }

}
