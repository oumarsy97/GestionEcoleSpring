package sn.odc.oumar.springproject.Web.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.oumar.springproject.Datas.listeners.impl.SoftDeletable;
import sn.odc.oumar.springproject.Services.Interfaces.BaseService;
import sn.odc.oumar.springproject.Services.Interfaces.ResponseService;
import sn.odc.oumar.springproject.Web.Dtos.Response.BaseResponse;

import java.util.List;
import java.util.Optional;

public abstract class BaseControllerImpl<T extends SoftDeletable, Dto , ID> {

    protected final BaseService<T, ID> service;

    @Autowired
    protected ResponseService responseService;

    protected BaseControllerImpl(BaseService<T, ID> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Dto>> create(@RequestBody Dto dto) {
        T entity = convertToEntity(dto); // Méthode à définir pour la conversion
        T createdEntity = service.create(entity);
        Dto createdDto = convertToDto(createdEntity); // Méthode à définir pour la conversion
        return responseService.createdResponse(createdDto, "Entity created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Dto>> findById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        if (entity.isPresent()) {
            Dto dto = convertToDto(entity.get()); // Méthode à définir pour la conversion
            return responseService.successResponse(dto, "Entity found");
        } else {
            return responseService.notFoundResponse("Entity not found");
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<T>>> readAll() {
        List<T> entities = service.findAll();
        entities.stream()
                .map(this::convertToDto) // Méthode à définir pour la conversion
                .toList();
        return responseService.successResponse(entities, "Entities retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Dto>> update(@PathVariable ID id, @RequestBody Dto dto) {
        T entity = convertToEntity(dto); // Méthode à définir pour la conversion
        T updatedEntity = service.update(id, entity);
        Dto updatedDto = convertToDto(updatedEntity); // Méthode à définir pour la conversion
        return responseService.successResponse(updatedDto, "Entity updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable ID id) {
        service.delete(id);
        return responseService.deletedResponse("Entity deleted successfully");
    }

    // Méthodes abstraites à définir dans les sous-classes pour la conversion
    protected abstract T convertToEntity(Dto dto);

    protected abstract Dto convertToDto(T entity);
}
