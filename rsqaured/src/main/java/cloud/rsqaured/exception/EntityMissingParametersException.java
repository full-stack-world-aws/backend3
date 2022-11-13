package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityMissingParametersException extends RuntimeException {
    public static final String MESSAGE = "'%s' is missing required parameter '%s'";

    private final Class objectClass;
    private final String missingParam;

    public EntityMissingParametersException(Class objectClass, String missingParam) {
        this.objectClass = objectClass;
        this.missingParam = missingParam;
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE, objectClass.getSimpleName(), missingParam);
    }

}
