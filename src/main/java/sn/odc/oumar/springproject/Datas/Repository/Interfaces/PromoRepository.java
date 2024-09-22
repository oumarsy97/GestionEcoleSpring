package sn.odc.oumar.springproject.Datas.Repository.Interfaces;

import sn.odc.oumar.springproject.Datas.Entity.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoRepository extends JpaRepository<Promo,Long> {
    boolean existsByLibelle(String  libelle);
}
