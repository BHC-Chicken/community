package com.peachdevops.community.repository;

import com.peachdevops.community.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>  {
    User findByUsername(String name);
    User findByNickname(String nickname);
}