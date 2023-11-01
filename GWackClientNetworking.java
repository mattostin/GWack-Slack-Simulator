import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class GWackClientNetworking {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private GWackClientGUI gui;

    public GWackClientNetworking(GWackClientGUI gui) {
        this.gui = gui;
    }

    public void connect(String host, int port, String username, String secret) {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream));

            out.println("SECRET");
            out.println(secret);
            out.println("NAME");
            out.println(username);

            // Handle the server response and member list updates
            // You will need to implement this part
            new ReadingThread().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (clientSocket != null && !clientSocket.isClosed()) {
            out.println(message);
        }
    }

    public List<String> getClientList() {
        // Implement logic to retrieve and update the member list
        // You will need to implement this part
        List<String> clients = new ArrayList<>();
        clients.add("User1");
        clients.add("User2");
        // Add more clients as needed
        return clients;
    }

    public boolean isConnected() {
        return clientSocket != null && !clientSocket.isClosed();
    }

    private class ReadingThread extends Thread {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    gui.newMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
