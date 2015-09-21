package net.sf.jncu.fdil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Dump a NSOF frame.
 * 
 * @author Moshe
 */
public class FrameDump {

	public FrameDump() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
		FrameDump dumper = new FrameDump();
		if (file.isDirectory())
			dumper.dumpFolder(file);
		else
			dumper.dump(file);
	}

	public void dumpFolder(File folder) throws IOException {
		File[] files = folder.listFiles();
		if ((files == null) || (files.length == 0)) {
			System.err.println("Folder [" + folder + "] empty");
			return;
		}

		for (File file : files)
			dump(file);
	}

	public void dump(File file) throws IOException {
		System.out.println(file + ":");
		InputStream in = null;
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame;
		try {
			in = new FileInputStream(file);
			frame = (NSOFFrame) decoder.inflate(in);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}

		dump(frame);
	}

	protected void dump(NSOFFrame frame) {
		StringBuilder buf = new StringBuilder();
		dump(frame, buf);
		System.out.println(buf);
	}

	protected void dump(NSOFFrame frame, StringBuilder buf) {
		buf.append('{').append('\n');

		NSOFObject value;
		int i = 0;
		for (NSOFSymbol key : frame.getKeys()) {
			if (i > 0)
				buf.append(",\n");
			value = frame.get(key);
			buf.append(key.getValue());
			buf.append('=');
			if (value instanceof NSOFFrame) {
				dump((NSOFFrame) value, buf);
			} else {
				buf.append(value.toString());
			}
			i++;
		}
		buf.append('\n').append('}');
	}
}
