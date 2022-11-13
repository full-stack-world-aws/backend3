package cloud.rsqaured.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends org.springframework.security.oauth2.common.exceptions.InvalidTokenException {

    public InvalidTokenException(String msg){
        super(msg);
    }
}
