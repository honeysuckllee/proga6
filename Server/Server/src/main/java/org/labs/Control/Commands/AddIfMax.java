package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Coordinates;
import org.labs.Model.Deque;
import org.labs.Model.Location;
import org.labs.Service.TransparentScannerWrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.labs.Service.Utilites.*;

/**
 * Класс `AddIfMax` реализует команду добавления нового элемента `Route` в коллекцию `Deque`,
 * только если его `id` больше, чем текущий максимальный `id` в коллекции.
 */
public class AddIfMax implements Command {
    /**
     * Имя маршрута. Поле не может быть null, строка не может быть пустой.
     */
    private String name;
    /**
     * Координаты маршрута. Поле не может быть null.
     */
    private Coordinates coordinates;
    /**
     * Дата создания маршрута. Поле не может быть null, значение этого поля должно генерироваться автоматически.
     */
    private LocalDate creationDate;
    /**
     * Начальная локация маршрута. Поле может быть null.
     */
    private Location from;
    /**
     * Конечная локация маршрута. Поле может быть null.
     */
    private Location to;
    /**
     * Дистанция маршрута.
     */
    private Float distance;
    /**
     * Коллекция `Deque`, в которую добавляется маршрут.
     */
    private Deque deque;
    /**
     * Идентификатор маршрута.
     */
    private int id;
    /**
     * Объект `Scanner` для чтения ввода пользователя.
     */
    private TransparentScannerWrapper scanner;

    /**
     * Конструктор класса `AddIfMax`.
     *
     * @param deque   Коллекция `Deque`, в которую добавляется маршрут.
     */
    public AddIfMax(Deque deque) {
        this.deque = deque;
    }

    /**
     * Выполняет команду добавления нового элемента `Route` в коллекцию, если его `id` больше максимального.
     * Запрашивает у пользователя данные для создания объекта `Route` и добавляет его в коллекцию `Deque`,
     * только если `id` больше, чем `maxId` в коллекции.
     */
    @Override
    public void execute() {

    }


    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        int index = 0;
        List<String> additional = Arrays.asList(additionalInput);
        // id
        int id = Integer.parseInt(additional.get(index++)); // id
        if (id > deque.getMaxId()) {

            // Парсим name
            this.name = additional.get(index++);

            // Парсим coordinates
            double x = Double.parseDouble(additional.get(index++));
            float y = Float.parseFloat(additional.get(index++));
            this.coordinates = new Coordinates(x, y);

            // Парсим creationDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            this.creationDate = LocalDate.parse(additional.get(index++), formatter);

            // Парсим from
            int fromX = Integer.parseInt(additional.get(index++));
            Float fromY = Float.parseFloat(additional.get(index++));
            int fromZ = Integer.parseInt(additional.get(index++));
            this.from = new Location(fromX, fromY, fromZ);

            // Парсим to
            int toX = Integer.parseInt(additional.get(index++));
            Float toY = Float.parseFloat(additional.get(index++));
            int toZ = Integer.parseInt(additional.get(index++));
            this.to = new Location(toX, toY, toZ);

            // Парсим distance
            this.distance = Float.parseFloat(additional.get(index));

            deque.addRoute(id, name, coordinates, creationDate, from, to, distance);

            return new CommandResult("Маршрут добавлен\n");
        }
        else{
            return new CommandResult("Введенное значение меньше максимального id коллекции\n ");
        }
    }
}
