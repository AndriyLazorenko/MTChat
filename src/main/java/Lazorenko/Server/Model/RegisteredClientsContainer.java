package Lazorenko.Server.Model;

import java.util.concurrent.BlockingQueue;

/**
 * Created by andriylazorenko on 26.06.15.
 */
public class RegisteredClientsContainer extends AbstractClientsContainer {

    @Override
    public BlockingQueue<RegisteredClientInfo> getQ() {
        return q;
    }
}
