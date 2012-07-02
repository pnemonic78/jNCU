/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.newton.os;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFReal;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton version information.
 * 
 * @author moshew
 */
public class NewtonInfo {

	/** English. */
	public static final int ENGLISH = 0;
	/** French. */
	public static final int FRENCH = 1;
	/** German. */
	public static final int GERMAN = 2;

	public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol SLOT_ID = new NSOFSymbol("newtonId");
	public static final NSOFSymbol SLOT_MANUFACTURER = new NSOFSymbol("manufacturerId");
	public static final NSOFSymbol SLOT_MACHINE = new NSOFSymbol("machineType");
	public static final NSOFSymbol SLOT_ROM_VERSION = new NSOFSymbol("romVersion");
	public static final NSOFSymbol SLOT_ROM_STAGE = new NSOFSymbol("romStage");
	public static final NSOFSymbol SLOT_RAM_SIZE = new NSOFSymbol("ramSize");
	public static final NSOFSymbol SLOT_SCREEN_HEIGHT = new NSOFSymbol("screenHeight");
	public static final NSOFSymbol SLOT_SCREEN_WIDTH = new NSOFSymbol("screenWidth");
	public static final NSOFSymbol SLOT_PATCH = new NSOFSymbol("patchVersion");
	public static final NSOFSymbol SLOT_OBJECTS = new NSOFSymbol("objectSystemVersion");
	public static final NSOFSymbol SLOT_INTERNAL_SIGNATURE = new NSOFSymbol("internalStoreSignature");
	public static final NSOFSymbol SLOT_SCREEN_VERTICAL = new NSOFSymbol("screenResolutionVertical");
	public static final NSOFSymbol SLOT_SCREEN_HORIZONTAL = new NSOFSymbol("screenResolutionHorizontal");
	public static final NSOFSymbol SLOT_SCREEN_DEPTH = new NSOFSymbol("screenDepth");
	public static final NSOFSymbol SLOT_SERIAL = new NSOFSymbol("serialNumber");
	public static final NSOFSymbol SLOT_PROTOCOL = new NSOFSymbol("targetProtocol");

	private String name;
	private int newtonId;
	private int manufacturerId;
	private int machineType;
	private int romVersion;
	private int romStage;
	private int ramSize;
	private int screenHeight;
	private int screenWidth;
	private int patchVersion;
	private int objectSystemVersion;
	private int internalStoreSignature;
	private int screenResolutionVertical;
	private int screenResolutionHorizontal;
	private int screenDepth;
	private long serialNumber;
	private int targetProtocol;

	/**
	 * Creates a new Newton information.
	 */
	public NewtonInfo() {
		super();
	}

	/**
	 * Get the Newton id.
	 * <p>
	 * An almost unique ID which represents a particular Newton. It is a random
	 * number from a very large domain, so very close to unique.
	 * 
	 * @return the Newton id.
	 */
	public int getNewtonId() {
		return newtonId;
	}

	/**
	 * Set the Newton id.
	 * 
	 * @param newtonId
	 *            the Newton id.
	 */
	public void setNewtonId(int newtonId) {
		this.newtonId = newtonId;
	}

	/**
	 * Get the manufacturer id.
	 * <p>
	 * An integer indicating the manufacturer of the Newton device.
	 * 
	 * @return the manufacturer id.
	 */
	public int getManufacturerId() {
		return manufacturerId;
	}

	/**
	 * Set the manufacturer id.
	 * 
	 * @param manufacturerId
	 *            the manufacturer id.
	 */
	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	/**
	 * Get the machine type.
	 * <p>
	 * An integer indicating the hardware type this ROM was built for.
	 * 
	 * @return the machine type.
	 */
	public int getMachineType() {
		return machineType;
	}

	/**
	 * Set the machine type.
	 * 
	 * @param machineType
	 *            the machine type.
	 */
	public void setMachineType(int machineType) {
		this.machineType = machineType;
	}

	/**
	 * Get the major ROM version number.
	 * 
	 * @return the version.
	 */
	public int getMajorVersion() {
		return (romVersion >> 16) & 0xFFFF;
	}

	/**
	 * Get the minor ROM version number.
	 * 
	 * @return the version.
	 */
	public int getMinorVersion() {
		return romVersion & 0xFFFF;
	}

	/**
	 * Get the ROM version.
	 * 
	 * @return the version.
	 */
	public int getROMVersion() {
		return romVersion;
	}

	/**
	 * Set the ROM version.
	 * 
	 * @param romVersion
	 *            the version.
	 */
	public void setROMVersion(int romVersion) {
		this.romVersion = romVersion;
	}

	/**
	 * Get the ROM language.
	 * <p>
	 * An integer indicating the language (English, German, French).
	 * 
	 * @return the ROM stage.
	 * @see #ENGLISH
	 * @see #FRENCH
	 * @see #GERMAN
	 */
	public int getLanguage() {
		return (romStage >> 16) & 0xFFFF;
	}

