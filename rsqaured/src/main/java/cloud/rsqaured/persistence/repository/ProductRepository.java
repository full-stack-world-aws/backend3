package cloud.rsqaured.persistence.repository;

import cloud.rsqaured.persistence.entity.ProductEntity;
import cloud.rsqaured.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findByUserEntity(UserEntity userEntity);
}

