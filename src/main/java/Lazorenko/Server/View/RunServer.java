package Lazorenko.Server.View;
import Lazorenko.Server.Controller.Server;
import Lazorenko.Server.Logger.ServerLogToFile;

import java.io.IOException;

public class RunServer {

    private static final int port = 8888;

    public static void main(String[] args){
        Server server = null;
        ServerLogToFile log = ServerLogToFile.getInstance();
        try {
            server = new Server(port);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
            log.getLogger().error("Server init error: " +e.getMessage()+"\n");
        }

    }
}
