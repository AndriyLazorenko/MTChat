package Lazorenko.Client.Controller;

import Lazorenko.Client.Commands.ClientCommands;
import Lazorenko.Client.Commands.Help;
import Lazorenko.Client.SerializationService.PassObject;
import Lazorenko.Client.SerializationService.SerializeObject;
import Lazorenko.Common.Messages.ChatMessage;

import java.io.InputStream;

/**
 * Created by Lazorenko on 09.07.2015.
 */

public class ClientMessageProcessor {

    private ChatMessage chatMessage;

    public ClientMessageProcessor(final String input, boolean registeredClient, InputStream console) {
        if (registeredClient) {
            if (input.contains("/")) {
                //Check if message is a command.
                switch (input) {
                    case "/h": {
                        ClientCommands command = new Help();
                        chatMessage = command.getChatMessage(console);
                        break;
                    }
                    case "/s": {
                        ClientCommands command = new PassObject();
                        chatMessage = command.getChatMessage(console);
                        break;
                    }
                    default: {
                        System.err.println("Wrong command syntax!");
                        ClientCommands command = new Help();
                        chatMessage = command.getChatMessage(console);
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
