package Lazorenko.Common.Messages;

import java.text.SimpleDateFormat;
import java.util.Date;

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
                    .append(" says: ").append(message.getSimpleMessage())
                    .append(" at ").append(dateFormat(message.getDate()));
        }
        else {
            formattedMessage = formattedMessage.append(message.getIp())
                    .append(":").append(":").append(message.getPort())
                    .append(" -> ").append(message.getUsername())
                    .append(" says: '").append(message.getSimpleMessage())
                    .append("' at ").append(dateFormat(message.getDate()));
        }
        String forRet = formattedMessage.toString();

        return forRet;
    }

    private String dateFormat(Date d){
        String forRet;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/y");
        forRet=simpleDateFormat.format(d);
        return forRet;
    }
}
