package ainius.chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    final static int ServerPort = 1234;

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner scn = new Scanner(System.in);

        // getting localhost IP
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // Ask user for their name
        System.out.println(dis.readUTF());
        String name = scn.nextLine();
        dos.writeUTF(name);

        // Ask user for the room name
        System.out.println(dis.readUTF());
        String roomName = scn.nextLine();
        dos.writeUTF(roomName);

        // sendMessage thread
        Thread sendMessage = new Thread(() -> {
            while (true) {
                // read the message to deliver
                String msg = scn.nextLine();

                try {
                    // write on the output stream
                    dos.writeUTF(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    // read the message sent to this client
                    String msg = dis.readUTF();
                    System.out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();
        readMessage.start();
    }
}
