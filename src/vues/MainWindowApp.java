package vues;

import managers.MainManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class MainWindowApp extends JFrame {

    private MainManager manager;
    private JPanel container = new JPanel();
    private JPanel panelTitle = new JPanel();
    private JPanel panelLabelDisplayMessage = new JPanel();
    private JPanel panelLabelWriteMessage = new JPanel();
    private JPanel panelDisplayMessage = new JPanel();
    private JPanel panelWriteMessage = new JPanel();

    private JPanel panelConnexionState = new JPanel();

    private JPanel panelButtons = new JPanel();
    private JLabel labelInfos = new JLabel("Infos :");
    private JLabel labelDisplayMessage = new JLabel("Réception :");
    private JLabel labelWriteMessage = new JLabel("Envoi :");

    private JLabel labelConnexionClient = new JLabel();
    private JLabel labelConnexionServer = new JLabel();
    private JTextArea compDisplayMessage = new JTextArea(1,44);
    private JTextArea compWriteMessage = new JTextArea(1,44);
    private JButton buttonRunClient = new JButton("Lancer le Client");
    private JButton buttonRunServer = new JButton("Lancer le Serveur");
    private JButton buttonSendMessage = new JButton("Envoyer un Message");
    public MainWindowApp(MainManager mainManager) {
        this.manager = mainManager;

        this.setTitle("Fenêtre");
        this.setSize(600, 420);
        this.setResizable(false);
        this.setBackground(new Color(20,20,20));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setDefaultLookAndFeelDecorated(true);

        this.getContentPane().add(container);
        container.setSize(new Dimension(600, 400));
        container.setBackground(new Color(20,20,20));
        GridLayout layout = new GridLayout();
        container.setLayout(null);
        panelTitle.setBackground(new Color(20,20,20));
        panelTitle.setBounds(50,30,500,20);
        labelInfos.setForeground(Color.ORANGE);
        panelTitle.add(labelInfos, null);
        container.add(panelTitle);

        panelLabelDisplayMessage.setBackground(new Color(20,20,20));
        panelLabelDisplayMessage.setBounds(50,75,500,20);
        labelDisplayMessage.setForeground(Color.ORANGE);
        panelLabelDisplayMessage.add(labelDisplayMessage, null);
        container.add(panelLabelDisplayMessage);

        panelDisplayMessage.setBackground(new Color(80,80,80));
        panelDisplayMessage.setBounds(50,100,500,28);
        panelDisplayMessage.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2, true));

        container.add(panelDisplayMessage);

        compDisplayMessage.setText("");
        compDisplayMessage.setBackground(new Color(80,80,80));
        compDisplayMessage.setForeground(Color.ORANGE);
        compDisplayMessage.setEditable(false);
        compDisplayMessage.setText("> ");
        panelDisplayMessage.add(compDisplayMessage);

        panelLabelWriteMessage.setBackground(new Color(20,20,20));
        panelLabelWriteMessage.setBounds(50,155,500,20);
        labelWriteMessage.setForeground(Color.ORANGE);
        panelLabelWriteMessage.add(labelWriteMessage, null);
        container.add(panelLabelWriteMessage);

        panelWriteMessage.setBackground(new Color(80,80,80));
        panelWriteMessage.setBounds(50,180,500,28);
        panelWriteMessage.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2, true));

        container.add(panelWriteMessage);

        compWriteMessage.setText("");
        compWriteMessage.setBackground(new Color(80,80,80));
        compWriteMessage.setForeground(Color.ORANGE);
        compWriteMessage.setEditable(true);
        compWriteMessage.setText("> ");
        panelWriteMessage.add(compWriteMessage);

        panelButtons.setBackground(new Color(20,20,20));
        panelButtons.setBounds(50,260,500,40);

        container.add(panelButtons);

        buttonRunClient.setBackground(new Color(80,80,80));
        buttonRunClient.setForeground(Color.ORANGE);
        buttonRunClient.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        buttonRunServer.setBackground(new Color(80,80,80));
        buttonRunServer.setForeground(Color.ORANGE);
        buttonRunServer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        buttonSendMessage.setBackground(new Color(80,80,80));
        buttonSendMessage.setForeground(Color.ORANGE);
        buttonSendMessage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        buttonRunClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {clicButtonClient();}
        });

        buttonRunServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {clicButtonServer();}
        });

        buttonSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {clicButtonMessage();}
        });

        buttonSendMessage.setEnabled(false);
        panelButtons.add(buttonRunClient);
        panelButtons.add(buttonRunServer);
        panelButtons.add(buttonSendMessage);

        panelConnexionState.setBackground(new Color(20,20,20));
        panelConnexionState.setBounds(50,310,500,60);
        container.add(panelConnexionState);

        //labelConnexionClient.setSize(40,40);
        labelConnexionClient.setBackground(new Color(20,20,20));
        labelConnexionClient.setBounds(190,0,40,40);

        ImageIcon imageIconClient = getImageIconConnexionState(Color.RED);
        labelConnexionClient.setIcon(imageIconClient);

        labelConnexionServer.setSize(40,40);
        labelConnexionServer.setBackground(new Color(60,60,60));
        labelConnexionServer.setBounds(240,0,40,40);

        ImageIcon imageIconServer = getImageIconConnexionState(Color.RED);
        labelConnexionServer.setIcon(imageIconServer);

        panelConnexionState.add(labelConnexionClient);
        panelConnexionState.add(labelConnexionServer);
        //pack();
        setVisible(true);
    }

    public ImageIcon getImageIconConnexionState(Color color) {
        BufferedImage imageConnexionServer = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dServer = (Graphics2D) imageConnexionServer.createGraphics();
        g2dServer.setColor(color);
        g2dServer.fillOval(0,0,40,40);
        ImageIcon imageIconServer = new ImageIcon(imageConnexionServer);
        return imageIconServer;
    }

    public void setConnexionClientStateIcon(ImageIcon icon) {
        labelConnexionClient.setIcon(icon);
        this.repaint();
    }

    public void setConnexionServerStateIcon(ImageIcon icon) {
        labelConnexionServer.setIcon(icon);
        this.repaint();
    }

    public void clicButtonClient() {
        //System.out.println("fenêtre : clic bouton client");
        manager.clicButtonClient();
    }

    public void clicButtonServer() {
        //System.out.println("fenêtre : clic bouton serveur");
        manager.clicButtonServer();
    }

    public void clicButtonMessage() {
        //System.out.println("fenêtre : clic bouton message");
        manager.clicButtonMessage();
    }

    public void setTextInfos(String text) {
        this.labelInfos.setText("Infos : " + text);
    }

    public void setTextDisplayMessage(String text) {
        compDisplayMessage.setText("> " + text);
    }
    public void setTextWriteMessage(String text) {
        compWriteMessage.setText("> " + text);
    }

    public String getTextWriteMessage() {
        return compWriteMessage.getText().substring(2);
    }

    public void enableButtonClient() {
        this.buttonRunClient.setEnabled(true);
    }
    public void disableButtonClient() {
        this.buttonRunClient.setEnabled(false);
    }
    public void setTextButtonClient(String text) {
        this.buttonRunClient.setText(text);
    }

    public void enableButtonServer() {
        this.buttonRunServer.setEnabled(true);
    }
    public void disableButtonServer() {
        this.buttonRunServer.setEnabled(false);
    }
    public void setTextButtonServer(String text) {
        this.buttonRunServer.setText(text);
    }

    public void enableButtonMessage() {
        this.buttonSendMessage.setEnabled(true);
    }
    public void disableButtonMessage() {
        this.buttonSendMessage.setEnabled(false);
    }
    public void setTextButtonMessage(String text) {
        this.buttonSendMessage.setText(text);
    }
}
