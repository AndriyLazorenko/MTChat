package Lazorenko.Client.SerializationService;

import Lazorenko.Client.Commands.ClientCommands;
import Lazorenko.Client.Exceptions.FileFormatIncorrect;
import Lazorenko.Client.Logger.ClientLogToFile;
import Lazorenko.Common.Messages.ChatMessage;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

/**
 * Created by Lazorenko on 07.07.2015.
 */

public abstract class SerializeObject {

    protected ChatMessage chatMessage;
    protected ClientLogToFile log = ClientLogToFile.getInstance();

    protected abstract ChatMessage processObject(InputStream is);

    protected String getFilename(String modifiedWindowsPath) {
        String filename = "";
        if (modifiedWindowsPath.contains("\\")) {
            filename = modifiedWindowsPath.substring(modifiedWindowsPath.lastIndexOf("\\") + 1);
        }
        else {
            filename = modifiedWindowsPath.substring(modifiedWindowsPath.lastIndexOf("/")+1);
        }
        return filename;
    }

    protected boolean locationExists(String modifiedWindowsPath, boolean exists) {
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
        return exists;
    }

    protected String windowsPathRearrange(String path) {
        String modifiedWindowsPath;
        if (path.contains("\\")) {
            modifiedWindowsPath = path.replaceAll("\\\\", "\\\\\\\\");
        } else {
            modifiedWindowsPath = path;
        }
        return modifiedWindowsPath;
    }


    public ChatMessage getChatMessage(InputStream is) {
        return processObject(is);
    }
}
