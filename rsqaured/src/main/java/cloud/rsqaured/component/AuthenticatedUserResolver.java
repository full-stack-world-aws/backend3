package cloud.rsqaured.component;

import cloud.rsqaured.persistence.entity.UserEntity;

public interface AuthenticatedUserResolver {
    UserEntity user();

}
