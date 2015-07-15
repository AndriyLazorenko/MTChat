package Lazorenko.Server.Model.Structures;

import Lazorenko.Server.Model.Info.AbstractClientInfo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
