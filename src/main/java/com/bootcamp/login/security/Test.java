package com.bootcamp.login.security;
import com.bootcamp.login.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        String password = "123456";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println(hashedPassword);
        boolean isMatch = passwordEncoder.matches(password, hashedPassword);
        System.out.println(isMatch);


    }
}