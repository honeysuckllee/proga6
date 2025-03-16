package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;
import org.labs.Service.TransparentScannerWrapper;

import java.util.Arrays;
import java.util.List;

import static org.labs.Service.Utilites.getValidInt;

public class RemoveLower implements Command {
    private Deque deque;
    private int id;

    public RemoveLower(Deque deque) {
        this.deque = deque;
    }

    @Override
    public void execute() {
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        List<String> additional = Arrays.asList(additionalInput);
        id = Integer.parseInt(additional.get(0));
        if (!deque.getDeque().isEmpty()) {
            int counterDell = deque.removeLower(id);
            return new CommandResult("Удалено " + counterDell + " элементов" + "\n");
        }
        return new CommandResult("Коллекция пуста" + "\n");
    }
}
