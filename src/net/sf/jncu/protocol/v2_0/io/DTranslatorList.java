/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFPlainArray;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDTranslatorList</tt><br>
 * This command returns an array of translators that can be used with the
 * specified file. The list can include DataViz translators and tab templates.
 * The array should be in the order that the translators should be displayed in
 * the list.
 * 
 * <pre>
 * 'trnl'
 * length
 * array of strings
 * </pre>
 * 
 * @author moshew
 */
public class DTranslatorList extends DockCommandToNewton {

	public static final String COMMAND = "trnl";

	private final List<String> translators = new ArrayList<String>();

	/**
	 * Constructs a new command.
	 */
	public DTranslatorList() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFString[] items = new NSOFString[translators.size()];
		int i = 0;
		for (String translator : translators) {
			items[i++] = new NSOFString(translator);
		}
		NSOFPlainArray arr = new NSOFPlainArray(items);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(arr, data);
	}

	/**
	 * Add a translator.
	 * 
	 * @param translator
	 *            the translator.
	 */
	public void addTranslator(String translator) {
		translators.add(translator);
	}
}
