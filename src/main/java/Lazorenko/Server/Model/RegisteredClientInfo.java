package Lazorenko.Server.Model;

import Lazorenko.Server.Logger.ServerLogToFile;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public class RegisteredClientInfo extends AbstractClientInfo{

    private String userName;
    private ServerLogToFile log = ServerLogToFile.getInstance();

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
    public synchronized void send (String message, Queue q) {
        try {
            bw.write(message);
            bw.write("\n");
            bw.flush();
        } catch (IOException e) {
            close(q);
            log.getLogger().error(e.getMessage()+"\n");
        }
    }

}
