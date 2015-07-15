package Lazorenko.Client.Commands;

import Lazorenko.Common.Messages.ChatMessage;

import java.io.InputStream;

/**
 * Created by Lazorenko on 08.07.2015.
 */
public interface ClientCommands {
    public ChatMessage getChatMessage(InputStream console);
}
