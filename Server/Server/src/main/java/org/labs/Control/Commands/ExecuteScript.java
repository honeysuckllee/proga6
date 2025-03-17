package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

/**
 * Класс `ExecuteScript` реализует интерфейс `Command` и представляет команду выполнения скрипта.
 * Команда читает команды из файла и передает их на выполнение в `CommandManager`.
 */
public class ExecuteScript implements Command {

    /**
     * Конструктор класса `ExecuteScript`.
     *
     * @param scanner Объект класса `Scanner` для чтения команд из файла.
     */
    public ExecuteScript() {
    }

    /**
     * Метод `execute` выполняет команду выполнения скрипта.
     * Читает команды из файла и передает их на выполнение в `CommandManager`.
     */
    @Override
    public void execute() {}

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        return new CommandResult("");
    }
}