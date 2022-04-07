package com.zaomengjia.common.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Date;

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
@Data
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

}