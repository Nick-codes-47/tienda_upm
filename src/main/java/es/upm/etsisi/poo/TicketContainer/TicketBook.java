package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketBook {
    private HashMap<String, TicketEntry> tickets;
    private HashMap<String, String[]> userToTicket;
    private static final DateTimeFormatter ID_DATE_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm-");
    private final Random random = new Random();
    private final App app;

    public TicketBook(App app) {
        this.app = app;
        this.tickets = new HashMap<>();
        this.userToTicket = new HashMap<>();
    }

    public HashMap<String, TicketEntry> getTickets() {
        return tickets;
    }

    /**
     * Obtiene todos los TicketEntry asociados a un usuario específico.
     * La clave del HashMap de retorno es el TicketId (String)
     * para coincidir con el formato de ID de ticket (YY-MM-dd-HH:mm-XXXXX).
     * @param userId El ID del cliente cuyos tickets se buscan.
     * @return HashMap donde la clave es el TicketId (String) y el valor es el TicketEntry.
     */
    public HashMap<String, TicketEntry> getTicketsFromUsers(String userId) {
        HashMap<String, TicketEntry> userTickets = new HashMap<>();

        String[] ticketIds = userToTicket.get(userId);

        if (ticketIds == null || ticketIds.length == 0) {
            return userTickets;
        }

        for (String ticketId : ticketIds) {
            TicketEntry entry = tickets.get(ticketId);

            if (entry != null) {
                userTickets.put(ticketId, entry);
            }
        }

        return userTickets;
    }

    /**
     * Method to obtain all the tickets that had deleted a product
     * @param product the product to delete
     */
    public void deleteProdFromTickets(BaseProduct product) {
        for (TicketEntry ticketEntry : tickets.values()) {
            Ticket ticket = ticketEntry.ticket;
            if (!ticket.isClosed())
                // we only delete if the ticket is still open
                ticket.deleteProduct(product);
        }
    }

    /**
     * Method to obtain all the tickets that contain a product
     * @return Tickets that contain the product
     */
    public ArrayList<Ticket> getTicketsWithProd(BaseProduct product) {
        ArrayList<Ticket> ticketsWithProd = new ArrayList<>();
        // We look which tickets have the product and delete it from them
        for (TicketEntry ticketEntry : this.tickets.values()) {
            if (ticketEntry.getTicket().hasProduct(product))
                ticketsWithProd.add(ticketEntry.getTicket());
        }
        return ticketsWithProd;
    }

    /**
     * Crea una nueva instancia de Ticket y su TicketEntry asociado, y la registra.
     * Genera un ID único si no se proporciona uno, o verifica la unicidad del proporcionado.
     * @param app La instancia de la aplicación.
     * @param ticketId El ID único del nuevo ticket (puede ser null/vacío para generar uno).
     * @param cashId El ID del cajero asociado.
     * @param customerId El ID del cliente asociado.
     * @return 0 si es exitoso, -1 si el ticketId ya existe.
     */
    public int addNewTicket(App app, String ticketId, String cashId, String customerId) {
        String finalTicketId;

        if (ticketId == null || ticketId.isEmpty()) {
            finalTicketId = generateUniqueTicketId();
        } else {
            finalTicketId = ticketId;
            if (this.tickets.containsKey(finalTicketId)) {
                return -1;
            }
        }

        Ticket newTicket = new Ticket(app);
        TicketEntry newEntry = new TicketEntry(cashId, customerId, newTicket);

        this.tickets.put(finalTicketId, newEntry);

        String[] existingTickets = this.userToTicket.getOrDefault(customerId, new String[0]);

        String[] updatedTickets = Arrays.copyOf(existingTickets, existingTickets.length + 1);
        updatedTickets[existingTickets.length] = finalTicketId;

        this.userToTicket.put(customerId, updatedTickets);

        return 0;
    }

    private String generateUniqueTicketId() {
        String baseId;
        String fullId;

        do {
            baseId = LocalDateTime.now().format(ID_DATE_FORMATTER);
            int randomPart = 10000 + random.nextInt(90000);
            fullId = baseId + randomPart;
        } while (this.tickets.containsKey(fullId));

        return fullId;
    }

    /**
     * Obtiene una lista de entradas de tickets ordenadas por ID de Cajero.
     * @return Lista de entradas de tickets.
     */
    public List<TicketEntry> listTicketsSortedByCashierId() {
        List<TicketEntry> allEntries = new ArrayList<>(tickets.values());
        allEntries.sort(Comparator.comparing(TicketEntry::getCashId));
        return allEntries;
    }

    /**
     * Obtiene un Ticket si su ID existe y el ID del cajero asociado coincide.
     * Este método se utiliza para acciones que requieren que el cajero esté
     * autorizado para manipular el ticket (como imprimirlo).
     * @param ticketId El ID único del ticket.
     * @param cashId El ID del cajero que intenta acceder al ticket.
     * @return El objeto Ticket si se encuentra y el cajero coincide, de lo contrario null.
     */
    public Ticket getTicketIfCashierMatches(String ticketId, String cashId) {
        TicketEntry entry = this.tickets.get(ticketId);

        if (entry == null) {
            return null;
        }

        if (entry.getCashId().equals(cashId)) {
            return entry.getTicket();
        } else {
            return null;
        }
    }

    /**
     * Intenta añadir un producto a un ticket dado por su ID.
     * @param ticketId ID del ticket.
     * @param cashId ID del cajero autorizado.
     * @param prodIdStr ID del producto (String).
     * @param amount Cantidad (o número de personas).
     * @param personalizations Lista de strings de personalización.
     * @return 0 si es exitoso. Códigos de error remapeados para la acción.
     */
    public int addProductToTicket(String ticketId, String cashId, String prodIdStr, int amount, ArrayList<String> personalizations) {
        Ticket ticket = getTicketIfCashierMatches(ticketId, cashId);
        if (ticket == null) {
            return -1;
        }

        int prodId;
        try {
            prodId = Integer.parseInt(prodIdStr);
        } catch (NumberFormatException e) {
            return -2;
        }

        BaseProduct product = this.app.catalog.getProduct(prodId);
        if (product == null) {
            return -3;
        }

        int result;
        if (product instanceof Product productInstance) {
            int maxEditable = 0;

            result = ticket.addProduct(productInstance, amount, maxEditable, personalizations);
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
        }
        return 0;
    }

    /**
     * Intenta eliminar un producto de un ticket dado por su ID.
     * Requiere que el ticket exista y que el cajero esté autorizado.
     * @param ticketId El ID del ticket.
     * @param cashId El ID del cajero.
     * @param prodIdStr El ID del producto a eliminar (como String desde el comando).
     * @return 0 si es exitoso.
     * -1 si el ticket no existe o el cajero no coincide.
     * -2 si el ID del producto no es un número válido.
     * -3 si el producto con ese ID no se encuentra en el Catalog.
     * -4 si el ticket está cerrado (delegate return from Ticket.deleteProduct, código -3).
     * -5 si el producto no se encuentra en el ticket (delegate return from Ticket.deleteProduct, código -1).
     */
    public int removeProduct(String ticketId, String cashId, String prodIdStr) {
        Ticket ticket = getTicketIfCashierMatches(ticketId, cashId);
        if (ticket == null) {
            return -1;
        }

        int prodId;
        try {
            prodId = Integer.parseInt(prodIdStr);
        } catch (NumberFormatException e) {
            return -2;
        }

        BaseProduct product = this.app.catalog.getProduct(prodId);
        if (product == null) {
            return -3;
        }

        int result = ticket.deleteProduct(product);

        if (result == -3) {
            return -4;
        } else if (result == -1) {
            return -5;
        }

        return result;
    }
}