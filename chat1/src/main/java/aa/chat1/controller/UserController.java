package aa.chat1.controller;


import aa.chat1.configuration.JwtFilter;
import aa.chat1.domain.User;
import aa.chat1.domain.UserDetails;
import aa.chat1.domain.dto.UserDetailRequest;
import aa.chat1.domain.dto.UserJoinRequest;
import aa.chat1.domain.dto.UserLoginRequest;
import aa.chat1.handler.ChatHandler;
import aa.chat1.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {


    private final UserService userService;
    private final JwtFilter jwtFilter;
    List<String> users = new ArrayList<>();

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
    public ResponseEntity<User> mainPage(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        Claims claims = Jwts.parser().setSigningKey("1234").parseClaimsJws(jwtToken).getBody();
        String username = (String) claims.get("userName");
        boolean isAdmin = (boolean) claims.get("isAdmin");

        User userData = new User(username, isAdmin);
        return ok().body(userData);
    }

    @GetMapping("/userList")
    public ResponseEntity<List> userList(){
        List users = userService.userList();
        return ok().body(users);
    }

    @PostMapping("/userList/Detail")
    public ResponseEntity<List<UserDetails>> userDetail(@RequestBody UserDetailRequest request) {
        String id = request.getId();
        Long userId = Long.parseLong(id);
        List<UserDetails> users = userService.userDetail(userId);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/Chat/Users")
    public Collection<String> ChatUserList(){
        return userService.chatUserList();
    }
}
