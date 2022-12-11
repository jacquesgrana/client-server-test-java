package server;

import managers.MainManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread {
    boolean isRunning = true;

    //boolean isNewMessage = false;

    String textToSend = null;
    BufferedReader in;
    PrintWriter out;
    ServerSocket serveurSocket ;
    Socket clientSocket;

    MainManager manager;

    public Server(MainManager manager) {
        this.manager = manager;
    }

    public void run() {



        //final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier

        try {
            System.out.println("Serveur démarré");
            serveurSocket = new ServerSocket(5000);
            manager.setInfosDisplayed("Serveur démarré");
            clientSocket = serveurSocket.accept();
            System.out.println("Client connecté");
            manager.setInfosDisplayed("Client connecté");
            manager.setConnexionServerState(true);
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

            /**
             * Send thread
             */
            Thread send = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {

                    while(getIsRunning()){
                        //msg = addToken(sc.nextLine(), TOKEN); // TODO ajouter token
                        // isRunning[0] = isAuthenticated(msg, TOKEN);

/*
                        msg = sc.nextLine();

                        if (msg != null) {
                            out.println(msg);
                            out.flush();
                            System.out.println("Envoi vers Client : " + msg);
                            msg = null;
                        }*/



                        //if(getIsNewMessage()) {
                            msg = getTextToSend();
                            //System.out.println("message a envoyer : " + getTextToSend());

                        /*
                            try {
                                Thread.sleep(100);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/

                            if (msg != null) {
                                out.println(msg);
                                out.flush();
                                System.out.println("Envoi vers Client : " + msg);
                                msg = null;
                                setTextToSend(null);
                            }
                            //setIsNewMessage(false);
                       // }
                    }
                }
            });
            send.start();
            //send.run();

            /**
             * Receive thread
             */
            Thread receive = new Thread(new Runnable() {
                String msg ;
                @Override
                public void run() {
                    System.out.println("Serveur démarré");
                    try {
                        msg = in.readLine();
                        System.out.println("Client : " + msg);
                        // TODO ajouter appel fonction du manager qui modifie l'affichage
                        getManager().setReceivedText(msg);

                        //tant que le client est connecté
                        while(msg!=null && getIsRunning()){
                            //isRunning = isAuthenticated(msg, TOKEN);
                            //msg = removeToken(in.readLine(), TOKEN);  // TODO enlever token
                            msg = in.readLine();
                            System.out.println("Client : " + msg);
                            // TODO ajouter appel fonction du manager qui modifie l'affichage
                            getManager().setReceivedText(msg);
                        }
                        //sortir de la boucle si le client a déconecté
                        System.out.println("Client déconnecté");
                        //fermer le flux et la session socket
                        out.close();
                        clientSocket.close();
                        serveurSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
            //receive.run();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        System.out.println("Serveur arrêté");
        //isRunning = false;
        setIsRunning(false);
        manager.setConnexionServerState(false);
        try {
            out.close();
            in.close();
            clientSocket.close();
            serveurSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void startServer() {
        System.out.println("Serveur lancé");
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
