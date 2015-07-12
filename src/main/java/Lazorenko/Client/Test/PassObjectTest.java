package Lazorenko.Client.Test;

import Lazorenko.Client.Commands.PassObject;
import Lazorenko.Common.Messages.ChatMessage;

/**
 * Created by Lazorenko on 09.07.2015.
 */
public class PassObjectTest {
    public static void main(String[] args) {
        PassObject passObject = new PassObject();
        ChatMessage chatMessage = passObject.getChatMessage();
        System.out.println(chatMessage.getFilename());
        System.out.println(chatMessage.getFile().length);
    }
}
