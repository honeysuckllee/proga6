package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;
import org.labs.Service.TransparentScannerWrapper;

import java.util.Arrays;
import java.util.List;

import static org.labs.Service.Utilites.getValidInt;

/**
 * Класс `RemoveById` реализует интерфейс `Command` и представляет команду удаления элемента из коллекции по его идентификатору.
 * Если идентификатор не был передан, он запрашивается у пользователя.
 */
public class RemoveById implements Command {
    /**
     * Коллекция `Deque`, из которой удаляется элемент.
     */
    private Deque deque;

    /**
     * Объект класса `Scanner` для чтения ввода пользователя.
     */
    private TransparentScannerWrapper scanner;

    /**
     * Идентификатор элемента, который необходимо удалить.
     */
    private int id;

    /**
     * Конструктор класса `RemoveById`.
     *

     * @param deque Коллекция `Deque`, из которой удаляется элемент.

     */
    public RemoveById( Deque deque) {
        this.deque = deque;
    }

    /**
     * Метод `execute` удаляет элемент из коллекции по его идентификатору.
     * Если идентификатор не был передан, он запрашивается у пользователя.
     */
    @Override
    public void execute() {
        if (id == 0) {
            id = getValidInt(scanner, "Введите id:");
        }
        deque.removeById(id);
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        List<String> additional = Arrays.asList(additionalInput);
        id = Integer.parseInt(additional.get(0));
        boolean dell = deque.removeById(id);
        if (dell){
            return new CommandResult("Элемент удален\n");
        }
        return new CommandResult("Элемент не может быть удален\n");
    }
}