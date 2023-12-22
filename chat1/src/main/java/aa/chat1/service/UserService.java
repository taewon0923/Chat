package aa.chat1.service;


import aa.chat1.domain.User;
import aa.chat1.domain.UserDetails;
import aa.chat1.domain.Users;
import aa.chat1.exception.AppException;
import aa.chat1.exception.ErrorCode;
import aa.chat1.repository.UserRepository;
import aa.chat1.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
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

        String token = JwtTokenUtil.createToken(selectedUser.getUserName(),key,expireTimeMs);

        return token;
    }

    public List<Users> userList(){
        return userRepository.findAllUserName();
    }
    public List<UserDetails> userDetail(Long id){
        return userRepository.findAllById(id);
    }
}
