package Lazorenko.Client.Commands;

import Lazorenko.Client.Exceptions.FileFormatIncorrect;
import Lazorenko.Client.Logger.ClientLogToFile;
import Lazorenko.Common.Messages.ChatMessage;
import org.apache.commons.io.IOUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

/**
 * Created by Lazorenko on 07.07.2015.
 */
public class PassObject implements ClientCommands  {

    private ChatMessage chatMessage;
    protected ClientLogToFile log = ClientLogToFile.getInstance();

    private synchronized ChatMessage passObject() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please insert a filepath that you want to transfer");
        boolean fileOkay = false;
        String modifiedWindowsPath = null;
        while (!fileOkay) {
            String path = null;
            try {
                path = br.readLine();
            } catch (IOException e) {
                log.getLogger().error(e.getMessage()+"\n");
                e.printStackTrace();
            }
            boolean exists = false;
            boolean isImage = false;
            //Simple windows path check
            if (path.contains("\\")) {
                modifiedWindowsPath = path.replaceAll("\\\\", "\\\\\\\\");
            } else {
                modifiedWindowsPath = path;
            }
            //Check if file exists
            if (!new File(modifiedWindowsPath).exists()) {
                try {
                    throw new FileNotFoundException("No such file exists! Try again!");
                } catch (FileNotFoundException e) {
                    log.getLogger().error(e.getMessage()+"\n");
                    e.printStackTrace();
                }
            }
            else {
                exists = true;
            }
            //Check if file has correct format
            File f = new File(modifiedWindowsPath);
            String mimetype = new MimetypesFileTypeMap().getContentType(f);
            String type = mimetype.split("/")[0];
            if(!type.equals("image")){
                try {
                    throw new FileFormatIncorrect();
                } catch (FileFormatIncorrect fileFormatIncorrect) {
                    log.getLogger().error(fileFormatIncorrect.getMessage()+"\n");
                    fileFormatIncorrect.printStackTrace();
                }
            }
            else {
                isImage = true;
            }
            //Total check
            if (exists&&isImage){
                fileOkay =true;
            }
            else {
                fileOkay =false;
            }
        }
        //Creating filename
        //Bad design
        String filename = "";
        if (modifiedWindowsPath.contains("\\")) {
            filename = modifiedWindowsPath.substring(modifiedWindowsPath.lastIndexOf("\\") + 1);
        }
        else {
            filename = modifiedWindowsPath.substring(modifiedWindowsPath.lastIndexOf("/")+1);
        }
        InputStream is;
        byte[] file;

        try {
            is = new FileInputStream(modifiedWindowsPath);
            file = IOUtils.toByteArray(is);
            chatMessage = new ChatMessage(file,filename);
            System.out.println("The file has been recorded");
        } catch (FileNotFoundException e) {
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        } catch (IOException e) {
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        }
        return chatMessage;
    }


    public ChatMessage getChatMessage() {
        return passObject();
    }
}
