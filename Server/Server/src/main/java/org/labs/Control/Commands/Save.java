package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;

public class Save implements Command {

    private Deque deq;

    public Save(Deque deque) {
        deq = deque;
    }

    @Override
    public void execute() {
        deq.save(deq.getCollectionFileName());
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        return null;
    }

}
