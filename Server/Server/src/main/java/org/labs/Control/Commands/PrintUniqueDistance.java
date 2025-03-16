package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;

/**
 * Класс `PrintUniqueDistance` реализует интерфейс `Command` и представляет команду вывода уникальных значений поля `distance` из коллекции.
 * При выполнении команды выводятся все уникальные значения поля `distance` элементов коллекции.
 */
public class PrintUniqueDistance implements Command {
    /**
     * Коллекция `Deque`, из которой извлекаются уникальные значения поля `distance`.
     */
    private Deque deque;

    /**
     * Конструктор класса `PrintUniqueDistance`.
     *
     * @param deque Коллекция `Deque`, из которой извлекаются уникальные значения поля `distance`.
     */
    public PrintUniqueDistance(Deque deque) {
        this.deque = deque;
    }

    /**
     * Метод `execute` выводит уникальные значения поля `distance` из коллекции.
     */
    @Override
    public void execute() {
        deque.printUniqueDistance();
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        return new CommandResult(deque.printUniqueDistance());
    }
}