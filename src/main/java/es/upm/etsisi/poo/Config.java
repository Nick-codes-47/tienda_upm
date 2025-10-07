package es.upm.etsisi.poo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Config {

    public Config()
    {
        this(System.getProperty("user.dir") + "/" + CONFIG_DEFAULT_FILENAME);
    }

    public Config(String configFilePath)
    {
        this.configFilePath = configFilePath;

        System.err.printf("LOG::Config> Loading config from %s\n", configFilePath);
        loadConfig();
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

    public String[] getCategories()
    {
        return categories.keySet().toArray(new String[0]);
    }

    public int getMaxProducts()
    {
        return maxProducts;
    }

    public int getMaxProductPerTicket()
    {
        return maxProductPerTicket;
    }

    private void setMaxProducts(int value)
    {
        maxProducts = value;
    }

    private void setMaxProductPerTicket(int value)
    {
        maxProductPerTicket = value;
    }

    private void loadConfig()
            throws RuntimeException // TODO ConfigException?
    {
        try (Scanner scanner = new Scanner(new File(configFilePath)))
        {
            VariableLoader variableLoader = new VariableLoader(this);

            System.err.print("LOG::Config> Loading application config variables ... \n");
            variableLoader.loadVariables(scanner);
            System.err.print("LOG::Config> Loading application config variables completed\n");
            System.err.print("LOG::Config> Loading categories and its discounts ... \n");
            loadCategories(scanner);
            System.err.print("LOG::Config> Loading categories and its discounts completed\n");
        }
        catch (FileNotFoundException exception)
        {
            throw new RuntimeException(String.format("Missing config file: %s\n", configFilePath));
        }
    }

    private void loadCategories(Scanner scanner)
            throws RuntimeException // TODO ConfigException?
    {
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.isEmpty())
            {
                loadCategory(line);
            }
        }
    }

    private void loadCategory(String line)
            throws RuntimeException // TODO ConfigException?
    {
        try
        {
            int indexOfSeparator = line.indexOf('=');
            String category = line.substring(0, indexOfSeparator);
            Double discount = new Double(line.substring(indexOfSeparator + 1));
            categories.put(category, discount);
        }
        catch (IndexOutOfBoundsException | NumberFormatException exception)
        {
            throw new RuntimeException("Incorrect syntax for category line, expected: CATEGORY_NAME=(double)\n");
        }
    }

    private class VariableLoader {

        public VariableLoader(Config config)
        {
            this.config = config;
            initVariableEntries();
        }

        private void initVariableEntries()
        {
            variables.put(
                    "MAX_PRODUCTS",
                    new VariableEntry(
                            "MAX_PRODUCTS",
                            s -> config.setMaxProducts(Integer.parseInt(s)),
                            () -> String.valueOf(config.getMaxProducts())
                    )
            );
            variables.put(
                    "MAX_PRODUCTS_PER_TICKET",
                    new VariableEntry(
                            "MAX_PRODUCTS_PER_TICKET",
                            s -> config.setMaxProductPerTicket(Integer.parseInt(s)),
                            () -> String.valueOf(config.getMaxProductPerTicket())
                    )
            );
        }

        public void loadVariables(Scanner scanner)
                throws RuntimeException // TODO Config exception?
        {
            try
            {
                for (int i = 0; i < variables.size(); i++)
                {
                    loadVariable(scanner.nextLine());
                }
            }
            catch (NoSuchElementException exception)
            {
                System.err.print("ERROR::Config::VariableLoader> Missing variables in config file\n");
            }
            catch (IndexOutOfBoundsException exception)
            {
                System.err.print("ERROR::Config::VariableLoader> Syntax error in config file: expecting '=' " +
                        "after variable name\n");
            }

            checkLoadedVariables(variables);
        }

        private void loadVariable(String line)
                throws IndexOutOfBoundsException
        {
            String key = line.substring(0, line.indexOf('='));
            String value = line.substring(line.indexOf('=') + 1);
            VariableEntry variable = variables.get(key);
            if (variable != null)
            {
                variable.setter.accept(value);
                variable.loaded = true;

                System.err.printf("LOG::Config::VariableLoader> %s = %s\n", key, variable.getter.get());
            }
            else
            {
                System.err.printf("WARNING::Config::VariableLoader> %s variable does not exist\n", key);
            }
        }

        private void checkLoadedVariables(Map<String, VariableEntry> variables)
                throws RuntimeException // TODO ConfigExtension?
        {
            for (VariableEntry variable : variables.values())
            {
                if (!variable.loaded)
                {
                    throw new RuntimeException(
                            String.format("Missing variable %s\n", variable.name)
                    );
                }
            }
        }

        private class VariableEntry {
            public String name;
            public Consumer<String> setter;
            public Supplier<String> getter;
            boolean loaded = false;

            public VariableEntry(String name, Consumer<String> setter, Supplier<String> getter)
            {
                this.name = name;
                this.setter = setter;
                this.getter = getter;
            }
        } // class Variable Entry

        private final Config config;
        private final Map<String, VariableEntry> variables = new HashMap<>();
    } // class VariableLoader

    private static final String CONFIG_DEFAULT_FILENAME = "config.txt";

    private final String configFilePath;

    private final Map<String, Double> categories = new HashMap<>();

    private int maxProducts;
    private int maxProductPerTicket;
} // class Config
