package com.siva.main.jwt;

import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {

	private static final String JWT_ALGO = "HmacSHA256";

	private static String base64UrlEncode(byte[] data) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
	}

	private static byte[] base64UrlDecode(String data) {
		return Base64.getUrlDecoder().decode(data);
	}

	private static byte[] hmacSha256(byte[] key, byte[] data) throws Exception {
		Mac mac = Mac.getInstance(JWT_ALGO);
		SecretKeySpec secretKey = new SecretKeySpec(key, JWT_ALGO);
		mac.init(secretKey);
		return mac.doFinal(data);
	}

	public static String createJwt(String userId, String role, String secret, long ttlSeconds) throws Exception {
		long now = System.currentTimeMillis();
		long exp = now + ttlSeconds * 1000;
		String payloadJson = "{" +
				"\"userId\":\"" + userId + "\"," +
				"\"role\":\"" + role + "\"," +
				"\"iat\":" + now + "," +
				"\"exp\":" + exp +
				"}";
		return sign(payloadJson, secret);
	}

	private static String sign(String payloadJson, String secret) throws Exception {
		String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
		String headerPart = base64UrlEncode(headerJson.getBytes("UTF-8"));
		String payloadPart = base64UrlEncode(payloadJson.getBytes("UTF-8"));
		String unsignedToken = headerPart + "." + payloadPart;
		byte[] signedToken = hmacSha256(secret.getBytes("UTF-8"), unsignedToken.getBytes("UTF-8"));
		String signaturePart = base64UrlEncode(signedToken);
		return unsignedToken + "." + signaturePart;
	}

	private static boolean signatureComparision(String original, String expected) {
		if (original == null || expected == null)
			return false;
		if (original.length() != expected.length())
			return false;
		int result = 0;
		for (int i = 0; i < original.length(); i++) {
			result |= original.charAt(i) ^ expected.charAt(i);
		}
		return result == 0;
	}

	private static Long extractLongField(String json, String field) {
		String pattern = "\"" + field + "\":";
		int idx = json.indexOf(pattern);
		if (idx == -1)
			return null;
		idx += pattern.length();
		int end = idx;
		while (end < json.length() && Character.isDigit(json.charAt(end))) {
			end++;
		}
		if (end == idx)
			return null;
		return Long.parseLong(json.substring(idx, end));
	}

	private static void validateStandardClaims(String payloadJson) {
		long now = System.currentTimeMillis();

		Long exp = extractLongField(payloadJson, "exp");
		if (exp != null && now > exp) {
			throw new SecurityException("Token expired");
		}

		Long iat = extractLongField(payloadJson, "iat");
		if (iat != null && iat > now + 1000L * 60L) {
			throw new SecurityException("Token issued in the future");
		}
	}

	public static String verifyJwt(String token, String secret) throws Exception {
		String[] parts = token.split("\\.");
		if (parts.length != 3) {
			throw new IllegalArgumentException("Invalid token");
		}

		String headerPart = parts[0];
		String payloadPart = parts[1];
		String signaturePart = parts[2];

		byte[] headerBytes = base64UrlDecode(headerPart);
		String headerJson = new String(headerBytes, "UTF-8");
		if (!headerJson.contains("\"alg\":\"HS256\"")) {
			throw new SecurityException("Unsupported alg");
		}

		String unsignedToken = headerPart + "." + payloadPart;
		byte[] expectedSigBytes = hmacSha256(secret.getBytes("UTF-8"), unsignedToken.getBytes("UTF-8"));
		String expectedSignaturePart = base64UrlEncode(expectedSigBytes);

		if (!signatureComparision(signaturePart, expectedSignaturePart)) {
			throw new SecurityException("Invalid signature");
		}

		byte[] payloadBytes = base64UrlDecode(payloadPart);
		String payloadJson = new String(payloadBytes, "UTF-8");

		validateStandardClaims(payloadJson);

		return payloadJson;
	}
}
