package sn.odc.oumar.springproject.Datas.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "promos")
@Data
public class Promo {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String libelle;


    private LocalDate dateDebut;


    private LocalDate dateFin;

    private int dureeReel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Etat etat = Etat.ACTIF;

    @ManyToMany // Relation ManyToMany avec Referentiel
    @JoinTable(
            name = "promo_referentiels", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "promo_id"), // Colonne pour Promotion
            inverseJoinColumns = @JoinColumn(name = "referentiel_id") // Colonne pour Referentiel
    )
    private Set<Referentiel> referentiels;

    public enum Etat {
        ACTIF,
        CLOTURE,
        INACTIF
    }

}
