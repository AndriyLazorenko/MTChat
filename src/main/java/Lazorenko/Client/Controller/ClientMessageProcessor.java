package Lazorenko.Client.Controller;

import Lazorenko.Common.Messages.ChatMessage;

/**
 * Created by andriylazorenko on 16.07.15.
 */
public interface ClientMessageProcessor {
    ChatMessage run();
}