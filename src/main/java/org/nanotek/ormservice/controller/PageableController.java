package org.nanotek.ormservice.controller;

import java.util.List;
import java.util.Optional;

import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.api.SearchContainer;
import org.nanotek.ormservice.service.BaseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class PageableController<T extends Base<?> , S extends BaseService<T,?>> 
extends BaseController<T, S> {
	
	
	public PageableController(S serv) {
		super(serv);
	}
	
	@GetMapping
	public ResponseEntity<List<T>> 
			getAll(@RequestParam(name = "start" ,required = false)Integer start , 
			@RequestParam(name = "max" ,required = false)Integer max , 
			@RequestParam(name = "sort" ,required = false)String sort,
			@RequestParam(name="page"   , required=false) Integer page){
		Integer count = Optional.ofNullable(start).orElse(0);
		Integer pageSize = Optional.ofNullable(max).orElse(MAX_FIND_ALL);
		Integer pageI = Optional.ofNullable(page).orElse(0);
		return ResponseEntity
		.ok(Optional
			.ofNullable(sort)
			.map(s -> {
				Pageable pageable = PageRequest.of(pageI, pageSize , Sort.by(Sort.Direction.ASC, s));
				return service.findAll(pageable);
			}).orElse(service.findAll(pageSize  , count)));
	}
	
	@PostMapping(path="/search")
	public ResponseEntity<List<T>> findByEntityUsingExample(@RequestParam(name = "start" ,required = false)Integer start , 
			@RequestParam(name = "max" ,required = false)Integer max , @RequestBody(required = true) SearchContainer<T> entityContainer){
		Integer count = Optional.ofNullable(start).orElse(0);
		Integer pageSize = Optional.ofNullable(max).orElse(MAX_FIND_ALL);
		return ResponseEntity.ok(service.findByEntityUsingExample(entityContainer,count,pageSize));
	}
}
