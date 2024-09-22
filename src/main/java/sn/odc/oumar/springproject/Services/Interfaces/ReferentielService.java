package sn.odc.oumar.springproject.Services.Interfaces;

import sn.odc.oumar.springproject.Datas.Entity.Referentiel;

import java.util.Optional;

public interface ReferentielService {
    Referentiel save(Referentiel referentiel);
    Iterable<Referentiel> findAll();
    Optional<Referentiel> findById(Long id);
    Referentiel update(Referentiel referentiel);
    void delete(Long id);
}
