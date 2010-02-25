package net.sf.jncu.cdil;

import net.sf.jncu.cdil.mnp.MNPSerialPort;

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
public class CDILExample {

	/**
	 * 
	 */
	public CDILExample() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CDLayer layer = CDLayer.getInstance();
			CDPipe pipe;
			byte[] dataBuffer = new byte[256];
			int count;
			layer.startUp(); // Initialize the library
			pipe = layer.createMNPSerial(0, MNPSerialPort.BAUD_38400); // Create
			// a connection object
			pipe.startListening(); // Have that object listen for a
			// connection from a Newton device
			while (layer.getState() == CDState.LISTENING) // Wait for a connect
			// request
			{
				// If you are displaying a dialog box telling the user to
				// initiate a connection from a Newton OS device, you could
				// check for clicks on a Cancel button here.
			}
			if (layer.getState() == CDState.CONNECT_PENDING) {
				pipe.accept(); // Accept the connect request
				// TODO MyFnToGetDataToSend(dataBuffer);
				// TODO pipe.write(dataBuffer);
				// This step is optional. We'd execute it if we wanted to
				// ensure that there were 100 bytes available before calling
				// CD_Read, which would otherwise block.
				do {
					pipe.idle();
					count = pipe.getInput().available();
					// You could check for clicks on menus or buttons, or call
					// WaitNextEvent here.
				} while (count < 100);
				pipe.getInput().read(dataBuffer, 0, 100); // Assumes we expect 100
				// bytes back
				pipe.disconnect(); // Break the connection.
			}
			pipe.dispose(); // Delete the pipe object
			layer.shutDown(); // Close the library
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
