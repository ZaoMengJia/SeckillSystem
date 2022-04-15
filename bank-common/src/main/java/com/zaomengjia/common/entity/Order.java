package com.zaomengjia.common.entity;

import com.zaomengjia.common.constant.OrderStatus;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 订单类，用户抢购秒杀商品时产生订单一人只能抢购一份商品
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "user_order")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Order implements Serializable {
    /**
     * 订单号
     * 新增的话，需要注入id
     */
    @Id
//    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    private String id;

    /**
     * 购买订单用户
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 购买的理财产品id
     */
    @Column(name = "financial_product_id")
    private String financialProductId;

    /**
     * 订单来自的秒杀活动id
     */
    @Column(name = "seckill_activity_id")
    private String seckillActivityId;

    /**
     * 购买产品数量
     */
    @Column(name = "quantity")
    private long quantity;

    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;


    /**
     * 是否已经持久化保存了
     */
    @Transient
    private OrderStatus status = OrderStatus.NORMAL;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
