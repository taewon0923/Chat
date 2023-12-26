package aa.chat1.service;


import aa.chat1.domain.Admin;
import aa.chat1.domain.User;
import aa.chat1.domain.UserDetails;
import aa.chat1.domain.UserList;

import aa.chat1.exception.AppException;
import aa.chat1.exception.ErrorCode;
import aa.chat1.handler.ChatHandler;
import aa.chat1.repository.UserRepository;
import aa.chat1.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ChatHandler chatHandler;

    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000*60*60L;

    public String join(String userName, String password, String phone, String email){
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new RuntimeException(userName+"는 이미 있습니다");
                });

        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .phone(phone)
                .email(email)
                .isAdmin(true)
                .build();
        userRepository.save(user);
        return "success";
    }

    public String login(String userName, String password) {
        //userName x
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다"));
        //password x
        if(!encoder.matches(password, selectedUser.getPassword())){
            throw  new AppException(ErrorCode.INVALID_PASSWORD,"패스워드를 잘못 입력했습니다");
        }

        String token = JwtTokenUtil.createToken(selectedUser.getUserName(),selectedUser.isAdmin(),key,expireTimeMs);

        return token;
    }

    public List<UserList> userList(){
        return userRepository.findAllBy();
    }
    public List<UserDetails> userDetail(Long id){
        return userRepository.findAllById(id);
    }

    public Collection<String> chatUserList(){
        return chatHandler.usersList();
    }


}
