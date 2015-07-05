
package Lazorenko.Client.Controller;

import Lazorenko.Client.Logger.ClientLogToFile;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class Client implements ClientAndObserver {
    private static String ip;
    private static int port;
    private static final int timeout = 10000;
    private ClientLogToFile log = ClientLogToFile.getInstance();

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        final Socket s = new Socket();
        try {
            System.out.println("Waiting for server");
            s.connect(new InetSocketAddress(ip, port), timeout);
            //Check for connection
            if (s.isConnected()) {
                System.out.println("You are connected to chat server " + s.getRemoteSocketAddress().toString());
                log.getLogger().info("Client "+ s.getRemoteSocketAddress().toString()+" has connected to server");
            }
                System.out.println("Insert your username for this chatroom");
                //Client writes message
                speak(s);
                //Client reads message
                listen(s);
        } catch (IOException e) {
            e.printStackTrace();
            log.getLogger().error(e.getMessage()+"\n");
        }
    }

    @Override
    public void speak(final Socket s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter pw = new PrintWriter(s.getOutputStream());
                    Scanner console = new Scanner(System.in);
                    while (true){
                        String message = console.nextLine();
                        pw.println(message);
                        pw.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    log.getLogger().error(e.getMessage()+"\n");
                }

            }
        }).start();

    }

    @Override
    public void listen(final Socket s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = s.getInputStream();
                    InputStreamReader adapter = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(adapter);
                    String remoteMessage = br.readLine();
                    while (remoteMessage!=null){
                        System.out.println(remoteMessage);
                        remoteMessage = br.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    log.getLogger().error(e.getMessage()+"\n");
                }

            }
        }).start();

    }
}
