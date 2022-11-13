package cloud.rsqaured.persistence.repository;

import cloud.rsqaured.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {

    UserRoleEntity findByUserEmail(String userEmail);

}
