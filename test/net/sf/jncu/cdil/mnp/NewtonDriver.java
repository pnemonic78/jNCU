package net.sf.jncu.cdil.mnp;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

import javax.crypto.Cipher;

import jssc.SerialPort;
import jssc.SerialPortException;
import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDPing;
import net.sf.jncu.crypto.DESNewton;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.IDockCommand;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;
import net.sf.jncu.protocol.v2_0.app.DRequestToInstall;
import net.sf.jncu.protocol.v2_0.io.DSetStoreGetNames;
import net.sf.jncu.protocol.v2_0.query.DRefResult;
import net.sf.jncu.protocol.v2_0.session.DDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DPassword;
import net.sf.jncu.protocol.v2_0.session.DSetTimeout;
import net.sf.jncu.protocol.v2_0.session.DWhichIcons;
import net.sf.jncu.protocol.v2_0.session.DockingState;
import net.sf.jncu.util.NumberUtils;

/**
 * Pretend to be a Newton simulator.
 * 
 * @author moshe
 */
public class NewtonDriver implements MNPPacketListener, DockCommandListener {

	protected static final long PING_TIME = 10000L;

	private static final byte SEQUENCE_LR = 0;
	private static final byte SEQUENCE_RTDK = 1;
	private static final byte SEQUENCE_NAME = 2;
	private static final byte SEQUENCE_NINF = 3;
	private static final byte SEQUENCE_DRES = 4;
	private static final byte SEQUENCE_PASS = 5;
	private static final byte SEQUENCE_CMD = 6;

