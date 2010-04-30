package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDNewtonName</tt><br>
 * This command is sent in response to a correct <tt>kDInitiateDocking</tt>
 * command from the docker. The Newton's name is used to locate the proper
 * synchronise file. The version info includes things like machine type (e.g.
 * J1), ROM version, etc. Here's the full list of what the version info includes
 * (all are <code>long</code>s):
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
 * string including the terminating zeros at the end. The string is padded to an
 * even 4 bytes by adding zeros as necessary (the padding bytes are not included
 * in the length sent as part of the command header).
 * 
 * <pre>
 * 'name'
 * length
 * version info
 * name
 * </pre>
 */
public class DCmdNewtonName extends DockCommandFromNewton {

	public static final String COMMAND = "name";

	private String name;
	private NewtonInfo info;

	/**
	 * Creates a new command.
	 */
	public DCmdNewtonName() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		int versionInfoLength = htonl(data);
		info = new NewtonInfo();
		info.newtonId = htonl(data);
		info.manufacturerId = htonl(data);
		info.machineType = htonl(data);
		info.romVersion = htonl(data);
		info.romStage = htonl(data);
		info.ramSize = htonl(data);
		info.screenHeight = htonl(data);
		info.screenWidth = htonl(data);
		info.patchVersion = htonl(data);
		info.objectSystemVersion = htonl(data);
		info.internalStoreSignature = htonl(data);
		info.screenResolutionVertical = htonl(data);
		info.screenResolutionHorizontal = htonl(data);
		info.screenDepth = htonl(data);

		final int versionSize = 14 * LENGTH_WORD;
		if (versionInfoLength > versionSize) {
			info.systemFlags = htonl(data);

			if ((info.systemFlags & 0x1) == 0x1) {
				long serHi = htonl(data) & 0xFFFFFFFFL;
				long serLo = htonl(data) & 0xFFFFFFFFL;
				info.serialNumber = (serHi << 32) | serLo;
			}
			if ((info.systemFlags & 0x2) == 0x2) {
				info.targetProtocol = htonl(data);
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
