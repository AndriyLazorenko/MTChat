package Lazorenko.Server.Model.Structures;


import java.util.concurrent.ConcurrentMap;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public class ClientsContainer extends AbstractClientsContainer {

    private volatile static ClientsContainer uniqueInstance;

    public static ClientsContainer getInstance(){
        if (uniqueInstance==null){
            synchronized (ConcurrentMap.class){
                if (uniqueInstance==null){
                    uniqueInstance = new ClientsContainer();
                }
            }
        }
        return uniqueInstance;
    }

    private ClientsContainer() {
    }
}
