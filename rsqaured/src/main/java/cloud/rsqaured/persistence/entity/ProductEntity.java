package cloud.rsqaured.persistence.entity;

import cloud.rsqaured.enums.CountryEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy=   GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "info")
    private String info;

    @NonNull
    @Column(name = "reference_number")
    private Integer referenceNumber;

    @NonNull
    @Column(name = "country", columnDefinition = "ENUM('USA', 'CANADA', 'MEXICO', 'CHINA')")
    @Enumerated(EnumType.STRING)
    private CountryEnum country;

    @Column(name = "image_location")
    private String imageLocation;

    @Column(name = "file_location")
    private String fileLocation;


    @JoinColumn(name="user_id")
    @ManyToOne(optional = true)
    private UserEntity userEntity;



    @Column(name = "created_timestamp")
    @CreationTimestamp
    private Timestamp createdTimestamp;

    @Column(name = "updated_timestamp")
    @UpdateTimestamp
    private Timestamp updatedTimestamp;




}
