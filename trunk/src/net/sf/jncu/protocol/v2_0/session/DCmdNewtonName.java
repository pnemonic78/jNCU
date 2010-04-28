package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Command to detail Newton's name.
 * 
 * @author moshew
 */
public class DCmdNewtonName extends DockCommandFromNewton {

	private String name;
	private VersionInfo version;

	/**
	 * Creates a new command.
	 */
	public DCmdNewtonName() {
		super(DockCommandSession.NewtonToDesktop.kDNewtonName);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		int versionInfoLength = htonl(data);
		version = new VersionInfo();
		version.newtonUniqueId = htonl(data);
		version.manufacturerId = htonl(data);
		version.machineType = htonl(data);
		version.romVersion = htonl(data);
		version.romStage = htonl(data);
		version.ramSize = htonl(data);
		version.screenHeight = htonl(data);
		version.screenWidth = htonl(data);
		version.systemUpdateVersion = htonl(data);
		version.objectSystemVersion = htonl(data);
		version.internalStoreSignature = htonl(data);
		version.screenResolutionVertical = htonl(data);
		version.screenResolutionHorizontal = htonl(data);
		version.screenDepth = htonl(data);

		final int versionSize = 14 * LENGTH_WORD;
		if (versionInfoLength > versionSize) {
			int versionOtherLength = (versionInfoLength - versionSize) / LENGTH_WORD;
			version.other = new int[versionOtherLength];
			for (int i = 0; i < versionOtherLength; i++) {
				version.other[i] = htonl(data);
			}
		}

		int nameLength = data.available();
		byte[] nameBytes = new byte[nameLength];
		data.read(nameBytes);
		try {
			while ((nameLength > 0) && (nameBytes[nameLength - 1] == 0)) {
				nameLength -= 2;
			}
			setName(new String(nameBytes, 0, nameLength, "UTF-16"));
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
	}

	/**
	 * Get the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
