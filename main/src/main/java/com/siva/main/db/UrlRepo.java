package com.siva.main.db;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface UrlRepo extends R2dbcRepository<UrlEntity, Integer> {
	@Query("SELECT * FROM url WHERE url_pattern = :urlPattern") // changet his later
	Flux<UrlEntity> findByUrlPattern(String urlPattern);

	Flux<UrlEntity> findAll();
}
