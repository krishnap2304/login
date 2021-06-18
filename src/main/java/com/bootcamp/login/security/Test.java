package com.bootcamp.login.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

    public static void main(String[] args) {
        String password = "Kpjenkins4321";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println(hashedPassword);
        boolean isMatch = passwordEncoder.matches(password, hashedPassword);
        System.out.println(isMatch);


    }
}