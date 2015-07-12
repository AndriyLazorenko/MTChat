package Lazorenko.Client.SerializationService;

import Lazorenko.Client.Logger.ClientLogToFile;
import Lazorenko.Common.Messages.ChatMessage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by andriylazorenko on 12.07.15.
 */
public class ReceiveObject extends SerializeObject {

    public ReceiveObject(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    private ChatMessage chatMessage;
    protected ClientLogToFile log = ClientLogToFile.getInstance();
    private static final String defaultPath = "./src/main/resources";

    @Override
    protected synchronized ChatMessage processObject() {
        //TODO understand why the fuck is double enter required at this stage?
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Do you want to save file to default location? y/n");
        String choiceDefaultOrNotLocation = "";
        try {
            choiceDefaultOrNotLocation = br.readLine();
            String absoluteFilePath;
        if (choiceDefaultOrNotLocation.toLowerCase().equals("y")){
            Path path = Paths.get(defaultPath);
            Path normalized = Paths.get(path.normalize().toString());
            absoluteFilePath = normalized.toAbsolutePath().toString()+"/"+chatMessage.getFilename();
        }
        else {
            System.out.println("Please insert a filepath that you want to transfer to");
            absoluteFilePath= getPathTo(br);
        }
            String finalPath = absoluteFilePath.concat("/"+chatMessage.getFilename());
        File file = new File(finalPath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(chatMessage.getFile());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        }
        return chatMessage;
    }

    protected String getPathTo(BufferedReader br) {
        String input;
        String forRet = null;
        boolean pathOkay = false;
        while (!pathOkay) {
            try {
                input = br.readLine();
                forRet = windowsPathRearrange(input);
                pathOkay = locationExists(forRet,pathOkay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return forRet;
    }
}
