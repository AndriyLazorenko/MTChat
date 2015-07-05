package Lazorenko.Server.View;
import Lazorenko.Server.Controller.Server;
import Lazorenko.Server.Logger.ServerLogToFile;

import java.io.IOException;

public class RunServer {
    public static void main(String[] args){
        Server server = null;
        ServerLogToFile log = ServerLogToFile.getInstance();
        try {
            server = new Server(8888);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
            log.getLogger().error(e.getMessage());
        }

    }
}
