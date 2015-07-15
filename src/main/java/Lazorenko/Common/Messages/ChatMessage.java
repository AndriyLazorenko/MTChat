package Lazorenko.Common.Messages;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lazorenko on 09.07.2015.
 */
public class ChatMessage implements Serializable {

    private String simpleMessage;
    private byte[] file;
    private String filename;
    private boolean clientRegistered;
    private int port;
    private String ip;
    private String username;
    private Date date;

    public Date getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isClientRegistered() {
        return clientRegistered;

    }

    public ChatMessage(boolean clientRegistered, String clientName) {
        this.clientRegistered = clientRegistered;
        this.username = clientName;
        this.date=new Date();
    }

    public ChatMessage(String simpleMessage) {
        this.simpleMessage = simpleMessage;
        this.date=new Date();
    }

    public ChatMessage(byte[] file, String filename) {
        this.file = file;
        this.filename = filename;
        this.date=new Date();
    }

    public byte[] getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public String getSimpleMessage() {
        return simpleMessage;
    }
}
