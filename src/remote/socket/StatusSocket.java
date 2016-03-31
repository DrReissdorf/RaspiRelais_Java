package remote.socket;

import remote.DataAndTools;
import java.net.Socket;

public class StatusSocket extends SocketComm {
    public StatusSocket(Socket socket) {
        super(socket);
    }

    public void deleteSocketFromList() {
        DataAndTools.statusSockets.remove(this);
    }
}
