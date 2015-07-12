package Lazorenko.Client.Commands;

import java.util.*;

/**
 * Created by Lazorenko on 07.07.2015.
 */
public class CommandsContainer {


    public Collection<String> getContainerOfCommands() {
        return containerOfCommands;
    }

    private Collection<String> containerOfCommands = new ArrayList<>();

    public CommandsContainer() {
        containerOfCommands.add("Press '/h' to list all available commands");
        containerOfCommands.add("Press '/s' to send a file. You can only send pictures in this chat");
    }
}
