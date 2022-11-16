package org.nanotek.ormservice.service;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.nanotek.ormservice.IBase;
import org.nanotek.ormservice.api.SearchContainer;
import org.nanotek.ormservice.validation.CreateValidationGroup;
import org.nanotek.ormservice.validation.UpdateValidationGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface BaseService<T extends IBase<ID>, ID extends Serializable> {

	@Transactional
	List<T> findByEntityUsingExample(SearchContainer<T> entity , Integer start , Integer size);
	
	@Transactional
	List<T> findAll(Integer pageSize, Integer count);
	
	@Transactional
	List<T> findAll(Pageable sort);
	
	@Transactional
	void deleteAll(Iterable<? extends T> entities);
	
	@Transactional
	<S extends T> List<S> saveAll(Iterable<S> entities);
	
	@Transactional
	T addEntity(@Valid @NotNull(groups={CreateValidationGroup.class}) T  entity);
	
	@Transactional
	T updateEntity(@Valid @NotNull(groups={UpdateValidationGroup.class})  ID id , @Valid @NotNull(groups={UpdateValidationGroup.class})  T entity);
	
	@Transactional
	Object deleteEntity(@Valid @NotNull ID id);
	
	@Transactional
	T findByEntityId(@Valid @NotNull ID id);

	@Transactional
	T findByEntityIdFetchProperty(ID id , String property);
	
}
