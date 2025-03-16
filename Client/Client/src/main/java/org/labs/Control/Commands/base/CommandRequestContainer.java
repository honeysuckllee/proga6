package org.labs.Control.Commands.base;

import java.io.Serializable;

public final class CommandRequestContainer implements Serializable {

    private static final long serialVersionUID = -7071028630270434499L;
    private final String commandName;
    private final String[] args;
    private final String[] input;

    public CommandRequestContainer(String commandName, String[] args, String ... input) {
        this.commandName = commandName;
        this.args = args;
        this.input = input;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean hasAdditionalInput() {
        return input.length != 0;
    }

    public String[] getInput() {
        return input;
    }
}
