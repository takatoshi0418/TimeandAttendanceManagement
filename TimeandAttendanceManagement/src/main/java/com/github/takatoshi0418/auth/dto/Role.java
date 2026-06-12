package com.github.takatoshi0418.auth.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Roleエンティティクラス
 * ユーザの権限を表すエンティティクラス
 */
@Entity
@Table(name = "roles")
@Data
public class Role {

    /* 権限ID  */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 権限名  */
    @Column(unique = true, nullable = false)
    private String authority;

    /** 権限の表示名  */
    @Column(nullable = false)
    private String name;
}
