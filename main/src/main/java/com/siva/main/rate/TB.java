package com.siva.main.rate;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

// @Component
public final class TB {
	// static final long CAPACITY = 1000L;
	private final double CAPACITY;
	private final double RATE;
	private final double COST;
	private double tokens;
	private double lastTimestamp;

	public TB(double capacity, double rate, double cost) {
		this.CAPACITY = capacity;
		this.RATE = rate;
		this.COST = cost;
		this.tokens = CAPACITY;
		this.lastTimestamp = now();
	}

	public synchronized boolean allowToken() { // synchronized for atomocity
		refill();
		if (tokens >= COST) {
			tokens -= COST;
			return true;
		}
		return false;
	}

	private void refill() {
		double current = now();
		double elapsed = lastTimestamp - current;
		if (elapsed > 0) {
			tokens = Math.min(CAPACITY, tokens + elapsed * RATE);
			lastTimestamp = current;
		}
	}

	private double now() {
		return System.nanoTime() / 1_000_000_000.0;
	}

}
