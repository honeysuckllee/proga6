package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

public class CommandWithNotParametr implements Command {
    public CommandWithNotParametr() {
    }
    @Override
    public CommandResult execute(TransparentScannerWrapper scanner, String[] args, String... additionalInput) throws CommandException {
        return new CommandResult("");
    }
}
