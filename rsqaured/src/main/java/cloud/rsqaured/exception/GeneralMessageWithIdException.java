package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GeneralMessageWithIdException extends RuntimeException {

    private final String message;
    private final int id;

    public GeneralMessageWithIdException(String message, int id) {
        this.message = message;
        this.id = id;

    }

    @Override
    public String getMessage() {
        return String.format(message, id);
    }

}

