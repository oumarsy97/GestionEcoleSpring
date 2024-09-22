package sn.odc.oumar.springproject.Web.Controller.Impl;



import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.mapping.Set;
import org.springframework.http.HttpStatus;
import sn.odc.oumar.springproject.Datas.Entity.Promo;
import sn.odc.oumar.springproject.Datas.Entity.Referentiel;
import sn.odc.oumar.springproject.Exceptions.ServiceException;
import sn.odc.oumar.springproject.Services.Interfaces.PromoService;
import sn.odc.oumar.springproject.Web.Dtos.Request.PromotionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.oumar.springproject.Web.Dtos.Response.ApiResponse;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/promos")
public class PromoController {


    private final PromoService promoService;

    @Autowired
    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Promo>> createPromotion(@RequestBody PromotionRequestDTO requestDTO) {
        try {
            // Check if a promotion with the same libelle already exists
            if (promoService.promoExists(requestDTO.getLibelle())) {
                return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "le libelle existe déja", null));
            }

            Promo promotion = new Promo();
            promotion.setLibelle(requestDTO.getLibelle());
            promotion.setDateDebut(requestDTO.getDateDebut());
            promotion.setDateFin(requestDTO.getDateFin());

            // Save the promotion using the service
            Promo savedPromo = promoService.savePromo(promotion);

            // Return success response
            return ResponseEntity.ok(ApiResponse.success("Promotion created successfully", savedPromo));
        } catch (Exception e) {
            // Handle exceptions and return error response
            return ResponseEntity.status(500).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Promo>> getPromotionById(@PathVariable Long id) {
        Optional<Promo> promo = promoService.findById(id);
        if (promo.isPresent()) {
            return ResponseEntity.ok(
                    new ApiResponse<>("success", HttpStatus.ACCEPTED, "Promotion found", promo.get())
            );
        } else {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>("error", HttpStatus.BAD_REQUEST, "Promotion not found", null)
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable Long id) {
        Optional<Promo> promo = promoService.findById(id);
        if (promo.isPresent()) {
            promoService.deletePromo(promo.get());
            return ResponseEntity.ok(
                    new ApiResponse<>("success", HttpStatus.ACCEPTED, "Promotion deleted successfully", null)
            );
        } else {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>("error", HttpStatus.BAD_REQUEST, "Promotion not found", null)
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Promo>> updatePromotion(@PathVariable Long id, @RequestBody Promo promo) {
        promo.setId(id);
        Promo updatedPromo = promoService.updatePromo(promo);
        return ResponseEntity.ok(
                new ApiResponse<>("success", HttpStatus.ACCEPTED, "Promotion updated successfully", updatedPromo)
        );
    }
    @Operation(summary = "Retrieve a promotion by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Promotion retrieved successfully")

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<Promo>>> getAllPromotions() {
        Iterable<Promo> promos = promoService.findAllPromos();
        return ResponseEntity.ok(
                new ApiResponse<>("success", HttpStatus.ACCEPTED, "List of all promotions", promos)
        );
    }

}
