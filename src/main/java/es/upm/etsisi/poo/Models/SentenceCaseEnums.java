package es.upm.etsisi.poo.Models;

public interface SentenceCaseEnums {
    default String toSentenceCase() {
        String name = ((Enum<?>) this).name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
