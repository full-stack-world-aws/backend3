package cloud.rsqaured.component;

import cloud.rsqaured.persistence.entity.UserEntity;
import cloud.rsqaured.service.UserRepositoryUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class UserRepositoryUserDetailsAuthenticatedUserResolver implements AuthenticatedUserResolver {

    @Override
    public final UserEntity user() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication;
        if (nonNull((authentication = securityContext.getAuthentication()))
                && authentication.isAuthenticated()
                && (authentication.getPrincipal() instanceof UserRepositoryUserDetails)) {
                return ((UserRepositoryUserDetails) authentication.getPrincipal()).getUserEntity();
        } else {
            throw new InvalidTokenException("Access token is either missing or invalid.");
        }
    }

}
