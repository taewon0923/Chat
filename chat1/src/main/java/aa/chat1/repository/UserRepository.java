package aa.chat1.repository;


import aa.chat1.domain.User;
import aa.chat1.domain.UserDetails;
import aa.chat1.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String userName);
    List<Users> findAllUserName();
    List<UserDetails> findAllById(Long id);
}
