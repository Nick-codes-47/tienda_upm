package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Ticket.Core.EntryArgs;

import java.util.ArrayList;

public class ProductEntryArgs implements EntryArgs {
    public int amount;
    public ArrayList<String> personalizations;
}
