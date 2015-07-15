package Lazorenko.Client.Commands;

import Lazorenko.Common.Messages.ChatMessage;

import java.io.InputStream;

/**
 * Created by Lazorenko on 07.07.2015.
 */
public class Help implements ClientCommands {

    private CommandsContainer commands = new CommandsContainer();

    private void help() {
        System.out.println("Full description of all commands");
        for (String s: commands.getContainerOfCommands()) {
            System.out.println(s);
        }
    }

    @Override
    public ChatMessage getChatMessage(InputStream console) {
        help();
        ChatMessage forRet = new ChatMessage("internal");
        return forRet;
    }
}
