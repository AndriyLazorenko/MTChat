package Lazorenko.Server.Controller;


import Lazorenko.Common.Messages.ChatMessage;
import Lazorenko.Server.Commands.RegisterClient;
import Lazorenko.Server.Commands.ServerCommands;
import Lazorenko.Server.Logger.ServerLogToFile;
import Lazorenko.Server.Model.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int port;
    private ServerSocket ss = null;
    private Thread mainThread;
    private ClientsContainer clientsContainer = ClientsContainer.getInstance();
    private RegisteredClientsContainer reg = RegisteredClientsContainer.getInstance();
    private ServerLogToFile log = ServerLogToFile.getInstance();

    public Server(int port) throws IOException {
        this.port = port;
        ss = new ServerSocket(this.port);
    }


    public void run() {
        System.out.println("Waiting for client");
        mainThread = Thread.currentThread();
        while (true) {
            Socket client = getNewConnection();//Method for easy exit
            if (mainThread.isInterrupted()) {
                break;
            } else if (client != null) {
                try {
                    //New object allocated to client
                    ClientInfo ci = new ClientInfo(client);
                    GeneralClientThread ct = new GeneralClientThread(ci);
                    //New thread allocated to client
                    Thread thread = new Thread(ct);
//                    thread.setDaemon(true);
                    thread.start();
                    //Client put to container for easy notification
                    clientsContainer.getContainer().put(Integer.toString(ci.getS().getPort()), ci);
                    //Message to server about new client
                    String connected = String.format("ip %s, port %s\n",
                            client.getInetAddress(),
                            client.getPort());
                    System.out.println(connected);
                } catch (IOException e) {
                    log.getLogger().error(e.getMessage()+"\n");
                    e.printStackTrace();
                }
            }
        }
    }

    private Socket getNewConnection() {
        Socket forRet = null;
        try {
            forRet = ss.accept();
        } catch (IOException e) {
            log.getLogger().error("Server is being shutdown because of "+e.getMessage()+"\n");
            shutdownServer();
        }
        return forRet;
    }

    private void shutdownServer() {
        for (AbstractClientInfo ci : clientsContainer.getContainer().values()) {
            ci.close(clientsContainer.getContainer());
        }
        if (!ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException ignored) {

            }
        }
    }

    class GeneralClientThread extends ClientInfo implements Runnable {
        private ClientInfo clientInfo;

        private GeneralClientThread(ClientInfo clientInfo) {
            this.clientInfo = clientInfo;
        }


        public void run() {
            ChatMessage name = null;
            while (!Thread.currentThread().isInterrupted()) {
                boolean clientNameIsCorrect = false;
                UsernameValidator validator = new UsernameValidator();
                boolean clientNameValidated = false;
                while (!clientNameIsCorrect) {
                    boolean matchingNameFound = false;
                    try {
                        ObjectInputStream ois = clientInfo.getOis();
                        name = (ChatMessage) ois.readObject();
                    } catch (IOException e) {
                        close(clientsContainer.getContainer());
                        log.getLogger().error(e.getMessage()+"\n");
                    } catch (ClassNotFoundException e) {
                        log.getLogger().error(e.getMessage()+"\n");
                        e.printStackTrace();
                    }
                    if (name == null) {
                        close(clientsContainer.getContainer());
                    } else if ("shutdown".equals(name.getSimpleMessage())) {
                        mainThread.interrupt();
                        try {
                            new Socket("localhost", port);
                        } catch (IOException ignored) {
                        } finally {
                            log.getLogger().info("Server is given command to shutdown"+"\n");
                            shutdownServer();
                        }
                    } else {

                        //We process clients input in here

                        for (String existingNames : reg.getContainer().keySet()) {
                            if (name.getSimpleMessage().toLowerCase().equals(existingNames.toLowerCase())) {
                                //Method to ask again for a new name
                                matchingNameFound = true;
                                clientInfo.nameExistsNotifyClient(clientsContainer.getContainer());
                            }
                        }
                    }
                    //Additional check for validity of name
                    if (!matchingNameFound) {
                        if (!clientNameValidated) {
                            clientNameValidated=validator.validate(name.getSimpleMessage());
                            if (clientNameValidated){
                                clientNameIsCorrect = true;
                            }
                            else {
                                clientInfo.validateNotifyClient(clientsContainer.getContainer());
                            }
                        }
                    }
                }
                //So, if the cycle is finished, the name is not found in database
                //Now we need to end lifecycles with not registered client and start registered client
                //lifecycles
                registerClient(name.getSimpleMessage());
            }
            System.out.println("The user has been registered");
        }

        private void registerClient(String validName){
            try {
                //New object allocated to client
                RegisteredClientInfo rci = new RegisteredClientInfo(clientInfo,validName);
                RegisteredClientThread rct = new RegisteredClientThread(rci);
                //Client put to container for easy notification
                reg.getContainer().put(validName,rci);
                //Sending message to client
                ChatMessage serverRegistration =
                        new ChatMessage("The server registered you as " + validName + "\n");
                rci.getOos().writeObject(serverRegistration);
                rci.getOos().flush();
                //New thread allocated to client
                Thread thread = new Thread(rct);
                thread.start();
                //Removing client from general queue and interrupting thread
                clientInfo.close(clientsContainer.getContainer());
                //Sending a command to client to register server
                ServerCommands command = new RegisterClient(rci);
                command.execute();

            } catch (IOException e) {
                log.getLogger().error(e.getMessage()+"\n");
                e.printStackTrace();
            }
        }
    }

    class RegisteredClientThread extends RegisteredClientInfo implements Runnable {
        private RegisteredClientInfo registeredClientInfo;

        RegisteredClientThread(RegisteredClientInfo registeredClientInfo) {
            this.registeredClientInfo = registeredClientInfo;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                ChatMessage message = null;
                try {
                    message = (ChatMessage) registeredClientInfo.getOis().readObject();
                } catch (IOException e) {
                    close(reg.getContainer());
                    log.getLogger().error(e.getMessage()+"\n");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    log.getLogger().error(e.getMessage() + "\n");
                }
                if (message == null) {
                    close(reg.getContainer());
                } else if ("shutdown".equals(message.getSimpleMessage())) {
                    mainThread.interrupt();
                    try {
                        new Socket("localhost", port);
                    } catch (IOException ignored) {
                    } finally {
                        log.getLogger().info("Server is being shutdown on command" + "\n");
                        shutdownServer();
                    }
                } else {
                    //A message is formed
                    String ip = registeredClientInfo.getS().getInetAddress().toString();
                    int port = registeredClientInfo.getS().getPort();
                    String username = registeredClientInfo.getUserName();
                    message.setIp(ip);
                    message.setPort(port);
                    message.setUsername(username);
                    //Formatted message is sent to all registered users
                    for (AbstractClientInfo rci : reg.getContainer().values()) {
                        rci.send(message, reg.getContainer());
                    }
                }
            }
        }
    }
}