	private static final byte[] COMMAND_RTDK = { 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 'r', 't', 'd', 'k', 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x09 };
	private static final byte[] COMMAND_NAME = { 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 'n', 'a', 'm', 'e', 0x00, 0x00, 0x00, 'h', 0x00, 0x00, 0x00, '8', 0x11, 'g', (byte) 0xfd,
			'\'', 0x01, 0x00, 0x00, 0x00, 0x00, 'r', 'c', 'w', 0x00, 0x02, 0x00, 0x01, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, 0x01, '@', 0x00, 0x00,
			0x00, (byte) 0xf0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x04, (byte) 0xb1, (byte) 0xce, '?', 0x00, 0x00, 0x00, 'U', 0x00, 0x00, 0x00, 'U', 0x00, 0x00, 0x00,
			0x01, 0x00, 'Y', 0x00, 'o', 0x00, 'n', 0x00, 'i', 0x00, 't', 0x00, ' ', 0x00, 'N', 0x00, 'e', 0x00, 'l', 0x00, 'l', 0x00, 'i', 0x00, 'e', 0x00, ' ', 0x00, 'W', 0x00,
			'a', 0x00, 'i', 0x00, 's', 0x00, 'b', 0x00, 'e', 0x00, 'r', 0x00, 'g', 0x00, 0x00 };
	private static final byte[] COMMAND_NINF = { 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 'n', 'i', 'n', 'f', 0x00, 0x00, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x0a, 0x00, '1', ';', ':',
			0x00, 0x04, (byte) 0xb3, 'W' };
	private static final byte[] COMMAND_DRES = { 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 'd', 'r', 'e', 's', 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00 };
	private static final byte[] COMMAND_PASS = { 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 'p', 'a', 's', 's', 0x00, 0x00, 0x00, 0x08, (byte) 0xdc, (byte) 0xd6, (byte) 0xb6, 'N',
			(byte) 0x9e, '-', '~', 0x1d };
	private static final byte[] COMMAND_STOR = { 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 's', 't', 'o', 'r', 0x00, 0x00, 0x01, '<', 0x02, 0x05, 0x02, 0x06, 0x0a, 0x07, 0x04, 'n',
			'a', 'm', 'e', 0x07, 0x09, 's', 'i', 'g', 'n', 'a', 't', 'u', 'r', 'e', 0x07, 0x09, 'T', 'o', 't', 'a', 'l', 'S', 'i', 'z', 'e', 0x07, 0x08, 'U', 's', 'e', 'd', 'S',
			'i', 'z', 'e', 0x07, 0x04, 'k', 'i', 'n', 'd', 0x07, 0x04, 'i', 'n', 'f', 'o', 0x07, 0x08, 'r', 'e', 'a', 'd', 'O', 'n', 'l', 'y', 0x07, 0x0d, 's', 't', 'o', 'r', 'e',
			'p', 'a', 's', 's', 'w', 'o', 'r', 'd', 0x07, 0x0c, 'd', 'e', 'f', 'a', 'u', 'l', 't', 'S', 't', 'o', 'r', 'e', 0x07, 0x0c, 's', 't', 'o', 'r', 'e', 'v', 'e', 'r',
			's', 'i', 'o', 'n', 0x08, 0x12, 0x00, 'I', 0x00, 'n', 0x00, 't', 0x00, 'e', 0x00, 'r', 0x00, 'n', 0x00, 'a', 0x00, 'l', 0x00, 0x00, 0x00, (byte) 0xFf, 0x06,
			(byte) 0xF9, 'K', '0', 0x00, (byte) 0xff, 0x00, (byte) 0xdc, '8', (byte) 0x80, 0x00, (byte) 0xff, 0x00, (byte) 0x8c, 'L', (byte) 0x80, 0x08, 0x12, 0x00, 'I', 0x00,
			'n', 0x00, 't', 0x00, 'e', 0x00, 'r', 0x00, 'n', 0x00, 'a', 0x00, 'l', 0x00, 0x00, 0x06, 0x02, 0x07, 0x13, 'l', 'a', 's', 't', 'r', 'e', 's', 't', 'o', 'r', 'e', 'f',
			'r', 'o', 'm', 'c', 'a', 'r', 'd', 0x09, 0x0a, 0x00, (byte) 0xFf, (byte) 0x8d, 'U', 0x05, (byte) 0xbc, 0x00, 0x1a, 0x0a, 0x0a, 0x00, 0x1a, 0x00, 0x10, 0x06, 0x09,
			0x09, 0x02, 0x09, 0x03, 0x09, 0x04, 0x09, 0x05, 0x09, 0x06, 0x09, 0x07, 0x09, 0x08, 0x09, 0x09, 0x09, 0x0b, 0x08, 0x16, 0x00, '2', 0x00, 'M', 0x00, 'B', 0x00, ' ',
			0x00, 'P', 0x00, 'C', 0x00, 'M', 0x00, 'C', 0x00, 'I', 0x00, 'A', 0x00, 0x00, 0x00, (byte) 0xFf, ',', 'U', 0x08, (byte) 0x80, 0x00, (byte) 0xff, 0x00, 'r',
			(byte) 0x9c, 0x00, 0x00, (byte) 0xFf, 0x00, 'o', 'B', 0x00, 0x08, '&', 0x00, 'F', 0x00, 'l', 0x00, 'a', 0x00, 's', 0x00, 'h', 0x00, ' ', 0x00, 's', 0x00, 't', 0x00,
			'o', 0x00, 'r', 0x00, 'a', 0x00, 'g', 0x00, 'e', 0x00, ' ', 0x00, 'c', 0x00, 'a', 0x00, 'r', 0x00, 'd', 0x00, 0x00, 0x06, 0x01, 0x09, 0x0a, 0x0a, 0x0a, 0x0a, 0x00,
			0x10 };
	private static final byte[] COMMAND_SOUP = {};

