package com.siva.main.jwt;

import java.util.Base64;

public class JwtUtil {

	private static final String JWT_ALGO = "HmacSHA256";

	private static String base64UrlEncode(byte[] data) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
	}

	private static byte[] base64UrlDecode(String data) {
		return Base64.getUrlDecoder().decode(data);
	}
}
