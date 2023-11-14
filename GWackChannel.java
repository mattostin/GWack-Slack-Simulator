import java.io.*;
import java.net.*;
import java.util.*;

public class GWackChannel {
    private static final Map<String, GWackConnectedClient> clients = new HashMap<>();
    private static final String SECRET = "3c3c4ac618656ae32b7f3431e75f7b26b1a14a87";
    private static ServerSocket serverSocket;    
    private static GWackChannel chan;



    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Error: A port needed to be entered");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try {
            chan = new GWackChannel();
            serverSocket = new ServerSocket(portNumber);
            chan.serve();
        

            System.out.println(Inet4Address.getLocalHost().getHostAddress());
            System.out.println("GWackChannel running on port: " + portNumber);


        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

    public void serve() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                GWackConnectedClient newClient = new GWackConnectedClient(client);
                clients.put(newClient.getClientName(), newClient);
                newClient.start();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addClient() {

    }

    public void enqueueMessage(String message) {

    }

    public void dequeueAll() {

    }

    public static LinkedList<String> getClientList() {
         LinkedList<String> clientListMsg = new LinkedList<String>();
            clientListMsg.add("START_CLIENT_LIST");
            for (String clientName : clients.keySet()) {
                clientListMsg.add(clientName + "\n");
            }
            clientListMsg.add("END_CLIENT_LIST");

            return clientListMsg;
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

        public boolean isValid() {
            return true;
        }

        public String getClientName() {
            return clientName;
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
            LinkedList<String> clients = getClientList();
            broadcastMessage(clients.toString());
        }
    }
}