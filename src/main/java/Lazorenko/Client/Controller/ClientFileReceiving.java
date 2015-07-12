package Lazorenko.Client.Controller;

import Lazorenko.Client.Logger.ClientLogToFile;
import Lazorenko.Common.Messages.ChatMessage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Lazorenko on 09.07.2015.
 */

public class ClientFileReceiving {

    private ChatMessage chatMessage;
    protected ClientLogToFile log = ClientLogToFile.getInstance();
    private static final String defaultPath = "./src/main/resources";

    public ClientFileReceiving(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    private String askForPath(){
        System.out.println("Insert correct filepath or type /d for default path");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input="";
        try {
            input = br.readLine();
            if (input.equals("/d")){
                return defaultPath;
            }
            else {
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
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        }
        return input;
    }

    public void receive() {
        Path path = Paths.get(askForPath());
        Path normalized = Paths.get(path.normalize().toString());
        String absoluteFilePath = normalized.toAbsolutePath().toString()+"/"+chatMessage.getFilename();
        File file = new File(absoluteFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(chatMessage.getFile());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        } catch (IOException e) {
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        }
    }
}