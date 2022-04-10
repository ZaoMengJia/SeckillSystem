package com.zaomengjia.common.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:02
 */
@Entity(name = "weixin_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@SQLDelete(sql = "update `weixin_user` set `deleted`= 1 where `id` = ?")
@Where(clause = "`deleted` = 0")
@EnableJpaAuditing
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class WeixinUser {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "openid", nullable = false)
    private String openid;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "gender")
    private int gender;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "id_card")
    private String idCard;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "deleted")
    private int deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WeixinUser that = (WeixinUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
