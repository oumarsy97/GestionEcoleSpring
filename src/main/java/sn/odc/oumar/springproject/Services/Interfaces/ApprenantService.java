package sn.odc.oumar.springproject.Services.Interfaces;

import sn.odc.oumar.springproject.Datas.Entity.Apprenant;
import sn.odc.oumar.springproject.Web.Dtos.Request.ApprenantDTO;

public interface ApprenantService extends BaseService<Apprenant, Long> {
    public Apprenant createApprenant(ApprenantDTO apprenantDTO);
}
