package sn.odc.oumar.springproject.Web.Controller.Impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.odc.oumar.springproject.Datas.Entity.Apprenant;
import sn.odc.oumar.springproject.Services.Interfaces.ApprenantService;
import sn.odc.oumar.springproject.Web.Controller.BaseControllerImpl;
import sn.odc.oumar.springproject.Web.Controller.Interfaces.ApprenantControllerInterface;
import sn.odc.oumar.springproject.Web.Dtos.Request.ApprenantDTO;

@RequestMapping("/appreants")
@RestController
@Tag(name = "apprenants", description = "API pour gérer les appreants")
public class ApprenantControllerImpl extends BaseControllerImpl<Apprenant, ApprenantDTO,Long> implements ApprenantControllerInterface {
    protected ApprenantService service;
    @Autowired
    public ApprenantControllerImpl(ApprenantService service) {
        super( service);
        this.service = service;
    }

    @Override
    protected Apprenant convertToEntity(ApprenantDTO apprenantDTO) {
        return null;
    }

    @Override
    protected ApprenantDTO convertToDto(Apprenant entity) {
        return null;
    }

}
