package org.labs;
import org.labs.Client;
import org.labs.Service.ConfigReader;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {
        Client client = new Client(ConfigReader.getPortFromConfig("src/main/java/org/labs/Service/config.json"));
        client.run();
    }
}