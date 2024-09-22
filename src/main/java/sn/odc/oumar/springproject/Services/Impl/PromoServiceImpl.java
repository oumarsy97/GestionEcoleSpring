package sn.odc.oumar.springproject.Services.Impl;

import sn.odc.oumar.springproject.Datas.Entity.Promo;
import sn.odc.oumar.springproject.Datas.Repository.Interfaces.PromoRepository;
import sn.odc.oumar.springproject.Services.Interfaces.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromoServiceImpl implements PromoService {

    private final PromoRepository promoRepository;

    @Autowired
    public PromoServiceImpl(PromoRepository promoRepository) {
        this.promoRepository = promoRepository; // Correction de l'affectation
        // Ajout du référentiel
    }

    @Override
    public Promo savePromo(Promo promo) {
        return promoRepository.save(promo);
    }

    @Override
    public Optional<Promo> findById(Long id) {
        return promoRepository.findById(id); // Ajout de la recherche par ID
    }

    @Override
    public void deletePromo(Promo promotion) {
        promoRepository.delete(promotion); // Suppression de la promotion
    }

    @Override
    public Promo updatePromo(Promo promotion) {
        return promoRepository.save(promotion); // Mise à jour de la promotion
    }

    @Override
    public Iterable<Promo> findAllPromos() {
        return promoRepository.findByDeletedFalse(); // Récupération de toutes les promotions
    }

    @Override
    public boolean promoExists(String libelle) {
        return promoRepository.existsByLibelle(libelle); // Assurez-vous d'avoir une méthode dans votre repo
    }

    public void softDelete(Long id) {
        Promo promo = promoRepository.findById(id).orElseThrow(() -> new RuntimeException("Promo not found"));
        promo.setDeleted(true); // Marquer comme supprimée
        promoRepository.save(promo); // Sauvegarder les modifications
    }
}
