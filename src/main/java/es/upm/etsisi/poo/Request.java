package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.Arrays;

public class Request {
    public String family;
    public String command;
    public ArrayList<String> args;

    public Request(String line)
    {
        String[] tokens = line.split("\\s+"); // using spaces without living empty Strings
        family = tokens[0];
        if (tokens.length > 1)
        {
            command = tokens[1];
            args = extractArgs(tokens);
        }

        System.err.printf("LOG::Command> command line received: %s %s %s\n", family, command, args);
    }

    private ArrayList<String> extractArgs(String[] tokens)
    {
        return new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(tokens, 2, tokens.length)));
    }
}
