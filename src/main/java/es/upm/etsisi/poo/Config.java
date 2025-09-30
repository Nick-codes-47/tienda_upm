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
    private static final String CONFIG_DEFAULT_FILENAME = "config.txt";

    private final String configFileName;

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

    private void setMaxProductPerTicket(int value)
    {
        maxProductPerTicket = value;
    }

    private void loadConfigFile(String configFilePath)
    {
        try (Scanner scanner = new Scanner(new File(configFilePath)))
        {
            VariableLoader variableLoader = new VariableLoader(this);

            System.err.printf("LOG::Config> Loading application variables...\n");
            variableLoader.loadVariables(scanner);
            System.err.printf("LOG::Config> Loading categories and its discounts ...\n");
//            loadCategories(scanner);
        }
        catch (FileNotFoundException exception)
        {
            System.out.printf("Configuration load failure: missing %s file in %s\n", configFilePath);
        }

    }

    private class VariableLoader {
        private final Config config;
        public Map<String, VariableEntry> variables = new HashMap<>();

        VariableLoader(Config config)
        {
            this.config = config;
            initVariableEntries();
        }

        private void initVariableEntries()
        {
            variables.put(
                    "MaxProducts",
                    new VariableEntry(
                            "MaxProducts",
                            s -> config.setMaxProducts(Integer.parseInt(s)),
                            () -> String.valueOf(config.getMaxProducts())
                    )
            );
            variables.put(
                    "MaxProductsPerTicket",
                    new VariableEntry(
                            "MaxProductsPerTicket",
                            s -> config.setMaxProductPerTicket(Integer.parseInt(s)),
                            () -> String.valueOf(config.getMaxProductPerTicket())
                    )
            );
        }

        public void loadVariables(Scanner scanner)
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
                System.err.printf("ERROR::Config::VariableLoader> Missing variables in config file\n");
            }
            catch (IndexOutOfBoundsException exception)
            {
                System.err.printf("ERROR::Config::VariableLoader> Syntax error in config file: expecting '=' after variable name\n");
            }

            checkLoadedVariables(variables);
        }

        private void loadVariable(String line)
        {
            String key = getVariableKey(line);
            String value = getVariableValue(line);
            VariableEntry variable = variables.get(key);
            variable.setter.accept(value);

            System.err.printf("LOG::Config::VariableLoader> %s = %s", key, variable.getter.get());
        }

        private void checkLoadedVariables(Map<String, VariableEntry> variables)
        {
            for (VariableEntry variable : variables.values())
            {
                if (!variable.loaded)
                {
                    throw new RuntimeException(
                            String.format("Missing variable %s\n", variable.name.toUpperCase())
                    );
                }
            }
        }

        private String getVariableKey(String line)
        {
            return line.substring(0, line.indexOf('='));
        }

        private String getVariableValue(String line)
        {
            return line.substring(line.indexOf('=') + 1);
        }

        private class VariableEntry {
            public String name;
            public Consumer<String> setter;
            public Supplier<String> getter;
            boolean loaded = false;

            VariableEntry(String name, Consumer<String> setter, Supplier<String> getter)
            {
                this.name = name;
                this.setter = setter;
                this.getter = getter;
            }
        }
    }
}
