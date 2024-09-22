package sn.odc.oumar.springproject.Web.Controller.Impl;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.oumar.springproject.Datas.Entity.Competence;
import sn.odc.oumar.springproject.Datas.Entity.Module;

import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import sn.odc.oumar.springproject.Services.Interfaces.CompetenceService;
import sn.odc.oumar.springproject.Services.Interfaces.ReferentielService;
import sn.odc.oumar.springproject.Web.Dtos.Request.ReferentielDTO;
import sn.odc.oumar.springproject.Web.Dtos.Response.CompetenceResponseDTO;
import sn.odc.oumar.springproject.Web.Dtos.Response.ReferentielResponseDTO;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Transactional
@RequestMapping("/api/v1/referentiels")
public class ReferentielController {

    private final ReferentielService referentielService;

    private final CompetenceService  competenceService;

    @Autowired
    public ReferentielController(ReferentielService referentielService, CompetenceService competenceService) {
        this.referentielService = referentielService;
        this.competenceService = competenceService;
    }

    @PostMapping
    public ResponseEntity<Referentiel> createReferentiel(@Valid @RequestBody ReferentielDTO referentielRequestDTO) {
        // Map DTO to Entity Referentiel
        Referentiel referentiel = new Referentiel();
        referentiel.setLibelle(referentielRequestDTO.getLibelle());
        referentiel.setDescription(referentielRequestDTO.getDescription());
        referentiel.setPhotoCouverture(referentielRequestDTO.getPhotoCouverture());

        // Save the Referentiel
        Referentiel savedReferentiel = referentielService.save(referentiel);

        // Map Competences and Modules from DTO
        if (referentielRequestDTO.getCompetences() != null) {
            Set<Competence> competences = referentielRequestDTO.getCompetences().stream()
                    .map(compDTO -> {
                        Competence competence = new Competence();
                        competence.setNom(compDTO.getNom());
                        competence.setDescription(compDTO.getDescription());
                        competence.setDureeAcquisition(compDTO.getDureeAcquisition());
                        competence.setType(Competence.Type.valueOf(compDTO.getType()));
                        competence.setReferentiel(savedReferentiel); // Link competence to referentiel
                        referentielService.save(savedReferentiel); // Save again to persist the competences and modules


                        // Map Modules for each Competence
                        Set<Module> modules = compDTO.getModules().stream()
                                .map(modDTO -> {
                                    Module module = new Module();
                                    module.setNom(modDTO.getNom());
                                    module.setDescription(modDTO.getDescription());
                                    module.setDureeAcquisition(modDTO.getDureeAcquisition());
                                    module.setCompetence(competence); // Link module to competence
                                    return module;
                                }).collect(Collectors.toSet());
                        competence.setModules(modules); // Set modules in competence
                        return competence;
                    }).collect(Collectors.toSet());

            savedReferentiel.setCompetences(competences); // Set competences in referentiel
        }

        // Return the saved referentiel entity
        return ResponseEntity.ok(savedReferentiel);
    }


    @GetMapping("/{id}/competences")
    public Optional<ResponseEntity<Set<CompetenceResponseDTO>>> getCompetencesByReferentielId(@PathVariable Long id) {
            Optional<Referentiel> ref = referentielService.findById(id);
        return ref.map(referentiel -> ResponseEntity.ok(mapCompetencesToDTOs(referentiel.getCompetences())));
    }


    // Méthode pour mapper les compétences à CompetenceResponseDTO
    private Set<CompetenceResponseDTO> mapCompetencesToDTOs(Set<Competence> competences) {
        return competences.stream()
                .map(comp -> new CompetenceResponseDTO(comp.getId(), comp.getNom(), comp.getDescription(), comp.getDureeAcquisition(), comp.getType()))
                .collect(Collectors.toSet());
    }

    // Méthode pour créer ReferentielResponseDTO
    private ReferentielResponseDTO mapToResponseDTO(Referentiel referentiel, Set<CompetenceResponseDTO> competenceResponseDTOs) {
        return new ReferentielResponseDTO(
                referentiel.getId(),
                referentiel.getLibelle(),
                referentiel.getCode(),
                referentiel.getDescription(),
                referentiel.getPhotoCouverture(),
                (Stream<CompetenceResponseDTO>) competenceResponseDTOs
        );
    }

    @GetMapping
    public ResponseEntity<Iterable<Referentiel>> getAllReferentiels() {
        return ResponseEntity.ok(referentielService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Referentiel> getReferentielById(@PathVariable Long id) {
        Optional<Referentiel> referentiel = referentielService.findById(id);
        return referentiel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Referentiel> updateReferentiel(@PathVariable Long id, @RequestBody Referentiel referentiel) {
        referentiel.setId(id); // Assure-toi de définir l'ID pour l'update
        Referentiel updatedReferentiel = referentielService.update(referentiel);
        return ResponseEntity.ok(updatedReferentiel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferentiel(@PathVariable Long id) {
        referentielService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
