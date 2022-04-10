package com.zaomengjia.common.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 23:50
 */
@Entity(name = "admin_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@SQLDelete(sql = "update `admin_user` set `deleted`= 1 where `id` = ?")
@Where(clause = "`deleted` = 0")
@EnableJpaAuditing
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AdminUser {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "deleted")
    private int deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdminUser adminUser = (AdminUser) o;
        return id != null && Objects.equals(id, adminUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
