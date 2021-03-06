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
package net.sf.jncu.protocol.app;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDPipeListener;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.CommTrace;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketLayer;
import net.sf.jncu.cdil.mnp.MNPPacketListener;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.cdil.mnp.PacketLogger;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.app.DPackageIDList;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DGetSoupIDs;
import net.sf.jncu.protocol.v1_0.data.DReturnEntry;
import net.sf.jncu.protocol.v1_0.data.DSetCurrentSoup;
import net.sf.jncu.protocol.v1_0.data.DSoupIDs;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.io.DSetCurrentStore;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v1_0.sync.DCurrentTime;
import net.sf.jncu.protocol.v1_0.sync.DLastSyncTime;
import net.sf.jncu.protocol.v2_0.DUnknownCommand;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.IconModule.IconModuleListener;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.protocol.v2_0.app.DAppNames;
import net.sf.jncu.protocol.v2_0.app.DGetAppNames;
import net.sf.jncu.protocol.v2_0.app.DPackageInfo;
import net.sf.jncu.protocol.v2_0.app.PackageInfo;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;

import java.util.List;

/**
 * Test to backup from the Newton.
 *
 * @author moshew
 */
public class PackagesTester implements IconModuleListener, MNPPacketListener, DockCommandListener, CDPipeListener<MNPPacket, MNPPacketLayer> {

    private String portName;
    private boolean running = false;
    private CDLayer layer;
    private MNPPipe pipe;
    private PacketLogger logger;
    private Integer result;
    private List<Store> stores;
    private Store store;
    private List<Soup> soups;
    private Soup soup;
    private List<AppName> names;
    private List<PackageInfo> pkgInfo;
    private Integer time;
    private List<Integer> soupIds;

    public PackagesTester() {
        super();
    }

