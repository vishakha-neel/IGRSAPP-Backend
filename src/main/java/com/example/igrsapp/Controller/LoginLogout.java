package com.example.igrsapp.Controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginLogout {

    @Autowired
    private AuthenticationManager authenticationManager;

    // Fetch CAPTCHA
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getCaptcha(HttpSession session) throws IOException {
        CaptchaImage obj = new CaptchaImage();
        BufferedImage ima = obj.getCaptchaImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(ima, "jpg", baos);
        byte[] imageInByteArray = baos.toByteArray();
        String b64 = Base64.getEncoder().encodeToString(imageInByteArray);
        String captchaStr = obj.getCaptchaString();
        session.setAttribute("captchaStr", captchaStr);

        Map<String, String> response = new HashMap<String, String>();
        response.put("captchaImage", "data:image/jpg;base64," + b64);
        response.put("sessionId", session.getId());
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String userId,
                                                    @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, password));

            SecurityContextHolder.getContext().setAuthentication(auth);

            response.put("success", true);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            response.put("success", false);
            response.put("message", "Wrong UserId/Password or Disabled User");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Logout
    @RequestMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    // @RequestMapping(value = "/logout", method = RequestMethod.GET)
    // public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
    //     String uId = modelInitializer.getId(request);
    //     Map<String, String> response = new HashMap<String, String>();
    //     if (uId == null) {
    //         response.put("message", "No active session");
    //         return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
    //     }
    //     // commonService.insertLogs(uId, "2", request);
    //     HttpSession session = request.getSession(false);
    //     if (session != null) {
    //         session.removeAttribute("uBean");
    //         session.invalidate();
    //     }
    //     response.put("message", "Logged out successfully");
    //     return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
    // }
    
}