	private SerialPort port;
	private boolean running = false;
	private DockingState state = DockingState.NONE;
	private MNPSerialPacketLayer packetLayer;
	private MNPCommandLayer commandLayer;
	private final Timer timer = new Timer();
	private CDPing ping;
	private PacketLogger logger;
	/** The password sent by the Desktop. */
	private transient long challengeDesktop;
	/** The ciphered password sent by the Desktop. */
	private transient long challengeDesktopCiphered;
	/** The Newton DES cryptography. */
	private DESNewton crypto;
	private int userCommand = 0;

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("args: port");
			System.exit(1);
			return;
		}

		NewtonDriver newton = new NewtonDriver();
		try {
			newton.setPortName(args[0]);
			newton.init();
			newton.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		} finally {
			newton.done();
		}
		System.exit(0);
	}

	public NewtonDriver() {
	}

	public void setPortName(String portName) throws SerialPortException {
		this.port = new SerialPort(portName);
	}

	public void init() throws Exception {
		logger = new PacketLogger('N');
		this.crypto = new DESNewton();
		crypto.init(Cipher.ENCRYPT_MODE);

		MNPSerialPort port = new MNPSerialPort(this.port);
		packetLayer = new MNPSerialPacketLayer(null, port);
		packetLayer.addPacketListener(this);
		packetLayer.setTimeout(Integer.MAX_VALUE);
		packetLayer.start();
		commandLayer = new MNPCommandLayer(packetLayer);
		commandLayer.addCommandListener(this);
		commandLayer.start();
	}

	public void run() throws Exception {
		running = true;

		next();
		next();
		while (running) {
			Thread.yield();
		}

		running = false;
	}

	private void done() {
		running = false;
		state = DockingState.DISCONNECTING;
		timer.cancel();
		if (commandLayer != null)
			commandLayer.close();
		if (packetLayer != null)
			packetLayer.close();
		state = DockingState.DISCONNECTED;
	}

	/**
	 * Send a packet.
	 * 
	 * @param packet
	 *            the packet.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws TimeoutException
	 *             if a timeout occurs.
	 */
	public void send(MNPPacket packet) throws IOException, TimeoutException {
		packetLayer.sendQueued(packet);
	}

	private void next() throws IOException, TimeoutException {
		switch (state) {
		case NONE:
			state = DockingState.HANDSHAKE_LR;
			break;
		case HANDSHAKE_LR:
			sendLR();
			break;
		case HANDSHAKE_RTDK:
			sendRequestToDock();
			break;
		case HANDSHAKE_DOCK:
			readInitiateDocking();
			break;
		case HANDSHAKE_NNAME:
			sendNewtonName();
			break;
		case HANDSHAKE_DINFO:
			readDesktopInfo();
			break;
		case HANDSHAKE_NINFO:
			sendNewtonInfo();
			break;
		case HANDSHAKE_ICONS:
			readWhichIcons();
			break;
		case HANDSHAKE_ICONS_RESULT:
			sendWhichIconsResult();
			break;
		case HANDSHAKE_TIMEOUT:
			readSetTimeout();
			break;
		case HANDSHAKE_PASS:
			sendPassword();
			break;
		case HANDSHAKE_PASS_REPLY:
			readPasswordReply();
			break;
		case HANDSHAKE_DONE:
			logger.log("next", null, "userCommand=" + userCommand);
			if (userCommand == 0) {
				handshakeDone();
			}
			break;
		default:
			throw new EOFException();
		}
	}

	private void sendLR() throws IOException, TimeoutException {
		MNPLinkRequestPacket packet = (MNPLinkRequestPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
		packet.setDataPhaseOpt((byte) 3);
		packet.setFramingMode((byte) 2);
		packet.setMaxInfoLength((short) 256);
		packet.setMaxOutstanding((byte) 8);
		packet.setProtocol((byte) 3);
		packet.setTransmitted(0);
		send(packet);
	}

	private void sendRequestToDock() throws IOException, TimeoutException {
		validateCommand(COMMAND_RTDK);
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		packet.setData(COMMAND_RTDK);
		packet.setSequence(SEQUENCE_RTDK);
		send(packet);
	}

	private void readInitiateDocking() {
	}

	private void sendNewtonName() throws IOException, TimeoutException {
		validateCommand(COMMAND_NAME);
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		packet.setData(COMMAND_NAME);
		packet.setSequence(SEQUENCE_NAME);
		send(packet);
	}

	private void readDesktopInfo() {
	}

	private void sendNewtonInfo() throws IOException, TimeoutException {
		validateCommand(COMMAND_NINF);
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		packet.setData(COMMAND_NINF);
		packet.setSequence(SEQUENCE_NINF);
		send(packet);
	}

	private void readWhichIcons() {
	}

	private void sendWhichIconsResult() throws IOException, TimeoutException {
		validateCommand(COMMAND_DRES);
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		packet.setData(COMMAND_DRES);
		packet.setSequence(SEQUENCE_DRES);
		send(packet);
	}

	private void readSetTimeout() {
	}

	private void sendPassword() throws IOException, TimeoutException {
		validateCommand(COMMAND_PASS);
		byte[] cmd = new byte[COMMAND_PASS.length];
		System.arraycopy(COMMAND_PASS, 0, cmd, 0, cmd.length);
		long key = challengeDesktopCiphered;
		byte[] keyBytes = NumberUtils.toBytes(key);
		System.arraycopy(keyBytes, 0, cmd, 16, 8);
		validateCommand(cmd);

		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		packet.setData(cmd);
		packet.setSequence(SEQUENCE_PASS);
		send(packet);
	}

	private void readPasswordReply() {
	}

	private void handshakeDone() throws IOException, TimeoutException {
		logger.log("handshakeDone", null, "userCommand=" + userCommand);
		userCommand++;
		state = DockingState.HANDSHAKE_DONE;
		if (ping == null)
			sendHello();
	}

	private void sendHello() throws IOException, TimeoutException {
		if (ping != null)
			ping.cancel();
		ping = new CDPing(commandLayer);
		timer.schedule(ping, PING_TIME, PING_TIME);
	}

	private void sendResult() throws IOException, TimeoutException {
		logger.log("sendResult", null, "userCommand=" + userCommand);
		userCommand++;
		validateCommand(COMMAND_DRES);
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		packet.setData(COMMAND_DRES);
		packet.setSequence(SEQUENCE_CMD);
		send(packet);
	}

	@Override
	public void packetAcknowledged(MNPPacket packet) {
		logger.log("a", packet, state);
	}

	@Override
	public void packetEOF() {
		logger.log("e", null, state);
	}

	@Override
	public void packetReceived(MNPPacket packet) {
		logger.log("r", packet, state);

		switch (packet.getType()) {
		case MNPPacket.LA:
			MNPLinkAcknowledgementPacket la = (MNPLinkAcknowledgementPacket) packet;

			switch (la.getSequence()) {
			case SEQUENCE_LR:
				throw new BadPipeStateException();
			case SEQUENCE_RTDK:
				if (state == DockingState.HANDSHAKE_RTDK) {
					state = DockingState.HANDSHAKE_DOCK;
					break;
				}
				throw new BadPipeStateException();
			case SEQUENCE_NAME:
				if (state == DockingState.HANDSHAKE_NNAME) {
					state = DockingState.HANDSHAKE_DINFO;
					break;
				}
				throw new BadPipeStateException();
			case SEQUENCE_NINF:
				if (state == DockingState.HANDSHAKE_NINFO) {
					state = DockingState.HANDSHAKE_ICONS;
					break;
				}
				throw new BadPipeStateException();
			case SEQUENCE_DRES:
				if (state == DockingState.HANDSHAKE_ICONS_RESULT) {
					state = DockingState.HANDSHAKE_TIMEOUT;
					break;
				}
				throw new BadPipeStateException();
			case SEQUENCE_PASS:
				if (state == DockingState.HANDSHAKE_PASS) {
					state = DockingState.HANDSHAKE_PASS_REPLY;
					break;
				}
				throw new BadPipeStateException();
			}
			break;
		case MNPPacket.LD:
			done();
			break;
		case MNPPacket.LR:
			sendLA(SEQUENCE_LR);
			if (state == DockingState.HANDSHAKE_LR) {
				state = DockingState.HANDSHAKE_RTDK;
			} else {
				throw new BadPipeStateException();
			}
			break;
		case MNPPacket.LT:
			MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;
			sendLA(lt.getSequence());
			DockCommandFactory factory = DockCommandFactory.getInstance();
			IDockCommandToNewton cmd = null;
			String name = null;
			if (userCommand <= 1) {
				cmd = (IDockCommandToNewton) factory.deserializeCommand(lt.getData());
				name = cmd.getCommand();
			}

			switch (state) {
			case HANDSHAKE_DOCK:
				if (DInitiateDocking.COMMAND.equals(name)) {
					state = DockingState.HANDSHAKE_NNAME;
					break;
				}
				throw new BadPipeStateException();
			case HANDSHAKE_DINFO:
				if (DDesktopInfo.COMMAND.equals(name)) {
					byte[] data = lt.getData();
					byte[] b = new byte[8];
					System.arraycopy(data, 24, b, 0, 8);
					this.challengeDesktop = NumberUtils.toLong(b);
					this.challengeDesktopCiphered = crypto.cipher(challengeDesktop);
					state = DockingState.HANDSHAKE_NINFO;
					break;
				}
				throw new BadPipeStateException();
			case HANDSHAKE_ICONS:
				if (DWhichIcons.COMMAND.equals(name)) {
					state = DockingState.HANDSHAKE_ICONS_RESULT;
					break;
				}
				throw new BadPipeStateException();
			case HANDSHAKE_TIMEOUT:
				if (DSetTimeout.COMMAND.equals(name)) {
					state = DockingState.HANDSHAKE_PASS;
					break;
				}
				throw new BadPipeStateException();
			case HANDSHAKE_PASS_REPLY:
				if (DPassword.COMMAND.equals(name)) {
					state = DockingState.HANDSHAKE_DONE;
					break;
				}
				if (DResult.COMMAND.equals(name)) {
					throw new BadPipeStateException((cmd == null) ? null : cmd.toString());
				}
				throw new BadPipeStateException();
			case HANDSHAKE_DONE:
				logger.log("r", packet, "userCommand=" + userCommand);
				if (userCommand == 1) {
					if (DRequestToInstall.COMMAND.equals(name)) {
						userCommand++;
					}
				}
				break;
			default:
				throw new BadPipeStateException();
			}
			break;
		}

		if (running) {
			try {
				next();
			} catch (Exception e) {
				e.printStackTrace();
				done();
			}
		}
	}

	@Override
	public void packetSent(MNPPacket packet) {
		logger.log("s", packet, state);
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		logger.log("cr", null, command);
	}

	@Override
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		logger.log("cs", null, command);
		final String name = command.getCommand();

		try {
			if (DRequestToInstall.COMMAND.equals(name)) {
				sendResult();
				// sendBig();
			} else if (DGetStoreNames.COMMAND.equals(name)) {
				sendStores();
			} else if (DSetStoreGetNames.COMMAND.equals(name)) {
				sendSoupNames();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (TimeoutException te) {
			te.printStackTrace();
		}
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
	}

	@Override
	public void commandEOF() {
	}

	private void sendLA(byte seq) {
		MNPLinkAcknowledgementPacket packet = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
		packet.setSequence(seq);
		try {
			send(packet);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (TimeoutException te) {
			te.printStackTrace();
		}
	}

	private void validateCommand(byte[] b) throws IOException {
		IDockCommandFromNewton cmd = (IDockCommandFromNewton) DockCommandFactory.getInstance().deserializeCommand(b);
		if (cmd == null)
			throw new NullPointerException();
	}

	private void sendBig() throws IOException, TimeoutException {
		logger.log("sendBig", null, "userCommand=" + userCommand);
		userCommand++;
		final int len = 500;
		NSOFPlainArray res = new NSOFPlainArray(len);
		for (int i = 0; i < len; i++)
			res.set(i, new NSOFInteger(i));

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(IDockCommand.COMMAND_PREFIX.getBytes());
		out.write(DRefResult.COMMAND.getBytes());
		ByteArrayOutputStream outRes = new ByteArrayOutputStream();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(res, outRes);
		DockCommandToNewton.htonl(outRes.size(), out);
		outRes.writeTo(out);
		switch (out.size() & 3) {
		case 1:
			out.write(0);
		case 2:
			out.write(0);
		case 3:
			out.write(0);
			break;
		}

		Iterable<MNPLinkTransferPacket> packets = MNPPacketFactory.getInstance().createTransferPackets(out.toByteArray());
		for (MNPLinkTransferPacket packet : packets) {
			send(packet);
		}
	}

	private void sendStores() throws IOException, TimeoutException {
		validateCommand(COMMAND_STOR);
		userCommand++;
		Iterable<MNPLinkTransferPacket> packets = MNPPacketFactory.getInstance().createTransferPackets(COMMAND_STOR);
		for (MNPLinkTransferPacket packet : packets) {
			send(packet);
		}
	}

	private void sendSoupNames() throws IOException, TimeoutException {
		validateCommand(COMMAND_SOUP);
		userCommand++;
		Iterable<MNPLinkTransferPacket> packets = MNPPacketFactory.getInstance().createTransferPackets(COMMAND_SOUP);
		for (MNPLinkTransferPacket packet : packets) {
			send(packet);
		}
	}
}
