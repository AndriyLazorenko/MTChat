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

    public RegisteredClientInfo() {
    }

    public RegisteredClientInfo(Socket s, String userName) throws IOException {
        this.userName = userName;
        this.s=s;
        this.ois = new ObjectInputStream(s.getInputStream());
        this.oos = new ObjectOutputStream(s.getOutputStream());
    }

    public String getUserName() {
        return userName;
    }

}
