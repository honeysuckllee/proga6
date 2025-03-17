package org.labs.Control.Commands;

import org.labs.Control.Command;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Coordinates;
import org.labs.Model.Location;
import org.labs.Service.TransparentScannerWrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.labs.Service.Utilites.*;

public class Update implements Command {
    private int id;

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

    public Update() {  }

    @Override
    public CommandResult execute(TransparentScannerWrapper scanner, String[] args, String... additionalInput) throws CommandException {
        if (args.length == 0){
            id = getValidInt(scanner, "Введите id:");
        }
        else{
            try{
                id = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e){
                id = 0;
                System.out.println("Введен некорректный id");
            }
        }
        name = getValidName(scanner);
        if (scanner.enable_out){
            System.out.println("Ввод Coordinates");}
        coordinates = getValidCoordinates(scanner);
        creationDate = LocalDate.now();
        if (scanner.enable_out){
            System.out.println("Ввод from:");}
        from = getValidLocation(scanner);
        if (scanner.enable_out){
            System.out.println("Ввод to:");}
        to = getValidLocation(scanner);
        if (scanner.enable_out){
            System.out.println("Ввод Distance");}
        distance = getValidFloatDistance(scanner);

        ArrayList<String> additional = new ArrayList<>();

        additional.add(String.valueOf(id));
        additional.add(name);

        additional.add(String.valueOf(coordinates.getX()));
        additional.add(String.valueOf(coordinates.getY()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        additional.add(creationDate.format(formatter));

        if (from != null) {
            additional.add(String.valueOf(from.getX()));
            additional.add(String.valueOf(from.getY()));
            additional.add(String.valueOf(from.getZ()));
        } else {
            additional.add("null");
        }
        if (to != null) {
            additional.add(String.valueOf(to.getX()));
            additional.add(String.valueOf(to.getY()));
            additional.add(String.valueOf(to.getZ()));
        } else {
            additional.add("null");
        }
        if (distance != null) {
            additional.add(String.valueOf(distance));
        } else {
            additional.add("null");
        }

        String[] to = new String[additional.size()];
        additional.toArray(to);
        return new CommandResult("", to);
    }
}
