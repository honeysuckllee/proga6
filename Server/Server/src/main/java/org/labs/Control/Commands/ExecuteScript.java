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
     * Объект класса `Scanner` для чтения команд из файла.
     */
    private TransparentScannerWrapper scanner;


    /**
     * Конструктор класса `ExecuteScript`.
     *
     * @param scanner Объект класса `Scanner` для чтения команд из файла.
     */
    public ExecuteScript(TransparentScannerWrapper scanner) {
        this.scanner = scanner;

    }

    /**
     * Метод `execute` выполняет команду выполнения скрипта.
     * Читает команды из файла и передает их на выполнение в `CommandManager`.
     */
    @Override
    public void execute() {
        while (scanner.hasNextLine()) {
            String scn = scanner.nextLine();
            if (scn.equals("exit")){
                break;
            }

        }
        scanner.close();
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        StringBuilder rez = new StringBuilder("Выполняется скрипт "+ args[0] +"\n");
        return new CommandResult(rez.toString());
    }
}