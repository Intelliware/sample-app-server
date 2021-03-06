package com.intelliware.sample.api.dao;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.intelliware.sample.api.model.User;

@Transactional(value=TxType.MANDATORY)
public interface UserRepository extends PagingAndSortingRepository<User, Long>{

	List<User> findByUsername(String username);

	List<User> findByNameLikeIgnoreCase(String filterString, Sort sort);

	Page<User> findByNameLikeIgnoreCase(String filterString, Pageable pageable);

}