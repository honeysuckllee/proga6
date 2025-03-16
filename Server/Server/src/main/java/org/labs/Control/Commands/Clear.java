package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;

/**
 * Класс `Clear` реализует команду очистки коллекции `Deque`.
 */
public class Clear implements Command {
    /**
     * Коллекция `Deque`, которую необходимо очистить.
     */
    private Deque deq;

    /**
     * Конструктор класса `Clear`.
     *
     * @param deque Коллекция `Deque`, которую необходимо очистить.
     */
    public Clear(Deque deque) {
        deq = deque;
    }

    /**
     * Выполняет команду очистки коллекции `Deque`.
     * Удаляет все элементы из коллекции `Deque`.
     */
    @Override
    public void execute() {
        deq.clear();
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        deq.clear();
        StringBuilder rez = new StringBuilder("Коллекция очищена\n");
        return new CommandResult(rez.toString());
    }
}
