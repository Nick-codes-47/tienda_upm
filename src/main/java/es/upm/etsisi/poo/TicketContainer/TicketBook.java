package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.ProductContainer.ProductTypes.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.CustomProduct;

import java.util.*;

public class TicketBook {
    private HashMap<String, TicketEntry> tickets;
    private HashMap<String, ArrayList<String>> userToTicket;
    private final Random random = new Random();

    public TicketBook() {
        this.tickets = new HashMap<>();
        this.userToTicket = new HashMap<>();
    }

    public HashMap<String, TicketEntry> getTickets() {
        return tickets;
    }

    public TicketEntry getTicketByTicketId(String ticketId) {
        return tickets.get(ticketId);
    }

    /**
     * Method to obtain all the tickets that contain a product
     *
     * @return Tickets that contain the product
     */
    public ArrayList<Ticket> getOpenedTicketsWithProd(int productId) {
        ArrayList<Ticket> ticketsWithProd = new ArrayList<>();
        // We look which tickets have the product and are opened
        for (TicketEntry ticketEntry : this.tickets.values()) {
            if (ticketEntry.ticket.hasProduct(productId) && !ticketEntry.ticket.isClosed())
                ticketsWithProd.add(ticketEntry.ticket);
        }
        return ticketsWithProd;
    }

    /**
     * Crea una nueva instancia de Ticket y su TicketEntry asociado, y la registra.
     * Genera un ID único si no se proporciona uno, o verifica la unicidad del proporcionado.
     *
     * @param ticketId   El ID único del nuevo ticket (puede ser null/vacío para generar uno).
     * @param cashId     El ID del cajero asociado.
     * @param customerId El ID del cliente asociado.
     * @return 0 si es exitoso, -1 si el ticketId ya existe.
     */
    public int addNewTicket(String ticketId, String cashId, String customerId) {
        String finalTicketId;

        if (ticketId == null || ticketId.isEmpty()) {
            finalTicketId = generateUniqueTicketId();
        } else {
            finalTicketId = ticketId;
            if (this.tickets.containsKey(finalTicketId)) {
                return -1;
            }
        }

        Ticket newTicket = new Ticket(finalTicketId);
        TicketEntry newEntry = new TicketEntry(cashId, customerId, newTicket);

        this.tickets.put(finalTicketId, newEntry);

        userToTicket.computeIfAbsent(cashId, k -> new ArrayList<>());
        userToTicket.computeIfAbsent(customerId, k -> new ArrayList<>());


        this.userToTicket.get(cashId).add(finalTicketId);
        this.userToTicket.get(customerId).add(finalTicketId);

        Ticket t = getTicketIfCashierMatches(finalTicketId, cashId);
        System.out.println(t.toString());

        return 0;
    }

    private String generateUniqueTicketId() {
        String fullId;

        do {
            int randomPart = 10000 + random.nextInt(90000);
            fullId = randomPart + "";
        } while (this.tickets.containsKey(fullId));

        return fullId;
    }

    /**
     * Obtiene una lista de entradas de tickets ordenadas por ID de Cajero.
     *
     * @return Lista de entradas de tickets.
     */
    public List<TicketEntry> listTicketsSortedByCashierId() {
        List<TicketEntry> allEntries = new ArrayList<>(tickets.values());
        allEntries.sort(Comparator.comparing(e -> e.cashId));
        return allEntries;
    }

    /**
     * Obtiene un Ticket si su ID existe y el ID del cajero asociado coincide.
     * Este método se utiliza para acciones que requieren que el cajero esté
     * autorizado para manipular el ticket (como imprimirlo).
     *
     * @param ticketId El ID único del ticket.
     * @param cashId   El ID del cajero que intenta acceder al ticket.
     * @return El objeto Ticket si se encuentra y el cajero coincide, de lo contrario null.
     */
    public Ticket getTicketIfCashierMatches(String ticketId, String cashId) {
        TicketEntry entry = this.tickets.get(ticketId);

        if (entry == null) {
            return null;
        }

        if (entry.cashId.equals(cashId)) {
            return entry.ticket;
        } else {
            return null;
        }
    }

    /**
     * Intenta añadir un producto a un ticket dado por su ID.
     *
     * @param ticketId         ID del ticket.
     * @param cashId           ID del cajero autorizado.
     * @param product          product a añadir.
     * @param amount           Cantidad (o número de personas).
     * @param personalizations Lista de strings de personalización.
     * @return 0 si es exitoso. Códigos de error remapeados para la acción.
     */
    public int addProductToTicket(String ticketId, String cashId, BaseProduct product, int amount, ArrayList<String> personalizations) {
        Ticket ticket = getTicketIfCashierMatches(ticketId, cashId);
        if (ticket == null) {
            return -1;
        }

        if (product == null) {
            return -3;
        }

        int result = 0;

        if (product instanceof CustomProduct customProduct) {
            result = ticket.addProduct(customProduct, amount, personalizations);
        } else {
            result = ticket.addProduct(product, amount);
        }

        if (result == -3) {
            return -4;
        } else if (result == -1) {
            return -5;
        } else if (result == -4) {
            return -6;
        } else if (result == -5) {
            return -7;
        } else if (result == -8) {
            return -8;
        }
        return 0;
    }

    /**
     * Method to remove a product from a ticket
     *
     * @param ticketId ticket id
     * @param cashId   cash id
     * @param prodId   product id to be removed
     * @return 0 product removed successfully from ticket
     *        -1 ticket state is closed
     *        -2 product not found in ticket
     *        -3 ticket not found
     */
    public int removeProduct(String ticketId, String cashId, int prodId) {
        Ticket ticket = getTicketIfCashierMatches(ticketId, cashId);
        if (ticket == null) {
            return -3;
        }

//        int result = ticket.deleteProduct(prodId);
//
//        if (result == -3) {
//            return -4;
//        } else if (result == -1) {
//            return -5;
//        }

        return ticket.deleteProduct(prodId);
    }

    public List<TicketEntry> getTicketsFrom(String cashierId) {
        if (!userToTicket.containsKey(cashierId))
            return null;

        List<TicketEntry> ticketsEntries = new ArrayList<>();
        for (String ticketId : userToTicket.get(cashierId)) {
            ticketsEntries.add(tickets.get(ticketId));
        }

        return ticketsEntries;
    }

    public int removeTicketsFrom(String userId) {
        if (userId == null)
            return 1;

        ArrayList<String> ticketIds = userToTicket.get(userId);
        userToTicket.remove(userId);

        if (ticketIds != null)
            for (String ticketId : ticketIds) {
                TicketEntry ticket = tickets.get(ticketId);
                userToTicket.get(ticket.customerId).remove(ticketId);
            }

        return 0;
    }
}