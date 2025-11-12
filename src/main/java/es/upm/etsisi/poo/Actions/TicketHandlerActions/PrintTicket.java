package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.TicketContainer.Ticket;

public class PrintTicket extends Action {
    public PrintTicket(App app) {
        super(app);
    }

    @Override
    public int execute(String[] args) {
        // 1. Control de argumentos
        if (args.length != 2) {
            System.err.println("ERROR: Se requieren dos argumentos: <ticketId> <cashId>.");
            return -1;
        }

        String ticketId = args[0];
        String cashId = args[1];

        // 2. Delegación de la validación y obtención del Ticket
        Ticket ticket = app.tickets.getTicketIfCashierMatches(ticketId, cashId);

        // 3. Control de resultados
        if (ticket == null) {
            System.err.printf("ERROR: No se encontró el ticket '%s' o el cajero '%s' no está autorizado para imprimirlo.\n", ticketId, cashId);
            return -2;
        }

        // 4. Delegación de la impresión
        System.out.println("--- IMPRESIÓN DE TICKET " + ticketId + " (Cajero: " + cashId + ") ---");
        ticket.printTicket();
        System.out.println("----------------------------------------------");

        return 0;
    }

    @Override
    public String help() {
        return "ticket print <ticketId> <cashId>: Muestra por consola el contenido de un ticket específico, verificando la autorización del cajero.";
    }
}