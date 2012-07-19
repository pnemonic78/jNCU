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
package net.sf.jncu.fdil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.jncu.dil.DILReadProc;
import net.sf.jncu.dil.DILWriteProc;
import net.sf.jncu.dil.ReadingFromPipeException;
import net.sf.jncu.dil.WritingToPipeException;

/**
 * <h1>FDIL Interface</h1> <br />
 * The Frames Desktop Integration Library (FDIL) is a small library allowing the
 * creation and manipulation of NewtonScript objects. Because the objects the
 * FDIL manipulates on are NewtonScript-compatible, they can be exchanged with
 * Newton devices using communications libraries such as the CDIL. <br>
 * The FDIL can create any type of NewtonScript object, including virtual binary
 * objects, and frames and arrays with circular references. The FDIL, unlike
 * NewtonScript, does not provide automatic garbage collection.
 * <p>
 * <h2>About the FDIL Objects</h2>
 * <br>
 * The objects the FDIL manipulate mimic the NewtonScript objects. There is a
 * one-to-one correspondence between NewtonScript and FDIL objects. There are a
 * number of minor implementation details that differ, however.
 * <p>
 * <h1>Using the FDIL</h1>
 * <h2>Initializing the Library</h2>
 * Before calling any FDIL function, you should initialize the library by
 * calling <tt>FD_Startup</tt>. When you are done using the library, call
 * <tt>FD_Shutdown</tt>; this function deallocates all memory used by the FDIL.
 * Usually you just call <tt>FD_Startup</tt> once, but you can call it multiple
 * times as long as an equal number of calls to <tt>FD_Shutdown</tt> are made.
 * <h2>Object Comparison</h2>
 * The <tt>FD_Equal</tt> function compares two FDIL objects. Objects of
 * different types are never equal. Note that this is unlike NewtonScript, where
 * the integer 3 and the real 3.0 are considered equal. All pointer objects:
 * binaries, arrays, frames, and large binaries, are equal only if they refer to
 * the same object.
 * <h2>Object Duplication</h2>
 * The <tt>FD_Clone</tt> and <tt>FD_DeepClone</tt> create duplicates of an FDIL
 * object. If the object is an aggregate object, that is an array or frame,
 * <tt>FD_Clone</tt> only copies the top level objects. <tt>FD_DeepClone</tt>
 * also makes copies of any nested objects, recursively.
 * <h2>Object Printing</h2>
 * The <tt>FD_PrintObject</tt> function prints formatted FDIL objects.
 * <tt>FD_PrintObject</tt> actually just converts the object into formatted
 * text. You must supply a function to actually print the formatted text.
 * <h2>Error Handling</h2>
 * All functions set an internal error code indicating the success of that
 * operation. A few functions also return that error code directly. You can
 * access the internal error code value with the <tt>FD_GetError</tt> function.
 * You should call <tt>FD_GetError</tt> after every FDIL function code that does
 * not return an error code. The functions listed in “FDIL Reference” (page
 * 3-29) list the possible error codes that each particular function might
 * create.
 * <h2>Object Streaming</h2>
 * The <tt>FD_Flatten</tt> function converts any FDIL object, including
 * aggregate objects such as frames and arrays, to a flat stream of bytes in
 * Newton Stream Object Format (NSOF). <tt>FD_Flatten</tt> then calls a callback
 * function you provide to actually write the data. You could, for instance,
 * send the data to a Newton device over a CDIL pipe with the CD_Write function,
 * or store it to disk. The <tt>FD_UnFlatten</tt> function conversely converts
 * from an NSOF byte stream to an FDIL object, calling a callback function you
 * provide to get the NSOF byte stream. For a description of NSOF, see Chapter
 * 4, “Newton Streamed Object Format,” in Newton Formats.
 * <h2>Object Classes</h2>
 * All objects have a class. An object’s class is primarily for your use as a
 * programmer in giving a meaning to your data. The class of integer, immediate,
 * and magic pointer objects is immutable. Pointer objects have default classes,
 * but you can change them with the <tt>FD_SetClass</tt> function.
 * <p>
 * 
 * @author Moshe
 */
public class FDILibrary implements FDConstants {

	private static FDHandles handles;
	private static long usedMemory;
	private static String charset;
	private static Map<NSOFSymbol, FDHandle> symbols;
	private static FDLargeBinaryProcs blobProcs;

	private FDILibrary() {
	}

