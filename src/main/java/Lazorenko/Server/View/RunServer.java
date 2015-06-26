package Lazorenko.Server.View;
import Lazorenko.Server.Controller.Server;

import java.io.IOException;

public class RunServer {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8888);
        server.run();

    }

}
