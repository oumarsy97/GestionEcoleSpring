package sn.odc.oumar.springproject.Services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import sn.odc.oumar.springproject.Datas.Entity.Apprenant;
import sn.odc.oumar.springproject.Datas.Repository.Interfaces.ApprenantRepository;
import sn.odc.oumar.springproject.Datas.Repository.Interfaces.BaseInterface;
import sn.odc.oumar.springproject.Services.Interfaces.ApprenantService;


@org.springframework.stereotype.Service
public class ApprenantServiceImpl extends BaseServiceImpl<Apprenant,Long> implements ApprenantService {
    protected ApprenantRepository apprenantRepository;
    @Autowired
    public ApprenantServiceImpl(ApprenantRepository apprenantRepository) {
        super(apprenantRepository);
        this.apprenantRepository = apprenantRepository;
    }
}
