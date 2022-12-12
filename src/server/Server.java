package server;

import managers.MainManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Server extends Thread {
    boolean isRunning = true;

    //boolean isNewMessage = false;

    String textToSend = null;
    BufferedReader in;
    PrintWriter out;
    ServerSocket serveurSocket;
    Socket clientSocket;

    MainManager manager;

    public Server(MainManager manager) {
        this.manager = manager;
    }

    public void run() {


        //final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier
        if (getIsRunning()) {
            try {
                System.out.println("Serveur démarré");
                serveurSocket = new ServerSocket(5000);
                manager.setInfosDisplayed("Serveur démarré");

                clientSocket = serveurSocket.accept();
                // TODO fait planter quand le server socket est fermé

                System.out.println("Client connecté");
                manager.setInfosDisplayed("Client connecté");
                manager.setConnexionServerState(true);
                // TODO lancer classe type thread contenant les deux threads et l'ajouter a un set pour pouvoir gérer plusieurs clients
                out = new PrintWriter(clientSocket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                /**
                 * Send thread
                 */
                Thread send = new Thread(new Runnable() {
                    String msg;

                    @Override
                    public void run() {

                        while (getIsRunning()) {

                            msg = getTextToSend();

                            if (msg != null) {
                                System.out.println("Envoi vers Client : " + msg);
                                send(msg);
                                msg = null;
                                /*
                                msg = manager.addToken(msg); // **************************************************
                                out.println(msg);
                                out.flush();
                                msg = null;
                                setTextToSend(null);*/
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
                        System.out.println("Serveur démarré");
                        try {
                            //tant que le client est connecté
                            do {
                                msg = in.readLine();
                                if (manager.isAuthenticated(msg)) {
                                    msg = manager.removeToken(msg); // *********************************
                                    System.out.println("Message Client : " + msg);
                                    getManager().setReceivedText(msg);
                                } else {
                                    // TODO faire autre chose ? --> envoyer message de refus
                                    //setIsRunning(false);
                                    setTextToSend("Message refusé : non autorisé");
                                }

                            }
                            while (msg != null && getIsRunning());
                            //sortir de la boucle si le client a déconnecté
                            System.out.println("Client déconnecté");

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            /*
                            setIsRunning(false);
                            //fermer le flux et la session socket
                            try {
                                in.close();
                                out.close();
                                clientSocket.close();

                                serveurSocket.close();
                                if(serveurSocket.isClosed()) {
                                    System.out.println("Socket serveur égal à null");
                                }
                                else {
                                    System.out.println("Socket serveur  différent de null");
                                }
                                System.out.println("Serveur arrêté dans 1e finally");

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            */

                        }
                    }
                });
                receive.start();
                //receive.run();
            } catch (SocketException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //stopServer();
            }

        }
        else {
            System.out.println("Arrêt");
        }

    }

    public void send(String msg) {
        msg = manager.addToken(msg); // **************************************************
        out.println(msg);
        out.flush();
        //msg = null;
        setTextToSend(null);
    }

    public void stopServer() {
        System.out.println("Serveur arrêté");
        //isRunning = false;
        setIsRunning(false);
        manager.setConnexionServerState(false);
        manager.setInfosDisplayed("");
        //fermer le flux et la session socket
        if(in != null && out != null && clientSocket != null && serveurSocket != null) {
            send("QUIT");
        }

        try {
            if (in != null) in.close(); // ???
            if (out != null) out.close();
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serveurSocket != null) {
                serveurSocket.close();
            }
            System.out.println("Serveur arrêté vraiment");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }

    }

    public void startServer() {
        //System.out.println("Serveur lancé");
        //isRunning = true;
        setIsRunning(true);
        this.start();
    }

    public void pause(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e1) {

        }
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
/*
    public synchronized boolean getIsNewMessage() {
        return isNewMessage;
    }

    public synchronized void setIsNewMessage(boolean newMessage) {
        isNewMessage = newMessage;
    }*/

    public synchronized String getTextToSend() {
        return textToSend;
    }

    public synchronized void setTextToSend(String textToSend) {
        this.textToSend = textToSend;
    }
}
