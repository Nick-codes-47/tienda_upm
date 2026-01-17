package es.upm.etsisi.poo.Models.User.Core;

import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;

import java.io.Serializable;
import java.util.regex.Pattern;

public class UserNIF implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String value;

    // Patrón básico: 8 números y 1 letra (DNI) O 1 letra, 7 números y 1 carácter de control (CIF)
    private static final Pattern FORMATO_NIF = Pattern.compile("^[A-Z0-9]{9}$");

    private static final String COMPANY_LETTERS = "ABCDEFGHJNPQRSUVW";

    public UserNIF(String value) throws InvalidAppIDException {
        value = value.toUpperCase();

        if (!FORMATO_NIF.matcher(value).matches()) {
            throw new InvalidAppIDException("Formato de NIF incorrecto: " + value);
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Determina si el NIF corresponde a una Empresa.
     * @return true si es empresa, false si es particular.
     */
    public boolean isCompany() {
        char primeraLetra = this.value.charAt(0);
        // Si la primera letra está en la lista de letras de empresas, es true
        return COMPANY_LETTERS.indexOf(primeraLetra) != -1;
    }

    /**
     * Determina si el NIF corresponde a una Persona (DNI).
     * @return true si es particular, false si es empresa.
     */
    public boolean isIndividual() {
        return !isCompany();
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNIF nif = (UserNIF) o;
        return value.equals(nif.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
