package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends RuntimeException {
    public static final String MESSAGE = "Email '%s' is already used.";
    private final String email;

    public EmailAlreadyExistsException(String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE, email);
    }
}
