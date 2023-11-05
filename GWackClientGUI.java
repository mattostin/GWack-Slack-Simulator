import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GWackClientGUI extends JFrame {
    // Declare your components as instance variables
    private JTextField nameField;
    private JTextField ipField;
    private JTextField portField;
    private JButton disconnectButton;
    private JButton sendButton;
    private JTextArea membersList;
    private JTextArea messagesArea;
    private JTextArea composeArea;

    private GWackClientNetworking clientNetworking;

    public GWackClientGUI() {
        // Set the frame title
        setTitle("GWack Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create a custom font for the application
        Font customFont = new Font("Arial", Font.PLAIN, 14);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        nameField = new JTextField(15);
        ipField = new JTextField(15);
        portField = new JTextField(6);
        disconnectButton = new JButton("Connect");

        // Apply custom font to text fields and button
        nameField.setFont(customFont);
        ipField.setFont(customFont);
        portField.setFont(customFont);
        disconnectButton.setFont(customFont);

        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(new JLabel("IP Address:"));
        topPanel.add(ipField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        topPanel.add(disconnectButton);

        // Members List Panel
        membersList = new JTextArea(15, 20);
        membersList.setEditable(false);
        JScrollPane membersScrollPane = new JScrollPane(membersList);

        // Messages Panel
        messagesArea = new JTextArea(15, 20);
        messagesArea.setEditable(false);
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);

        // Compose Panel
        JPanel composePanel = new JPanel();
        composePanel.setLayout(new BorderLayout());

        JLabel composeLabel = new JLabel("Compose");
        composeArea = new JTextArea(3, 20);
        sendButton = new JButton("Send");

        // Apply custom font to labels, text area, and button
        composeLabel.setFont(customFont);
        composeArea.setFont(customFont);
        sendButton.setFont(customFont);

        composePanel.add(composeLabel, BorderLayout.NORTH);
        composePanel.add(new JScrollPane(composeArea), BorderLayout.CENTER);
        composePanel.add(sendButton, BorderLayout.SOUTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Members Online Panel
        JPanel membersOnlinePanel = new JPanel();
        membersOnlinePanel.setLayout(new BorderLayout());

        membersOnlinePanel.add(new JLabel("Members Online"), BorderLayout.NORTH);
        membersOnlinePanel.add(membersScrollPane, BorderLayout.CENTER);

        // Messages Panel
        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BorderLayout());

        messagesPanel.add(new JLabel("Messages"), BorderLayout.NORTH);
        messagesPanel.add(messagesScrollPane, BorderLayout.CENTER);

        mainPanel.add(membersOnlinePanel, BorderLayout.WEST);
        mainPanel.add(messagesPanel, BorderLayout.CENTER);
        mainPanel.add(composePanel, BorderLayout.SOUTH);

        add(mainPanel);

        clientNetworking = new GWackClientNetworking(this);

        // Handle Disconnect Button
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });

        // Handle Send Button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        setVisible(true);
    }

    public void disconnect() {
        // Implement the disconnect functionality
    }

    public void sendMessage() {
        // Implement the send message functionality
    }

    // Other methods for updating the members list and displaying messages

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GWackClientGUI();
            }
        });
    }
}
