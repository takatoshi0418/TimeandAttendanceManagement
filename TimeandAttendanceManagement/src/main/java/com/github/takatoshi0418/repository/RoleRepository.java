package com.github.takatoshi0418.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.takatoshi0418.entity.Role;

/**
 * RoleRepositoryインターフェース
 * Roleエンティティのリポジトリインターフェース
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
