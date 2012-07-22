package net.sf.jncu.protocol.sync;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jncu.cdil.mnp.TraceDecode;
import net.sf.jncu.data.Archive;
import net.sf.jncu.data.ArchiveReader;
import net.sf.jncu.data.ArchiveWriter;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFLargeBinary;
import net.sf.jncu.fdil.NSOFNil;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.fdil.contrib.NSOFIcon;
import net.sf.jncu.fdil.contrib.NSOFIconPro;
import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.app.DPackageIDList;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.protocol.v2_0.app.DAppNames;
import net.sf.jncu.protocol.v2_0.app.PackageInfo;
import net.sf.jncu.protocol.v2_0.session.DNewtonName;
import net.sf.jncu.protocol.v2_0.sync.DBackupSoupDone;

import org.junit.Assert;

public class BackupDecode extends TraceDecode implements DockCommandListener {

	private Store store;
	private List<Soup> soups;
	private Soup soup;
	private Archive archive;
	private boolean done;

	public BackupDecode() throws IOException {
		this.archive = new Archive();
	}

	@Override
	protected DecodePayload createDecodePayload(InputStream receivedFromNewton, InputStream sentToNewton) throws Exception {
		DecodePayload dp = super.createDecodePayload(receivedFromNewton, sentToNewton);
		dp.getPipe().addCommandListener(this);
		return dp;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		File f = new File(args[0]);
		BackupDecode decoder;
		try {
			if ((args.length > 1) && (args[1].charAt(0) == 'r')) {
				unzip(args);
			} else {
				decoder = new BackupDecode();
				decoder.setFile(f);
				decoder.run();
			}
			if (!Boolean.getBoolean("debug")) {
				Thread.sleep(1000L);
				System.exit(0);// Kill all threads.
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected static void unzip(String[] args) throws Exception {
		String dir = System.getProperty("user.home");
		dir = dir + File.separator + "jNCU/Backups";
		File f = new File(dir, args[0]);
		ArchiveReader reader;
		Archive archive = null;
		reader = new ArchiveReader(f);
		archive = reader.read();
		System.out.println("archive read from " + f);
		System.out.println("archive=" + archive);
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		final String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
			DResult dResult = (DResult) command;
			int r = dResult.getErrorCode();
			if (r != 0) {
				System.out.println("result=" + r);
			}
		} else if (DNewtonName.COMMAND.equals(cmd)) {
			DNewtonName dNewtonName = (DNewtonName) command;
			archive.setDeviceInfo(dNewtonName.getInformation());
		} else if (DStoreNames.COMMAND.equals(cmd)) {
			DStoreNames dStoreNames = (DStoreNames) command;
			store = dStoreNames.getDefaultStore();
			archive.setStores(dStoreNames.getStores());
		} else if (DSoupNames.COMMAND.equals(cmd)) {
			DSoupNames dSoupNames = (DSoupNames) command;
			soups = dSoupNames.getSoups();
			soup = soups.get(0);
			store.setSoups(soups);
		} else if (DSoupInfo.COMMAND.equals(cmd)) {
			DSoupInfo dSoupInfo = (DSoupInfo) command;
			soup = dSoupInfo.getSoup();
			Soup soupFound = store.findSoup(soup.getName(), soup.getSignature());
			if (soupFound != null) {
				soupFound.getInformation().putAll(soup.getInformation());
			}
		} else if (DEntry.COMMAND.equals(cmd)) {
			DEntry dEntry = (DEntry) command;
			SoupEntry entry = dEntry.getEntry();
			if (!"Packages".equals(soup.getName()))
				soup.addEntry(entry);

			NSOFObject pkgRef = entry.get("pkgRef");
			if (pkgRef != NSOFNil.NIL) {
				NSOFLargeBinary bin = (NSOFLargeBinary) pkgRef;
				System.out.println(bin + " len=" + bin.getValue().length);
				// String companderName = bin.isCompressed() ?
				// bin.getCompanderName() : "pkg";
				// byte[] ca = bin.getCompanderArguments();
				try {
					String name = ((NSOFString) entry.get("packageName")).getValue();
					ApplicationPackage pkg = new ApplicationPackage(name);
					pkg.setBinary(bin);
					store.getPackages().add(pkg);

					// FileOutputStream fout = new FileOutputStream(new
					// File("Packages/" + name + "(" + entry.getId() + ")." +
					// companderName));
					// fout.write(bin.getValue());
					// fout.close();
					//
					// if (ca != null) {
					// fout = new FileOutputStream(new File("Packages/" + name +
					// "(" + entry.getId() + ")." + "args"));
					// fout.write(ca);
					// fout.close();
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			NSOFObject iconSlot = entry.get("icon");
			if ((iconSlot != NSOFNil.NIL) && (iconSlot instanceof NSOFFrame)) {
				NSOFSymbol oc = iconSlot.getObjectClass();
				Assert.assertNotNull(oc);
				NSOFIcon icon = new NSOFIcon();
				icon.putAll((NSOFFrame) iconSlot);
				NSOFObject bounds = icon.getBounds();
				Assert.assertNotNull(bounds);
				NSOFObject bits = icon.getBits();
				Assert.assertNotNull(bits);
				// NSOFObject mask = icon.getMask();
				// Assert.assertNotNull(mask);
			}
			iconSlot = entry.get("iconPro");
			if ((iconSlot != NSOFNil.NIL) && (iconSlot instanceof NSOFFrame)) {
				NSOFSymbol oc = iconSlot.getObjectClass();
				Assert.assertNotNull(oc);
				NSOFIconPro icon = new NSOFIconPro();
				icon.putAll((NSOFFrame) iconSlot);
				NSOFObject unhilited = icon.getUnhilited();
				Assert.assertNotNull(unhilited);
				NSOFObject hilited = icon.getHilited();
				Assert.assertNotNull(hilited);
			}
		} else if (DBackupSoupDone.COMMAND.equals(cmd)) {
			System.out.println();
		} else if (DAppNames.COMMAND.equals(cmd)) {
			DAppNames dAppNames = (DAppNames) command;
			List<AppName> names = dAppNames.getNames();
			for (AppName name : names) {
				if (name.hasPackages()) {
					soup = new Soup(((NSOFString) name.getSoups().get(0)).getValue());
					break;
				}
			}
		} else if (DPackageIDList.COMMAND.equals(cmd)) {
			DPackageIDList dPackageIDList = (DPackageIDList) command;
			List<PackageInfo> pkgInfo = dPackageIDList.getPackages();
			if (!pkgInfo.isEmpty())
				System.out.println();
		}
	}

	@Override
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
	}

	@Override
	public void commandEOF() {
		if (done)
			return;
		String dir = System.getProperty("user.dir");
		File f = new File(dir, "Backups/backup.zip");
		ArchiveWriter writer = new ArchiveWriter(f);
		try {
			writer.write(archive);
			done = true;
			System.out.println("archive saved to " + f);
			if (!Boolean.getBoolean("debug")) {
				System.exit(0);// Kill all threads.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
