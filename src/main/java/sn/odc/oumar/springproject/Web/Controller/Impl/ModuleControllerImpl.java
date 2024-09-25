package sn.odc.oumar.springproject.Web.Controller.Impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.oumar.springproject.Datas.Entity.Competence;
import sn.odc.oumar.springproject.Datas.Entity.Module;
import sn.odc.oumar.springproject.Exceptions.ServiceException;
import sn.odc.oumar.springproject.Services.Interfaces.CompetenceService;
import sn.odc.oumar.springproject.Services.Interfaces.ModuleService;
import sn.odc.oumar.springproject.Web.Controller.BaseControllerImpl;
import sn.odc.oumar.springproject.Web.Controller.Interfaces.ModuleControllerInterface;
import sn.odc.oumar.springproject.Web.Dtos.Request.ModuleDTO;
import sn.odc.oumar.springproject.Web.Dtos.Request.ModuleRequestDTO;
import sn.odc.oumar.springproject.Web.Dtos.Response.ApiResponse;


@RestController
@RequestMapping("/api/v1/modules")
@Tag(name = "Modules", description = "API pour gérer les modules")
public class ModuleControllerImpl extends BaseControllerImpl<Module, ModuleDTO,Long> implements ModuleControllerInterface {
    private final ModuleService moduleService;
    private final CompetenceService competenceService;

    @Autowired
    public ModuleControllerImpl(ModuleService moduleService, CompetenceService competenceService) {
        super(moduleService); // Utiliser le constructeur parent avec le service de module
        this.moduleService = moduleService;
        this.competenceService = competenceService;
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Module>> create(@RequestBody @Valid ModuleRequestDTO moduleDTO) {

        // Vérification si le module existe déjà
        if (moduleService.moduleExists(moduleDTO.getNom())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Le module existe déjà", null));
        }

        // Vérification si la compétence associée existe
        Competence competence = competenceService.findById(moduleDTO.getCompetenceId())
                .orElseThrow(() -> new ServiceException("Compétence non trouvée"));

        // Création du module
        Module module = new Module();
        module.setNom(moduleDTO.getNom());
        module.setDescription(moduleDTO.getDescription());
        module.setDureeAcquisition(moduleDTO.getDureeAcquisition());
        module.setCompetence(competence); // Associer la compétence trouvée

        // Sauvegarder le module
        Module savedModule = moduleService.create(module);

        // Retourner une réponse avec succès
        return ResponseEntity.ok(ApiResponse.success("Module créé avec succès", savedModule));
    }




    @Override
    protected Module convertToEntity(ModuleDTO moduleDTO) {
        return null;
    }

    @Override
    protected ModuleDTO convertToDto(Module entity) {
        return null;
    }
}
