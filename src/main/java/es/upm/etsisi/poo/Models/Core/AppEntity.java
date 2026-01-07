package es.upm.etsisi.poo.Models.Core;

public abstract class AppEntity {

    public abstract AppID getID();

    public abstract AppEntity clone();

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof AppEntity entt)) return false;
        return getID().equals(entt.getID());
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    };
}