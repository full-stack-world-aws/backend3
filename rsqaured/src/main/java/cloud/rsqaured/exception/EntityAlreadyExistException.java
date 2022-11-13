package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyExistException extends RuntimeException {
    public static final String MESSAGE = "'%s' already has transaction, transaction id is '%s'";

    private final Class objectClass;
    private final Integer transactionId;

    public EntityAlreadyExistException(Class objectClass, Integer transactionId) {
        this.objectClass = objectClass;
        this.transactionId = transactionId;
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE, objectClass.getSimpleName(), transactionId);
    }

}
