package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {
    public static final String MESSAGE = "Access Denied";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
