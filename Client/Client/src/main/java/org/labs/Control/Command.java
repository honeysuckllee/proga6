package org.labs.Control;

import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

import java.util.Scanner;

/**
 * Интерфейс, представляющий команду.
 * Классы, реализующие этот интерфейс, должны предоставить реализацию метода execute(),
 * который выполняет определённое действие.
 */
public interface Command {
    /**
     * Выполняет команду.
     * Конкретная реализация этого метода зависит от класса, реализующего интерфейс.
     */
    CommandResult execute(TransparentScannerWrapper scanner, String[] args, String ... additionalInput) throws CommandException;

}