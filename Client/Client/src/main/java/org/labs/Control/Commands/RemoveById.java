package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

import java.util.ArrayList;

import static org.labs.Service.Utilites.getValidInt;

public class RemoveById implements Command {
    private TransparentScannerWrapper scanner;
    private int id;
    public RemoveById(TransparentScannerWrapper scanner) {
        this.scanner = scanner;
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        if (args.length == 0){
            id = getValidInt(scanner, "Введите id:");
        }
        else{
            try{
                id = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e){
                id = 0;
                System.out.println("Введен некорректный id");
            }
        }
        ArrayList<String> additional = new ArrayList<>();
        additional.add(String.valueOf(id));
        String[] to = new String[additional.size()];
        additional.toArray(to);
        return new CommandResult("", to);
    }
}
