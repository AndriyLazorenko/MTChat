package Lazorenko.Server.Model;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public class RegisteredClientInfo extends AbstractClientInfo{

    private String userName;

    public RegisteredClientInfo() {
    }

    public RegisteredClientInfo(Socket s,String userName) throws IOException {
        this.userName = userName;
        this.s=s;
        this.br=new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public synchronized void send (String line, Queue q) {
        try {
            String ip = s.getInetAddress().toString();
            int port = s.getPort();
            String message = ip + ":" + ":" + port + " -> " +getUserName()+" says: "+line;
            bw.write(message);
            bw.write("\n");
            bw.flush();
        } catch (IOException e) {
            close(q);
        }
    }

}
