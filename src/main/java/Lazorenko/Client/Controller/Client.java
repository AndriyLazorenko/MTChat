
package Lazorenko.Client.Controller;

import Lazorenko.Client.Logger.ClientLogToFile;
import Lazorenko.Common.Messages.ChatMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class Client implements ClientAndObserver {
    protected static String ip;
    protected static int port;
    protected static final int timeout = 10000;
    protected ClientLogToFile log = ClientLogToFile.getInstance();
    protected static boolean isClientRegistered = false;
    protected static String clientName;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public Client() {
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
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        }
    }

    @Override
    public void speak(final Socket s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                    Scanner console = new Scanner(System.in);
                    while (true){
                        String message = console.nextLine();
                        ClientMessageProcessor processor = new ClientMessageProcessor(message,isClientRegistered);
                        ChatMessage chatMessage = processor.run();
                        if (chatMessage!=null) {
                            oos.writeObject(chatMessage);
                            oos.flush();
                        }
                    }
                } catch (IOException e) {
                    log.getLogger().error(e.getMessage()+"\n");
                    e.printStackTrace();
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
                    ObjectInputStream ois = new ObjectInputStream(is);
                    ChatMessage message = (ChatMessage) ois.readObject();
                    while (message!=null){

                        //Check if message is a command. If it is, it is processed. If not - the message is simply passed
                        if (message.isClientRegistered()) {
                            isClientRegistered = true;
                            clientName = message.getUsername();
                        }
                        else if (message.getFile()!=null){
                            ClientFileReceiving fileReceiving = new ClientFileReceiving(message);
                            fileReceiving.receive();
                        }
                        else {
                            StringBuilder formattedMessage = null;
                            formattedMessage = formattedMessage.append(message.getIp())
                                    .append(":") .append(":") .append(message.getPort())
                                    .append(" -> ") .append(message.getUsername())
                                    .append(" says: ") .append(message.getSimpleMessage());
                            System.out.println(formattedMessage.toString());
                        }
                    }
                } catch (IOException e) {
                    log.getLogger().error(e.getMessage()+"\n");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    log.getLogger().error(e.getMessage()+"\n");
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
