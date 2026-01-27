package com.siva.main.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siva.main.db.UrlEntity;
import com.siva.main.db.UrlRepo;

import reactor.core.publisher.Flux;

@Service
public class DBService {

	private UrlEntity urlEntity;
	private final UrlRepo repo;

	public DBService(UrlRepo repo) {
		this.repo = repo;
	}

	public Flux<UrlEntity> displayall() {
		return repo.findAll();
	}

}
