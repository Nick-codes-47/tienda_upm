package es.upm.etsisi.poo.AppExceptions;

public class EntityAlreadyExistsException extends AppException {
    public EntityAlreadyExistsException(String type, String id) {
        super(type + " with id " + id + " already exists");
    }

    public EntityAlreadyExistsException(String type, String id, String msg) {
        super(type + " with id " + id + " already exists: " + msg);
    }
}
