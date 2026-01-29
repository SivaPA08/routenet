package com.siva.main.web;

import org.springframework.stereotype.Service;

import com.siva.main.db.UrlEntity;
import com.siva.main.db.UrlRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DBService {

	private final UrlRepo repo;

	public DBService(UrlRepo repo) {
		this.repo = repo;
	}

	public Flux<UrlEntity> displayall() {
		return repo.findAll();
	}

	public Mono<UrlEntity> save(UrlEntity urlEntity) {
		return repo.save(urlEntity);
	}

	public Mono<Void> deleteRoute(Integer id) {
		return repo.deleteById(id);
	}

}
