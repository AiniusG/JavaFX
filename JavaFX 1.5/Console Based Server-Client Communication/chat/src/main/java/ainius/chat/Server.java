package ainius.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    // Map to store rooms and their active clients
    static Map<String, Vector<ClientHandler>> rooms = new HashMap<>();
    static Map<String, List<String>> roomLogs = new HashMap<>();
    static List<String> serverLog = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);

        // Create a separate thread to listen for admin commands
        Thread adminThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("generate_logs")) {
                    generateLogFiles();
                }
                // Additional server commands can be added here
            }
        });
        adminThread.start();

        Socket s;

        // running infinite loop for getting client requests
        while (true) {
            // Accept the incoming request
            s = ss.accept();

            serverLog.add("New client request received: " + s);
            System.out.println("New client request received: " + s);

            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // Ask for the client's name
            dos.writeUTF("Enter your name:");
            String name = dis.readUTF();

            // Ask for the room name
            dos.writeUTF("Enter the room name:");
            String roomName = dis.readUTF();

            serverLog.add("Creating a new handler for client: " + name + " in room: " + roomName);
            System.out.println("Creating a new handler for client: " + name + " in room: " + roomName);

            // Create a new handler object for handling this request
            ClientHandler mtch = new ClientHandler(s, name, roomName, dis, dos);

            // Add the client to the room
            rooms.putIfAbsent(roomName, new Vector<>());
            roomLogs.putIfAbsent(roomName, Collections.synchronizedList(new ArrayList<>()));
            rooms.get(roomName).add(mtch);

            // Create a new Thread with this object
            Thread t = new Thread(mtch);

            serverLog.add("Adding this client to room: " + roomName);
            System.out.println("Adding this client to room: " + roomName);

            // start the thread
            t.start();
        }
    }

    public static void generateLogFiles() {
        for (String roomName : roomLogs.keySet()) {
            String filename = "log_" + roomName + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (String entry : roomLogs.get(roomName)) {
                    writer.write(entry);
                    writer.newLine();
                }
                System.out.println("Log file generated for room " + roomName + ": " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String serverLogFilename = "server_log.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(serverLogFilename))) {
            for (String entry : serverLog) {
                writer.write(entry);
                writer.newLine();
            }
            System.out.println("Server log file generated: " + serverLogFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

