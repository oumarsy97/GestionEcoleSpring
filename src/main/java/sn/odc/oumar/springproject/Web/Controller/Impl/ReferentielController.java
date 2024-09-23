package sn.odc.oumar.springproject.Web.Controller.Impl;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import sn.odc.oumar.springproject.Datas.Entity.Competence;
import sn.odc.oumar.springproject.Datas.Entity.Module;
import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import sn.odc.oumar.springproject.Services.Interfaces.CompetenceService;
import sn.odc.oumar.springproject.Services.Interfaces.ModuleService;
import sn.odc.oumar.springproject.Services.Interfaces.ReferentielService;
import sn.odc.oumar.springproject.Web.Dtos.Request.*;
import sn.odc.oumar.springproject.Web.Dtos.Response.ApiResponse;
import sn.odc.oumar.springproject.Web.Dtos.Response.ExcelExporter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/referentiels")
public class ReferentielController {

    private final ReferentielService referentielService;
    private final CompetenceService competenceService;
    private final ModuleService moduleService;

    @Autowired
    public ReferentielController(ReferentielService referentielService, CompetenceService competenceService, ModuleService moduleService) {
        this.referentielService = referentielService;
        this.competenceService = competenceService;
        this.moduleService = moduleService;

    }

    @PostMapping
    public ResponseEntity<ApiResponse<Referentiel>> createReferentiel(@Valid @RequestBody ReferentielDTO referentielRequestDTO) {
        try {
            if (referentielService.existsByLibelle(referentielRequestDTO.getLibelle())) {
                return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Le libellé existe déjà", null));
            }

            // Mapper le DTO à l'entité Referentiel
            Referentiel referentiel = new Referentiel();
            referentiel.setLibelle(referentielRequestDTO.getLibelle());
            referentiel.setDescription(referentielRequestDTO.getDescription());
            referentiel.setPhotoCouverture(referentielRequestDTO.getPhotoCouverture());

            // Sauvegarder le Référentiel
            Referentiel savedReferentiel = referentielService.save(referentiel);

            // Mapper les compétences et les modules du DTO
            if (referentielRequestDTO.getCompetences() != null) {
                Set<Competence> competences = referentielRequestDTO.getCompetences().stream()
                        .map(compDTO -> {
                            Competence competence = new Competence();
                            competence.setNom(compDTO.getNom());
                            competence.setDescription(compDTO.getDescription());
                            competence.setDureeAcquisition(compDTO.getDureeAcquisition());
                            competence.setType(Competence.Type.valueOf(compDTO.getType()));
                            competence.setReferentiel(savedReferentiel);

                            // Mapper les Modules
                            Set<Module> modules = compDTO.getModules().stream()
                                    .map(modDTO -> {
                                        Module module = new Module();
                                        module.setNom(modDTO.getNom());
                                        module.setDescription(modDTO.getDescription());
                                        module.setDureeAcquisition(modDTO.getDureeAcquisition());
                                        module.setCompetence(competence);
                                        return module;
                                    }).collect(Collectors.toSet());

                            competence.setModules(modules);
                            return competence;
                        }).collect(Collectors.toSet());

                savedReferentiel.setCompetences(competences);
            }

            // Retourner l'entité référentiel sauvegardée
            return ResponseEntity.ok(new ApiResponse<>("succès", HttpStatus.CREATED, "Référentiel créé", savedReferentiel));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>("error", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/competences")
    public ResponseEntity<Set<Competence>> getCompetencesByReferentielId(@PathVariable Long id) {
        Optional<Referentiel> ref = referentielService.findById(id);
        return ref.map(referentiel -> ResponseEntity.ok(referentiel.getCompetences()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<Referentiel>> getAllReferentiels(@RequestParam(required = false) Referentiel.Etat etat) {
        Iterable<Referentiel> referentiels;
        if (etat != null) {
            referentiels = referentielService.findByEtat(Referentiel.Etat.valueOf(String.valueOf(etat))); // Utilisez l'enum directement
        } else {
            referentiels = referentielService.findAll();
        }
        ExcelExporter<Referentiel> exporter = new ExcelExporter<Referentiel>();
        String filePath = "promos.xlsx"; // Chemin du fichier à créer

        exporter.exportToExcel((List<Referentiel>) referentiels, filePath);

        System.out.println("Exportation terminée : " + filePath);
        return ResponseEntity.ok(referentiels);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Referentiel> getReferentielById(@PathVariable Long id) {
        Optional<Referentiel> referentiel = referentielService.findById(id);
        return referentiel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Referentiel> updateReferentiel(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReferentielDto updateReferentielDto) {

        Optional<Referentiel> optionalReferentiel = referentielService.findById(id);
        if (optionalReferentiel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Referentiel referentiel = optionalReferentiel.get();

        // Mettre à jour les champs du référentiel
        if (updateReferentielDto.getLibelle() != null) {
            referentiel.setLibelle(updateReferentielDto.getLibelle());
        }
        if (updateReferentielDto.getDescription() != null) {
            referentiel.setDescription(updateReferentielDto.getDescription());
        }
        if (updateReferentielDto.getPhotoCouverture() != null) {
            referentiel.setPhotoCouverture(updateReferentielDto.getPhotoCouverture());
        }
        if (updateReferentielDto.getEtat() != null) {
            referentiel.setEtat(updateReferentielDto.getEtat());
        }

        // Mettre à jour ou ajouter des compétences
        if (updateReferentielDto.getCompetences() != null) {
            for (UpdateCompetenceDto competenceDto : updateReferentielDto.getCompetences()) {
                Competence competence;

                // Si l'ID de la compétence est non nul, on cherche la compétence existante, sinon on en crée une nouvelle
                if (competenceDto.getId() != null) {
                    Optional<Competence> optionalCompetence = competenceService.findById(competenceDto.getId());
                    if (optionalCompetence.isPresent()) {
                        competence = optionalCompetence.get();
                    } else {
                        // Si la compétence avec cet ID n'est pas trouvée, on passe à la suivante
                        continue;
                    }
                } else {
                    // Créer une nouvelle compétence
                    competence = new Competence();
                    competence.setNom(competenceDto.getNom());
                    competence.setDescription(competenceDto.getDescription());
                    competence.setDureeAcquisition(competenceDto.getDureeAcquisition());
                    competence.setType(Competence.Type.valueOf(String.valueOf(competenceDto.getType())));
                    referentiel.getCompetences().add(competence);
                }

                // Gérer les modules associés à la compétence
                if (competenceDto.getModules() != null) {
                    for (UpdateModuleDto moduleDto : competenceDto.getModules()) {
                        Module module;

                        // Si l'ID du module est non nul, on cherche le module existant, sinon on en crée un nouveau
                        if (moduleDto.getId() != null) {
                            Optional<Module> optionalModule = moduleService.findById(moduleDto.getId());
                            if (optionalModule.isPresent()) {
                                module = optionalModule.get();
                            } else {
                                // Si le module avec cet ID n'est pas trouvé, on passe au suivant
                                continue;
                            }
                        } else {
                            // Créer un nouveau module
                            module = new Module();
                            module.setNom(moduleDto.getNom());
                            module.setDescription(moduleDto.getDescription());
                            module.setDureeAcquisition(moduleDto.getDureeAcquisition());
                            competence.getModules().add(module);
                        }

                        // Mettre à jour les champs du module (que ce soit un nouveau ou un existant)
                        module.setNom(moduleDto.getNom());
                        module.setDescription(moduleDto.getDescription());
                        module.setDureeAcquisition(moduleDto.getDureeAcquisition());
                    }
                }
            }
        }

        // Sauvegarder les modifications
        referentielService.save(referentiel);

        return ResponseEntity.ok(referentiel);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Referentiel> updateReferentiel(@PathVariable Long id, @RequestBody Referentiel referentiel) {
        referentiel.setId(id);
        Referentiel updatedReferentiel = referentielService.update(referentiel);
        return ResponseEntity.ok(updatedReferentiel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferentiel(@PathVariable Long id) {
        referentielService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
