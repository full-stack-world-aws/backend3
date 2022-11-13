package cloud.rsqaured.service;

import cloud.rsqaured.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    User get();

    @Transactional
    User createUser(User user);
}
