package org.labs;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerApp server = new ServerApp(ConfigReader.getPortFromConfig("C:\\Users\\Светлана\\IdeaProjects\\lab6\\Server\\Server\\src\\main\\resources\\config.json"));
        server.run();
    }
}