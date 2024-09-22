package sn.odc.oumar.springproject.Services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import sn.odc.oumar.springproject.Datas.Repository.Interfaces.ReferentielRepository;
import sn.odc.oumar.springproject.Services.Interfaces.ReferentielService;

import java.util.Optional;

@Service
public class ReferentielServiceImpl implements ReferentielService {
    private static ReferentielRepository referentielRepository;

    @Autowired
    public ReferentielServiceImpl(ReferentielRepository referentielRepository) {
        ReferentielServiceImpl.referentielRepository = referentielRepository;
    }
    @Override
    public Referentiel save(Referentiel referentiel) {
        return  referentielRepository.save(referentiel);
    }

    @Override
    public Iterable<Referentiel> findAll() {
        return referentielRepository.findAll();
    }

    @Override
    public Optional<Referentiel> findById(Long id) {
        return  referentielRepository.findById(id);
    }

    @Override
    public Referentiel update(Referentiel referentiel) {
          return referentielRepository.save(referentiel);
    }

    @Override
    public void delete(Long id) {
        referentielRepository.deleteById(id);
    }
}
