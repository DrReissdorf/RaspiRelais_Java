package remote.thread;

import remote.DataAndTools;

import java.net.*;
import java.util.Enumeration;

public class UdpServerThread extends Thread {

    public UdpServerThread() {
        System.out.println(getClass().getName()+" ==> Server started. Listening on port "+DataAndTools.udpPort);
    }

    public void run() {
        try {
            String received;
            String local_ip;
            DatagramSocket serverSocket = new DatagramSocket(DataAndTools.udpPort);
            byte[] receiveData;
            byte[] sendData;

            System.out.println(getClass().getName()+" ==> My ip is "+getLocalAddress());

            while (true) {
                receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                received = new String(receivePacket.getData()).trim();
                local_ip = getLocalAddress();

                System.out.println(getClass().getName()+" ==> Received UDP packet from "+receivePacket.getAddress());
                System.out.println(getClass().getName()+" ==> Message: "+received);

                if(received.equals("relay_control_reissdorf")) {
                    System.out.println(getClass().getName()+" ==> Valid request! Sending my ip: "+local_ip);
                    InetAddress IPAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();

                    sendData = local_ip.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
                    serverSocket.send(sendPacket);
                } else {
                    System.out.println(getClass().getName()+" ==> Invalid request! Dropping packet");
                }
            }
        } catch (Exception e) {
            System.err.println(getClass().getName()+" ==> Exception!");
            e.printStackTrace();
        }
    }

    private String getLocalAddress() throws SocketException {
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        while( ifaces.hasMoreElements() ) {
            NetworkInterface iface = ifaces.nextElement();
            Enumeration<InetAddress> addresses = iface.getInetAddresses();

            while( addresses.hasMoreElements() ) {
                InetAddress addr = addresses.nextElement();
                if( addr instanceof Inet4Address && !addr.isLoopbackAddress() )
                {
                    return addr.getHostAddress();
                }
            }
        }

        return null;
    }

}

