package org.nanotek.ormservice.service;

import java.io.Serializable;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.nanotek.ormservice.IBase;
import org.nanotek.ormservice.validation.CreateValidationGroup;
import org.nanotek.ormservice.validation.UpdateValidationGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CrudBaseService<T extends IBase<ID>, ID extends Serializable> 
extends BaseService<T,ID>{
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity; will never be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
	 */
	<S extends T> S save(S entity);


	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found.
	 * @throws IllegalArgumentException if {@literal id} is {@literal null}.
	 */
	Optional<T> findById(ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@literal id} is {@literal null}.
	 */
	boolean existsById(ID id);

	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	Iterable<T> findAll();

	/**
	 * Returns all instances of the type {@code T} with the given IDs.
	 * <p>
	 * If some or all ids are not found, no entities are returned for these IDs.
	 * <p>
	 * Note that the order of elements in the result is not guaranteed.
	 *
	 * @param ids must not be {@literal null} nor contain any {@literal null} values.
	 * @return guaranteed to be not {@literal null}. The size can be equal or less than the number of given
	 *         {@literal ids}.
	 * @throws IllegalArgumentException in case the given {@link Iterable ids} or one of its items is {@literal null}.
	 */
	Iterable<T> findAllById(Iterable<ID> ids);

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities.
	 */
	long count();

	/**
	 * Deletes the entity with the given id.
	 *
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
	 */
	void deleteById(ID id);

	/**
	 * Deletes a given entity.
	 *
	 * @param entity must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	void delete(T entity);

	/**
	 * Deletes the given entities.
	 *
	 * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
	 * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
	 */
	void deleteAll(Iterable<? extends T> entities);

	/**
	 * Deletes all entities managed by the repository.
	 */
	void deleteAll();
	
	@Override
	@Transactional
	@Validated(value=CreateValidationGroup.class)
	default T addEntity(@Valid @NotNull(groups = CreateValidationGroup.class) T entity) {
		return save(entity);
	}
	
	@Override
	@Transactional
	@Validated(value=UpdateValidationGroup.class)
	default T updateEntity(@Valid @NotNull(groups = UpdateValidationGroup.class) ID id,
			@Valid @NotNull(groups = UpdateValidationGroup.class) T entity) {
		return findById(id)
				.filter(s ->s.getId().equals(entity.getId()))
				.map(s ->{
					copyProperties(entity,s);
					prepare(s);
					return save(s);})
				.orElseThrow();
	}


	default void prepare(T s) {};


	default void copyProperties(T entity, T s) {
		BeanUtils.copyProperties(entity, s);
	}

	@Override
	@Transactional
	default Object deleteEntity(@Valid @NotNull ID id) {
		return findById(id)
				.map(s ->{
					delete (s);
					return true;
				}).orElseThrow();
	}
	
	@Override
	@Transactional
	default T findByEntityId(@Valid @NotNull ID id) {
		return findById(id).orElseThrow();
	}
	
}
