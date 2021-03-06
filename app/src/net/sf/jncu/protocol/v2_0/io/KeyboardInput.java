/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.swing.JNCUModuleDialog;

import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;

/**
 * Keyboard input for pass-through mode. <br>
 *
 * @author Moshe
 */
public class KeyboardInput extends IconModule implements WindowListener, KeyboardInputListener {

    /**
     * Windows EOL.
     */
    protected static final String CRLF = "\r\n";
    /**
     * Macintosh EOL.
     */
    protected static final String CR = "\r";
    /**
     * Unix EOL.
     */
    protected static final char LF = '\n';

    private enum State {
        /**
         * None.
         */
        NONE,
        /**
         * Initialised.
         */
        INITIALISED,
        /**
         * Listening for keyboard input.
         */
        INPUT,
        /**
         * Cancelled.
         */
        CANCELLED,
        /**
         * Finished.
         */
        FINISHED
    }

    /**
     * Wait to batch several "kbdc" as single "kbds" (unless the key is control
     * character).
     */
    private static final long TIMEOUT = 500;

    private State state = State.NONE;
    private KeyboardInputDialog dialog;
    private KeyboardTask task;
    private Timer timer;
    private StringBuilder buffer = new StringBuilder();

    /**
     * Constructs a new input.
     *
     * @param pipe  the pipe.
     * @param owner the owner window.
     */
    public KeyboardInput(CDPipe pipe, Window owner) {
        super(JNCUResources.getString("keyboardInput"), pipe, owner);
        setName("KeyboardInput-" + getId());

        this.state = State.INITIALISED;

        this.timer = new Timer();
        this.dialog = new KeyboardInputDialog(owner);
        dialog.addInputListener(this);
        dialog.addWindowListener(this);
    }

    /**
     * Show the input window.
     */
    @Override
    protected void runImpl() throws Exception {
        pipe.ping();
        state = State.INPUT;
        dialog.setVisible(true);
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        if (!isEnabled())
            return;

        super.commandReceived(command);

        final String cmd = command.getCommand();

        if (DOperationCanceled.COMMAND.equals(cmd)) {
            state = State.CANCELLED;
        }
    }

    @Override
    public void commandSent(DockCommandToNewton command) {
        if (!isEnabled())
            return;

        super.commandSent(command);

        final String cmd = command.getCommand();

        if (DOperationDone.COMMAND.equals(cmd)) {
            state = State.FINISHED;
        } else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
            state = State.CANCELLED;
        }
    }

    /**
     * Write the character to the Newton.
     *
     * @param c     the character.
     * @param flags the state flags.
     */
    public void writeChar(char c, int flags) {
        if (state != State.INPUT)
            return;

        DKeyboardChar cmd = new DKeyboardChar();
        cmd.setCharacter(c);
        cmd.setState(flags);
        write(cmd);
    }

    /**
     * Write the string to the Newton.
     *
     * @param s the string.
     */
    public void writeString(String s) {
        if (state != State.INPUT)
            return;

        DKeyboardString cmd = new DKeyboardString();
        cmd.setString(s);
        write(cmd);
    }

    @Override
    public void windowOpened(WindowEvent event) {
    }

    @Override
    public void windowClosing(WindowEvent event) {
        done();
    }

    @Override
    public void windowClosed(WindowEvent event) {
    }

    @Override
    public void windowIconified(WindowEvent event) {
    }

    @Override
    public void windowDeiconified(WindowEvent event) {
    }

    @Override
    public void windowActivated(WindowEvent event) {
    }

    @Override
    public void windowDeactivated(WindowEvent event) {
    }

    @Override
    public void charTyped(KeyEvent event) {
        if (event.getID() != KeyEvent.KEY_PRESSED)
            return;
        char keyChar = DKeyboardChar.toNewtonChar(event.getKeyChar(), event.getKeyCode());
        // Ignore unknown characters.
        if (keyChar == 0)
            return;

        int keyFlags = DKeyboardChar.toNewtonState(event.getModifiers());

        if (keyFlags == 0) {
            buffer.append(keyChar);
            startTimer();
        } else {
            flush();
            writeChar(keyChar, keyFlags);
        }
    }

    @Override
    public void stringTyped(String text) {
        int length = text.length();
        if (length == 0)
            return;

        buffer.append(text);
        flush();
    }

    @Override
    protected void done() {
        if (state == State.INPUT) {
            writeDone();
        }
        timer.cancel();
        dialog.removeInputListener(this);
        dialog.removeWindowListener(this);
        dialog.close();
        super.done();
    }

    @Override
    protected boolean isEnabled() {
        if (state == State.CANCELLED)
            return false;
        if (state == State.FINISHED)
            return false;
        return super.isEnabled();
    }

    /**
     * Flush the text buffer.
     */
    public synchronized void flush() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        final int length = buffer.length();
        if (length == 1) {
            char keyChar = buffer.charAt(0);
            buffer = new StringBuilder();
            writeChar(keyChar, 0);
        } else if (length > 1) {
            String text = buffer.toString();
            buffer = new StringBuilder();

            // Replace with Macintosh EOL.
            text = text.replaceAll(CRLF, CR);
            text = text.replace(LF, DKeyboardChar.RETURN);
            writeString(text);
        }
    }

    /**
     * Start the timer.
     */
    private void startTimer() {
        if (task == null) {
            task = new KeyboardTask(this);
            timer.schedule(task, TIMEOUT);
        }
    }

    /**
     * Get the input dialog.
     *
     * @return the dialog.
     */
    public KeyboardInputDialog getDialog() {
        return dialog;
    }

    /**
     * Tell the Newton we want to start typing.
     */
    public void initiate() {
        if (state == State.INITIALISED) {
            write(new DKeyboardPassthrough());
        }
    }

    @Override
    protected JNCUModuleDialog getProgress() {
        return null;
    }
}
