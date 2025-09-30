package es.upm.etsisi.poo;

import java.util.ArrayList;

public class App
{
    Ticket currentTicket;
    ArrayList<Product> productList;
    Config config;

    public static void main( String[] args ) {
    }

    public void printTicket() {
        System.out.println(currentTicket.toString());
    }

    public void printProdList() {
        System.out.println("Catalog: ");
        for (Product p : this.productList) {
            System.out.println(" "+p.toString());
        }
    }
}
