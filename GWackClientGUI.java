import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class GWackClientGUI extends JFrame {
    private JTextField nameField;
    private JTextField ipField;
    private JTextField portField;
    private JButton connectButton;
    private JTextArea membersList;
    private JTextArea messagesArea;
    private JTextArea composeArea;
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

        add(mainPanel);
        clientNetworking = new GWackClientNetworking(this);
        // Corrected button functionaility 
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
        connected = false; 
    }

    public void connect() {
        connectButton.setText("Disconnect"); 
        connected = true; 
        
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GWackClientGUI();
            }
        });
    }
}
