package Lazorenko.Server.Model;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public class ClientInfo extends AbstractClientInfo {

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
            String ip = s.getInetAddress().toString();
            int port = s.getPort();
            String message = ip + ":" + ":" + port + " -> " + line;
            bw.write(message);
            bw.write("\n");
            bw.flush();
        } catch (IOException e) {
            close(q);
        }
    }

}


