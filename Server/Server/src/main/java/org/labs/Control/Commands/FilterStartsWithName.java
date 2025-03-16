package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;
import org.labs.Model.Route;
import org.labs.Service.TransparentScannerWrapper;

import java.util.Arrays;
import java.util.List;

import static org.labs.Service.Utilites.getValidName;

/**
 * Класс `FilterStartsWithName` реализует интерфейс `Command` и представляет команду фильтрации элементов коллекции,
 * которые начинаются с указанного имени.
 */
public class FilterStartsWithName implements Command {
    /**
     * Объект класса `Scanner` для чтения ввода пользователя.
     */
    private TransparentScannerWrapper scanner;

    /**
     * Коллекция `Deque`, с которой работает команда.
     */
    private Deque deque;

    /**
     * Имя, с которого должны начинаться элементы коллекции.
     */
    private String name;

    /**
     * Конструктор класса `FilterStartsWithName`.
     *
     * @param deque Коллекция `Deque`, с которой работает команда.
     */
    public FilterStartsWithName(Deque deque) {
        this.deque = deque;
    }

    /**
     * Метод `execute` выполняет команду фильтрации элементов коллекции, которые начинаются с указанного имени.
     * Если имя не было передано, запрашивает его у пользователя.
     */
    @Override
    public void execute() {
        if (name.isEmpty()) {
            name = getValidName(scanner);
        }
        deque.filterStartsWithName(name);
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        List<String> additional = Arrays.asList(additionalInput);
        this.name = additional.get(0);
        return new CommandResult(deque.filterStartsWithName(name));
    }
}