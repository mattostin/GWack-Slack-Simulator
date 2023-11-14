import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GWackClientGUI extends JFrame {

    private JTextField nameField;
    private JTextField ipField;
    private JTextField portField;
    private JButton connectButton;
    private JTextArea membersList;
    private JTextArea messagesArea;
    private JTextArea composeArea;
    private static GWackClientGUI gui;
    private GWackClientNetworking clientNetworking;

    private boolean connected = false;

    public GWackClientGUI() {
        setTitle("GWack Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        Font customFont = new Font("Arial", Font.PLAIN, 14);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        nameField = new JTextField(15);
        ipField = new JTextField(15);
        portField = new JTextField(6);
        connectButton = new JButton("Connect");

        nameField.setFont(customFont);
        ipField.setFont(customFont);
        portField.setFont(customFont);
        connectButton.setFont(customFont);

        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(new JLabel("IP Address:"));
        topPanel.add(ipField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        topPanel.add(connectButton);

        membersList = new JTextArea(15, 20);
        membersList.setEditable(false);
        JScrollPane membersScrollPane = new JScrollPane(membersList);

        messagesArea = new JTextArea(15, 20);
        messagesArea.setEditable(false);
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);
        JPanel composePanel = new JPanel();
        composePanel.setLayout(new BorderLayout());

        JLabel composeLabel = new JLabel("Compose");
        composeArea = new JTextArea(3, 20);
        JButton sendButton = new JButton("Send");

        composeLabel.setFont(customFont);
        composeArea.setFont(customFont);
        sendButton.setFont(customFont);

        composePanel.add(composeLabel, BorderLayout.NORTH);
        composePanel.add(new JScrollPane(composeArea), BorderLayout.CENTER);
        composePanel.add(sendButton, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel membersOnlinePanel = new JPanel();
        membersOnlinePanel.setLayout(new BorderLayout());

        membersOnlinePanel.add(new JLabel("Members Online"), BorderLayout.NORTH);
        membersOnlinePanel.add(membersScrollPane, BorderLayout.CENTER);

        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BorderLayout());

        messagesPanel.add(new JLabel("Messages"), BorderLayout.NORTH);
        messagesPanel.add(messagesScrollPane, BorderLayout.CENTER);

        mainPanel.add(membersOnlinePanel, BorderLayout.WEST);
        mainPanel.add(messagesPanel, BorderLayout.CENTER);
        mainPanel.add(composePanel, BorderLayout.SOUTH);

        sendButton.addActionListener((e) -> {
            sendMessage();
        });

        composeArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    sendMessage();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        add(mainPanel);
        connectButton.addActionListener((e) -> {
            if (connected) {
                disconnect();
            } else {
                connect();
            }
        });
        setVisible(true);
    }

    public void disconnect() {
        connectButton.setText("Connect");
        clientNetworking.disconnect();
        connected = false;
    }

    public void sendMessage() {
        clientNetworking.writeMessage(composeArea.getText());
        composeArea.setText("");
    }

    public void updateClients(String clientsList) {
        membersList.setText(clientsList);
    }

    public void newMessage(String msg) {

        messagesArea.append(msg + "\n");

    }

    public void connect() {
        connectButton.setText("Disconnect");
        try{
        clientNetworking = new GWackClientNetworking(this, ipField.getText(), Integer.parseInt(portField.getText()),
                nameField.getText());
        }
        catch (Exception e){
            return;
        }
        connected = true;

    }

    public void showError(String error) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, error, "Error cannot connect to server", JOptionPane.ERROR_MESSAGE);
        });
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui = new GWackClientGUI();
            }
        });
    }
}
