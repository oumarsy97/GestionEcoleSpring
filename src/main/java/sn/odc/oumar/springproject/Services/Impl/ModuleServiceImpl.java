package sn.odc.oumar.springproject.Services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.odc.oumar.springproject.Datas.Repository.Interfaces.ModuleRepository;
import sn.odc.oumar.springproject.Services.Interfaces.ModuleService;
import sn.odc.oumar.springproject.Datas.Entity.Module;


import java.util.Optional;

@Service
public class ModuleServiceImpl implements ModuleService {

    private static ModuleRepository moduleRepository;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        ModuleServiceImpl.moduleRepository = moduleRepository;
    }


    @Override
    public Module saveModule(Module module) {
        return  moduleRepository.save(module);
    }

    @Override
    public void deleteModule(Module module) {
     moduleRepository.delete(module);
    }

    @Override
    public Module updateModule(Module module) {
        return  moduleRepository.save(module);
    }

    @Override
    public Iterable<Module> findAllModules() {
        return  moduleRepository.findAll();
    }

    @Override
    public Optional<Module> findById(Long id) {
        return  moduleRepository.findById(id);
    }

    @Override
    public boolean moduleExists(String libelle) {
        return  moduleRepository.existsByNom(libelle);
    }
}
