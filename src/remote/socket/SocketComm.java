package remote.socket;

import remote.DataAndTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractList;
import java.util.ArrayList;

public class SocketComm {
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader bufferedReader;

    protected SocketComm(Socket socket) {
        this.socket = socket;
        try {
            pw = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String text) {
        pw.println(text);
        pw.flush();
    }

    public String receive() {
        try {
            return bufferedReader.readLine().trim();
        } catch (IOException e) {
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }
        return null;
    }

    public String getIP() {
        return socket.getInetAddress().getHostAddress();
    }

    public String getHostname() {
        return socket.getInetAddress().getHostName();
    }
}
