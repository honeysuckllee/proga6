package org.labs;

import org.labs.Control.Command;
import org.labs.Control.Commands.*;
import org.labs.Control.Commands.base.CommandRequestContainer;
import org.labs.Control.Commands.base.CommandResponseContainer;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

import java.io.*;
import java.net.Socket;
import java.util.*;
/**
 * Класс клиента.
 * Используется для подключения к серверу.
 */
public class Client {
    /**
     * Конструктор
     *
     * @param port - порт подключения
     */
    public Client(int port) {
        this.port = port;
        useScript = new Stack<>();
        useScanners = new Stack<>();
    }

    /**
     * port - порт подключения
     */
    private static int port;
    /**
     * useScript - список выполняемых скриптов
     */
    private static Stack<String> useScript;
    /**
     * useScanners - список используемых сканеров
     */
    private static Stack<TransparentScannerWrapper> useScanners;

    /**
     * метод для взаимодействия с сервером
     */
    public static void run() {
        boolean success = false;
        while (!success) {
            try {
                TransparentScannerWrapper scn;
                TransparentScannerWrapper scanner = new TransparentScannerWrapper(new Scanner(System.in), true);
                scn = scanner; // текущий сканер
                useScanners.push(scanner);

                System.out.println("Клиентское приложение для управления коллекцией. ");
                System.out.println("Введите 'help' чтобы увидеть доступные команды или 'exit' для завершения работы.");

                HashMap<String, Command> commandHashMap = new HashMap<>();
                commandHashMap.put("help", new Help());
                commandHashMap.put("add", new Add());
                commandHashMap.put("show", new Show());
                commandHashMap.put("info", new CommandWithNotParametr());
                commandHashMap.put("remove_first", new CommandWithNotParametr());
                commandHashMap.put("remove_lower", new RemoveLower());
                commandHashMap.put("remove_by_id", new RemoveById());
                commandHashMap.put("add_if_max", new AddIfMax());
                commandHashMap.put("clear", new CommandWithNotParametr());
                commandHashMap.put("filter_starts_with_name", new FilterStartsWithName());
                commandHashMap.put("print_unique_distance", new CommandWithNotParametr());
                commandHashMap.put("print_field_descending_distance", new CommandWithNotParametr());
                commandHashMap.put("update", new Update());
                commandHashMap.put("execute_script", new ExecuteScript());

                // Создаем TCP-сокет и подключаемся к серверу
                Socket socket = new Socket("localhost", port);
                System.out.println("Подключение к серверу установлено.");

                // Получаем входной и выходной потоки для обмена данными с сервером
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();

                String line;
                String[] input;
                while (true) {
                    line = scanner.nextLine();
                    input = line.split(" ");

                    try {
                        if (input[0].equals("exit")) {
                            System.out.println("Завершение клиента.");
                            success = true;
                            // Закрываем соединение с сервером
                            socket.close();
                            break;
                        } else if (input[0].equals("execute_script")) {
                            if (input.length == 1) {
                                System.out.println("Скрипт не задан");
                                continue;
                            }
                            if (useScanners.empty()){
                                useScanners.push(scn);
                            }
                            String scriptsPath = System.getenv("ROUTE_SCRIPTS");
                            // создание сканера для скрипта
                            TransparentScannerWrapper scriptScanner = new TransparentScannerWrapper(new Scanner(new FileReader(scriptsPath + input[1])), false);
                            useScanners.push(scriptScanner);
                            useScript.push(scriptsPath + input[1]);
                            // делаем созданный сканер текущим
                            scn = scriptScanner;

                            while (useScanners.size() > 1) {
                                while (scn.hasNextLine()) {
                                    String[] scriptInput = scn.nextLine().split(" ");
                                    if (commandHashMap.containsKey(scriptInput[0])) {
                                        if (scriptInput[0].equals("execute_script")) {
                                            if (useScript.contains(scriptsPath + scriptInput[1])) {
                                                // убираем зацикливание
                                                continue;
                                            }
                                            // создаем новый сканер
                                            TransparentScannerWrapper embedScanner = new TransparentScannerWrapper(new Scanner(new FileReader(scriptsPath + scriptInput[1])), false);
                                            useScript.push(scriptsPath + scriptInput[1]);
                                            useScanners.push(embedScanner);
                                            scn = embedScanner;

                                        } else {
                                            CommandResult result = commandHashMap.get(scriptInput[0]).execute(scn, Arrays.copyOfRange(scriptInput, 1, scriptInput.length));
                                            CommandRequestContainer commandRequestContainer = new CommandRequestContainer(scriptInput[0], Arrays.copyOfRange(scriptInput, 1, scriptInput.length), result.getInput());

                                            // Отправка данных на сервер
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                                            objectOutputStream.writeObject(commandRequestContainer);

                                            //if (!result.getResult().isEmpty())
                                            //    System.out.println(result.getResult());

                                            // Получение ответа от сервера
                                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                                            CommandResponseContainer inputContainer = (CommandResponseContainer) objectInputStream.readObject();
                                            System.out.println(inputContainer.getResult());
                                        }
                                    } else if (scriptInput[0].equals("exit")) {
                                        break;
                                    } else {
                                        System.err.println("Команда " + Arrays.toString(input) + " не найдена.");
                                    }
                                }
                                if (!useScript.empty()) {
                                    useScript.pop();// убираем выполненный скрипт из списка
                                }
                                if (!useScanners.empty()) {
                                    scn = useScanners.pop(); // делаем предыдущий сканер текущим
                                }
                            }

                            if (!useScript.empty()) {
                                useScript.pop();
                            }
                            if (!useScanners.empty()) {
                                scn = useScanners.pop();
                            }
                            System.out.println("Выполнение скрипта завершено.");

                        } else if (commandHashMap.containsKey(input[0])) {

                            CommandResult result = commandHashMap.get(input[0]).execute(scn, Arrays.copyOfRange(input, 1, input.length));
                            CommandRequestContainer commandRequestContainer = new CommandRequestContainer(input[0], Arrays.copyOfRange(input, 1, input.length), result.getInput());

                            // Отправка данных на сервер
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(commandRequestContainer);

                            if (!result.getResult().isEmpty())
                                System.out.println(result.getResult());

                            // Получение ответа от сервера
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                            CommandResponseContainer inputContainer = (CommandResponseContainer) objectInputStream.readObject();
                            System.out.println(inputContainer.getResult());

                        } else {
                            System.err.println("Команда '" + input[0] + "' не найдена");
                        }
                    } catch (CommandException | ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                    } catch (FileNotFoundException e1) {
                        System.err.println("Файл не найден");
                    }
                }

                // Закрываем соединение с сервером
                //socket.close();

            } catch (NoSuchElementException e) {
                System.err.println("Не тыкай Ctrl+D\n");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}