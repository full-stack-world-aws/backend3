package cloud.rsqaured.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;

@Data
@NoArgsConstructor
@Entity
@Table(name = "oauth_refresh_token")
public class OAuthRefreshToken {

    @Id
    @Column(name = "token_id")
    private String tokenId;
    @Lob
    @Column(columnDefinition = "mediumblob")
    private Blob token;
    @Lob
    @Column(columnDefinition = "mediumblob")
    private Blob authentication;

}
