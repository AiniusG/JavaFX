package ainius.chat;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

class ClientHandler implements Runnable {

    private String name;
    private String roomName;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket s, String name, String roomName, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.roomName = roomName;
        this.s = s;
        this.isloggedin = true;
        Server.serverLog.add("Client " + name + " joined room " + roomName);
    }

    @Override
    public void run() {

        String received;
        while (true) {
            try {
                // receive the string
                received = dis.readUTF();

                String logEntry = this.name + " : " + received;
                Server.roomLogs.get(roomName).add(logEntry);
                Server.serverLog.add("Message in room " + roomName + " from " + this.name + ": " + received);
                System.out.println(logEntry);

                if (received.equals("logout")) {
                    this.isloggedin = false;
                    //this.s.close();
                    Server.rooms.get(roomName).remove(this);
                    Server.serverLog.add("Client " + this.name + " logged out from room " + roomName);
                    break;
                }

                // check if the message contains "#"
                if (received.contains("#")) {
                    // break the string into message and recipient part
                    StringTokenizer st = new StringTokenizer(received, "#");
                    String MsgToSend = st.nextToken();
                    String recipient = st.nextToken().trim();

                    // search for the recipient in the room's connected devices list
                    for (ClientHandler mc : Server.rooms.get(roomName)) {
                        // if the recipient is found, write on its output stream
                        if (mc.name.equals(recipient) && mc.isloggedin) {
                            mc.dos.writeUTF(this.name + " : " + MsgToSend);
                            break;
                        }
                    }
                } else {
                    // broadcast the message to all clients in the room
                    for (ClientHandler mc : Server.rooms.get(roomName)) {
                        // send the message to all active clients
                        if (mc.isloggedin) {
                            mc.dos.writeUTF(this.name + " : " + received);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // closing resources
            this.dis.close();
            this.dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
