package tia_sarwoedhi.belajar_spring_restful_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    private String id;

    private String street;

    private String city;

    private String country;

    @Column(name = "postal_code")
    private String postCode;

    @ManyToOne
    @JoinColumn(name = "contact_id",referencedColumnName = "id")
    private Contact contact;
}
