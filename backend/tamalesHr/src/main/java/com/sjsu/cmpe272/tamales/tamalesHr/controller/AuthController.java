package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final static String TOKEN_URL ="http://0.0.0.0:8080/realms/tamalesHr/protocol/openid-connect/token";
  private final static String CLIENT_ID = "tamalesHr-rest-api";
  private final static String KEY_GRANT_TYPE = "grant_type";
  private final static String KEY_PASSWORD = "password";
  private final static String KEY_USERNAME = "username";
  private final static String KEY_CLIENT_ID = "client_id";;

  @PostMapping("/token")
  public ResponseEntity<?> getToken(@RequestParam String username, @RequestParam String password) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add(KEY_GRANT_TYPE, "password");
      body.add(KEY_USERNAME, username);
      body.add(KEY_PASSWORD, password);
      body.add(KEY_CLIENT_ID, CLIENT_ID);

      HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

      try {
          RestTemplate restTemplate = new RestTemplate();
          ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, entity, String.class);
          return ResponseEntity.ok(response.getBody());
      } catch(Exception e){
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
      }
  }
}
