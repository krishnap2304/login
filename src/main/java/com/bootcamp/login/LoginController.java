package com.bootcamp.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    public ResponseEntity<String> getLoginDetails(){
        return ResponseEntity.ok("Hello from Login controller");
    }

}
