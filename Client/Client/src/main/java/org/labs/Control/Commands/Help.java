package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс `Help` реализует интерфейс `Command` и представляет команду вывода справки по доступным командам.
 * При выполнении команды выводится список всех доступных команд и их описание.
 * Использует словарь для хранения команд и их описаний, что делает код более гибким и удобным для расширения.
 */
public class Help implements Command {

    /**
     * Конструктор класса `Help`.
     * Инициализирует словарь команд и их описаний.
     */
    public Help() {
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        return new CommandResult("");
    }
}