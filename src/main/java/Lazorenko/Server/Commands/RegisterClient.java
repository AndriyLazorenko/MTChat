package Lazorenko.Server.Commands;

import Lazorenko.Common.Messages.ChatMessage;
import Lazorenko.Server.Logger.ServerLogToFile;
import Lazorenko.Server.Model.RegisteredClientInfo;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Lazorenko on 07.07.2015.
 */
public class RegisterClient implements ServerCommands {

    private RegisteredClientInfo rci;
    protected ServerLogToFile log = ServerLogToFile.getInstance();

    public RegisterClient(RegisteredClientInfo rci) {
        this.rci = rci;
    }

    private synchronized void registerClient() {
        ObjectOutputStream oos = rci.getOos();
        String userName = rci.getUserName();
        ChatMessage command = new ChatMessage(true,userName);
        try {
            oos.writeObject(command);
            oos.flush();
        } catch (IOException e) {
            log.getLogger().error(e.getMessage()+"\n");
            e.printStackTrace();
        }
    }

    @Override
    public void execute() {
        registerClient();
    }
}
