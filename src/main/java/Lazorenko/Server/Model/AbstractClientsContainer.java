package Lazorenko.Server.Model;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by andriylazorenko on 26.06.15.
 */
public abstract class AbstractClientsContainer {

    public AbstractClientsContainer() {
    }
    protected ConcurrentMap <String,AbstractClientInfo> container = new ConcurrentHashMap<>();

    public ConcurrentMap<String, AbstractClientInfo> getContainer() {
        return container;
    }
}
