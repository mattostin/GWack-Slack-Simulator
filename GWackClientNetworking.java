import java.io.*;
import java.net.*;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class GWackClientNetworking {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private GWackClientGUI gui;

    public GWackClientNetworking(GWackClientGUI gui, String host, int port, String username) {
        try {
            InetAddress.getByName(host);
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            this.gui = gui;

            out.println("SECRET");
            out.println("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87");
            out.println("NAME");
            out.println(username);
            out.flush();

            new ReadingThread().start();
         } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gui,"Invalid Port Number","ERROR",JOptionPane.ERROR_MESSAGE);
            
            
        }
    }

    public void disconnect() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gui, "Error disconnecting from the server", "Disconnect Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void writeMessage(String message) {
        if (clientSocket != null && !clientSocket.isClosed()) {
            out.println(message);
            out.flush();
        }
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
                    System.out.println(message);
                    if(message.equals("START_CLIENT_LIST")){
                        message = in.readLine();
                        String clients = "";
                        while (!message.equals("END_CLIENT_LIST")) {
                            clients += message + "\n";
                            message = in.readLine();
                        }
                        gui.updateClients(clients);
                        continue;
                    }
                    gui.newMessage(message);
                }
            } catch (IOException e) {
                if(!clientSocket.isClosed()){
                    System.out.println("Lost connection");
                }
            }finally{
                disconnect();
            }
        }

    }

}
