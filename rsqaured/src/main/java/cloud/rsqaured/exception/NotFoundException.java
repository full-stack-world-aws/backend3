package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundException extends RuntimeException {
    public static final String MESSAGE = " '%s' with id '%s' was not found!";

    private final int id;
    private final String name;

    public NotFoundException(String name ,int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE,name, id);
    }
}
