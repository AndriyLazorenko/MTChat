package Lazorenko.Server.Controller;

import Lazorenko.Common.Messages.ChatMessage;

/**
 * Created by Lazorenko on 09.07.2015.
 */
public class ServerMessageProcessor {
    private ChatMessage chatMessage;

    public ServerMessageProcessor(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage run(){
        return chatMessage;
    }
}
