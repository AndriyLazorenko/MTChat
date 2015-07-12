package Lazorenko.Client.SerializationService;

import Lazorenko.Client.Commands.ClientCommands;
import Lazorenko.Client.Exceptions.FileFormatIncorrect;
import Lazorenko.Common.Messages.ChatMessage;
import org.apache.commons.io.IOUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

/**
 * Created by andriylazorenko on 12.07.15.
 */
public class PassObject extends SerializeObject implements ClientCommands {


    @Override
    protected synchronized ChatMessage processObject() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please insert a filepath that you want to transfer");
        String modifiedWindowsPath = getPathFrom(br);
        //Creating filename
        String filename = getFilename(modifiedWindowsPath);
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

    protected String getPathFrom(BufferedReader br) {
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
            modifiedWindowsPath = windowsPathRearrange(path);
            //Check if file exists
            exists = locationExists(modifiedWindowsPath, exists);
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
        return modifiedWindowsPath;
    }
}
