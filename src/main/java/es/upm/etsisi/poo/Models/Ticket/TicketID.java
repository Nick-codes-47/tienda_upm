package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Core.AppID;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketID extends AppID implements Serializable {

    private static final long serialVersionUID = 1L;

    public TicketID(int ID) throws InvalidAppIDException {
        super(ID);

        this.creationDate = LocalDateTime.now();
    }

    public void close() {
        closed = true;
        closingDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (closed)
            return creationDate.format(DATE_TIME_FORMATTER)
                    + "-"
                    + super.baseID;
        else
            return super.baseID
                    + "-"
                    + this.closingDate.format(DATE_TIME_FORMATTER);

    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");

    private boolean closed = false;
    private final LocalDateTime creationDate;
    private LocalDateTime closingDate;
}