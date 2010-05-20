package net.sf.jncu.protocol.v1_0;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.NewtonInfo;

/**
 * <tt>kDNewtonName</tt><br>
 * The name of the Newton.<br>
 * This command is sent in response to a correct kDInitiateDocking command from
 * the docker. The Newton's name is used to locate the proper synchronise file.
 * The version info includes things like machine type (e.g. J1), ROM version,
 * etc.
 * 
 * <pre>
 * 'name'
 * length
 * version info
 * name
 * </pre>
 */
public class DNewtonName extends DockCommandFromNewton {

	public static final String COMMAND = "name";

	private String name;
	private NewtonInfo info;

	/**
	 * Creates a new command.
	 */
	public DNewtonName() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		int versionInfoLength = htonl(data);
		info = new NewtonInfo();
		info.setNewtonId(htonl(data)); // #1
		info.setManufacturerId(htonl(data)); // #2
		info.setMachineType(htonl(data)); // #3
		info.setRomVersion(htonl(data)); // #4
		info.setRomStage(htonl(data)); // #5
		info.setRamSize(htonl(data)); // #6
		info.setScreenHeight(htonl(data)); // #7
		info.setScreenWidth(htonl(data)); // #8
		info.setPatchVersion(htonl(data)); // #9
		info.setObjectSystemVersion(htonl(data)); // #10
		info.setInternalStoreSignature(htonl(data)); // #11
		info.setScreenResolutionVertical(htonl(data)); // #12
		info.setScreenResolutionHorizontal(htonl(data)); // #13
		info.setScreenDepth(htonl(data)); // #14

		final int versionSize = 14 * LENGTH_WORD;
		if (versionInfoLength > versionSize) {
			/**
			 * A bit field. The following two bits are defined:<br>
			 * 1 = has serial number <br>
			 * 2 = has target protocol
			 */
			int systemFlags = htonl(data);

			if ((systemFlags & 0x1) == 0x1) {
				long serHi = htonl(data) & 0xFFFFFFFFL;
				long serLo = htonl(data) & 0xFFFFFFFFL;
				info.setSerialNumber((serHi << 32) | serLo);
			}
			if ((systemFlags & 0x2) == 0x2) {
				info.setTargetProtocol(htonl(data));
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
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the Newton information.
	 * 
	 * @return the information.
	 */
	public NewtonInfo getInformation() {
		return info;
	}
}
