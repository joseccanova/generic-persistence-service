package org.nanotek.ormservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.EntityRepository;
import org.nanotek.ormservice.api.SearchContainer;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;


public abstract class SearchServiceImpl<T extends Base<?>, R extends EntityRepository<T , ?>> {

	protected R repository;
	
	public List<T> findByEntityUsingExample(SearchContainer<T> entityContainer , Integer start , Integer pageSize){
		ExampleMatcher matcher = ExampleMatcher
					.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING)
					.withIgnoreNullValues();
		T entity = entityContainer.getEntity();
		Example<T> ex = Example.of(entity,matcher);
		Pageable pageRequest = Optional
		.ofNullable(entityContainer.getSortParameters())
		.filter(sp -> sp.keySet().size()>0)
		.map(sp ->{
			List<Order> sortOrder = new ArrayList<Order>();
			sp
			.keySet()
			.stream()
			.forEach(k -> {
				String value = sp.get(k);
				if ("ASC".equals(value))
				 	sortOrder.add(Order.asc(k));
				else 
				 	sortOrder.add(Order.desc(k));
			});
			return PageRequest.of(start , pageSize, Sort.by(sortOrder));
		}).orElse(PageRequest.of(start, pageSize));
		return repository.findAll(ex, pageRequest).getContent();
	}
}
