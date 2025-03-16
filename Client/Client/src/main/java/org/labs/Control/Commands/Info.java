package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;

public class Info implements Command {
    /**
     * Конструктор класса `Help`.
     * Инициализирует словарь команд и их описаний.
     */
    public Info() {
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        return new CommandResult("");
    }
}
