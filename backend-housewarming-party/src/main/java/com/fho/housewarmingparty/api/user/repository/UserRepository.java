package com.fho.housewarmingparty.api.user.repository;

import java.util.List;
import java.util.Optional;

import com.fho.housewarmingparty.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderByNameAsc();

    Optional<User> findByEmail(String email);
}
