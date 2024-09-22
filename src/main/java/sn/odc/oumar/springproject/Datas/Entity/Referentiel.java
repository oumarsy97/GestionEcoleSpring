package sn.odc.oumar.springproject.Datas.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.odc.oumar.springproject.Datas.listeners.CodeGeneratorListener;
import sn.odc.oumar.springproject.Datas.listeners.SoftDeleteListener;
import sn.odc.oumar.springproject.Datas.listeners.impl.CodeGeneratable;
import sn.odc.oumar.springproject.Datas.listeners.impl.SoftDeletable;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "referentiels")
@EntityListeners({SoftDeleteListener.class, CodeGeneratorListener.class}) // Ajoute le listener pour générer le code
public class Referentiel implements SoftDeletable, CodeGeneratable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String libelle;

    @Column(unique = true, nullable = false)
    private String code;  // Le code sera généré automatiquement

    private String description;

    private String photoCouverture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Promo.Etat etat = Promo.Etat.ACTIF;

    @ManyToMany(mappedBy = "referentiels")
    private Set<Promo> promos;

    @OneToMany(mappedBy = "referentiel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Competence> competences;

    private boolean deleted = false;

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
