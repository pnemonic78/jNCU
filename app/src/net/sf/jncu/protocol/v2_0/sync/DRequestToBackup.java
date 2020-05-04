package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;
import net.sf.jncu.protocol.v2_0.session.DDesktopInfo;

/**
 * Sent by the Newton when the user clicked the Backup icon.<br>
 * Sent again by the Newton when the user clicked the Backup button.
 * <p>
 * The result object is an immediate value.
 * <p>
 * This command is not found in any published documentation.
 * <p>
 * The following behaviour was discovered when
 * {@link DDesktopInfo#PROTOCOL_VERSION} is 11 or greater:
 * <ol>
 * <li>User clicks "Backup" icon on Newton;
 * <li>Newton sends "ntic" command with TRUE;
 * <li>User clicks either the "Backup" or the "Cancel" button;
 * <li>Newton sends "ntic" command with FALSE;
 * <li>Newton does not send {@link DRequestToSync} command;
 * <li>Newton does not send correct {@link DSyncOptions} command in response to
 * a {@link DGetSyncOptions} request;
 * </ol>
 *
 * @author Moshe
 */
public class DRequestToBackup extends DockCommandFromNewtonScript<NSOFImmediate> {

    public static final String COMMAND = "ntic";

    /**
     * Creates a new command.
     */
    public DRequestToBackup() {
        super(COMMAND);
    }
}
