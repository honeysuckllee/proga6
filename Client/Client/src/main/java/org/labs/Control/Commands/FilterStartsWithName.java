package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

import java.util.ArrayList;
import java.util.Deque;

import static org.labs.Service.Utilites.getValidInt;
import static org.labs.Service.Utilites.getValidName;

public class FilterStartsWithName implements Command {
    private Deque deque;
    private String name;
    private TransparentScannerWrapper scanner;
    public FilterStartsWithName(TransparentScannerWrapper scanner){
        this.scanner = scanner;
    }
    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        if (args.length == 0) {
            name = getValidName(scanner);
        } else {
            name = args[0];
        }
        ArrayList<String> additional = new ArrayList<>();
        additional.add(name);
        String[] to = new String[additional.size()];
        additional.toArray(to);
        return new CommandResult("", to);
    }
}
