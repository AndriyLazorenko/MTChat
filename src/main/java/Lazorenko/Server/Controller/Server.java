package Lazorenko.Server.Controller;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private static int port;
    ServerSocket ss = null;
    Thread mainThread;

    public Server (int port) throws IOException {
        this.port = port;
        ss = new ServerSocket(this.port);
    }

    BlockingQueue<ClientInfo> q = new LinkedBlockingQueue<>();

    public void run() {
        System.out.println("Waiting for client");
        mainThread = Thread.currentThread();
        while (true) {
                Socket client = getNewConnection();//Method for easy exit
                if (mainThread.isInterrupted()) {
                    break;
                }
                else if (client!=null){
                    try {
                        ClientInfo ci = new ClientInfo(client);
                        Thread thread = new Thread(ci);
                        thread.setDaemon(true);
                        thread.start();
                        q.offer(ci);
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
    private Socket getNewConnection(){
        Socket forRet = null;
        try {
            forRet = ss.accept();
        } catch (IOException e) {
            shutdownServer();
        }
        return forRet;
    }

    private void shutdownServer(){
        for (ClientInfo ci: q) {
            ci.close();
        }
        if (!ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException ignored) {

            }
        }
    }



class ClientInfo extends Thread{

    Socket s;
    BufferedReader br;
    BufferedWriter bw;

    ClientInfo(Socket s) throws IOException {
        this.s = s;
        this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    }

    public void run() {

        while (!s.isClosed()) {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                close();
            }

            if (line == null) {
                close();
            } else if ("shutdown".equals(line)) {
                mainThread.interrupt();
                try {
                    new Socket("localhost", port);
                } catch (IOException ignored) {
                } finally {
                    shutdownServer();
                }
            } else {
                for (ClientInfo ci : q) {
                    ci.send(line);
                }
            }
        }
    }

    public synchronized void close() {
        q.remove(this);
        if (!s.isClosed()) {
            try {
                s.close();
            } catch (IOException ignored) {

            }
        }
    }

    public synchronized void send(String line) {
        try {
            String ip = s.getInetAddress().toString();
            int port = s.getPort();
            String message = ip + ":" + ":" + port + " -> " + line;
            bw.write(message);
            bw.write("\n");
            bw.flush();
        } catch (IOException e) {
            close();
        }
    }
}
}