package Lazorenko.Server.Model;


import java.util.concurrent.BlockingQueue;

/**
 * Created by andriylazorenko on 26.06.15.
 */

public class ClientsContainer extends AbstractClientsContainer {

    @Override
    public BlockingQueue<ClientInfo> getQ() {
        return q;
    }
}