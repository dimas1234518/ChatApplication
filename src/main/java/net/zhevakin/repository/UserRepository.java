package net.zhevakin.repository;

import net.zhevakin.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByUsername(String username);

	List<User> findByUsernameIn(Set<String> users);
}
