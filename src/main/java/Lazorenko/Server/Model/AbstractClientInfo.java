package Lazorenko.Server.Model;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public abstract class AbstractClientInfo {

    protected Socket s;
    protected BufferedReader br;
    protected BufferedWriter bw;

    public Socket getS() {
        return s;
    }

    public BufferedReader getBr() {
        return br;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void close(Queue q) {
        q.remove(this);
        //Interrupting the thread
        Thread.currentThread().interrupt();
    }

    public abstract void send (String line, Queue q);
}
