package client;

import managers.MainManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client extends Thread{

    boolean isRunning = true;

    String textToSend = null;
    MainManager manager;
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    //Scanner sc = new Scanner(System.in);//pour lire à partir du clavier
    public Client(MainManager manager) {this.manager = manager;}

    public void run() {
        try {
            /*
             * les informations du serveur ( port et adresse IP ou nom d'hote
             * 127.0.0.1 est l'adresse local de la machine
             */
            System.out.println("Client démarré");

            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress("127.0.0.1",5000),1000);
            //clientSocket = new Socket("127.0.0.1",5000);
            System.out.println("Connexion au Serveur");
            manager.setInfosDisplayed("Client connecté");
            manager.setConnexionClientState(true);
            //flux pour envoyer
            out = new PrintWriter(clientSocket.getOutputStream());
            //flux pour recevoir
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            /**
             * Send thread
             */
            Thread send = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while(getIsRunning()){
                        //isRunning[0] = isAuthenticated(msg, TOKEN);
                        msg = getTextToSend();

                        if (msg != null) {
                            System.out.println("Envoi vers Serveur : " + msg);
                            msg = manager.addToken(msg); // **************************************************
                            out.println(msg);
                            out.flush();
                            msg = null;
                            setTextToSend(null);
                        }
                    }
                }
            });
            send.start();
            //send.run();

            /**
             * Receive thread
             */
            Thread receive = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        do {
                            msg =in.readLine();
                            if(manager.isAuthenticated(msg)) {
                                msg = manager.removeToken(msg); // ********************************************
                                if(!msg.equals("QUIT")) {
                                    System.out.println("Message Serveur : " + msg);
                                    getManager().setReceivedText(msg);
                                }
                                else {
                                    setIsRunning(false);
                                    stopClient();
                                    //getManager().clicButtonClient();
                                    manager.setClientButtonState(false);
                                }

                            }
                            else {
                                // TODO faire autre chose ? --> envoyer message de refus
                                //setIsRunning(false);
                                setTextToSend("Message refusé : non autorisé");
                            }

                        }
                        while(msg!=null && getIsRunning());
                        stopClient();
                        getManager().clicButtonClient();
                        /*
                        System.out.println("Serveur déconnecté");
                        in.close();
                        out.close();
                        //clientSocket.shutdownOutput(); // a tester
                        clientSocket.close();
                        System.out.println("Client arrêté");*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
            //receive.run();

        } catch (SocketException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopClient() {
        System.out.println("Client arrêté");
       // isRunning = false;

        setIsRunning(false);
        manager.setConnexionClientState(false);
        manager.setInfosDisplayed("");
        try {
            if (in != null) in.close();
            if (out != null) out.close();

            if (clientSocket != null && !clientSocket.isClosed()) {
                //clientSocket.shutdownOutput(); // a tester
                clientSocket.close();
            }
        } catch (SocketException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }

    }

    public void startClient() {
        System.out.println("Client lancé");
        //isRunning = true;
        setIsRunning(true);
        this.start();
    }

    public boolean getIsRunning() {
        return this.isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public MainManager getManager() {
        return manager;
    }

    public void setManager(MainManager manager) {
        this.manager = manager;
    }

    public synchronized String getTextToSend() {
        return textToSend;
    }

    public synchronized void setTextToSend(String textToSend) {
        this.textToSend = textToSend;
    }
}
