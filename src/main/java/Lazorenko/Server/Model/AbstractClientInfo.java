package Lazorenko.Server.Model;

import Lazorenko.Common.Messages.ChatMessage;
import Lazorenko.Server.Logger.ServerLogToFile;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public abstract class AbstractClientInfo {

    protected Socket s;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;
    protected ServerLogToFile log = ServerLogToFile.getInstance();

    public Socket getS() {
        return s;
    }
    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void close(ConcurrentMap map) {
        map.remove(this);
        //Interrupting the thread
        Thread.currentThread().interrupt();
    }

    public synchronized void send (ChatMessage message, ConcurrentMap map) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            close(map);
            log.getLogger().error(e.getMessage()+"\n");
        }
    }
}
