package client;

import managers.MainManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
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
                        /*
                        //msg = addToken(sc.nextLine(), TOKEN); // TODO ajouter token
                        msg = sc.nextLine();
                        //isRunning[0] = isAuthenticated(msg, TOKEN);
                        out.println(msg);
                        out.flush();
                        System.out.println("Envoi vers Serveur : " + msg);*/

                        msg = getTextToSend();
                        /*
                        //System.out.println("message a envoyer : " + getTextToSend());
                        try {
                            Thread.sleep(100);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

                        if (msg != null) {
                            out.println(msg);
                            out.flush();
                            System.out.println("Envoi vers Serveur : " + msg);
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
                        msg = in.readLine();
                        System.out.println("Réponse Serveur : " + msg);
                        getManager().setReceivedText(msg);
                        while(msg!=null && getIsRunning()){
                            //isRunning = isAuthenticated(msg, TOKEN);

                            //msg = removeToken(in.readLine(), TOKEN); // TODO enlever token
                            msg =in.readLine();
                            System.out.println("Réponse Serveur : " + msg);
                            getManager().setReceivedText(msg);
                        }
                        System.out.println("Serveur déconnecté");
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
            //receive.run();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopClient() {
        System.out.println("Client arrêté");
       // isRunning = false;

        setIsRunning(false);
        manager.setConnexionClientState(false);
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
