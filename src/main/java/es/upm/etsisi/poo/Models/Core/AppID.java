package es.upm.etsisi.poo.Models.Core;

import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;

import java.io.Serializable;
import java.util.Objects;

public abstract class AppID implements Comparable<AppID>, Serializable {

    private static final long serialVersionUID = 1L;

    protected final int baseID;

    public AppID(int id) throws InvalidAppIDException {
        if (id <= 0)
            throw new InvalidAppIDException("can't be 0 or less than 0");

        this.baseID = id;
    }

    public AppID(String id) throws InvalidAppIDException {
        if (!id.matches("\\d+")) {
            throw new InvalidAppIDException("can't have non numeric characters");
        }

        int numID = Integer.parseInt(id);
        if (numID <= 0)
            throw new InvalidAppIDException("can't be 0 or less than 0");

        this.baseID = numID;
    }

    public AppID(AppID other) {
        this.baseID = other.baseID;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AppID other = (AppID) obj;
        return baseID == other.baseID;
    }

    public abstract String toString();

    @Override
    public final int hashCode() {
        // Allows as to generate different hashCodes for different classes with the same baseID
        return Objects.hash(getClass(), baseID);
    }

    public int compareTo(AppID other) {
        if (!getClass().equals(other.getClass()))
            throw new ClassCastException("Cannot compare different AppID types");

        return Integer.compare(baseID, other.baseID);
    }
}
