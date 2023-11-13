import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GWackChannel {
    private static final Map<String, GWackConnectedClient> clients = new ConcurrentHashMap<>();
    private static final String SECRET = "3c3c4ac618656ae32b7f3431e75f7b26b1a14a87";

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: java GWackChannel <port>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("GWackChannel running on port: " + portNumber);

            while (listening) {
                new GWackConnectedClient(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

    private static class GWackConnectedClient extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public GWackConnectedClient(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String message) throws IOException {
            out.println(message);
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                String secretReceived = in.readLine();
                if (!SECRET.equals(secretReceived)) {
                    socket.close();
                    return;
                }
                out.println("Handshake successful. Waiting for name...");

                String nameReceived = in.readLine();
                boolean nameValid = nameReceived != null && !nameReceived.isEmpty();
                if (!nameValid || clients.containsKey(nameReceived)) {
                    socket.close();
                    return;
                }
                clientName = nameReceived;
                clients.put(clientName, this);
                broadcastClientList(); 

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    broadcastMessage("[" + clientName + "] " + inputLine);
                }
            } catch (IOException e) {
                System.err.println("Error handling client [" + clientName + "]: " + e.getMessage());
            } finally {
                if (clientName != null && !clientName.isEmpty()) {
                    clients.remove(clientName);
                    broadcastClientList(); 
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void broadcastMessage(String message) {
            for (GWackConnectedClient client : clients.values()) {
                try {
                    client.sendMessage(message);
                } catch (IOException e) {
                }
            }
        }

        private void broadcastClientList() {
            StringBuilder clientListMsg = new StringBuilder("START_CLIENT_LIST\n");
            for (String clientName : clients.keySet()) {
                clientListMsg.append(clientName).append("\n");
            }
            clientListMsg.append("END_CLIENT_LIST");

            broadcastMessage(clientListMsg.toString());
        }
    }
}