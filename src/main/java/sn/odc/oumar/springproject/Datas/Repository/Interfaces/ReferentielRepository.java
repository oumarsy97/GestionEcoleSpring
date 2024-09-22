package sn.odc.oumar.springproject.Datas.Repository.Interfaces;

import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferentielRepository extends JpaRepository<Referentiel, Long> {
    boolean existsByLibelle(String libelle);

}
