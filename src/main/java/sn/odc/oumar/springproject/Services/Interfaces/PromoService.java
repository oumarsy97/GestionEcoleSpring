package sn.odc.oumar.springproject.Services.Interfaces;

import sn.odc.oumar.springproject.Datas.Entity.Promo;
import sn.odc.oumar.springproject.Web.Dtos.Request.PromotionRequestDTO;

import java.util.Optional;


public interface PromoService {
    public Promo savePromo(Promo promotion);
    public Optional<Promo> findById(Long id);
    public void deletePromo(Promo promotion);
    public Promo updatePromo(Promo promotion);
    public Iterable<Promo> findAllPromos();
    public boolean promoExists(String libelle);
}
