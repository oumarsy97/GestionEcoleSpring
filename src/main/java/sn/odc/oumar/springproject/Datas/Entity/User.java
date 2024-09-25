package sn.odc.oumar.springproject.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;
    private String nom;
    private String prenom;
    private String adresse;
    private String password;
    private String photo;
    private String telephone;
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public User() {
        super();  // Appel du constructeur de la superclasse (BaseEntity)
        this.role = new Role(); // Initialisation du rôle par défaut à null au constructeur
    }



    @ManyToOne(fetch = FetchType.EAGER) // Relation avec Role
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnore
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER, optional = true) // Relation facultative avec Fonction
    @JoinColumn(name = "fonction_id", nullable = true) // Colonne 'fonction_id' acceptant les valeurs nulles
    private Fonction fonction;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false; // Valeur par défaut à false

    @Enumerated(EnumType.STRING)
    private Status status; // Utilisation de l'enum Status

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    public enum Status {
        BLOQUER,
        ACTIF,
        INACTIF
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

