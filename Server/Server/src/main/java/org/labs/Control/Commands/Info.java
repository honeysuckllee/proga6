package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;

import java.io.IOException;
import java.util.Map;

/**
 * Класс `Info` реализует интерфейс `Command` и представляет команду вывода информации о коллекции.
 * При выполнении команды выводятся тип коллекции, время создания и количество элементов.
 */
public class Info implements Command {
    /**
     * Коллекция `Deque`, для которой выводится информация.
     */
    private Deque deq;

    /**
     * Конструктор класса `Info`.
     *
     * @param deque Коллекция `Deque`, для которой выводится информация.
     */
    public Info(Deque deque) {
        deq = deque;
    }

    /**
     * Метод `execute` выводит информацию о коллекции, включая тип коллекции,
     * время создания и количество элементов.
     * Если возникает ошибка при получении времени создания, выбрасывается исключение `RuntimeException`.
     */
    @Override
    public void execute() {
        System.out.println("Тип коллекции:" + deq.getCollectionType());
        try {
            System.out.println("Время создания коллекции:" + deq.getCollectionFileCreationDate());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Количество элементов в коллекции:" + deq.getDeque().size());
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        StringBuilder rez = new StringBuilder("Информация о коллекции:\n");
        rez.append("Тип коллекции:" + deq.getCollectionType()).append("\n");
        try {
            rez.append("Время создания коллекции:" + deq.getCollectionFileCreationDate()).append("\n");
        } catch (IOException e) {
            return new CommandResult("Ошибка\n");
        }
        rez.append("Количество элементов в коллекции:" + deq.getDeque().size()).append("\n");
        return new CommandResult(rez.toString());
    }
}