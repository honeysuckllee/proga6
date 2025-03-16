package org.labs;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
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
        //commandHashMap.put("execute_script");
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

        InetAddress hostIP = InetAddress.getLocalHost();
        InetSocketAddress address = new InetSocketAddress(hostIP, port);
        DatagramChannel datagramChannel = DatagramChannel.open();
        DatagramSocket datagramSocket = datagramChannel.socket();
        datagramSocket.bind(address);

        ByteBuffer buffer = ByteBuffer.allocate(4096 * 4);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            /*try {
                commandHashMap.get("save").execute(new String[]{});
                System.out.println("Collection saved. Server stopped.");
            } catch (CommandException e) {
                e.printStackTrace();
            }*/
        }));
        // exit command thread
        Thread exitThread = new Thread(() -> {
            while (true) {
                try {
                    String line = scanner.nextLine();
                    if (line.equals("exit")) {
                        logger.info("Сервер завершил работу.");
                        System.exit(0);
                    } else if (line.equals("save")) {
                       /* try {
                            commandHashMap.get("save").execute(new String[]{});
                            System.out.println("Collection saved");
                        } catch (CommandException e) {
                            e.printStackTrace();
                        }*/
                    } else {
                        System.err.println("Wrong command. You can use only 'exit' command to stop server or 'save' to save collection.");
                    }
                } catch (NoSuchElementException e) {
                    System.err.println("What are you doing? Stop that! Don't Ctrl+D my program, you are not good..." + "\n" +
                            "Restart the program and be polite.");
                }
            }
        });
        exitThread.start();
        while (true) {
            SocketAddress from = datagramChannel.receive(buffer);
            System.out.println(from);
            buffer.flip();
            System.out.print("\nData...: ");
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getMimeDecoder().decode(bytes));
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            CommandRequestContainer inputContainer = (CommandRequestContainer) objectInputStream.readObject();

            String command = inputContainer.getCommandName();
            buffer.clear();

            try {
                if (commandHashMap.containsKey(command)) {
                    CommandResult result = commandHashMap.get(command).execute(inputContainer.getArgs(), inputContainer.getInput());
                    System.out.println(result.getResult());

                    CommandResponseContainer commandResponseContainer = new CommandResponseContainer(result.getResult());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
                    objectOutputStream.writeObject(commandResponseContainer);

                    datagramChannel.send(ByteBuffer.wrap(Base64.getMimeEncoder().encode(baos.toByteArray())), from);
                } else {
                    System.err.println("Команда " + command + " не найдена.");
                    CommandResult result = new CommandResult("Команда " + command + " не найдена.");
                    CommandResponseContainer commandResponseContainer = new CommandResponseContainer(result.getResult());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
                    objectOutputStream.writeObject(commandResponseContainer);

                    datagramChannel.send(ByteBuffer.wrap(Base64.getMimeEncoder().encode(baos.toByteArray())), from);
                }
            } catch (CommandException e) {
                CommandResponseContainer commandResponseContainer = new CommandResponseContainer(e.getMessage());

                ByteArrayOutputStream bass = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(bass);
                objectOutputStream.writeObject(commandResponseContainer);

                datagramChannel.send(ByteBuffer.wrap(Base64.getMimeEncoder().encode(bass.toByteArray())), from);
            }
        }
    }

}

