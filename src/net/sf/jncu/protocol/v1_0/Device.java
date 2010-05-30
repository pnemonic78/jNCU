/**
 * 
 */
package net.sf.jncu.protocol.v1_0;

/**
 * Device information.
 * 
 * @author moshew
 */
public class Device {

	/** Desktop path type. */
	public static final int kDesktop = 0;
	/** File path type type. */
	public static final int kFile = 1;
	/** Folder path type. */
	public static final int kFolder = 2;
	/** Disk path type. */
	public static final int kDisk = 3;

	/** Floppy disk device. */
	public static final int kFloppyDisk = 0;
	/** Hard disk drive device. */
	public static final int kHardDisk = 1;
	/** CD-ROM disc device. */
	public static final int kCdRomDisk = 2;
	/** Network drive device. */
	public static final int kNetDrive = 3;

	/**
	 * Creates a new device.
	 */
	public Device() {
		super();
	}

}
