package es.upm.etsisi.poo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Config {
    private static final String CONFIG_DEFAULT_FILENAME = "config.txt";

    private String configFileName;

    private Map<String, Double> categories;

    private int maxProducts;
    private int maxProductPerTicket;

    public Config()
    {
        this(System.getProperty("user.dir") + CONFIG_DEFAULT_FILENAME);
    }

    public Config(String configFileName)
    {
        this.configFileName = configFileName;

        System.err.printf("LOG::Config> Loading config from file %s\n", configFileName);
        loadConfigFile(configFileName);
    }

    private void loadConfigFile(String configFilePath)
    {
        try (Scanner scanner = new Scanner(new File(configFilePath)))
        {
            System.err.printf("LOG::Config> Loading application variables...\n");
            loadVariables(scanner);
            System.err.printf("LOG::Config> Loading categories and its discounts ...\n");

        }
        catch (FileNotFoundException exception)
        {
            System.out.printf("Configuration load failure: missing %s file in %s\n", configFilePath);
        }
    }

    public void loadVariables(Scanner scanner)
    {
        Map<String, Consumer> variables = Map.of(
                "MaxProducts", Config::setMaxProducts
        );

        for (String variable: variables.keySet())
        {
            System.err.printf("LOG::Config> Getting %s value ...\n", variable);
        }

        setMaxProducts(Integer.parseInt(scanner.nextLine()));
        System.err.printf("LOG::Config> MaxProducts set with value %d\n", maxProducts);
        System.err.printf("LOG::Config> Getting MaxProductPerTicket value ...\n");
        setMaxProductPerTicket(Integer.parseInt(scanner.nextLine()));
        System.err.printf("LOG::Config> MaxProductPerTicket set with value %d\n", maxProductPerTicket);
    }

    public boolean validCategory(String category)
    {
        return categories.containsKey(category);
    }

    public Double getDiscount(String category)
    {
        Double discount = null;

        if (categories.containsKey(category))
        {
            discount = categories.get(category);
        }

        return discount;
    }

    public int getMaxProducts()
    {
        return maxProducts;
    }

    private void setMaxProducts(int value)
    {
        maxProducts = value;
    }

    public int getMaxProductPerTicket()
    {
        return maxProductPerTicket;
    }

    private int setMaxProductPerTicket(int value)
    {
        maxProductPerTicket = value;
    }
}
