package Lazorenko.Common.Messages;

import java.io.Serializable;
import java.util.Date;

/**
 * Class is used to pass info in serialized form between server and client
 * @author andriylazorenko
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
    private boolean askClientToAcceptFile = false;
    private boolean clientAcceptsFile = false;
    private int fileSize;
    private boolean clientRejectsFile = false;

    /**
     * Constructors
     */

    public ChatMessage() {

    }

    /**
     * Constructor responsible for client's response to accept a file
     * @param clientAcceptsFile
     */


    public ChatMessage(boolean clientAcceptsFile) {
        this.clientAcceptsFile = clientAcceptsFile;
    }

    /**
     * Constructor responsible for server's request whether the client wants to
     * accept file from specified user and with specified file size
     * @param fileSize
     * @param askClientToAcceptFile
     * @param username
     */

    public ChatMessage(int fileSize, boolean askClientToAcceptFile, String username) {
        this.fileSize = fileSize;
        this.askClientToAcceptFile = askClientToAcceptFile;
        this.username = username;
    }

    /**
     * Constructor responsible for server's response to register a client on server
     * and to assign it a name
     * @param clientRegistered
     * @param clientName
     */

    public ChatMessage(boolean clientRegistered, String clientName) {
        this.clientRegistered = clientRegistered;
        this.username = clientName;
        this.date=new Date();
    }

    /**
     * This constructor is used for unregistered user's communications with server as well as for
     * ordinary messaging of registered users
     * @param simpleMessage
     */

    public ChatMessage(String simpleMessage) {
        this.simpleMessage = simpleMessage;
        this.date=new Date();
    }

    /**
     * This constructor is used to pass a file in message (registered users only)
     * @param file
     * @param filename
     */

    public ChatMessage(byte[] file, String filename) {
        this.file = file;
        this.filename = filename;
        this.date=new Date();
    }

    /**
     * Getters
     */

    public boolean isClientAcceptsFile() {
        return clientAcceptsFile;
    }

    public int getFileSize() {
        return fileSize;
    }

    public boolean isAskClientToAcceptFile() {
        return askClientToAcceptFile;
    }

    public Date getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public boolean isClientRegistered() {
        return clientRegistered;
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

    public boolean isClientRejectsFile() {
        return clientRejectsFile;
    }

    /**
     * Setters
     */

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setClientRejectsFile(boolean clientRejectsFile) {
        this.clientRejectsFile = clientRejectsFile;
    }
}
