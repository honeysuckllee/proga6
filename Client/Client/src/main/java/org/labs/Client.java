package org.labs;

import org.labs.Control.Command;
import org.labs.Control.Commands.*;
import org.labs.Control.Commands.base.CommandRequestContainer;
import org.labs.Control.Commands.base.CommandResponseContainer;
import org.labs.Control.Commands.base.CommandResult;
import org.labs.Control.Commands.exception.CommandException;
import org.labs.Service.TransparentScannerWrapper;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;

public class Client {
    public Client(int port) {
        this.port = port;
        useScript = new Stack<>();
    }

    private static int port;
    private static Stack<String> useScript;

    public static void run(){
        boolean success = false;
        while (!success) {
            try {
                Scanner scanner = new Scanner(System.in);
                TransparentScannerWrapper scn = new TransparentScannerWrapper(new Scanner(System.in), true);

                System.out.println("Клиентское приложение для управления коллекцией. ");
                System.out.println("Введите 'help' чтобы увидеть доступные команды или 'exit' для завершения работы.");

                HashMap<String, Command> commandHashMap = new HashMap<>();
                commandHashMap.put("help", new Help());
                commandHashMap.put("add", new Add(scn));
                commandHashMap.put("show", new Show());
                commandHashMap.put("info", new CommandWithNotParametr());
                commandHashMap.put("remove_first", new CommandWithNotParametr());
                commandHashMap.put("remove_lower", new RemoveLower(scn));
                commandHashMap.put("remove_by_id", new RemoveById(scn));
                commandHashMap.put("add_if_max", new AddIfMax(scn));
                commandHashMap.put("clear", new CommandWithNotParametr());
                commandHashMap.put("filter_starts_with_name", new FilterStartsWithName(scn));
                commandHashMap.put("print_unique_distance", new CommandWithNotParametr());
                commandHashMap.put("print_field_descending_distance", new CommandWithNotParametr());
                commandHashMap.put("update", new Update(scn));
                commandHashMap.put("execute_script", new ExecuteScript(scn));


                InetAddress hostIP = InetAddress.getLocalHost();
                InetSocketAddress myAddress = new InetSocketAddress(hostIP, port);
                DatagramChannel datagramChannel = DatagramChannel.open();
                datagramChannel.bind(null);
                datagramChannel.connect(myAddress);

                ByteBuffer buffer = ByteBuffer.allocate(4096*4);

                String line;
                String[] input;
                while (true) {
                    line = scanner.nextLine();
                    input = line.split(" ");

                    try {
                        if (input[0].equals("exit")) {
                            System.out.println("Завершение клиента.");
                            success = true;
                            break;
                        }
                        else if (input[0].equals("execute_script")) {
                            if (input.length == 1) {
                                System.out.println("Скрипт не задан");
                                continue;
                            }
                            String scriptsPath = System.getenv("ROUTE_SCRIPTS");
                            Scanner scriptScanner = new Scanner(new FileReader(scriptsPath + input[1]));
                            scn.swapScanner(scriptScanner, false);
                            if (useScript.contains(scriptsPath + input[1]))
                            {
                                // зацикливание
                                continue;
                            }


                            while (scriptScanner.hasNextLine()) {
                                String[] scriptInput = scriptScanner.nextLine().split(" ");
                                if (commandHashMap.containsKey(scriptInput[0])) {
                                    CommandResult result = commandHashMap.get(scriptInput[0]).execute(Arrays.copyOfRange(scriptInput, 1, scriptInput.length));
                                    CommandRequestContainer commandRequestContainer = new CommandRequestContainer(scriptInput[0], Arrays.copyOfRange(scriptInput, 1, scriptInput.length), result.getInput());

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
                                    objectOutputStream.writeObject(commandRequestContainer);

                                    if (!result.getResult().isEmpty())
                                        System.out.println(result.getResult());
                                    buffer.put(Base64.getMimeEncoder().encode(baos.toByteArray()));
                                    buffer.flip();
                                    datagramChannel.send(buffer, myAddress);
                                    buffer.clear();
                                    buffer.rewind();
                                    int bytesRead = datagramChannel.read(buffer);
                                    if (bytesRead != -1) {
                                        buffer.rewind();
                                        byte[] b = new byte[bytesRead];
                                        for (int i = 0; i < bytesRead; i++) {
                                            b[i] = buffer.get();
                                        }

                                        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getMimeDecoder().decode(b));
                                        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
                                        CommandResponseContainer inputContainer = (CommandResponseContainer) objectInputStream.readObject();
                                        System.out.println(inputContainer.getResult());
                                    }

                                    buffer.clear();
                                } else if (scriptInput[0].equals("exit")){
                                    break;
                                } else
                                {
                                    System.err.println("Команда " + Arrays.toString(input) + " не найдена.");
                                }
                            }
                            if (!useScript.empty()) { useScript.pop();}
                            System.out.println("Выполнение скрипта завершено.");
                            scn.swapScanner(scanner, true);

                        } else if (commandHashMap.containsKey(input[0])) {

                            CommandResult result = commandHashMap.get(input[0]).execute(Arrays.copyOfRange(input, 1, input.length));
                            CommandRequestContainer commandRequestContainer = new CommandRequestContainer(input[0], Arrays.copyOfRange(input, 1, input.length), result.getInput());

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
                            objectOutputStream.writeObject(commandRequestContainer);

                            if (!result.getResult().isEmpty())
                                System.out.println(result.getResult());
                            buffer.put(Base64.getMimeEncoder().encode(baos.toByteArray()));
                            buffer.flip();
                            datagramChannel.send(buffer, myAddress);
                            buffer.clear();
                            buffer.rewind();
                            int bytesRead = datagramChannel.read(buffer);
                            if (bytesRead != -1) {
                                buffer.rewind();
                                byte[] b = new byte[bytesRead];
                                for (int i = 0; i < bytesRead; i++) {
                                    b[i] = buffer.get();
                                }
                                ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getMimeDecoder().decode(b));
                                ObjectInputStream objectInputStream = new ObjectInputStream(bais);
                                CommandResponseContainer inputContainer = (CommandResponseContainer) objectInputStream.readObject();
                                System.out.println(inputContainer.getResult());
                            }

                            buffer.clear();
                        } else {
                            System.err.println("Command '" + input[0] + "' not found");
                        }
                    } catch (CommandException | ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                    } catch (PortUnreachableException e) {
                        System.err.println("Server is unavailable");
                    } catch (FileNotFoundException e1) {
                        System.err.println("File not found");
                    }
                }

            } catch (NoSuchElementException e) {
                System.err.println("What are you doing? Stop that! Don't Ctrl+D my program, you are not good..." + "\n" +
                        "Restart the program and be polite.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}