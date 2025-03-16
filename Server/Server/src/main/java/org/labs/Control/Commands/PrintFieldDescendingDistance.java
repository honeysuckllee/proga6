package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;

/**
 * Команда для печати элементов коллекции в порядке убывания расстояния до указанного поля.
 */
public class PrintFieldDescendingDistance implements Command {

    /**
     * Коллекция, с которой будет выполняться операция сортировки и вывода.
     */
    private final Deque deque;

    /**
     * Конструктор команды.
     *
     * @param deque коллекция
     */
    public PrintFieldDescendingDistance(Deque deque) {
        this.deque = deque;
    }

    /**
     * Выполняет команду печати элементов коллекции  в порядке убывания.
     */
    @Override
    public void execute() {
        deque.printFieldDescendingDistance();
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        return new CommandResult(deque.printFieldDescendingDistance());
    }
}