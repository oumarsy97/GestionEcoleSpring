package sn.odc.oumar.springproject.Web.Controller.Impl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sn.odc.oumar.springproject.Datas.Entity.Competence;
import sn.odc.oumar.springproject.Datas.Entity.Module;
import sn.odc.oumar.springproject.Exceptions.ServiceException;
import sn.odc.oumar.springproject.Services.Interfaces.CompetenceService;
import sn.odc.oumar.springproject.Services.Interfaces.ModuleService;
import sn.odc.oumar.springproject.Web.Dtos.Request.ModuleRequestDTO;
import sn.odc.oumar.springproject.Web.Dtos.Response.ApiResponse;


@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {
    private final ModuleService moduleService;
    private final CompetenceService competenceService;

    @Autowired
    public ModuleController(ModuleService moduleService, CompetenceService competenceService) {
        this.moduleService = moduleService;
        this.competenceService = competenceService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Module>> createModule(@RequestBody @Valid ModuleRequestDTO moduleDTO) {

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
        Module savedModule = moduleService.saveModule(module);

        // Retourner une réponse avec succès
        return ResponseEntity.ok(ApiResponse.success("Module créé avec succès", savedModule));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Module>>  getModuleById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Module retrieved successfully", moduleService.findById(id).orElse(null))
        );
    }
    @DeleteMapping("/{id}")
    public void deleteModule(@PathVariable Long id) {
        moduleService.findById(id).ifPresent(moduleService::deleteModule);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Module>>  updateModule(@PathVariable Long id, @RequestBody Module module) {
        module.setId(id);
         moduleService.saveModule(module);
        return ResponseEntity.ok(
                ApiResponse.success("Module updated successfully", moduleService.updateModule(module))
        );

    }
    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<Module>>>getAllModules() {

        Iterable<Module> modules = moduleService.findAllModules();
        return ResponseEntity.ok(
                new ApiResponse<>("success", HttpStatus.CREATED, "List of all promotions", modules)
        );
    }

}
