package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.App;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TicketBook {
    private HashMap<String, TicketEntry> tickets;
    private HashMap<String, String[]> userToTicket;

    public TicketBook(App app) {
        this.tickets = new HashMap<>();
        this.userToTicket = new HashMap<>();
    }

    public Ticket getTicket(String id) {
        TicketEntry entry = this.tickets.get(id);
        return (entry != null) ? entry.getTicket() : null;
    }

    /**
     * Busca un TicketEntry por su ID y verifica si el cashId coincide.
     * @param ticketId El ID del ticket a buscar.
     * @param cashId El ID del cajero para la validación.
     * @return El Ticket si existe y el cashId coincide, o null en caso contrario.
     */
    public Ticket getTicketIfCashierMatches(String ticketId, String cashId) {
        TicketEntry entry = this.tickets.get(ticketId);

        if (entry == null) {
            return null; // Ticket no encontrado
        }

        // Comprobar si el cashId coincide
        if (!entry.getCashId().equals(cashId)) {
            return null; // CashId no coincide
        }

        return entry.getTicket();
    }

    /**
     * Crea una nueva instancia de Ticket y su TicketEntry asociado, y la registra.
     * @param app La instancia de la aplicación.
     * @param ticketId El ID único del nuevo ticket.
     * @param cashId El ID del cajero asociado.
     * @param customerId El ID del cliente asociado.
     * @return 0 si es exitoso, -1 si el ticketId ya existe.
     */
    public int add(App app, String ticketId, String cashId, String customerId) {
        if (this.tickets.containsKey(ticketId)) {
            return -1; // Ticket duplicado
        }

        Ticket newTicket = new Ticket(app, ticketId);
        TicketEntry newEntry = new TicketEntry(cashId, customerId, newTicket);

        this.tickets.put(ticketId, newEntry);
        return 0;
    }

    /**
     * Devuelve una lista de todos los TicketEntry ordenados por el ID del cajero.
     * @return Una lista de TicketEntry, ordenada alfabéticamente por cashId.
     */
    public List<TicketEntry> listTicketsSortedByCashierId() {
        List<TicketEntry> allEntries = new ArrayList<>(tickets.values());

        allEntries.sort(Comparator.comparing(TicketEntry::getCashId));

        return allEntries;
    }
}