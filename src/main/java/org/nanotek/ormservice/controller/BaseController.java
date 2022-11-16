package org.nanotek.ormservice.controller;

import java.util.List;

import org.nanotek.ormservice.IBase;
import org.nanotek.ormservice.service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public abstract class BaseController<T extends IBase<?>  , S extends BaseService<T,?>> 
implements UrlBaseLocator {

	public static final Integer MAX_FIND_ALL = Integer.MAX_VALUE-1;

	protected S service;
	
	public BaseController(S serv) {
		this.service = serv;
	}
	
	public BaseController() { 
	}
	
	@ApiResponses(value = {
			@ApiResponse(message = "Entity added sucessfully" , code = 200), 
			@ApiResponse(message = "AccessDeniedException Unauthorized - Quando Segurança Ativada e usuário nao possui nivel de acesso para operação" , code = 401), 
			@ApiResponse(message = "RuntimeException  - " , code = 500) 
	})
	@DeleteMapping(path="/delete-list")
	public ResponseEntity<?> deleteAll(@RequestBody List<T> items){
		service.deleteAll(items);
		return ResponseEntity.accepted().body(true);
	}
	
	@ApiResponses(value = {
			@ApiResponse(message = "Entity added sucessfully" , code = 200), 
			@ApiResponse(message = "AccessDeniedException Unauthorized - Quando Segurança Ativada e usuário nao possui nivel de acesso para operação" , code = 401), 
			@ApiResponse(message = "RuntimeException  - " , code = 500) 
	})
	@PutMapping(path="/update-list")
	public ResponseEntity<?> salveAll(@RequestBody List<T> items){
		return ResponseEntity.accepted().body(service.saveAll(items));
	}
	
}
