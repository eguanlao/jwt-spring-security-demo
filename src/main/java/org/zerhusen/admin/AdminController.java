package org.zerhusen.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> getProtectedGreeting() {
    return ResponseEntity.ok("Greetings from admin protected method!");
  }

}