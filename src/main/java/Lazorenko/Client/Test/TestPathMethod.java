package Lazorenko.Client.Test;

import Lazorenko.Client.Logger.ClientLogToFile;

import java.io.*;

/**
 * Created by andriylazorenko on 12.07.15.
 */
public class TestPathMethod {
    private static final String defaultPath = "./src/main/resources";

    public static void main(String[] args) {
        FuckingHell fuck = new FuckingHell();
        System.out.println(fuck.askForPath());
    }
    static class FuckingHell {
        protected ClientLogToFile log = ClientLogToFile.getInstance();
        public String askForPath() {
            System.out.println("Insert correct filepath or type d for default path");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
            try {
                input = br.readLine();
                if (input.trim().toLowerCase().equals("d")) {
                    input = defaultPath;
                } else {
                    if (!new File(input).exists()) {
                        try {
                            throw new FileNotFoundException("No such file exists! Try again!");
                        } catch (FileNotFoundException e) {
                            log.getLogger().error(e.getMessage() + "\n");
                            e.printStackTrace();
                        }
                    }

                }
            } catch (IOException e) {
                log.getLogger().error(e.getMessage() + "\n");
                e.printStackTrace();
            }
            return input;
        }
    }
}
