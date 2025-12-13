package com.siva.main.gw;

import org.springframework.http.HttpHeaders;

public final class HeaderUtil {
	private static final String[] HOP_HEADERS = {
			"connection", "keep-alive", "proxy-authenticate", "proxy-authorization",
			"te", "trailers", "transfer-encoding", "upgrade"
	};

	private HeaderUtil() {
	}

	public static void copyRequestHeader(HttpHeaders from, HttpHeaders to) {

		from.forEach((k, v) -> {
			if (!isHopHeader(k)) {
				to.put(k, v);
			}
		});
	}

	public static void copyResponseHeader(HttpHeaders from, HttpHeaders to) {
		return;
	}

	public static boolean isHopHeader(String name) {
		for (String h : HOP_HEADERS) {
			if (h.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}
