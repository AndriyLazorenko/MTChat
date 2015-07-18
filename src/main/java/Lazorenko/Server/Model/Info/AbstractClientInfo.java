package Lazorenko.Server.Model.Info;

import Lazorenko.Common.Messages.ChatMessage;
import Lazorenko.Server.Logger.ServerLogToFile;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public abstract class AbstractClientInfo {

    protected Socket s;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;
    protected ServerLogToFile log = ServerLogToFile.getInstance();
    private Queue<ChatMessage> files = new LinkedBlockingQueue<>();

    public Queue<ChatMessage> getFiles() {
        return files;
    }

    public Socket getS() {
        return s;
    }
    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void close(ConcurrentMap map, String name) {

        map.remove(name);
        //Interrupting the thread
        Thread.currentThread().interrupt();
    }

    public void send (ChatMessage message, ConcurrentMap map, String sender) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            close(map,sender);
            log.getLogger().error(e.getMessage()+"\n");
        }
    }
}
