package cloud.rsqaured.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;

@Data
@NoArgsConstructor
@Entity
@Table(name = "oauth_code")
public class OAuthCode {

    @Id
    private String code;

    @Lob
    @Column(columnDefinition = "mediumblob")
    private Blob authentication;

}
