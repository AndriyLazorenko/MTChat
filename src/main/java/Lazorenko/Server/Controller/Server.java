package Lazorenko.Server.Controller;


import Lazorenko.Server.Model.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int port;
    ServerSocket ss = null;
    Thread mainThread;
    ClientsContainer clientsContainer = new ClientsContainer();
    RegisteredClientsContainer reg = new RegisteredClientsContainer();

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
                    clientsContainer.getQ().offer(ci);
                    //Message to server about new client
                    String message = String.format("ip %s, port %s\n",
                            client.getInetAddress(),
                            client.getPort());
                    System.out.println(message);
                } catch (IOException e) {
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
            shutdownServer();
        }
        return forRet;
    }

    private void shutdownServer() {
        for (ClientInfo ci : clientsContainer.getQ()) {
            ci.close(clientsContainer.getQ());
        }
        if (!ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException ignored) {

            }
        }
    }

    class GeneralClientThread extends ClientInfo implements Runnable {
        ClientInfo clientInfo;

        GeneralClientThread(ClientInfo clientInfo) {
            this.clientInfo = clientInfo;
        }


        public void run() {
            String name = null;
            while (!Thread.currentThread().isInterrupted()) {
                boolean clientNameIsCorrect = false;
                while (!clientNameIsCorrect) {
                    boolean matchingNameFound = false;
                    try {
                        name = clientInfo.getBr().readLine();
                    } catch (IOException e) {
                        close(clientsContainer.getQ());
                    }
                    if (name == null) {
                        close(clientsContainer.getQ());
                    } else if ("shutdown".equals(name)) {
                        mainThread.interrupt();
                        try {
                            new Socket("localhost", port);
                        } catch (IOException ignored) {
                        } finally {
                            shutdownServer();
                        }
                    } else {
                        //We process clients input in here
                        for (RegisteredClientInfo rci : reg.getQ()) {
                            if (name.toString().toLowerCase().equals(rci.getUserName().toLowerCase())) {
                                //Method to ask again for a new name
                                matchingNameFound = true;
                                //TODO
                                //Understand why doesn't the method work
                                try {
                                    clientInfo.getBw().write("Name already exists! Try again!");
                                    clientInfo.getBw().flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if (!matchingNameFound) {
                        clientNameIsCorrect = true;
                    }
                }
                //So, if the cycle is finished, the name is not found in database
                //Now we need to end lifecycles with not registered client and start registered client
                //lifecycles
                registerClient(name);
            }
            System.out.println("The user has been registered");
        }

        private void registerClient(String validName){
            try {
                //New object allocated to client
                RegisteredClientInfo rci = new RegisteredClientInfo(clientInfo.getS(),validName);
                RegisteredClientThread rct = new RegisteredClientThread(rci);
                //Client put to container for easy notification
                reg.getQ().offer(rci);
                //Sending message to client
                rci.getBw().write("The server registered you as "+validName+"\n");
                rci.getBw().flush();
                //New thread allocated to client
                Thread thread = new Thread(rct);
//                thread.setDaemon(true);
                thread.start();
                //Removing client from general queue and interrupting thread
                clientInfo.close(clientsContainer.getQ());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class RegisteredClientThread extends RegisteredClientInfo implements Runnable {
        RegisteredClientInfo registeredClientInfo;

        RegisteredClientThread(RegisteredClientInfo registeredClientInfo) {
            this.registeredClientInfo = registeredClientInfo;
        }

        @Override
        //TODO
        //Remaster exit logic
        public void run() {
            while (!registeredClientInfo.getS().isClosed()) {
                String line = null;
                try {
                    line = registeredClientInfo.getBr().readLine();
                } catch (IOException e) {
                    close(reg.getQ());
                }

                if (line == null) {
                    close(reg.getQ());
                } else if ("shutdown".equals(line)) {
                    mainThread.interrupt();
                    try {
                        new Socket("localhost", port);
                    } catch (IOException ignored) {
                    } finally {
                        shutdownServer();
                    }
                } else {
                    //We process clients input in here
                    for (RegisteredClientInfo rci : reg.getQ()) {
                        rci.send(line, reg.getQ());
                    }
                }
            }
        }
    }
}