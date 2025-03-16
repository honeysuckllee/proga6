package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Route;

import java.util.ArrayDeque;
import java.util.Map;

public class Show implements Command {
    private ArrayDeque<Route> deque;

    public Show(ArrayDeque<Route> deque) {
        this.deque = deque;
    }

    @Override
    public void execute() {
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        StringBuilder rez = new StringBuilder("Коллекция:\n");
        if (!deque.isEmpty()) {
            for (Route route : deque) {
                rez.append(route.toString()).append("\n");
            }
        }
        else {
            rez.append("пуста").append("\n");
        }
        return new CommandResult(rez.toString());
    }
}
