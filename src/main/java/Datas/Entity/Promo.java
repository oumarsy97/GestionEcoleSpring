package Datas.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;



@Entity
@Table(name = "promotions")
@Data
public class Promotion {

    public enum Etat {
        ACTIF,
        CLOTURE,
        INACTIF
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String libelle;


    private LocalDate dateDebut;

    
    private LocalDate dateFin;

    private int dureeReel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Etat etat;

}
