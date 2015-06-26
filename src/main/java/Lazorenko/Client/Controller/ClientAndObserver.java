package Lazorenko.Client.Controller;

import java.net.Socket;

/**
 * Created by Master on 11-May-15.
 */
public interface ClientAndObserver {
    void run();
    void speak(Socket s);
    void listen(Socket s);
}
