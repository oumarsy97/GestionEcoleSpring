package sn.odc.oumar.springproject.Datas.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name = "referentiels")
public class Referentiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String libelle;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;

    private String photoCouverture;

    @ManyToMany(mappedBy = "referentiels") // Relation ManyToMany avec Promotion
    private Set<Promo> promos;

    @OneToMany(mappedBy = "referentiel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Competence> competences;
}
