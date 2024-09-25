package sn.odc.oumar.springproject.Web.Controller.Impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import sn.odc.oumar.springproject.Datas.Entity.Role;
import sn.odc.oumar.springproject.Services.Interfaces.RoleService;
import sn.odc.oumar.springproject.Web.Controller.BaseControllerImpl;
import sn.odc.oumar.springproject.Web.Controller.Interfaces.RoleControllerInterface;
import sn.odc.oumar.springproject.Web.Dtos.Request.RoleDTO;
import sn.odc.oumar.springproject.Web.Dtos.Response.BaseResponse;

@RestController
@RequestMapping("/api/v1/roles")

@Tag(name = "roles", description = "API pour gérer les roles")
public class RoleController extends BaseControllerImpl<Role,RoleDTO,Long> implements RoleControllerInterface {

   protected RoleService roleService;

   @Autowired
    public RoleController(RoleService roleService) {
       super(roleService);
        this.roleService = roleService;
    }

    @Override
    @GetMapping("/libelle")
    protected Role convertToEntity(RoleDTO roleDTO) {
         Role role = new Role();
         role.setId(roleDTO.getId());
        return role;
    }

    @Override
    protected RoleDTO convertToDto(Role entity) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setLibelle(entity.getLibelle());
        return roleDTO;
    }

    @Override
    public ResponseEntity<BaseResponse<Role>> findByLibelle(String libelle) {
        Role role = roleService.findByLibelle(libelle);
        return   responseService.successResponse(role,"success");
    }


}
