package org.labs;
import java.io.*;
import java.net.*;
import java.util.*;
import lombok.Data;
import org.labs.Control.Command;
import org.labs.Control.Commands.*;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Model.Deque;
import org.labs.Control.Commands.base.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

@Data
public class ServerApp {
    private static int port;
    private static HashMap<String, Command> commandHashMap;
    private static Deque deq;
    public static final Logger logger = Logger.getLogger("Server");

    public ServerApp(int port) throws IOException {
        this.port = port;
        Handler handler = new FileHandler("log.txt");
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
    }

    public static void initCommandsMap(){
        commandHashMap = new HashMap<>();
        deq = new Deque();

        commandHashMap.put("add", new Add(deq));
        commandHashMap.put("execute_script", new ExecuteScript());
        commandHashMap.put("help", new Help());
        commandHashMap.put("show", new Show(deq.getDeque()));
        commandHashMap.put("info", new Info(deq));
        commandHashMap.put("remove_first", new RemoveFirst(deq));
        commandHashMap.put("remove_lower", new RemoveLower(deq));
        commandHashMap.put("remove_by_id", new RemoveById(deq));
        commandHashMap.put("add_if_max", new AddIfMax(deq));
        commandHashMap.put("clear",new Clear(deq));
        commandHashMap.put("print_unique_distance", new PrintUniqueDistance(deq));
        commandHashMap.put("filter_starts_with_name", new FilterStartsWithName(deq));
        commandHashMap.put("print_field_descending_distance", new PrintFieldDescendingDistance(deq));
        commandHashMap.put("update", new Update(deq));
    }
    public static void run() throws IOException, ClassNotFoundException {
        initCommandsMap();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Сервер начал работу.");
        logger.info("Сервер начал работу.");

        // Создаем серверный сокет и привязываем его к порту
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Сервер ожидает подключения на порту " + port);

        // Поток для обработки команд "exit"
        Thread exitThread = new Thread(() -> {
            while (true) {
                try {
                    String line = scanner.nextLine();
                    if (line.equals("exit")) {
                        logger.info("Сервер завершил работу.");
                        System.exit(0);
                    }
                } catch (NoSuchElementException e) {
                    System.err.println("Не тыкай Ctrl+D\n");
                }
            }
        });
        exitThread.start();

        // Основной цикл обработки клиентских подключений
        while (true) {
            // Ожидаем подключения клиента
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

            // Получаем входной и выходной потоки для обмена данными с клиентом
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            try {
                // Основной цикл обработки запросов от клиента
                while (true) {
                    // Чтение данных от клиента
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    CommandRequestContainer inputContainer;
                    try {
                        inputContainer = (CommandRequestContainer) objectInputStream.readObject();
                    } catch (EOFException e) {
                        // Клиент отключился
                        System.out.println("Клиент отключился: " + clientSocket.getInetAddress());
                        break;
                    }

                    String command = inputContainer.getCommandName();

                    try {
                        if (commandHashMap.containsKey(command)) {
                            CommandResult result = commandHashMap.get(command).execute(inputContainer.getArgs(), inputContainer.getInput());

                            CommandResponseContainer commandResponseContainer = new CommandResponseContainer(result.getResult());

                            // Отправка результата клиенту
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(commandResponseContainer);
                        } else {
                            System.err.println("Команда " + command + " не найдена.");
                            CommandResult result = new CommandResult("Команда " + command + " не найдена.");
                            CommandResponseContainer commandResponseContainer = new CommandResponseContainer(result.getResult());

                            // Отправка результата клиенту
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(commandResponseContainer);
                        }
                    } catch (CommandException e) {
                        CommandResponseContainer commandResponseContainer = new CommandResponseContainer(e.getMessage());

                        // Отправка результата клиенту
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.writeObject(commandResponseContainer);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при работе с клиентом: " + e.getMessage());
            } finally {
                // Закрываем соединение с клиентом
                clientSocket.close();
                System.out.println("Соединение с клиентом закрыто.");
            }
        }
    }

}

