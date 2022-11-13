package cloud.rsqaured.service;

import cloud.rsqaured.component.AuthenticatedUserResolver;
import cloud.rsqaured.exception.EmailAlreadyExistsException;
import cloud.rsqaured.model.User;
import cloud.rsqaured.persistence.entity.UserEntity;
import cloud.rsqaured.persistence.entity.UserRoleEntity;
import cloud.rsqaured.persistence.repository.UserRepository;
import cloud.rsqaured.persistence.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.UserRoles;

import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            AuthenticatedUserResolver authenticatedUserResolver,
            PasswordEncoder passwordEncoder,
            UserRoleRepository userRoleRepository
            ){
        this.authenticatedUserResolver = authenticatedUserResolver;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User get() {
        UserEntity userEntity = authenticatedUserResolver.user();
        userEntity =  userRepository.findById(userEntity.getId()).orElse(null);
        return User.fromEntity(userEntity);
    }


    @Override
    @Transactional
    public User createUser(User user) {
        String email = user.getEmail().trim();
        if (ofNullable(userRepository.findByEmail((email))).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setBCryptEncodedPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setName(user.getName());
            userEntity.setEnabled(true);
            UserEntity userEntity1 =userRepository.save(userEntity);

            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserEmail(email);
            userRoleEntity.setRole(UserRoles.USER_ROLE);
            userRoleRepository.save(userRoleEntity);

        return User.fromEntity(userEntity1);

    }



}
