package de.arnomann.martin.ntrmbot;

import java.io.*;

public class Configuration {

    private static String token = "";

    public static void read() {
        File file = new File("config.cfg");

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] splittedLine = line.split(": ", 2);

                if(splittedLine[0].equals("TOKEN"))
                    token = splittedLine[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getToken() {
        return token;
    }

}
