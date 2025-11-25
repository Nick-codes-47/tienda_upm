package es.upm.etsisi.poo.Requests;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    public String handlerId = "";
    public String actionId = "";
    public String[] args;

    public Request(String line)
    {
        ArrayList<String> tokens = tokenize(line);

        if (!tokens.isEmpty()) {
            handlerId = tokens.get(0);
            if (tokens.size() > 1) {
                actionId = tokens.get(1);
                if (tokens.size() > 2) {
                    args = tokens.subList(2, tokens.size()).toArray(new String[0]);
                }
            }
        }

//        System.err.printf("LOG::Command> command line received: %s %s %s\n", family, command, args);
    }

    private static ArrayList<String> tokenize(String line)
    {
        ArrayList<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(line); // get Strings between spaces to group 1 OR Strings between quotes to group 2

        // Fuses both groups of words
        while (m.find()) {
            if (m.group(1) != null)
                tokens.add(m.group(1)); // quoted part
            else
                tokens.add(m.group(2)); // normal part
        }

        return tokens;
    }
}
