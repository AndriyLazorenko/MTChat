
package Lazorenko.Client.Controller;

import Lazorenko.Client.Logger.ClientLogToFile;
import Lazorenko.Client.SerializationService.ReceiveObject;
import Lazorenko.Common.Messages.ChatMessage;
import Lazorenko.Common.Messages.MessageFormatter;

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
    protected Thread speaking;
    volatile InputStream consoleInput = System.in;
    volatile static boolean askToAcceptFile = false;

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
                log.getLogger().info("Client "+ s.getRemoteSocketAddress().toString()+" has connected to server"
                        + "\n");
            }
                System.out.println("Insert your username for this chatroom");
                //Client writes message
                speak(s);
                //Client reads message
                listen(s);
        } catch (IOException e) {
            log.getLogger().error(e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    class ClientSpeak implements Runnable {

        Socket s;

        public ClientSpeak(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                Scanner console = new Scanner(consoleInput);
                while (s.isConnected()) {
                    if (askToAcceptFile){
                        sendMessage(oos, console,askToAcceptFile);
                    }
                    else {
                        sendMessage(oos, console,askToAcceptFile);
                    }
                }
            }catch (IOException e) {
                log.getLogger().error(e.getMessage() + "\n");
                e.printStackTrace();
            }
        }

        private void sendMessage(ObjectOutputStream oos, Scanner console, boolean file) {
            ClientMessageProcessor processor;
            String message = console.nextLine();
            if (message != null) {
                if (file){
                    processor = new FileClientMessageProcessor(message, isClientRegistered);
                    askToAcceptFile = false;
                }
                else {
                    processor = new DefaultClientMessageProcessor(message, isClientRegistered, consoleInput);
                }
                ChatMessage chatMessage = processor.run();
                try {
                    oos.writeObject(chatMessage);
                    oos.flush();
                } catch (IOException e) {
                    log.getLogger().error(e.getMessage() + "\n");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void speak(final Socket s) {
        speaking = new Thread(new ClientSpeak(s));
        speaking.start();
    }

    @Override
    public void listen(final Socket s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = s.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    while (s.isConnected()){
                        ChatMessage message = (ChatMessage) ois.readObject();
                        //Check if message is a command. If it is, it is processed.
                        // If not - the message is simply passed
                        processMessage(message);
                    }
                } catch (IOException e) {
                    log.getLogger().error(e.getMessage()+"\n");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    log.getLogger().error(e.getMessage() + "\n");
                    e.printStackTrace();
                }
            }

            private void processMessage(ChatMessage message) {
                if (message.isClientRegistered()) {
                    isClientRegistered = true;
                    clientName = message.getUsername();
                }

                else if (message.isAskClientToAcceptFile()){
                    System.out.println("Type 'internal' to continue");
                    //TODO try to debug the confusing part. Think of a workaround architecturally first
                    askToAcceptFile = true;
                    System.out.println("Do you want to accept picture of size " + message.getFileSize()
                            + " coming from " + message.getUsername() +" ? y/n");
                }
                else if (message.getFile()!=null) {
                    System.out.println("Type 'internal' to continue");
                    synchronized (consoleInput){
                        ReceiveObject receiveObject = new ReceiveObject(message);
                        receiveObject.getChatMessage(consoleInput);
                    }
                }

                else {
                    MessageFormatter formatter = new MessageFormatter(message);
                    System.out.println(formatter.returnFormattedMessage());
                }
            }
        }).start();

    }
}
