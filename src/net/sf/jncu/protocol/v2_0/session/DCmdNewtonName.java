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

	/**
	 * <tt>kDNewtonName</tt><br>
	 * This command is sent in response to a correct <tt>kDInitiateDocking</tt>
	 * command from the docker. The Newton's name is used to locate the proper
	 * synchronise file. The version info includes things like machine type
	 * (e.g. J1), ROM version, etc. Here's the full list of what the version
	 * info includes (all are <code>long</code>s):
	 * <ol>
	 * <li>length of version info in bytes
	 * <li>NewtonUniqueID - a number uniquely identifying the Newton
	 * <li>manufacturer id
	 * <li>machine type
	 * <li>ROM version
	 * <li>ROM stage
	 * <li>RAM size
	 * <li>screen height
	 * <li>screen width
	 * <li>system update version
	 * <li>Newton object system version
	 * <li>signature of internal store
	 * <li>vertical screen resolution
	 * <li>horizontal screen resolution
	 * <li>screen depth
	 * </ol>
	 * The version info is followed by the name of the Newton sent as a Unicode
	 * string including the terminating zeros at the end. The string is padded
	 * to an even 4 bytes by adding zeros as necessary (the padding bytes are
	 * not included in the length sent as part of the command header).
	 * 
	 * <pre>
	 * 'name'
	 * length
	 * version info
	 * name
	 * </pre>
	 */
	public static final String COMMAND = "name";

	private String name;
	private VersionInfo version;

	/**
	 * Creates a new command.
	 */
	public DCmdNewtonName() {
		super(COMMAND);
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
