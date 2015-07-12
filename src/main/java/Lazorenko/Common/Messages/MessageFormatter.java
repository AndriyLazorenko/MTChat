package Lazorenko.Common.Messages;

/**
 * Created by andriylazorenko on 12.07.15.
 */

public class MessageFormatter {
    private ChatMessage message;

    public MessageFormatter(ChatMessage message) {
        this.message = message;
    }

    public String returnFormattedMessage(){
        StringBuilder formattedMessage = new StringBuilder("");
        if (message.getUsername()==null){
            formattedMessage = formattedMessage.append("Server")
                    .append(" says: ").append(message.getSimpleMessage());
        }
        else {
            formattedMessage = formattedMessage.append(message.getIp())
                    .append(":").append(":").append(message.getPort())
                    .append(" -> ").append(message.getUsername())
                    .append(" says: ").append(message.getSimpleMessage());
        }
        String forRet = formattedMessage.toString();

        return forRet;
    }
}
