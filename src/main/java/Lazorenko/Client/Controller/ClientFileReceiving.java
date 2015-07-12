package Lazorenko.Client.Controller;

import Lazorenko.Common.Messages.ChatMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Lazorenko on 09.07.2015.
 */

public class ClientFileReceiving {
    private ChatMessage chatMessage;
    private static final String defaultPath = "src/main/resources/ReceivedFiles/";

    public ClientFileReceiving(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;

    }
    public void receive(){
        String path = defaultPath+chatMessage.getFilename();
        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(chatMessage.getFile());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}