package Lazorenko.Client.Controller;

import Lazorenko.Client.Commands.ClientCommands;
import Lazorenko.Client.Commands.Help;
import Lazorenko.Client.Commands.PassObject;
import Lazorenko.Common.Messages.ChatMessage;

/**
 * Created by Lazorenko on 09.07.2015.
 */

public class ClientMessageProcessor {

    private ChatMessage chatMessage;

    public ClientMessageProcessor(final String input, boolean registeredClient) {
        if (registeredClient) {
            if (input.contains("/")) {
                //Check if message is a command.
                switch (input) {
                    case "/h": {
                        ClientCommands command = new Help();
                        chatMessage = command.getChatMessage();
                        break;
                    }
                    case "/s": {
                        ClientCommands command = new PassObject();
                        chatMessage = command.getChatMessage();
                        break;
                    }
                    default: {
                        System.err.println("Wrong command syntax! Type '/h' for full command list");
                    }
                }
            } else {
                chatMessage = new ChatMessage(input);
            }
        }
        else {
            chatMessage = new ChatMessage(input);
        }
    }
    public ChatMessage run(){
        return chatMessage;
    }
}
