package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GeneralMessageException extends RuntimeException {
//    public static final String MESSAGE;

    private final String message;

    public GeneralMessageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

