package sn.odc.oumar.springproject.Web.Controller.Impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.oumar.springproject.Datas.Entity.Competence;
import sn.odc.oumar.springproject.Datas.Entity.Promo;
import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import sn.odc.oumar.springproject.Services.Interfaces.CompetenceService;
import sn.odc.oumar.springproject.Services.Interfaces.ReferentielService;
import sn.odc.oumar.springproject.Web.Dtos.Request.CompetenceRequestDTO;
import sn.odc.oumar.springproject.Web.Dtos.Response.ApiResponse;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/competences")
@Tag(name = "Competences", description = "API pour gérer les compétences")
public class CompetenceController {
    private static CompetenceService competenceService;
    private ReferentielService referentielService;

    @Autowired
    public CompetenceController(CompetenceService competenceService, ReferentielService referentielService) {
        this.competenceService = competenceService;
        this.referentielService = referentielService;


    }
    @PostMapping
    public ResponseEntity<ApiResponse<Competence>> createCompetence(@RequestBody @Valid CompetenceRequestDTO request) {
        // Création de la compétence à partir des données du DTO
        Competence competence = new Competence();
        competence.setNom(request.getNom());
        competence.setDescription(request.getDescription());
        competence.setDureeAcquisition(request.getDureeAcquisition());
        competence.setType(Competence.Type.valueOf(request.getType()));

        // Vérifier si le référentiel existe
        Long referentielId = request.getReferentielId();
        Optional<Referentiel> referentielOptional = referentielService.findById(referentielId);

        if (referentielOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<> ("echec", HttpStatus.BAD_REQUEST,"Referentiel non trouvée", null));

        }
        // Vérification du type
        if (request.getType() == null || request.getType().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>("echec", HttpStatus.BAD_REQUEST,"Le type est obligatoire", null));
        }

        // Associer le référentiel à la compétence
        competence.setReferentiel(referentielOptional.get());

        // Sauvegarder la compétence
        competenceService.save(competence);

        return ResponseEntity.ok(
                new ApiResponse<>("success", HttpStatus.CREATED,"Compétence créée avec succès", competence)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Competence>> getCompetenceById(@PathVariable Long id) {
        Optional<Competence> competenceOptional = competenceService.findById(id);
        return competenceOptional
               .map(competence -> ResponseEntity.ok(
                        new ApiResponse<>("success", HttpStatus.OK,"Compétence récupérée avec succès", competence)
                ))
               .orElseGet(() -> ResponseEntity.notFound().build());

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompetence(@PathVariable Long id) {
        competenceService.findById(id).ifPresent(competenceService::deleteById);
        return ResponseEntity.ok(new ApiResponse<>("success", HttpStatus.NO_CONTENT,"Compétence supprimée avec succès", null));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Competence>> updateCompetence(@PathVariable Long id, @RequestBody Competence competence) {
        Optional<Competence> competenceOptional = competenceService.findById(id);
        if (competenceOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        competence.setId(id);
        competenceService.save(competence);
        return ResponseEntity.ok(new ApiResponse<>("success", HttpStatus.OK,"Compétence mise à jour avec succès", competence));
    }


}
