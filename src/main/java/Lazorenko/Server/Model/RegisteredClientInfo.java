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

    public RegisteredClientInfo(ClientInfo ci, String userName) throws IOException {
        this.userName = userName;
        this.s=ci.getS();
        this.ois = ci.getOis();
        this.oos = ci.getOos();
    }

    public String getUserName() {
        return userName;
    }

}
