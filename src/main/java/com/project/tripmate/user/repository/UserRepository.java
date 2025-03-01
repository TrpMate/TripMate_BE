package com.project.tripmate.user.repository;

import com.project.tripmate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMailVerificationToken(String token);

    Optional<User> findBySocialId(String socialId);

    Optional<User> findBySocialTypeAndEmail(String socialType, String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