    /**
     * Main method.
     *
     * @param args the array of arguments.
     */
    public static void main(String[] args) throws Exception {
        String[] argv = {"/dev/ttyUSB0", "/tmp/COM8", "38400", "./trace/trace.txt"};
        CommTrace.main(argv);

        PackagesTester tester = new PackagesTester();
        if (args.length < 1) {
            System.out.println("args: port");
            System.exit(1);
            return;
        }
        tester.setPortName(args[0]);
        // tester.setPath(args[1]);

        try {
            try {
                tester.init();
                tester.run();
            } finally {
                tester.done();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public void init() throws Exception {
        logger = new PacketLogger('P');
        layer = CDLayer.getInstance();
        // Initialize the library
        layer.startUp();
        // Create a connection object
        pipe = layer.createMNPSerial(portName, MNPSerialPort.BAUD_38400);
        pipe.addPacketListener(this);
        pipe.addCommandListener(this);
        pipe.setTimeout(30);
        pipe.startListening();
        // Wait for a connect request
        while (layer.getState() == CDState.LISTENING) {
            Thread.yield();
        }
        pipe.accept();
    }

    public void run() throws Exception {
        running = true;

        // result = null;
        // DRequestToSync dRequestToSync = new DRequestToSync();
        // pipe.write(dRequestToSync);
        // Thread.sleep(1000);
        // while (running && (result == null))
        // Thread.yield();
        //
        // result = null;
        // sync = null;
        // DGetSyncOptions dGetSyncOptions = new DGetSyncOptions();
        // pipe.write(dGetSyncOptions);
        // Thread.sleep(1000);
        // while (running && (sync == null))
        // Thread.yield();

        result = null;
        store = null;
        DGetStoreNames dGetStoreNames = new DGetStoreNames();
        pipe.write(dGetStoreNames);
        Thread.sleep(1000);
        while (running && (store == null))
            Thread.yield();

        result = null;
        DSetCurrentStore dSetCurrentStore = new DSetCurrentStore();
        dSetCurrentStore.setStore(store);
        pipe.write(dSetCurrentStore);
        Thread.sleep(1000);
        while (running && (result == null))
            Thread.yield();

        result = null;
        time = null;
        DLastSyncTime dLastSyncTime = new DLastSyncTime();
        pipe.write(dLastSyncTime);
        Thread.sleep(1000);
        while (running && (time == null))
            Thread.yield();

        // DSetCurrentSoup dSetCurrentSoup = new DSetCurrentSoup();
        // dSetCurrentSoup.setName("Calls");
        // pipe.write(dSetCurrentSoup);
        // Thread.sleep(1000);
        // DGetSoupIDs dGetSoupIDs = new DGetSoupIDs();
        // pipe.write(dGetSoupIDs);
        // Thread.sleep(1000);

        // DONE
        result = null;
        names = null;
        soup = null;
        DGetAppNames dGetAppNames = new DGetAppNames();
        dGetAppNames.setWhat(DGetAppNames.CURRENT_STORE_NAMES_SOUPS);
        pipe.write(dGetAppNames);
        Thread.sleep(1000);
        while (running && (result == null) && (soup == null))
            Thread.yield();

        result = null;
        DSetCurrentSoup dSetCurrentSoup = new DSetCurrentSoup();
        dSetCurrentSoup.setSoup(soup);
        pipe.write(dSetCurrentSoup);
        Thread.sleep(1000);
        while (running && (result == null))
            Thread.yield();

        result = null;
        soupIds = null;
        DGetSoupIDs dGetSoupIDs = new DGetSoupIDs();
        pipe.write(dGetSoupIDs);
        Thread.sleep(1000);
        while (running && (result == null) && (soupIds == null))
            Thread.yield();

        for (int id : soupIds) {
            DReturnEntry dReturnEntry = new DReturnEntry();
            dReturnEntry.setId(id);
            pipe.write(dReturnEntry);
        }
        Thread.sleep(1000);
        while (running && (soup.getEntries().size() < soupIds.size()))
            Thread.yield();

        // DONE
        // result = null;
        // pkgInfo = null;
        // DGetPackageInfo dGetPackageInfo = new DGetPackageInfo();
        // dGetPackageInfo.setTitle("newtWorks");
        // pipe.write(dGetPackageInfo);
        // Thread.sleep(1000);
        // while (running && (result == null) && (pkgInfo == null))
        // Thread.yield();

        // UNKNOWN
        // DGetPackageIDs dGetPackageIDs = new DGetPackageIDs();
        // pipe.write(dGetPackageIDs);
        // Thread.sleep(1000);
        // // while (running && (result == null))
        // // Thread.yield();

        // UNKNOWN
        // result = null;
        // DBackupPackages dBackupPackages = new DBackupPackages();
        // pipe.write(dBackupPackages);
        // Thread.sleep(1000);
        // // while (running && (result == null))
        // // Thread.yield();

        // TODO
        // DGetPatches dGetPatches = new DGetPatches();
        // pipe.write(dGetPatches);
        // Thread.sleep(1000);
        // // while (running && (result == null) && (names == null))
        // // Thread.yield();
        // // }

        // TODO
        // DGetPackageIDs dGetPackageIDs = new DGetPackageIDs();
        // pipe.write(dGetPackageIDs);
        // Thread.sleep(1000);
        // while (running && (result == null) && (names == null))
        // Thread.yield();

        Thread.sleep(10000);
        exit();

        while (running)
            Thread.yield();
    }

    private void exit() throws Exception {
        running = false;

        if (pipe.isConnected()) {
            DOperationDone dOperationDone = new DOperationDone();
            pipe.write(dOperationDone);
            Thread.sleep(2000);
        }
        if (pipe.isConnected()) {
            DOperationCanceled dOperationCanceled = new DOperationCanceled();
            pipe.write(dOperationCanceled);
            Thread.sleep(2000);
        }
        if (pipe.isConnected()) {
            DDisconnect dDisconnect = new DDisconnect();
            pipe.write(dDisconnect);
            Thread.sleep(2000);
        }
    }

    private void done() throws Exception {
        if (pipe != null) {
            pipe.disconnect(); // Break the connection.
            pipe.dispose(); // Delete the pipe object
        }
        if (layer != null) {
            layer.shutDown(); // Close the library
        }
    }

    @Override
    public void successModule(IconModule module) {
        System.out.println("successModule");
        running = false;
    }

    @Override
    public void cancelModule(IconModule module) {
        System.out.println("cancelModule");
        running = false;
    }

    @Override
    public void packetAcknowledged(MNPPacket packet) {
        logger.log("A", packet, pipe.getDockingState());
    }

    @Override
    public void packetEOF() {
        logger.log("E", null, pipe.getDockingState());
    }

    @Override
    public void packetReceived(MNPPacket packet) {
        logger.log("R", packet, pipe.getDockingState());
    }

    @Override
    public void packetSending(MNPPacket packet) {
        logger.log("s", packet, pipe.getDockingState());
    }

    @Override
    public void packetSent(MNPPacket packet) {
        logger.log("S", packet, pipe.getDockingState());
    }

    @Override
    public void commandReceiving(DockCommandFromNewton command, int progress, int total) {
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        System.out.println("rcv: " + command);
        final String cmd = command.getCommand();

        if (DDisconnect.COMMAND.equals(cmd)) {
            running = false;
        } else if (DOperationCanceled.COMMAND.equals(cmd)) {
            running = false;
        } else if (DOperationCanceled2.COMMAND.equals(cmd)) {
            running = false;
        } else if (net.sf.jncu.protocol.v1_0.session.DOperationCanceled.COMMAND.equals(cmd)) {
            running = false;
        } else if (DResult.COMMAND.equals(cmd)) {
            DResult dResult = (DResult) command;
            int r = dResult.getErrorCode();
            if (r != DResult.OK) {
                try {
                    exit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            result = r;
        } else if (DStoreNames.COMMAND.equals(cmd)) {
            DStoreNames dStoreNames = (DStoreNames) command;
            stores = dStoreNames.getStores();
            store = dStoreNames.getDefaultStore();
        } else if (DAppNames.COMMAND.equals(cmd)) {
            DAppNames dAppNames = (DAppNames) command;
            names = dAppNames.getNames();
            for (AppName name : names) {
                if (name.hasPackages()) {
                    soup = new Soup(((NSOFString) name.getSoups().get(0)).getValue());
                    break;
                }
            }
        } else if (DPackageInfo.COMMAND.equals(cmd)) {
            DPackageInfo dPackageInfo = (DPackageInfo) command;
            pkgInfo = dPackageInfo.getPackages();
            if (!pkgInfo.isEmpty())
                System.out.println();
        } else if (DPackageIDList.COMMAND.equals(cmd)) {
            DPackageIDList dPackageIDList = (DPackageIDList) command;
            pkgInfo = dPackageIDList.getPackages();
            if (!pkgInfo.isEmpty())
                System.out.println();
        } else if (DCurrentTime.COMMAND.equals(cmd)) {
            DCurrentTime dCurrentTime = (DCurrentTime) command;
            time = dCurrentTime.getTime();
        } else if (DUnknownCommand.COMMAND.equals(cmd)) {
            try {
                exit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (DSoupNames.COMMAND.equals(cmd)) {
            DSoupNames dSoupNames = (DSoupNames) command;
            soups = dSoupNames.getSoups();
        } else if (DSoupIDs.COMMAND.equals(cmd)) {
            DSoupIDs dSoupIDs = (DSoupIDs) command;
            soupIds = dSoupIDs.getIDs();
        } else if (DEntry.COMMAND.equals(cmd)) {
            DEntry dEntry = (DEntry) command;
            SoupEntry entry = dEntry.getEntry();
            soup.addEntry(entry);
        }
    }

    @Override
    public void commandSending(DockCommandToNewton command, int progress, int total) {
    }

    @Override
    public void commandSent(DockCommandToNewton command) {
        System.out.println("snt: " + command);
        final String cmd = command.getCommand();

        if (DDisconnect.COMMAND.equals(cmd)) {
            running = false;
        }
    }

    @Override
    public void commandEOF() {
    }

    @Override
    public void pipeDisconnected(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
    }

    @Override
    public void pipeDisconnectFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
        System.exit(100);
    }

    @Override
    public void pipeConnectionListening(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
    }

    @Override
    public void pipeConnectionListenFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
        System.exit(101);
    }

    @Override
    public void pipeConnectionPending(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
    }

    @Override
    public void pipeConnectionPendingFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
        System.exit(102);
    }

    @Override
    public void pipeConnected(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
    }

    @Override
    public void pipeConnectionFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
        System.exit(103);
    }

}
