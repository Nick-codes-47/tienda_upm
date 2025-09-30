package es.upm.etsisi.poo;

import java.util.Map;
import java.util.Scanner;

public class Config {
    private Map<String, Double> categories;

    private int maxProducts;
    private int maxProductPerTicket;

    public Config()
    {
        this(System.getProperty("user.dir"));
    }

    public Config(String configFilePath)
    {
        loadConfigFile(configFilePath);
    }

    private void loadConfigFile(String configFilePath)
    {
        Scanner scanner = new Scanner(new File(configFilePath));
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

    public int getMaxProductPerTicket()
    {
        return maxProductPerTicket;
    }
}
