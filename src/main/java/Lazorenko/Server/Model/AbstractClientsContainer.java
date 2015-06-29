package Lazorenko.Server.Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by andriylazorenko on 26.06.15.
 */
public abstract class AbstractClientsContainer {

    public AbstractClientsContainer() {
    }

    protected BlockingQueue q = new LinkedBlockingQueue<>();

    public abstract BlockingQueue getQ();
}
