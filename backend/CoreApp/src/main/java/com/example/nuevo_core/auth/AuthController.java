package com.example.nuevo_core.auth;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE},allowCredentials = "true")
public class AuthController {

    public AuthController (){

    }

    @PostMapping("/set-cookie")
    public ResponseEntity<String> setCookie(HttpServletResponse response){
        System.out.println("👉 Entró al endpoint set-cookie");

        Cookie cookie = new Cookie("user_id","EMP1234");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true SOLO en HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        // Spring 6+
        response.addCookie(cookie);

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/get-cookie")
    public String getCookie(@CookieValue(value = "user_id",defaultValue = "Invitado") String usuario){
       return "Hola " + usuario;
    }

    @GetMapping("/me")
    public String getCurrentUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("❌ No cookies in request");
        } else {
            for (Cookie c : cookies) {
                System.out.println("🍪 Cookie: " + c.getName() + " = " + c.getValue());
            }
        }

        return "Test";
    }
}
