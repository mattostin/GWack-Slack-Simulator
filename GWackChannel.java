import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GWackChannel {
    private static  String SECRET = "3c3c4ac618656ae32b7f3431e75f7b26b1a14a87";
    private int port;
    private ServerSocket serverSocket;
    private  List<GWackConnectedClient> connectedClients = Collections.synchronizedList(new ArrayList<>());

    public GWackChannel(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        System.out.println("GWackChannel started on port: " + port);
    }

    public void serve() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                GWackConnectedClient connectedClient = new GWackConnectedClient(clientSocket);
                connectedClients.add(connectedClient);
                connectedClient.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message) {
        synchronized (connectedClients) {
            for (GWackConnectedClient client : connectedClients) {
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(GWackConnectedClient client) {
        connectedClients.remove(client);
        updateClientList();
    }

    private void updateClientList() {
        StringBuilder clientListMessage = new StringBuilder("START_CLIENT_LIST\n");
        for (GWackConnectedClient client : connectedClients) {
            clientListMessage.append(client.getClientName()).append("\n");
        }
        clientListMessage.append("END_CLIENT_LIST\n");
        broadcastMessage(clientListMessage.toString());
    }

    private class GWackConnectedClient extends Thread {
        private  Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public GWackConnectedClient(Socket socket) throws IOException {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                String secretLine = in.readLine();
                String nameLine = in.readLine();

                if (!secretLine.equals(SECRET)) {
                    socket.close();
                    return;
                }

                clientName = nameLine.substring(5); 
                updateClientList();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    broadcastMessage("[" + clientName + "] " + inputLine);
                }
            } catch (IOException e) {
                System.out.println(clientName + " disconnected.");
            } finally {
                removeClient(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public String getClientName() {
            return clientName;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java GWackChannel <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        try {
            GWackChannel server = new GWackChannel(port);
            server.serve();
        } catch (IOException e) {
            System.out.println("Could not start server on port: " + port);
            e.printStackTrace();
        }
    }
}