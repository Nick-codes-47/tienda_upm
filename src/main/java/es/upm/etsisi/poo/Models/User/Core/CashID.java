package es.upm.etsisi.poo.Models.User.Core;

import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;

import java.io.Serializable;

public class CashID implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;

    public CashID(String id) throws InvalidAppIDException {
        if (id.length() == 9 && id.charAt(0) == 'U' && id.charAt(1) == 'W')
            throw new InvalidAppIDException("cashier id must be UWXXXXXXX");

        this.id = id;
    }

    public CashID(CashID other) {
        this.id = other.id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashID cashID = (CashID) o;
        return id.equals(cashID.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
