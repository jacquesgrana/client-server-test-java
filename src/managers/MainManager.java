package managers;

import client.Client;
import server.Server;
import vues.MainWindowApp;

import java.awt.*;

public class MainManager {

    MainWindowApp window;
    Client client;
    Server server;

    final String TOKEN = "54gh7856dqd";

    boolean isClientLaunched = false, isServerLaunched = false;

    public MainManager() {
        window = new MainWindowApp(this);
    }

    public void clicButtonClient() {
        //System.out.println("manager : clic bouton client");

        this.isClientLaunched = !isClientLaunched;
        if (this.isClientLaunched) {
            isServerLaunched = false;
            window.disableButtonServer();
            window.enableButtonMessage();
            window.setTextButtonClient("Arrêter le Client");
            runClient();
        } else {
            window.enableButtonServer();
            window.disableButtonMessage();
            window.setTextButtonClient("Lancer le Client");
            stopClient();
        }

    }

    public void clicButtonServer() {
        //System.out.println("manager : clic bouton serveur");

        this.isServerLaunched = !isServerLaunched;

        if (this.isServerLaunched) {
            isClientLaunched = false;
            window.disableButtonClient();
            window.enableButtonMessage();
            window.setTextButtonServer("Arrêter le Serveur");
            runServer();
        } else {
            window.enableButtonClient();
            window.disableButtonMessage();
            window.setTextButtonServer("Lancer le Serveur");
            stopServer();
        }
    }

    public void clicButtonMessage() {
        //System.out.println("manager : clic bouton message");

        //System.out.println("message envoyé : " + window.getTextWriteMessage());
        if(isServerLaunched) {
            //server.setIsNewMessage(true);
            server.setTextToSend(window.getTextWriteMessage());
            //server.setTextToSend(null);
        }
        else if(isClientLaunched) {
            client.setTextToSend(window.getTextWriteMessage());
        }
        window.setTextWriteMessage("");
    }

    private void runClient() {
        System.out.println("manager : run client");
        // this.isClientLaunched = true;
        Client client = new Client(this);
        setClient(client);
        client.startClient();
/*
        EventQueue.invokeLater(new Runnable() {
            public void run() {
               Client client = new Client();
               setClient(client);
               client.startClient();
            }
        });*/

    }

    private void stopClient() {
        System.out.println("manager : stop client");
        //this.isClientLaunched = false;
        this.client.stopClient();

    }

    private void runServer() {
        System.out.println("manager : run server");
        Server server = new Server(this);
        setServer(server);
        server.startServer();
        //this.isServerLaunched = true;
        /*
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Server server = new Server();
                setServer(server);
                server.startServer();
            }
        });*/

        window.setFocusable(true);
        window.requestFocus();
    }

    private void stopServer() {
        System.out.println("manager : stop server");
        /*
        //this.isServerLaunched = false;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                getServer().stop();
            }
        });*/
        this.server.stopServer();
        //window.setFocusable(true);
    }

    public void setConnexionClientState(boolean isConnected) {
        if(isConnected) {
            window.setConnexionClientStateIcon(window.getImageIconConnexionState(Color.GREEN));
        }
        else {
            window.setConnexionClientStateIcon(window.getImageIconConnexionState(Color.RED));
        }
    }

    public void setConnexionServerState(boolean isConnected) {
        if(isConnected) {
            window.setConnexionServerStateIcon(window.getImageIconConnexionState(Color.GREEN));
        }
        else {
            window.setConnexionServerStateIcon(window.getImageIconConnexionState(Color.RED));
        }
    }
    public void stopApp() {
        System.exit(0);
    }
    public void setInfosDisplayed(String text) {
        window.setTextInfos(text);
    }


    public String addToken(String string) {
        return this.getTOKEN() + string;
    }

    public String removeToken(String string) {
        return string.replace(this.getTOKEN(),"");
    }

    // TODO améliorer, utiliser substring
    public boolean isAuthenticated(String msg) {
        System.out.println("isAuthenticated : " + msg.contains(this.getTOKEN()));
        return msg.substring(0, this.getTOKEN().length()).equals(this.getTOKEN());
        //return msg.contains(this.getTOKEN());
    }

    public void setReceivedText(String text) {
        window.setTextDisplayMessage(text);
    }

    public String getTextToSend() {
        return window.getTextWriteMessage();
    }

    public void setTextToSend(String text) {
        window.setTextWriteMessage(text);
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return this.client;
    }

    public String getTOKEN() {
        return TOKEN;
    }
}