	/**
	 * Creates an integer object.<br>
	 * <tt>FD_Handle FD_MakeInt(long val)</tt>
	 * 
	 * @param val
	 *            An integer between -536,870,912...536,870,911, inclusive.
	 * 
	 * @return An integer FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ValueOutOfRangeException
	 */
	public static FDHandle makeInt(int val) throws FDILNotInitializedException, ValueOutOfRangeException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFInteger i = new NSOFInteger(val);
		FDHandle obj = handles.create(i);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is an integer object.<br>
	 * <tt>int FD_IsInt(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isInt(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return (obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_INTEGER;
	}

	/**
	 * Returns the long value stored in the object.<br>
	 * <tt>long FD_GetInt(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL integer object.
	 * @return A long.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedIntegerException
	 */
	public static int getInt(FDHandle obj) throws FDILNotInitializedException, ExpectedIntegerException {
		if (!isInt(obj))
			throw new ExpectedIntegerException();
		NSOFImmediate i = (NSOFImmediate) handles.get(obj);
		return i.getValue();
	}

	/**
	 * Creates the specified type of immediate object.<br>
	 * <tt>FD_Handle FD_MakeImmediate(long type, long value)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * This is a low-level function that you should rarely, if ever, call. The
	 * kinds of immediate objects applications are likely to require are
	 * character objects (which can be created with the <tt>FD_MakeChar</tt> and
	 * <tt>FD_MakeWideChar</tt> functions), NIL objects (which can be accessed
	 * through the <tt>kFD_NIL</tt> constant), or Boolean objects (the sole type
	 * of which can be access through the <tt>kFD_True</tt> constant). Note that
	 * <tt>FD_MakeImmediate</tt> does not perform ASCII to Unicode conversion
	 * when creating a character object. That higher-level operation is
	 * performed only by <tt>FD_MakeChar</tt>.
	 * 
	 * @param type
	 *            One of the following constants: <tt>kImmedSpecial</tt>,
	 *            <tt>kImmedCharacter</tt>, <tt>kImmedBoolean</tt>, or
	 *            <tt>kImmedReserved</tt>.
	 * @param value
	 *            The value of the immediate object.
	 * @return An immediate FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ValueOutOfRangeException
	 */
	public static FDHandle makeImmediate(int type, int value) throws FDILNotInitializedException, ValueOutOfRangeException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFImmediate i = NSOFNil.NIL;
		switch (type) {
		case FD_IMMED_BOOLEAN:
			if (value == 0)
				i = NSOFBoolean.FALSE;
			else
				i = NSOFBoolean.TRUE;
			break;
		case FD_IMMED_CHARACTER:
			i = new NSOFCharacter((char) value);
			break;
		case FD_IMMED_RESERVED:
			i = new NSOFImmediate(value, NSOFImmediate.IMMEDIATE_INTEGER);
			break;
		case FD_IMMED_SPECIAL:
			i = new NSOFMagicPointer(value);
			break;
		default:
			i = new NSOFImmediate(value, NSOFImmediate.IMMEDIATE_INTEGER);
		}
		FDHandle obj = handles.create(i);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is an immediate object.<br>
	 * <tt>int FD_IsImmediate(FD_Handle obj)</tt>
	 * <p>
	 * <em>SPECIAL CONSIDERATIONS</em><br>
	 * In NewtonScript the term “immediate” includes integers. Therefore, the
	 * NewtonScript function <tt>IsImmediate</tt> differs from
	 * <tt>FD_IsImmediate</tt>.
	 * 
	 * @param obj
	 *            The object to test.
	 * 
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isImmediate(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return (obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_IMMEDIATE;
	}

	/**
	 * Returns the components of an immediate object.<br>
	 * <tt>DIL_Error FD_GetImmediate(FD_Handle obj, long* type, long* value)</tt>
	 * 
	 * @param obj
	 *            An FDIL immediate object.
	 * @param typeAndValue
	 *            A pointer to where the type should be stored. This value will
	 *            be set to <tt>kImmedSpecial</tt>, <tt>kImmedCharacter</tt>,
	 *            <tt>kImmedBoolean</tt>, or <tt>kImmedReserved</tt>.<br>
	 *            A pointer to where the value should be stored. If this value
	 *            is {@code NULL}, the immediate value is simply not returned,
	 *            no error is signaled.
	 * 
	 * @return type at index {@code 0}, and value at index {@code 1}.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedImmediateException
	 */
	public static int getImmediate(FDHandle obj, int[] typeAndValue) throws FDILNotInitializedException, ExpectedImmediateException {
		if (!isImmediate(obj))
			throw new ExpectedImmediateException();
		NSOFImmediate i = (NSOFImmediate) handles.get(obj);
		int value = i.getValue();
		if (typeAndValue != null) {
			typeAndValue[0] = obj.getReference() & FDHandle.MASK_TYPE_IMMEDIATE;
			typeAndValue[1] = value;
		}
		return value;
	}

	/**
	 * Creates a character object.<br>
	 * <tt>FD_Handle FD_MakeChar(char val)</tt>
	 * 
	 * @param val
	 *            An ASCII character.
	 * @return A character FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static FDHandle makeChar(byte val) throws FDILNotInitializedException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFCharacter c = new NSOFAsciiCharacter(val);
		FDHandle obj = handles.create(c);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Creates a character object.<br>
	 * <tt>FD_Handle FD_MakeChar(char val)</tt>
	 * 
	 * @param val
	 *            An ASCII character.
	 * @return A character FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static FDHandle makeChar(char val) throws FDILNotInitializedException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFCharacter c = new NSOFAsciiCharacter(val);
		FDHandle obj = handles.create(c);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Creates a character object.<br>
	 * <tt>FD_Handle FD_MakeWideChar(DIL_WideChar val)</tt>
	 * 
	 * @param val
	 *            An Unicode character.
	 * @return A character FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static FDHandle makeWideChar(char val) throws FDILNotInitializedException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFCharacter c = new NSOFUnicodeCharacter(val);
		FDHandle obj = handles.create(c);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Creates a character object.<br>
	 * <tt>FD_Handle FD_MakeWideChar(DIL_WideChar val)</tt>
	 * 
	 * @param val
	 *            An Unicode code point.
	 * @return A character FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static FDHandle makeWideChar(int val) throws FDILNotInitializedException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFCharacter c = new NSOFUnicodeCharacter(val);
		FDHandle obj = handles.create(c);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a character object.<br>
	 * <tt>int FD_IsChar(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isChar(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return (obj.getReference() & FDHandle.MASK_TYPE_IMMEDIATE) == FDHandle.TYPE_IMMEDIATE_CHARACTER;
	}

	/**
	 * Returns the character value stored in the object.<br>
	 * <tt>char FD_GetChar(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL character object.
	 * @return An ASCII character.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedCharException
	 */
	public static char getChar(FDHandle obj) throws FDILNotInitializedException, ExpectedCharException {
		if (!isChar(obj))
			throw new ExpectedCharException();
		NSOFCharacter c = (NSOFCharacter) handles.get(obj);
		return c.getChar();
	}

	/**
	 * Returns the character value stored in the object.<br>
	 * <tt>DIL_WideChar FD_GetWideChar(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL character object.
	 * @return A Unicode character.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedCharException
	 */
	public static char getWideChar(FDHandle obj) throws FDILNotInitializedException, ExpectedCharException {
		if (!isChar(obj))
			throw new ExpectedCharException();
		NSOFCharacter c = (NSOFCharacter) handles.get(obj);
		return c.getChar();
	}

	/**
	 * Converts the characters in the buffer specified by {@code src} from
	 * Unicode to ASCII, storing the resulting characters in the buffer
	 * specified by {@code dest}.<br>
	 * <tt>DIL_Error FD_ConvertFromWideChar(char* dest, const DIL_WideChar* src, long numChars)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Only {@code numChars} characters are converted and transferred. No regard
	 * is given for {@code NULL} terminators.<br>
	 * Unicode characters which have no corresponding character in the
	 * destination character set are converted to <tt>0x1A</tt>.<br>
	 * <p>
	 * <em>SPECIAL CONSIDERATIONS</em><br>
	 * The characters in {@code src} are considered to be in big-endian format.
	 * 
	 * @param src
	 *            An array of DIL_WideChar objects to translate.
	 * @param numChars
	 *            How many characters to convert.
	 * @return A buffer for the converted ASCII characters.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 * @throws ExpectedNonNegativeValueException
	 */
	public static byte[] convertFromWideChar(final char[] src, int numChars) throws FDILNotInitializedException, NullPointerException, ExpectedNonNegativeValueException {
		checkInitialized();
		if (numChars < 0)
			throw new ExpectedNonNegativeValueException();
		String s = new String(src, 0, numChars);
		byte[] b;
		try {
			b = s.getBytes(charset);
		} catch (UnsupportedEncodingException uee) {
			throw new ExpectedNonNegativeValueException(uee.getMessage());
		}
		return b;
	}

	/**
	 * Converts the characters in the buffer specified by {@code src} from ASCII
	 * to Unicode, storing the resulting characters in the buffer specified by
	 * {@code dest}.<br>
	 * <tt>DIL_Error FD_ConvertToWideChar(DIL_WideChar* dest, const char* src, long numChars)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Only {@code numChars} characters are converted and transferred. No regard
	 * is given for {@code NULL} terminators.
	 * <p>
	 * <em>SPECIAL CONSIDERATIONS</em><br>
	 * The characters in {@code dest} are in big-endian format.
	 * 
	 * @param src
	 *            An array of DIL_WideChar objects to translate.
	 * @param numChars
	 *            How many characters to convert.
	 * @return A buffer for the converted ASCII characters.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 * @throws ExpectedNonNegativeValueException
	 */
	public static String convertToWideChar(final byte[] src, int numChars) throws FDILNotInitializedException, NullPointerException, ExpectedNonNegativeValueException {
		checkInitialized();
		if (numChars < 0)
			throw new ExpectedNonNegativeValueException();
		String s;
		try {
			s = new String(src, 0, numChars, charset);
		} catch (UnsupportedEncodingException uee) {
			throw new ExpectedNonNegativeValueException(uee.getMessage());
		}
		return s;
	}

	/**
	 * Changes the character set to use when converting Unicode and 8-bit
	 * characters.<br>
	 * <tt>DIL_Error FD_SetWideCharEncoding(long encoding)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * By default, the Macintosh version of the FDIL converts using the
	 * Macintosh character set, and the Windows version of the FDIL converts
	 * using the Windows character set. Currently, these are the only two
	 * character sets supported.
	 * 
	 * @param encoding
	 *            One of following constants: <tt>kFD_MacEncoding</tt>,
	 *            <tt>kFD_WindowsEncoding<tt>, or <tt>kFD_DefaultEncoding</tt>
	 *            (which is equal to <tt>kFD_MacEncoding</tt> on Macintosh
	 *            platforms, and <tt>kFD_WindowsEncoding</tt> on Windows
	 *            platforms).
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ValueOutOfRangeException
	 */
	public static void setWideCharEncoding(int encoding) throws FDILNotInitializedException, ValueOutOfRangeException {
		checkInitialized();
		switch (encoding) {
		case FD_MAC_ENCODING:
			charset = NSOFString.CHARSET_MAC;
			break;
		case FD_WINDOWS_ENCODING:
			charset = NSOFString.CHARSET_WIN;
			break;
		default:
			throw new ValueOutOfRangeException();
		}
	}

	/**
	 * Determines whether or not an FDIL object is a Boolean object.<br>
	 * <tt>int FD_IsBoolean(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isBoolean(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return (obj.getReference() & FDHandle.MASK_TYPE_IMMEDIATE) == FDHandle.TYPE_IMMEDIATE_BOOLEAN;
	}

	/**
	 * Determines whether the given object is the <tt>nil</tt> object.<br>
	 * <tt>int FD_IsNIL(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * This function is the inverse of <tt>FD_NotNIL</tt>.
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @see #isNotNil(FDHandle)
	 */
	public static boolean isNil(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return (obj.getReference() & FDHandle.MASK_TYPE_IMMEDIATE) == FDHandle.TYPE_IMMEDIATE_SPECIAL;
	}

	/**
	 * Determines whether the given object is anything but the <tt>nil</tt>
	 * object.<br>
	 * <tt>int FD_NotNIL(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * This function is the inverse of <tt>FD_IsNIL</tt>.
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @see #isNil(FDHandle)
	 */
	public static boolean isNotNil(FDHandle obj) throws FDILNotInitializedException {
		return !isNil(obj);
	}

	/**
	 * Determines whether or not an FDIL object is a pointer object.<br>
	 * <tt>int FD_IsPointerObject(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isPointerObject(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return (obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_POINTER;
	}

	/**
	 * Returns the length of the given object.<br>
	 * <tt>long FD_GetLength(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Only pointer objects have a length. For frames and arrays, the length is
	 * the number of elements they contain. For binary objects and large binary
	 * objects, the length is the number of bytes in the object.
	 * 
	 * @param obj
	 *            An FDIL pointer object.
	 * @return The length of the object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedPointerObjectException
	 */
	public static int getLength(FDHandle obj) throws FDILNotInitializedException, ExpectedPointerObjectException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			return 0;
		if (o instanceof NSOFArray)
			return ((NSOFArray) o).length();
		if (o instanceof NSOFBinaryObject) {
			NSOFBinaryObject b = (NSOFBinaryObject) o;
			byte[] val = b.getValue();
			return (val == null) ? 0 : val.length;
		}
		if (o instanceof NSOFFrame)
			return ((NSOFFrame) o).size();
		if (o instanceof NSOFString) {
			NSOFString s = (NSOFString) o;
			String val = s.getValue();
			return (val == null) ? 0 : val.length() << 1;
		}
		return 0;
	}

	/**
	 * Sets the length of the object.<br>
	 * <tt>DIL_Error FD_SetLength(FD_Handle obj, long newSize)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Only non-frame pointer objects can have their lengths changed. For
	 * arrays, {@code newSize} specifies the number of slots that should be in
	 * the array. For binaries and large binaries, {@code newSize} specifies the
	 * number of bytes that should be allocated to the object.
	 * <p>
	 * <em>SPECIAL CONSIDERATIONS</em><br>
	 * If an array is grown as a result of settings its length, additional slots
	 * are appended to the end of the array and set to <tt>kFD_NIL</tt>. If the
	 * array is reduced, slots are removed from the end of the array. If those
	 * slots contained pointer objects, it is up to you to make sure that the
	 * objects are deleted or otherwise handled before the references to them in
	 * the array are lost. All pointers to data within a binary object obtained
	 * with <tt>FD_GetBinaryData</tt> are invalidated if the object’s size is
	 * changed.
	 * 
	 * @param obj
	 *            An FDIL pointer object.
	 * @param newSize
	 *            The size to set the object’s length to.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedPointerObjectException
	 * @throws OutOfMemoryError
	 * @throws ValueOutOfRangeException
	 */
	public static void setLength(FDHandle obj, int newSize) throws FDILNotInitializedException, ExpectedPointerObjectException, OutOfMemoryError, ValueOutOfRangeException {
		if (!isPointerObject(obj))
			throw new ExpectedPointerObjectException();
		if (newSize < 0)
			throw new ValueOutOfRangeException();
		NSOFPointer p = (NSOFPointer) handles.get(obj);
		if (p instanceof NSOFArray) {
			NSOFArray a = (NSOFArray) p;
			a.setLength(newSize);
		} else if (p instanceof NSOFBinaryObject) {
			NSOFBinaryObject b = (NSOFBinaryObject) p;
			byte[] valueOld = b.getValue();
			if (valueOld == null)
				valueOld = new byte[0];
			if (valueOld.length == newSize)
				return;
			byte[] valueNew = new byte[newSize];
			System.arraycopy(valueOld, 0, valueNew, 0, Math.min(valueOld.length, newSize));
			b.setValue(valueNew);
		} else if (p instanceof NSOFString) {
			NSOFString s = (NSOFString) p;
			String valueOld = s.getValue();
			if (valueOld == null)
				valueOld = "";
			String valueNew = valueOld.substring(0, Math.min(valueOld.length(), newSize));
			s.setValue(valueNew);
		}
	}

	/**
	 * Creates a raw, unformatted binary object of the given size.<br>
	 * <tt>FD_Handle FD_MakeBinary(long size, const char* cls)</tt>
	 * <p>
	 * The contents of the binary object can be accessed with
	 * <tt>FD_GetBinaryData</tt>.
	 * 
	 * @param size
	 *            The length to make the binary object.
	 * @param cls
	 *            Either {@code NULL} in which case the binary object is given a
	 *            default class, or a string that is passed to
	 *            <tt>FD_MakeSymbol</tt> and becomes the object’s class.
	 * 
	 * @return A binary FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ValueOutOfRangeException
	 * @throws InvalidClassException
	 */
	public static FDHandle makeBinary(int size, String cls) throws FDILNotInitializedException, ValueOutOfRangeException, InvalidClassException {
		checkInitialized();
		if (size < 0)
			throw new ValueOutOfRangeException();
		long memBefore = Runtime.getRuntime().totalMemory();
		byte[] val = new byte[size];
		NSOFBinaryObject b = new NSOFBinaryObject(val);
		b.setObjectClass(cls);
		FDHandle obj = handles.create(b);
		FDHandle oClass = makeSymbol(cls);
		try {
			setClass(obj, oClass);
		} catch (ExpectedPointerObjectException upe) {
			upe.printStackTrace();
		}
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a binary object.<br>
	 * <tt>int FD_IsBinary(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isBinary(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		if ((obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_POINTER) {
			return (obj.getFlags() & FDHandle.MASK_FLAG_TYPE) == FDHandle.FLAG_BINARY;
		}
		return false;
	}

	/**
	 * Returns a pointer to the raw binary data stored in the binary object.<br>
	 * <tt>void* FD_GetBinaryData(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * <tt>FD_GetBinaryData</tt> cannot be used to get a pointer to the contents
	 * of a large binary object. Instead, use <tt>FD_ReadFromLargeBinary</tt>
	 * and <tt>FD_WriteToLargeBinary</tt> to access and modify a large binary’s
	 * contents.
	 * <p>
	 * <em>SPECIAL CONSIDERATIONS</em><br>
	 * Any pointers obtained with <tt>FD_GetBinaryData</tt> are invalidated by
	 * calling <tt>FD_SetLength</tt> on that binary object.
	 * 
	 * @param obj
	 *            An FDIL binary object.
	 * @return A pointer to where the data is stored.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedBinaryException
	 */
	public static Object getBinaryData(FDHandle obj) throws FDILNotInitializedException, ExpectedBinaryException {
		if (!isBinary(obj))
			throw new ExpectedBinaryException();
		NSOFBinaryObject o = (NSOFBinaryObject) handles.get(obj);
		return o.getValue();
	}

	/**
	 * Creates a real number object from the given value.<br>
	 * <tt>FD_Handle FD_MakeReal(double val)</tt>
	 * 
	 * @param val
	 *            Any valid IEEE-754 floating point value.
	 * 
	 * @return A real FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static FDHandle makeReal(float val) throws FDILNotInitializedException, ValueOutOfRangeException {
		return makeReal((double) val);
	}

	/**
	 * Creates a real number object from the given value.<br>
	 * <tt>FD_Handle FD_MakeReal(double val)</tt>
	 * 
	 * @param val
	 *            Any valid IEEE-754 floating point value.
	 * 
	 * @return A real FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static FDHandle makeReal(double val) throws FDILNotInitializedException, ValueOutOfRangeException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFReal r = new NSOFReal(val);
		FDHandle obj = handles.create(r);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a real number object.<br>
	 * <tt>int FD_IsInt(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isReal(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			return false;
		if (o instanceof NSOFReal)
			return true;
		return NSOFReal.CLASS_REAL.equals(o.getObjectClass());
	}

	/**
	 * Returns the double value stored in the object.<br>
	 * <tt>double FD_GetReal(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL real number object.
	 * @return A double.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedRealException
	 */
	public static double getReal(FDHandle obj) throws FDILNotInitializedException, ExpectedRealException {
		if (!isReal(obj))
			throw new ExpectedRealException();
		NSOFObject o = handles.get(obj);
		if (o instanceof NSOFReal) {
			NSOFReal r = (NSOFReal) o;
			return r.getReal();
		}
		if (o instanceof NSOFBinaryObject) {
			NSOFBinaryObject b = (NSOFBinaryObject) o;
			NSOFReal r = new NSOFReal(b.getValue());
			return r.getReal();
		}
		return 0;
	}

	/**
	 * Returns a symbol object, creating one if necessary.<br>
	 * <tt>FD_Handle FD_MakeSymbol(const char* str)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Symbols are a pooled resource. Once created, a symbol is added to an
	 * internal table. Subsequent requests to create a new symbol from the same
	 * text results in a reference to the previously created symbol to be
	 * returned; where “the same text” implies a case-insensitive comparison.
	 * 
	 * @param str
	 *            A <tt>NULL</tt>-terminated series of less than 254 ASCII
	 *            characters with values between 32-127, excluding the vertical
	 *            bar (‘|’) and backslash (‘\’) characters.
	 * 
	 * @return A symbol FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 * @throws SymbolTooLongException
	 * @throws IllegalCharInSymbolException
	 */
	public static FDHandle makeSymbol(final byte[] str) throws FDILNotInitializedException, NullPointerException, SymbolTooLongException, IllegalCharInSymbolException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		int length = 0;
		for (int i = 0; i < length; i++) {
			if (str[i] == 0) {
				length = i;
				break;
			}
		}
		String val;
		try {
			val = new String(str, 0, length, charset);
		} catch (UnsupportedEncodingException uee) {
			throw new ExpectedStringException(uee.getMessage());
		}
		NSOFSymbol s = new NSOFSymbol(val);
		FDHandle obj = symbols.get(s);
		if (obj == null) {
			obj = handles.create(s);
			symbols.put(s, obj);
		}
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Returns a symbol object, creating one if necessary.<br>
	 * <tt>FD_Handle FD_MakeSymbol(const char* str)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Symbols are a pooled resource. Once created, a symbol is added to an
	 * internal table. Subsequent requests to create a new symbol from the same
	 * text results in a reference to the previously created symbol to be
	 * returned; where “the same text” implies a case-insensitive comparison.
	 * 
	 * @param str
	 *            A <tt>NULL</tt>-terminated series of less than 254 ASCII
	 *            characters with values between 32-127, excluding the vertical
	 *            bar (‘|’) and backslash (‘\’) characters.
	 * 
	 * @return A symbol FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 * @throws SymbolTooLongException
	 * @throws IllegalCharInSymbolException
	 */
	public static FDHandle makeSymbol(final char[] str) throws FDILNotInitializedException, NullPointerException, SymbolTooLongException, IllegalCharInSymbolException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		String val = new String(str);
		NSOFSymbol s = new NSOFSymbol(val);
		FDHandle obj = symbols.get(s);
		if (obj == null) {
			obj = handles.create(s);
			symbols.put(s, obj);
		}
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Returns a symbol object, creating one if necessary.<br>
	 * <tt>FD_Handle FD_MakeSymbol(const char* str)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Symbols are a pooled resource. Once created, a symbol is added to an
	 * internal table. Subsequent requests to create a new symbol from the same
	 * text results in a reference to the previously created symbol to be
	 * returned; where “the same text” implies a case-insensitive comparison.
	 * 
	 * @param str
	 *            A <tt>NULL</tt>-terminated series of less than 254 ASCII
	 *            characters with values between 32-127, excluding the vertical
	 *            bar (‘|’) and backslash (‘\’) characters.
	 * 
	 * @return A symbol FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 * @throws SymbolTooLongException
	 * @throws IllegalCharInSymbolException
	 */
	public static FDHandle makeSymbol(final String str) throws FDILNotInitializedException, NullPointerException, SymbolTooLongException, IllegalCharInSymbolException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFSymbol s = new NSOFSymbol(str);
		FDHandle obj = symbols.get(s);
		if (obj == null) {
			obj = handles.create(s);
			symbols.put(s, obj);
		}
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a symbol object.<br>
	 * <tt>int FD_IsSymbol(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isSymbol(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			return false;
		if (o instanceof NSOFSymbol)
			return true;
		if (o instanceof NSOFBinaryObject)
			return NSOFSymbol.CLASS_SYMBOL.equals(o.getObjectClass());
		return false;
	}

	/**
	 * Returns a pointer to the <tt>NULL</tt>-terminated string of characters of
	 * the symbol object.<br>
	 * <tt>const char* FD_GetSymbol(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL symbol object.
	 * @return A pointer to the string that is the name of the symbol.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedSymbolException
	 */
	public static String getSymbol(FDHandle obj) throws FDILNotInitializedException, ExpectedSymbolException {
		if (!isSymbol(obj))
			throw new ExpectedSymbolException();
		NSOFSymbol s = (NSOFSymbol) handles.get(obj);
		return s.getValue();
	}

	/**
	 * Creates a binary object containing a <tt>NULL</tt>-terminated Unicode
	 * string.<br>
	 * <tt>FD_Handle FD_MakeString(const char* str)</tt>
	 * 
	 * @param str
	 *            A <tt>NULL</tt>-terminated series of ASCII characters; in
	 *            other words, a “C string.”
	 * 
	 * @return A string FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 * @throws ExpectedStringException
	 */
	public static FDHandle makeString(final byte[] str) throws FDILNotInitializedException, NullPointerException, ExpectedStringException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		int length = 0;
		for (int i = 0; i < length; i++) {
			if (str[i] == 0) {
				length = i;
				break;
			}
		}
		String val;
		try {
			val = new String(str, 0, length, charset);
		} catch (UnsupportedEncodingException uee) {
			throw new ExpectedStringException(uee.getMessage());
		}
		NSOFString s = new NSOFString(val);
		FDHandle obj = handles.create(s);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Creates a binary object containing a <tt>NULL</tt>-terminated Unicode
	 * string.<br>
	 * <tt>FD_Handle FD_MakeString(const char* str)</tt>
	 * 
	 * @param str
	 *            A <tt>NULL</tt>-terminated series of ASCII characters; in
	 *            other words, a “C string.”
	 * 
	 * @return A string FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 */
	public static FDHandle makeString(final char[] str) throws FDILNotInitializedException, NullPointerException {
		return makeWideString(str);
	}

	/**
	 * Creates a binary object containing a <tt>NULL</tt>-terminated Unicode
	 * string.<br>
	 * <tt>FD_Handle FD_MakeString(const char* str)</tt>
	 * 
	 * @param str
	 *            A <tt>NULL</tt>-terminated series of ASCII characters; in
	 *            other words, a “C string.”
	 * 
	 * @return A string FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 */
	public static FDHandle makeString(final CharSequence str) throws FDILNotInitializedException, NullPointerException {
		return makeWideString(str);
	}

	/**
	 * Creates a binary object containing a <tt>NULL</tt>-terminated Unicode
	 * string.<br>
	 * <tt>FD_Handle FD_MakeWideString(const DIL_WideChar* unicodeStr)</tt>
	 * 
	 * @param unicodeStr
	 *            <tt>NULL</tt>-terminated series of Unicode characters.
	 * 
	 * @return A string FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 */
	public static FDHandle makeWideString(final char[] unicodeStr) throws FDILNotInitializedException, NullPointerException {
		String val = new String(unicodeStr);
		return makeWideString(val);
	}

	/**
	 * Creates a binary object containing a <tt>NULL</tt>-terminated Unicode
	 * string.<br>
	 * <tt>FD_Handle FD_MakeWideString(const DIL_WideChar* unicodeStr)</tt>
	 * 
	 * @param unicodeStr
	 *            <tt>NULL</tt>-terminated series of Unicode characters.
	 * 
	 * @return A string FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws NullPointerException
	 */
	public static FDHandle makeWideString(final CharSequence unicodeStr) throws FDILNotInitializedException, NullPointerException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		String val = (unicodeStr == null) ? null : unicodeStr.toString();
		NSOFString s = new NSOFString(val);
		FDHandle obj = handles.create(s);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a string object.<br>
	 * <tt>int FD_IsString(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * This function returns true if <tt>FD_IsSubclass(obj,"string")</tt> would
	 * return true.
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isString(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			return false;
		if (o instanceof NSOFString)
			return true;
		if (o instanceof NSOFBinaryObject)
			return NSOFString.CLASS_STRING.equals(o.getObjectClass());
		return false;
	}

	/**
	 * Determines whether or not an FDIL object is a rich string object.<br>
	 * <tt>int FD_IsRichString(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Rich string objects are string containing embedded ink. These object
	 * cannot be created by the FDIL, nor can the ink be extracted or
	 * interpreted. However, you may receive such objects from a Newton device
	 * and may need to detect strings that cannot be completely interpreted.
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isRichString(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			return false;
		if (o instanceof NSOFString)
			return ((NSOFString) o).isRich();
		return false;
	}

	/**
	 * Copies over bufLen characters from a string object, converting from
	 * Unicode to ASCII.<br>
	 * <tt>DIL_Error FD_GetString(FD_Handle obj, char* buffer, long bufLen)</tt>
	 * 
	 * @param obj
	 *            An FDIL string object.
	 * @return Pointer to buffer for the C string.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedStringException
	 * @throws NullPointerException
	 * @throws ExpectedNonNegativeValueException
	 */
	public static String getString(FDHandle obj) throws FDILNotInitializedException, ExpectedStringException, NullPointerException, ExpectedNonNegativeValueException {
		return getWideString(obj);
	}

	/**
	 * Copies over bufLen characters from a string object, converting from
	 * Unicode to ASCII.<br>
	 * <tt>DIL_Error FD_GetString(FD_Handle obj, char* buffer, long bufLen)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * At most {@code bufLen} characters are copied over. If {@code obj} has
	 * more than {@code bufLen} characters, buffer points to an array of
	 * characters that is not <tt>NULL</tt>-terminated.
	 * 
	 * @param obj
	 *            An FDIL string object.
	 * @param buffer
	 *            Pointer to buffer for the C string.
	 * @param bufLen
	 *            The size of the string buffer.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedStringException
	 * @throws NullPointerException
	 * @throws ExpectedNonNegativeValueException
	 */
	public static void getString(FDHandle obj, byte[] buffer, int bufLen) throws FDILNotInitializedException, ExpectedStringException, NullPointerException,
			ExpectedNonNegativeValueException {
		if (!isString(obj))
			throw new ExpectedStringException();
		if (bufLen < 0)
			throw new ExpectedNonNegativeValueException();
		NSOFString s = (NSOFString) handles.get(obj);
		String val = s.getValue();
		if (val == null) {
			buffer[0] = '\u0000';
			return;
		}
		byte[] b;
		try {
			b = val.getBytes(NSOFString.CHARSET_UTF16);
		} catch (UnsupportedEncodingException uee) {
			throw new ExpectedStringException(uee.getMessage());
		}
		final int length = Math.min(bufLen, b.length - 2);
		// The 1st and 2nd bytes are UTF-16 header 0xFE and 0xFF.
		System.arraycopy(b, 2, buffer, 0, length);
		if (length < bufLen)
			buffer[length] = 0;
	}

	/**
	 * Copies over bufLen characters from a string object.<br>
	 * <tt>DIL_Error FD_GetString(FD_Handle obj, char* buffer, long bufLen)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * At most {@code bufLen} characters are copied over. If {@code obj} has
	 * more than {@code bufLen} characters, buffer points to an array of
	 * characters that is not <tt>NULL</tt>-terminated.
	 * 
	 * @param obj
	 *            An FDIL string object.
	 * @param buffer
	 *            Pointer to buffer for the C string.
	 * @param bufLen
	 *            The size of the string buffer.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedStringException
	 * @throws NullPointerException
	 * @throws ExpectedNonNegativeValueException
	 */
	public static void getString(FDHandle obj, char[] buffer, int bufLen) throws FDILNotInitializedException, ExpectedStringException, NullPointerException,
			ExpectedNonNegativeValueException {
		getWideString(obj, buffer, bufLen);
	}

	/**
	 * Copies over bufLen characters from a string object.<br>
	 * <tt>DIL_Error FD_GetWideString(FD_Handle obj, DIL_WideChar* buffer, long bufLen)</tt>
	 * 
	 * @param obj
	 *            An FDIL string object.
	 * @return Pointer to buffer for the C string.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedStringException
	 * @throws NullPointerException
	 * @throws ExpectedNonNegativeValueException
	 */
	public static String getWideString(FDHandle obj) throws FDILNotInitializedException, ExpectedStringException, NullPointerException, ExpectedNonNegativeValueException {
		if (!isString(obj))
			throw new ExpectedStringException();
		NSOFString s = (NSOFString) handles.get(obj);
		return s.getValue();
	}

	/**
	 * Copies over bufLen characters from a string object.<br>
	 * <tt>DIL_Error FD_GetString(FD_Handle obj, char* buffer, long bufLen)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * At most {@code bufLen} characters are copied over. If {@code obj} has
	 * more than {@code bufLen} characters, buffer points to an array of
	 * characters that is not <tt>NULL</tt>-terminated.
	 * 
	 * @param obj
	 *            An FDIL string object.
	 * @param buffer
	 *            Pointer to buffer for the C string.
	 * @param bufLen
	 *            The size of the string buffer.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedStringException
	 * @throws NullPointerException
	 * @throws ExpectedNonNegativeValueException
	 */
	public static void getWideString(FDHandle obj, char[] buffer, int bufLen) throws FDILNotInitializedException, ExpectedStringException, NullPointerException,
			ExpectedNonNegativeValueException {
		if (!isString(obj))
			throw new ExpectedStringException();
		if (bufLen < 0)
			throw new ExpectedNonNegativeValueException();
		NSOFString s = (NSOFString) handles.get(obj);
		String val = s.getValue();
		if (val == null) {
			buffer[0] = '\u0000';
			return;
		}
		final int length = Math.min(bufLen, val.length());
		for (int i = 0; i < length; i++) {
			buffer[i] = val.charAt(i);
		}
		if (length < bufLen)
			buffer[length] = '\u0000';
	}

	/**
	 * Converts a string binary object to a binary object whose data consists of
	 * a <tt>NULL</tt>-terminated array of ASCII characters.<br>
	 * <tt>FD_Handle FD_ASCIIString(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL string object.
	 * @return A binary object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedStringException
	 */
	public static FDHandle toASCIIString(FDHandle obj) throws FDILNotInitializedException, ExpectedStringException {
		if (!isString(obj))
			throw new ExpectedStringException();
		NSOFString s = (NSOFString) handles.get(obj);
		String str = s.getValue();
		if (str == null)
			str = "";
		byte[] ascii;
		try {
			ascii = str.getBytes(NSOFString.CHARSET_ASCII);
		} catch (UnsupportedEncodingException uee) {
			throw new ExpectedStringException();
		}
		// Append NULL-terminator
		byte[] val = new byte[ascii.length + 1];
		System.arraycopy(ascii, 0, val, 0, ascii.length);
		NSOFBinaryObject b = new NSOFBinaryObject(val);
		return handles.create(b);
	}

	/**
	 * Creates a large binary object of the given size.<br>
	 * <tt>FD_Handle FD_MakeLargeBinary(long size, const char * objClass, long compressed)</tt>
	 * 
	 * @param size
	 *            The size of the large binary object.
	 * @param objClass
	 *            Either <tt>NULL</tt> in which case the large binary object is
	 *            given a default class, or a string that is passed to
	 *            <tt>FD_MakeSymbol</tt> and becomes the object’s class.
	 * @param compressed
	 *            A value indicating whether to compress the data when storing
	 *            it, and what compression scheme to use. This compression is
	 *            done for you; you do not need to supply functions to compress
	 *            the data. Specify <tt>kFD_NoCompression</tt> if you do not
	 *            want the data compressed, and <tt>kFD_LZCompression</tt> to
	 *            compress the data.
	 * 
	 * @return A large binary FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ValueOutOfRangeException
	 * @throws CreatingStoreException
	 */
	public static FDHandle makeLargeBinary(int size, String objClass, int compressed) throws FDILNotInitializedException, ValueOutOfRangeException, CreatingStoreException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFLargeBinary b = new NSOFLargeBinary();
		b.setObjectClass(objClass);
		switch (compressed) {
		case FD_NO_COMPRESSION:
			b.setCompressed(false);
			break;
		case FD_LZ_COMPRESSION:
			b.setCompressed(true);
			b.setCompanderName(NSOFLargeBinary.COMPANDER_LZ);
			break;
		}
		if (blobProcs != null)
			blobProcs.create(b);
		FDHandle obj = handles.create(b);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a large binary object.<br>
	 * <tt>int FD_IsLargeBinary(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isLargeBinary(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		if ((obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_POINTER) {
			return (obj.getFlags() & FDHandle.MASK_FLAG_TYPE) == FDHandle.FLAG_BLOB;
		}
		return false;
	}

	/**
	 * Reads bytes from the large binary object.<br>
	 * <tt>DIL_Error FD_ReadFromLargeBinary(FD_Handle obj, long offset, void* buffer, long count)</tt>
	 * 
	 * @param obj
	 *            An FDIL large binary object.
	 * @param offset
	 *            Where to start reading from, in bytes from the beginning of
	 *            the binary object.
	 * @param buffer
	 *            Where to store the data.
	 * @param count
	 *            How many bytes to read.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedLargeBinaryException
	 * @throws ExpectedNonNegativeValueException
	 * @throws NullPointerException
	 * @throws CouldNotDecompressDataException
	 * @throws ReadingFromStoreException
	 */
	public static void readFromLargeBinary(FDHandle obj, int offset, byte[] buffer, int count) throws FDILNotInitializedException, ExpectedLargeBinaryException,
			ExpectedNonNegativeValueException, NullPointerException, CouldNotDecompressDataException, ReadingFromStoreException {
		if (!isLargeBinary(obj))
			throw new ExpectedLargeBinaryException();
		if (offset < 0)
			throw new ExpectedNonNegativeValueException();
		if (count < 0)
			throw new ExpectedNonNegativeValueException();
		NSOFLargeBinary l = (NSOFLargeBinary) handles.get(obj);
		if (l == null)
			throw new NullPointerException();
		byte[] val = l.getValue();
		System.arraycopy(val, offset, buffer, 0, count);
	}

	/**
	 * Reads bytes from the large binary object.<br>
	 * <tt>DIL_Error FD_ReadFromLargeBinary(FD_Handle obj, long offset, void* buffer, long count)</tt>
	 * 
	 * @param obj
	 *            An FDIL large binary object.
	 * @param offset
	 *            Where to start reading from, in bytes from the beginning of
	 *            the binary object.
	 * @param buffer
	 *            Where to store the data.
	 * @param count
	 *            How many bytes to read.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedLargeBinaryException
	 * @throws ExpectedNonNegativeValueException
	 * @throws NullPointerException
	 * @throws CouldNotDecompressDataException
	 * @throws ReadingFromStoreException
	 */
	public static void readFromLargeBinary(FDHandle obj, int offset, OutputStream buffer, int count) throws FDILNotInitializedException, ExpectedLargeBinaryException,
			ExpectedNonNegativeValueException, NullPointerException, CouldNotDecompressDataException, ReadingFromStoreException {
		if (!isLargeBinary(obj))
			throw new ExpectedLargeBinaryException();
		if (offset < 0)
			throw new ExpectedNonNegativeValueException();
		if (count < 0)
			throw new ExpectedNonNegativeValueException();
		NSOFLargeBinary l = (NSOFLargeBinary) handles.get(obj);
		if (l == null)
			throw new NullPointerException();
		byte[] val = l.getValue();
		try {
			buffer.write(val, 0, count);
		} catch (IOException ioe) {
			throw new ReadingFromStoreException(ioe.getMessage());
		}
	}

	/**
	 * Writes bytes to a large binary object.<br>
	 * <tt>DIL_Error FD_WriteToLargeBinary(FD_Handle obj, long offset, const void* buffer, long count)</tt>
	 * 
	 * @param obj
	 *            An FDIL large binary object.
	 * @param offset
	 *            Where to start writing from, in bytes from the beginning of
	 *            the binary object.
	 * @param buffer
	 *            Where the data is stored.
	 * @param count
	 *            How many bytes to write.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedLargeBinaryException
	 * @throws ExpectedNonNegativeValueException
	 * @throws NullPointerException
	 * @throws CouldNotDecompressDataException
	 * @throws WritingToStoreException
	 */
	public static void writeToLargeBinary(FDHandle obj, int offset, final byte[] buffer, int count) throws FDILNotInitializedException, ExpectedLargeBinaryException,
			ExpectedNonNegativeValueException, NullPointerException, CouldNotDecompressDataException, WritingToStoreException {
		if (!isLargeBinary(obj))
			throw new ExpectedLargeBinaryException();
		if (offset < 0)
			throw new ExpectedNonNegativeValueException();
		if (count < 0)
			throw new ExpectedNonNegativeValueException();
		NSOFLargeBinary l = (NSOFLargeBinary) handles.get(obj);
		if (l == null)
			throw new NullPointerException();
		byte[] val = l.getValue();
		if (offset + count > val.length) {
			byte[] val2 = new byte[offset + count];
			System.arraycopy(val, 0, val2, 0, val.length);
			val = val2;
			l.setValue(val);
		}
		System.arraycopy(buffer, 0, val, offset, count);
	}

	/**
	 * Writes bytes to a large binary object.<br>
	 * <tt>DIL_Error FD_WriteToLargeBinary(FD_Handle obj, long offset, const void* buffer, long count)</tt>
	 * 
	 * @param obj
	 *            An FDIL large binary object.
	 * @param offset
	 *            Where to start writing from, in bytes from the beginning of
	 *            the binary object.
	 * @param buffer
	 *            Where the data is stored.
	 * @param count
	 *            How many bytes to write.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedLargeBinaryException
	 * @throws ExpectedNonNegativeValueException
	 * @throws NullPointerException
	 * @throws CouldNotDecompressDataException
	 * @throws WritingToStoreException
	 */
	public static void writeToLargeBinary(FDHandle obj, int offset, InputStream buffer, int count) throws FDILNotInitializedException, ExpectedLargeBinaryException,
			ExpectedNonNegativeValueException, NullPointerException, CouldNotDecompressDataException, WritingToStoreException {
		if (!isLargeBinary(obj))
			throw new ExpectedLargeBinaryException();
		if (offset < 0)
			throw new ExpectedNonNegativeValueException();
		if (count < 0)
			throw new ExpectedNonNegativeValueException();
		NSOFLargeBinary l = (NSOFLargeBinary) handles.get(obj);
		if (l == null)
			throw new NullPointerException();
		byte[] val = l.getValue();
		if (offset + count > val.length) {
			byte[] val2 = new byte[offset + count];
			System.arraycopy(val, 0, val2, 0, val.length);
			val = val2;
			l.setValue(val);
		}
		int r;
		while (count >= 0) {
			try {
				r = buffer.read(val, offset, count);
				if (r == -1) {
					if (count > 0)
						throw new WritingToStoreException();
					break;
				}
				offset += r;
				count -= r;
			} catch (IOException ioe) {
				throw new WritingToStoreException(ioe.getMessage());
			}
		}
	}

	/**
	 * Sets the default set of procedures to use when creating a large binary
	 * object.<br>
	 * <tt>DIL_Error FD_SetLargeBinaryProcs(const FD_LargeBinaryProcs* procsPtr)</tt>
	 * 
	 * @param procsPtr
	 *            A pointer to a struct with function pointers to the functions
	 *            that create a large binary object and page it in and out of
	 *            memory. You can pass in the constant
	 *            <tt>kFD_MemoryStoreProcs</tt> to store large binary objects in
	 *            main memory, <tt>kFD_DiskStoreProcs</tt> to store the object
	 *            on disk, or <tt>kFD_NullStoreProcs</tt> to simply discard the
	 *            data. <tt>FD_SetLargeBinaryProcs</tt> copies over the struct
	 *            this pointer points to. This struct need not be permanent
	 *            data.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static void setLargeBinaryProcs(final FDLargeBinaryProcs procsPtr) throws FDILNotInitializedException {
		checkInitialized();
		blobProcs = procsPtr;
	}

	/**
	 * Creates an array large enough to hold the given number of elements.<br>
	 * <tt>FD_Handle FD_MakeArray(long size, const char* cls)</tt>
	 * 
	 * @param size
	 *            The initial size, number of slots, of the array.
	 * @param cls
	 *            Either <tt>NULL</tt> in which case the array’s is given a
	 *            default class, or a string that is passed to
	 *            <tt>FD_MakeSymbol</tt> and becomes the array’s class.
	 * 
	 * @return An array FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ValueOutOfRangeException
	 */
	public static FDHandle makeArray(int size, String cls) throws FDILNotInitializedException, ValueOutOfRangeException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFArray o = null;
		if (cls == null)
			o = new NSOFArray(size);
		else {
			NSOFSymbol oClass = new NSOFSymbol(cls);
			if (NSOFPlainArray.CLASS_PLAIN_ARRAY.equals(oClass)) {
				o = new NSOFPlainArray(size);
			} else {
				o = new NSOFArray(size);
				o.setObjectClass(oClass);
			}
		}
		FDHandle obj = handles.create(o);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is an array object.<br>
	 * <tt>int FD_IsArray(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isArray(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		if ((obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_POINTER) {
			return (obj.getFlags() & FDHandle.MASK_FLAG_TYPE) == FDHandle.FLAG_ARRAY;
		}
		return false;
	}

	/**
	 * Inserts the given object into the array at the specified position.<br>
	 * <tt>DIL_Error FD_InsertArraySlot(FD_Handle array, long pos, FD_Handle item)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Any objects between that position and the end of the array are moved down
	 * in the array to make room. Calling this function with
	 * {@code pos == FD_GetSize(array) -1} , is equivalent to appending an
	 * object to the array.
	 * 
	 * @param array
	 *            An FDIL array object.
	 * @param pos
	 *            Where to insert the item.
	 * @param item
	 *            The item to insert.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedArrayException
	 * @throws ValueOutOfRangeException
	 */
	public static void insertArraySlot(FDHandle array, int pos, FDHandle item) throws FDILNotInitializedException, ExpectedArrayException, ValueOutOfRangeException {
		if (!isArray(array))
			throw new ExpectedArrayException();
		NSOFArray a = (NSOFArray) handles.get(array);
		NSOFObject i = handles.get(item);
		a.insert(pos, i);
	}

	/**
	 * Appends the given element to the end of the array.<br>
	 * <tt>DIL_Error FD_AppendArraySlot(FD_Handle array, FD_Handle item)</tt>
	 * 
	 * @param array
	 *            An FDIL array object.
	 * @param item
	 *            The item to insert.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedArrayException
	 * @throws ValueOutOfRangeException
	 */
	public static void appendArraySlot(FDHandle array, FDHandle item) throws FDILNotInitializedException, ExpectedArrayException, ValueOutOfRangeException {
		if (!isArray(array))
			throw new ExpectedArrayException();
		NSOFArray a = (NSOFArray) handles.get(array);
		NSOFObject i = handles.get(item);
		a.add(i);
	}

	/**
	 * Removes the object at the given position in the array.<br>
	 * <tt>FD_Handle FD_RemoveArraySlot(FD_Handle array, long pos)</tt>
	 * 
	 * @param array
	 *            An FDIL array object.
	 * @param pos
	 *            Which item to remove.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedArrayException
	 * @throws ValueOutOfRangeException
	 */
	public static void removeArraySlot(FDHandle array, int pos) throws FDILNotInitializedException, ExpectedArrayException, ValueOutOfRangeException {
		if (!isArray(array))
			throw new ExpectedArrayException();
		NSOFArray a = (NSOFArray) handles.get(array);
		a.remove(pos);
	}

	/**
	 * Removes the object at the given position in the array.<br>
	 * <tt>FD_Handle FD_RemoveArraySlot(FD_Handle array, long pos)</tt>
	 * 
	 * @param array
	 *            An FDIL array object.
	 * @param pos
	 *            Where to begin removing array slots from.
	 * @param count
	 *            How many slots to remove.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedArrayException
	 * @throws ValueOutOfRangeException
	 */
	public static void removeArraySlotCount(FDHandle array, int pos, int count) throws FDILNotInitializedException, ExpectedArrayException, ValueOutOfRangeException {
		if (!isArray(array))
			throw new ExpectedArrayException();
		NSOFArray a = (NSOFArray) handles.get(array);
		for (int p = pos, c = 0; c < count; p++, c++)
			a.remove(p);
	}

	/**
	 * Sets the array slot at the given position to contain the specified new
	 * element.<br>
	 * <tt>FD_Handle FD_SetArraySlot(FD_Handle array, long pos, FD_Handle item)</tt>
	 * 
	 * @param array
	 *            An FDIL array object.
	 * @param pos
	 *            Which array slot to set.
	 * @param item
	 *            The new value of that array slot.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedArrayException
	 * @throws ValueOutOfRangeException
	 */
	public static void setArraySlot(FDHandle array, int pos, FDHandle item) throws FDILNotInitializedException, ExpectedArrayException, ValueOutOfRangeException {
		if (!isArray(array))
			throw new ExpectedArrayException();
		NSOFArray a = (NSOFArray) handles.get(array);
		NSOFObject i = handles.get(item);
		a.set(pos, i);
	}

	/**
	 * Returns the object in the given slot of the array.<br>
	 * <tt>FD_Handle FD_GetArraySlot(FD_Handle array, long pos)</tt>
	 * 
	 * @param array
	 *            An FDIL array object.
	 * @param pos
	 *            Which array slot to access.
	 * @return The item in that array slot.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedArrayException
	 * @throws ValueOutOfRangeException
	 */
	public static FDHandle getArraySlot(FDHandle array, int pos) throws FDILNotInitializedException, ExpectedArrayException, ValueOutOfRangeException {
		if (!isArray(array))
			throw new ExpectedArrayException();
		NSOFArray arr = (NSOFArray) handles.get(array);
		if (pos > arr.length())
			throw new ValueOutOfRangeException();
		return handles.create(arr.get(pos));
	}

	/**
	 * Creates an empty frame.<br>
	 * <tt>FD_Handle FD_MakeFrame()</tt>
	 * 
	 * @return A frame FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static FDHandle makeFrame() throws FDILNotInitializedException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFFrame f = new NSOFFrame();
		FDHandle obj = handles.create(f);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a frame object.<br>
	 * <tt>int FD_IsFrame(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isFrame(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		if ((obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_POINTER) {
			return (obj.getFlags() & FDHandle.MASK_FLAG_TYPE) == FDHandle.FLAG_FRAME;
		}
		return false;
	}

	/**
	 * Adds a key/value pair to the frame, where the key is specified by
	 * slotName and the value is specified by item.<br>
	 * <tt>FD_Handle FD_SetFrameSlot(FD_Handle frame, const char* slotName, FD_Handle item)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * If a pair with the specified key already exists in the frame, its
	 * corresponding value object is replaced with item, and the old value is
	 * returned for you to dispose of.
	 * 
	 * @param frame
	 *            An FDIL frame object.
	 * @param slotName
	 *            A C string for the slot name.
	 * @param item
	 *            An FDIL object to store in that slot.
	 * @return An FDIL object or <tt>kFD_NIL</tt> if the slot does not exist.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedFrameException
	 */
	public static FDHandle setFrameSlot(FDHandle frame, String slotName, FDHandle item) throws FDILNotInitializedException, ExpectedFrameException {
		if (!isFrame(frame))
			throw new ExpectedFrameException();
		NSOFFrame f = (NSOFFrame) handles.get(frame);
		NSOFSymbol s = new NSOFSymbol(slotName);
		NSOFObject i = handles.get(item);
		NSOFObject p = f.put(s, i);
		return handles.find(p);
	}

	/**
	 * Retrieves the slot identified by {@code slotName}.<br>
	 * <tt>FD_Handle FD_GetFrameSlot(FD_Handle frame, const char* slotName)</tt>
	 * 
	 * @param frame
	 *            An FDIL frame object.
	 * @param slotName
	 *            A C string for the slot name.
	 * @return An FDIL object if the slot exists, <tt>kFD_NIL</tt> otherwise.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedFrameException
	 */
	public static FDHandle getFrameSlot(FDHandle frame, String slotName) throws FDILNotInitializedException, ExpectedFrameException {
		if (!isFrame(frame))
			throw new ExpectedFrameException();
		NSOFFrame f = (NSOFFrame) handles.get(frame);
		return handles.create(f.get(slotName));
	}

	/**
	 * Returns whether or not a slot with the given name exists in the frame.<br>
	 * <tt>int FD_FrameHasSlot(FD_Handle frame, const char* slotName)</tt>
	 * 
	 * @param frame
	 *            An FDIL frame object.
	 * @param slotName
	 *            A C string for the slot name.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedFrameException
	 */
	public static boolean frameHasSlot(FDHandle frame, String slotName) throws FDILNotInitializedException, ExpectedFrameException {
		if (!isFrame(frame))
			throw new ExpectedFrameException();
		NSOFFrame f = (NSOFFrame) handles.get(frame);
		return f.hasSlot(slotName);
	}

	/**
	 * Removes the slot/value pair identified by {@code slotName}.<br>
	 * <tt>FD_Handle FD_RemoveFrameSlot(FD_Handle frame, const char* slotName)</tt>
	 * 
	 * @param frame
	 *            An FDIL frame object.
	 * @param slotName
	 *            A C string for the slot name.
	 * @return The FDIL object in the slot, if the slot exists, <tt>kFD_NIL</tt>
	 *         , otherwise.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedFrameException
	 */
	public static FDHandle removeFrameSlot(FDHandle frame, String slotName) throws FDILNotInitializedException, ExpectedFrameException {
		if (!isFrame(frame))
			throw new ExpectedFrameException();
		NSOFFrame f = (NSOFFrame) handles.get(frame);
		NSOFSymbol s = new NSOFSymbol(slotName);
		NSOFObject r = f.remove(s);
		return handles.find(r);
	}

	/**
	 * Allows traversal of the list of slots in a frame.<br>
	 * <tt>FD_Handle FD_GetIndFrameSlot(FD_Handle frame, long pos)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * By calling <tt>FD_GetIndFrameSlot</tt> with values of pos ranging from
	 * zero to <tt>FD_GetLength(frame) - 1</tt> (inclusive), you can retrieve
	 * the contents of all the slots in the frame.<br>
	 * The order in which the objects are returned is not defined. In
	 * particular, you should not expect to retrieve them in the order in which
	 * they were inserted.
	 * 
	 * @param frame
	 *            An FDIL frame object.
	 * @param pos
	 *            An index into the frame.
	 * @return The object in the position {@code pos}.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedFrameException
	 * @throws ValueOutOfRangeException
	 */
	public static FDHandle getIndFrameSlot(FDHandle frame, int pos) throws FDILNotInitializedException, ExpectedFrameException, ValueOutOfRangeException {
		if (!isFrame(frame))
			throw new ExpectedFrameException();
		NSOFFrame f = (NSOFFrame) handles.get(frame);
		return handles.create(f.get(pos));
	}

	/**
	 * Allows traversal of the list of slots in the frame, getting the name for
	 * each one.<br>
	 * <tt>FD_Handle FD_GetIndFrameSlotName(FD_Handle frame, long pos)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * By calling <tt>FD_GetIndFrameSlotName</tt> with values of pos ranging
	 * from zero to <tt>FD_GetLength(frame) - 1</tt> (inclusive), you can
	 * retrieve the names of all the slots in the frame.<br>
	 * The order in which the slot names are returned is not defined. In
	 * particular, you should not expect to retrieve them in the order in which
	 * they were inserted.
	 * 
	 * @param frame
	 *            An FDIL frame object.
	 * @param pos
	 *            An index into the frame.
	 * @return An FDIL string object with the slot’s name.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedFrameException
	 * @throws ValueOutOfRangeException
	 */
	public static FDHandle getIndFrameSlotName(FDHandle frame, int pos) throws FDILNotInitializedException, ExpectedFrameException, ValueOutOfRangeException {
		if (!isFrame(frame))
			throw new ExpectedFrameException();
		NSOFFrame f = (NSOFFrame) handles.get(frame);
		List<NSOFSymbol> names = new ArrayList<NSOFSymbol>(f.getNames());
		return handles.create(names.get(pos));
	}

	/**
	 * Creates a magic pointer object.<br>
	 * <tt>FD_Handle FD_MakeMagicPointer(long val)</tt>
	 * <p>
	 * You should only need to create magic pointer objects if you are creating
	 * a Newton development environment.
	 * 
	 * @param val
	 *            The pointer value.
	 * 
	 * @return A magic pointer FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ValueOutOfRangeException
	 */
	public static FDHandle makeMagicPointer(int val) throws FDILNotInitializedException, ValueOutOfRangeException {
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		NSOFMagicPointer p = new NSOFMagicPointer(val);
		FDHandle obj = handles.create(p);
		usedMemory += Runtime.getRuntime().totalMemory() - memBefore;
		return obj;
	}

	/**
	 * Determines whether or not an FDIL object is a magic pointer object.<br>
	 * <tt>int FD_IsMagicPointer(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isMagicPointer(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return (obj.getReference() & FDHandle.MASK_TYPE) == FDHandle.TYPE_MAGIC;
	}

	/**
	 * Returns the value stored in a magic pointer object.<br>
	 * <tt>long FD_GetMagicPointer(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL magic pointer object.
	 * @return A long.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedMagicPointerException
	 */
	public static int getMagicPointer(FDHandle obj) throws FDILNotInitializedException, ExpectedMagicPointerException {
		if (!isMagicPointer(obj))
			throw new ExpectedMagicPointerException();
		NSOFMagicPointer m = (NSOFMagicPointer) handles.get(obj);
		return m.getValue();
	}

	/**
	 * Initializes the FDIL.<br>
	 * <tt>DIL_Error FD_Startup()</tt>
	 * <p>
	 * You must call this function before calling any other FDIL function. It is
	 * generally called just once at the beginning of your application, but can
	 * be called more than once as long as an equal number of calls to
	 * <tt>FD_Shutdown</tt> are also made.
	 * 
	 * @see #shutdown()
	 */
	public static void startup() {
		handles = FDHandles.getInstance();
		charset = NSOFString.CHARSET_ASCII;
		symbols = new TreeMap<NSOFSymbol, FDHandle>();
	}

	/**
	 * Closes the library.<br>
	 * <tt>DIL_Error FD_Shutdown()</tt>
	 * <p>
	 * If this is the last call to <tt>FD_Shutdown</tt>, then all memory
	 * allocated by the FDIL since <tt>FD_Startup</tt> was called is
	 * deallocated.
	 * 
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static void shutdown() throws FDILNotInitializedException {
		checkInitialized();
		if (handles != null)
			handles.clear();
		handles = null;
		if (symbols != null)
			symbols.clear();
		symbols = null;
		blobProcs = null;
		usedMemory = 0;
	}

	/**
	 * Determines whether or not two objects are equal to each other.<br>
	 * <tt>int FD_Equal(FD_Handle obj1, FD_Handle obj2)</tt>
	 * 
	 * @param obj1
	 *            An FDIL object.
	 * @param obj2
	 *            An FDIL object.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean equal(FDHandle obj1, FDHandle obj2) throws FDILNotInitializedException {
		checkInitialized();
		if (obj1.equals(obj2))
			return true;
		NSOFObject o1 = handles.get(obj1);
		NSOFObject o2 = handles.get(obj2);
		if (o1 == null) {
			if (o2 == null)
				return true;
			return false;
		}
		return o1.equals(o2);
	}

	/**
	 * Creates a copy of the given object.<br>
	 * <tt>FD_Handle FD_Clone(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @return The new FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws PointerObjectIsFreeException
	 */
	public static FDHandle clone(FDHandle obj) throws FDILNotInitializedException, PointerObjectIsFreeException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			throw new PointerObjectIsFreeException();
		NSOFObject c;
		try {
			c = (NSOFObject) o.clone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
			return null;
		}
		return handles.create(c);
	}

	/**
	 * Creates a copy of the given object.<br>
	 * <tt>FD_Handle FD_DeepClone(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @return The new FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws PointerObjectIsFreeException
	 */
	public static FDHandle deepClone(FDHandle obj) throws FDILNotInitializedException, PointerObjectIsFreeException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			throw new PointerObjectIsFreeException();
		NSOFObject c;
		try {
			c = o.deepClone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
			return null;
		}
		return handles.create(c);
	}

	/**
	 * Disposes of an object’s allocated memory.<br>
	 * <tt>DIL_Error FD_Dispose(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Upon return {@code obj} is no longer valid, if it used to be a pointer
	 * object.<br>
	 * This function simply ignores non-pointer objects, since they contain no
	 * data outside the <tt>FD_Handle</tt>. Symbol objects are not disposed of
	 * either, since they are a pooled resource.<br>
	 * This function does a shallow-dispose of an object; that is if the object
	 * is an aggregate object such as an array or a frame, memory used by the
	 * component objects is not freed. To perform a deep-disposing of an
	 * aggregate object, use <tt>FD_DeepDispose</tt>.
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws PointerObjectIsFreeException
	 * @see #deepDispose(FDHandle)
	 */
	public static void dispose(FDHandle obj) throws FDILNotInitializedException, PointerObjectIsFreeException {
		if (obj == null)
			return;// Nothing to dispose.
		checkInitialized();
		long memBefore = Runtime.getRuntime().totalMemory();
		if (isLargeBinary(obj)) {
			NSOFLargeBinary l = (NSOFLargeBinary) handles.get(obj);
			if (blobProcs != null)
				blobProcs.destroy(l);
		}
		handles.dispose(obj);
		// Theoretically we are using less memory after disposal.
		long memDisposed = Math.min(0, Runtime.getRuntime().totalMemory() - memBefore);
		usedMemory = Math.max(0, usedMemory + memDisposed);
	}

	/**
	 * Disposes of an object’s allocated memory, and if the object is an array
	 * or frame, disposes of any objects contained within them.<br>
	 * <tt>DIL_Error FD_DeepDispose(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * Upon return {@code obj} is no longer valid, if it used to be a pointer
	 * object.<br>
	 * This function simply ignores non-pointer objects, since they contain no
	 * data outside the <tt>FD_Handle</tt>. Symbol objects are not disposed of
	 * either, since they are a pooled resource.
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws PointerObjectIsFreeException
	 * @throws ExpectedPointerObjectException
	 */
	public static void deepDispose(FDHandle obj) throws FDILNotInitializedException, PointerObjectIsFreeException {
		if (obj == null)
			return;// Nothing to dispose.
		if (!isPointerObject(obj)) {
			dispose(obj);
			return;
		}
		NSOFPointer p = (NSOFPointer) handles.get(obj);
		if (p == null)
			throw new PointerObjectIsFreeException();
		if (p instanceof NSOFArray) {
			NSOFArray a = (NSOFArray) p;
			int length = a.length();
			NSOFObject item;
			for (int i = 0; i < length; i++) {
				item = a.remove(0);
				if (item != NSOFNil.NIL)
					deepDispose(handles.find(item));
			}
		} else if (p instanceof NSOFBinaryObject) {
			NSOFBinaryObject b = (NSOFBinaryObject) p;
			b.setValue(null);
			dispose(obj);
		} else if (p instanceof NSOFFrame) {
			NSOFFrame f = (NSOFFrame) p;
			Set<NSOFSymbol> names = f.getNames();
			NSOFObject item;
			for (NSOFSymbol name : names) {
				item = f.remove(name);
				if (item != NSOFNil.NIL)
					deepDispose(handles.find(item));
			}
		} else if (p instanceof NSOFString) {
			NSOFString s = (NSOFString) p;
			s.setValue((String) null);
			dispose(obj);
		}
	}

	/**
	 * Formats and prints an FDIL object.<br>
	 * <tt>DIL_Error FD_PrintObject(FD_Handle obj, const char* EOLString, DIL_WriteProc writeFn, void* userData)</tt>
	 * 
	 * @param obj
	 *            The FDIL object to print.
	 * @param writeFn
	 *            A <tt>DIL_WriteProc</tt> that prints out the formatted text.
	 *            As with other functions that call a <tt>DIL_WriteProc</tt>,
	 *            this function calls your DIL_WriteProc with an <tt>amt</tt>
	 *            parameter that is the number of bytes to be written from the
	 *            {@code buf} parameter. This function adds a <tt>NULL</tt> byte
	 *            to the end of {@code buf}, as a convenience, allowing you to
	 *            treat {@code buf} as a C string. The <tt>NULL</tt> byte is
	 *            added in the <tt>(amt+1)</tt><sup>th</sup> position of
	 *            {@code buf}; that is <tt>buf[amt] == 0</tt>.
	 * @param userData
	 *            A pointer to data you provided to the function that calls your
	 *            writing procedure. For instance, it can contain a
	 *            <tt>FILE*</tt> if the <tt>DIL_WriteProc</tt> writes data to
	 *            disk, or a <tt>CD_Handle</tt> if the <tt>DIL_WriteProc</tt>
	 *            sends data to a Newton device, or <tt>NULL</tt> if no extra
	 *            data is needed.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static void printObject(FDHandle obj, DILWriteProc writeFn, Object userData) throws FDILNotInitializedException, IOException {
		checkInitialized();

		NSOFObject o = handles.get(obj);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream prn = new PrintStream(out);

		prn.print(o.toString());
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		writeFn.write(in, in.available(), userData);
	}

	/**
	 * Converts the given object into a flat stream of bytes in Newton Stream
	 * Object Format (NSOF) suitable for saving to disk or for transmission to a
	 * Newton device.<br>
	 * <tt>DIL_Error FD_Flatten(FD_Handle obj, DIL_WriteProc writeFn, void* userData)</tt>
	 * <p>
	 * <tt>FD_Flatten</tt> just performs the conversion of objects into bytes;
	 * the actual disposition of the bytes is determined by the writeFn function
	 * you provide.
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @param writeFn
	 *            A <tt>DIL_WriteProc</tt> to actually write the streamed bytes.
	 * @param userData
	 *            A pointer to any data you wish to be passed on to your
	 *            {@code writeFn}.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws WritingToPipeException
	 * @throws ReadingFromStoreException
	 */
	public static void flatten(FDHandle obj, DILWriteProc writeFn, Object userData) throws FDILNotInitializedException, WritingToPipeException, ReadingFromStoreException {
		checkInitialized();

		NSOFObject o = handles.get(obj);
		NSOFEncoder encoder = new NSOFEncoder();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in;
		try {
			encoder.flatten(o, out);
			in = new ByteArrayInputStream(out.toByteArray());
			writeFn.write(in, in.available(), userData);
		} catch (IOException ioe) {
			throw new WritingToPipeException(ioe);
		}
	}

	/**
	 * Converts a flat stream of bytes in Newton Stream Object Format (NSOF)
	 * into an FDIL object.<br>
	 * <tt>FD_Handle FD_Unflatten(DIL_ReadProc readFn, void* userData)</tt>
	 * <p>
	 * <tt>FD_Unflatten</tt> does not care where the bytes come from. It is only
	 * responsible for using them to recreate the original objects from which
	 * they were formed.
	 * 
	 * @param readFn
	 *            A <tt>DIL_ReadProc</tt> to actually read the streamed bytes.
	 * @param userData
	 *            A pointer to any data you wish to be passed on to your
	 *            {@code readFn}.
	 * @return An FDIL object.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ReadingFromPipeException
	 * @throws UnknownStreamVersionException
	 * @throws StreamCorruptedException
	 * @throws UnsupportedCompressionException
	 * @throws UnsupportedStoreVersionException
	 * @throws CreatingStoreException
	 * @throws WritingToStoreException
	 */
	public static FDHandle unflatten(DILReadProc readFn, Object userData) throws FDILNotInitializedException, ReadingFromPipeException, UnknownStreamVersionException,
			StreamCorruptedException, UnsupportedCompressionException, UnsupportedStoreVersionException, CreatingStoreException, WritingToStoreException {
		checkInitialized();

		NSOFObject o;
		NSOFDecoder decoder = new NSOFDecoder();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in;
		try {
			readFn.read(out, Integer.MAX_VALUE, userData);
			in = new ByteArrayInputStream(out.toByteArray());
			o = decoder.inflate(in);
		} catch (IOException ioe) {
			throw new ReadingFromPipeException(ioe);
		}
		FDHandle obj = handles.create(o);
		return obj;
	}

	/**
	 * Returns the class of the given object.<br>
	 * <tt>FD_Handle FD_GetClass(FD_Handle obj)</tt>
	 * 
	 * @param obj
	 *            An FDIL object.
	 * @return An FDIL symbol object that is the class of {@code obj}.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws PointerObjectIsFreeException
	 */
	public static FDHandle getClass(FDHandle obj) throws FDILNotInitializedException, PointerObjectIsFreeException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			throw new PointerObjectIsFreeException();
		return handles.create(o.getObjectClass());
	}

	/**
	 * Sets the class of given object to the specified class.<br>
	 * <tt>DIL_Error FD_SetClass(FD_Handle obj, FD_Handle oClass)</tt>
	 * <p>
	 * Only classes for non-symbol pointer objects can be set or changed. In
	 * general, classes should be specified as symbol objects. However, you can
	 * also set an object’s class to <tt>kFD_NIL</tt>.
	 * 
	 * @param obj
	 *            An FDIL pointer object.
	 * @param oClass
	 *            An FDIL symbol object for the class, or <tt>kFD_NIL</tt>.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws ExpectedPointerObjectException
	 * @throws InvalidClassException
	 */
	public static void setClass(FDHandle obj, FDHandle oClass) throws FDILNotInitializedException, ExpectedPointerObjectException, InvalidClassException {
		if (!isPointerObject(obj))
			throw new ExpectedPointerObjectException();
		if (!isSymbol(oClass))
			throw new InvalidClassException("class not symbol");
		NSOFPointer p = (NSOFPointer) handles.get(obj);
		NSOFSymbol cls = (NSOFSymbol) handles.get(oClass);
		p.setObjectClass(cls);
	}

	/**
	 * Returns whether or not an object is an instance of the given object
	 * class.<br>
	 * <tt>int FD_IsSubClass(FD_Handle obj, const char* class)</tt>
	 * <p>
	 * The <tt>FD_IsSubClass</tt> function determines if an object’s class is a
	 * subclass of a given class. This function uses the same algorithm used in
	 * the NewtonScript language, namely:
	 * <ul>
	 * <li>Every class is a subclass of the empty class "".
	 * <li>Every class is a subclass of itself.
	 * <li>A class x is a subclass of y, if y is a prefix of x at a period (.)
	 * boundary. For example, "foo.bar" is a subclass of "foo".
	 * <li>For compatibility with the version of NewtonScript found on Newton
	 * 1.x OS devices, the following classes are considered subclasses of
	 * "string":
	 * <ul>
	 * <li>"address"
	 * <li>"company"
	 * <li>"name"
	 * <li>"title"
	 * <li>"phone"
	 * </ul>
	 * <li>Furthermore the following classes are considered subclasses of
	 * "phone":
	 * <ul>
	 * <li>"homePhone"
	 * <li>"workPhone"
	 * <li>"faxPhone"
	 * <li>"otherPhone"
	 * <li>"carPhone"
	 * <li>"beeperPhone"
	 * <li>"mobilePhone"
	 * <li>"homeFaxPhone"
	 * </ul>
	 * </ul>
	 * 
	 * @param obj
	 *            The object to test.
	 * @param cls
	 *            The class to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 * @throws PointerObjectIsFreeException
	 */
	public static boolean isSubClass(FDHandle obj, String cls) throws FDILNotInitializedException, PointerObjectIsFreeException {
		checkInitialized();
		NSOFObject o = handles.get(obj);
		if (o == null)
			return false;

		// Every class is a subclass of the empty class "".
		if ((cls == null) || (cls.length() == 0))
			return true;

		NSOFSymbol oClass = new NSOFSymbol(cls);
		NSOFSymbol oSuperclass = o.getObjectClass();

		// Every class is a subclass of itself.
		if (oClass.equals(oSuperclass))
			return true;

		// A class x is a subclass of y, if y is a prefix of x at a period (.)
		// boundary.
		int indexDot;
		String prefix1 = oClass.getValue().toLowerCase(Locale.ENGLISH);
		indexDot = prefix1.indexOf('.');
		if (indexDot >= 0)
			prefix1 = prefix1.substring(0, indexDot);
		String prefix2 = oSuperclass.getValue().toLowerCase(Locale.ENGLISH);
		indexDot = prefix2.indexOf('.');
		if (indexDot >= 0)
			prefix2 = prefix2.substring(0, indexDot);
		if (prefix1.equals(prefix2))
			return true;

		NSOFSymbol inheritance = NSOFSymbol.getInheritance(oClass);
		if (inheritance != null) {
			return inheritance.equals(oSuperclass);
		}

		return false;
	}

	/**
	 * Returns the total amount of memory allocated by the FDIL library,
	 * including that occupied by created objects and that used by internal data
	 * structures.<br>
	 * <tt>long FD_AllocatedMemory()</tt>
	 * 
	 * @return The amount of memory used in bytes.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static long allocatedMemory() throws FDILNotInitializedException {
		checkInitialized();
		return usedMemory;
	}

	/**
	 * Determines whether the FDIL object refers to a deleted pointer object.<br>
	 * <tt>int FD_IsFree(FD_Handle obj)</tt>
	 * <p>
	 * <em>DISCUSSION</em><br>
	 * FDIL objects containing non-pointer objects such as integers or the nil
	 * object cause this function to return {@code false}, 0.
	 * <p>
	 * <em>SPECIAL CONSIDERATIONS</em><br>
	 * This function may return false, even if the object originally referenced
	 * by the given <tt>FD_Handle</tt> was deleted. This can occur, for example,
	 * if a new object was allocated in such a way that it occupies the same
	 * space previously occupied by the deleted object. The <tt>FD_Handle</tt>
	 * effectively refers to the newly created object, causing
	 * <tt>FD_IsFree</tt> to return false. Thus, <tt>FD_IsFree</tt> is mostly
	 * useful in the tracking down of object allocation and deletion bugs, and
	 * should not be called in shipping code.
	 * 
	 * @param obj
	 *            The object to test.
	 * @return Zero or non-zero.
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	public static boolean isFree(FDHandle obj) throws FDILNotInitializedException {
		checkInitialized();
		return handles.get(obj) == null;
	}

	/**
	 * Check that library has in fact been initialized by a call to
	 * {@link #shutdown()}.
	 * 
	 * @throws FDILNotInitializedException
	 *             if the library is not initialized.
	 */
	private static void checkInitialized() throws FDILNotInitializedException {
		if (handles == null)
			throw new FDILNotInitializedException();
	}
}
