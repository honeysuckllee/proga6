package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

import java.util.Deque;

public class ExecuteScript implements Command {
    private String nameFile;
    private Deque deque;

    public ExecuteScript(){ }

    public CommandResult execute(TransparentScannerWrapper scanner, String[] args, String... additionalInput) throws CommandException {
        nameFile = args[0];
        return new CommandResult("", nameFile);
    }
}
