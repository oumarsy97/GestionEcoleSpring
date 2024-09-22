package sn.odc.oumar.springproject.Services.Interfaces;

import sn.odc.oumar.springproject.Datas.Entity.Module;

import java.util.Optional;

public interface ModuleService {
    public Module saveModule(Module module);
    public void deleteModule(Module module);
    public Module updateModule(Module module);
    public Iterable<Module> findAllModules();
    public Optional<Module> findById(Long id);
    public boolean moduleExists(String libelle);
}
