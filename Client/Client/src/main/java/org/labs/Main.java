package org.labs;
import org.labs.Service.ConfigReader;


public class Main {

    public static void main(String[] args) {
        Client client = new Client(ConfigReader.getPortFromConfig("C:\\Users\\Светлана\\IdeaProjects\\lab6\\Client\\Client\\src\\main\\java\\org\\labs\\Service\\config.json"));
        client.run();
    }
}