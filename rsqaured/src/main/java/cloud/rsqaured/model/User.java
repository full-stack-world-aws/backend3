package cloud.rsqaured.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import cloud.rsqaured.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private Timestamp createdTimestamp;

    public static User fromEntity(UserEntity userEntity){
        return User.builder()
                .id(userEntity.getId())
                .build();
    }
}