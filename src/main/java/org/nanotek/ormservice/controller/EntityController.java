package org.nanotek.ormservice.controller;

import java.io.Serializable;
import java.net.URI;

import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public class EntityController 
<T extends Base<ID>, ID extends Serializable , S extends BaseService<T,ID>> 
extends PageableController<T, S>{

	public EntityController(S serv) {
		super(serv);
	}

	@ApiResponses(value = {
			@ApiResponse(message = "Entity Added sucessfully" , code = 202), 
			@ApiResponse(message = "Constraint Violation ou Data Integrity Exception" , code = 409), 
			@ApiResponse(message = "AccessDeniedException Unauthorized - Quando Segurança Ativada" , code = 401) 
			})
	@PostMapping
	public ResponseEntity<T> addEntity(@RequestBody T entity){
		T savedEntity = service.addEntity(entity);
		return ResponseEntity.created(URI.create(getBaseUrl(savedEntity))).body(savedEntity);
	}
	
	@ApiResponses(value = {
			@ApiResponse(message = "Entity Added sucessfully" , code = 200), 
			@ApiResponse(message = "Constraint Violation Exception" , code = 409), 
			@ApiResponse(message = "AccessDeniedException Unauthorized - Quando Segurança Ativada e usuario nao possui nivel de acesso para operacao" , code = 401), 
			@ApiResponse(message = "NoSuchElementException Unauthorized - Quando Entity Não é Encontrado" , code = 500) 
	})
	@PutMapping(path="/{id}")
	public ResponseEntity<T> updateEntity (@PathVariable(name="id",required=true) ID id  , @RequestBody T entity){
		return ResponseEntity.accepted().body(service.updateEntity(id, entity));
	}
	
	@ApiResponses(value = {
			@ApiResponse(message = "Entity Added sucessfully" , code = 200), 
			@ApiResponse(message = "AccessDeniedException Unauthorized - Quando Segurança Ativada e usuario nao possui nivel de acesso para operacao" , code = 401), 
			@ApiResponse(message = "NoSuchElementException  - Quando Entity Não é Encontrado" , code = 500) 
	})
	@DeleteMapping(path="/{id}")
	public ResponseEntity<?> deleteEntity(@PathVariable(name="id",required=true) ID id ){
		return ResponseEntity.accepted().body(service.deleteEntity(id));
	}
	
	@ApiResponses(value = {
			@ApiResponse(message = "Entity Added sucessfully" , code = 200), 
			@ApiResponse(message = "AccessDeniedException Unauthorized - Quando Segurança Ativada e usuario nao possui nivel de acesso para operacao" , code = 401), 
			@ApiResponse(message = "NoSuchElementException  - Quando Entity Não é Encontrado" , code = 500) 
	})
	@GetMapping(path="/{id}")
	public ResponseEntity<T> findByEntityId(@PathVariable(name="id",required=true) ID id ){
		return ResponseEntity.ok(service.findByEntityId(id));
	}
	
	@ApiResponses(value = {
			@ApiResponse(message = "Entity added sucessfully" , code = 200), 
			@ApiResponse(message = "AccessDeniedException Unauthorized - Quando Segurança Ativada e usuario nao possui nivel de acesso para operacao" , code = 401), 
			@ApiResponse(message = "SysmatEntityException  - Quando a propriedade não é encontrada" , code = 500) 
	})
	@GetMapping(path="/{id}/{property}")
	public ResponseEntity<T> findByEntityIdFetchProperty(@PathVariable(name="id",required=true) ID id , 
			@PathVariable(name="property",required=true) String property){
		return ResponseEntity.ok(service.findByEntityIdFetchProperty(id,property));
	}
	
}
