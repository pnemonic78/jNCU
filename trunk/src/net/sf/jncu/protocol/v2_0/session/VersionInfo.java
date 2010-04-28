package net.sf.jncu.protocol.v2_0.session;

/**
 * Newton version information.
 * 
 * @author moshew
 */
public class VersionInfo {

	/** Number uniquely identifying the Newton. */
	public int newtonUniqueId;
	/** Manufacturer id. */
	public int manufacturerId;
	/** Machine type. */
	public int machineType;
	/** ROM version. */
	public int romVersion;
	/** ROM stage. */
	public int romStage;
	/** RAM size. */
	public int ramSize;
	/** Screen height (pixels). */
	public int screenHeight;
	/** Screen width (pixels). */
	public int screenWidth;
	/** System update version. */
	public int systemUpdateVersion;
	/** Newton object system version. */
	public int objectSystemVersion;
	/** Signature of internal store. */
	public int internalStoreSignature;
	/** Vertical screen resolution (dots per inch). */
	public int screenResolutionVertical;
	/** Horizontal screen resolution (dots per inch). */
	public int screenResolutionHorizontal;
	/** Screen depth (bits). */
	public int screenDepth;
	/** Other information. */
	public int[] other;

	/**
	 * Creates a new version information.
	 */
	public VersionInfo() {
		super();
	}

}
