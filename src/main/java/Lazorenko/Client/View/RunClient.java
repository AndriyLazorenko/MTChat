package Lazorenko.Client.View;

import Lazorenko.Client.Controller.Client;

public class RunClient {

    private static String defaultIP = "localhost";
    private static int defaultPort = 8888;

    public static void main(String[] args) {
        Client client = new Client(defaultIP,defaultPort);
        client.run();
    }
}
