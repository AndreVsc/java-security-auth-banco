package andrevsc.java_api_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import andrevsc.java_api_security_web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT e FROM User e WHERE e.username = (:username)")
    public User findByUsername(@Param ("username") String username);
}
