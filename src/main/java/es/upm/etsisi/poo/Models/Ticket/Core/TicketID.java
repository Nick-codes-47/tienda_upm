package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;
import es.upm.etsisi.poo.Models.Core.AppID;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketID extends AppID implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean closed = false;
    private final LocalDateTime creationDate;
    private LocalDateTime closingDate;

    public TicketID(int id) throws InvalidAppIDException {
        super(id);

        if (id > 99999) throw new InvalidAppIDException("Ticket ID has to be 5digits.");

        this.creationDate = LocalDateTime.now();
    }

    public TicketID(String id) throws InvalidAppIDException {
        super(id);

        if (id.length() != 5) throw new InvalidAppIDException("Ticket ID has to be 5 digits.");

        this.creationDate = LocalDateTime.now();
    }

    public void close() {
        closed = true;
        closingDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (!closed)
            return creationDate.format(DATE_TIME_FORMATTER)
                    + "-"
                    + String.format("%05d", super.baseID);
        else
            return String.format("%05d", super.baseID)
                    + "-"
                    + this.closingDate.format(DATE_TIME_FORMATTER);

    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
}