	/**
	 * Get the ROM stage.
	 * <p>
	 * An integer indicating the stage of the ROM (alpha, beta, final).
	 * 
	 * @return the ROM stage.
	 */
	public int getStage() {
		return romStage & 0xFFFF;
	}

	/**
	 * Get the ROM stage.
	 * 
	 * @return the ROM stage.
	 */
	public int getROMStage() {
		return romStage;
	}

	/**
	 * Set the ROM stage.
	 * 
	 * @param romStage
	 *            the ROM stage.
	 */
	public void setROMStage(int romStage) {
		this.romStage = romStage;
	}

	/**
	 * Get the RAM size.
	 * <p>
	 * The amount of RAM on the Newton device.
	 * 
	 * @return the RAM size.
	 */
	public int getRAMSize() {
		return ramSize;
	}

	/**
	 * Set the RAM size.
	 * 
	 * @param ramSize
	 *            the RAM size.
	 */
	public void setRAMSize(int ramSize) {
		this.ramSize = ramSize;
	}

	/**
	 * Get the screen height.
	 * <p>
	 * An integer representing the height of the screen in pixels. The height
	 * takes into account the current screen orientation.
	 * 
	 * @return the screen height.
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Set the screen height.
	 * 
	 * @param screenHeight
	 *            the screen height.
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * Get the screen width.
	 * <p>
	 * An integer representing the width of the screen in pixels. The width
	 * takes into account the current screen orientation.
	 * 
	 * @return the screen width.
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Set the screen width.
	 * 
	 * @param screenWidth
	 *            the screen width.
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * Get the patch version.
	 * 
	 * @return the patch version.
	 */
	public int getPatchVersion() {
		return patchVersion;
	}

	/**
	 * Set the patch version.
	 * <p>
	 * This value is {@code 0} on an unpatched Newton device, and non-zero on a
	 * patched Newton.
	 * 
	 * @param patchVersion
	 *            the patch version.
	 */
	public void setPatchVersion(int patchVersion) {
		this.patchVersion = patchVersion;
	}

	/**
	 * Get the object system version.
	 * <p>
	 * Newton Object System (NOS) version, or Newton Streamed Object Format
	 * (NSOF) version.<br>
	 * The version of the NewtonScript interpreter.
	 * 
	 * @return the version.
	 */
	public int getObjectSystemVersion() {
		return objectSystemVersion;
	}

	/**
	 * Set the NOS version.
	 * 
	 * @param objectSystemVersion
	 *            the version.
	 */
	public void setObjectSystemVersion(int objectSystemVersion) {
		this.objectSystemVersion = objectSystemVersion;
	}

	/**
	 * Get the internal store signature.
	 * <p>
	 * The signature of the internal store. Note that this value is changed with
	 * a hard reset.
	 * 
	 * @return the signature.
	 */
	public int getInternalStoreSignature() {
		return internalStoreSignature;
	}

	/**
	 * Set the internal store signature.
	 * 
	 * @param internalStoreSignature
	 *            the signature.
	 */
	public void setInternalStoreSignature(int internalStoreSignature) {
		this.internalStoreSignature = internalStoreSignature;
	}

	/**
	 * Get the vertical screen resolution.
	 * 
	 * @return the resolution.
	 */
	public int getScreenResolutionVertical() {
		return screenResolutionVertical;
	}

	/**
	 * Set the vertical screen resolution.
	 * <p>
	 * The number of horizontal pixels per inch.
	 * 
	 * @param screenResolutionVertical
	 *            the resolution.
	 */
	public void setScreenResolutionVertical(int screenResolutionVertical) {
		this.screenResolutionVertical = screenResolutionVertical;
	}

	/**
	 * Get the horizontal screen resolution.
	 * 
	 * @return the resolution.
	 */
	public int getScreenResolutionHorizontal() {
		return screenResolutionHorizontal;
	}

	/**
	 * Set the horizontal screen resolution.
	 * <p>
	 * The number of vertical pixels per inch.
	 * 
	 * @param screenResolutionHorizontal
	 *            the resolution.
	 */
	public void setScreenResolutionHorizontal(int screenResolutionHorizontal) {
		this.screenResolutionHorizontal = screenResolutionHorizontal;
	}

	/**
	 * Get the screen depth.
	 * 
	 * @return the depth.
	 */
	public int getScreenDepth() {
		return screenDepth;
	}

	/**
	 * Set the screen depth.
	 * <p>
	 * The number of bits per pixel.<br>
	 * The bit depth of the LCD screen. For the MessagePad 120, the LCD supports
	 * a monochrome screen depth of 1. The eMate 300 and MessagePad 200 have 4
	 * bit depth LCD screens.
	 * 
	 * @param screenDepth
	 *            the depth.
	 */
	public void setScreenDepth(int screenDepth) {
		this.screenDepth = screenDepth;
	}

