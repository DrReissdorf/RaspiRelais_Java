package remote.socket;

import remote.DataAndTools;
import java.net.Socket;

public class ControlSocket extends SocketComm {
    public ControlSocket(Socket socket) {
        super(socket);
    }

    public void deleteSocketFromList() {
        DataAndTools.controlSockets.remove(this);
    }
}
