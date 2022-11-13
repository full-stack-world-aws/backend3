package cloud.rsqaured.component;

import cloud.rsqaured.exception.AccessDeniedException;
import cloud.rsqaured.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


import java.util.Objects;

@Component
public class PermissionsValidator {

    private AuthenticatedUserResolver userResolver;
    @Autowired
    public PermissionsValidator(
            AuthenticatedUserResolver userResolver
    ) {
        this.userResolver = userResolver;

    }

}
