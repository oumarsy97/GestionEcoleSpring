package sn.odc.oumar.springproject.Datas.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modules")
@Data
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String description;

    @Column(nullable = false )
    private Integer dureeAcquisition;

    @ManyToOne
    @JoinColumn(name = "competence_id") // Nom de la colonne de clé étrangère
    private Competence competence; // Référence vers la compétence
}
