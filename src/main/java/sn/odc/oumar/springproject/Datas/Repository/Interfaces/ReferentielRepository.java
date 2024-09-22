package sn.odc.oumar.springproject.Datas.Repository.Interfaces;

import sn.odc.oumar.springproject.Datas.Entity.Competence;
import sn.odc.oumar.springproject.Datas.Entity.Promo;
import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferentielRepository extends JpaRepository<Referentiel, Long> {
    boolean existsByLibelle(String libelle);
    List<Referentiel> findByDeletedFalse();
    List<Referentiel> findByCompetencesContaining(Competence competence);

}