	/**
	 * Get the serial number.
	 * 
	 * @return the serial number.
	 */
	public long getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Set the serial number.
	 * <p>
	 * An 8-byte object containing the unique hardware serial number of the
	 * Newton device on those devices that contain this hardware.
	 * 
	 * @param serialNumber
	 *            the serial number.
	 */
	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * Get the target protocol.
	 * 
	 * @return the protocol.
	 */
	public int getTargetProtocol() {
		return targetProtocol;
	}

	/**
	 * Set the target protocol.
	 * <p>
	 * The version of the protocol used by the Dock application. On Newton 2.0
	 * devices this is {@code 9}. On Newton 2.1 devices this is {@code 11}.
	 * 
	 * @param targetProtocol
	 *            the protocol.
	 */
	public void setTargetProtocol(int targetProtocol) {
		this.targetProtocol = targetProtocol;
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

	/**
	 * Get the information as a frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFFrame frame = new NSOFFrame();

		frame.put(SLOT_NAME, new NSOFString(getName()));
		frame.put(SLOT_ID, new NSOFInteger(getNewtonId()));
		frame.put(SLOT_MANUFACTURER, new NSOFInteger(getManufacturerId()));
		frame.put(SLOT_MACHINE, new NSOFInteger(getMachineType()));
		frame.put(SLOT_ROM_VERSION, new NSOFInteger(getROMVersion()));
		frame.put(SLOT_ROM_STAGE, new NSOFInteger(getROMStage()));
		frame.put(SLOT_RAM_SIZE, new NSOFInteger(getRAMSize()));
		frame.put(SLOT_SCREEN_HEIGHT, new NSOFInteger(getScreenHeight()));
		frame.put(SLOT_SCREEN_WIDTH, new NSOFInteger(getScreenWidth()));
		frame.put(SLOT_PATCH, new NSOFInteger(getPatchVersion()));
		frame.put(SLOT_OBJECTS, new NSOFInteger(getObjectSystemVersion()));
		frame.put(SLOT_INTERNAL_SIGNATURE, new NSOFInteger(getInternalStoreSignature()));
		frame.put(SLOT_SCREEN_VERTICAL, new NSOFInteger(getScreenResolutionVertical()));
		frame.put(SLOT_SCREEN_HORIZONTAL, new NSOFInteger(getScreenResolutionHorizontal()));
		frame.put(SLOT_SCREEN_DEPTH, new NSOFInteger(getScreenDepth()));
		frame.put(SLOT_SERIAL, new NSOFReal(getSerialNumber()));
		frame.put(SLOT_PROTOCOL, new NSOFInteger(getTargetProtocol()));

		return frame;
	}

	/**
	 * Decode the information from a frame.
	 * 
	 * @return the frame.
	 */
	public void fromFrame(NSOFFrame frame) {
		this.setName(((NSOFString) frame.get(NewtonInfo.SLOT_NAME)).getValue());
		this.setNewtonId(((NSOFInteger) frame.get(NewtonInfo.SLOT_ID)).getValue());
		this.setManufacturerId(((NSOFInteger) frame.get(NewtonInfo.SLOT_MANUFACTURER)).getValue());
		this.setMachineType(((NSOFInteger) frame.get(NewtonInfo.SLOT_MACHINE)).getValue());
		this.setROMVersion(((NSOFInteger) frame.get(NewtonInfo.SLOT_ROM_VERSION)).getValue());
		this.setROMStage(((NSOFInteger) frame.get(NewtonInfo.SLOT_ROM_STAGE)).getValue());
		this.setRAMSize(((NSOFInteger) frame.get(NewtonInfo.SLOT_RAM_SIZE)).getValue());
		this.setScreenHeight(((NSOFInteger) frame.get(NewtonInfo.SLOT_SCREEN_HEIGHT)).getValue());
		this.setScreenWidth(((NSOFInteger) frame.get(NewtonInfo.SLOT_SCREEN_WIDTH)).getValue());
		this.setPatchVersion(((NSOFInteger) frame.get(NewtonInfo.SLOT_PATCH)).getValue());
		this.setObjectSystemVersion(((NSOFInteger) frame.get(NewtonInfo.SLOT_OBJECTS)).getValue());
		this.setInternalStoreSignature(((NSOFInteger) frame.get(NewtonInfo.SLOT_INTERNAL_SIGNATURE)).getValue());
		this.setScreenResolutionVertical(((NSOFInteger) frame.get(NewtonInfo.SLOT_SCREEN_VERTICAL)).getValue());
		this.setScreenResolutionHorizontal(((NSOFInteger) frame.get(NewtonInfo.SLOT_SCREEN_HORIZONTAL)).getValue());
		this.setScreenDepth(((NSOFInteger) frame.get(NewtonInfo.SLOT_SCREEN_DEPTH)).getValue());
		this.setSerialNumber((long) ((NSOFReal) frame.get(NewtonInfo.SLOT_SERIAL)).getReal());
		this.setTargetProtocol(((NSOFInteger) frame.get(NewtonInfo.SLOT_PROTOCOL)).getValue());
	}
}
