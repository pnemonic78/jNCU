package net.sf.jncu.cdil;

import net.sf.jncu.cdil.adsp.ADSPPipe;
import net.sf.jncu.cdil.ctb.CTBPipe;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.tcp.TCPPipe;

/**
 * CDIL pipe factory.
 * 
 * @author moshew
 */
public class CDPipeFactory {

	private static CDPipeFactory instance;

	/**
	 * Creates a new factory.
	 */
	protected CDPipeFactory() {
		super();
	}

	/**
	 * Get the factory instance.
	 * 
	 * @return the factory.
	 */
	public static CDPipeFactory getInstance() {
		if (instance == null) {
			instance = new CDPipeFactory();
		}
		return instance;
	}

	/**
	 * These functions create a connection with the Newton device using the
	 * AppleTalk communication service.
	 * 
	 * @return the connection.
	 */
	public CDPipe createADSP() {
		return new ADSPPipe();
	}

	/**
	 * These functions create a connection with the Newton device using the MNP
	 * Serial communication service.
	 * 
	 * @return the connection.
	 */
	public CDPipe createMNPSerial() {
		return new MNPPipe();
	}

	/**
	 * These functions create a connection with the Newton device using the TCP
	 * communication service.
	 * 
	 * @return the connection.
	 */
	public CDPipe createTCP() {
		return new TCPPipe();
	}

	/**
	 * These functions create a connection with the Newton device using the
	 * Macintosh Communication Tool communication service.
	 * 
	 * @return the connection.
	 */
	public CDPipe createCTB() {
		return new CTBPipe();
	}

	/**
	 * Test to see if the AppleTalk service is available.
	 * 
	 * @return true if the respective pipe creation function will succeed, but
	 *         you cannot determine if the connection attempt will succeed until
	 *         you try.
	 */
	public static boolean checkADSP() {
		return false;
	}

	/**
	 * Test to see if the MNP Serial service is available.
	 * 
	 * @return true if the respective pipe creation function will succeed, but
	 *         you cannot determine if the connection attempt will succeed until
	 *         you try.
	 */
	public static boolean checkMNPSerial() {
		return true;
	}

	/**
	 * Test to see if the appropriate service is available.
	 * 
	 * @return true if the respective pipe creation function will succeed, but
	 *         you cannot determine if the connection attempt will succeed until
	 *         you try.
	 */
	public static boolean checkTCP() {
		return true;
	}

	/**
	 * Test to see if the Macintosh Communication Too service is available.
	 * 
	 * @return true if the respective pipe creation function will succeed, but
	 *         you cannot determine if the connection attempt will succeed until
	 *         you try.
	 */
	public static boolean checkCTB() {
		return false;
	}

}
