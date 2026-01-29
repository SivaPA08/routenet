package com.siva.main.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siva.main.db.UrlEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//go to localhost:8080/admin/index.html for the ui

@RestController
@RequestMapping("/admin")
public class APIService {

	private final DBService dbService;

	public APIService(DBService dbService) {
		this.dbService = dbService;
	}

	@GetMapping("/display")
	public Flux<UrlEntity> displayalltables() {
		Flux<UrlEntity> res = dbService.displayall();
		return res;
	}

	@PostMapping("routes/save")
	public Mono<UrlEntity> saveUrRoutes(@RequestBody UrlEntity routes) {
		return dbService.save(routes);
	}

}
