package com.siva.main.rate;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class RateService {

	private final ConcurrentHashMap<String, TB> bucketsPerUser = new ConcurrentHashMap<>();

	public boolean allowToken(String clientId) {
		TB bucket = bucketsPerUser.computeIfAbsent(clientId, id -> new TB(100, 10, 1));

		return bucket.allowToken(); // allowToken feature from TB.java

	}

}
