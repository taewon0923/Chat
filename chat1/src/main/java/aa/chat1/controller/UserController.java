package aa.chat1.controller;


import aa.chat1.configuration.JwtFilter;
import aa.chat1.domain.User;
import aa.chat1.domain.UserDetails;
import aa.chat1.domain.dto.UserJoinRequest;
import aa.chat1.domain.dto.UserLoginRequest;
import aa.chat1.service.UserService;
import aa.chat1.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {


    private final UserService userService;
    private final JwtFilter jwtFilter;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest dto){
        userService.join(dto.getUserName(), dto.getPassword(),dto.getPhone(),dto.getEmail());
        return ok().body("회원 가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto){
        String token = userService.login(dto.getUserName(),dto.getPassword());
        return ok().body(token);
    }

    @GetMapping("/main")
    public ResponseEntity<String> mainPage(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey("1234").parseClaimsJws(jwtToken).getBody();
            String username = (String) claims.get("userName");
            return ok().body(username);

    }
    @GetMapping("/userList")
    public ResponseEntity<List> userList(){
        List users = userService.userList();
        return ok().body(users);
    }

    @PostMapping("/userList/Detail")
    public ResponseEntity<List<UserDetails>> userDetail(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id");
        Long userId = Long.parseLong(id);
        List<UserDetails> users = userService.userDetail(userId);
        return ResponseEntity.ok().body(users);
    }

}
