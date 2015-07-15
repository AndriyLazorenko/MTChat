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
    protected synchronized ChatMessage processObject(InputStream consoleInput) {
        BufferedReader br = new BufferedReader(new InputStreamReader(consoleInput));
        System.out.println("Do you want to save file to default location? y/n");
        String choiceDefaultOrNotLocation = "";
        try {
            choiceDefaultOrNotLocation = br.readLine();
            String absoluteFilePath;
            String finalPath;
        if (choiceDefaultOrNotLocation.toLowerCase().equals("y")){
            Path path = Paths.get(defaultPath);
            Path normalized = Paths.get(path.normalize().toString());
            absoluteFilePath = normalized.toAbsolutePath().toString()+"/"+chatMessage.getFilename();
            finalPath=absoluteFilePath;
        }
        else {
            System.out.println("Please insert a filepath that you want to transfer to");
            absoluteFilePath= getPathTo(br);
            finalPath = absoluteFilePath.concat("/"+chatMessage.getFilename());
        }
        File file = new File(finalPath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(chatMessage.getFile());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        } finally {
            System.out.println("The file has been received!");
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
                if (!pathOkay){
                    System.err.println("The path is incorrect. Try again!");
                }
            } catch (IOException e) {
                log.getLogger().error(e.getMessage()+"\n");
                e.printStackTrace();
            }
        }
        return forRet;
    }
}
