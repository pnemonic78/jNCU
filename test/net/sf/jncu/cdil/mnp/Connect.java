package net.sf.jncu.cdil.mnp;

import java.io.IOException;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDState;

/**
 * <code>
CD_Handle pipe;<br>
CD_State state;<br>
char dataBuffer[256];<br>
long count;<br>
CD_Startup(); // Initialize the library<br>
CD_CreateADSP(&pipe, NULL, NULL); // Create a connection object<br>
CD_StartListening(pipe); // Have that object listen for a<br>
// connection from a Newton device<br>
while (CD_GetState(pipe) == kCD_Listening) //Wait for a connect request<br>
{<br>
// If you are displaying a dialog box telling the user to<br>
// initiate a connection from a Newton OS device, you could<br>
// check for clicks on a Cancel button here.<br>
}<br>
if (CD_GetState(pipe) == kCD_ConnectPending)<br>
{<br>
CD_Accept(pipe); // Accept the connect request<br>
MyFnToGetDataToSend(dataBuffer);<br>
CD_Write(pipe, dataBuffer, sizeof(dataBuffer));<br>
// This step is optional. We'd execute it if we wanted to<br>
// ensure that there were 100 bytes available before calling<br>
// CD_Read, which would otherwise block.<br>
do<br>
{<br>
CD_Idle(pipe);<br>
CD_BytesAvailable(pipe, &count);<br>
// You could check for clicks on menus or buttons, or call<br>
// WaitNextEvent here.<br>
}<br>
while (count<100);<br>
CD_Read(pipe, dataBuffer, 100); // Assumes we expect 100 bytes back<br>
CD_Disconnect(pipe); // Break the connection.<br>
}<br>
CD_Dispose(pipe); // Delete the pipe object<br>
CD_Shutdown(); // Close the library
 * </code>
 * 
 * @author moshew
 */
public class Connect {

	/**
	 * Test connection.
	 */
	public Connect() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		String arg0 = args[0];

		try {
			CDLayer layer = CDLayer.getInstance();
			CDPipe pipe;
			// Initialize the library
			layer.startUp();
			// Create a connection object
			int portIndex = 0;
			String portName = layer.getSerialPortName(portIndex);
			while (!arg0.equals(portName)) {
				portIndex++;
				portName = layer.getSerialPortName(portIndex);
			}
			pipe = layer.createMNPSerial(portIndex, MNPSerialPort.BAUD_38400);

			try {
				// Have that object listen for a connection from a Newton device
				pipe.startListening();
				// Wait for a connect request
				while (layer.getState() == CDState.LISTENING) {
					// If you are displaying a dialog box telling the user to
					// initiate a connection from a Newton OS device, you could
					// check for clicks on a Cancel button here.
					Thread.yield();
				}
				if (layer.getState() == CDState.CONNECT_PENDING) {
					pipe.accept(); // Accept the connect request

					pipe.idle();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				pipe.disconnect(); // Break the connection.

				pipe.dispose(); // Delete the pipe object
				layer.shutDown(); // Close the library
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println("Exit");
		System.exit(0);
	}
}
