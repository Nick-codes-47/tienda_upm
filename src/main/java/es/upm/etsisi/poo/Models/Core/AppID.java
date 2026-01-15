package es.upm.etsisi.poo.Models.Core;

import java.util.Objects;

public abstract class AppID implements Comparable<AppID> {

    public AppID(int ID) throws InvalidAppIDException {
        if (ID <= 0)
            throw new InvalidAppIDException("0 or less than 0");

        this.baseID = ID;
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

    public static class InvalidAppIDException extends AppException {
        public InvalidAppIDException(String message) {
            super("App ID can not be " + message);
        }
    }

    protected final int baseID;
}
