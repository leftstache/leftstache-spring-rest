package com.leftstache.spring.rest.stub;

import com.leftstache.spring.rest.core.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;

import java.io.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
public class RepositoryStub<ENTITY, ID extends Serializable> implements PagingAndSortingRepository<ENTITY, ID>, Restful<ENTITY, ID> {
	private Map<ID, ENTITY> entities;

	public RepositoryStub() {
		entities = new HashMap<>();
	}

	public void add(ID id, ENTITY entity) {
		entities.put(id, entity);
	}

	@Override
	public ENTITY findOne(ID id) {
		return entities.get(id);
	}

	@Override
	public Iterable<ENTITY> findAll(Sort sort) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<ENTITY> findAll(Pageable pageable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends ENTITY> S save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends ENTITY> Iterable<S> save(Iterable<S> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean exists(ID id) {
		return entities.containsKey(id);
	}

	@Override
	public Iterable<ENTITY> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<ENTITY> findAll(Iterable<ID> ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(ID id) {
		entities.remove(id);
	}

	@Override
	public void delete(ENTITY entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Iterable<? extends ENTITY> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}
}
