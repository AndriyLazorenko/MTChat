package Lazorenko.Server.Model.Info;

import java.io.*;

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
