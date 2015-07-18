package Lazorenko.Client.Controller;

import Lazorenko.Common.Messages.ChatMessage;

public class FileClientMessageProcessor implements ClientMessageProcessor {

    private ChatMessage chatMessage;

    public FileClientMessageProcessor (final String input, boolean registeredClient){
        if (registeredClient) {
            if (input.toLowerCase().startsWith("y")) {
                chatMessage = new ChatMessage(true);
            } else {
                chatMessage = new ChatMessage();
                chatMessage.setClientRejectsFile(true);
            }
        }
        else {
            chatMessage = new ChatMessage(input);
        }
    }

    @Override
    public ChatMessage run() {
        return chatMessage;
    }
}
