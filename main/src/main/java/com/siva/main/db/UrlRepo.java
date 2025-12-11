package com.siva.main.db;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;

public interface UrlRepo extends R2dbcRepository<UrlEntity, Integer> {
	@Query("SELECT * FROM url WHERE url_pattern = :urlPattern")
	Flux<UrlEntity> findByUrlPattern(String urlPattern);
}
