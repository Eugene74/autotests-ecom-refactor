package com.ecom.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONObject;

public class JwtUtils {

  private JwtUtils() {}

  public static String createJwt(String header, String payload, String signature) {
    JSONObject jwt = new JSONObject();
    jwt.put("header", header);
    jwt.put(
        "payload", Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8)));
    jwt.put("signature", signature);
    return jwt.toString();
  }

  public static String decodePayload(String jwtResponse) {
    JSONObject jwtObject = new JSONObject(jwtResponse);
    String encodedPayload = jwtObject.getString("payload");
    return new String(Base64.getDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
  }
}
