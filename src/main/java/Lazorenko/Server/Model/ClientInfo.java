package Lazorenko.Server.Model;

import Lazorenko.Server.Logger.ServerLogToFile;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public class ClientInfo extends AbstractClientInfo {
    private ServerLogToFile log = ServerLogToFile.getInstance();

    public ClientInfo() {
    }

    public ClientInfo(Socket s) throws IOException {
        this.s = s;
        this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    }

    @Override
    public synchronized void send (String line, Queue q) {
        try {
            String message = line;
            bw.write(message);
            bw.write("\n");
            bw.flush();
        } catch (IOException e) {
            close(q);
            log.getLogger().error(e.getMessage());
        }
    }

    public void notifyClient(Queue q){
        String notification = "Name already exists! Try again!";
        send(notification,q);
    }

}


