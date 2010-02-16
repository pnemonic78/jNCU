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
	 * Determines whether the ADSP service is available.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	@SuppressWarnings("unused")
	public static void checkADSP() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
		throw new ServiceNotSupportedException();
	}

	/**
	 * Determines whether the MNP service is available.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	@SuppressWarnings("unused")
	public static void checkMNPSerial() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
	}

	/**
	 * Determines whether the TCP service is available.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	@SuppressWarnings("unused")
	public static void checkTCP() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
	}

	/**
	 * Determines whether the CTB service is available.
	 * 
	 * @param toolName
	 *            the name of the tool.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	@SuppressWarnings("unused")
	public static void checkCTB(String toolName) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
		throw new ServiceNotSupportedException();
	}

}
