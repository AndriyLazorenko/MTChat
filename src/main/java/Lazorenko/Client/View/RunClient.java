package Lazorenko.Client.View;

import Lazorenko.Client.Controller.Client;
import Lazorenko.Client.Logger.ClientLogToFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunClient {

    private static final String defaultIP = "localhost";
    private static final int defaultPort = 8888;

    public static void main(String[] args) {
        ClientLogToFile log = ClientLogToFile.getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //Block for IP selection
        System.out.println("Select local server or remote? L/R");
        String ip = null;
        try {
            String serverChoice = br.readLine();
            if (serverChoice.equals("R")||serverChoice.equals("r")){
                System.out.println("Insert IP");
                String ipInput = br.readLine();
                if (!validIP(ipInput))
                    ipInput = askForCorrectIP();
                ip = ipInput;
            }
            else {
                ip = defaultIP;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.getLogger().error(e.getMessage()+"\n");
        }
        //Block for establishing client
        Client client = new Client(ip,defaultPort);
        client.run();
    }

    public static String askForCorrectIP (){
        ClientLogToFile log = ClientLogToFile.getInstance();
        String ipInput = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.err.println("Incorrect IP! Insert IP again!");
        try {
            ipInput = br.readLine();
            if (!validIP(ipInput))
                ipInput=askForCorrectIP();
        } catch (IOException e) {
            e.printStackTrace();
            log.getLogger().error(e.getMessage()+"\n");
        }
        return ipInput;
    }

    public static boolean validIP (String ip) {
        ClientLogToFile log = ClientLogToFile.getInstance();
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i<0)||(i>255)) {
                    return false;
                }
            }
            if(ip.endsWith(".")) {
                return false;
            }
            return true;
        } catch (NumberFormatException nfe) {
            log.getLogger().info("Incorrect IP! "+nfe.getMessage()+"\n");
            return false;
        }
    }
}
