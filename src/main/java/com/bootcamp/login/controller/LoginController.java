package com.bootcamp.login.controller;

import com.bootcamp.login.model.User;
import com.bootcamp.login.repository.UserRepository;
import com.bootcamp.login.request.LoginRequest;
import com.bootcamp.login.response.JwtResponse;
import com.bootcamp.login.response.MessageResponse;
import com.bootcamp.login.security.JwtUtils;
import com.bootcamp.login.security.UserDetailsImpl;
import com.bootcamp.login.service.CaptchaValidatorService;
import com.bootcamp.login.service.LoginServiceImpl;
import com.bootcamp.login.service.SendEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class used for the Login Process using the spring security.
 * @Author: pilprasa
 */

@CrossOrigin(origins ="*",maxAge = 3600)
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    SendEmailService sendEmailService;

    @Autowired
    CaptchaValidatorService captchaValidatorService;

    /**
     * This method does the login process.
     * @param loginRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        boolean isvalidCaptcha = captchaValidatorService.validateCaptcha(loginRequest.getCaptchaResp(),null);
        if (!isvalidCaptcha) {
            return ResponseEntity.
                    badRequest().
                    body(new MessageResponse("Error: Invalid Captcha not validated"));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @GetMapping("/request-reset-password")
    public void requestResetPassword(@RequestParam @NotNull  String email){
        try {
            String reset_password_link = loginService.resetPasswordRequestUrl(email);
            System.out.println(email);
            logger.info("Password Reset Link Created and sendingout an email to the recipient.");
            sendEmailService.sendEmail(email, reset_password_link);
        }catch(Exception e){
            logger.info("An Error while sending out an email to the recipient."+e.getMessage());
        }

    }

    @GetMapping("/get-user")
    public ResponseEntity<User> getUserDetails(@RequestParam @NotNull String username){
        System.out.println("GetUser:: username"+username);
        return ResponseEntity.ok(userRepository.findByUsername(username).get());
    }

    /**
     * This method does the forgot password.
     * @param user
     * @return
     */
    @PutMapping("/reset-password")
    public ResponseEntity<MessageResponse>forgetPassword(@RequestBody User user){
        User user_fromdb =  userRepository.findByUsername(user.getUsername()).get();
        if(user_fromdb!=null) {
            User cur_user = userRepository.findById(user_fromdb.getId()).get();
            cur_user.setId(cur_user.getId());
            cur_user.setPassword(passwordEncoder.encode(user.getNewPassword()));
            cur_user.setNewPassword(null);
            userRepository.save(cur_user);
            logger.error("Password change success ....");
            return ResponseEntity.ok(new MessageResponse("Password Changed successfully"));
        } else{
            logger.error("User does not exists");
            return ResponseEntity.badRequest().body(new MessageResponse("An Error occurred in resetting the password"));
        }
    }

    /**
     * This method does the reset Password, user will be redirected to this to reset the password.
     * @param user
     * @return
     */
    @PutMapping("/forget-password")
    public ResponseEntity<MessageResponse>changePassword(@RequestBody User user){
        User dbsuer = userRepository.findByUsername(user.getUsername()).get();
        User cur_user = userRepository.findById(dbsuer.getId()).get();
        boolean isPasswordMatch = passwordEncoder.matches(user.getPassword(), cur_user.getPassword());
        if(isPasswordMatch){
            cur_user.setPassword(passwordEncoder.encode(user.getNewPassword()));
            cur_user.setNewPassword("");
            userRepository.save(cur_user);
            logger.info("User Password changed Successfully");

            return ResponseEntity.ok(new MessageResponse("User Password changed Successfully"));
        }else{
            return ResponseEntity.badRequest().body(new MessageResponse("Old Password not matching.. Please enter correct Password"));
        }
    }

}

