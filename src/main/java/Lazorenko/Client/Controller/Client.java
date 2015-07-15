
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
                    String message = console.nextLine();
                    if (message != null) {
                        ClientMessageProcessor processor =
                                new ClientMessageProcessor(message, isClientRegistered, consoleInput);
                        ChatMessage chatMessage = processor.run();
                        oos.writeObject(chatMessage);
                        oos.flush();
                    }
                }
            }catch (IOException e) {
                log.getLogger().error(e.getMessage() + "\n");
                e.printStackTrace();
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
                        //Check if message is a command. If it is, it is processed. If not - the message is simply passed
                        if (message.isClientRegistered()) {
                            isClientRegistered = true;
                            clientName = message.getUsername();
                        }
                        else if (message.getFile()!=null){
                            //TODO ask Serhiy
                            /**
                             * Currently a spike is in place.
                             * The bitch would stick to System.in and won't let go
                             * The only reasonable way seems to put some input, then - pass to
                             * sync block where our guys catch the InputStream and do their job
                             * of receiving file to correct path
                             */
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
                } catch (IOException e) {
                    log.getLogger().error(e.getMessage()+"\n");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    log.getLogger().error(e.getMessage() + "\n");
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
