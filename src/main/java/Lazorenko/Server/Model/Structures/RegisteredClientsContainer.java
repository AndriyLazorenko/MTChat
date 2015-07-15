package Lazorenko.Server.Model.Structures;


import java.util.concurrent.ConcurrentMap;

/**
 * Created by andriylazorenko on 26.06.15.
 */
public class RegisteredClientsContainer extends AbstractClientsContainer {

    private volatile static RegisteredClientsContainer uniqueInstance;

    public static RegisteredClientsContainer getInstance(){
        if (uniqueInstance==null){
            synchronized (ConcurrentMap.class){
                if (uniqueInstance==null){
                    uniqueInstance = new RegisteredClientsContainer();
                }
            }
        }
        return uniqueInstance;
    }

    private RegisteredClientsContainer() {
    }
}
