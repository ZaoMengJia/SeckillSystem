package com.zaomengjia.common.entity;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 秒杀活动类，记录秒杀活动的id，名称，以及秒杀活动的商品明细
 * 商品明细由Detail类建表建立活动与对应理财产品
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "seckill_activity")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@SQLDelete(sql = "update `seckill_activity` set `deleted`= 1 where `id` = ?")
@Where(clause = "`deleted` = 0")
@EnableJpaAuditing
public class SeckillActivity {

    /**
     * 秒杀活动id
     */
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    /**
     * 秒杀活动名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 图像地址
     */
    @Column(name = "image")
    private String image;

    /**
     * 秒杀活动简介（详情等）
     */
    @Column(name = "detail")
    private String detail;

    /**
     * 秒杀活动开始时间
     */
    @Column(name = "activity_begin_time")
    private Date beginTime;

    /**
     * 秒杀活动结束时间
     */
    @Column(name = "activity_end_time")
    private Date endTime;

    /**
     * 秒杀活动创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 逻辑删除
     */
    @Column(name = "deleted")
    private int deleted;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SeckillActivity that = (SeckillActivity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
