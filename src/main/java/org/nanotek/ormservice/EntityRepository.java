package org.nanotek.ormservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository<T,ID> extends JpaRepository<T, ID> {
	
	
	default Page<T> findAll(Integer pageSize , Integer count){ 
		Pageable page = PageRequest.of(count, pageSize);
		return findAll(page);
	}
	
}
