package Lazorenko.Client.Test;

import Lazorenko.Client.SerializationService.PassObject;
import Lazorenko.Client.SerializationService.SerializeObject;
import Lazorenko.Common.Messages.ChatMessage;

/**
 * Created by Lazorenko on 09.07.2015.
 */
public class PassObjectTest {
    public static void main(String[] args) {
        SerializeObject serializeObject = new PassObject();
        ChatMessage chatMessage = serializeObject.getChatMessage();
        System.out.println(chatMessage.getFilename());
        System.out.println(chatMessage.getFile().length);
    }
}
