package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;

import java.io.IOException;

/**
 * Класс `RemoveFirst` реализует интерфейс `Command` и представляет команду удаления первого элемента из коллекции.
 * При выполнении команды удаляется первый элемент коллекции.
 */
public class RemoveFirst implements Command {
    /**
     * Коллекция `Deque`, из которой удаляется первый элемент.
     */
    private Deque deque;

    /**
     * Конструктор класса `RemoveFirst`.
     *
     * @param deque Коллекция `Deque`, из которой удаляется первый элемент.
     */
    public RemoveFirst(Deque deque) {
        this.deque = deque;
    }

    /**
     * Метод `execute` удаляет первый элемент из коллекции.
     */
    @Override
    public void execute() {
        deque.removeFirst();
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        StringBuilder rez = new StringBuilder("\n");

        if (deque.removeFirst()){
            rez.append("Успешно удален первый элемент").append("\n");
        }
        else{
            rez.append("Коллекция пуста").append("\n");
        }
        return new CommandResult(rez.toString());
    }
}