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

public class Update implements Command {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле может быть null
    private Location to; //Поле может быть null
    private Float distance;
    private Deque deque;
    private int id;

    public Update(Deque deque) {
        this.deque = deque;
    }

    @Override
    public void execute() {
    }

    @Override
    public CommandResult execute(String[] args, String... additionalInput) throws CommandException {
        int index = 0;
        List<String> additional = Arrays.asList(additionalInput);
        // id
        int id = Integer.parseInt(additional.get(index++)); // id

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
        if (additional.get(index).equals("null")) {
            this.from = null;
        }
        else {
            int fromX = Integer.parseInt(additional.get(index++));
            Float fromY = Float.parseFloat(additional.get(index++));
            int fromZ = Integer.parseInt(additional.get(index++));
            this.from = new Location(fromX, fromY, fromZ);
        }

        // Парсим to
        if (additional.get(index).equals("null")) {
            this.to = null;
        }
        else {
            int toX = Integer.parseInt(additional.get(index++));
            Float toY = Float.parseFloat(additional.get(index++));
            int toZ = Integer.parseInt(additional.get(index++));
            this.to = new Location(toX, toY, toZ);
        }

        // Парсим distance
        if (additional.get(index).equals("null")) {
            this.distance = null;
        }
        else {
            this.distance = Float.parseFloat(additional.get(index));
        }



        return new CommandResult(deque.updateRoute(id, name, coordinates, creationDate, from, to, distance));

    }
}