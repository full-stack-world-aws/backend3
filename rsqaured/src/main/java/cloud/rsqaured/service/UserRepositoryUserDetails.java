package cloud.rsqaured.service;

import cloud.rsqaured.persistence.entity.UserEntity;
import cloud.rsqaured.persistence.entity.UserRoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.util.Optional.ofNullable;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class UserRepositoryUserDetails implements UserDetails {

    private final UserEntity userEntity;

    private final UserRoleEntity userRoleEntity;

    UserRepositoryUserDetails(UserEntity userEntity, UserRoleEntity userRoleEntity) {
        this.userEntity = userEntity;
        this.userRoleEntity = userRoleEntity;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return createAuthorityList(
                ofNullable(userRoleEntity)
                        .map(UserRoleEntity::getRole)
                        .orElse(""));
    }

    @Override
    public String getPassword() {
        return userEntity.getBCryptEncodedPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

}
