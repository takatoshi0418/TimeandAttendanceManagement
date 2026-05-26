package com.github.takatoshi0418.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.takatoshi0418.entity.User;

/**
 * UserRepositoryインターフェース
 * Userエンティティのリポジトリインターフェース
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmployeeNumber(String employeeNumber);
}
